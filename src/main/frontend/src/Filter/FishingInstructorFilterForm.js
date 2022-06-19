import React, {useEffect, useState} from "react";
import {Form} from "react-bootstrap";
import {Typeahead} from "react-bootstrap-typeahead";
import axios from "axios";
import {backLink} from "../Consts";

export function FishingInstructorFilterForm({setField}) {

    const [fishingInstructorNames, setFishingInstructorNames] = useState([])

    const fetchInstructorNames = () => {
        axios.get(backLink + "/fishinginstructor/names").then(
            value => {
                setFishingInstructorNames(value.data)
            }
        )
    }
    useEffect(() => {
        fetchInstructorNames()
    }, [])
    return (<>
            <Form.Group>
                <Form.Label>Broj klijenata</Form.Label>
                <Form.Control
                    type="number"
                    onChange={e => setField("numberOfClients", e.target.value)}/>
            </Form.Group>
            <Form.Group>
                <Form.Label>Instruktor</Form.Label>
                <Form.Select onChange={event => {
                    setField("fishingInstructorName", event.target.value)
                }}>
                    <option/>
                    {fishingInstructorNames.map((name, index) => {
                        return <option key={index} value={name}>
                            {name}
                        </option>
                    })}
                </Form.Select>
            </Form.Group>
        </>
    )
}