import React, {useEffect, useState} from 'react';
import axios from "axios";
import Banner from '../Banner';
import BeginButton from '../BeginButton';

import OwnerInfo from '../OwnerInfo';
import OwnerHouses from './OwnerHouses';
import AddVacationHouse from './AddVacationHouse';
import HouseOwnerForm from "./HouseOwnerForm";
import {useParams} from "react-router-dom";
import Navigation from "../Navigation/Navigation";
import {backLink, frontLink, profilePicturePlaceholder} from '../Consts';
import {Calendar} from "../Calendar/Calendar";
import {ReservationCardGrid} from "../Calendar/ReservationCardGrid";
import {processReservationsForUsers} from "../ProcessToEvent";
import Ratings from '../Reviews/Ratings';
import {Complaints} from "../Complaints";
import {isLoggedIn, isMyPage, isClient} from "../Autentification";
import {ReservationsToReview} from "../Calendar/ReservationsToReview";
import { getProfileLink } from '../Autentification';


const UpdateOwner = ({show, setShow, owner}) => {
    if (typeof owner.firstName !== "undefined") {
        if (owner.profileImg !== null) {
            var profileImg = backLink + owner.profileImg.path;
        } else {
            var profileImg = profilePicturePlaceholder;
        }
        return <HouseOwnerForm show={show} setShow={setShow} owner={owner} profileImg={profileImg}/>
    } else {
        return <></>
    }
}

const ReviewsComp = ({reviews}) => {
    if (typeof reviews !== "undefined") {
        return <Ratings reviews={reviews} type={"vacationHouseOwner"}/>
    } else {
        return <></>
    }
}

function HouseOwnerPage() {
    const {id} = useParams();
    const [houseOwner, setHouseOwner] = useState({address: '', profileImg: {path: profilePicturePlaceholder}});
    const [ownerHouses, setOwnerHouses] = useState([]);
    const [ownerReviews, setOwnerReviews] = useState([])
    const [events, setEvents] = useState(null);
    const [show, setShow] = useState(false);
    const [myPage, setMyPage] = useState(null);

    const handleShow = () => setShow(true);

    const [reservations, setReservations] = useState([]);

    const [stat, setStat] = useState(null);

    const fetchStat = () => {
        axios
            .get(backLink + "/houseowner/getStat/" + id)
            .then(res => {
                setStat(res.data);
                console.log(res.data);
            });
    };

    const fetchReservations = () => {
        axios.get(backLink + "/house/reservation/vacationHouseOwner/" + id).then(res => {
            setReservations(res.data);
            setEvents(processReservationsForUsers(res.data));
        })
    }

    const fetchOwnerHouses = () => {
        axios
            .get(backLink + "/house/getownerhouses/" + id)
            .then(res => {
                var houses = res.data;
                for (let house of houses) {
                    if (!house.thumbnailPath.includes(backLink)) {
                        house.thumbnailPath = backLink + house.thumbnailPath;
                    }
                }
                setOwnerHouses(res.data);
                setMyPage(isMyPage("VACATION_HOUSE_OWNER", id));
            });
    };
    const fetchHouseOwner = () => {
        axios
            .get(backLink + "/houseowner/" + id)
            .then(res => {
                if (res.data === ''){
                    var profileLink = getProfileLink();
                    window.location.href = frontLink + "pageNotFound"
                }
                setHouseOwner(res.data);
                fetchOwnerHouses();
                fetchReservations();
                fetchReviews();
                fetchStat();
            });
    };

    const fetchReviews = () => {
        axios
            .get(backLink + "/review/getVendorReviews/" + id)
            .then(res => {
                setOwnerReviews(res.data);
            });
    };

    useEffect(() => {
        fetchHouseOwner();
        fetchReviews();
    }, []);

    let buttons = [
        {text: "Osnovne informacije", path: "#info"},
        {text: "Vikendice", path: "#houses"},
        {text: "Kalendar", path: "#calendar"}

    ];
    if (myPage) {
        buttons.push({text: "Rezervacije", path: "#reservations"});
    }

    buttons.push({text: "Recenzije", path: "#reviews"});

    let html = "";
    if (stat !== null) {
        html = <><>
            <Banner caption={houseOwner.firstName + " " + houseOwner.lastName}/>
            <Navigation buttons={
                buttons}
                        editable={myPage} editFunction={handleShow}
                        searchable={true} showProfile={true}
                        showReports={true} type="house"/>

            <AddVacationHouse/>
            <UpdateOwner show={show} setShow={setShow} owner={houseOwner}/>
            <div className='p-5 pt-0'>

                <OwnerInfo
                    name={houseOwner.firstName + " " + houseOwner.lastName}
                    rate={stat.rating}
                    email={houseOwner.email}
                    phoneNum={houseOwner.phoneNumber}
                    address={houseOwner.address}
                    profileImg={houseOwner.profileImg !== null ? backLink + houseOwner.profileImg.path : profilePicturePlaceholder}
                    category={stat.category}
                    points={stat.points}
                />
                <hr/>
                <OwnerHouses myPage={myPage} houses={ownerHouses}/>


            </div>
            <h2 className="me-5 ms-5 mt-5">Kalendar</h2>

            <hr className="me-5 ms-5"/>

            <Calendar events={events} reservable={false}/>
            {myPage &&
                <>


                    <ReservationsToReview type={"vacationHouse"}/>

                    <h2 className="me-5 ms-5 mt-5" id="reservations">Rezervacije</h2>
                    <hr className="me-5 ms-5"/>

                    <ReservationCardGrid reservations={reservations}/>

                </>

            }

            <div className="ms-5 me-5">
                <h2 className="mt-5" id="reviews">Recenzije</h2>
                <hr/>
                <ReviewsComp reviews={ownerReviews}/>
            </div>

            {isLoggedIn() && isClient() &&
                <Complaints type={"vacationHouseOwner"} toWhom={houseOwner.firstName + " " + houseOwner.lastName}/>
            }

            <BeginButton/>
        </>
        </>;
    }

    return html;
}

export default HouseOwnerPage;