import React, {useState} from 'react'
import {Button, Form, Modal} from "react-bootstrap";
import {Info} from "../Info";
import axios from "axios";
import Collapse from "react-bootstrap/Collapse";
import {backLink, notifyError, notifySuccess} from "../Consts";

export default function DeleteUserPopUp({user, showDelete, handleClose, type}) {
    const [deleteReason, setReason] = useState("")
    function handleDeleteAccount() {
        if (type === "CLIENT") {
            console.log(user.id)
            console.log(deleteReason.toString())
            axios.post(backLink+"/deletionRequests/client/" + user.id.toString(), deleteReason.toString()).then(
                res => {
                    notifySuccess(res.data)
                    handleClose();
                }
            )
        }
        else if (type === "FISHING_INSTRUCTOR") {
            axios
            .delete(backLink+"/deletionRequests/fishingInstructor/" + user.id.toString(), {
                headers: {"Content-Type": "text/plain"},
                data: deleteReason,
            })
            .then(res => {
                notifySuccess(res.data)
                handleClose();
                })
                .catch(function (error) {
                    notifyError(error.response.data)
                });
        }

    }

    return (
        <Modal show={showDelete} onHide={handleClose} size="medium" className="mt-lg-5">
            <Modal.Header closeButton>
                <Modal.Title>Brisanje naloga korisnika</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Info text={"Da li ste sigurni da zelite da obrisete nalog?"}/>
                <Form.Group className="mb-3" controlId="formPhoneNumber">
                    <Form.Group className="mb-3 m-2 hidden" controlId="form">
                        <Form.Label>Obrazloženje za brisanje naloga</Form.Label>
                        <Form.Control as="textarea" rows={2}
                                      onChange={e => setReason(e.target.value)}/>
                    </Form.Group>
                </Form.Group>
            </Modal.Body>
            <Modal.Footer>
                <Button className="ms-auto" variant="btn btn-outline-danger" onClick={handleDeleteAccount}>
                    DA
                </Button>
                <Button variant="btn btn-outline-success" onClick={handleClose}>
                    NE
                </Button>
            </Modal.Footer>
        </Modal>
    )
}
