import {Sidebar} from "./Sidebar/Sidebar";
import React, {useEffect, useState} from "react"
import {RegistrationRequestCard} from "./RegistrationRequestCard";
import {Button, Modal} from "react-bootstrap";
import {backLink} from "../Consts";
import axios from "axios";

export function RegistrationRequests() {

    const [requests, setRequests] = useState([])

    const fetchData = () => {
        axios.get(backLink+"/vendorRegistration").then(
            response => {
                setRequests(response.data)
            }
        )
    }

    useEffect(() => {
        fetchData()
    }, [])

    return (<div className="d-flex" style={{height: "100vh"}}>
        <div className="w-25" style={{backgroundColor: "#f7f8f9"}}>
            <Sidebar/>
        </div>
        <div className="w-75 overflow-auto">

            {requests.map((request,index) => {
                return (
                    <RegistrationRequestCard key={index} props={request}  />
                )
            })}

        </div>
    </div>)
}