import {Sidebar} from "./Sidebar/Sidebar";
import React, {useEffect, useState} from "react";
import {Badge, Button, Card, Form, Modal} from "react-bootstrap";
import {BsArrowRight} from "react-icons/bs";
import StarRatings from "react-star-ratings";
import {backLink, loadingToast, updateForFetchedDataSuccess} from "../Consts";
import axios from "axios";
import {ToastContainer} from "react-toastify";

export function PenaltyRequests() {

    const [requests, setRequests] = useState([]);

    const fetchPenalty = () => {
        axios.get(backLink + "/penaltyReview").then(
            response => {
                setRequests(response.data)
            }
        )
    }

    useEffect(() => {
        fetchPenalty()
    }, [])

    return (<div className="d-flex" style={{height: "100vh"}}>
        <div className="w-25" style={{backgroundColor: "#f7f8f9"}}>
            <Sidebar/>
        </div>
        <div className="w-75 overflow-auto">

            {requests.map((request, index) => {
                return (
                    <PenaltyRequestCard key={index} request={request}/>
                )
            })}

        </div>
    </div>)
}

function PenaltyRequestCard({request}) {
    const [show, setShow] = useState(false);

    let variant = "secondary";

    if (request.penalty === true) {
        variant = "danger"
    } else if (request.noShow === true) {
        variant = "warning"
    } else if (request.rating === 5) {
        variant = "success"
    }


    return (
        <Card className="m-3" border={variant}>
            <Card.Header className="d-flex align-items-center">
                <h5 className="mt-2">{request.resourceTitle}</h5>

                {request.penalty === true &&
                <Badge className="ms-auto" bg="danger">PENAL</Badge>
                }
                {request.noShow === true && request.penalty === false &&
                <Badge className="ms-auto" bg="warning">BEZ POJAVLJIVANJA</Badge>
                }
                {request.noShow === true && request.penalty === true &&
                <Badge className="ms-2" bg="warning">BEZ POJAVLJIVANJA</Badge>
                }

            </Card.Header>
            <Card.Body className="d-flex align-items-center">
                <div className="flex-column">
                    <div className="d-flex align-items-center">

                        {request.vendorFullName}
                        <BsArrowRight className="me-1 ms-1"/>
                        {request.clientFullName}

                    </div>
                    <StarRatings rating={request.rating} starDimension="17px" starSpacing="1px"
                                 starRatedColor="#f4b136"/>
                    <div>{request.comment}</div>

                </div>

                <Button className="ms-auto m-1" variant="outline-secondary"
                        onClick={() => setShow(true)}>Odgovori</Button>
                <PenaltyRequestModal request={request} show={show} setShow={setShow}/>
            </Card.Body>
        </Card>
    )
}

function PenaltyRequestModal({request, show, setShow}) {
    const [checkPenalty, setCheckPenalty] = useState(false)
    const [checkNoShow, setNoShow] = useState(false)
    const [response, setResponse] = useState("");

    const approveVendorReview = () => {
        const dto = {
            response: response,
            clientId: request.clientId,
            penalty: request.penalty,
            noShow: request.noShow,
            reservationId: request.reservationId,
            resourceId: request.resourceId,
            rating: request.rating,
            text: request.comment,
            checkNoShow: checkNoShow,
            checkPenalty: checkPenalty,
            vendorReviewRequestId: request.vendorReviewRequestId
        }
        console.log(dto)
        let id = loadingToast()
        axios.post(backLink + "/penaltyReview/approve", dto).then(
            res => {
                updateForFetchedDataSuccess(res.data, id)
                setShow(false)
                setTimeout(function () {
                    window.location.reload();
                }, 2000)
            }
        )
    }
    const denyVendorReview = () => {

        const dto = {
            vendorReviewRequestId: request.vendorReviewRequestId,
            resourceId: request.resourceId,
            response: response,
        }
        console.log(dto)
        let id = loadingToast()
        axios.post(backLink + "/penaltyReview/deny", dto).then(
            res => {
                updateForFetchedDataSuccess(res.data, id)
                setShow(false)
                setTimeout(function () {
                    window.location.reload();
                }, 2000)
            }
        )
    }


    return <> <Modal show={show} onHide={() => setShow(false)}>
        <Modal.Header closeButton className="d-flex align-items-center">
            <h5 className="mt-2">{request.resourceTitle}</h5>

        </Modal.Header>
        <Modal.Body>
            <div className="d-flex align-items-center">

                {request.vendorFullName}
                <BsArrowRight className="me-1 ms-1"/>
                {request.clientFullName}

            </div>
            <StarRatings rating={request.rating} starDimension="17px" starSpacing="1px" starRatedColor="#f4b136"/>
            <div>{request.comment}</div>


            <Form>
                <Form.Label>Odgovor:</Form.Label>
                <Form.Control as="textarea" rows={3} name="response" value={response}
                              onChange={e => setResponse(e.target.value)}/>
                {request.penalty === true &&
                <Form.Check
                    value={checkPenalty}
                    onChange={() => {
                        setCheckPenalty(!checkPenalty)
                    }}
                    type="switch"
                    id="penalty"
                    label="Dodeli penal"
                    className="mt-2"
                />}
                {request.noShow === true &&
                <Form.Check
                    value={checkNoShow}
                    onChange={() => {
                        setNoShow(!checkNoShow)
                    }}
                    type="switch"
                    id="noShow"
                    label="Dodeli penal za nepojavljivanje"
                    className="mt-2"
                />}
            </Form>

        </Modal.Body>

        <Modal.Footer className="d-flex justify-content-end">
            {request.penalty === true &&
            <Badge bg="danger">PENAL</Badge>
            }
            {request.noShow === true &&
            <Badge bg="warning">BEZ POJAVLJIVANJA</Badge>
            }
            <Button className="ms-auto m-1" variant="outline-success" onClick={() => approveVendorReview()}>Odobri</Button>
            <Button className="m-1" variant="outline-danger" onClick={() => denyVendorReview()}>Poništi</Button>
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
    </>
}