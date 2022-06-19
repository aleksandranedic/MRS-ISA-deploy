import React from 'react';
import {Card} from "react-bootstrap";
import {BsGeoAltFill} from 'react-icons/bs'
import {backLink, frontLink} from "../Consts";
function OwnerHouseCard({id, pic, title, text, address}) {
    const url = frontLink + "house/" + id;
    return (
        <Card className="m-3">
            <a href={url} className="text-decoration-none h-100">
            <Card.Img style={{height: '30vh', width: "100%"}} variant="top" src={pic} />
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

export default OwnerHouseCard;