import {msal} from "./services";

type Claims = {
  name?: string
  extension_ZalarisID?: string
  extension_ssid?: string
  tfp?: string
}

const claims = (name: keyof Claims, def: string = '') => {
  const { idTokenClaims = {}} = msal.getActiveAccount() || {};
  const claims = idTokenClaims as Claims;
  return (claims && claims[name]) || def;
}

const getClaimName = () => claims('name');

const getClaimZalarisId = () => claims('extension_ZalarisID');

const getClaimCorporateId = () => {
  const zalarisId = getClaimZalarisId();
  if (zalarisId) {
    const parts = zalarisId.split("-");
    if (parts.length === 2) {
      return parts[0];
    }
  }
  return undefined;
}
const getClaimSsid = () => claims('extension_ssid');

const verifyZalarisClaim = () : boolean => {
  switch(msal.getConfiguration().auth.authority) {
    case window.__RUNTIME_CONFIG__.MSAL_CORPORATE_AUTHORITY:
      return !!getClaimZalarisId();
    case window.__RUNTIME_CONFIG__.MSAL_PRIVATE_AUTHORITY:
      return !getClaimZalarisId();
    default:
      return true;
  }
}

export { getClaimName, getClaimZalarisId, getClaimCorporateId, getClaimSsid, verifyZalarisClaim };
