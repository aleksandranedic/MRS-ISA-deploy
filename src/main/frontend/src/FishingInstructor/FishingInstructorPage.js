import axios from "axios";
import React, {useEffect, useState} from "react";
import Banner from "../Banner";

import {FishingInstructorForm} from "./FishingInstructorForm";
import AdventureCarousel from "../Adventure/AdventureCarousel";
import Navigation from "../Navigation/Navigation";
import {useParams} from "react-router-dom";
import {Calendar} from "../Calendar/Calendar";
import {backLink, profilePicturePlaceholder} from "../Consts";
import {ReservationCardGrid} from "../Calendar/ReservationCardGrid";
import OwnerInfo from "../OwnerInfo"
import {ReservationsToReview} from "../Calendar/ReservationsToReview";
import {processReservationsForUsers} from "../ProcessToEvent";
import {isMyPage} from "../Autentification";
import BeginButton from "../BeginButton.js"
import Ratings from '../Reviews/Ratings';
import {Complaints} from "../Complaints";

const ReviewsComp = ({reviews}) => {
    if (typeof reviews !== "undefined") {
        return <Ratings reviews={reviews} type={"fishingInstructor"}/>
    } else {
        return <></>
    }
}

const FishingInstructors = ({id}) => {
    const [fishingInstructor, setFishingInstructor] = useState({address: '', profileImg: {path: profilePicturePlaceholder}});
    const [adventures, setAdventures] = useState([]);
    const [reservations, setReservations] = useState([]);
    const [events, setEvents] = useState(null);
    const [myPage, setMyPage] = useState(null);
    const [stat, setStat] = useState(null);
    const [ownerReviews, setOwnerReviews] = useState([])


    const fetchReservations = () => {
        axios.get(backLink + "/adventure/reservation/fishingInstructor/" + id).then(res => {
            setReservations(res.data);
            setEvents(processReservationsForUsers(res.data));
        })
    }

    const fetchStat = () => {
        axios
            .get(backLink + "/fishinginstructor/getStat/" + id)
            .then(res => {
                setStat(res.data);
            });
    };

    const fetchReviews = () => {
        axios
            .get(backLink + "/review/getVendorReviews/" + id)
            .then(res => {
                setOwnerReviews(res.data);
            });
    };

    const fetchFishingInstructors = () => {
        axios.get(backLink + "/fishinginstructor/" + id).then(res => {
            setFishingInstructor(res.data);
            setMyPage(isMyPage("FISHING_INSTRUCTOR", id));
        });
    };

    const fetchAdventures = () => {
        axios.get(backLink + "/adventure/owner/" + id).then(res => {
            setAdventures(res.data);
        });
    };

    useEffect(() => {
        fetchFishingInstructors();
        fetchAdventures();
        fetchReservations();
        fetchReviews();
        fetchStat();
    }, []);

    const [show, setShow] = useState(false);
    const handleShow = () => setShow(true);

    let html;

    if (fishingInstructor.length !== 0 && stat !== null) {

        let buttons = [
            {text: "Osnovne informacije", path: "#info"},
            {text: "Avanture", path: "#adventures"},
            {text: "Kalendar", path: "#calendar"}
        ];
        if (myPage) {
            buttons.push({text: "Rezervacije", path: "#reservations"});
        }

        buttons.push({text: "Recenzije", path: "#reviews"});
        html = (<div key={fishingInstructor.id}>

            <Banner caption={fishingInstructor.firstName + " " + fishingInstructor.lastName}/>

            <Navigation buttons={buttons}
                        editable={myPage} editFunction={handleShow} searchable={true}
                        showReports={myPage} type="adventure"
            />

            <div className="pe-5 pt-0">
                <OwnerInfo bio={fishingInstructor.biography}
                           name={fishingInstructor.firstName + " " + fishingInstructor.lastName}
                           rate={stat.rating}
                           email={fishingInstructor.email}
                           phoneNum={fishingInstructor.phoneNumber}
                           address={fishingInstructor.address}
                           profileImg={fishingInstructor.profileImg !== null ? backLink + fishingInstructor.profileImg.path : profilePicturePlaceholder}
                           category={stat.category}
                           points={stat.points}
                />
            </div>
            <hr className="me-5 ms-5"/>

            <AdventureCarousel adventures={adventures} add={myPage} ownerId={fishingInstructor.id}/>

            <FishingInstructorForm show={show} setShow={setShow} fishingInstructor={fishingInstructor}
                                   profileImg={fishingInstructor.profileImg !== null ? backLink + fishingInstructor.profileImg.path : profilePicturePlaceholder}/>

            <h2 className="mt-5 ms-5">Kalendar</h2>

            <hr className="me-5 ms-5"/>

            <Calendar events={events} reservable={false} reservations = {reservations}  myPage={myPage}/>


            {myPage && <div id="reservations">

                <ReservationsToReview type={"adventure"}/>
                <h2 className="me-5 ms-5 mt-5" id="reservations">Rezervacije</h2>

                <hr className="me-5 ms-5"/>

                <ReservationCardGrid reservations={reservations}/>

            </div>}
            <div className="ms-5 me-5" id="reviews">
                <h2 className="mt-5" id="reviews">Recenzije</h2>
                <hr/>
                <ReviewsComp reviews={ownerReviews}/>
            </div>

                <Complaints type={"fishingInstructor"} toWhom={fishingInstructor.firstName + " " + fishingInstructor.lastName}/>


            <BeginButton/>

        </div>)
    }

    return html;

};



            export function FishingInstructorPage() {
    const {id} = useParams();
    return (
        <>

            <FishingInstructors id={id}/>

        </>


    )
}

