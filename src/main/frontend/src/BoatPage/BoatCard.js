import React from 'react'
import 'bootstrap/dist/css/bootstrap.css'
import "../CustomCard.css"
import CustomCard from "../CustomCard";
import {backLink, frontLink} from "../Consts";


export default function BoatCard({boat, editable}) {

    return (<CustomCard
        imagePath={backLink + boat.images.at(0).path}
        editable={editable}
        itemToEdit={boat}
        title={boat.title}
        subtitle={boat.description}
        link={frontLink + "boat/" + boat.id}
        type={"boat"}
        address={boat.address.street + " " + boat.address.number + ", " + boat.address.place}
    />)


}