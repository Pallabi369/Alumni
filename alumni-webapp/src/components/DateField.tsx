import {FieldType} from "./types";
import { DateTime } from "luxon";
import {InfiniteIcon} from "./InfiniteIcon";

function DateField(props: FieldType) {
    const sapInfiniteDate = '9999-12-31';

    const date:DateTime = DateTime.fromISO(props.value);

    const renderAsInfinite = () => (<InfiniteIcon className="w-4 h-4 mr-1" />)
    const renderAsDate = () => ( <>
        <CalendarIcon  className="w-4 h-4 mr-1 text-gray-500" />
        {date.setLocale('en').toLocaleString(DateTime.DATE_MED)}
    </>)


    return (
        <>
            <div className="sm:col-span-1">
                <dt className="text-sm font-medium font-semibold text-blue-700">{props.label}</dt>
                <dd className="mt-1 text-sm text-gray-800 flex">{ props.value === sapInfiniteDate ? renderAsInfinite() : renderAsDate() }</dd>
            </div>
        </>
    )
}

export default DateField;


function CalendarIcon(props:any) {
    return (
        <svg xmlns="http://www.w3.org/2000/svg" className={props.className} viewBox="0 0 512 512"><title>Calendar</title>
            <rect fill="none" stroke="currentColor" strokeLinejoin="round" strokeWidth="32" x="48" y="80" width="416"
                  height="384" rx="48"/>
            <circle cx="296" cy="232" r="24"/>
            <circle cx="376" cy="232" r="24"/>
            <circle cx="296" cy="312" r="24"/>
            <circle cx="376" cy="312" r="24"/>
            <circle cx="136" cy="312" r="24"/>
            <circle cx="216" cy="312" r="24"/>
            <circle cx="136" cy="392" r="24"/>
            <circle cx="216" cy="392" r="24"/>
            <circle cx="296" cy="392" r="24"/>
            <path fill="none" stroke="currentColor" strokeLinejoin="round" strokeWidth="32" strokeLinecap="round"
                  d="M128 48v32M384 48v32"/>
            <path fill="none" stroke="currentColor" strokeLinecap="round" strokeWidth="32" d="M464 160H48"/>
        </svg>
    )
}



