import React from 'react';
import {Card} from "react-bootstrap";
import {BsGeoAltFill} from 'react-icons/bs'
import { frontLink } from '../Consts';
function BoatOwnerCard({id, pic, title, text, address}) {
    const url = frontLink + "boat/" + id;
    return (
        <Card className="m-3">
            <a href={url} className="text-decoration-none h-100">
            <Card.Img variant="top" src={pic} style={{minWidth:"100%",maxWidth:"100%",minHeight:"50vh",maxHeight:"50vh"}}/>
            </a>
            <Card.Body className="w-100">
                <Card.Title className="text-dark">
                    <div className="d-flex justify-content-between">
                        {title}
                    </div>
                    </Card.Title>
                <Card.Text className="text-dark">
                    {text}
                </Card.Text>
            </Card.Body>
            <Card.Footer className="w-100">
            <small className="text-muted">
                <BsGeoAltFill/>
                <span className="ms-2">{address}</span>
            </small>
            </Card.Footer>
        </Card>
    );
}

export default BoatOwnerCard;