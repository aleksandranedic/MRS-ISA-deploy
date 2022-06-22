import React from "react";
import {Info, AddressInfo, TagInfo, LinkInfo} from "../Info";
import 'bootstrap/dist/css/bootstrap.css'
import Map from "../Map";
import { backLink, frontLink, profilePicturePlaceholder } from "../Consts";

export default function AdventureInfo({adventure}) {

    return (
        <div id="info" className="p-5 pt-3 mt-4">
            <div className="d-flex">
               <h4 className="fw-light" style={{ wordWrap: "break-word", width:"80%"}}>{adventure.description}</h4> 
               <div className="d-flex align-items-start justify-content-end border-start" style={{width:"20%"}}>
                    <LinkInfo title="Vlasnik:" profileImg = {adventure.owner.profileImg !== null ? backLink + adventure.owner.profileImg.path : profilePicturePlaceholder} text={adventure?.owner.firstName + " " + adventure?.owner.lastName} link={frontLink + "fishingInstructor/"+adventure.owner.id}/>
               </div>
            </div>

            <div className="d-flex mt-2">
                <div id="left-column" className="w-50 pe-2">
                    <Info title="Broj klijenata" text={adventure?.numberOfClients}/>
                    <div className="m-3">
                        <TagInfo title="Oprema za pecanje" tagList={adventure?.fishingEquipment}/>
                    </div>
                    <div className="m-3">
                        <TagInfo title="Dodatne usluge" tagList={adventure?.additionalServices}/>
                    </div>
                    <Info title="Pravila ponasanja" text={adventure?.rulesAndRegulations}/>
                   
                </div>

                <div id="right-column" className="w-50">
                    <Info title="Adresa" text={adventure.address.street + " " +  adventure.address.number + ", " + adventure.address.place + ", " + adventure.address.country}/>
                        <div className="w-100" style={{height: "70%"}}> 
                            <Map address={adventure.address.street + " " +  adventure.address.number + ", " + adventure.address.place + ", " + adventure.address.country}/>
                        </div>
                </div>
            </div>
        </div>


    )
}