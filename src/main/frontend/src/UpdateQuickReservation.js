import {React, useEffect, useRef, useState} from 'react';
import {Button, Col, Form, InputGroup, Modal, Row} from 'react-bootstrap'
import {TagInfo} from './Info';
import axios from "axios";
import {useParams} from "react-router-dom";
import {backLink, notifyError, notifySuccess} from './Consts';
import {ToastContainer} from 'react-toastify';
import './material.css'

function UpdateQuickReservation({state, setState, closeModal, showModal, entity, durationText, availableTags}) {
    const [startDateInt, setStartDateInt] = useState("");
    const [startTimeInt, setStartTimeInt] = useState("");
    const [originalState, setOriginalState] = useState(state);
    const [sd, setSd] = useState("");
    const [availableTagsConst, setAvailableTagsConst] = useState(availableTags);
    const [tagText, setTagText] = useState('');
    const [validated, setValidated] = useState(false);
    const form = useRef();
    const {id} = useParams();

    useEffect(() => {
        if (state.additionalServices.length === 0)
            state.additionalServices = [{id: '', text: ''}]
        setOriginalState(state);
        setDateTimeInt();
    }, []);

    const setDateTimeInt = () => {
        var day = state.startDate.split(" ")[0];
        var monthStr = state.startDate.split(" ")[1];
        var year = state.startDate.split(" ")[2];
        var time = state.startDate.split(" ")[3];
        setStartTimeInt(time.substring(0, time.length - 1));
        if (monthStr === "Jan") {
            setStartDateInt(year + "-" + "01-" + day);
            var sdDMY = day + " 01 " + year;
            setSd(sdDMY + " " + time.substring(0, time.length - 1))
        }
        if (monthStr === "Feb") {
            setStartDateInt(year + "-" + "02-" + day);
            var sdDMY = day + " 02 " + year;
            setSd(sdDMY + " " + time.substring(0, time.length - 1))
        }
        if (monthStr === "Mar") {
            setStartDateInt(year + "-" + "03-" + day);
            var sdDMY = day + " 03 " + year;
            setSd(sdDMY + " " + time.substring(0, time.length - 1))
        }
        if (monthStr === "Apr") {
            setStartDateInt(year + "-" + "04-" + day);
            var sdDMY = day + " 04 " + year;
            setSd(sdDMY + " " + time.substring(0, time.length - 1))
        }
        if (monthStr === "May") {
            setStartDateInt(year + "-" + "05-" + day);
            var sdDMY = day + " 05 " + year;
            setSd(sdDMY + " " + time.substring(0, time.length - 1))
        }
        if (monthStr === "Jun") {
            setStartDateInt(year + "-" + "06-" + day);
            var sdDMY = day + " 06 " + year;
            setSd(sdDMY + " " + time.substring(0, time.length - 1))
        }
        if (monthStr === "Jul") {
            setStartDateInt(year + "-" + "07-" + day);
            var sdDMY = day + " 07 " + year;
            setSd(sdDMY + " " + time.substring(0, time.length - 1))
        }
        if (monthStr === "Avg") {
            setStartDateInt(year + "-" + "08-" + day);
            var sdDMY = day + " 08 " + year;
            setSd(sdDMY + " " + time.substring(0, time.length - 1))
        }
        if (monthStr === "Sep") {
            setStartDateInt(year + "-" + "09-" + day);
            var sdDMY = day + " 09 " + year;
            setSd(sdDMY + " " + time.substring(0, time.length - 1))
        }
        if (monthStr === "Oct") {
            setStartDateInt(year + "-" + "10-" + day);
            var sdDMY = day + " 10 " + year;
            setSd(sdDMY + " " + time.substring(0, time.length - 1))
        }
        if (monthStr === "Nov") {
            setStartDateInt(year + "-" + "11-" + day);
            var sdDMY = day + " 11 " + year;
            setSd(sdDMY + " " + time.substring(0, time.length - 1))
        }
        if (monthStr === "Dec") {
            setStartDateInt(year + "-" + "12-" + day);
            var sdDMY = day + " 12 " + year;
            setSd(sdDMY + " " + time.substring(0, time.length - 1))
        }
    }
    const submit = e => {
        e.preventDefault()

        if (form.current.checkValidity() === false) {
            e.stopPropagation();
            setValidated(true);
        } else {
            var data = new FormData(form.current);
            state.tagsText = []
            for (let i = 0; i < state.additionalServices.length; i++) {
                if (state.additionalServices[i].text !== '') {
                    state.tagsText.push(state.additionalServices[i].text)
                }
            }

            data.append("tagsText", state.tagsText);
            data.append("reservationID", state.reservationID);
            data.append("startDate", sd);
            axios
                .post(backLink + "/" + entity + "/updateQuickReservation/" + id, data)
                .then(res => {
                    if (res.data)
                        notifySuccess("Izmena uspešna.")
                    else
                        notifyError("Izmena neuspšna. Molimo Vas pokušajte ponovo.")
                    close();
                    setTimeout(function () {
                        window.location.reload();
                    }, 2000);
                })
        }

    }

    const deleteQuickReservation = () => {
        var reservationID = state.reservationID;
        axios
            .post(backLink + "/" + entity + "/deleteQuickReservation/" + id, reservationID, {headers: {"Content-Type": "text/plain"}})
            .then(res => {
                if (res.data)
                    notifySuccess("Akcija obrisana.")
                else
                    notifyError("Brisanje neuspšno. Molimo Vas pokušajte ponovo.")
                close();
                setTimeout(function () {
                    window.location.reload();
                }, 2000);
            })
    }

    function close() {
        Reset();
        closeModal();
    }

    const Reset = () => {
        setValidated(false);
        setState(originalState);
    }

    const setStartDate = (val) => {
        var sd = state.startDate;
        var date = val.split('-')
        var time = sd.split(" ")[3]
        if (time.substring(time.length - 1) === 'h') {
            time = time.substring(0, time.length - 1)
        }
        var newStartDate = date[2] + " " + date[1] + " " + date[0] + " " + time
        setState(prevState => {
            return {...prevState, startDate: newStartDate}
        })
        setSd(newStartDate)
    }
    const setStartTime = (val) => {
        var sd = state.startDate;
        var date = sd.split(" ");
        var day = date[0];
        var month = date[1];
        var year = date[2];
        if (month === "Jan")
            month = ("01");
        if (month === "Feb")
            month = ("02");
        if (month === "Mar")
            month = ("03");
        if (month === "Apr")
            month = ("04");
        if (month === "May")
            month = ("05");
        if (month === "Jun")
            month = ("06");
        if (month === "Jul")
            month = ("07");
        if (month === "Avg")
            month = ("08");
        if (month === "Sep")
            month = ("09");
        if (month === "Oct")
            month = ("10");
        if (month === "Nov")
            month = ("11");
        if (month === "Dec")
            month = ("12");
        var newStartDate = day + " " + month + " " + year + " " + val;
        console.log(newStartDate)
        setState(prevState => {
            return {...prevState, startDate: newStartDate}
        })
        setSd(newStartDate)
    }

    const setDuration = (value) => {
        setState(prevState => {
            return {...prevState, duration: value}
        })
    }
    const setNumberOfPeople = (value) => {
        setState(prevState => {
            return {...prevState, numberOfPeople: value}
        })
    }

    const setPrice = (value) => {
        setState(prevState => {
            return {...prevState, price: value}
        })
    }

    function addButton() {
        if (tagText !== '') {
            var can = false;
            for (var item of availableTagsConst) {
                if (item.text === tagText) {
                    can = true;
                    setState(prevState => {
                        return {...prevState,
                            additionalServices: [...prevState.additionalServices, {
                                id: prevState.additionalServices.at(-1).id + 1,
                                text: tagText
                            }]
                        }
                    })
                    setTagText('')
                }
            }
            if (can === false) {
                notifyError("Tag se može dodati samo iz postojećih dodatnih usluga entiteta.")
            }
        }
    }

    return (
        <Modal show={showModal} onHide={close}>
            <Form ref={form} noValidate validated={validated}>
                <Modal.Header>
                    <Modal.Title>Ažuriranje akcije</Modal.Title>
                </Modal.Header>
                <Modal.Body>

                    <Row className="mb-3">

                        <Form.Group as={Col}>
                            <Form.Label>Datum početka važenja akcije</Form.Label>
                            <Form.Control required type="date" name="date" defaultValue={startDateInt}
                                          onChange={e => setStartDate(e.target.value)}/>
                            <Form.Control.Feedback type="invalid">Molimo Vas unesite početni datum za koje akcija
                                važi.</Form.Control.Feedback>
                        </Form.Group>

                        {entity !== "house" &&
                            <Form.Group as={Col}>
                                <Form.Label>Vreme početka važenja akcije</Form.Label>
                                <Form.Control required type="time" name="time" defaultValue={startTimeInt}
                                              onChange={e => setStartTime(e.target.value)}/>
                                <Form.Control.Feedback type="invalid">Molimo Vas unesite početno vreme za koje akcija
                                    važi.</Form.Control.Feedback>
                            </Form.Group>
                        }
                    </Row>

                    <Row className="mb-3">

                        <Form.Group as={Col}>
                            <Form.Label>Broj {durationText} za koje akcija važi</Form.Label>
                            <Form.Control required type="number" min={1} name="duration" defaultValue={state.duration}
                                          onChange={e => setDuration(e.target.value)}/>
                            <Form.Control.Feedback type="invalid">Molimo Vas unesite broj {durationText} za koje akcija
                                važi.</Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group as={Col}>
                            <Form.Label>Maksimalan broj osoba</Form.Label>
                            <Form.Control required type="number" min={1} name="numberOfPeople"
                                          defaultValue={state.numberOfPeople}
                                          onChange={e => setNumberOfPeople(e.target.value)}/>
                            <Form.Control.Feedback type="invalid">Molimo Vas unesite broj ljudi za koje važi
                                akcija..</Form.Control.Feedback>
                        </Form.Group>
                    </Row>


                    <Form.Group as={Col}>
                        <Form.Label>Cena po noćenju</Form.Label>
                        <InputGroup>
                            <Form.Control required type="number" min={1} name="price" defaultValue={state.price}
                                          onChange={e => setPrice(e.target.value)}/>
                            <InputGroup.Text>€</InputGroup.Text>
                            <Form.Control.Feedback type="invalid">Molimo Vas unesite grad.</Form.Control.Feedback>
                        </InputGroup>
                    </Form.Group>

                    <Form.Group as={Col} controlId="formGridServices">
                        <Form.Label>Dodatne usluge</Form.Label>
                        <div className='d-flex justify-content-start'>
                            <TagInfo tagList={state.additionalServices} edit={true} setState={setState}
                                     entity="additionalServices"/>
                            <InputGroup className="p-0 mt-2" style={{maxWidth: "17vh", minWidth: "17vh"}}>
                                <Form.Control style={{height: "4vh"}} aria-describedby="basic-addon2"
                                              placeholder='Dodaj tag' value={tagText}
                                              onChange={e => setTagText(e.target.value)}/>
                                <Button className="p-0 pe-2 ps-2" style={{height: "4vh"}} variant="primary"
                                        id="button-addon2" onClick={addButton}> + </Button>
                            </InputGroup>
                        </div>
                    </Form.Group>

                </Modal.Body>
                <Modal.Footer className="justify-content-between">
                    <Button variant="outline-danger" onClick={deleteQuickReservation}>Obriši</Button>
                    <div>
                        <Button className="me-2" variant="secondary" onClick={close}>Nazad</Button>
                        <Button variant="primary" onClick={submit}>Sačuvaj</Button>
                    </div>
                </Modal.Footer>
            </Form>
            <ToastContainer
                position="top-center"
                autoClose={5000}
                hideProgressBar={false}
                newestOnTop={false}
                closeOnClick
                rtl={false}
                pauseOnFocusLoss
                draggable
                pauseOnHover
                theme={"colored"}
            />
        </Modal>
    );
}

export default UpdateQuickReservation;