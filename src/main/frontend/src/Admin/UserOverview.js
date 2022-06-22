import {Sidebar} from "./Sidebar/Sidebar";
import React, {useEffect, useState} from "react";
import {GetAllVacationHouseOwners} from "../VacationHouseOwnerPage/AllVacationHouseOwners";
import {GetAllFishingInstructors} from "../FishingInstructor/AllFishingInstructors";
import {GetAllBoatOwners} from "../BoatOwnerPage/AllBoatOwners";
import {backLink, frontLink, profilePicturePlaceholder} from "../Consts";

import "./OverviewCard.css"
import {Card, Row, Col, Button} from "react-bootstrap";
import {GetAllAdministrators} from "./AllAdministrators";
import axios from "axios";
import {MessagePopupModal} from "../MessagePopupModal";
import {GetAllClients} from "../ClientPage/AllClients";
import {isLoggedIn} from "../Autentification";
import {AdminForm} from "./AdminForm";

export function UserOverview() {
   // const profilePicturePlaceholder = require("../images/houseIncomeCopy.jpg");
    const [user, setUser] = useState(null);
    const [showRegister, setShowRegister] = useState(false);

    useEffect(() => {
        if (isLoggedIn()) {
            getLoggedUser();
        }
    }, [])


    const getLoggedUser = () => {
        let id = localStorage.getItem("userId");


        axios.get(backLink + "/admin/" + id).then(
            response => {
                setUser(response.data);
            }
        )

    }


    let vacationHouseOwners = GetAllVacationHouseOwners();
    let fishingInstructors = GetAllFishingInstructors();
    let boatOwners = GetAllBoatOwners();
    let admins = GetAllAdministrators();

    let clients = GetAllClients();

    let linkHouseOwner = "";
    let houseOwner = vacationHouseOwners[Math.floor(Math.random() * vacationHouseOwners.length)];
    if (typeof houseOwner != "undefined") {
        linkHouseOwner = houseOwner.profileImg.path !== null ? backLink +houseOwner.profileImg.path : profilePicturePlaceholder;
    }

    let linkBoatOwner = "";
    let boatOwner = boatOwners[Math.floor(Math.random() * boatOwners.length)];
    if (typeof boatOwner != "undefined") {
        linkBoatOwner = boatOwner.profileImg.path !== null ? backLink + boatOwner.profileImg.path: profilePicturePlaceholder;
    }

    let linkFishingInstructor = "";
    let fishingInstructor = fishingInstructors[Math.floor(Math.random() * fishingInstructors.length)];
    if (typeof fishingInstructor != "undefined") {
        linkFishingInstructor = fishingInstructor.profileImg.path !== null ? backLink + fishingInstructor.profileImg.path: profilePicturePlaceholder

    }

    let linkAdmin = "";
    let admin = admins[Math.floor(Math.random() * admins.length)];
    if (typeof admin != "undefined") {
        linkAdmin = admin.profileImg.path !== null ? backLink + admin.profileImg.path : profilePicturePlaceholder;
    }

    let linkClient = "";
    let client = clients[Math.floor(Math.random() * clients.length)];
    if (typeof client != "undefined") {
        linkClient = backLink + client.profileImg.path;
    }

    const [userType, setUserType] = useState("");
    const [showAlert, setShowAlert] = useState(false);

    function deleteFishingInstructor(fishingInstructor) {
        axios.post(backLink + "/fishinginstructor/delete/" + fishingInstructor.id)
            .then(response => {

                    window.location.reload();
                }
            ).catch(error => {
            setShowAlert(true);


        })
    }

    function deleteClient(client) {
        axios.post(backLink + "/client/delete/" + client.id)
            .then(response => {
                    window.location.reload();
                }
            ).catch(error => {
            setShowAlert(true);
        })
    }

    function deleteAdmin(admin) {
        axios.post(backLink + "/admin/delete/" + admin.id)
            .then(response => {

                    window.location.reload();
                }
            ).catch(error => {
            setShowAlert(true);
        })
    }

    function deleteBoatOwner(boatOwner) {
        axios.post(backLink + "/boatowner/delete/" + boatOwner.id)
            .then(response => {

                    window.location.reload();
                }
            ).catch(error => {
            setShowAlert(true);
        })
    }

    function deleteVacationHouseOwner(houseOwner) {
        axios.post(backLink + "/houseowner/delete/" + houseOwner.id)
            .then(response => {

                    window.location.reload();
                }
            ).catch(error => {
            setShowAlert(true);
        })
    }

    let html = "";

    if (user !== null) {
        html = <div className="d-flex" style={{height: "100vh"}}>

            <MessagePopupModal
                show={showAlert}
                setShow={setShowAlert}
                message="Korisnik koga ste pokušali da obrišete ima rezervacije koje se još nisu ostvarile."
                heading="Zabranjeno brisanje"
            />

            <div className="w-25" style={{backgroundColor: "#f7f8f9"}}>
                <Sidebar/>
            </div>
            <div className="w-75 overflow-auto">

                <div className='d-flex m-0 p-0 text-white justify-content-between adminIncome'>
                    <Card id="house" className="fade-in bg-white h-100 border-0 m-2 ms-4" style={{borderRadius: "15PX"}}
                          onClick={() => setUserType("houseOwner")}>
                        <Card.Img src={linkHouseOwner} alt="Card image" className="navigation-card-image-user"/>
                        <Card.ImgOverlay className="d-flex justify-content-center align-items-center">
                            <Card.Title className="display-6 user-card-title" style={{fontWeight: "500"}}>Vlasnik
                                vikendice</Card.Title>
                        </Card.ImgOverlay>
                    </Card>
                    <Card className="fade-in bg-white text-white h-100 border-0 m-2" style={{borderRadius: "15PX"}}
                          onClick={() => setUserType("boatOwner")}>
                        <Card.Img src={linkBoatOwner} alt="Card image" className="navigation-card-image-user"/>
                        <Card.ImgOverlay className="d-flex justify-content-center align-items-center">
                            <Card.Title className="display-6 user-card-title" style={{fontWeight: "500"}}>Vlasnik
                                broda</Card.Title>
                        </Card.ImgOverlay>
                    </Card>
                    <Card className="fade-in bg-white text-white h-100 border-0 m-2" style={{borderRadius: "15PX"}}
                          onClick={() => setUserType("fishingInstructor")}>
                        <Card.Img src={linkFishingInstructor} alt="Card image" className="navigation-card-image-user"/>
                        <Card.ImgOverlay className="d-flex justify-content-center align-items-center">
                            <Card.Title className="display-6 user-card-title" style={{fontWeight: "500"}}>Instruktor
                                pecanja</Card.Title>
                        </Card.ImgOverlay>
                    </Card>
                    <Card className="fade-in bg-white text-white h-100 border-0 m-2" style={{borderRadius: "15PX"}}
                          onClick={() => setUserType("admin")}>
                        <Card.Img src={linkAdmin} alt="Card image" className="navigation-card-image-user"/>
                        <Card.ImgOverlay className="d-flex justify-content-center align-items-center">
                            <Card.Title className="display-6 user-card-title"
                                        style={{fontWeight: "500"}}>Administrator</Card.Title>
                        </Card.ImgOverlay>
                    </Card>
                    <Card className="fade-in bg-white text-white h-100 border-0 m-2" style={{borderRadius: "15PX"}}
                          onClick={() => setUserType("client")}>
                        <Card.Img src={linkClient} alt="Card image" className="navigation-card-image-user"/>
                        <Card.ImgOverlay className="d-flex justify-content-center align-items-center">
                            <Card.Title className="display-6 user-card-title"
                                        style={{fontWeight: "500"}}>Korisnik</Card.Title>
                        </Card.ImgOverlay>
                    </Card>
                </div>

                {userType === "admin" && <div className="m-4">

                    {user.superAdministrator === true &&
                        <div className="mb-4 mt-4">
                            <AdminForm show={showRegister} setShow={setShowRegister} administrator={null}/>
                            <Button onClick={()=>setShowRegister(true)}>Registruj novog administratora</Button>
                        </div>}

                    <Row xs={1} md={3} lg={4} className="g-4 mb-4 w-100">
                        {admins.map((admin, index) => {
                            return <Col key={index + "col"}>
                                <Card key={index}
                                      className="d-flex flex-column justify-content-center align-items-center"
                                      style={{height: "60vh", width: "35vh"}}>

                                    <Card.Img src={admin.profileImg.path !== null ? backLink + admin.profileImg.path: profilePicturePlaceholder} style={{
                                        width: "20vh",
                                        height: "20vh",
                                        objectFit: "cover",
                                        borderRadius: "50%",
                                        marginBottom: "3vh"
                                    }}/>
                                    <Card.Title>
                                        {admin.firstName + " " + admin.lastName}
                                    </Card.Title>

                                    <div className="d-flex ms-5 me-5 flex-column">
                                        <div className="mt-2">Email:</div>
                                        <div style={{fontWeight: "500"}}>
                                            {admin.email}
                                        </div>
                                        <div className="mt-2">Broj telefona:</div>
                                        <div style={{fontWeight: "500"}}>
                                            {admin.phoneNumber}
                                        </div>
                                    </div>
                                    {admin.superAdministrator === false ?
                                        <Button className="mt-3 w-50" onClick={() => deleteAdmin(admin)}
                                                style={{height: "5vh"}}>Obriši</Button>
                                        : <div className="mt-3 w-50" style={{height: "5vh"}}/>
                                    }

                                </Card>
                            </Col>

                        })}
                    </Row>
                </div>}

                {userType === "boatOwner" && <div className="m-4">
                    <Row xs={1} md={3} lg={4} className="g-4 mb-4 w-100">
                        {boatOwners.map((boatOwner, index) => {
                            return <Col key={index + "col"}>
                                <Card key={index}
                                      className="d-flex flex-column justify-content-center align-items-center"
                                      style={{height: "60vh", width: "35vh"}}>

                                    <Card.Img src={boatOwner.profileImg.path !== null ? backLink + boatOwner.profileImg.path: profilePicturePlaceholder} style={{
                                        width: "20vh",
                                        height: "20vh",
                                        objectFit: "cover",
                                        borderRadius: "50%",
                                        marginBottom: "3vh"
                                    }}/>
                                    <Card.Title>
                                        {boatOwner.firstName + " " + boatOwner.lastName}
                                    </Card.Title>

                                    <div className="d-flex ms-5 me-5 flex-column">
                                        <div className="mt-2">Email:</div>
                                        <div style={{fontWeight: "500"}}>
                                            {boatOwner.email}
                                        </div>
                                        <div className="mt-2">Broj telefona:</div>
                                        <div style={{fontWeight: "500"}}>
                                            {boatOwner.phoneNumber}
                                        </div>
                                    </div>
                                    <Button className="mt-3 w-50" onClick={()=>deleteBoatOwner(boatOwner)}>Obriši</Button>

                                </Card>
                            </Col>

                        })}

                    </Row>
                </div>}

                {userType === "fishingInstructor" && <div className="m-4">
                    <Row xs={1} md={3} lg={4} className="g-4 mb-4 w-100">
                        {fishingInstructors.map((fishingInstructor, index) => {
                            return <Col key={index + "col"}>
                                <Card key={index}
                                      className="d-flex flex-column justify-content-center align-items-center"
                                      style={{height: "60vh", width: "35vh"}}>

                                    <Card.Img src={fishingInstructor.profileImg.path !== null ? backLink + fishingInstructor.profileImg.path: profilePicturePlaceholder} style={{
                                        width: "20vh",
                                        height: "20vh",
                                        objectFit: "cover",
                                        borderRadius: "50%",
                                        marginBottom: "3vh"
                                    }}/>
                                    <Card.Title>
                                        {fishingInstructor.firstName + " " + fishingInstructor.lastName}
                                    </Card.Title>

                                    <div className="d-flex ms-5 me-5 flex-column">
                                        <div className="mt-2">Email:</div>
                                        <div style={{fontWeight: "500"}}>
                                            {fishingInstructor.email}
                                        </div>
                                        <div className="mt-2">Broj telefona:</div>
                                        <div style={{fontWeight: "500"}}>
                                            {fishingInstructor.phoneNumber}
                                        </div>
                                    </div>

                                    <Button className="mt-3 w-50"
                                            onClick={() => deleteFishingInstructor(fishingInstructor)}
                                    >Obriši</Button>

                                </Card>
                            </Col>

                        })}
                    </Row>
                </div>}

                {userType === "client" && <div className="m-4">
                    <Row xs={1} md={3} lg={4} className="g-4 mb-4 w-100">
                        {clients.map((client, index) => {
                            return <Col key={index + "col"}>
                                <Card key={index}
                                      className="d-flex flex-column justify-content-center align-items-center"
                                      style={{height: "60vh", width: "35vh"}}>

                                    <Card.Img src={client.profileImg.path !== null ? backLink + client.profileImg.path: profilePicturePlaceholder} style={{
                                        width: "20vh",
                                        height: "20vh",
                                        objectFit: "cover",
                                        borderRadius: "50%",
                                        marginBottom: "3vh"
                                    }}/>
                                    <Card.Title>
                                        {client.firstName + " " + client.lastName}
                                    </Card.Title>

                                    <div className="d-flex ms-5 me-5 flex-column">
                                        <div className="mt-2">Email:</div>
                                        <div style={{fontWeight: "500"}}>
                                            {client.email}
                                        </div>
                                        <div className="mt-2">Broj telefona:</div>
                                        <div style={{fontWeight: "500"}}>
                                            {client.phoneNumber}
                                        </div>
                                    </div>

                                    <Button onClick={()=>deleteClient(client)} className="mt-3 w-50"

                                    >Obriši</Button>

                                </Card>
                            </Col>

                        })}
                    </Row>
                </div>}

                {userType === "houseOwner" && <div className="m-4">
                    <Row xs={1} md={3} lg={4} className="g-4 mb-4 w-100">
                        {vacationHouseOwners.map((houseOwner, index) => {
                            return <Col key={index + "col"}>
                                <Card key={index}
                                      className="d-flex flex-column justify-content-center align-items-center"
                                      style={{height: "60vh", width: "35vh"}}>

                                    <Card.Img src={houseOwner.profileImg.path !== null ? backLink + houseOwner.profileImg.path: profilePicturePlaceholder} style={{
                                        width: "20vh",
                                        height: "20vh",
                                        objectFit: "cover",
                                        borderRadius: "50%",
                                        marginBottom: "3vh"
                                    }}/>
                                    <Card.Title>
                                        {houseOwner.firstName + " " + houseOwner.lastName}
                                    </Card.Title>

                                    <div className="d-flex ms-5 me-5 flex-column">
                                        <div className="mt-2">Email:</div>
                                        <div style={{fontWeight: "500"}}>
                                            {houseOwner.email}
                                        </div>
                                        <div className="mt-2">Broj telefona:</div>
                                        <div style={{fontWeight: "500"}}>
                                            {houseOwner.phoneNumber}
                                        </div>
                                    </div>
                                    <Button className="mt-3 w-50" onClick={()=>deleteVacationHouseOwner(houseOwner)}>Obriši</Button>

                                </Card>
                            </Col>

                        })}
                    </Row>
                </div>}


            </div>
        </div>;
    }

    return html
}