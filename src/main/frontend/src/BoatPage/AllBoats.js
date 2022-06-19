import {useEffect, useState} from "react";
import axios from "axios";
import {backLink} from "../Consts";

export function GetAllBoats() {
    const [boats, setBoats] = useState([]);

    const fetchAdventures = () => {
        axios.get(backLink + "/boat", {headers:{'Access-Control-Allow-Origin': '*'}}).then(res => {
            setBoats(res.data);
        });
    };

    useEffect(() => {
        fetchAdventures();
    }, []);

    return boats;
}