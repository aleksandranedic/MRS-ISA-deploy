import React, {useEffect, useState} from "react";
import StarRatings from "react-star-ratings";
import {Button, Form} from "react-bootstrap";
import axios from "axios";
import {backLink} from "../Consts";
import {useParams} from "react-router-dom";
import {isLoggedIn} from "../Autentification";

export function AddReview({type}) {

    const [show, setShow] = useState(false);
    const [user, setUser] = useState(null);
    const [text, setText] = useState(null);
    const {id} = useParams();

    const addReview = () => {
        let vendorId = -1;
        let resourceId = -1;

        if (type === "adventure" || type === "vacationHouse" || type === "boat") {
            resourceId = id;
        }
        else {
            vendorId = id;
        }

        let review = {
            resourceId: resourceId,
            vendorId: vendorId,
            rating: rating,
            text: text,
            clientId: user.id
        };

        axios.post(backLink + "/review/add", review).then(
            res => {
                window.location.reload()
            }
        ).catch();
    }


    const fetchUser = () => {
        if (isLoggedIn()) {
            axios.get(backLink + "/getLoggedUser").then(
                res => {
                    setUser(res.data);
                }
            ).catch(err => {
            })
        }

    }

    const fetchShow = () => {

        if (type === "adventure") {
            axios
                .get(backLink + "/adventure/clientCanReview/" + id + "/" + user.id)
                .then(res => {
                    setShow(res.data);
                });
        } else if (type === "boat") {
            axios
                .get(backLink + "/boat/clientCanReview/" + id + "/" + user.id)
                .then(res => {
                    setShow(res.data);
                });
        } else if (type === "vacationHouse") {
            axios
                .get(backLink + "/house/clientCanReview/" + id + "/" + user.id)
                .then(res => {
                    setShow(res.data);
                });
        } else if (type === "fishingInstructor") {
            axios
                .get(backLink + "/adventure/clientCanReviewVendor/" + id + "/" + user.id)
                .then(res => {
                    setShow(res.data);
                });
        } else if (type === "boatOwner") {
            axios
                .get(backLink + "/boat/clientCanReviewVendor/" + id + "/" + user.id)
                .then(res => {
                    setShow(res.data);
                });
        } else if (type === "vacationHouseOwner") {
            axios
                .get(backLink + "/house/clientCanReviewVendor/" + id + "/" + user.id)
                .then(res => {
                    setShow(res.data);
                });
        }

    };

    useEffect(() => {
        fetchUser();
        if (user !== null) {
            fetchShow();
        }

    })

    const [rating, setRating] = useState(0);

    let html = "";

    if (show) {
        html = <div className="w-25 m-2" style={{border: "1px solid rgba(0, 0, 0, 0.125)", borderRadius: "15px"}}>
            <div className='w-100 m-2 d-flex flex-column align-items-center mb-3'>
                <h1 className="m-3 text-lead"
                    style={{color: "#313041", fontSize: "28px", lineHeight: "60px", letterSpacing: "-.02em"}}>
                    Unesi ocenu
                </h1>
                <StarRatings rating={rating} changeRating={setRating} numberOfStars={5} starDimension="27px"
                             starSpacing="1px" starRatedColor="#f4b136"/>

                <Form.Control as="textarea" rows={5} className="mt-3"
                              style={{
                                  border: "1px solid rgba(0, 0, 0, 0.125)", borderRadius: "15px",
                                  width: "80%", height: "8.5rem"
                              }}
                              onChange={(e) => setText(e.target.value)}/>

                <Button className="mt-2" onClick={() => addReview()}>Ostavi recenziju</Button>
            </div>

        </div>;
    }
    return html
}