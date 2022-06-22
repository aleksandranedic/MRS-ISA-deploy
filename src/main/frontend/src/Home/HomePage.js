import "../Home/HomePage.css"
import {Button, Card, Form} from "react-bootstrap";
import React, {useState} from "react";
import {GetAllVacationHouses} from "../VacationHousePage/AllVacationHouses";
import {GetAllAdventures} from "../Adventure/AllAdventures";
import {GetAllBoats} from "../BoatPage/AllBoats";
import {backLink, frontLink, logOut} from "../Consts";
import "../complaints.css"
import {GetAllVacationHouseOwners} from "../VacationHouseOwnerPage/AllVacationHouseOwners";
import {GetAllFishingInstructors} from "../FishingInstructor/AllFishingInstructors";
import {GetAllBoatOwners} from "../BoatOwnerPage/AllBoatOwners";
import {BsPerson, BsSearch} from "react-icons/bs";
import {getProfileLink, isLoggedIn} from "../Autentification";

export function HomePage() {

    const [searchTerm, setSearchTerm] = useState("searchTerm");

    let vacationHouses = GetAllVacationHouses();
    if (vacationHouses.length > 6) {
        vacationHouses = vacationHouses.slice(0, 6);
    }

    let vacationHouseOwners = GetAllVacationHouseOwners();

    if (vacationHouseOwners.length > 4) {
        vacationHouseOwners = vacationHouseOwners.slice(0, 4);
    }

    let fishingInstructors = GetAllFishingInstructors();
    if (fishingInstructors.length > 4) {
        fishingInstructors = fishingInstructors.slice(0, 4);
    }

    let adventures = GetAllAdventures();
    if (adventures.length > 6) {
        adventures = adventures.slice(0, 6);
    }
    let boats = GetAllBoats();
    if (boats.length > 6) {
        boats = boats.slice(0, 6);
    }

    let boatOwners = GetAllBoatOwners();
    if (boatOwners.length > 4) {
        boatOwners = boatOwners.slice(0, 4);
    }



    let profileLink = getProfileLink();

    return (<div className="page">
        <div id="hero">

            <div className="w-100 d-flex justify-content-end align-items-center">

                {(isLoggedIn()) &&

                    <a href={profileLink} className="m-3">
                        <BsPerson className="icon"/>
                    </a>}

                {(!isLoggedIn()) ?
                    <Button href={frontLink + "login"} className="m-3" variant="outline-secondary">Prijavi se</Button> :
                    <Button className="m-3" variant="outline-secondary" onClick={() => logOut()}>Odjavi se</Button>}

            </div>

            <div>
                <p className="logo display-3 text-light">Savana</p>
                <p className="subtitle display-6 text-light">Vreme je za odmor. Obradujte sebe i svoje voljene nekom od
                    ponuda iz naše kolekcije.</p>
            </div>

            <div className="search d-flex align-items-center w-100">

                <Form.Control className="search-bar" type="text" onChange={e => setSearchTerm(e.target.value)}/>


                <a href={frontLink + "search/" + searchTerm}>
                    <BsSearch className="icon"/>
                </a>
            </div>

            <div className="d-flex justify-content-center">
                <div className="resources d-flex mt-auto mb-2">

                    <div className="resource d-flex">
                        <img className="resource-img" src={require("../images/homepage/pexels-pixabay-290518.jpg")}/>
                        <div>
                            <a style={{color: "#3f3f3f", textDecoration: "none"}} href="#vacationHomes">
                                <h1>VIKENDICE</h1>
                            </a>

                            <p>Pronađite najbolje vile, vikendice, kuće i splavove za iznajmljivanje, za vaš savršeni
                                odmor,
                                okupljanje ili događaj.</p>


                            <div className="d-flex w-100 justify-content-end">
                                <Button href={frontLink + "houses"} variant="outline-secondary"
                                        className="me-2 move-button">PREGLEDAJ</Button>
                            </div>

                        </div>

                    </div>
                    <div className="resource d-flex">
                        <img className="resource-img" src={require("../images/homepage/pexels-bianca-754355.jpg")}/>
                        <div>
                            <a style={{color: "#3f3f3f", textDecoration: "none"}} href="#boats">
                            <h1>BRODOVI</h1></a>
                            <p>Ukoliko vaša ideja savršenog odmora uključuje boravak na vodenoj površini,
                                pronađite jahtu, čamac ili brod koji bi ispunio vaš ideal.</p>
                            <div className="d-flex w-100 justify-content-end">
                                <Button href={frontLink + "boats"} variant="outline-secondary"
                                        className="me-2 move-button">PREGLEDAJ</Button>
                            </div>
                        </div>

                    </div>
                    <div className="resource d-flex">
                        <img className="resource-img" src={require("../images/homepage/pexels-pixabay-39854.jpg")}/>
                        <div>
                            <a style={{color: "#3f3f3f", textDecoration: "none"}} href="#adventures">
                                <h1>AVANTURE</h1></a>
                            <p>Želite stručno mišljenje i da vas provede kroz najrazličitije pecaroške destinacije?
                                Pogledajte avanture koje vam nude baš to iskustvo.</p>
                            <div className="d-flex w-100 justify-content-end">
                                <Button href={frontLink + "adventures"} variant="outline-secondary"
                                        className="me-2 move-button">PREGLEDAJ</Button>
                            </div>

                        </div>
                    </div>


                </div>
            </div>

        </div>
        <div className="main-content">

            <div id="vacationHomes" className="links">
                <div className="background">
                    <p className="bg-text">VIKENDICE</p>
                </div>
                <div className="content d-flex justify-content-center align-items-center w-100">
                    {vacationHouses.map(vacationHouse => {
                        return <HomePageResourceCard type={"house"} key={vacationHouse.id} id={vacationHouse.id}
                                                     title={vacationHouse.title} images={vacationHouse.images}
                                                     rate={4.5}/>
                    })}
                </div>

                <div className="vendor-banner vacation-house-owner-banner"/>

                <div className="d-flex justify-content-center align-items-center w-100">
                    {vacationHouseOwners.map(owner => {
                        return <HomePageVendorCard type={"houseOwner"} key={owner.id} id={owner.id}
                                                   fullName={owner.firstName + " " + owner.lastName} rate={5}
                                                   profileImage={owner.profileImg}/>
                    })}
                </div>
            </div>
            <div id="boats" className="links">
                <div className="background">
                    <p className="bg-text">BRODOVI</p>
                </div>
                <div className="content d-flex justify-content-center align-items-center w-100">
                    {boats.map(boat => {
                        return <HomePageResourceCard type={"boat"} key={boat.id} id={boat.id} title={boat.title}
                                                     images={boat.images} rate={2.7}/>

                    })}
                </div>

                <div className="vendor-banner boat-owner-banner"/>

                <div className="d-flex justify-content-center align-items-center w-100">
                    {boatOwners.map(owner => {
                        return <HomePageVendorCard type={"boatOwner"} key={owner.id} id={owner.id}
                                                   fullName={owner.firstName + " " + owner.lastName} rate={5}
                                                   profileImage={owner.profileImg}/>
                    })}
                </div>


            </div>
            <div id="adventures" className="links">
                <div className="background">
                    <p className="bg-text">AVANTURE</p>
                </div>
                <div className="content d-flex justify-content-center align-items-center w-100">
                    {adventures.map(adventure => {
                        return <HomePageResourceCard type={"adventure"} key={adventure.id} id={adventure.id}
                                                     title={adventure.title} images={adventure.images} rate={3.0}/>
                    })}
                </div>

                <div className="vendor-banner fishing-instructor-banner"/>

                <div className="d-flex justify-content-center align-items-center w-100">
                    {fishingInstructors.map(owner => {
                        return <HomePageVendorCard type={"fishingInstructor"} key={owner.id} id={owner.id}
                                                   fullName={owner.firstName + " " + owner.lastName} rate={5}
                                                   profileImage={owner.profileImg}/>
                    })}
                </div>
            </div>


        </div>
        <div className="page filler"/>


    </div>)
}

