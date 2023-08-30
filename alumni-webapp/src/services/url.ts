import URLParse from "url-parse";

const basePath = window.__RUNTIME_CONFIG__.BASE_PATH;

const buildPathname = (href: string = window.location.href) => {
  const { pathname } = new URLParse(href);
  const basePathname = new URLParse(basePath).pathname;
  if(pathname.startsWith(basePathname)) {
    return pathname.substring(basePathname.length);
  }
  return pathname;
}

const corporateId = (href: string = window.location.href) => {
  // "", "/", "/123", "/123/", "text/"
  const parts = buildPathname(href).split("/");
  const cid = (parts.length > 0 && parts[0]) || "";
  return cid.match("\\d+") ? cid : "";
}

const hasCorporateId = (href: string = window.location.href) => {
  return !!corporateId(href);
}

const isRedirect = (href: string =  window.location.href) => {
  const parts = buildPathname(href).split("/");
  return parts.length > 0 && parts[0].startsWith("redirect");
}

const navigateToBasePath = (corporateId: string = "") => {
  window.location.href = basePath + (corporateId && "/" + corporateId);
}

export {
  corporateId, hasCorporateId, isRedirect, navigateToBasePath
}
