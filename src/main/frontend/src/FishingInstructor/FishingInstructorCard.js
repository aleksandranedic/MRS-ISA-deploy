import React from 'react'
import 'bootstrap/dist/css/bootstrap.css'

import CustomCard from "../CustomCard";
import {frontLink} from "../Consts";


export default function FishingInstructorCard({fishingInstructor, editable}) {

    return (<CustomCard
        imagePath={"../images/fishing2.jpg"}
        editable={editable}
        itemToEdit={fishingInstructor}
        title={fishingInstructor.firstName + " " + fishingInstructor.lastName}
        subtitle={fishingInstructor.biography}
        link={frontLink + "fishinginstructor/" + fishingInstructor.id}
        type={"fishingInstructor"}
        address={fishingInstructor.address.street + " " + fishingInstructor.address.number +", " + fishingInstructor.address.place}

    />)


}