import React from 'react'
import 'bootstrap/dist/css/bootstrap.css'
import "../CustomCard.css"
import CustomCard from "../CustomCard";
import {backLink, frontLink} from "../Consts";


export default function AdventureCard({adventure, editable}) {



    return (<CustomCard
        imagePath={backLink + adventure.images.at(0).path}
        editable={editable}
        itemToEdit={adventure}
        title={adventure.title}
        subtitle={adventure.description}
        link={frontLink + "adventure/" + adventure.id}
        type={"adventure"}
        address={adventure.address.street + " " + adventure.address.number +", " + adventure.address.place}


    />)


}