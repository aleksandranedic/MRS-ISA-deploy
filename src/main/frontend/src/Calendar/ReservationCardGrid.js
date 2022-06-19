import  {Button,Card, Col, Row} from "react-bootstrap";
import {convertToDate} from "./ReservationDateConverter";
import Tag from "../Tag";
import React from "react";
import {TimeFooter} from "./TimeFooter";
import {backLink, frontLink, notifySuccess} from "../Consts";
import axios from "axios";

export function ReservationCardGrid({reservations}) {

    const cancelReservation=(reservation)=>{
        axios.post(backLink+"/"+reservation.entityType+"/cancelReservation/"+reservation.id).then(
            response=>{
                notifySuccess(response.data)
                setTimeout(function () {
                    window.location.reload();
                }, 2000)
            }
        )
    }
    console.log(reservations)
    let indexesToPop = []

    for (let index in reservations) {
        let reservation = reservations.at(index);
        if (reservation.busyPeriod === true) {
            indexesToPop.push(index);
        } else if (convertToDate(reservation.appointments.at(0).startTime) < Date.now()) {
            indexesToPop.push(index);
        }
    }

    for (let index in indexesToPop) {
        reservations.pop(index);
    }

    let html = "";

    if (reservations !== null)
        if (reservations.length > 0)
        html = <>
        <div className="ms-5 me-5"
             style={{
                 maxHeight: "60vh", overflow: "auto"
             }}>
            <Row xs={1} md={2} lg={3} className="g-4 mb-4 w-100">
                {reservations.map((reservation, index) => (
                    <Col key={index + "col"}>
                        <Card key={index} style={{
                            borderRadius: "0"
                        }}>
                            <Card.Header className="d-flex text-light lead" style={{
                                background: "linear-gradient(50deg, rgba(9,56,126,1) 38%, rgba(10,138,126) 96%)",
                                borderRadius: "0"
                            }}>
                                <div>{reservation.resourceTitle}</div>
                                <div className="ms-auto fw-bold">
                                    {reservation.client.firstName + " " + reservation.client.lastName}
                                </div>
                                <Button variant={"light"} className="ms-1" onClick={()=>cancelReservation(reservation)}>Otkaži</Button>
                            </Card.Header>
                            <Card.Body>

                                <div className="d-flex">
                                    <div>
                                        <div>{reservation.additionalServices !== [] && reservation.additionalServices.map((tagData) => {
                                            return <Tag key={tagData.id} tag={tagData.text} id={tagData.id}/>
                                        })}</div>

                                    </div>
                                    <div className="ms-auto fs-4" style={{color: "#ED7301"}}>
                                        {reservation.price}€
                                    </div>
                                </div>
                            </Card.Body>
                            <Card.Footer className="fw-light">


                                <TimeFooter reservation={reservation}/>
                            </Card.Footer>
                        </Card>
                    </Col>))}
            </Row>


        </div>
    </>;
    return html;
}