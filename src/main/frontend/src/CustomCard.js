import React, {useState} from "react";
import {Button, Card} from "react-bootstrap";
import {BsGeoAltFill, BsLink45Deg, BsPencilSquare} from "react-icons/bs";
import {AdventureModal} from "./Adventure/AdventureModal";
import {FishingInstructorForm} from "./FishingInstructor/FishingInstructorForm";


export default function CustomCard({imagePath, editable, itemToEdit, title, subtitle, link, type, address}) {
    const [show, setShow] = useState(false);
    const handleShow = () => setShow(true);


    return (
        <Card className="bg-light text-black" style={{width: '18vw', padding: 0}}>
            <a href={link} className="text-decoration-none">
                <img style={{height: '30vh', width: "100%"}}  src={imagePath} alt="Card image"/>
            </a>


            <div className="d-flex">
                <Card.Title className="text-dark h-25 w-75 p-3" id="title">
                    {title}
                </Card.Title>

                {editable && <BsPencilSquare className="m-3" style={{width: '1rem', height: '1.25rem', color: "black", cursor: "pointer"}}
                                             onClick={handleShow}/>}
            </div>

            <Card.Body className="w-100 d-flex">
                <Card.Subtitle className="text-secondary w-100">
                    {subtitle}
                </Card.Subtitle>

            </Card.Body>

            {type === "adventure" && <AdventureModal show={show} setShow={setShow} adventure={itemToEdit}/>}
            {type === "fishingInstructor" && <FishingInstructorForm show={show} setShow={setShow} fishingInstructor={itemToEdit}/>}
            <Card.Footer className="w-100">
                <small className="text-muted">
                    <BsGeoAltFill/>
                    <span className="ms-2">{address}</span>
                </small>
            </Card.Footer>

        </Card>
    )
}