import {useEffect, useState} from "react";
import axios from "axios";
import {backLink} from "../Consts";

export function GetAllVacationHouses() {
    const [vacationHouses, setVacationHouses] = useState([]);

    const fetch = () => {
        axios.get(backLink + "/house").then(res => {
            setVacationHouses(res.data);
        });
    };

    useEffect(() => {
        fetch();
    }, []);

    return vacationHouses;
}