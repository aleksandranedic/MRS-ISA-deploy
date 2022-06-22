import React, {useEffect, useState} from "react";
import axios from "axios";
import ImagesGallery from "../ImageGallery";
import QuickReservations from "../QuickReservations";
import Banner from "../Banner";
import BoatInfo from "./BoatInfo";
import UpdateBoat from "./UpdateBoat"
import BeginButton from "../BeginButton";
import {useParams} from "react-router-dom";
import "react-image-gallery/styles/css/image-gallery.css";
import Navigation from "../Navigation/Navigation";
import Ratings from "../Reviews/Ratings";
import {backLink, frontLink} from "../Consts";
import {Calendar} from "../Calendar/Calendar";
import {ReservationCardGrid} from "../Calendar/ReservationCardGrid";
import {processReservationsForResources} from "../ProcessToEvent";
import {Complaints} from "../Complaints";
import {isMyPage} from "../Autentification";
import { getProfileLink } from "../Autentification";


const Gallery = ({boat, images}) => {
    if (typeof boat.imagePaths !== "undefined"){
        let empty = images.length === 0;
        for (let i=0; i<boat.imagePaths.length;i++){
            if (!boat.imagePaths[i].includes(backLink)){
                boat.imagePaths[i] = backLink + boat.imagePaths[i];
                images.push({original:boat.imagePaths[i], thumbnail:boat.imagePaths[i]})
            } else if (empty){
                images.push({original:boat.imagePaths[i], thumbnail:boat.imagePaths[i]})   
            }
        }
        return <ImagesGallery images={images}/>
    }
    else {
        return <></>
    }
}
const Update = ({boat, showModal, closeModal}) => {
    if (typeof boat.name !== "undefined"){
        if (boat.navigationEquipment.length === 0){
            boat.navigationEquipment = [{id:0, text:''}]
        }
        if (boat.additionalServices.length === 0){
            boat.additionalServices = [{id:0, text:''}]
        }
        if (boat.fishingEquipment.length === 0){
            boat.fishingEquipment = [{id:0, text:''}]
        }
        return <UpdateBoat closeModal={closeModal} showModal={showModal} boat = {boat}/>
    }
    else {
        return <></>
    }
}

const Reservations = ({reservations, name, address, myPage, additionalServices}) => {
    if (typeof reservations !== "undefined"){
        return <QuickReservations type={"boat"} reservations={reservations} name={name} address={address} additionalServices={additionalServices} entity="boat" priceText="po vožnji" durationText="h" myPage={myPage}
            addable={myPage}
        />
    }
    else {
        return <></>
    }
}

const ReviewsComp = ({reviews}) => {
    if (typeof reviews !== "undefined"){
        return <Ratings reviews = {reviews} type={"boat"}/>
    }
    else {
        return <></>
    }
}


export function BoatProfilePage() {
    const [show, setShow] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
    const {id} = useParams();
    const [boat, setBoat] = useState({});
    const [boatReviews, setBoatReviews] = useState([])
    const [events, setEvents] = useState(null);
    var [imgs, setImgs] = useState([{thumbnail: '', original: ''}]);
    const [myPage, setMyPage] = useState(null);


    const [reservations, setReservations] = useState([]);

    const fetchReservations = () => {
        axios.get(backLink+ "/boat/reservation/boat/" + id).then(res => {
            setReservations(res.data);
            setEvents(processReservationsForResources(res.data, myPage));
        })
    }


    const fetchBoat = () => {
        axios
        .get(backLink + "/boat/boatprof/" + id)
        .then(res => {
            if (res.data === ''){
                var profileLink = getProfileLink();
                window.location.href = frontLink + "pageNotFound"
            }
            setBoat(res.data);
            setImgs([]);
            setMyPage(isMyPage("BOAT_OWNER", res.data.ownerId));

            fetchReviews();
            fetchReservations();
        });
    };

    const fetchReviews = () => {
        axios
        .get(backLink + "/review/getReviews/" + id)
        .then(res => {
            setBoatReviews(res.data);
        });
    };

    useEffect(() => {
        fetchBoat();
    }, []);

    return (
    <>
        <Banner caption={boat.name}/>
        <Navigation buttons={
            [
                {text: "Osnovne informacije", path: "#info"},
                {text: "Fotografije", path: "#gallery"},
                {text: "Akcije", path: "#actions"},
                {text: "Kalendar", path: "#calendar"},
                {text: "Recenzije", path: "#reviews"}
            ]}
                    editable={myPage} editFunction={handleShow} searchable={true}/>
        { typeof boat.name !== "undefined" && <BoatInfo boat={boat}/>}
        <Update closeModal={handleClose} showModal={show} boat = {boat}/>
        <div className='p-5 pt-0'>
            <Gallery boat={boat} images={imgs}/>

            <Reservations myPage={myPage} reservations={boat.quickReservations} name={boat.name} address={boat.address} additionalServices={boat.additionalServices}/>
            <h2 className="mt-5">
                            Kalendar
                        </h2>
                        <hr/>
                        <Calendar myPage={myPage} reservable={true} pricelist={{price: boat.price}} type="boat"
                                  resourceId={id} events={events} additionalServices={boat.additionalServices}
                                  cancellationFee = {boat.cancellationFee} reservations = {reservations}
                        />

            {myPage && <>
                <h2 className="mt-5" id="reservations">Rezervacije</h2>
                <hr/>
                <ReservationCardGrid reservations={reservations}/>
            </>}

            <h2 className="mt-5">
                Recenzije
            </h2>
            <hr/>
            <ReviewsComp reviews = {boatReviews} showAddReview={true}/>

        </div>
        <Complaints type={"boat"} toWhom={boat.name}/>
        <BeginButton/>
    </>

    )
}
export default BoatProfilePage;