import {Sidebar} from "./Sidebar/Sidebar";
import React, {useState} from "react";
import {backLink, frontLink} from "../Consts";
import {Button, Card} from "react-bootstrap";
import {GetAllVacationHouses} from "../VacationHousePage/AllVacationHouses";
import {GetAllAdventures} from "../Adventure/AllAdventures";
import {GetAllBoats} from "../BoatPage/AllBoats";
import "./OverviewCard.css"

import {AddressInfo, Info} from "../Info";
import axios from "axios";
import {MessagePopupModal} from "../MessagePopupModal";

export function ResourceOverview() {

    let vacationHouses = GetAllVacationHouses();
    let adventures = GetAllAdventures();
    let boats = GetAllBoats();


    let linkHouse = "";
    let house = vacationHouses[Math.floor(Math.random() * vacationHouses.length)];
    if (typeof house != "undefined") {
        linkHouse = backLink + house.images[Math.floor(Math.random() * house.images.length)].path;
    }
    let linkBoat = "";
    let boat = boats[Math.floor(Math.random() * boats.length)];
    if (typeof boat != "undefined") {
        linkBoat = backLink + boat.images[Math.floor(Math.random() * boat.images.length)].path;
    }
    let linkAdventure = "";
    let adventure = adventures[Math.floor(Math.random() * adventures.length)];
    if (typeof adventure != "undefined") {

        linkAdventure = backLink + adventure.images[Math.floor(Math.random() * adventure.images.length)].path;
    }

    const [showAlert, setShowAlert] = useState(false);


    function deleteAdventure(adventure) {
        axios.post(backLink + "/adventure/delete/" + adventure.id)
            .then(response => {
                window.location.reload();
            }
        ).catch(error => {
            setShowAlert(true);

        })
    }

    const deleteHouse = (house) => {
        axios.get(backLink + "/house/delete/" + house.id)
            .then(response => {
                    window.location.reload();
                }
            ).catch(error => {
            setShowAlert(true);

        })
    }

    const deleteBoat = (boat) => {
        axios.get(backLink + "/boat/delete/" + boat.id)
            .then(response => {
                    window.location.reload();
                }
            ).catch(error => {
            setShowAlert(true);

        })


    }

    const [resourceType, setResourceType] = useState("");

    return <div className="d-flex" style={{height: "100vh"}}>

        <MessagePopupModal
            show={showAlert}
            setShow={setShowAlert}
            message="Resurs koji ste pokušali da obrišete sadrži rezervacije koje se još nisu ostvarile."
            heading="Zabranjeno brisanje"
        />

        <div className="w-25" style={{backgroundColor: "#f7f8f9"}}>
            <Sidebar/>
        </div>


        <div className="w-75 overflow-auto">
            <div className='d-flex m-0 p-0 text-white justify-content-between adminIncome'>
                <Card id="house" className="bg-white h-100 border-0 m-2 ms-4" style={{borderRadius: "15PX"}}
                      onClick={() => setResourceType("vacationHouse")}>
                    <Card.Img src={linkHouse} alt="Card image" className="navigation-card-image-resources"/>
                    <Card.ImgOverlay className="d-flex justify-content-center align-items-center">
                        <Card.Title className="display-6" style={{fontWeight: "500"}}>Vikendice</Card.Title>
                    </Card.ImgOverlay>
                </Card>
                <Card id="boat" className="bg-white text-white h-100 border-0 m-2" style={{borderRadius: "15PX"}}
                      onClick={() => setResourceType("boat")}>
                    <Card.Img src={linkBoat} alt="Card image" className="navigation-card-image-resources"/>
                    <Card.ImgOverlay className="d-flex justify-content-center align-items-center">
                        <Card.Title className="display-6" style={{fontWeight: "500"}}>Brodovi</Card.Title>
                    </Card.ImgOverlay>
                </Card>
                <Card id="adventure" className="bg-white text-white h-100 border-0 m-2" style={{borderRadius: "15PX"}}
                      onClick={() => setResourceType("adventure")}>
                    <Card.Img src={linkAdventure} alt="Card image" className="navigation-card-image-resources"/>
                    <Card.ImgOverlay className="d-flex justify-content-center align-items-center">
                        <Card.Title className="display-6" style={{fontWeight: "500"}}>Avanture</Card.Title>
                    </Card.ImgOverlay>
                </Card>
            </div>

            <div className="m-4">

                {resourceType === "adventure" && <div>
                    {adventures.map((adventure, index) => {
                        return <Card key={index} className="d-flex m-3"
                                     style={{borderRadius: "0", border: "none"}}>

                            <Card.Img className="overview-image" src={backLink + adventure.images[0].path}
                            />

                            <Card.ImgOverlay className="overlay">
                                <div className="d-flex m-5 mb-0">
                                    <a className="text-decoration-none d-flex "
                                       href={frontLink + "adventure/" + adventure.id}>
                                        <h1 className="title-link text-light ">{adventure.title}</h1>

                                    </a>
                                    <Button variant="outline-light" className="delete-button"
                                    onClick={()=>deleteAdventure(adventure)}
                                    >Obriši</Button>
                                </div>

                                <hr className="ms-4 me-4" style={{color: "white"}}/>
                                <div className="m-2 text-light w-100">
                                    <div className="ms-5 mt-0">
                                        <h5>{adventure.description}</h5>
                                    </div>

                                    <div className="container">
                                        <div className="row text-center">
                                            <div className="col-3">
                                                <Info title="Vlasnik"
                                                      text={adventure.owner.firstName + " " + adventure.owner.lastName}/>

                                            </div>
                                            <div className="col-3">
                                                <Info title={"Broj klijenata"} text={adventure.numberOfClients}/>

                                            </div>
                                            <div className="col-3">
                                                <Info title="Pravila ponasanja" text={adventure?.rulesAndRegulations}/>

                                            </div>
                                            <div className="col-3">
                                                <AddressInfo className="ms-auto" title={"Adresa"}
                                                             address={adventure.address}/>

                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </Card.ImgOverlay>

                        </Card>
                    })

                    }
                </div>}

                {resourceType === "vacationHouse" && <div>
                    {vacationHouses.map((house, index) => {
                        return <Card key={index} className="m-2 d-flex"
                                     style={{borderRadius: "0", border: "none"}}>

                            <Card.Img className="overview-image" src={backLink + house.images[0].path}
                            />
                            <Card.ImgOverlay className="overlay">
                                <div className="d-flex m-5 mb-0">
                                    <a className="text-decoration-none d-flex " href={frontLink + "house/" + house.id}>
                                        <h1 className="title-link text-light ">{house.title}</h1>

                                    </a>
                                    <Button variant="outline-light" className="delete-button" onClick={()=>deleteHouse(house)}>Obriši</Button>
                                </div>
                                <hr className="ms-4 me-4" style={{color: "white"}}/>

                                <div className="m-2 text-light w-100">
                                    <div className="ms-5 mt-0">
                                        <h5>{house.description}</h5>
                                    </div>
                                    <div className="container">
                                        <div className="row text-center">
                                            <div className="col-3">
                                                <Info title="Pravila ponasanja" text={house.rulesAndRegulations}/>
                                            </div>
                                            <div className="col-3">
                                                <AddressInfo className="ms-auto" title={"Adresa"}
                                                             address={house.address}/>
                                            </div>
                                            <div className="col-3">
                                                <Info title={"Broj soba"} text={house.numberOfRooms}/>
                                            </div>
                                            <div className="col-3">
                                                <Info title={"Broj kreteveta po sobi"}
                                                      text={house.numberOfBedsPerRoom}/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </Card.ImgOverlay>


                        </Card>
                    })}
                </div>}


                {resourceType === "boat" && <div>
                    {boats.map((boat, index) => {
                        return <Card key={index} className="m-2 d-flex"
                                     style={{borderRadius: "0", border: "none"}}>

                            <Card.Img className="overview-image" src={backLink + boat.images[0].path}
                            />
                            <Card.ImgOverlay className="overlay">
                                <div className="d-flex m-5 mb-0">
                                    <a className="text-decoration-none d-flex " href={frontLink + "house/" + boat.id}>
                                        <h1 className="title-link text-light ">{boat.title}</h1>

                                    </a>
                                    <Button variant="outline-light" className="delete-button" onClick={()=>deleteBoat(boat)}>Obriši</Button>
                                </div>
                                <hr className="ms-4 me-4" style={{color: "white"}}/>

                                <div className="m-2 text-light w-100">
                                    <div className="ms-5 mt-0">
                                        <h5>{boat.description}</h5>
                                    </div>
                                    <div className="container">
                                        <div className="row text-center">
                                            <div className="col-3">
                                                <Info title="Pravila ponasanja" text={boat.rulesAndRegulations}/>
                                            </div>
                                            <div className="col-3">
                                                <AddressInfo className="ms-auto" title={"Adresa"}
                                                             address={boat.address}/>
                                            </div>
                                            <div className="col-3">
                                                <Info title={"Tip"} text={boat.type}/>
                                            </div>
                                            <div className="col-3">
                                                <Info title={"Kapacitet"} text={boat.capacity}/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </Card.ImgOverlay>


                        </Card>
                    })}
                </div>}
            </div>


        </div>
    </div>
}
