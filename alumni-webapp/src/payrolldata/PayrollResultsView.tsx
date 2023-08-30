import {PayrollResult} from "../api";
import {PayrollYearGroup} from "./PayrollYearGroup";
import _ from "lodash";
import {Dictionary} from "../components/types";
import {DateTime} from "luxon";
import PayrollCriteria from "../payrolldata/PayrollCriteriaPanel";
import React from "react";
import {usePayrollData} from "./hooks";
import {PayrollTable} from "./PayrollTable";
import {Switch, Transition} from '@headlessui/react'
import {ViewGridIcon, ViewListIcon} from "@heroicons/react/outline";
import {useTranslation} from "react-i18next";

const distinctPeriods = (payrollResults: PayrollResult[]) => {
  // @ts-ignore
  return [...payrollResults.reduce((acc, current) => {
    if (!acc.has(current.forPeriod!) ||
          isSeqNewerThan(current.sequence!, acc.get(current.forPeriod!)!.sequence!)) {
      acc.set(current.forPeriod!, current);
    }
    return acc;
  }, new Map<string, PayrollResult>()).values()];
}

const isSeqNewerThan = (seq1: string, seq2: string) => {
  const seq1number = parseInt(seq1);
  const seq2number = parseInt(seq2);

  if (Number.isNaN(seq1number) || Number.isNaN(seq2number)) {
    return seq1 > seq2;
  }
  return seq1number > seq2number;
}

export function PayrollResultsView() {
  const {t: root_t} = useTranslation("");
  const {
    navToDetails, selectedYear, setSelectedYear, selectedPayrollResult,
    setSelectedPayrollResult, keyPrefix, tableView, setTableView, payrollResults
  } = usePayrollData()

  const parseDate = (value?: string) => (value && DateTime.fromISO(value)) || undefined;
  const payrolls = distinctPeriods(payrollResults);
  const payrollPerYear: Dictionary<PayrollResult> = _.groupBy(payrolls, (it:PayrollResult) => parseDate(it.forPeriod)?.year);
  const sortedYearPeriods = () => Object.keys(payrollPerYear).reverse();

  const clearFilter = () => {
    setSelectedYear(undefined);
    setSelectedPayrollResult(undefined)
  }

  return payrolls.length > 0 ? (
    <>
      <div className="flex w-full justify-between items-center flex-col sm:flex-row space-y-4 sm:space-y-0">
        <PayrollCriteria
          selectedYear={selectedYear} setSelectedYear={setSelectedYear}
          selectedPayrollResult={selectedPayrollResult} setSelectedPayrollResult={navToDetails}

          payrollPerYear={payrollPerYear}
          clearCriteria={clearFilter}
        />
        <Switch
          checked={tableView}
          onChange={(v) => setTableView(v)}
          className={`relative inline-flex items-center h-9 w-16 rounded-lg bg-gray-200 text-gray-400`}
        >
          <div className="w-full flex flex-row justify-evenly">
            <ViewGridIcon className="w-4 z-20"/>
            <ViewListIcon className="w-4 z-20"/>
          </div>
          <span
            className={`${tableView ? 'translate-x-8' : 'translate-x-1'} absolute inline-block w-7 h-7 transform transition ease-in-out bg-white rounded-md`}/>

        </Switch>
      </div>

      <div className="pt-8">
        {
          !tableView && sortedYearPeriods().map((yearPayroll, index) => {
            return (
              <Transition
                appear={true}
                show={!selectedYear || selectedYear === yearPayroll}
                className="pt-16 first:pt-0"
                enter="transition ease-in-out duration-100"
                enterFrom="opacity-0 scale-95"
                enterTo="opacity-100 scale-100"
                leave="transition ease-in-out duration-100"
                leaveFrom="opacity-100 scale-100"
                leaveTo="opacity-0 scale-95"
                key={index}
              >
                  <PayrollYearGroup key={index} keyPrefix={keyPrefix}
                                    year={yearPayroll}
                                    payrollResults={payrollPerYear[yearPayroll]}
                                    itemClicked={(r: PayrollResult) => navToDetails(r)}
                  />
              </Transition>
            )
          })
        }
        {tableView &&
          <Transition
            appear={true}
            show={true}
            enter="transition-opacity duration-500"
            enterFrom="opacity-0"
            enterTo="opacity-100"
            leave="transition-opacity duration-500"
            leaveFrom="opacity-100"
            leaveTo="opacity-0"
          >
            <PayrollTable
              payrollPerYear={payrollPerYear}
              selectedYear={selectedYear}
              keyPrefix={keyPrefix}
              navToDetails={(r: PayrollResult) => navToDetails(r)}
            />
          </Transition>
        }
      </div>
    </>
  ) : (
    <div>{root_t('error.payrollResultsEmpty')}</div>
  )
}
