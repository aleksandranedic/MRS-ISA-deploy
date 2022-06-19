import {React, useState, useEffect} from "react";
import axios from "axios";
import { useParams } from "react-router-dom";
import { backLink, frontLink, profilePicturePlaceholder } from "../Consts";
import {Info, TagInfo, LinkInfo} from "../Info";
import 'bootstrap/dist/css/bootstrap.css'
import Map from "../Map";

export default function HouseInfo({house}) {
    const {id} = useParams();
    const [owner, setOwner] = useState({id:"", name:"", profileImg:null})

    const fetchOwner = () => {
        axios
        .get(backLink + "/house/getOwner/" + id)
        .then(res => {
            setOwner(res.data);
        });
    };

    useEffect(() => {
        fetchOwner();
    }, []);

    if (typeof house.additionalServices !== 'undefined'){
    return (
        <div className="p-5 pt-3 mt-4" id="info">
            <div className="d-flex">
               <h4 className="fw-light" style={{ wordWrap: "break-word", width:"80%"}}>{house.description}</h4> 
               <div className="d-flex align-items-start justify-content-end border-start" style={{width:"20%"}}>
                    <LinkInfo title="Vlasnik:" profileImg = {owner.profileImg !== null ? backLink + owner.profileImg.path : profilePicturePlaceholder} text={owner.name} link={frontLink + "houseOwner/"+ owner.id}/>
               </div>
            </div>
            <hr/>
            <div className="d-flex">
                <div id="left-column" className="w-50 pe-2">
                    <Info title="Broj soba" text={house.numberOfRooms}/>
                    <Info title="Kapacitet" text={house.capacity}/>
                    <Info title="Pravila ponasanja" text={house.rulesAndRegulations}/>
                    <div className="m-3">
                        <TagInfo title="Dodatne usluge" tagList={house.additionalServices}/>
                    </div>
                </div>

                <div id="right-column" className="w-50">
                <Info title="Adresa" text={house.address}/>
                    <div className="w-100" style={{height: "70%"}}> 
                        <Map address={house.address}/>
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