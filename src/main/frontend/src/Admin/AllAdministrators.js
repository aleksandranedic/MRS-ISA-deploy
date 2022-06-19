import {useEffect, useState} from "react";
import axios from "axios";
import {backLink} from "../Consts";

export function GetAllAdministrators() {
    const [admins, setAdmins] = useState([]);

    const fetch = () => {
        axios.get(backLink + "/admin").then(res => {
            setAdmins(res.data);
        });
    };

    useEffect(() => {
        fetch();
    }, []);

    return admins;
}