import {React, useEffect, useState} from 'react';
import { useParams } from "react-router-dom";
import axios from "axios";
import StarRatings from 'react-star-ratings';
import { Line } from 'rc-progress';
import { AiFillStar } from "react-icons/ai";
import { backLink } from '../Consts';

function ReviewScores({type}) {

    const [scores, setScores] = useState({fiveStars:0, fourStars:0, threeStars:0, twoStars:0, oneStars:0})
    const [rating, setRating] = useState(0)
    const {id} = useParams();
    
    const fetchResourceScores = () => {
        axios
        .get(backLink + "/review/getReviewScores/" + id)
        .then(res => {
            setScores(res.data);
        });
    };

    const fetchResourceRating = () => {
        axios
        .get(backLink + "/review/getRating/" + id)
        .then(res => {
            setRating(res.data);
        });
    };

    const fetchVendorScores = () => {
        axios
        .get(backLink + "/review/getVendorReviewScores/" + id)
        .then(res => {
            setScores(res.data);
        });
    };

    const fetchVendorRating = () => {
        axios
        .get(backLink + "/review/getVendorRating/" + id)
        .then(res => {
            setRating(res.data);
        });
    };

    useEffect(() => {
        if (type === "vacationHouse" || type === "boat" || type === "adventure"){
            fetchResourceScores();
            fetchResourceRating();
        } else {
            fetchVendorScores();
            fetchVendorRating();
        }
    }, []);

    return (
        <div className="w-75 m-2" style={{border:"1px solid rgba(0, 0, 0, 0.125)", borderRadius: "15px"}}>
            <div className='d-flex' style={{width:"auto"}}>
                <div className='w-25 d-flex flex-column align-items-center justify-content-center p-4 pb-5' style={{borderRight:"1px solid rgba(0, 0, 0, 0.125)"}}>
                    <p className="m-0" style={{blockSize:"fit-content", fontWeight: "400", fontSize: "100px", color:"#313041"}} >{rating}</p>
                    <StarRatings rating={rating} starDimension="27px" starSpacing="1px" starRatedColor="#f4b136"/>
                </div>
                <div className='w-75 p-5'>
                    <div>
                        <div className='d-flex justify-content-between m-0 p-0'>
                        <span className='d-flex'>
                                <p className='text-secondary m-0 p-0' style={{marginRigth:"5px"}}>5</p>
                                <p className='text-secondary m-0 p-0'><AiFillStar className='pb-1' fill='#f4b136' size={21}/></p>
                            </span>
                            <span className='d-flex'>
                                <p className='text-secondary m-0 p-0'>{scores.fiveStars}</p>
                                <p className='text-secondary m-0 p-0'>%</p>
                            </span>
                        </div>
                        <Line className="mb-3" percent={scores.fiveStars} strokeWidth={1} strokeColor="#0d6efd" />
                    </div>

                    <div>
                        <div className='d-flex justify-content-between m-0 p-0'>
                        <span className='d-flex'>
                                <p className='text-secondary m-0 p-0' style={{marginRigth:"5px"}}>4</p>
                                <p className='text-secondary m-0 p-0'><AiFillStar className='pb-1' fill='#f4b136' size={21}/></p>
                            </span>
                            <span className='d-flex'>
                                <p className='text-secondary m-0 p-0'>{scores.fourStars}</p>
                                <p className='text-secondary m-0 p-0'>%</p>
                            </span>
                        </div>
                        <Line className="mb-3" percent={scores.fourStars} strokeWidth={1} strokeColor="#0d6efd" />
                    </div>

                    <div>
                        <div className='d-flex justify-content-between m-0 p-0'>
                            <span className='d-flex'>
                                <p className='text-secondary m-0 p-0' style={{marginRigth:"5px"}}>3</p>
                                <p className='text-secondary m-0 p-0'><AiFillStar className='pb-1' fill='#f4b136' size={21}/></p>
                            </span>
                            <span className='d-flex'>
                                <p className='text-secondary m-0 p-0'>{scores.threeStars}</p>
                                <p className='text-secondary m-0 p-0'>%</p>
                            </span>
                        </div>
                        <Line className="mb-3" percent={scores.threeStars} strokeWidth={1} strokeColor="#0d6efd" />
                    </div>

                    <div>
                        <div className='d-flex justify-content-between m-0 p-0'>
                        <span className='d-flex'>
                                <p className='text-secondary m-0 p-0' style={{marginRigth:"5px"}}>2</p>
                                <p className='text-secondary m-0 p-0'><AiFillStar className='pb-1' fill='#f4b136' size={21}/></p>
                            </span>
                            <span className='d-flex'>
                                <p className='text-secondary m-0 p-0'>{scores.twoStars}</p>
                                <p className='text-secondary m-0 p-0'>%</p>
                            </span>
                        </div>
                        <Line className="mb-3" percent={scores.twoStars} strokeWidth={1} strokeColor="#0d6efd" />
                    </div>
                    
                    <div>
                        <div className='d-flex justify-content-between m-0 p-0'>
                        <span className='d-flex'>
                                <p className='text-secondary m-0 p-0' style={{marginRigth:"5px"}}>1</p>
                                <p className='text-secondary m-0 p-0'><AiFillStar className='pb-1' fill='#f4b136' size={21}/></p>
                            </span>
                            <span className='d-flex'>
                                <p className='text-secondary m-0 p-0'>{scores.oneStars}</p>
                                <p className='text-secondary m-0 p-0'>%</p>
                            </span>
                        </div>
                        <Line className="mb-3" percent={scores.oneStars} strokeWidth={1} strokeColor="#0d6efd" />
                    </div>                
                </div>
            </div>
        </div>
    );
}

export default ReviewScores;