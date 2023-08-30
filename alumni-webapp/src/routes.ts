import {appStore} from "./store/appStore";

export const BASE = "";

export const PERSONAL_DATA_PAGE = 'personal-data';
export const PAYROLL_DATA_PAGE = 'payroll-data';

export const linkTo = (page: string) => {
  const {corporateId: cid} = appStore();
  const p = page === '/' ? '' : page;
  return ((cid && ["", cid]) || [cid]).concat([p]).join("/");
}
