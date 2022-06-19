import {backLink, responsive} from "../Consts";

import Carousel from "react-multi-carousel";
import React, {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import axios from "axios";
import {Card} from "react-bootstrap";
import {TimeFooter} from "./TimeFooter";
import StarRatings from "react-star-ratings";
import {Button, Form} from "react-bootstrap";


export function ReservationsToReview({type}) {

    const {id} = useParams();
    const [reservations, setReservations] = useState([]);

    const fetchReservations = () => {

        if (type === "adventure") {
            axios
                .get(backLink + "/adventure/reservation/forReview/" + id)
                .then(res => {
                    setReservations(res.data);
                });
        } else if (type === "boat") {
            axios
                .get(backLink + "/boat/reservation/forReview/" + id)
                .then(res => {
                    setReservations(res.data);
                });

        } else if (type === "vacationHouse") {
            axios
                .get(backLink + "/house/reservation/forReview/" + id)
                .then(res => {
                    setReservations(res.data);
                });
        }

    };

    const addReview = ({reservation}) => {
        let dto = {
            resourceId: reservation.resourceId,
            vendorId: id,
            rating: rating,
            text: text,
            clientId: reservation.client.id,
            penalty: penalty,
            noShow: noShow,
            reservationId: reservation.id
        }

        axios.post(backLink + "/review/vendor/add", dto).then( response => {
            console.log(response.data);
            }
        ).catch(err => {

        });

        window.location.reload();

    }

    useEffect(() => {
        fetchReservations();
    }, [])

    const [rating, setRating] = useState(0);
    const [penalty, setPenalty] = useState(false);
    const [noShow, setNoShow] = useState(false);
    const [text, setText] = useState("");

    return (
        <div>

            <Carousel className="m-5" responsive={responsive} interval="25000">
                {reservations.map((reservation, index) => {
                    return (
                        <Card
                            border="primary"
                            key={index}
                            text="primary"
                            style={{width: '18rem'}}
                            className="mb-2"
                        >

                            <Card.Body>
                                <Card.Title
                                    className="ms-2">{reservation.client.firstName + " " + reservation.client.lastName}</Card.Title>

                                <div className="m-2 flex-column justify-content-center">
                                    <StarRatings rating={rating}
                                                 changeRating={setRating}
                                                 numberOfStars={5}
                                                 starDimension="27px"
                                                 starSpacing="1px"
                                                 starRatedColor="#0f7c93"
                                                 starEmptyColor="#0f7c9355"/>

                                    <Form.Control className="mt-4" as="textarea" rows={3}
                                                  onChange={(e) => setText(e.target.value)}/>

                                    <div className="mt-4 ms-2">
                                        <Form.Check
                                            type="switch"
                                            label="Bez pojavljivanja"
                                            onChange={()=> setNoShow(!noShow)}
                                        />
                                        <Form.Check
                                            type="switch"
                                            label="Penal"
                                            onChange={()=> setPenalty(!penalty)}
                                        />
                                    </div>
                                    <Button variant="outline-primary" className="mt-4" onClick={()=> addReview({reservation})}>Ostavi recenziju</Button>

                                </div>
                            </Card.Body>
                            <Card.Footer>
                                <TimeFooter reservation={reservation}/>
                            </Card.Footer>
                        </Card>
                    )

                })}


            </Carousel>
        </div>
    )
}