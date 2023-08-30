import {GroupSet} from "../components/GroupSet";
import {BankDetails} from "../api";

export default function BankDetailsView(props: {keyPrefix: string, data?: Array<BankDetails>}) {

    const bankDetails = props.data || [];

    return (
        <>
            {bankDetails.map((entry, index) => (
                <div className="pb-4" key={index}>
                    <GroupSet
                        keyPrefix={props.keyPrefix}
                        groups={ { "main": entry } }
                    />
                </div>


            ))}
        </>
    )
}



