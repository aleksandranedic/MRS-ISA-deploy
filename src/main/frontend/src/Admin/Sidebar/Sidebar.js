import {Button, Nav, Navbar} from "react-bootstrap";
import React from 'react';
import 'bootstrap/dist/css/bootstrap.css'
import {BsArrowRight} from "react-icons/bs";
import {frontLink, logOut, logOutAdmin} from "../../Consts";

export function Sidebar() {

    return (<div className="flex-column"><Navbar bg="light" expand="lg">
        <Nav className="d-flex flex-column m-3 align-items-stretch">
            <a href={frontLink+"admin"} style={{textDecoration:"none", color:"black"}}>
                <h2 className="fw-light m-3">
                    {localStorage.getItem("firstName") + " " + localStorage.getItem("lastName")}
                </h2>
            </a>

            <h4 className="fw-light m-3">Zahtevi</h4>

            <Nav.Link className="ms-4" href={frontLink+"admin/registrationRequests"}>
                <BsArrowRight className="me-2"/>
                Zahtevi za registraciju
            </Nav.Link>

            <Nav.Link className="ms-4" href={frontLink+"admin/deletionRequests"}>
                <BsArrowRight className="me-2"/>
                Zahtevi za brisanje naloga
            </Nav.Link>

            <Nav.Link className="ms-4" href={frontLink+"admin/penaltyRequests"}>
                <BsArrowRight className="me-2"/>
                Zahtevi za recenzije pružaoca usluga
            </Nav.Link>

            <Nav.Link className="ms-4" href={frontLink+"admin/reviewRequests"}>
                <BsArrowRight className="me-2"/>
                Zahtevi za korisničke recenzije
            </Nav.Link>

            <Nav.Link className="ms-4" href={frontLink+"admin/complaints"}>
                <BsArrowRight className="me-2"/>
                Žalbe
            </Nav.Link>

            <Nav.Link className="ms-4" href={frontLink+"admin/reports"}>
                <BsArrowRight className="me-2"/>
                Izveštaji
            </Nav.Link>

            <Nav.Link className="ms-4" href={frontLink+"admin/resources"}>
                <BsArrowRight className="me-2"/>
                Pregled resursa
            </Nav.Link>

            <Nav.Link className="ms-4" href={frontLink+"admin/users"}>
                <BsArrowRight className="me-2"/>
                Pregled korisnika
            </Nav.Link>

            <h4 className="fw-light ms-3">Loyalti program</h4>

            <Nav.Link className="ms-4" href={frontLink + "admin/categories"}>
                <BsArrowRight className="me-2"/>
                Kategorije
            </Nav.Link>

            <Nav.Link className="ms-4" href={frontLink + "admin/points"}>
                <BsArrowRight className="me-2" />
                Bodovi
            </Nav.Link>
        </Nav>
    </Navbar>
        <Button className="m-3" variant="outline-secondary" onClick={() => logOutAdmin()}>Odjavi se</Button>
    </div>)
}