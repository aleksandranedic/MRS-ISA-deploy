import React, {useState, useEffect} from 'react'
import {} from 'bootstrap'
import {Button, Card} from 'react-bootstrap'
import {TagInfo} from "./Info";
import {BsPencilSquare, BsPeople} from "react-icons/bs";
import {AiOutlineCalendar, AiOutlineClockCircle} from "react-icons/ai";
import UpdateQuickReservation from './UpdateQuickReservation';
import axios from "axios";
import {backLink, notifyError, notifySuccess} from "./Consts";
import {isLoggedIn} from "./Autentification";

function QuickReservation({reservation,availableTags, name, address, image, entity, durationText, type, myPage}) {
    const [state, setState] = useState({
        startDate: '',
        price: '',
        numberOfPeople: '',
        duration: '',
        discount: '',
        additionalServices: [{id: '', text: ''}]
    });
    const [show, setShow] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
    const [loggedUser, setLoggedUser] = useState(null);


    const getLoggedUser = () => {
        if (isLoggedIn()) {
            axios.get(backLink + "/getLoggedUser").then(
                res => {
                    setLoggedUser(res.data);
                }
            )
        }

    }

    useEffect(() => {

        setState(reservation);
        getLoggedUser();
    }, []);

    const addReservation = () => {
        let dto = {
            reservationID: reservation.reservationID,
            clientID: loggedUser.id

        }


        if (type === "adventure") {
            axios.post(backLink + "/adventure/quickReservations/reserve", dto)
            .then(res => {
                if (res.data === ''){
                    notifyError("Neuspešno. Molimo Vas pokušajte ponovo.")
                }
                else if (res.data === -1){
                    notifyError("Akcija je rezervisana, molimo Vas izaberite drugu.")
                }              
                else {
                    notifySuccess("Akcija rezervisana.");
                    setTimeout(function () {window.location.reload();}, 3000)    
                }
            }).catch(error => {
                console.log(error.message);
            })
        }
        else if (type === "vacationHouse") {
            axios.post(backLink + "/house/quickReservations/reserve", dto)
            .then(res => {
                if (res.data === ''){
                    notifyError("Neuspešno. Molimo Vas pokušajte ponovo.")
                }
                else if (res.data === -1){
                    notifyError("Akcija je rezervisana, molimo Vas izaberite drugu.")
                }              
                else {
                    notifySuccess("Akcija rezervisana.");
                    setTimeout(function () {window.location.reload();}, 3000)         
                }
            }).catch(error => {
                console.log(error.message);
            })
        }
        else if (type === "boat") {
            axios.post(backLink + "/boat/quickReservations/reserve", dto)
            .then(res => {
                if (res.data === ''){
                    notifyError("Neuspešno. Molimo Vas pokušajte ponovo.")
                }
                else if (res.data === -1){
                    notifyError("Akcija je rezervisana, molimo Vas izaberite drugu.")
                }              
                else {
                    notifySuccess("Akcija rezervisana.");
                    setTimeout(function () {window.location.reload();}, 3000)        
                }
            }).catch(error => {
                console.log(error.message);
            })
        }

    }

    return (
        <Card className="houseQuickReservationCard m-3 mt-4 me-5" style={{borderRadius: "15PX", minWidth: "45vh"}}>
            <Card.Img variant="top" src={require("./images/loginBackground.jpg")}
                      style={{borderRadius: "15px 15px 0px 0px", maxHeight: "28vh", minHeight: "28vh"}}/>
            <Card.ImgOverlay className="p-2">
                <div className='w-100 d-flex justify-content-end m-0 p-0'>
                    <div className='rounded m-0 p-0 d-flex justify-content-center align-items-center'
                         style={{width: "25%", backgroundColor: "#ED7301"}}>
                        <p className='fw-bold text-lead text-light m-1 me-0 p-0'>{state.discount}</p>
                        <p className='fw-bold text-lead text-light m-0 p-0'>% off</p>
                    </div>
                </div>
            </Card.ImgOverlay>
            <Card.Body>
                <div className='d-flex justify-content-between'>
                    <Card.Title className='mb-1'>{name}</Card.Title>


                        {myPage &&
                        <button onClick={handleShow} style={{zIndex: "3", border: "0", background: "transparent"}}>
                            <BsPencilSquare/></button>
                        }

                </div>
                <div>
                    <div className="d-flex flex-column">
                        <small className='text-secondary'>{address}</small>
                        <div className="d-flex justify-content-center mt-4">
                            <span className="d-flex">
                                <AiOutlineCalendar className="me-1" color="#0d6efd"/>
                                <p className="m-0 p-0" style={{fontSize: "13px"}}>{state.startDate}</p>
                            </span>
                            <span className="d-flex ms-3 me-3">
                                <AiOutlineClockCircle className="me-1" color="#0d6efd"/>
                                <p className="m-0 p-0 pe-1" style={{fontSize: "13px"}}>{state.duration}</p>
                                <p className="m-0 p-0" style={{fontSize: "13px"}}>{durationText}</p>
                            </span>
                            <span className="d-flex">
                                <BsPeople className="me-1" color="#0d6efd"/>
                                <p className="m-0 p-0 pe-1" style={{fontSize: "13px"}}>{state.numberOfPeople}</p>
                                <p className="m-0 p-0" style={{fontSize: "13px"}}>ljudi</p>
                            </span>
                        </div>
                        <div>
                            <div className="mt-3 mb-3 d-flex justify-content-center" style={{minHeight: "6vh"}}>
                                <TagInfo tagList={state.additionalServices} edit={false}/>
                            </div>
                        </div>
                        <div className='d-flex justify-content-between'>
                            <div className="d-flex">
                                <span className='d-flex' style={{height: "auto"}}>
                                    <p className="text-lead fw-bold m-0 d-flex align-items-end"
                                       style={{fontSize: "30px", color: "#ED7301"}}>€</p>
                                    <p className="text-lead m-0"
                                       style={{fontSize: "30px", color: "#ED7301"}}>{state.price}</p>
                                </span>

                            </div>

                            {isLoggedIn() && localStorage.getItem("userRoleName") === "CLIENT" &&
                                <Button onClick={() => addReservation()}
                                        className="btn btn-primary align-self-center h-75"
                                        style={{zIndex: "3"}}>Rezerviši</Button>
                            }
                        </div>
                    </div>
                </div>
            </Card.Body>
            {state.discount !== '' ?
                (<UpdateQuickReservation state={state} setState={setState} closeModal={handleClose} showModal={show}
                                         entity={entity} durationText={durationText} availableTags={availableTags}/>) : <></>
            }
        </Card>
    )
}

export default QuickReservation;