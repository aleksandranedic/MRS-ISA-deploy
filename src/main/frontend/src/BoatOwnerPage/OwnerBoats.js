import React, { useState } from 'react';
import "react-multi-carousel/lib/styles.css";
import '../VacationHouseOwnerPage/OwnerHouses.css'
import Carousel from 'react-multi-carousel';
import {Container, Button} from "react-bootstrap";
import {BsFillPlusCircleFill} from 'react-icons/bs'

import BoatOwnerCard from "./BoatOwnerCard";
import AddBoat from './AddBoat';

function OwnerBoats({boats}) {
  const [show, setShow] = useState(false);
  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  const responsive = {
    superLargeDesktop: {
        breakpoint: {max: 4000, min: 3000},
        items: 5
    },
    desktop: {
        breakpoint: {max: 3000, min: 1400},
        items: 4
    },
    desktop2: {
        breakpoint: {max: 1400, min: 1024},
        items: 3
    },
    tablet: {
        breakpoint: {max: 1024, min: 700},
        items: 2
    },
    mobile: {
        breakpoint: {max: 700, min: 0},
        items: 1
    }
};

    return (
       <Container className='d-flex p-0' id="boats">            
            <Carousel responsive={responsive} interval="250000" className='w-100 h-100  .houses-carousel' autoPlay={false} autoPlaySpeed={9000000}>    
            {boats.map( (boat) => (
            <div className='house-container' key={boat.id}>
                    <BoatOwnerCard
                        id={boat.id}
                        pic={boat.thumbnailPath}
                        title={boat.title}
                        text={boat.description}
                        address={boat.address}
                        />
            </div>               
            )   
        )}
            </Carousel> 
            <Button className="btn btn-light add border-radius-lg align-self-center" onClick={handleShow}>
                <BsFillPlusCircleFill viewBox='0 0 16 16' size={25} fill="#7d7d7d"/>          
            </Button> 
            <AddBoat closeModal={handleClose} showModal={show}/>
    </Container>  
    )
}

export default OwnerBoats;