import {Button, Form, Modal} from "react-bootstrap";
import React, {useState, useRef} from "react";
import {backLink, missingDataErrors} from "../Consts";
import axios from "axios";
import {useParams} from "react-router-dom";
import DeleteUserPopUp from "../ClientPage/DeleteUserPopUp";
import {ChangeClientPassword} from "../ClientPage/ChangeClientPassword";


export function FishingInstructorForm({show, setShow, fishingInstructor, profileImg}) {

    const [showPassword, setShowPassword] = useState(false);
    const handleShowPassword = () => setShowPassword(true);

    const [showDelete, setShowDelete] = useState(false);
    const handleHideDelete = () => setShowDelete(false);

    const {id} = useParams();
    const formRef = useRef();

    const [formValues, setFormValues] = useState({
        firstName: fishingInstructor.firstName,
        lastName: fishingInstructor.lastName,
        phoneNumber: fishingInstructor.phoneNumber,
        street: fishingInstructor.address.street,
        number: fishingInstructor.address.number,
        place: fishingInstructor.address.place,
        country: fishingInstructor.address.country
    });
    const [formErrors, setFormErrors] = useState({});
    const [isSubmit, setIsSubmit] = useState(false);


    const opetFileExplorer = () => {
        document.getElementById("fileImage").click();
    }

    const setProfileImageView = () => {
        var file = document.getElementById("fileImage").files[0]
        document.getElementById("profPic").src = URL.createObjectURL(file);
    }

    const setField = (fieldName, value) => {
        setFormValues({
            ...formValues,
            [fieldName]: value
        })
        if (!!formErrors[fieldName]) {
            setFormErrors({
                ...formErrors,
                [fieldName]: null
            })
        }
    }

    const handleSubmit = (e) => {
        e.preventDefault()
        let errors = validate(formValues)
        if (Object.keys(errors).length > 0) {
            setFormErrors(errors);
        } else {
            var file = document.getElementById("fileImage").files[0];
            if (typeof file !== "undefined") {
                var files = document.getElementById("fileImage").files;
                var data = new FormData();
                var images = []
                for (let i = 0; i < files.length; i++) {
                    images.push(files[i])
                }
                data.append("fileImage", file);
                axios
                    .post(backLink + "/fishinginstructor/changeProfilePicture/" + id, data)
                    .then(res => {
                        console.log(res.data);
                        setShow(false);
                        window.location.reload();
                    });
            }

            let dto = {
                firstName: formValues.firstName,
                lastName: formValues.lastName,
                phoneNumber: formValues.phoneNumber,
                street: formValues.street,
                number: formValues.number,
                place: formValues.place,
                country: formValues.country,
                id: fishingInstructor.id
            }

            axios
                .post(backLink + "/fishinginstructor/edit", dto)
                .then(response => {
                    console.log(response);
                    setShow(false);
                    window.location.reload();
                })
                .catch(error => {
                    console.log(error);
                })


        }
    };

    const validate = (formValues) => {
        const errors = {};
        if (!formValues.firstName) {
            errors.firstName = missingDataErrors.firstName;
        }
        if (!formValues.lastName) {
            errors.lastName = missingDataErrors.lastName;
        }
        if (!formValues.phoneNumber) {
            errors.phoneNumber = missingDataErrors.phoneNumber;
        }
        if (!formValues.street) {
            errors.address.street = missingDataErrors.address.street;
        }
        if (!formValues.place) {
            errors.address.place = missingDataErrors.address.place;
        }
        if (!formValues.number) {
            errors.address.number = missingDataErrors.address.number;
        }
        if (!formValues.country) {
            errors.address.country = missingDataErrors.address.country;
        }

        return errors;
    };


    let html;
    if (fishingInstructor !== null) {
        html = <>
            <Modal
            size="lg"
            show={show}
            onHide={() => setShow(false)}>

            <Form>
                <Modal.Header closeButton>
                    <Modal.Title>{formValues.firstName + " " + formValues.lastName}</Modal.Title>
                </Modal.Header>

                <Modal.Body>

                    <div className="d-flex">
                        <div className="d-flex justify-content-center" style={{width: "28%"}}>
                            <img id="profPic" className="rounded-circle" style={{
                                objectFit: "cover",
                                maxWidth: "25vh",
                                minWidth: "25vh",
                                maxHeight: "25vh",
                                minHeight: "25vh"
                            }} src={profileImg}/>
                            <Form.Control id="fileImage" onChange={e => setProfileImageView()} className="d-none"
                                          type="file" name="fileImage"
                                          style={{position: "absolute", width: "25vh", top: "12vh"}}

                            />
                            <p id="setNewProfileImage" className="d-flex justify-content-center align-items-center"
                               onClick={e => opetFileExplorer()}><u>Postavite profilnu</u></p>
                        </div>
                        <div style={{width: "72%"}}>
                            <Form.Group className="mb-3 m-2" controlId="firstName" ref={formRef}>
                                <Form.Label>Ime</Form.Label>
                                <Form.Control type="text" defaultValue={formValues.firstName}
                                              onChange={(e) => setField("firstName", e.target.value)}
                                              isInvalid={!!formErrors.firstName}
                                />
                                <Form.Control.Feedback> {formErrors.firstName} </Form.Control.Feedback>
                            </Form.Group>

                            <Form.Group className="mb-3 m-2" controlId="lastName">
                                <Form.Label>Prezime</Form.Label>
                                <Form.Control type="text" defaultValue={formValues.lastName}
                                              onChange={(e) => setField("lastName", e.target.value)}
                                              isInvalid={!!formErrors.lastName}/>
                                <Form.Control.Feedback> {formErrors.lastName} </Form.Control.Feedback>

                            </Form.Group>
                        </div>
                    </div>

                    <Form.Group className="mb-3 m-2" controlId="phoneNumber">
                        <Form.Label>Broj telefona</Form.Label>
                        <Form.Control type="text" defaultValue={formValues.phoneNumber}
                                      onChange={(e) => setField("phoneNumber", e.target.value)}
                                      isInvalid={!!formErrors.phoneNumber}/>
                        <Form.Control.Feedback> {formErrors.phoneNumber} </Form.Control.Feedback>
                    </Form.Group>

                    <div className="d-flex">
                        <Form.Group className="mb-3 m-2" controlId="street">
                            <Form.Label>Ulica</Form.Label>
                            <Form.Control type="text" defaultValue={formValues.street}
                                          onChange={(e) => setField("street", e.target.value)}
                                          isInvalid={!!formErrors.street}/>
                            <Form.Control.Feedback> {formErrors.street} </Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group className="mb-3 m-2 " controlId="number">
                            <Form.Label>Broj</Form.Label>
                            <Form.Control type="text" defaultValue={formValues.number}
                                          onChange={(e) => setField("number", e.target.value)}
                                          isInvalid={!!formErrors.number}/>
                            <Form.Control.Feedback> {formErrors.number} </Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group className="mb-3 m-2" controlId="place">
                            <Form.Label>Mesto</Form.Label>
                            <Form.Control type="text" defaultValue={formValues.place}
                                          onChange={(e) => setField("place", e.target.value)}
                                          isInvalid={!!formErrors.place}/>
                            <Form.Control.Feedback> {formErrors.place} </Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group className="mb-3 m-2" controlId="country">
                            <Form.Label>Drzava</Form.Label>
                            <Form.Control type="text" defaultValue={formValues.country}
                                          onChange={(e) => setField("country", e.target.value)}
                                          isInvalid={!!formErrors.country}/>
                            <Form.Control.Feedback> {formErrors.country} </Form.Control.Feedback>
                        </Form.Group>
                    </div>
                    <Button variant="link" onClick={handleShowPassword}>Promenite lozinku</Button>
                    <ChangeClientPassword show={showPassword} setShow={setShowPassword}/>
                </Modal.Body>
                <Modal.Footer>

                    <Button variant="outline-danger" className="me-auto" onClick={() => setShowDelete(true)}>
                        Obriši
                    </Button>
                    <Button variant="secondary" onClick={() => setShow(false)}>
                        Otkaži
                    </Button>
                    <Button variant="primary" onClick={handleSubmit}>
                        Izmeni
                    </Button>
                </Modal.Footer>
            </Form>
                <DeleteUserPopUp user={fishingInstructor} show={showDelete} handleClose={handleHideDelete}
                                 type={"FISHING_INSTRUCTOR"}/>

        </Modal>

        </>;
    }

    return html;

}


