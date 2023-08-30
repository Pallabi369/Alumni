import React, {Fragment, useEffect, useState} from "react";
import {PayslipDownloadButton} from "./PayslipDownloadButton";
import {Dictionary} from "../components/types";
import {PayrollResult} from "../api";
import {useAppStore} from "../store/appStore";
import {DateTime} from "luxon";
import {PayslipOpenButton} from "./PayslipOpenButton";
import {useTranslation} from "react-i18next";
import {ChevronUpIcon, DownloadIcon} from "@heroicons/react/solid";

import {Transition} from "@headlessui/react";
import Group from "../components/Group";
import _ from "lodash";
import HideableValue from "../components/HideableValue";
import HideableValueField from "../components/HideableValueField";
import DateField from "../components/DateField";
import {DownloadIndicator} from "./DownloadIndicator";

const colNumber = 6;

type PayrollTableType = {
    payrollPerYear: Dictionary<PayrollResult>,
    selectedYear: string,
    keyPrefix: string,
    navToDetails: any
}
export function PayrollTable(props:PayrollTableType) {

    const { employment: { zalarisId } = {}} = useAppStore()

    const sortedYearPeriods = () =>  Object.keys(props.payrollPerYear).reverse();

    if (!zalarisId) {
        throw new Error("No zalarisId selected");
    }

    return (
        <table className="min-w-full table-fixed">
            <thead className="bg-white">
                <PayrollTableHeader keyPrefix={props.keyPrefix} />
            </thead>
            <tbody className="bg-white">
            {sortedYearPeriods().map((yearPayroll, idx) => {
                return (!props.selectedYear || props.selectedYear === yearPayroll) && (
                <Fragment key={idx}>
                    <PayrollTableGroupRow yearPayroll={yearPayroll} />
                    {props.payrollPerYear[yearPayroll].map((result, rIdx) => (
                        <PayrollTableRow key={rIdx} keyPrefix={props.keyPrefix} result={result} navToDetails={props.navToDetails} zalarisId={zalarisId}/>
                    ))}
                </Fragment>
                )}
            )}
            </tbody>
        </table>
    )
}

function PayrollTableHeader(props: {keyPrefix: string}) {

    const { t } = useTranslation("", { keyPrefix: props.keyPrefix });

    return (
        <tr>
            <th scope="col" className="md:sticky top-0 z-20 bg-white pt-6 text-left text-sm font-normal text-blue-700 sm:pl-6 md:pl-0"></th>
            <th scope="col" className="md:sticky top-0 z-20 bg-white pt-6 px-3 text-left text-sm font-normal text-blue-700 hidden sm:table-cell">
                {t('table.period')}
            </th>
            <th scope="col" className="md:sticky top-0 z-20 bg-white pt-6 px-3 text-left text-sm font-normal text-blue-700 hidden sm:table-cell	">
                {t('table.earned')}
            </th>
            <th scope="col" className="md:sticky top-0 z-20 bg-white pt-6 px-3 text-left text-sm font-normal text-blue-700 hidden lg:table-cell	">
                {t('table.startDate')}
            </th>
            <th scope="col" className="md:sticky top-0 z-20 bg-white pt-6 px-3 text-left text-sm font-normal text-blue-700 hidden sm:table-cell	">
                {t('table.paidAt')}
            </th>
            <th scope="col" className="md:sticky top-0 z-20 bg-white pt-6 pl-3 pr-4 sm:pr-6"></th>
        </tr>

    )
}

function PayrollTableGroupRow(props: {yearPayroll: string}) {
    return (
        <tr className="border-b border-gray-300">
            <th
                colSpan={colNumber}
                scope="colgroup"
                className="md:sticky top-10 z-20 bg-white pt-4 pb-2 text-left text-sm font-normal text-blue-800"
            >
                {props.yearPayroll}
            </th>
        </tr>
    )
}

