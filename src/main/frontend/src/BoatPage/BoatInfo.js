import {React, useState, useEffect} from "react";
import axios from "axios";
import { useParams } from "react-router-dom";
import {Info, TagInfo, LinkInfo} from "../Info";
import { backLink, frontLink, profilePicturePlaceholder } from "../Consts";
import Map from "../Map"
import 'bootstrap/dist/css/bootstrap.css'
import 'leaflet/dist/leaflet.css'

export default function BoatInfo({boat}) {
    const {id} = useParams();
    const [owner, setOwner] = useState({id:"", name:"", profileImg:null})

    const fetchOwner = () => {
        axios
        .get(backLink + "/boat/getOwner/" + id)
        .then(res => {
            setOwner(res.data);
        });
    };

    useEffect(() => {
        fetchOwner();
    }, []);

    if (typeof boat.navigationEquipment !== 'undefined'){ 
    
    return (
        <div className="p-5 pt-3 mt-4" id="info">
             <div className="d-flex">
               <h4 className="fw-light" style={{ wordWrap: "break-word", width:"80%"}}>{boat.description}</h4> 
               <div className="d-flex align-items-start justify-content-end border-start" style={{width:"20%"}}>
                    <LinkInfo title="Vlasnik:" profileImg = {owner.profileImg !== null ? backLink + owner.profileImg.path : profilePicturePlaceholder} text={owner.name} link={frontLink + "boatOwner/"+ owner.id}/>
               </div>
            </div>
            <hr/>
            <div className="d-flex">
                <div id="left-column" className="w-50 pe-2">
                    <Info title="Tip broda" text={boat.type}/>
                    <Info title="Dužina broda" text={boat.length}/>
                    <Info title="Kapacitet" text={boat.capacity}/>
                    <Info title="Broj motora" text={boat.engineNumber}/>
                    <Info title="Snaga motora" text={boat.engineStrength}/>
                    <Info title="Maksimalna brzina broda" text={boat.topSpeed}/>
                    <Info title="Pravila ponasanja" text={boat.rulesAndRegulations}/>
                </div>

                <div id="right-column" className="w-50">
                    <div className="m-2">
                        <TagInfo title="Navigaciona oprema" tagList={boat.navigationEquipment}/>
                    </div>
                    <div className="m-2">
                        <TagInfo title="Oprema za pecanje" tagList={boat.fishingEquipment}/>
                    </div>
                    <div className="m-2">
                        <TagInfo title="Dodatne usluge" tagList={boat.additionalServices}/>
                    </div>
                    <Info title="Adresa" text={boat.address}/>
                    <div className="w-100" style={{height: "35%"}}>
                        <Map address={boat.address}/>
                    </div>
                </div>
            </div>
        </div>
    )
    }
    else {
        return <></>
    }   
}