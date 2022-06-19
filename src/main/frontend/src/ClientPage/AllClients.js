import {useEffect, useState} from "react";
import axios from "axios";
import {backLink} from "../Consts";

export function GetAllClients() {
    const [clients, setClients] = useState([]);

    const fetch = () => {
        axios.get(backLink + "/client").then(res => {
            setClients(res.data);
        });
    };

    useEffect(() => {
        fetch();
    }, []);

    return clients;
}