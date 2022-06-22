import {backLink} from "../Consts";
import React, {useEffect, useState} from "react";
import axios from "axios";
import {EntityOverview} from "./EntityOverview";

export function BoatOverview() {


    const [entities, setEntities] = useState([]);
    const fetch = () => {

        axios.get(backLink + "/boat/entity",).then(res => {
            setEntities(res.data);
        });
    };

    useEffect(() => {
        fetch()
    }, []);

    return <EntityOverview entities={entities} caption={"Brodovi"}/>
}
