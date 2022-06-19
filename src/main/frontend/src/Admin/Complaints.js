import {Sidebar} from "./Sidebar/Sidebar";
import React, {useEffect, useState} from "react";
import {Button, Card, Form, Modal} from "react-bootstrap";
import {backLink, loadingToast, updateForFetchedDataError, updateForFetchedDataSuccess} from "../Consts";
import axios from "axios";
import {ToastContainer} from "react-toastify";
import {BsArrowRight} from "react-icons/bs";

export function Complaints() {

    const [complaints, setComplaints] = useState([]);

    const fetchComplaints = () => {
        axios.get(backLink + "/complaints").then(
            response => {
                setComplaints(response.data)
            }
        )
    }
    useEffect(() => {
        fetchComplaints()
    }, [])


    return (<div className="d-flex" style={{height: "100vh"}}>
        <div className="w-25" style={{backgroundColor: "#f7f8f9"}}>
            <Sidebar/>
        </div>
        <div className="w-75 overflow-auto">

            {complaints.map((request, index) => {
                return (
                    <Complaint key={index} request={request}/>
                )
            })}

        </div>
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
    </div>)
}

function Complaint({request}) {
    const [show, setShow] = useState(false);

    let variant = "secondary";


    return (
        <Card className="m-3" border={variant}>
            <Card.Header className="d-flex align-items-center">
                <h5 className="mt-2">{request.userFullName} <BsArrowRight/> {request.entityName}</h5>
            </Card.Header>
            <Card.Body className="d-flex align-items-center">
                <div className="flex-column">

                    <div>{request.comment}</div>

                </div>

                <Button className="ms-auto m-1" variant="outline-secondary"
                        onClick={() => setShow(true)}>Odgovori</Button>
                <ComplaintModal request={request} show={show} setShow={setShow}/>
            </Card.Body>
        </Card>
    )
}

function ComplaintModal({request, show, setShow}) {

    const [response, setResponse] = useState("");
    const answerComplaint = () => {
        const dto = {
            complaintId: request.complaintId,
            userId: request.userId,
            entityId: request.entityId,
            response: response,
            entityType: request.entityType
        }
        let id = loadingToast()
        axios
        .post(backLink + "/complaints", dto)
        .then(response => {
            updateForFetchedDataSuccess(response.data, id)
            setShow(false)
            setTimeout(function () {
                window.location.reload();
            }, 2000)
        })
        .catch(function (error) {
            updateForFetchedDataError(error.response.data, id)
            setShow(false)
            setTimeout(function () {window.location.reload();}, 2000);
        });
    }

    return <Modal show={show} onHide={() => setShow(false)}>
        <Modal.Header closeButton className="d-flex align-items-center">
            <h5 className="mt-2">{request.userFullName}</h5>

        </Modal.Header>
        <Modal.Body>

            <Form>
                <Form.Label>Odgovor:</Form.Label>
                <Form.Control as="textarea" rows={3} name="response" value={response}
                              onChange={e => setResponse(e.target.value)}/>
            </Form>

        </Modal.Body>

        <Modal.Footer className="d-flex justify-content-end">
            <Button className="ms-auto m-1" variant="outline-secondary"
                    onClick={() => answerComplaint()}>Odgovori</Button>
        </Modal.Footer>
    </Modal>
}