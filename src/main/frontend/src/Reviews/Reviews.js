import React from 'react';
import {Row, Button} from 'react-bootstrap'
import Review from './Review';
import { MdOutlineKeyboardArrowDown, MdKeyboardArrowUp } from "react-icons/md";

function toggleReviews() {
    var text = document.getElementById("expandButton").innerHTML;
    if (text === "See more"){
        var fadeout = document.getElementsByClassName("fadeout")[0];
        fadeout.style.display = "none";
        var reviews = document.getElementById("allReviews");
        reviews.style.overflow = "visible";
        reviews.style.maxHeight = "fit-content"
        var arrowUp = document.getElementById("arrowUp");
        arrowUp.style.display = "flex";
        var arrowDown = document.getElementById("arrowDown");
        arrowDown.style.display = "none";
        document.getElementById("expandButton").innerHTML = "See less"
    }
    else {
        var fadeout = document.getElementsByClassName("fadeout")[0];
        fadeout.style.display = "block";
        var reviews = document.getElementById("allReviews");
        reviews.style.overflow = "hidden";
        reviews.style.maxHeight = "30vh"
        var arrowUp = document.getElementById("arrowUp");
        arrowUp.style.display = "none";
        var arrowDown = document.getElementById("arrowDown");
        arrowDown.style.display = "flex";
        document.getElementById("expandButton").innerHTML = "See more"   
    }
}
function Reviews({reviews}) {
    return (
        <div className='d-flex flex-column'>
            <Row id="allReviews" xs={1} md={2} className="g-4 mt-3" style={{position:"relative", maxHeight:"30vh", minHeight:"30vh",overflow:"hidden"}}>
                {reviews.map( (review) => (
                    <Review key={review.id} review={review}/>                      
                ))}
                <div className="fadeout"></div>
            </Row>
            {  reviews.length > 2 ?
                
                <div onClick={e => toggleReviews()} className="d-flex flex-column align-self-center justify-content-center toggleReviews" style={{cursor:"pointer", zIndex:"100"}}>
                    <p id="arrowUp" className='m-0 p-0 justify-content-center' style={{display:"none"}}><MdKeyboardArrowUp/></p>
                    <p id="expandButton" className='text-secondary m-0 p-0'>See more</p>
                    <p id="arrowDown" className='m-0 p-0 justify-content-center' style={{display:"flex"}}><MdOutlineKeyboardArrowDown/></p>
                </div>
                : <></>
            }
        </div>
    );
}

export default Reviews;