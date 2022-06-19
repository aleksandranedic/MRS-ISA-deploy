import React from "react";
import {Badge, CloseButton} from "react-bootstrap";

export default function Tag({id, tag, edit, remove}) {
    if (tag !== "") {
        if (edit){
            return (
                    <Badge bg="light" text="primary" className="fw-normal d-inline-flex" style={{padding:"0.4rem", margin: "0.5rem 0.1rem", fontWeight: "thin", borderStyle: "solid", borderWidth:"0.001rem"}}>
                        <p className="align-self-center m-0">
                            {tag}
                        </p>
                        <CloseButton className="align-self-start p-0 m-0 ms-1" style={{width:"9px", height:"9px"}} onClick={ () => remove(id) }/>
                    </Badge>
            )
        }
        else {
            return (
                <Badge bg="light" text="primary" className="fw-normal" style={{padding:"0.4rem", margin: "0.5rem 0.1rem", fontWeight: "thin", borderStyle: "solid", borderWidth:"0.001rem"}}>
                    {tag}
                </Badge>
            )
        }
    }
    else {
        return <></>
    }
}