import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from '@fullcalendar/timegrid';
import {Button, Card, Dropdown} from "react-bootstrap";
import React, {useState} from "react";
import interactionPlugin from "@fullcalendar/interaction";
import {ReservationModal} from "./ReservationModal";
import {BusyPeriodModal} from "./BusyPeriodModal";
import {BsPlusLg} from "react-icons/bs";
import {isLoggedIn, isClient} from "../Autentification";

export function Calendar({reservable, pricelist, type, resourceId, events, myPage, additionalServices}) {

    let perHour = true;
    if (type === "vacationHouse") {
        perHour = false;
    }
    const [showReservationDialog, setShowReservationDialog] = useState(false);
    const [showBusyPeriodDialog, setShowBusyPeriodDialog] = useState(false);

    const calendarRef = React.createRef();

    const reservationClick = () => {
        setShowReservationDialog(true);
    }

    const changeView = (view) => {
        calendarRef.current
            .getApi()
            .changeView(view);
    }

    let html = "";

    if (events !== null) {
        html = <><>

            <div id="calendar" className="d-flex justify-content-center">

                <div className="ms-5 me-3 mt-4" style={{width: "65%"}}>
                    <Dropdown>
                        <Dropdown.Toggle className="mb-3" variant="outline-primary" id="dropdown-basic">
                            Tip pregleda kalendara
                        </Dropdown.Toggle>

                        <Dropdown.Menu>
                            <Dropdown.Item onClick={() => changeView('dayGrid')}>Dnevni pregled</Dropdown.Item>
                            <Dropdown.Item onClick={() => changeView('dayGridWeek')}>Nedelji pregled</Dropdown.Item>
                            <Dropdown.Item onClick={() => changeView('dayGridMonth')}>Mesečni pregled</Dropdown.Item>
                            <Dropdown.Item onClick={() => changeView('timeGridWeek')}>Nedeljni vremenski
                                pregled</Dropdown.Item>
                            <Dropdown.Item onClick={() => changeView('timeGridDay')}>Dnevni vremenski
                                pregled</Dropdown.Item>
                        </Dropdown.Menu>
                    </Dropdown>
                    <FullCalendar
                        plugins={[dayGridPlugin, interactionPlugin, timeGridPlugin]}
                        initialView="dayGridMonth"
                        timeZone="CEST"
                        events={events}
                        ref={calendarRef}
                    />
                </div>

                {
                    reservable &&
                    <div className="d-flex flex-column align-items-center justify-content-center">
                        <div className="d-flex align-items-center justify-content-center"
                             style={{
                                 width: "12rem", height: "10rem",
                                 background: "linear-gradient(50deg, rgba(13,110,252,1) 38%, rgba(13,252,229,1) 96%)",
                                 borderRadius: "0.5rem"
                             }}

                        >
                            <Card style={{
                                width: "11.5rem",
                                height: "9.5rem",
                                backgroundColor: "white",
                                border: "none"
                            }}>
                                <Card.Body>
                                    <div className="d-flex align-items-center justify-content-center w-100 h-75">
                                        <h3 style={{color: "rgba(13,110,252,1)"}}>{pricelist.price}€</h3>
                                        <h1 style={{fontSize: "5.5rem", fontWeight: "lighter"}}>/</h1>
                                        <h6 style={{color: "rgba(13,252,229,1)"}}>{perHour ? "Sat" : "Dan"}</h6>
                                    </div>


                                </Card.Body>
                            </Card>
                        </div>

                        {isLoggedIn() && (isClient() || myPage) &&
                            <Button className="m-2" style={{width: "12rem"}} onClick={reservationClick}>Rezerviši</Button>

                        }

                        {myPage &&
                            <Button className="m-2 d-flex align-items-center" style={{width: "12rem"}}
                                    onClick={() => setShowBusyPeriodDialog(true)}>
                                <BsPlusLg className="ms-1 me-1" style={{height: "0.8rem", paddingTop: "0.1rem"}}/>
                                Period zauzetosti
                            </Button>
                        }

                    </div>
                }


                <ReservationModal show={showReservationDialog} setShow={setShowReservationDialog} type={type}
                                  resourceId={resourceId} myPage={myPage} additionalServices={additionalServices}/>
                <BusyPeriodModal show={showBusyPeriodDialog} setShow={setShowBusyPeriodDialog} type={type}
                                 resourceId={resourceId}/>
            </div>
        </></>;
    }
    return html;
}