import {getPayslip} from "../services/employees";
import {DateTime} from "luxon";

export function PayslipDownloadButton(props: { sequenceId: string, zalarisId: string, children: JSX.Element  }) {

    function downloadPayslip() {
        getPayslip(props.sequenceId)().then(value => {
            createLinkOnTheFlyAndClickIt(value.data as Blob, props.sequenceId, props.zalarisId);
        })
        return false;
    }

    function createLinkOnTheFlyAndClickIt(data: Blob, sequenceId: string, zalarisId: string) {
        const link = document.createElement('a');
        link.href = window.URL.createObjectURL(data);
        link.download = `payslip-${zalarisId}-${sequenceId}-${DateTime.now().toMillis()}.pdf`;
        link.click();
    }

    return (
        <span onClick={() => downloadPayslip()}>
            {props.children}
        </span>
    )
}
