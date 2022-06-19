import {Button, Nav} from "react-bootstrap";
import React from "react";

export default function NavigationButton({text, path}) {
    return (
        <Button variant="outline-" className="border-0 m-0">
            <Nav.Link className="line-dark font-monospace p-1" href={path}>{text}</Nav.Link>
        </Button>


    )
}