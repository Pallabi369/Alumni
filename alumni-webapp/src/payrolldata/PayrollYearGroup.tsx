import {PayrollResult} from "../api";
import {PayslipTile} from "./PayslipTile";
import {DateTime} from "luxon";
import {useTranslation} from "react-i18next";

type PayrollYearGroupProps = {
    year: string,
    payrollResults: PayrollResult[],
    itemClicked: any,
    keyPrefix: string
}
export function PayrollYearGroup(props: PayrollYearGroupProps) {

    const { t } = useTranslation("", { keyPrefix: 'common' });
    const parseDate = (value: string|undefined) => (value && DateTime.fromISO(value)) || undefined;

    return (
        <>
            <div className="mb-6 border-b border-gray-200 sm:col-span-3 ">
                <h3 className="leading-6 text-blue-800 font-semibold">{t('date.year')} {props.year}</h3>
            </div>
            <ul className="grid gap-x-6 lg:gap-x-8  gap-y-12 grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 2xl:grid-cols-6 ">
                {props.payrollResults.map((result, index) => (
                    <li key={index} className="relative" >
                        <PayslipTile keyPrefix={props.keyPrefix}
                                     startPayroll={parseDate(result.startPayroll)}
                                     payDate={parseDate(result.payDate)}
                                     paid={`${result.amountPaid} ${result.currency}`}
                                     sequence={result.sequence}
                                     showDownload={false}
                                     onTileClicked={() => props.itemClicked(result)}
                        />
                    </li>
                ))}
            </ul>
        </>

    )
}
