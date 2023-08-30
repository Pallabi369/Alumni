import {DateTime} from "luxon";
import {useTranslation} from "react-i18next";
import {PayslipDownloadButton} from "./PayslipDownloadButton";

import {useAppStore} from "../store/appStore";
import {getImageForDate} from "./monthImageLibrary";
import React, {useEffect, useState} from "react";
import HideableValue from "../components/HideableValue";
import {DownloadIndicator} from "./DownloadIndicator";

type PayslipTileType = {
    sequence?: string,
    startPayroll?: DateTime,
    payDate?: DateTime,
    paid: string,
    keyPrefix: string,
    showDownload: boolean,
    onTileClicked: Function
}
export function PayslipTile(props: PayslipTileType) {

    const { t } = useTranslation("", { keyPrefix: props.keyPrefix });
    const { employment: { zalarisId } = {}} = useAppStore();
    const [indicator, setIndicator] = useState(false);

    useEffect(() => {
        if (indicator) {
            setTimeout(() => setIndicator(false), 1500);
        }
    }, [indicator]);


    if (!zalarisId) {
        throw new Error("No zalarisId selected");
    }

    return (
        <>
            <div onClick={() => props.onTileClicked()} className="cursor-pointer aspect-w-3 aspect-h-2 rounded-lg relative bg-cover bg-no-repeat bg-center" style={{backgroundImage: `url(${getImageForDate(props.startPayroll)})` }}>
                <div className='flex items-end justify-start'>
                    <span className="text-blue-800 uppercase font-semibold rounded-md px-2 py-1 bg-white -mb-1 -ml-1">{formatMonthYear(props.startPayroll)}</span>
                </div>
            </div>
            <div className='mt-4 flex flex-row justify-between'>
                <div className='grow'>
                    <p className="block text-md font-medium text-blue-800 truncate">{t('tile.earned')}: <HideableValue value={props.paid}/></p>
                    <p className="block text-sm font-medium text-gray-500 pointer-events-none">{t('tile.paid')}: {formatDate(props.payDate)}</p>
                </div>
                <div>
                    {props.showDownload && props.sequence &&
                        <PayslipDownloadButton sequenceId={props.sequence} zalarisId={zalarisId} >
                            <button className='bg-gray-200 px-4 py-1 rounded-xl text-xs h-6 text-blue-800' onClick={() => setIndicator(true)}>{t('btn.downloadPayslip')}</button>
                        </PayslipDownloadButton>}
                </div>
                <div className={"w-2"} />
                {(indicator && <DownloadIndicator />) || <div className={"w-8 h-8"} />}
            </div>
        </>
    )
}


const formatMonthYear = (value:DateTime|undefined) =>  {
    if (!value) {
        return 'Unknown';
    }
    return value.setLocale('en').toLocaleString({ month: 'long', year: 'numeric' });
}

const formatDate = (value:DateTime|undefined) =>  {
    if (!value) {
        return 'Unknown';
    }
    return value.setLocale('en').toLocaleString();
}
