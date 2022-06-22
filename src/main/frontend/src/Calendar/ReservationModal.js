import {Button, Col, Dropdown, Form, InputGroup, Modal} from "react-bootstrap";
import React, {useEffect, useState} from "react";
import axios from "axios";
import {backLink, missingDataErrors, notifyError, notifySuccess} from "../Consts";
import {TagInfo} from "../Info";
import {MessagePopupModal} from "../MessagePopupModal";
import {isLoggedIn} from "../Autentification";
import {compareDateStrings, compareDateStringToToday, compareTimeStrings} from "./ReservationDateConverter";

export function ReservationModal({show, setShow, type, resourceId, myPage, additionalServices}) {

    const [clients, setClients] = useState([]);

    const [selectedClient, setSelectedClient] = useState([]);

    const [loggedClient, setLoggedClient] = useState([]);

    const [additionalServicesText, setAdditionalServicesText] = useState('');


    const fetchClients = () => {
        axios.get(backLink + "/client").then(res => {
            setClients(res.data);
        });
    };

    const fetchLoggedClient = () => {
        if (isLoggedIn()) {
            axios.get(backLink + "/getLoggedUser").then(
                response => {
                    setLoggedClient(response.data);
                }
            )
        }
    }

    const [formErrors, setFormErrors] = useState({});

    const validateForm = () => {
        let errors = {}

        if (myPage) {
            if (!selectedClient.firstName) {
                errors.client = missingDataErrors.client;
            }
        }

        if (formValues.startDate === "") {
            errors.startDate = missingDataErrors.date;
        }

        else if (compareDateStringToToday(formValues.startDate)) {
            errors.startDate = "Datum ne sme biti u prošlosti."
        }


        if (type === "vacationHouse") {
            if (formValues.endDate === "") {
                errors.endDate = missingDataErrors.date;
            }

            else if (compareDateStringToToday(formValues.endDate)) {
                errors.endDate = "Datum ne sme biti u prošlosti."
            }

            if (compareDateStrings(formValues.startDate, formValues.endDate)) {
                errors.endDate = "Krajnji datum ne sme biti pre početnog."
                errors.startDate = "Krajnji datum ne sme biti pre početnog."
            }



        }

        if (type !== "vacationHouse") {
            if (formValues.startTime === "") {
                errors.startTime = missingDataErrors.time;
            }

            if (formValues.endTime === "") {
                errors.endTime = missingDataErrors.time;
            }

            if (compareTimeStrings(formValues.startTime, formValues.endTime)) {
                errors.startTime = "Krajnje vreme ne sme biti pre početnog."
                errors.endTime = "Krajnje vreme ne sme biti pre početnog."
            }
        }

        if (formValues.numberOfClients === "") {
            errors.numberOfClients = missingDataErrors.numberOfClients
        }

        return errors;
    }

    const [formValues, setFormValues] = useState({
        startDate: "",
        endDate: "",
        startTime: "",
        endTime: "",
        numberOfClients: "",
        price: "",
        additionalServices: additionalServices
    });

    const setField = (fieldName, value) => {
        setFormValues({
            ...formValues,
            [fieldName]: value
        })
    }

    useEffect(() => {
        fetchClients();
        fetchLoggedClient();
    }, [])

    function extractTime() {

        let startYear = 0;
        let startMonth = 0;
        let startDay = 0;

        if (formValues.startDate) {
            startYear = parseInt(formValues.startDate.substring(0, 4));
            startMonth = parseInt(formValues.startDate.substring(5, 7));
            startDay = parseInt(formValues.startDate.substring(8, 10));
        }

        let startHour = 0;
        let startMinute = 0;

        if (formValues.startTime) {
            startHour = parseInt(formValues.startTime.substring(0, 2));
            startMinute = parseInt(formValues.startTime.substring(3, 5));
        }

        let endYear = 0;
        let endMonth = 0;
        let endDay = 0;

        if (type === "vacationHouse") {
            if (formValues.endDate) {
                endYear = parseInt(formValues.endDate.substring(0, 4));
                endMonth = parseInt(formValues.endDate.substring(5, 7));
                endDay = parseInt(formValues.endDate.substring(8, 10));
            }
        } else {
            endYear = startYear;
            endDay = startDay;
            endMonth = startMonth;
        }

        let endHour = 0;
        let endMinute = 0;

        if (formValues.endTime) {
            endHour = parseInt(formValues.endTime.substring(0, 2));
            endMinute = parseInt(formValues.endTime.substring(3, 5));
        }
        return {startYear, startMonth, startDay, startHour, startMinute, endYear, endMonth, endDay, endHour, endMinute};
    }

    const [showAlert, setShowAlert] = useState(false);
    const [message, setMessage] = useState("");

    const handleSubmit = e => {
        e.preventDefault()
        let errors = validateForm()
        if (Object.keys(errors).length > 0) {
            setFormErrors(errors);
        } else {
            addReservation(e);
        }
    }

    const addReservation = e => {
        let additionalServicesStrings = [];
        for (let index in formValues.additionalServices) {
            additionalServicesStrings.push(formValues.additionalServices.at(index).text);
        }
        let {
            startYear,
            startMonth,
            startDay,
            startHour,
            startMinute,
            endYear,
            endMonth,
            endDay,
            endHour,
            endMinute
        } = extractTime();

        let clientId;

        if (myPage) {
            clientId = selectedClient.id;
        }
        else {
            clientId = loggedClient.id;
        }


        let dto = {
            clientId: clientId,
            resourceId: resourceId,
            numberOfClients: formValues.numberOfClients,
            additionalServicesStrings: additionalServicesStrings,
            price: -1,
            type: type,
            startYear: startYear,
            startMonth: startMonth,
            startDay: startDay,
            startHour: startHour,
            startMinute: startMinute,
            endYear: endYear,
            endMonth: endMonth,
            endDay: endDay,
            endHour: endHour,
            endMinute: endMinute,
            isBusyPeriod: false,
            isQuickReservation: false
        }


        if (type === "adventure") {
            axios.post(backLink + "/client/canReserve", dto).then(res => {
                if (res.data === "Ok") {
                    axios
                        .post(backLink + "/adventure/reservation/add", dto)
                        .then(response => {
                            if (response.data === -1){
                                notifyError("Došlo je do greške prilikom zakazivanja. Molimo Vas pokušajte ponovo.")
                            }
                            else{
                                notifySuccess("Avantura uspešno rezervisana.")
                                setTimeout(function(){window.location.reload();}, 2000);
                            }
                        })
                        .catch(error => {
                            setMessage("Termin koji ste pokušali da zauzmete nije dostupan. Pogledajte kalendar zauzetosti avanture i kalendar zauzetosti instruktora pecanja pa pokušajte ponovo.")
                            setShowAlert(true);
                        })
                }
                else {
                    setMessage(res.data);
                    setShowAlert(true);
                }

            })



        } else if (type === "boat") {

            axios.post(backLink + "/client/canReserve", dto).then(res => {
                if (res.data === "Ok") {
                    axios
                        .post(backLink + "/boat/reservation/add", dto)
                        .then(response => {
                            if (response.data === -1){
                                notifyError("Došlo je do greške prilikom zakazivanja. Molimo Vas pokušajte ponovo.")
                            }
                            else{
                                notifySuccess("Brod uspešno rezervisan.")
                                setTimeout(function(){window.location.reload();}, 2000);
                            }
                        })
                        .catch(error => {
                            setMessage("Termin koji ste pokušali da zauzmete nije dostupan. Pogledajte kalendar zauzetosti broda pa pokušajte ponovo.")
                            setShowAlert(true);

                        })
                }
                else {
                    setMessage(res.data);
                    setShowAlert(true);
                }
            })

        } else if (type === "vacationHouse") {
            axios.post(backLink + "/client/canReserve", dto).then(res => {
                if (res.data === "Ok") {
                    axios
                        .post(backLink + "/house/reservation/add", dto)
                        .then(response => {
                            if (response.data === -1){
                                notifyError("Došlo je do greške prilikom zakazivanja. Molimo Vas pokušajte ponovo.")
                            }
                            else{
                                notifySuccess("Vikendica uspešno rezervisana.")
                                setTimeout(function(){window.location.reload();}, 2000);
                            }
                        })
                        .catch(error => {
                            setMessage("Termin koji ste pokušali da zauzmete nije dostupan. Pogledajte kalendar zauzetosti vikendice pa pokušajte ponovo.")
                            setShowAlert(true);

                        })
                }
                else {
                    setMessage(res.data);
                    setShowAlert(true);
                }

            })


        }

    }


    let html = "";

    if (clients.length > 0) {
        html = <><Modal show={show} onHide={() => setShow(false)}>
            <Modal.Header closeButton>
                <Modal.Title>Rezervacija</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>

                    {type === "vacationHouse" ?
                        <div className="d-flex w-100 m-2">
                            <Form.Group className="me-2 w-50 mt-2">
                                <Form.Label>Početni datum</Form.Label>
                                <Form.Control type="date" value={formValues.startDate}
                                              onChange={(e) => setField("startDate", e.target.value)}
                                              isInvalid={!!formErrors.startDate}
                                />
                                <Form.Control.Feedback type="invalid">
                                    {formErrors.startDate}
                                </Form.Control.Feedback>
                            </Form.Group>

                            <Form.Group className="ms-2 w-50 mt-2">
                                <Form.Label>Završni datum</Form.Label>
                                <Form.Control type="date" value={formValues.endDate}
                                              onChange={(e) => setField("endDate", e.target.value)}
                                              isInvalid={!!formErrors.endDate}
                                />
                                <Form.Control.Feedback type="invalid">
                                    {formErrors.endDate}
                                </Form.Control.Feedback>
                            </Form.Group>
                        </div>
                        :
                        <div className="d-flex w-100 m-2">
                            <Form.Group className="me-2 mt-2 w-100">
                                <Form.Label>Datum</Form.Label>
                                <Form.Control type="date" value={formValues.startDate}
                                              onChange={(e) => {
                                                  setField("endDate", e.target.value);
                                                  setField("startDate", e.target.value);
                                              }}
                                              isInvalid={!!formErrors.startDate}

                                />
                                <Form.Control.Feedback type="invalid">
                                    {formErrors.startDate}
                                </Form.Control.Feedback>
                            </Form.Group>
                        </div>
                    }
                    {type !== "vacationHouse" &&
                        <div className="d-flex w-100 m-2">
                            <Form.Group className="me-2 w-50 mt-2">
                                <Form.Label>Vreme početka</Form.Label>
                                <Form.Control type="time" min="05:00" max="20:00" value={formValues.startTime}
                                              onChange={(e) => setField("startTime", e.target.value)}
                                              isInvalid={!!formErrors.startTime}
                                />
                                <Form.Control.Feedback type="invalid">
                                    {formErrors.startTime}
                                </Form.Control.Feedback>
                            </Form.Group>

                            <Form.Group className="ms-2 w-50 mt-2">
                                <Form.Label>Vreme završetka</Form.Label>
                                <Form.Control type="time" min="05:00" max="20:00"
                                              value={formValues.endTime}
                                              onChange={(e) => setField("endTime", e.target.value)}
                                              isInvalid={!!formErrors.endTime}
                                />
                                <Form.Control.Feedback type="invalid">
                                    {formErrors.endTime}
                                </Form.Control.Feedback>
                            </Form.Group>
                        </div>}

                    {myPage &&

                        <Form.Group className="m-2">
                            <Form.Label>Klijent</Form.Label>
                            <Dropdown>
                                <Dropdown.Toggle variant="light">
                                    {selectedClient.firstName ?
                                        selectedClient.firstName + " " + selectedClient.lastName
                                        :
                                        "Izaberi klijenta"
                                    }
                                </Dropdown.Toggle>

                                <Dropdown.Menu>
                                    {clients.map((client, index) => {
                                        return <Dropdown.Item key={index}
                                                              onClick={() => setSelectedClient(client)}>
                                            {client.firstName + " " + client.lastName}
                                        </Dropdown.Item>;
                                    })}
                                </Dropdown.Menu>
                            </Dropdown>
                            <Form.Control isInvalid={!!formErrors.client} style={{display: "none"}}/>
                            <Form.Control.Feedback type="invalid">
                                {formErrors.client}
                            </Form.Control.Feedback>
                        </Form.Group>
                    }


                    <Form.Group className="m-2">
                        <Form.Label>Broj klijenata</Form.Label>
                        <Form.Control type="number"
                                      value={formValues.numberOfClients}
                                      onChange={(e) => setField("numberOfClients", e.target.value)}
                                      isInvalid={!!formErrors.numberOfClients}

                        />
                        <Form.Control.Feedback type="invalid">
                            {formErrors.numberOfClients}
                        </Form.Control.Feedback>

                    </Form.Group>

                    <Form.Group as={Col} className=" m-2">
                        <Form.Label>Dodatne usluge</Form.Label>
                        <div className='d-flex justify-content-start align-items-center'>
                            <TagInfo tagList={formValues.additionalServices} edit={true}
                                     setState={setFormValues} entity="additionalServices"/>
                        </div>
                    </Form.Group>

                </Form>

            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={() => setShow(false)}>
                    Otkaži
                </Button>
                <Button variant="primary" onClick={e => handleSubmit(e)}>
                    Rezerviši
                </Button>
            </Modal.Footer>
        </Modal>

            <MessagePopupModal
                show={showAlert}
                setShow={setShowAlert}
                message={message}
                heading="Zauzet termin"
            />
        </>;
    }
    return html;
}