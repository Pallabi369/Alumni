import {useParams} from "react-router-dom";
import _ from "lodash";
import {PayslipTile} from "./PayslipTile";
import Group from "../components/Group";
import {TableSet} from "../components/TableSet";
import {DateTime} from "luxon";
import DateField from "../components/DateField";
import {FieldType} from "../components/types";
import React from "react";
import {usePayrollData} from "./hooks";
import {animateScroll} from "react-scroll";
import {useTranslation} from "react-i18next";
import HideableValue from "../components/HideableValue";
import HideableValueField from "../components/HideableValueField";

export function PayrollDetailsView() {
  const { t: root_t } = useTranslation("");
  animateScroll.scrollToTop();
  const {keyPrefix, payrollResults} = usePayrollData();
  const parseDate = (value: string | undefined) => (value && DateTime.fromISO(value)) || undefined;
  const params = useParams();
  const result = payrollResults.find(pr => pr.sequence === params.seqId);

  const renderer = {
    'startPayroll': DateField,
    'endPayroll': DateField,
    'payDate': DateField,
    'inPeriod': DatePeriodField,
    'forPeriod': DatePeriodField,
    'amountPaid': HideableValueField
  }

  return result ? (
    <div className='flex flex-col space-y-8'>
      <div className='relative w-80'>
        <PayslipTile keyPrefix={keyPrefix}
                     startPayroll={parseDate(result.startPayroll)}
                     payDate={parseDate(result.payDate)}
                     paid={`${result.amountPaid} ${result.currency}`}
                     sequence={result.sequence}
                     showDownload={true}
                     onTileClicked={()=>{}}
        />
      </div>
      <dl className="grid grid-cols-1 gap-x-4 gap-y-4 sm:grid-cols-3 grow">
        <Group keyPrefix={keyPrefix + '.group.general'}
               data={_.omit(result, ['payrollResultsDetails','cumulativePayrollResults', 'bankTransfers'])}
               renderer={renderer}/>
      </dl>
      <div className='sm:col-span-3 pt-8'>
        <TableSet keyPrefix={keyPrefix+ '.group.bankTransfers'} elements={result.bankTransfers || []}
                  columnOrder={['wageType', 'hrPayrollAmount', 'currency', 'transferDate']}
                  renderer={{'hrPayrollAmount': HideableValue}}
        />
      </div>
    </div>) : (
      <div>{root_t('error.payrollDetailsNotFound')}</div>
    );
}

const DatePeriodField = (props: FieldType) => {
  const date: DateTime = DateTime.fromISO(props.value);
  const value = date.toLocaleString({month: 'long', year: 'numeric'});

  return (
    <div className="sm:col-span-1">
      <dt className="text-sm font-semibold text-blue-700">{props.label}</dt>
      <dd className="mt-1 text-sm text-gray-800 flex">{value}</dd>
    </div>
  )
}
