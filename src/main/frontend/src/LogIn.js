import React, {useState} from 'react'
import 'bootstrap/dist/css/bootstrap.css';
import {Form} from "react-bootstrap";
import axios from "axios";
import Button from "react-bootstrap/Button";
import {backLink, frontLink, missingDataErrors, notifyError, notifySuccess} from "./Consts";
import {ToastContainer} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css'

export default function Login() {
    function handleValidateLogIn({email, password}) {

        let loginDto = {
            username: email,
            password: password
        }
        axios.post(backLink + "/login", loginDto).then(res => {
            if (res.data.id === null) {
                notifyError(res.data.jwt)
            } else {
                notifySuccess("Uspešno ste se ulogovali")
                localStorage.setItem("token", res.data.jwt)
                localStorage.setItem("userRoleName", res.data.roleName);
                localStorage.setItem("userId", res.data.id);
                setTimeout(function () {
                    if (res.data.roleName === "ADMINISTRATOR") {
                        console.log(res.data);
                        if (res.data.confirmed === false) {
                            window.location.href = frontLink + "admin/conformation";
                        }
                        else
                        window.location.href = frontLink + "admin";
                    } else {
                        window.location.href = frontLink;
                    }
                }, 2000)
            }
        })
    }

    const [form, setForm] = useState({})
    const [errors, setErrors] = useState({})

    const emailRegExp = new RegExp("[A-Za-z0-9]+(@[a-z]+.(com)|(@maildrop\\.cc))")

    const findFormErrors = () => {
        const {email, password} = form
        const newErrors = {}

        if (!password) newErrors.password = missingDataErrors.password

        if (!email) newErrors.email = missingDataErrors.email
        else if (!emailRegExp.test(email)) newErrors.email = missingDataErrors.gmail

        return newErrors
    }

    const handleSubmit = e => {
        e.preventDefault()
        const newErrors = findFormErrors()
        if (Object.keys(newErrors).length > 0) {
            setErrors(newErrors)
        } else {
            handleValidateLogIn(form)
        }
    }
    const setField = (field, value) => {
        setForm({
            ...form,
            [field]: value
        })
        if (!!errors[field]) setErrors({
            ...errors,
            [field]: null
        })
    }
    return (
        <>
            <div className="d-flex justify-content-center w-100"
                 style={{
                     height: "100vh",
                     background: "linear-gradient(175deg, rgba(248,248,248,0.9) 50%, rgba(201, 201, 201, 0.6) 100%)"
                 }}
            >
                <img
                    src={require("./images/LoginImage.jpg")}
                    alt="login"
                    style={{width: "60%"}}
                />
                <div className="p-5 d-flex flex-column"
                     style={{width: "40%", backgroundColor: "rgba(255,255,255, 0.8)"}}>
                    <h1 className="mb-5">Prijava</h1>
                    <Form.Group className="mb-3" controlId="formEmail">
                        <Form.Label>Email</Form.Label>
                        <Form.Control type="text"
                                      onChange={e => setField('email', e.target.value)}
                                      isInvalid={!!errors.email}/>
                        <Form.Control.Feedback type='invalid'>
                            {errors.email}
                        </Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group className="mb-3" controlId="formPassword">
                        <Form.Label>Lozinka</Form.Label>
                        <Form.Control type="password"
                                      onChange={e => setField('password', e.target.value)}
                                      isInvalid={!!errors.password}/>
                        <Form.Control.Feedback type='invalid'>
                            {errors.password}
                        </Form.Control.Feedback>
                    </Form.Group>
                    <button onClick={handleSubmit} type="submit" className="btn btn-primary btn-block mt-2">
                        Prijavi se
                    </button>
                    <div className="d-flex flex-column mt-5 pt-lg-5">
                        <label>Želite da se registrujete?</label>
                        <Button className="btn btn-primary btn-block mt-2"
                                href={frontLink + "registration"}>Registracija</Button>
                        <label>Da li želite da posetite sajt?</label>
                        <Button className="btn btn-primary btn-block mt-2" href={frontLink}>Nazad na početnu
                            stranu</Button>
                    </div>
                </div>
            </div>
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
        </>
    )
}