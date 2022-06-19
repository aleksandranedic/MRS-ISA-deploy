import React, {useState} from 'react'
import {Button, Form, Modal} from "react-bootstrap";
import background from "./images/boatsnotext.png"
import axios from "axios";
import Collapse from "react-bootstrap/Collapse";
import {backLink, frontLink, loadingToast, updateForFetchedDataError, updateForFetchedDataSuccess} from "./Consts";
import PopUp from "./PopUp";
import {ToastContainer} from "react-toastify";

export default function Registration() {
    const [form, setForm] = useState({})
    const [errors, setErrors] = useState({})

    const stringRegExp = new RegExp("[A-Z][A-Za-z]+")
    const streetRegExp = new RegExp("[A-Z][A-Za-z]+.?[A-Za-z]*")
    const passwordExp = new RegExp(".[^\\n\\t\\s]+")
    const numExp = new RegExp("[1-9][0-9]*[a-z]?")
    const phoneNumRegExp = new RegExp("^[0-9]{7,10}$")
    const emailRegExp = new RegExp(".+(@.+\\.com)|(@maildrop\\.cc)")


    const [show, setShow] = useState(false)
    const [text, setText] = useState("")
    const handleClose = () => setShow(false)


    const findFormErrors = () => {
        const {firstName, lastName, password, confPass, number, street, place, country, phoneNumber, email, role} = form
        const newErrors = {}

        if (!firstName) newErrors.firstName = 'Polje ne sme da bude prazno!'
        else if (!stringRegExp.test(firstName)) newErrors.firstName = 'Mora da pocne sa velikim slovom!'

        if (!lastName) newErrors.lastName = 'Polje ne sme da bude prazno!'
        else if (!stringRegExp.test(lastName)) newErrors.lastName = 'Mora da pocne sa velikim slovom!'

        if (!password || !passwordExp.test(password)) newErrors.password = 'Polje ne sme da bude prazno!'

        if (!confPass || !passwordExp.test(confPass)) newErrors.confPass = 'Polje ne sme da bude prazno!'
        else if (confPass !== password) newErrors.confPass = 'Sifre se ne poklapaju!'

        if (!number) newErrors.number = 'Polje ne sme da bude prazno!'
        else if (!numExp.test(number)) newErrors.number = 'Mora da sadrzi brojeve!'

        if (!street || !streetRegExp.test(street)) newErrors.street = 'Polje ne sme da bude prazno!'

        if (!place) newErrors.place = 'Polje ne sme da bude prazno!'
        else if (!streetRegExp.test(place)) newErrors.place = 'Mora da pocne sa velikim slovom!'

        if (!country) newErrors.country = 'Polje ne sme da bude prazno!'
        else if (!streetRegExp.test(country)) newErrors.country = 'Mora da pocne sa velikim slovom!'

        if (!phoneNumber) newErrors.phoneNumber = 'Polje ne sme da bude prazno!'
        if (!phoneNumRegExp.test(phoneNumber)) newErrors.phoneNumber = 'Mora da sadrzi od 7 do 10 cifara!'

        if (!email) newErrors.email = 'Polje ne sme da bude prazno!'
        else if (!emailRegExp.test(email)) newErrors.email = 'Mora da sadrzi @gmail.com!'

        if (role === undefined || !role) newErrors.role = 'Mora te izabrati vrstu korisnika'

        return newErrors

    }

    const handleSubmit = e => {
        e.preventDefault()
        const newErrors = findFormErrors()
        if (Object.keys(newErrors).length > 0) {
            setErrors(newErrors)
        } else {
            const userDTO = {
                firstName: form.firstName,
                lastName: form.lastName,
                password: form.password,
                phoneNumber: form.phoneNumber,
                email: form.email,
                userRole: form.role,
                biography: form.biography === undefined ? "" : form.biography,
                registrationRationale: form.registrationRationale === undefined ? "" : form.registrationRationale,
                number: form.number,
                street: form.street,
                place: form.place,
                country: form.country
            }
            registerUser(userDTO)
        }
    }

    function registerUser(userDTO) {
        let id=loadingToast()
        axios.post(backLink+"/registration", userDTO).then(res => {
            console.log(res.data)
            if(!res.data.startsWith("Korisnik"))
                updateForFetchedDataSuccess(res.data,id)
            else
                updateForFetchedDataError(res.data,id)
        })
    }

    const [openForInstructor, setOpenForInstructor] = useState(false);
    const [openForOtherVendors, setOpenForOtherVendors] = useState(false);


    const setField = (field, value) => {
        setForm({
            ...form,
            [field]: value
        })
        if (field === 'role' && value === "FISHING_INSTRUCTOR") {
            setOpenForInstructor(true)
            setOpenForOtherVendors(true)
        } else if (field === 'role' && value === "CLIENT") {
            setOpenForOtherVendors(false)
            setOpenForInstructor(false)
        } else if (field === 'role' && (value === "BOAT_OWNER" || value === "VACATION_HOUSE_OWNER")) {
            setOpenForOtherVendors(true)
            setOpenForInstructor(false)
        } else if (field === 'role' && value === "") {
            setOpenForOtherVendors(false)
            setOpenForInstructor(false)
        }
        if (!!errors[field]) setErrors({
            ...errors,
            [field]: null
        })
    }
    return (
        <div className="m-0 p-0 min-vw-90 min-vh-100"
             style={{backgroundImage: `url(${background})`, backgroundSize: "cover",}}
        >
            <div className="d-flex justify-content-center h-50 w-100">
                <Modal.Dialog size="lg">
                    <Modal.Header>
                        <Modal.Title>Registracija</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <Form className="d-flex scrollbar scrollbar-primary">
                            <div className="m-2 w-50">
                                <Form.Group className="mb-3" controlId="formName">
                                    <Form.Label>Ime</Form.Label>
                                    <Form.Control type="text"
                                                  onChange={e => setField('firstName', e.target.value)}
                                                  isInvalid={!!errors.firstName}/>
                                    <Form.Control.Feedback type='invalid'>
                                        {errors.firstName}
                                    </Form.Control.Feedback>
                                </Form.Group>
                                <Form.Group className="mb-3" controlId="formSurname">
                                    <Form.Label>Prezime</Form.Label>
                                    <Form.Control type="text"
                                                  onChange={e => setField('lastName', e.target.value)}
                                                  isInvalid={!!errors.lastName}/>
                                    <Form.Control.Feedback type='invalid'>
                                        {errors.lastName}
                                    </Form.Control.Feedback>
                                </Form.Group>
                                <Form.Group className="mb-3" controlId="formPhoneNumber">
                                    <Form.Label>Broj telefona</Form.Label>
                                    <Form.Control type="text"
                                                  onChange={e => setField('phoneNumber', e.target.value)}
                                                  isInvalid={!!errors.phoneNumber}/>
                                    <Form.Control.Feedback type='invalid'>
                                        {errors.phoneNumber}
                                    </Form.Control.Feedback>
                                </Form.Group>
                            </div>
                            <div className="m-2 w-50">
                                <Form.Group className="mb-3 m" controlId="formBasicEmail">
                                    <Form.Label>Email</Form.Label>
                                    <Form.Control type="email"
                                                  onChange={e => setField('email', e.target.value)}
                                                  isInvalid={!!errors.email}/>
                                    <Form.Control.Feedback type='invalid'>
                                        {errors.email}
                                    </Form.Control.Feedback>
                                </Form.Group>
                                <Form.Group className="mb-3 m" controlId="formBasicPassword">
                                    <Form.Label>Lozinka</Form.Label>
                                    <Form.Control type="password"
                                                  onChange={e => setField('password', e.target.value)}
                                                  isInvalid={!!errors.password}/>
                                    <Form.Control.Feedback type='invalid'>
                                        {errors.password}
                                    </Form.Control.Feedback>
                                </Form.Group>

                                <Form.Group className="mb-3" controlId="formConfirmPassword">
                                    <Form.Label>Ponovi lozinku</Form.Label>
                                    <Form.Control type="password"
                                                  onChange={e => setField('confPass', e.target.value)}
                                                  isInvalid={!!errors.confPass}/>
                                    <Form.Control.Feedback type='invalid'>
                                        {errors.confPass}
                                    </Form.Control.Feedback>
                                </Form.Group>
                            </div>
                        </Form>
                        <Form>
                            <div className="m-2 ">
                                <Form.Group className="mb-3" controlId="formBasicUserRole">
                                    <Form.Label>Vrsta korisnika</Form.Label>
                                    <Form.Select onChange={e => setField('role', e.target.value)}
                                                 isInvalid={!!errors.role}>
                                        <option/>
                                        <option value="CLIENT">Obican korisnik</option>
                                        <option value="FISHING_INSTRUCTOR">Instruktor pecanja</option>
                                        <option value="VACATION_HOUSE_OWNER">Vlasnik vikendice</option>
                                        <option value="BOAT_OWNER">Vlasnik čamca</option>
                                    </Form.Select>
                                    <Form.Control.Feedback type='invalid'>
                                        {errors.role}
                                    </Form.Control.Feedback>
                                </Form.Group>
                            </div>
                            <Collapse in={openForInstructor} dimension="height">
                                <Form.Group className="mb-3 m-2 hidden" controlId="formBasicBiography">
                                    <Form.Label>Biografija</Form.Label>
                                    <Form.Control as="textarea" rows={2}
                                                  onChange={e => setField('biography', e.target.value)}/>
                                </Form.Group>
                            </Collapse>
                            <Collapse in={openForOtherVendors} dimension="height">
                                <Form.Group className="mb-3 m-2 hidden" controlId="formBasicRegistrationReason">
                                    <Form.Label>Zahtev za registraciju</Form.Label>
                                    <Form.Control as="textarea" rows={2}
                                                  onChange={e => setField('ration', e.target.value)}/>
                                </Form.Group>
                            </Collapse>
                        </Form>
                        <Form className="d-flex gap-4">
                            <Form.Group className="mb-3 m-2" controlId="formStreet">
                                <Form.Label>Adresa</Form.Label>
                                <Form.Control type="text"
                                              onChange={e => setField('street', e.target.value)}
                                              isInvalid={!!errors.street}/>
                                <Form.Control.Feedback type='invalid'>
                                    {errors.street}
                                </Form.Control.Feedback>
                            </Form.Group>
                            <Form.Group className="mb-3 m-2" controlId="formNumOfAddress">
                                <Form.Label>Broj</Form.Label>
                                <Form.Control type="text"
                                              onChange={e => setField('number', e.target.value)}
                                              isInvalid={!!errors.number}/>
                                <Form.Control.Feedback type='invalid'>
                                    {errors.number}
                                </Form.Control.Feedback>
                            </Form.Group>
                            <Form.Group className="mb-3 m-2 " controlId="formCity">
                                <Form.Label>Grad</Form.Label>
                                <Form.Control type="text"
                                              onChange={e => setField('place', e.target.value)}
                                              isInvalid={!!errors.place}/>
                                <Form.Control.Feedback type='invalid'>
                                    {errors.place}
                                </Form.Control.Feedback>
                            </Form.Group>

                            <Form.Group className="mb-3 m-2" controlId="formCountry">
                                <Form.Label>Drzava</Form.Label>
                                <Form.Control type="text"
                                              onChange={e => setField('country', e.target.value)}
                                              isInvalid={!!errors.country}/>
                                <Form.Control.Feedback type='invalid'>
                                    {errors.country}
                                </Form.Control.Feedback>
                            </Form.Group>
                        </Form>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="btn btn-outline-primary" href={frontLink+"login"}>
                            Prijavi se
                        </Button>
                        <Button variant="success" onClick={handleSubmit}>
                            Registruj se
                        </Button>
                    </Modal.Footer>
                </Modal.Dialog>
            </div>
            <PopUp text={text} handleClose={handleClose} show={show}/>
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
        </div>
    );
}
