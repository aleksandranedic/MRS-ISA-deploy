import {Image, Modal} from "react-bootstrap";
import {backLink, frontLink} from "../Consts";
import {Info, TagInfo} from "../Info";
import {TimeFooter} from "./TimeFooter";
import React from "react";

export function ReservationAdditionalInfoModal({reservation, showReservation, setShowReservation, myPage}) {

    let color = "";
    if (reservation.quickReservation === true) {
        color = "linear-gradient(100deg, rgba(186,195,34,1) 0%, rgba(253,187,45,1) 53%, rgba(253,111,45,1) 86%)";
    }else if(reservation.busyPeriod === true) {
        color = "linear-gradient(90deg, rgba(131,58,180,1) 12%, rgba(164,27,27,1) 68%, rgba(246,41,23,1) 100%)";
    }else {
        color="linear-gradient(50deg, rgba(9,56,126,1) 38%, rgba(10,138,126) 96%)";
    }

    let html;

    if (typeof myPage !== "undefined") {
        html = <Modal show={showReservation} onHide={() => setShowReservation(false)} style={{border: "none"}}>
            <Modal.Header style={{background: color, color:"white"}} closeButton className="d-flex">
                <Modal.Title>

                    {reservation.busyPeriod === true && "Period zauzetosti"}
                    {reservation.quickReservation === true && "Akcija: " + reservation.resourceTitle}
                    {reservation.quickReservation === false && reservation.busyPeriod === false && reservation.resourceTitle}

                </Modal.Title>


            </Modal.Header>

            <Modal.Body style={{border: "none"}}>
                {reservation !== "" &&
                    <div className="p-0 m-0">

                        {reservation.client !== null && myPage &&
                            <div className="d-flex align-items-center">
                                <Image
                                    className="ms-3"
                                    style={{
                                        width: "5rem",
                                        height: "5rem",
                                        borderRadius: "5rem"
                                    }} src={backLink + reservation.client.profileImg.path}/>
                                <a href={frontLink + "client/" + reservation.client.id} style={{textDecoration: "none", color: "rgb(68,68,68)"}}>
                                    <h4 className="ms-4">{reservation.client.firstName} {reservation.client.lastName}</h4>
                                </a>

                            </div>

                        }
                        {reservation.busyPeriod === false &&
                            <div className="d-flex">
                                <div className="m-1">
                                    <Info title="Resurs" text={reservation.resourceTitle}/>
                                </div>
                                <div className="m-1">
                                    <Info title="Broj klijentata" text={reservation.numberOfClients}/>
                                </div>
                                <div className="m-1">
                                    <Info title="Cena" text={reservation.price + "€"}/>
                                </div>
                            </div>

                        }

                        {reservation.additionalServices.length > 0 &&
                            <div className="m-3">
                                <TagInfo title="Dodatne usluge" tagList={reservation.additionalServices}/>
                            </div>
                        }
                        {reservation.busyPeriod === true &&
                            <div className="d-flex">
                                U ovom terminu nije moguće napraviti rezervaciju.
                            </div>

                        }


                    </div>}

            </Modal.Body>

            <Modal.Footer className="fw-light">
                <div className="ms-auto">
                    <TimeFooter reservation={reservation}/>
                </div>
            </Modal.Footer>

        </Modal>;

    }

    return html;
}