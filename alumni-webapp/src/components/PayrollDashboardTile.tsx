import {useQuery} from "react-query";
import {cacheableQueryKey} from "../services/services";
import {getPayrollResults} from "../services/employees";
import {PayrollResult} from "../api";

import {Suspense} from "react";
import _ from "lodash";
import {BanIcon} from "@heroicons/react/outline";
import {useTranslation} from "react-i18next";
import HideableValue from "./HideableValue";


export function PayrollDashboardTile() {
    return (
        <div className="rounded-md bg-gray-300 h-24 md:h-48 p-2">
            <Suspense fallback={<PayrollLoadingView/>}>
                <PayrollDashboardTileDataLoader>
                    { data => (<PayrollDashboardTileView year={data.year} payrollSums={data.payrollSums} />) }
                </PayrollDashboardTileDataLoader>
            </Suspense>
        </div>
    )
}

const PayrollDashboardTileDataLoader = (props: {children: (data:any) => JSX.Element}) => {

    const {data} = useQuery(cacheableQueryKey('payrollResults'),
        getPayrollResults(), {retry: 1, staleTime: Infinity, suspense: true});
    const payrollResults: Array<PayrollResult> = (data && data.data && data.data.payrollResults) || [];

    const year = takeLastYear(payrollResults);
    if (!year) {
        return (<PayrollNoDataView/>)
    }
    const payrollSums = calculateSumsForYear(payrollResults, year);

    return (<>{props.children({year, payrollSums})}</>)

}

type PayrollSumData = {
    amount: number,
    currency: string
}
const PayrollDashboardTileView = (props: {year: string, payrollSums: Array<PayrollSumData>}) => {

    return (
        <div className="flex items-center justify-start h-full">
            <div className="p-8">
                <h3 className="text-blue-800 text-1xl md:text-lg lg:text-lg xl:text-lg ">Total net pay in {props.year}</h3>
                { props.payrollSums.map((s, index) =>
                    (
                        <h3 key={index} className="text-blue-800 text-2xl font-semibold text-5xl md:text-3xl lg:text-3xl xl:text-4xl lining-nums text-clip overflow-hidden">
                            <HideableValue value={s.amount} hideByDefault={true}/> {s.currency}
                        </h3>
                    )
                )}
            </div>
        </div>
    )
}

const PayrollNoDataView = () => {
    const {t} = useTranslation("", {keyPrefix: 'error'});
    return (
        <div className="flex justify-center items-center space-x-2 h-full w-full border-4 border-dashed border-gray-400 rounded-lg">
            <BanIcon className="w-6 h-6 text-gray-500"/>
            <span className="text-sm text-gray-500">{t('payrollResultsEmpty')}</span>
        </div>)
}

const PayrollLoadingView = () => {
    return (<div className="flex items-center justify-start h-full animate-pulse opacity-20">
        <div className="p-8 space-y-4 w-full">
            <h3 className="w-1/2 h-6 bg-blue-800 opacity-50 rounded-md">&nbsp;</h3>
            <h3 className="w-3/4 h-8 bg-blue-800 opacity-50 rounded-md"></h3>
        </div>
    </div>)
}

export const takeLastYear = ( payrollResults: Array<PayrollResult> ) => _.maxBy(payrollResults, (pr) => pr.startPayroll)?.startPayroll?.substring(0,4)
export const calculateSumsForYear = (payrollResults: Array<PayrollResult>, year:string): Array<PayrollSumData> => {
    return _.chain(payrollResults)
        .map(pr => {
            return {
                year: pr.startPayroll?.substring(0,4),
                month: pr.startPayroll?.substring(4,6),
                amount: pr.amountPaid ? parseFloat(pr.amountPaid) : 0,
                currency: pr.currency
            }
        })
        .sortBy('year')
        .groupBy('year')
        .get(year)
        .groupBy('currency')
        .map((vals, cur) => {
            function addFloats(a: number, b: number, precision: number) {
                var x = Math.pow(10, precision || 2);
                return (Math.round(a * x) + Math.round(b * x)) / x;
            }

            return {
                currency: cur,
                amount: vals.map(v=> v.amount).reduce((a,b)=> addFloats(a, b, 2) )
            } as PayrollSumData
        })
        .value()
}

