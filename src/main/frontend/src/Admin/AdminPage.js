import {Sidebar} from "./Sidebar/Sidebar";
import React, {useEffect, useState} from "react";
import {isLoggedIn} from "../Autentification";
import axios from "axios";
import {backLink, profilePicturePlaceholder} from "../Consts";
import {BsEnvelope, BsTelephone, BsGeoAlt, BsPencilSquare} from 'react-icons/bs'
import {Button} from "react-bootstrap";
import {AdminForm} from "./AdminForm";

export function AdminPage() {
    const [user, setUser] = useState(null);
    const [show, setShow] = useState(false);

    useEffect(() => {
        if (isLoggedIn()) {
            getLoggedUser();
        }
    }, [])

    const getLoggedUser = () => {
        let id = localStorage.getItem("userId");

        axios.get(backLink + "/admin/" + id).then(
            response => {
                setUser(response.data);
            }
        )

    }

    let html = ""
    if (user !== null) {
        html = <>

            <AdminForm show={show} setShow={setShow} administrator={user}/>

            <div className="d-flex" style={{height: "100vh"}}>
                <div className="w-25" style={{backgroundColor: "#f7f8f9"}}>
                    <Sidebar/>
                </div>
                <div className="w-75 ms-5">
                    <div className="d-flex">
                        <div className="ms-auto m-3">
                            <BsPencilSquare onClick={()=>setShow(true)}/>
                        </div>
                    </div>
                    <div className="w-100 mt-2 d-flex flex-column justify-content-center align-items-center align-text-center">
                        <img className="rounded-circle" style={{
                            objectFit: "cover",
                            maxWidth: "30vh",
                            minWidth: "30vh",
                            maxHeight: "30vh",
                            minHeight: "30vh"
                        }}
                             src={user.profileImg !== null ? backLink + user.profileImg.path : profilePicturePlaceholder}/>
                        <h2 className=" display-6 pt-2fs-3">{user.firstName + " " + user.lastName}</h2>
                    </div>


                    <div className="d-flex flex-column  m-5">
                        <div className="d-flex align-items-center w-100">
                            <BsEnvelope style={{
                                height: "1.6rem",
                                width: "1.6rem",
                                marginRight: "0.75rem"
                            }}/>
                            <p className="m-0 p-0 fs-5">
                                Email
                            </p>
                        </div>
                        <p className="m-0 p-0 fw-light fs-6">
                            {user.email}
                        </p>
                    </div>

                    <div className="d-flex flex-column  m-5 ">
                        <div className="d-flex align-items-center w-100">
                            <BsTelephone style={{
                                height: "1.6rem",
                                width: "1.6rem",
                                marginRight: "0.75rem"
                            }}/>
                            <p className="m-0 p-0 fs-5">
                                Broj telefona
                            </p>
                        </div>
                        <p className="m-0 p-0 fw-light fs-6">
                            {user.phoneNumber}
                        </p>
                    </div>

                    <div className="d-flex flex-column m-5">
                        <div className="d-flex align-items-center w-100">
                            <BsGeoAlt style={{
                                height: "1.6rem",
                                width: "1.6rem",
                                marginRight: "0.75rem"
                            }}/>
                            <p className="m-0 p-0 fs-5">
                                Adresa
                            </p>
                        </div>
                        <p className="m-0 p-0 fw-light fs-6">
                            {user.address.street + " " + user.address.number + ", " + user.address.place + ", " + user.address.country}</p>


                    </div>

                </div>

            </div>


        </>;
    }


    return html;


}