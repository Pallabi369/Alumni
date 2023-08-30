import {Dictionary} from "../components/types";
import {PayrollResult} from "../api";
import {DateTime} from "luxon";
import {XCircleIcon} from "@heroicons/react/outline";
import {Listbox, Transition} from "@headlessui/react";
import {CheckIcon, SelectorIcon} from "@heroicons/react/solid";
import {Fragment} from "react";
import {useTranslation} from "react-i18next";


type PayrollCriteriaProps = {
    selectedYear: any,
    setSelectedYear: any,
    selectedPayrollResult: any,
    setSelectedPayrollResult: any,
    payrollPerYear: Dictionary<PayrollResult>,
    clearCriteria: any
}
export default function PayrollCriteria(props: PayrollCriteriaProps) {

    const { t } = useTranslation("", { keyPrefix : 'common' });


    function getYearOptions(): Array<CriteriaOption> {
        return Object.keys(props.payrollPerYear).reverse().map(value => {
            return { label: value, value: value } as CriteriaOption
        });
    }

    function getMonthLabel(value: PayrollResult) {
        if (value.payDate != null) {
            const date = DateTime.fromISO(value.payDate);
            return date.toLocaleString({month: 'long'})
        }
        return t('unknown');
    }

    function getMonthOptions(): Array<CriteriaOption> {
        if (props.selectedYear && props.payrollPerYear[props.selectedYear]) {
            return props.payrollPerYear[props.selectedYear]
                .map((payrollResult: PayrollResult) => {
                    return { label: getMonthLabel(payrollResult), value: payrollResult }
                })
        }
        return [];
    }

    function yearLabel(value: string) {
        return value ? value : t('date.year');
    }

    function monthLabel(value:PayrollResult) {
        return value ? getMonthLabel(value) : t('date.month')
    }

    return (
        <div className='flex items-center space-x-4'>
            <div className="z-30 w-36">
                <CriteriaFilter selected={props.selectedYear} setSelected={props.setSelectedYear} options={getYearOptions()} disabled={false} labelOf={yearLabel}/>
            </div>
            <div className="z-30 w-36">
                <CriteriaFilter selected={props.selectedPayrollResult} setSelected={props.setSelectedPayrollResult} options={getMonthOptions()} disabled={!props.selectedYear} labelOf={monthLabel}/>
            </div>
            {props.selectedYear && (
                <div className='w-8 text-orange-400 cursor-pointer' onClick={() => props.clearCriteria()}>
                    <XCircleIcon/>
                </div>
            )}
        </div>
    )
}


type CriteriaOption= {label: string, value: any}
function CriteriaFilter(props: {selected:any, setSelected: any, options: Array<CriteriaOption>, disabled: boolean, labelOf: any}) {
    return (
        <div className='relative'>
            <Listbox value={props.selected} onChange={props.setSelected} disabled={props.disabled}>
                <Listbox.Button className={`relative w-full py-2 pl-3 pr-10 text-left ${props.disabled ? 'bg-gray-50 text-gray-400' : 'bg-gray-200'} rounded-lg cursor-pointer focus:outline-none focus-visible:ring-2 focus-visible:ring-opacity-75 focus-visible:ring-white focus-visible:ring-offset-orange-300 focus-visible:ring-offset-2 focus-visible:border-indigo-500 sm:text-sm`}>
                    <span className="block truncate">{props.labelOf(props.selected)}</span>
                    <span className="absolute inset-y-0 right-0 flex items-center pr-2 pointer-events-none"> <SelectorIcon className="w-5 h-5 text-gray-400" aria-hidden="true" /> </span>
                </Listbox.Button>
                <Transition
                    as={Fragment}
                    leave="transition ease-in duration-100"
                    leaveFrom="opacity-100"
                    leaveTo="opacity-0"
                >
                    <Listbox.Options className="absolute z-20 w-full py-1 mt-1 overflow-auto text-base bg-white rounded-md shadow-lg max-h-60 ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm">
                        {props.options.map((option, idx) => (
                            <Listbox.Option
                                key={idx}
                                className={({ active }) => `cursor-default select-none relative py-2 pl-10 pr-4 ${active ? 'text-gray-900 bg-gray-100' : 'text-gray-900'}` }
                                value={option.value}
                            >
                                {({ selected }) => (
                                    <>
                                        <span className={`block truncate ${ selected ? 'font-medium' : 'font-normal' }`} > {option.label} </span>
                                        {selected ? (<span className="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-600"><CheckIcon className="w-5 h-5" aria-hidden="true" /></span>) : null}
                                    </>
                                )}
                            </Listbox.Option>
                        ))}
                    </Listbox.Options>
                </Transition>
            </Listbox>
        </div>
    )
}

