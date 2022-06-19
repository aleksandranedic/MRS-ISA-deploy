import React, {useState} from 'react';
import "react-multi-carousel/lib/styles.css";
import './OwnerHouses.css'
import Carousel from 'react-multi-carousel';
import {Container, Button} from "react-bootstrap";
import {BsFillPlusCircleFill} from 'react-icons/bs'

import OwnerHouseCard from "./OwnerHouseCard";
import AddVacationHouse from './AddVacationHouse';
import {responsive} from '../Consts';

function OwnerHouses({houses, myPage}) {
    const [show, setShow] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);


    return (
        <Container className='d-flex p-0' id="houses">
            <Carousel responsive={responsive} interval="250000" className='w-100 h-100  .houses-carousel'
                      autoPlay={false} autoPlaySpeed={9000000}>
                {houses.map((house) => (
                        <div className='house-container' key={house.id}>
                            <OwnerHouseCard
                                id={house.id}
                                pic={house.thumbnailPath}
                                title={house.title}
                                text={house.description}
                                address={house.address}
                            />
                        </div>
                    )
                )}
            </Carousel>

            {myPage &&
                <Button className="btn btn-light add border-radius-lg align-self-center" onClick={handleShow}>
                    <BsFillPlusCircleFill viewBox='0 0 16 16' size={25} fill="#7d7d7d"/>
                </Button>}
            <AddVacationHouse closeModal={handleClose} showModal={show}/>
        </Container>
    )
}

export default OwnerHouses;