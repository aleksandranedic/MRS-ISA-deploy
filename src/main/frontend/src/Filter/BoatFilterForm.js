import React, {useEffect, useState} from "react";
import {Form} from "react-bootstrap";
import axios from "axios";
import {backLink} from "../Consts";

export function BoatFilterForm({ setField}) {
    const [boatOwnerNames, setBoatOwnerNames] = useState([])
    const [boatTypes, setBoatTypes] = useState([])

    const fetchBoatTypes = () => {
        axios.get(backLink + "/boat/types").then(
            value => {
                setBoatTypes(value.data)
            }
        )
    }

    const fetchBoatOwner = () => {
        axios.get(backLink + "/boatowner/names").then(
            value => {
                setBoatOwnerNames(value.data)
            }
        )
    }
    useEffect(() => {
        fetchBoatOwner()
        fetchBoatTypes()
    }, [])


    return (<>

        <Form.Group>
            <Form.Label>Tip</Form.Label>
            <Form.Select onChange={event => {
                setField('boatType', event.target.value)
                console.log(event.target.value)
            }}>
                <option/>
                {boatTypes.map((type, index) => {
                    return <option key={index} value={type}>{type}</option>
                })}
            </Form.Select>
        </Form.Group>

        <div className="d-flex">
            <Form.Group className="me-2">
                <Form.Label>Jačina motora</Form.Label>
                <Form.Control type="number"

                              onChange={event => {
                                  setField('boatEnginePower', event.target.value)
                              }}
                />
            </Form.Group>

            <Form.Group className="ms-2">
                <Form.Label>Broj motora</Form.Label>
                <Form.Control type="number"
                              onChange={event => {
                                  setField('boatEngineNum', event.target.value)
                              }}/>
            </Form.Group>
        </div>

        <div className="d-flex">
            <Form.Group className="me-2">
                <Form.Label>Maksimalna brzina</Form.Label>
                <Form.Control type="number"
                              onChange={event => {
                                  setField('boatMaxSpeed', event.target.value)
                              }}/>
            </Form.Group>

            <Form.Group className="ms-2">
                <Form.Label>Kapacitet</Form.Label>
                <Form.Control type="number"
                              onChange={event => {
                                  setField('boatCapacity', event.target.value)
                              }}
                />
            </Form.Group>
        </div>

        <Form.Group>
            <Form.Label>Vlasnik</Form.Label>
            <Form.Select onChange={event => {
                setField('boatOwnerName', event.target.value)
            }}>
                <option/>
                {boatOwnerNames.map((name, index) => {
                    return <option key={index} value={name}>{name}</option>
                })}
            </Form.Select>
        </Form.Group>

    </>)
}