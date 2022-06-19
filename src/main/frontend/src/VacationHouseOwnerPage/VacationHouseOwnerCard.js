import CustomCard from "../CustomCard";
import {frontLink} from "../Consts";
import React from "react";

export default function VacationHouseOwnerCard({vacationHouseOwner, editable}) {

    return (<CustomCard
        imagePath={"../images/vikendica2.jpeg"}
        editable={editable}
        itemToEdit={vacationHouseOwner}
        title={vacationHouseOwner.firstName + " " + vacationHouseOwner.lastName}
        subtitle={""}
        link={frontLink + "houseOwner/" + vacationHouseOwner.id}
        type={"houseOwner"}
        address={vacationHouseOwner.address.street + " " + vacationHouseOwner.address.number +", " + vacationHouseOwner.address.place}

    />)


}