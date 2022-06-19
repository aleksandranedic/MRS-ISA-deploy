import {Badge, Button, Card, Form, Modal} from "react-bootstrap";
import React, {useState} from "react";
import image from "../images/document.png"
import {backLink, frontLink, notifyError, notifySuccess} from "../Consts";
import axios from "axios";
import {ToastContainer} from "react-toastify";

export function RegistrationRequestCard({props}) {
    const [show, setShow] = useState(false);
    const [response,setResponse]=useState("")
    


    function handleSubmit(typeOfResponse) {

        const dto = {
            fullName: props.firstName+" "+props.lastName,
            username: props.email,
            type: typeOfResponse,
            requestId: props.id,
            response: response
        }
        axios.post(backLink + "/vendorRegistration/validate", dto)
        .then(
            response => {
                if(response.data==="Odobravanje registracije je uspešno")
                    notifySuccess(response.data)
                else
                    notifyError(response.data)
                setShow(false);
                setTimeout(function () {
                    window.location.reload();
                }, 5000)
            }
        )
        .catch(function (error) {
            notifyError(error.response.data)
            setShow(false)
            setTimeout(function () {window.location.reload();}, 5000)
        });
    }

    return (
        <div>
            <Card className="m-3">
                <Card.Header className="d-flex align-items-center">
                    {props.firstName+" "+props.lastName}
                    <Badge className="ms-auto" bg="primary">{props.userRole}</Badge>
                </Card.Header>
                <Card.Body className="d-flex align-items-center">
                    <ul>
                        <li>Email: {props.email}</li>
                        <li>Broj telefona: {props.phoneNumber}</li>
                        <li>Adresa: {props.street + " " + props.number + ", " + props.place + ", " + props.country}</li>
                    </ul>

                    <Button onClick={() => setShow(true)} variant="outline-primary"
                            className="ms-auto m-1">Pregledaj</Button>
                    <Button variant="outline-success" className="m-1"
                            onClick={() => handleSubmit('approve')}>Odobri</Button>
                    <Button variant="outline-danger" className="m-1"
                            onClick={() => handleSubmit('deny')}>Poništi</Button>
                </Card.Body>
            </Card>

            <Modal show={show} onHide={() => setShow(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>{props.firstName+" "+props.lastName}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p>{props.registrationRationale}</p>

                    <hr className="mb-3 mt-3"/>

                    <Form.Group>
                        <Form.Label>Komentar</Form.Label>
                        <Form.Control as="textarea" rows={3} name="comment" onChange={e=>setResponse(e.target.value)}/>
                    </Form.Group>

                </Modal.Body>
                <Modal.Footer>
                    <Button onClick={() => setShow(false)} variant="outline-primary"
                            className="ms-auto m-1">Otkaži</Button>

                    <Button onClick={() => handleSubmit('approve')} variant="outline-success"
                            className="m-1">Odobri</Button>
                    <Button onClick={() => handleSubmit('deny')} variant="outline-danger"
                            className="m-1">Poništi</Button>
                </Modal.Footer>
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
        </div>
    )
}