import {decode} from "jsonwebtoken";
const { authority, clientId, clientSecret, environment, scopes, tenantId } = Cypress.env();

export const login = ({username, password}) => {
  return cy.visit("").request({
    url: authority,
    method: "POST",
    body: {
      client_id: clientId,
      client_secret: clientSecret,
      username,
      password,
      grant_type: 'password',
      // offline_access - to receive refreshToken
      scope: ["openid", "profile", "offline_access"].concat(scopes).join(" "),
      // id_token - to receive IdToken.
      response_type: "token id_token"
    },
    form: true
  }).then(response => {
    injectTokens(response.body);
  });
};

const injectTokens = (tokenResponse) => {
  const idToken = decode(tokenResponse.id_token);

  const localAccountId = idToken.oid || idToken.sid || idToken.sub // sub
  const policy = idToken.tfp;
  const realm = "";
  const homeAccountId = `${localAccountId}-${policy}.${tenantId}`;
  const username = idToken.preferred_username || "";
  const name = idToken.name || "";

  const accountKey = `${homeAccountId}-${environment}-${realm}`;
  const accountEntity = {
    authorityType: "MSSTS",
    clientInfo: "",
    homeAccountId,
    environment,
    realm,
    localAccountId,
    username,
    name,
    idTokenClaims: idToken
  };

  const idTokenKey = `${homeAccountId}-${environment}-idtoken-${clientId}-${realm}---`;
  const idTokenEntity = {
    credentialType: "IdToken",
    homeAccountId,
    environment,
    clientId,
    secret: tokenResponse.id_token,
    realm,
  };

  const accessTokenKey = `${homeAccountId}-${environment}-accesstoken-${clientId}-${realm}-${scopes.join(" ")}--`;
  const now = Math.floor(Date.now() / 1000);
  const accessTokenEntity = {
    homeAccountId,
    credentialType: "AccessToken",
    secret: tokenResponse.access_token,
    cachedAt: now.toString(),
    expiresOn: (now + 60 * 1000).toString(),
    extendedExpiresOn: (now + 60 * 1000).toString(),
    environment,
    clientId,
    realm,
    target: scopes.map((s) => s.toLowerCase()).join(" "),
  };

  const refreshTokenKey =  `${homeAccountId}-${environment}-refreshtoken-${clientId}-${realm}---`;
  const refreshTokenEntity = {
    clientId,
    credentialType: "RefreshToken",
    environment,
    homeAccountId,
    secret: tokenResponse.refresh_token
  };

  sessionStorage.setItem(accountKey, JSON.stringify(accountEntity));
  sessionStorage.setItem(idTokenKey, JSON.stringify(idTokenEntity));
  sessionStorage.setItem(accessTokenKey, JSON.stringify(accessTokenEntity));
  sessionStorage.setItem(refreshTokenKey, JSON.stringify(refreshTokenEntity));
  sessionStorage.setItem("msal." + clientId + ".active-account", localAccountId);
};

export const appendZalarisId = (zalarisId: string) => {
  for (let i = 0; i < sessionStorage.length; i++) {
    const key = sessionStorage.key(i);
    try {
      const json = JSON.parse(sessionStorage.getItem(key)) as
        { authorityType: string, idTokenClaims: { extension_ZalarisID: string} };
      if (json.authorityType === 'MSSTS') {
        json.idTokenClaims = {...json.idTokenClaims, "extension_ZalarisID": zalarisId};
        sessionStorage.setItem(key, JSON.stringify(json));
        return;
      }
    } catch (_) {}
  }
}

export const deleteClaimSsid = () => {
  for (let i = 0; i < sessionStorage.length; i++) {
    const key = sessionStorage.key(i);
    try {
      const json = JSON.parse(sessionStorage.getItem(key)) as
        { authorityType: string, idTokenClaims: { extension_ssid: string} };
      if (json.authorityType === 'MSSTS') {
        const tokenClaims = json.idTokenClaims;
        expect(!!tokenClaims.extension_ssid).to.true;
        delete tokenClaims["extension_ssid"];
        expect(!!tokenClaims.extension_ssid).to.false;
        sessionStorage.setItem(key, JSON.stringify(json));
        return true;
      }
    } catch (_) {}
  }
  return false;
}


