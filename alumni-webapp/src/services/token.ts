import {Configuration, TokenProviderApi} from "../api";
import {withAccessToken} from "./services";

const basePath = window.__RUNTIME_CONFIG__.BASE_PATH || undefined;
const api = new TokenProviderApi(new Configuration({basePath}));

export function sendCode(code: string) {
  return withAccessToken<void>(config => api.token(code, config))();
}
