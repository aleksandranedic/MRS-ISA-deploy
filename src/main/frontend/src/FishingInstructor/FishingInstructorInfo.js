import React from "react";
import {AddressInfo, Info} from "../Info";



export function FishingInstructorInfo({fishingInstructor}) {
    return (
        <div className="p-5 pt-3 mt-4" id="info">
            <h4 className="fw-light text-center">
                {fishingInstructor.biography}
            </h4>

            <div className="d-flex">
                <div id="left-column" className="w-50 pe-2">
                    <Info title="Ime i prezime" text={fishingInstructor.firstName + " " + fishingInstructor.lastName}/>
                    <Info title="E-mail adresa" text={fishingInstructor.email}/>
                    <Info title="Broj telefona" text={fishingInstructor.phoneNumber}/>
                </div>

                <div id="right-column" className="w-50">
                    <AddressInfo title="Adresa" address={fishingInstructor.address}/>
                </div>
            </div>
        </div>


    )
}