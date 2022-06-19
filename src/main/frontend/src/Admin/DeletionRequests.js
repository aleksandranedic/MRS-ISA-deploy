import {Sidebar} from "./Sidebar/Sidebar";
import React, {useEffect, useState} from "react";
import {DeletionRequestCard} from "./DeletionRequestCard";
import {backLink} from "../Consts";
import axios from "axios";

export function DeletionRequests() {

    const [requests, setRequests] = useState([])

    const fetchData = () => {
        axios.get(backLink + "/deletionRequests").then(
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
        <div className="w-75">
            {requests.map((request, index) => {
                return (
                    <DeletionRequestCard key={index} request={request}/>
                )
            })}
        </div>
    </div>)
}