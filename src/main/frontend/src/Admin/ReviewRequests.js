import {Sidebar} from "./Sidebar/Sidebar";
import React, {useEffect, useState} from "react";
import {Button, Card, Form, Modal} from "react-bootstrap";
import StarRatings from "react-star-ratings";
import axios from "axios";
import {backLink, frontLink, loadingToast, notifySuccess, updateForFetchedDataSuccess} from "../Consts";
import {ToastContainer} from "react-toastify";

export function ReviewRequests() {

    const [requests, setRequests] = useState([]);

    const fetchClientReviews = () => {
        axios.get(backLink + "/clientReviews").then(
            response => {
                setRequests(response.data)
            }
        )
    }

    useEffect(() => {
        fetchClientReviews()
    }, [])

    return (<div className="d-flex" style={{height: "100vh"}}>
        <div className="w-25" style={{backgroundColor: "#f7f8f9"}}>
            <Sidebar/>
        </div>
        <div className="w-75 overflow-auto">

            {requests.map((request, index) => {
                return (
                    <ReviewRequest key={index} request={request}/>
                )
            })}

        </div>
    </div>)
}

function ReviewRequest({request}) {
    const [show, setShow] = useState(false);

    let variant = "secondary";

    if (request.rating === 1) {
        variant = "danger"
    } else if (request.rating >= 4) {
        variant = "success"
    }


    return (
        <Card className="m-3" border={variant}>
            <Card.Header className="d-flex align-items-center">

                {request.resourceTitle !== null &&
                <h5 className="mt-2">{request.resourceTitle}</h5>
                }

                {request.vendorFullName !== null &&
                <h5 className="mt-2">{request.vendorFullName}</h5>
                }


            </Card.Header>
            <Card.Body className="d-flex align-items-center">
                <div className="flex-column">
                    <div className="d-flex align-items-center">
                        {request.clientFullName}
                    </div>
                    <StarRatings rating={request.rating} starDimension="17px" starSpacing="1px"
                                 starRatedColor="#f4b136"/>
                    <div>{request.comment}</div>

                </div>

                <Button className="ms-auto m-1" variant="outline-secondary"
                        onClick={() => setShow(true)}>Odgovori</Button>
                <ReviewRequestModal request={request} show={show} setShow={setShow}/>
            </Card.Body>
        </Card>
    )
}

function ReviewRequestModal({request, show, setShow}) {

    const [response, setResponse] = useState("");
    const approveClientReview = () => {
        const dto = {
            response: response,
            clientId: request.clientId,
            text: request.comment,
            rating: request.rating,
            resourceId: request.resourceId,
            vendorId: request.vendorId,
            requestId: request.requestId
        }
        const id1 = loadingToast()
        axios.post(backLink + "/clientReviews/approve", dto).then(
            response => {
                updateForFetchedDataSuccess(response.data, id1)
                setShow(false)
                setTimeout(function () {
                    window.location.reload()
                }, 2000)
            }
        )
    }
    const denyClientReview = () => {
        const dto = {
            requestId: request.requestId,
            response: response,
            clientId: request.clientId,
            text: request.comment,
            rating: request.rating,
            resourceId: request.resourceId,
            vendorId: request.vendorId
        }
        const id2 = loadingToast()
        axios.post(backLink + "/clientReviews/deny", dto).then(
            response => {
                updateForFetchedDataSuccess(response.data, id2)
                setShow(false)
                setTimeout(function () {
                    window.location.reload()
                }, 2000)
            }
        )
    }

    return <> <Modal show={show} onHide={() => setShow(false)}>
        <Modal.Header closeButton className="d-flex align-items-center">
            {request.resourceTitle !== null &&
            <h5 className="mt-2">{request.resourceTitle}</h5>
            }

            {request.vendorFullName !== null &&
            <h5 className="mt-2">{request.vendorFullName}</h5>
            }

        </Modal.Header>
        <Modal.Body>
            <div className="d-flex align-items-center">
                {request.clientFullName}
            </div>
            <StarRatings rating={request.rating} starDimension="17px" starSpacing="1px" starRatedColor="#f4b136"/>
            <div>{request.comment}</div>

            <Form>
                <Form.Label>Odgovor:</Form.Label>
                <Form.Control as="textarea" rows={3} name="response" value={response}
                              onChange={e => setResponse(e.target.value)}/>
            </Form>

        </Modal.Body>

        <Modal.Footer className="d-flex justify-content-end">
            <Button className="ms-auto m-1" variant="outline-success"
                    onClick={() => approveClientReview()}>Odobri</Button>
            <Button className="m-1" variant="outline-danger" onClick={() => denyClientReview()}>Odbij</Button>
        </Modal.Footer>
    </Modal>
        <ToastContainer
            position="top-center"
            autoClose={2000}
            hideProgressBar={false}
            newestOnTop={false}
            closeOnClick
            rtl={false}
            pauseOnFocusLoss
            draggable
            pauseOnHover
            theme={"colored"}
        />
        </>
}