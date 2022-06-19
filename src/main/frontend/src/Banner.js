import React from 'react'
import {} from 'bootstrap'
import {} from 'react-bootstrap'
import Carousel from 'react-bootstrap/Carousel'
import boatnotext from './images/boatsnotext.png'
import housenotext from './images/housenotext.png'
import './Banner.css';

export default function Banner({caption}) {
    return (<div>
            <Carousel slide>
                <Carousel.Item>
                    <img className="carousel-image d-block w-100" height="300vh" src={boatnotext}
                        alt="First" style={{opacity:"0.8"}}/>
                    <Carousel.Caption >
                        <h1 style={{fontSize:"70px", fontWeight:"400"}}>{caption}</h1>
                    </Carousel.Caption>
                </Carousel.Item>

                <Carousel.Item>
                    <img className="d-block w-100" height="300vh" src={housenotext}
                         alt="Second" style={{opacity:"0.8"}}
                    />
                    <Carousel.Caption>
                        <h1 style={{fontSize:"70px", fontWeight:"400"}}>{caption}</h1>
                    </Carousel.Caption>
                </Carousel.Item>



            </Carousel>

        </div>


    )
}