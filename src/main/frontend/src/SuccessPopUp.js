import React from 'react'
import {BsCheck2Circle} from "react-icons/bs";

export default function SuccessPopUp() {
    return (
        <div className="d-flex flex-row w-auto border border-2">
            <div className="justify-content-center" style={{background: "green", width: "45px"}}>
                <BsCheck2Circle size="2rem" color="white"/>
            </div>
            <div>
                <div className="d-flex flex-column px-3">
                    <h5 className="p-0 m-0">Success</h5>
                    <small>poruka da li ste uspeli</small>

                </div>
            </div>
        </div>
    )
}
