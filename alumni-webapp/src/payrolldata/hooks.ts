import {PayrollResult} from "../api";
import {useOutletContext} from "react-router-dom";

export type PayrollContextType = {
  navToDetails: (payrollResult: PayrollResult) => void,
  selectedYear: string,
  setSelectedYear: Function,
  selectedPayrollResult: PayrollResult,
  setSelectedPayrollResult: Function,
  keyPrefix: string,
  tableView: boolean,
  setTableView: Function,
  payrollResults: Array<PayrollResult>
};

export function usePayrollData() {
  return useOutletContext<PayrollContextType>();
}
