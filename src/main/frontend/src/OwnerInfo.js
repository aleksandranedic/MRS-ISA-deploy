import React from 'react';
import StarRatings from 'react-star-ratings';
import ProfileInfo from './ProfileInfo'
import {BsEnvelope, BsTelephone, BsGeoAlt} from 'react-icons/bs'
import {MdStars, MdDoNotDisturbOn} from "react-icons/md";
import {Badge} from "react-bootstrap";

function OwnerInfo({name, rate, bio, email, phoneNum, address, profileImg, category, points, penalty}) {
    return (
        <div className = "d-flex align-items-center justify-content-center ps-2 m-5" id="info">
            <div className='pt-4 ps-0 d-flex justify-content-center me-2' style={{width:"15%"}}>
                <img className="rounded-circle" style={{objectFit: "cover", maxWidth: "30vh", minWidth: "30vh", maxHeight: "30vh", minHeight: "30vh"}} src={profileImg}/>
            </div>
            <div className='d-flex flex-column align-items-center justify-content-center ps-2 m-0 w-100' style={{width:"85%"}}>
                <div className='h-75 w-100'>
                    <div className='pt-3 ps-0 d-flex justify-content-between' style={{width:"100%"}}>
                        <div className="d-flex align-items-center justify-content-around infoDiv" style={{width:"100%"}}>
                            <div className="d-flex flex-column justify-content-start">
                                <h2 className="fw-normal p-0 m-0 pt-2">{name}</h2>
                                <div className="d-flex align-items-center">
                                    <p className="fw-bold m-0 mt-1 p-0"> {rate} </p>
                                    <StarRatings rating={rate} starDimension="17px" starSpacing="1px" starRatedColor="#f4b136"/>
                                </div>
                            </div>
                        </div>
                        <ProfileInfo icon={BsEnvelope} label={"Email"} text={email}/>
                        <ProfileInfo icon={BsTelephone} label={"Broj telefona"} text={phoneNum}/>
                        <ProfileInfo icon={BsGeoAlt} label={"Adresa"} text={address.street + " " + address.number + ", " + address.place +", " + address.country}/>
                    </div>
                </div>

                    <div className='h-25 w-100 d-flex'>
                        {  typeof category != "undefined" ?
                        <div className="mt-4 ms-3 w-50 d-flex align-items-center">
                            <div>
                                <div className="d-flex align-items-center justify-content-center"
                                     style={{
                                         background: category.color,
                                         width: "8vw",
                                         borderRadius: "3px",
                                         color: "white",
                                         fontWeight: "500"
                                     }}>
                                    {category.name}
                                </div>
                            </div>

                            <div className="d-flex align-items-center">

                                <div className="d-flex">
                                    <MdStars className="pe-2 ms-3" style={{width: "4vh", height: "4vh", color: "#f4b136"}}/>
                                    {points}

                                </div>
                                {typeof penalty != "undefined" &&
                                    <div className="d-flex">
                                        <MdDoNotDisturbOn className="pe-2 ms-3" style={{width: "4vh", height: "4vh", color: "#ff4242"}}/>
                                        {penalty}

                                    </div>
                                }
                            </div>
                        </div>:<></>}
                        {  typeof bio != "undefined" ?
                        <div className="mt-4 ms-3 w-50">
                            <p   style={{ textAlign: 'justify', textJustify: 'inter-word', fontSize: "18px"}}>
                                {bio}
                            </p>
                        </div>: <></>}


                    </div>


            </div>
        </div>
    );
}

export default OwnerInfo;