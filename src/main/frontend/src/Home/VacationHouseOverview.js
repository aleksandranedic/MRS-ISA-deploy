import {backLink} from "../Consts";
import React, {useEffect, useState} from "react";
import axios from "axios";
import {EntityOverview} from "./EntityOverview";

export function VacationHouseOverview() {

    const [entities, setEntities] = useState([]);
    const fetch = () => {

        axios.get(backLink + "/house/entity",).then(res => {
            setEntities(res.data);
        });
    };

    useEffect(() => {
        fetch()
    }, []);

    return <EntityOverview entities={entities} caption={"Vikendice"}/>
}
