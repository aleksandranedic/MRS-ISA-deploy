import CustomCard from "../CustomCard";
import {frontLink} from "../Consts";
import React from "react";

export default function BoatOwnerCard1({boatOwner, editable}) {

    return (<CustomCard
        imagePath={"../images/fishing2.jpg"}
        editable={editable}
        itemToEdit={boatOwner}
        title={boatOwner.firstName + " " + boatOwner.lastName}
        subtitle={""}
        link={frontLink + "boatOwner/" + boatOwner.id}
        type={"boatOwner"}
        address={boatOwner.address.street + " " + boatOwner.address.number +", " + boatOwner.address.place}

    />)


}