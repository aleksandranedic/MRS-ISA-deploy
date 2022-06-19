import React from 'react'
import {Button, Modal} from "react-bootstrap";
import {Info} from "./Info";

export default function PopUp({show, handleClose, text}) {
    return (
        <Modal show={show} onHide={handleClose} size="sm" className="mt-lg-5">
            <Modal.Header closeButton>
                <Modal.Title>Obavestenje</Modal.Title>
            </Modal.Header>
            <Modal.Body>

                <Info text={text}/>
            </Modal.Body>
            <Modal.Footer>
                <Button className="btn-default " onClick={handleClose}>OK</Button>
            </Modal.Footer>
        </Modal>
    )
}
