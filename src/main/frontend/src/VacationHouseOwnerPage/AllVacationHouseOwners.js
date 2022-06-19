import {useEffect, useState} from "react";
import axios from "axios";
import {backLink} from "../Consts";

export function GetAllVacationHouseOwners() {
    const [vacationHouseOwners, setVacationHouseOwners] = useState([]);

    const fetch = () => {
        axios.get(backLink + "/houseowner").then(res => {
            setVacationHouseOwners(res.data);
        });
    };

    useEffect(() => {
        fetch();
    }, []);

    return vacationHouseOwners;
}