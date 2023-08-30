import {GroupSet} from "../components/GroupSet";
import GenderField from "../components/GenderField";
import ChildrenNumberField from "../components/ChildrenNumberField";
import DateField from "../components/DateField";
import {PersonalInformationGroups} from "../api";

export default function PersonalInformationView(props: {keyPrefix: string, data?: PersonalInformationGroups}) {

  const renderer = {
      'genderKey': GenderField,
      'nrOfChildren': ChildrenNumberField,
      'birthDate': DateField,
      'startDate': DateField,
      'endDate': DateField,
      'changedAt': DateField
  }

  return (
          <GroupSet
              keyPrefix={props.keyPrefix}
              groups={props.data}
              renderer={renderer}
          />
  )
}
