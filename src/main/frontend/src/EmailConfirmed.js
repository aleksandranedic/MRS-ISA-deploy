import React, {useEffect, useState} from 'react'
import {useParams} from "react-router-dom";
import axios from "axios";
import {BsFillCheckCircleFill} from "react-icons/bs";
import background from "./images/RegistrationImage.jpg";
import {IoCloseCircleSharp} from "react-icons/io5";
import Button from "react-bootstrap/Button";
import {backLink, frontLink} from "./Consts";


export default function EmailConfirmed() {
    const token = useParams()
    const [pageData, setData] = useState({
        confirmed: true,
        messageTitle: "Vaša verifikacija je uspešna",
        message: "Vaš nalog je verifikovan. Možete se ulogavati na naš sajt"
    })

    const getData = () => {
        axios.get(backLink + "/registration/confirm/" + token.token).then(res => {
            console.log(res.data)
            setData(res.data)
        })
    }
    useEffect(() => {
        getData()
    }, [])

    return (
        <div className="m-0 p-0 min-vw-90 min-vh-100"
             style={{backgroundImage: `url(${background})`, backgroundSize: "cover",}}
        >
            <div className=" d-flex justify-content-center">
                <div
                    className="d-flex flex-column align-items-center border border-white  mt-lg-5 p-3 gap-2 rounded border-3">
                    {pageData.confirmed ? <BsFillCheckCircleFill size="5rem" color="green"/> :
                        <IoCloseCircleSharp size="5rem" color="red"/>
                    }
                    <h1 className="text-center text-white">{pageData.messageTitle}</h1>
                    <label className="text-center text-white">{pageData.message}</label>
                    <div className="d-flex flex-row gap-3">
                        <Button className="btn btn-primary" href={frontLink + "login"}>Prijavi se na sajt</Button>
                        <Button className="btn btn-primary" href={frontLink + "registration"}>Ponovo se
                            registrujte</Button>
                    </div>
                </div>
            </div>
        </div>
    )
}
