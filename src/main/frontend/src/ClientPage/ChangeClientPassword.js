import {Button, Form, Modal} from "react-bootstrap";
import React, {useState} from "react";
import axios from "axios";

import {backLink, notifyError, notifySuccess} from "../Consts";
import PopUp from "../PopUp";
import {ToastContainer} from "react-toastify";

export function ChangeClientPassword({show, setShow}) {

    const [showPopUp, setShowPopUp] = useState(false)
    const handleShowPopUp = () => setShowPopUp(true)
    const handleClosePopUp = () => setShowPopUp(false)
    const [text, setText] = useState("")

    const [errors, setErrors] = useState({})
    const [form, setForm] = useState({})
    const setField = (field, value) => {
        setForm({
            ...form,
            [field]: value
        })
        if (!!errors[field]) setErrors({
            ...errors,
            [field]: null
        })
    }
    const findFormErrors = () => {

        const {oldPassword, newPassword} = form
        const newErrors = {}
        if (!oldPassword || oldPassword === "") newErrors.oldPassword = 'Polje ne sme da bude prazno!'
        if (!newPassword || newPassword === "") newErrors.newPassword = 'Polje ne sme da bude prazno!'
        console.log(newErrors)
        return newErrors
    }

    const handlePasswordChange = e => {
        e.preventDefault()
        // get our new errors
        const newErrors = findFormErrors()
        // Conditional logic:
        if (Object.keys(newErrors).length > 0) {
            // We got errors!
            setErrors(newErrors)
        } else {
            const {oldPassword, newPassword} = form
            var dto = {
                oldPassword: oldPassword,
                newPassword: newPassword
            }
            console.log(dto)
            axios
            .post(backLink + "/user/changePassword", dto, { headers: {"Content-Type": "application/json"} })
            .then( res => {
                localStorage.setItem('token', res.data)
                notifySuccess("Uspešno ste promenili šifru")
                handleShowPopUp()
            })
            .catch(function (error) {
                notifyError(error.response.data)
                handleShowPopUp()
            }); 
        }
    }

    const handleClose = () => setShow(false);

    return (
        <>
            <Modal show={show} onHide={() => setShow(false)} className="p-5 mt-5">
                <Form>
                    <Modal.Header closeButton>
                        <Modal.Title>Promena lozinke</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <Form.Group className="mb-3 m-2" controlId="oldPassword">
                            <Form.Label>Stara lozinka</Form.Label>
                            <Form.Control type="password"
                                          onChange={e => setField('oldPassword', e.target.value)}
                                          isInvalid={!!errors.oldPassword}/>
                            <Form.Control.Feedback type='invalid'>
                                {errors.oldPassword}
                            </Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group className="mb-3 m-2" controlId="newPassword">
                            <Form.Label>Nova lozinka</Form.Label>
                            <Form.Control type="password"
                                          onChange={e => setField('newPassword', e.target.value)}
                                          isInvalid={!!errors.newPassword}/>
                            <Form.Control.Feedback type='invalid'>
                                {errors.newPassword}
                            </Form.Control.Feedback>
                        </Form.Group>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={handleClose}>
                            Otkazi
                        </Button>
                        <Button variant="primary" onClick={handlePasswordChange}>
                            Izmeni
                        </Button>
                    </Modal.Footer>
                </Form>

            </Modal>
            <ToastContainer
                position="top-center"
                autoClose={5000}
                hideProgressBar={false}
                newestOnTop={false}
                closeOnClick
                rtl={false}
                pauseOnFocusLoss
                draggable
                pauseOnHover
                theme={"colored"}
            />
        </>)
}
