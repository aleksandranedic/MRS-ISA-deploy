import axios from "axios";
import React, {useEffect, useState} from "react";
import Banner from "../Banner";
import ClientLoyalty from "./ClientLoyalty";
import UpdateClientInfo from "./UpdateClientInfo"
import Navigation from "../Navigation/Navigation";
import {useParams} from "react-router-dom";
import {backLink, profilePicturePlaceholder} from "../Consts";
import {Calendar} from "../Calendar/Calendar";
import {ReservationCardGrid} from "../Calendar/ReservationCardGrid";
import {Collapse} from "react-bootstrap";
import {ReservationsTable} from "../Calendar/ReservationsTable";
import BeginButton from "../BeginButton.js"
import OwnerInfo from "../OwnerInfo";
import {processReservationsForUsers} from "../ProcessToEvent";
import {isMyPage} from "../Autentification";
import SubscriptionCardCarousel from "./SubscriptionCardCarousel";


const UpdateClientInfoComp = ({client, handleClose, showPopUp, setClient}) => {
    if (typeof client.firstName !== "undefined") {
        if (client.profileImg !== null) {

            var profileImg = backLink + client.profileImg.path;
        } else {
            var profileImg = profilePicturePlaceholder;
        }

        return <UpdateClientInfo client={client} handleClose={handleClose} showPopUp={showPopUp} setClient={setClient}
                                 profileImg={profileImg}/>
    } else {
        return <></>
    }
}

const Client = () => {
    const [client, setClient] = useState([]);
    const [reservations, setReservations] = useState([]);
    const [events, setEvents] = useState(null);
    const [adventures, setAdventures] = useState([]);
    const [boats, setBoats] = useState([]);
    const [houses, setHouses] = useState([]);


    const [myPage, setMyPage] = useState(null);


    const {id} = useParams()
    let html;

    const [stat, setStat] = useState(null);

    const fetchStat = () => {
        axios
            .get(backLink + "/review/getStat/" + id)
            .then(res => {
                setStat(res.data);
                console.log(res.data);
            });
    };

    const fetchClient = () => {
        axios.get(backLink + "/client/" + id).then(res => {
            setClient(res.data)
            setMyPage(isMyPage("CLIENT", res.data.id))
        });
    };

    const fetchReservations = () => {

        axios.get(backLink + "/client/reservation/" + id).then(res => {
            setReservations(res.data);
            setEvents(processReservationsForUsers(res.data));
        })

    }

    const fetchAdventures = ()  => {
        axios.get(backLink + "/adventure/subs/" + id).then(res => {
            console.log(res.data);
            setAdventures(res.data);
        })
    }

    const fetchBoats = ()  => {
        axios.get(backLink + "/boat/subs/" + id).then(res => {
            console.log(res.data);
            setBoats(res.data);
        })
    }

    const fetchHouses = ()  => {
        axios.get(backLink + "/house/subs/" + id).then(res => {
            console.log(res.data);
            setHouses(res.data);
        })
    }

    useEffect(() => {
        fetchClient();
        fetchReservations();
        fetchAdventures();
        fetchHouses();
        fetchBoats();
        fetchStat();
    }, []);

    const [show, setShow] = useState(false);

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    const [open, setOpen] = useState(false);

    if (client.length !== 0 && stat !== null) {
        html = (<div>
            <Banner caption={client.firstName + " " + client.lastName}/>
            <UpdateClientInfoComp client={client} handleClose={handleClose} showPopUp={show} setClient={setClient}/>
            <Navigation buttons={
                [
                    {text: "Osnovne informacije", path: "#info"},
                    {text: "Kalendar", path: "#calendar"},
                    {text: "Rezervacije", path: "#reservations"}
                ]}
                        editable={myPage} editFunction={handleShow}
                        searchable={true}/>


            <div id="info"><OwnerInfo
                bio={client.biography}
                name={client.firstName + " " + client.lastName}
                rate={stat.rating}
                email={client.email}
                phoneNum={client.phoneNumber}
                address={client.address}
                profileImg={client.profileImg !== null ? backLink + client.profileImg.path : profilePicturePlaceholder}
                category={stat.category}
                points={stat.points}
                penalty={stat.penalty}
            /></div>
            <ClientLoyalty/>

            {myPage && <>

                <SubscriptionCardCarousel subs={[...houses, ...boats, ...adventures]}/>
                <h2 className="me-5 ms-5 mt-5">Kalendar</h2>

                <hr className="me-5 ms-5"/>
                <Calendar id="calendar" events={events} reservable={false}/>

                <h2 className="me-5 ms-5 mt-5" id="reservations">Rezervacije</h2>
                <hr className="me-5 ms-5"/>

                <ReservationCardGrid reservations={reservations}/>

                <h2 className="me-5 ms-5 mt-5" onClick={() => setOpen(!open)}
                    aria-controls="reservationsTable"
                    aria-expanded={open}
                    style = {{cursor: "pointer"}}
                >Istorija rezervacija</h2>

                <hr className="me-5 ms-5"/>
                <Collapse in={open}>
                    <div id="reservationsTable">
                        <ReservationsTable  reservations={reservations} showResource={false}/>
                    </div>
                </Collapse>
            </>
                }
            <BeginButton/>
        </div>)
    }
    return (html)
};

export function ClientProfilePage() {
    return (
        <>
            <Client/>
        </>
    )
}



