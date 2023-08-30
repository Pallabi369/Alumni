import {GroupSet} from "../components/GroupSet";
import DateField from "../components/DateField";
import {OrganizationalAssignmentGroups} from "../api";

export default function OrgAssignmentView(props: {keyPrefix: string, data?: OrganizationalAssignmentGroups}) {

    const renderer = {
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
