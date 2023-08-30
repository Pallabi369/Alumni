import {
  Configuration,
  EmployeeApi,
  EmploymentHistoryModel,
  PayrollApi,
  PayrollResultsModel,
  PersonalDataModel,
} from "../api";
import {appStore} from "../store/appStore";
import {appInsights, AREA_PROPERTY, collectUserData, withAccessToken} from "./services";
import {SeverityLevel} from "@microsoft/applicationinsights-common";
import log from "loglevel";

const basePath = window.__RUNTIME_CONFIG__.BASE_PATH || undefined;
const employeeApi = new EmployeeApi(new Configuration({basePath}));
const payrollApi = new PayrollApi(new Configuration({basePath}));

export function getPersonalData() {
  return withAccessToken<PersonalDataModel>(
      config => employeeApi.getPersonalData(getZalarisId(), config));
}

export function getEmploymentHistory(zalarisId?: string) {
  return withAccessToken<EmploymentHistoryModel>(
    config => employeeApi.getEmploymentHistory(getSsid(false), zalarisId, config));
}

export function getPayrollResults() {
  return withAccessToken<PayrollResultsModel>(
      config => payrollApi.getPayrollResults(getZalarisId(), config));
}

export function getPayslip(sequenceId: string) {
  return withAccessToken<object>(
      config => {
        return payrollApi.getPayslip(getZalarisId(), sequenceId, {...config, responseType: 'blob'})
      });
}

const getZalarisId = () => {
  const { employment: { zalarisId} = {}} = appStore();
  if (!zalarisId) {
    appInsights.trackTrace({
        message: "No zalarisId selected", severityLevel: SeverityLevel.Critical },
      { ...collectUserData(), [AREA_PROPERTY]: "employees"});
    log.error("No zalarisId selected");
    throw new Error("No zalarisId selected");
  }
  return zalarisId;
}

const getSsid = (throwsError: boolean = true) => {
  const { ssid } = appStore();
  if (throwsError && !ssid) {
    appInsights.trackTrace({
        message: "No ssid selected", severityLevel: SeverityLevel.Critical },
      { ...collectUserData(), [AREA_PROPERTY]: "employees"});
    log.error("No ssid selected");
    throw new Error("No ssid selected");
  }
  return ssid;
}
