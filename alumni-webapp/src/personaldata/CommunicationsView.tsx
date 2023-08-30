import {TableSet} from "../components/TableSet";
import {Communication} from "../api";


export default function CommunicationsView(props: {keyPrefix: string, data?: Array<Communication>}) {
    const coms = props.data || [];

    return (
        <TableSet
            keyPrefix={props.keyPrefix + '.group.communication'}
            elements={coms}
            columnOrder={['type', 'communicationId']}
        />
    )
}
