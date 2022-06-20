import {useEffect, useState} from "react";
import axios from "axios";
import {backLink} from "../Consts";

export function GetAllBoats() {
    const [boats, setBoats] = useState([]);

    const fetchAdventures = () => {
        axios.get(backLink + "/boat").then(res => {
            setBoats(res.data);
        });
    };

    useEffect(() => {
        fetchAdventures();
    }, []);

    return boats;
}