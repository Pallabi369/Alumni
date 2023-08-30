import {getPayslip} from "../services/employees";

export function PayslipOpenButton(props: { sequenceId: string, zalarisId: string, children: JSX.Element}) {
    function downloadPayslip() {
        getPayslip(props.sequenceId)().then(value => {
            createLinkOnTheFlyAndClickIt(value.data as Blob, props.sequenceId, props.zalarisId);
        })
        return false
    }

    function createLinkOnTheFlyAndClickIt(data: Blob, sequenceId: string, zalarisId: string) {

        // const filename = `payslip-${zalarisId}-${sequenceId}-${DateTime.now().toMillis()}.pdf`;

        // IE
        /*if (window.navigator && window.navigator.msSaveOrOpenBlob) {
            window.navigator.msSaveOrOpenBlob(data, filename);
            return;
        }*/

        // Chrome, FF
        const fileUrl = URL.createObjectURL(data);
        const w = window.open(fileUrl, '_blank');
        w && w.focus();

    }

    return (
        <span onClick={() => downloadPayslip()}>
            {props.children}
        </span>
    )
}
