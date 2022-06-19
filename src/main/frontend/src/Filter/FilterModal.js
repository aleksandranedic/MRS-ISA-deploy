import {Accordion, Button, Form, Modal} from "react-bootstrap";

import React, {useEffect, useState} from "react";
import {ResourceFilterForm} from "./ResourceFilterForm";
import {VacationHouseFilterForm} from "./VacationHouseFilterForm";
import {BoatFilterForm} from "./BoatFilterForm";
import {FishingInstructorFilterForm} from "./FishingInstructorFilterForm";
import DropdownMultiselect from "react-multiselect-dropdown-bootstrap";
import {backLink} from "../Consts";
import axios from "axios";
import {DateTimePickerComponent} from "@syncfusion/ej2-react-calendars";
import {Slider} from "@material-ui/core";

export function FilterModal({updateResults, showFilters, setShowFilters}) {
    const handleClose = () => setShowFilters(false);
    const [adventuresChecked, setAdventuresChecked] = useState(true);
    const [vacationHousesChecked, setVacationHousesChecked] = useState(true);
    const [boatsChecked, setBoatsChecked] = useState(true);

    const [location, setLocations] = useState([])

    const sortingOptions = [
        'po imenu vikendice opadajuće',
        'po imenu vikendice rastuće',
        'po imenu avanture opadajuće',
        'po imenu avanture rastuće',
        'po imenu broda opadajuće',
        'po imenu broda rastuće'
    ]

    const [formValuesInput, setFormValuesInput] = useState({
        numberOfClients: "",
        fishingInstructorName: "",
        numOfVacationHouseRooms: "",
        numOfVacationHouseBeds: "",
        vacationHouseOwnerName: "",
        boatType: "",
        boatEnginePower: "",
        boatEngineNum: "",
        boatMaxSpeed: "",
        boatCapacity: "",
        boatOwnerName: "",
        startDate: "",
        endDate: "",
        startTime: "",
        endTime: "",
        location: "",
        reviewRating: 0,
        cancellationFee: false,
        priceRange:[50,3000],
        sort: []
    });

    function filterData() {
        formValuesInput.adventuresChecked = adventuresChecked
        formValuesInput.vacationHousesChecked = vacationHousesChecked
        formValuesInput.boatsChecked = boatsChecked
        updateResults(formValuesInput)
        handleClose()
    }

    const setField = (fieldName, value) => {
        setFormValuesInput({
            ...formValuesInput,
            [fieldName]: value
        })
        console.log(formValuesInput);
    }

    const fetchLocationNames = () => {
        let array = [];
        axios.get(backLink + "/adventure/address").then(
            response => {
                for (let i = 0; i < response.data.length; i++) {
                    if (!array.includes(response.data[i]))
                        array.push(response.data[i])
                }
            }
        )
        axios.get(backLink + "/boat/address").then(
            response => {
                for (let i = 0; i < response.data.length; i++) {
                    if (!array.includes(response.data[i]))
                        array.push(response.data[i])
                }
            }
        )
        axios.get(backLink + "/house/address").then(
            response => {
                for (let i = 0; i < response.data.length; i++) {
                    if (!array.includes(response.data[i]))
                        array.push(response.data[i])
                }
                setLocations(array)
            }
        )

    }

    useEffect(() => {
        fetchLocationNames()
    }, [])

    const updateRating = (e, data) => {
        setField('reviewRating', data)
    }

    return (
        <Modal show={showFilters} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>Filteri</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Form.Group className="m-3">
                        <div className="d-flex w-100 my-2">
                            <Form.Group className="me-2 w-50 mt-2">
                                <Form.Label>Početni datum</Form.Label>
                                <Form.Control type="date"
                                              value={formValuesInput.startDate}
                                              onChange={e => setField("startDate", e.target.value)}
                                />
                                <Form.Label>Vreme početka</Form.Label>
                                <Form.Control type="time" min="05:00" max="20:00"
                                              value={formValuesInput.startTime}
                                              onChange={e => setField("startTime", e.target.value)}
                                />
                            </Form.Group>
                            <Form.Group className="ms-2 w-50 mt-2">
                                <Form.Label>Završni datum</Form.Label>
                                <Form.Control type="date"
                                              value={formValuesInput.endDate}
                                              onChange={e => setField("endDate", e.target.value)}
                                />
                                <Form.Label>Vreme zavrsetka</Form.Label>
                                <Form.Control type="time" min="05:00" max="20:00"
                                              value={formValuesInput.endTime}
                                              onChange={e => setField("endTime", e.target.value)}
                                />
                            </Form.Group>

                        </div>
                        <Form.Label>Izaberite lokaciju</Form.Label>
                        <Form.Select
                            onChange={event => {
                                setField("location", event.target.value)
                            }}
                        >
                            <option/>
                            {location.map((address, index) => {
                                return <option key={index} value={address}>
                                    {address}
                                </option>
                            })
                            }
                        </Form.Select>
                        <Form.Label>Ocena recenzije</Form.Label>
                        <Slider
                            value={formValuesInput.reviewRating}
                            onChange={updateRating}
                            valueLabelDisplay="auto"
                            step={0.1}
                            min={0}
                            max={5}
                            style={{color: "#0d6efc"}}
                        />
                    </Form.Group>
                    <ResourceFilterForm minimumValue={50} maximumValue={3000} setField={setField}/>
                    <Accordion>
                        <Accordion.Item eventKey="0">
                            <Accordion.Header>
                                <Form.Check
                                    type="switch"
                                    id="adventure-switch"
                                    label="Avanture"
                                    defaultChecked={adventuresChecked}
                                    onChange={() => setAdventuresChecked(!adventuresChecked)}
                                />
                            </Accordion.Header>
                            <Accordion.Body>
                                <FishingInstructorFilterForm setField={setField}/>
                            </Accordion.Body>
                        </Accordion.Item>
                        <Accordion.Item eventKey="1">
                            <Accordion.Header>
                                <Form.Check
                                    type="switch"
                                    id="vacationHouse-switch"
                                    label="Vikendice"
                                    defaultChecked={vacationHousesChecked}
                                    onChange={() => setVacationHousesChecked(!vacationHousesChecked)}
                                />
                            </Accordion.Header>
                            <Accordion.Body>
                                <VacationHouseFilterForm setField={setField}/>
                            </Accordion.Body>
                        </Accordion.Item>
                        <Accordion.Item eventKey="2">
                            <Accordion.Header>
                                <Form.Check
                                    type="switch"
                                    id="boatSwitch"
                                    label="Brodovi"
                                    defaultChecked={boatsChecked}
                                    onChange={() => setBoatsChecked(!boatsChecked)}/>
                            </Accordion.Header>
                            <Accordion.Body>
                                <BoatFilterForm setField={setField}/>
                            </Accordion.Body>
                        </Accordion.Item>
                    </Accordion>
                    {/*<Form.Label>Način sortiranja</Form.Label>*/}
                    {/*<DropdownMultiselect options={sortingOptions} name={"sortingOptions"}*/}
                    {/*                     placeholder={"Ništa nije izabrano"}*/}
                    {/*                     handleOnChange={(selected) => {*/}
                    {/*                         setField('sortingOptions', selected)*/}
                    {/*                     }}*/}
                    {/*/>*/}
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>
                    Otkaži
                </Button>
                <Button variant="primary" onClick={filterData}>
                    Pretraži sa filterima
                </Button>
            </Modal.Footer>
        </Modal>
    )
}