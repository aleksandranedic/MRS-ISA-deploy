import React from "react";
import 'bootstrap/dist/css/bootstrap.css'
import Tag from "./Tag";
import { IoKeyOutline } from "react-icons/io5";

export function Info({title, text}) {
    return (
        <div className="m-3">
            <p className="lead fw-normal m-0 p-0">{title}</p>
            <p className="fw-light">{text}</p>
        </div>


    )
}

export function LinkInfo({title, text, link, profileImg}) {
    return (
        <div className="d-flex justify-content-end">
            <a href={link} className="d-flex align-items-center" style={{textDecoration: "None", color: "#212529", cursor: "pointer"}}>
                <img className="rounded-circle" src={profileImg} style={{objectFit:"cover",minWidth:"7vh", maxWidth:"7vh", minHeight:"7vh", maxHeight:"7vh"}}/>
                <p className="fw-light m-0 p-0 lead ms-2">{text}</p>
            </a>
        </div>


    )
}



export function AddressInfo({title, address}) {
    return (
        <div className="m-3">
            <p className="lead fw-normal m-0 p-0">{title}</p>
            <p className="fw-light m-0">{address["street"] + " " + address["number"]}</p>
            <p className="fw-light">{address["place"] + ", " + address["country"]}</p>
        </div>
    )
}

export function TagInfo({title, tagList, edit, setState, entity}) {

    function removeTag(id){

        let object = tagList.find((element) => {
            return element.id === id;
        });
        let newTagList = tagList.filter(function (tag) {
            return tag !== object
        });
        if (newTagList.length === 0){
            newTagList = [{id:0, text:''}]
        }
        if (entity === "additionalServices") {
            setAdditionalServices(newTagList);
        }
        if (entity === "navigationEquipment") {
            setNavigationEquipment(newTagList);
        }
        if (entity === "fishingEquipment") {
            setFishingEquipment(newTagList);
        }
    
    }
    function setAdditionalServices(newTagList) {
        setState( prevState => {
            return {...prevState, additionalServices:newTagList}
        });
    }
    function setNavigationEquipment(newTagList) {
        setState( prevState => {
            return {...prevState, navigationEquipment:newTagList}
        });
    }
    function setFishingEquipment(newTagList) {
        setState( prevState => {
            return {...prevState, fishingEquipment:newTagList}
        });
    }
    return (
        <div className="m-0">
            <p className="lead fw-normal m-0 p-0">{title}</p>
            {tagList !== [] && tagList.map((tagData)=> {
                return <Tag key={tagData.id} tag={tagData.text} edit={edit} id={tagData.id} remove={removeTag}/>
            })}
        </div>
    )
}