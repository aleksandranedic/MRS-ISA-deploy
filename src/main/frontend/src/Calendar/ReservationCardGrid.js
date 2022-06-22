import {Button, Card, Col, Row} from "react-bootstrap";
import {compareDates, compareDuration, convertToDate} from "./ReservationDateConverter";
import Tag from "../Tag";
import React, {useState} from "react";
import {TimeFooter} from "./TimeFooter";
import {backLink, frontLink, notifySuccess} from "../Consts";
import axios from "axios";
import {ReservationAdditionalInfoModal} from "./ReservationAdditionalInfoModal";
import {MdUnfoldMore} from "react-icons/md";
import {isClient} from "../Autentification";
import {ToastContainer} from "react-toastify";


export function ReservationCardGrid({reservations}) {

    const [sortBy, setSortBy] = useState("");


    function sortByPrice() {
        setSortBy("price");
    }

    function sortByDate() {
        setSortBy("date");
    }

    function sortByDuration() {
        setSortBy("duration");
    }


    let futureReservations = []

    for (let index in reservations) {
        let reservation = reservations.at(index);
        if (reservation.busyPeriod === false && reservation.quickReservation === false && convertToDate(reservation.appointments.at(0).startTime) >= Date.now()) {
            futureReservations.push(reservation);
        }
    }

    let html = "";



    if (reservations !== null)
        if (futureReservations.length > 0)
            html = <>

                <div className="d-flex ms-5 me-5">
                    <div className="ms-auto d-flex align-items-center">Sortiraj po:</div>
                    <Button onClick={sortByPrice} variant="secondary" className="m-2">Cena</Button>
                    <Button onClick={sortByDate} variant="secondary" className="m-2">Datum</Button>
                    <Button onClick={sortByDuration} variant="secondary" className="m-2">Trajanje</Button>

                </div>
                <div className="ms-5 me-5"
                     style={{
                         maxHeight: "60vh", overflow: "auto"
                     }}>

                    {sortBy === "" &&

                        <Grid futureReservations={futureReservations}/>
                    }

                    {sortBy === "price" &&
                        <Grid futureReservations={futureReservations.sort((a,b) => (a.price > b.price) ? 1 : ((b.price > a.price) ? -1 : 0))}/>
                    }

                    {sortBy === "date" &&
                        <Grid futureReservations={futureReservations.sort((a,b) => (compareDates(a,b)) ? 1 : (compareDates(a,b) ? -1 : 0))}/>
                    }

                    {sortBy === "duration" &&
                        <Grid futureReservations={futureReservations.sort((a,b) => (compareDuration(a,b)) ? 1 : ((compareDuration(b,a)) ? -1 : 0))}/>

                    }
                </div>

            </>;
    return html;
}

export function Grid({futureReservations}) {

    const [showReservation, setShowReservation] = useState(false);
    const [reservation, setReservation] = useState("");

    const cancelReservation = (reservation) => {
        axios.post(backLink + "/" + reservation.entityType + "/cancelReservation/" + reservation.id).then(
            response => {
                notifySuccess(response.data)
                setTimeout(function () {
                    window.location.reload();
                }, 2000)
            }
        )
    }

    return <><Row xs={1} md={2} lg={3} className="g-4 mb-4 w-100">
        {futureReservations.map((reservation, index) => (
            <Col key={index + "col"}>
                <Card key={index} style={{
                    borderRadius: "0"
                }}>
                    <Card.Header className="d-flex text-light lead" style={{
                        background: "linear-gradient(50deg, rgba(9,56,126,1) 38%, rgba(10,138,126) 96%)",
                        borderRadius: "0"
                    }}>
                        <div>{reservation.resourceTitle}</div>
                        <a href={frontLink + "client/" + reservation.client.id}
                           style={{textDecoration: "none", color: "white"}}
                           className="ms-auto fw-bold"
                        >

                            <div>
                                {reservation.client.firstName + " " + reservation.client.lastName}
                            </div>
                        </a>
                        <Button variant={"outline-light"} size="sm" className="ms-3"
                                onClick={() => cancelReservation(reservation)}>Otkaži</Button>
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
                    <Card.Footer className="fw-light d-flex align-items-center">

                        <div style={{width: "95%"}}>
                            <TimeFooter reservation={reservation}/>
                        </div>

                        <div className="ms-auto">
                            <MdUnfoldMore onClick={() => {
                                setReservation(reservation);
                                setShowReservation(true)
                            }} style={{cursor: "pointer"}}/>
                        </div>


                    </Card.Footer>
                </Card>
            </Col>))}
    </Row>
        <ReservationAdditionalInfoModal reservation={reservation} setShowReservation={setShowReservation}
                                        showReservation={showReservation} myPage={true}/>
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