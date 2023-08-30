import {FieldType} from "./types";
import {ElementType} from "react";

import {useTranslation} from "react-i18next";


function GenderField(props: FieldType) {
    const { t: root_t } = useTranslation();

    const config : {[key: string]: {icon: ElementType, label: string}} = {
        "1": { icon: MaleGenderIcon, label: 'male' },
        "2": { icon: FemaleGenderIcon, label: 'female'},
        "A": { icon: TransGenderIcon, label: 'trans'},
        "3": { icon: UnknownGenderIcon, label: 'nonbinary'},
        "9": { icon: UnknownGenderIcon, label: 'undeclared'}
    }
    const GenderIcon:ElementType = config[props.value]?.icon || UnknownGenderIcon;
    const label = root_t(`common.gender.${config[props.value]?.label||'unknown'}`)

    return (
        <>
            <div className="sm:col-span-1">
                <dt className="text-sm font-medium font-semibold text-blue-700">{props.label}</dt>
                <dd className="mt-1 text-sm text-gray-800 flex">
                    <GenderIcon className="w-4 h-4" />
                    <span className="ml-1">{label}</span>
                </dd>
            </div>
        </>
    )
}

export default GenderField;

function MaleGenderIcon(props:any) {
    return (
        <svg xmlns="http://www.w3.org/2000/svg" className={props.className} viewBox="0 0 512 512"><title>Male</title>
            <circle cx="216" cy="296" r="152" fill="none" stroke="currentColor" strokeLinecap="round"
                    strokeLinejoin="round" strokeWidth="32"/>
            <path fill="none" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="32"
                  d="M448 160V64h-96M324 188L448 64"/>
        </svg>
    )
}

function FemaleGenderIcon(props:any) {
    return (
        <svg xmlns="http://www.w3.org/2000/svg" className={props.className} viewBox="0 0 512 512"><title>Female</title>
            <circle cx="256" cy="184" r="152" fill="none" stroke="currentColor" strokeLinecap="round"
                    strokeLinejoin="round" strokeWidth="32"/>
            <path fill="none" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="32"
                  d="M256 336v144M314 416H198"/>
        </svg>
    )
}

function TransGenderIcon(props:any) {
    return (
        <svg xmlns="http://www.w3.org/2000/svg" className={props.className} viewBox="0 0 512 512"><title>Transgender</title>
            <circle cx="256" cy="256" r="128" fill="none" stroke="currentColor" strokeLinecap="round"
                    strokeLinejoin="round" strokeWidth="32"/>
            <path fill="none" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="32"
                  d="M448 352l-96 96M176 80l-95.98 95.98M464 128V48h-80M48 128V48h80M464 48L346.5 165.5M48 48l117.49 117.49M464 464L346.65 346.37"/>
        </svg>
    )
}

function UnknownGenderIcon(props:any) {
    return (
        <svg xmlns="http://www.w3.org/2000/svg" className={props.className}  viewBox="0 0 512 512"><title>Male Female</title>
            <circle cx="216" cy="200" r="136" fill="none" stroke="currentColor" strokeLinecap="round"
                    strokeLinejoin="round" strokeWidth="32"/>
            <path fill="none" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="32"
                  d="M216 352v128M272 416H160M432 112V32h-80M335.28 128.72L432 32"/>
        </svg>
    )
}
