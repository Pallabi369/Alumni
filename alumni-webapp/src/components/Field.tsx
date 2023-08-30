import {FieldType} from "./types";

function Field(props: FieldType) {
  return (
    <>
      <div className="sm:col-span-1">
        <dt className="text-sm font-semibold text-blue-700">{props.label}</dt>
        <dd className="mt-1 text-sm text-gray-800">{props.value}</dd>
      </div>
    </>
  )
}

export default Field;
