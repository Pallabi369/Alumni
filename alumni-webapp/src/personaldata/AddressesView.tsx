import Field from "../components/Field";
import {useTranslation} from "react-i18next";
import {Address} from "../api";
import {FieldType} from "../components/types";
import {ReactNode} from "react";

export default function AddressesView(props: { keyPrefix: string, data?: Array<Address> }) {

  const {t} = useTranslation("", {keyPrefix: props.keyPrefix});
  const addresses = props.data || [];


  function fieldsOf(data: Address, renderer?: { [key: string]: ReactNode }) {
    const fields: FieldType[] = [];
    if (data) {
      let k: keyof Address;
      for (k in data) {
        const value = data[k];
        const component: ReactNode = renderer?.[k] || Field
        if (value) {
          // @ts-ignore
          fields.push({label: t(k), value, component})
        }
      }
    }
    return fields;
  }

  return (
    <>
      {addresses.map((address, index) => (
        <dl key={index} className="grid grid-cols-1 gap-x-4 gap-y-4 sm:grid-cols-3 mb-8">
          <div className="pt-10 first:pt-0 pb-2 border-b border-gray-200 sm:col-span-3">
            <h3 className="leading-6 text-gray-700">{address.addressType}</h3>
          </div>

          {fieldsOf(address).map((field: any, index: number) => {
              return (
                <field.component key={index} {...field} />
              )
            }
          )}
        </dl>
      ))}
    </>
  )
}



