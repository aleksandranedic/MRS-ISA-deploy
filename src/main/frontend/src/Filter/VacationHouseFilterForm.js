import React, {useEffect, useState} from "react";
import {Form} from "react-bootstrap";
import {Typeahead} from "react-bootstrap-typeahead";
import axios from "axios";
import {backLink} from "../Consts";

export function VacationHouseFilterForm({setField}) {

    const [vacationHouseOwnerNames, setVacationHouseOwnerNames] = useState([])

    const fetchVacationHouseOwnerNames = () => {
        axios.get(backLink + "/houseowner/names ").then(
            response => {
                setVacationHouseOwnerNames(response.data)
            }
        )
    }

    useEffect(() => {
        fetchVacationHouseOwnerNames()
    }, [])
    return (<>
        <Form.Group>
            <Form.Label>Broj soba</Form.Label>
            <Form.Control type="number"
                          onChange={event => setField('numOfVacationHouseRooms', event.target.value)}
            />
        </Form.Group>
        <Form.Group>
            <Form.Label>Broj mesta za spavanje</Form.Label>
            <Form.Control type="number"
                          onChange={event => {
                              setField('numOfVacationHouseBeds', event.target.value)
                          }}
            />
        </Form.Group>
        <Form.Group>
            <Form.Label>Vlasnik</Form.Label>
            <Form.Select onChange={event => {
                setField("vacationHouseOwnerName", event.target.value)
            }}>
                <option/>
                {vacationHouseOwnerNames.map((name, index) => {
                    return <option key={index} value={name} >
                        {name}
                    </option>
                })}
            </Form.Select>
        </Form.Group>

    </>)
}