import {Modal} from "react-bootstrap";

export function MessagePopupModal(props) {
    if (props.show) {
        return (
            <>
                <Modal show={props.show} onHide={()=> props.setShow(false)}>
                    <Modal.Header closeButton>
                        <Modal.Title>{props.heading}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>{props.message}</Modal.Body>
                </Modal>
            </>
        );
    }
}