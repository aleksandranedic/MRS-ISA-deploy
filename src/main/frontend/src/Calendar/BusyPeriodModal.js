import {Button, Form, Modal} from "react-bootstrap";
import React, {useState} from "react";
import axios from "axios";
import {backLink, missingDataErrors} from "../Consts";
import {MessagePopupModal} from "../MessagePopupModal";


export function BusyPeriodModal({show, setShow, type, resourceId}) {

    const [showAlert, setShowAlert] = useState(false);
    const [formValues, setFormValues] = useState({startDate: null, endDate: null});
    const [formErrors, setFormErrors] = useState({});

    const validateForm = () => {
        let errors = {}

        if (formValues.startDate === null) {
            errors.startDate = missingDataErrors.date;
        }

        if (formValues.endDate === null) {
            errors.endDate = missingDataErrors.date;
        }

        return errors;
    }


    const setField = (fieldName, value) => {
        setFormValues({
            ...formValues,
            [fieldName]: value
        })
    }

    function extractTime() {
        let startYear = 0;
        let startMonth = 0;
        let startDay = 0;

        if (formValues.startDate) {
            startYear = parseInt(formValues.startDate.substring(0, 4));
            startMonth = parseInt(formValues.startDate.substring(5, 7));
            startDay = parseInt(formValues.startDate.substring(8, 10));
        }

        let endYear = 0;
        let endMonth = 0;
        let endDay = 0;

        if (formValues.endDate) {
            endYear = parseInt(formValues.endDate.substring(0, 4));
            endMonth = parseInt(formValues.endDate.substring(5, 7));
            endDay = parseInt(formValues.endDate.substring(8, 10));
        }

        return {startYear, startMonth, startDay, endYear, endMonth, endDay};
    }

    const handleSubmit = e => {
        e.preventDefault()
        let errors = validateForm();
        if (Object.keys(errors).length > 0) {
            setFormErrors(errors);
        } else {
            addReservation(e);
        }
    }

    function addReservation() {

        let {
            startYear,
            startMonth,
            startDay,
            endYear,
            endMonth,
            endDay
        } = extractTime();

        let dto = {
            startYear: startYear,
            startMonth: startMonth,
            startDay: startDay,
            endYear: endYear,
            endMonth: endMonth,
            endDay: endDay,
            type: type,
            resourceId: resourceId
        }

        if (type === 'adventure') {
            axios
                .post(backLink + "/adventure/reservation/busyPeriod/add", dto)
                .then(response => {
                    window.location.reload();
                })
                .catch(error => {
                    console.log(error);
                    setShowAlert(true);
                })
        } else if (type === 'vacationHouse') {
            axios
                .post(backLink + "/house/reservation/busyPeriod/add", dto)
                .then(response => {
                    window.location.reload();
                })
                .catch(error => {
                    console.log(error);
                    setShowAlert(true);
                })
        } else if (type === 'boat') {
            axios
                .post(backLink + "/boat/reservation/busyPeriod/add", dto)
                .then(response => {
                    window.location.reload();
                })
                .catch(error => {
                    console.log(error);
                    setShowAlert(true);
                })
        }
    }

    return (<><Modal show={show} onHide={() => setShow(false)}>
            <Modal.Header closeButton>
                <Modal.Title>Period zauzetosti</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Form.Group>
                        <Form.Label>Datum</Form.Label>
                        <Form.Control type="date" name="startDate"
                                      onChange={(e) => setField("startDate", e.target.value)}
                                      isInvalid={!!formErrors.startDate}
                        />
                        <Form.Control.Feedback type="invalid">
                            {formErrors.startDate}
                        </Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label>Datum</Form.Label>
                        <Form.Control type="date" name="endDate"
                                      onChange={(e) => setField("endDate", e.target.value)}
                                      isInvalid={!!formErrors.endDate}
                        />
                        <Form.Control.Feedback type="invalid">
                            {formErrors.endDate}
                        </Form.Control.Feedback>
                    </Form.Group>

                </Form>

            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={() => setShow(false)}>
                    Otkaži
                </Button>
                <Button variant="primary" onClick={e => handleSubmit(e)}>
                    Dodaj
                </Button>
            </Modal.Footer>
        </Modal>
            <MessagePopupModal
                show={showAlert}
                setShow={setShowAlert}
                message="Za period koji ste pokušali da zazmete postoje rezervacije ili je već dodat period zauzetosti. Pogledajte kalendar zauzetosti i pokušajte ponovo."
                heading="Zauzet termin"
            />
        </>
    )
}