function PayrollTableRow(props:{keyPrefix: string, result: PayrollResult, navToDetails: Function, zalarisId: string}) {

    const [ showPreview, setShowPreview ] = useState<boolean>(false);
    const { t } = useTranslation("", { keyPrefix: props.keyPrefix });
    const [indicator, setIndicator] = useState(false);

    const parseDate = (value: string) => value && DateTime.fromISO(value).setLocale('en');
    const formatDate = (value: string | undefined, options: any) => value ? parseDate(value).toLocaleString(options) : "Unknown";

    useEffect(() => {
      if (indicator) {
        setTimeout(() => setIndicator(false), 1500);
      }
    }, [indicator]);

    return (

        <Fragment>
            <tr>
                <td className="cursor-pointer flex items-center justify-center h-14 w-8" >
                    <button onClick={() => setShowPreview(!showPreview)} >
                        <ChevronUpIcon className={`w-4 h-4 text-gray-800 transform transition-all ${showPreview ? 'rotate-0' : 'rotate-180'}`} />
                    </button>
                </td>
                <td className="whitespace-nowrap px-3 py-4 text-sm font-semibold text-blue-800 w-max cursor-pointer"
                    onClick={() => props.navToDetails(props.result)}>
                    { formatDate(props.result.startPayroll, { month: 'long', year: 'numeric' })}
                </td>
                <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-800"><HideableValue value={props.result.amountPaid||""}/> {props.result.currency}</td>
                <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-800 hidden lg:table-cell	">{ formatDate(props.result.startPayroll, { month: 'long', year: 'numeric', day: 'numeric' })}</td>
                <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-800 hidden sm:table-cell	">{ formatDate(props.result.payDate, { month: 'long', year: 'numeric', day: 'numeric' })}</td>
                <td className="relative p-3 text-blue-700 text-right text-sm font-medium">
                    <div className="flex flex-row justify-end space-x-2">
                        {props.result.sequence &&
                            <PayslipOpenButton sequenceId={props.result.sequence} zalarisId={props.zalarisId} >
                                <button className='text-blue-800 hidden md:block' onClick={() => setIndicator(true)}>{t('btn.previewPayslip')}</button>
                            </PayslipOpenButton>
                        }
                        <span className="text-gray-300 hidden md:block" aria-hidden="true">|</span>
                        {props.result.sequence &&
                            <PayslipDownloadButton sequenceId={props.result.sequence} zalarisId={props.zalarisId} >
                                <>
                                    <button className='text-blue-800 hidden md:block' onClick={() => setIndicator(true)}>{t('btn.downloadPayslip')}</button>
                                    <div className="block md:hidden bg-gray-300 rounded-full p-2">
                                        <DownloadIcon className="w-4 h-4 text-blue-800" />
                                    </div>
                                </>
                            </PayslipDownloadButton>
                        }
                        {(indicator && <DownloadIndicator />) || <div className={"w-8 h-8"} />}
                    </div>
                </td>
            </tr>
            <tr className="border-b border-gray-300">
                <Transition
                    as={Fragment}
                    show={showPreview}
                    enter="transition-all duration-100"
                    enterFrom="opacity-0 scale-95"
                    enterTo="opacity-100 scale-100"
                    leave="transition-opacity duration-150"
                    leaveFrom="opacity-100 scale-100"
                    leaveTo="opacity-0 scale-95"
                >
                    <td colSpan={colNumber} className="p-12 bg-blue-100">
                        <dl className="grid grid-cols-1 gap-x-4 gap-y-4 sm:grid-cols-3 grow">
                            <Group keyPrefix={'service.payrollData.group.general'}
                                   data={_.omit(props.result, ['payrollResultsDetails','cumulativePayrollResults', 'bankTransfers'])}
                                   renderer={{
                                       'amountPaid': HideableValueField,
                                       'startPayroll': DateField,
                                       'endPayroll': DateField,
                                       'payDate': DateField}}/>
                        </dl>
                    </td>
                </Transition>
            </tr>
        </Fragment>

    )
}
