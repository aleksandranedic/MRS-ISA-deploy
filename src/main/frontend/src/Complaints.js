import {getProfileLink, isLoggedIn, isClient} from "./Autentification";
import {backLink, missingDataErrors} from "./Consts";
import {Badge, Button, Form} from "react-bootstrap";
import React, {useEffect, useState} from "react";
import "./complaints.css"
import axios from "axios";
import {useParams} from "react-router-dom";

export function Complaints({type, toWhom}) {

    const [show, setShow] = useState(false);
    const {id} = useParams();
    const [text, setText] = useState("");
    const [noText, setNoText] = useState(false);
    const [entityType, setEntityType] = useState("");
    useEffect(() => {
        if (isLoggedIn()) {
            fetchShow();
        }
    }, [])
    console.log(toWhom);
    const fetchShow = () => {

        if (type === "adventure") {
            axios
                .get(backLink + "/adventure/clientCanReview/" + id + "/" + localStorage.getItem("userId"))
                .then(res => {
                    setShow(res.data);
                });
            setEntityType("ADVENTURE");
        } else if (type === "boat") {
            axios
                .get(backLink + "/boat/clientCanReview/" + id + "/" + localStorage.getItem("userId"))
                .then(res => {
                    setShow(res.data);
                });
            setEntityType("BOAT");
        } else if (type === "vacationHouse") {
            axios
                .get(backLink + "/house/clientCanReview/" + id + "/" + localStorage.getItem("userId"))
                .then(res => {
                    setShow(res.data);
                });
            setEntityType("VACATION_HOUSE");
        } else if (type === "fishingInstructor") {
            axios
                .get(backLink + "/adventure/clientCanReviewVendor/" + id + "/" + localStorage.getItem("userId"))
                .then(res => {
                    setShow(res.data);
                });
            setEntityType("FISHING_INSTRUCTOR");
        } else if (type === "boatOwner") {
            axios
                .get(backLink + "/boat/clientCanReviewVendor/" + id + "/" + localStorage.getItem("userId"))
                .then(res => {
                    setShow(res.data);
                });
            setEntityType("BOAT_OWNER");
        } else if (type === "vacationHouseOwner") {
            axios
                .get(backLink + "/house/clientCanReviewVendor/" + id + "/" + localStorage.getItem("userId"))
                .then(res => {
                    setShow(res.data);
                });
            setEntityType("VACATION_HOUSE_OWNER");
        }

    };


    const addComplaint = e => {

        e.preventDefault();

        if (text === "") {
            setNoText(true);
        }
        else {

            let dto = {
                comment: text,
                response: "",
                userId: localStorage.getItem("userId"),
                entityId: id,
                entityType: entityType
            }

            axios
                .post(backLink + "/complaints/add", dto)
                .then(response => {
                    console.log(response);
                    window.location.reload();
                })
                .catch(error => {
                    console.log(error)
                })

        }

    }

    return (<>{isLoggedIn() && show && isClient() &&
        <div className="complaints d-flex w-100" style={{height: "55vh",
            backgroundImage: "../images/homepage/pexels-pok-rie-2049422-complaints.jpg",
            backgroundSize: "cover",
            backgroundPosition: "center"}}>

            <div style={{width: "40vw"}} className="d-flex">

                <a href={getProfileLink()}>
                    <img className="profile-image" src={backLink + localStorage.getItem("profileImage")} alt={"profil"}/>

                </a>
                <div className="d-flex flex-column">
                    <h1 className="display-1 text-light" style={{marginTop:"6.5vw",fontSize: "5vh"}}>{localStorage.getItem("firstName")}</h1>
                    <h1 className="display-1 text-light" style={{fontSize: "5vh"}}>{localStorage.getItem("lastName")}</h1>
                </div>
            </div>

            <div className="d-flex flex-column w-50">


                <Badge className="mt-5 mb-2 custom-badge" style={{fontSize: "3vh"}}>
                    Podnesi žalbu na {toWhom}
                </Badge>





                <Form.Control className="w-100" as="textarea" rows={6} value={text} onChange={(e)=>setText(e.target.value)}
                              style={{backgroundColor: "rgba(255,255,255, 0.75)", border: "none"}}
                              isInvalid={noText}>
                </Form.Control>
                <Form.Control.Feedback type="invalid">
                    {missingDataErrors.complaintText}
                </Form.Control.Feedback>

                <Button className="mt-3 w-25 ms-auto me-2" onClick={addComplaint} style={{backgroundColor: "rgb(235,115,2)", borderColor: "rgb(235,115,2)"}}>Pošalji žalbu</Button>
            </div>



        </div>
}</>)
}