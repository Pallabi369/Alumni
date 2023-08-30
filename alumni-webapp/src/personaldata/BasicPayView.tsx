import {GroupSet} from "../components/GroupSet";
import {TableSet} from "../components/TableSet";
import DateField from "../components/DateField";
import {AdditionalPaymentDeduction, BasicPayGroups, RecurringPaymentDeduction} from "../api";
import HideableValueField from "../components/HideableValueField";
import React from "react";
import HideableValue from "../components/HideableValue";

export default function BasicPayView(props: {keyPrefix: string, data?:BasicPayGroups, recurringPaymentDeductions?: Array<RecurringPaymentDeduction>, additionalPaymentDeductions?: Array<AdditionalPaymentDeduction>}) {

    const model = props.data || {};
    const { payWages = [] } = model;
    const { recurringPaymentDeductions = [], additionalPaymentDeductions = [] } = props;

    const groups = {
        personnel: model.personnel,
        payscale: model.payscale
    }

    const renderer = {
        'startDate': DateField,
        'endDate': DateField,
        'changedAt': DateField,
        'annualSalary': HideableValueField
    }

    return (
      <>
          <GroupSet
              keyPrefix={props.keyPrefix}
              groups={groups}
              renderer={renderer}
          />

          <TableSet
              keyPrefix={props.keyPrefix + '.group.payWages'}
              elements={payWages}
              columnOrder={['type', 'amount']}
              renderer={{'amount': HideableValue}}
          />

          <TableSet
              keyPrefix={props.keyPrefix + '.group.recurringPaymentDeductions'}
              elements={recurringPaymentDeductions}
              columnOrder={['type', 'amount']}
              renderer={{'amount': HideableValue}}
          />

          <TableSet
              keyPrefix={props.keyPrefix + '.group.additionalPaymentDeductions'}
              elements={additionalPaymentDeductions}
              columnOrder={['type', 'amount']}
              renderer={{'amount': HideableValue}}
          />
      </>
    )
}