function HomePageResourceCard({id, title, rate, images, type}) {

    return <Card className="bg-dark text-white links-card reveal">
        <Card.Img src={backLink + images.at(0).path} alt="Card image"/>
        <Card.ImgOverlay>
            <Card.Title><a
                href={frontLink + type + "/" + id}>{title}</a></Card.Title>


        </Card.ImgOverlay>
    </Card>
}

function HomePageVendorCard({id, fullName, rate, profileImage, type}) {
    let path;
    if (profileImage) {
        path = backLink + profileImage.path;
    }

    return <>
        <Card className="vendor-card m-5 reveal">
            <Card.Body className="d-flex h-100">
                <div>
                    <a href={frontLink + type + "/" + id}>
                        {profileImage ? <img src={path} alt="profileImage" className="vendor-profile-image"/> : <div
                            className="d-flex justify-content-center align-items-center vendor-profile-image-placeholder">
                            <BsPerson style={{color: "rgba(0,0,0,0.3)"}}/>

                        </div>}
                    </a>
                </div>
                <div className="ms-3 mt-auto mb-auto flex-column h-100">
                    <h6 className="vendor-name">{fullName}</h6>

                </div>
            </Card.Body>
        </Card>

    </>
}

function reveal() {
    let reveals = document.querySelectorAll(".reveal");

    for (let i = 0; i < reveals.length; i++) {
        let windowHeight = window.innerHeight;
        let elementTop = reveals[i].getBoundingClientRect().top;
        let elementVisible = 150;

        if (elementTop < windowHeight - elementVisible) {
            reveals[i].classList.add("active");
        } else {
            reveals[i].classList.remove("active");
        }
    }
}

window.addEventListener("scroll", reveal);