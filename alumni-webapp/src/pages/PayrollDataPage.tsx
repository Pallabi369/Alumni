import {useTranslation} from "react-i18next";
import React, {useState} from "react";
import {PageHeader} from "../components/PageHeader";
import {Outlet, useNavigate, useParams} from "react-router-dom";
import {PayrollResult} from "../api";
import {useQuery} from "react-query";
import {getPayrollResults} from "../services/employees";
import {cacheableQueryKey} from "../services/services";
import {linkTo, PAYROLL_DATA_PAGE} from "../routes";


export function PayrollDataPage() {
  const navigate = useNavigate();
  const keyPrefix = 'service.payrollData';
  const {t} = useTranslation("", {keyPrefix});
  const {seqId} = useParams();

  const {data} = useQuery(cacheableQueryKey('payrollResults'),
      getPayrollResults(), {retry: 1, staleTime: Infinity, suspense: true});
  const payrollResults: Array<PayrollResult> = (data && data.data && data.data.payrollResults) || [];

  const [selectedYear, setSelectedYear] = useState<string>();
  const [selectedPayrollResult, setSelectedPayrollResult] = useState<PayrollResult>();
  const [tableView, setTableView] = useState<boolean>(payrollResults.length > 24);

  function navToDetails(payrollResult: PayrollResult) {
    navigate(linkTo([PAYROLL_DATA_PAGE, payrollResult.sequence].join("/")));
  }

  const backHref = () => {
    return seqId ? linkTo(PAYROLL_DATA_PAGE) : linkTo("/");
  }

  return (
    <div className="py-6 lg:py-12 px-4 sm:px-6 lg:px-8 max-w-7xl mx-auto">
      <PageHeader title={t('title')} subtitle={t('subtitle')} backHref={backHref}/>
      <Outlet context={{
        navToDetails,
        selectedYear,
        setSelectedYear,
        selectedPayrollResult,
        setSelectedPayrollResult,
        keyPrefix,
        tableView,
        setTableView,
        payrollResults
      }}/>
    </div>
  )
}
