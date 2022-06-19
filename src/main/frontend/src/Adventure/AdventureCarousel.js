import React, {useState} from 'react'
import Carousel from "react-multi-carousel";
import "react-multi-carousel/lib/styles.css";
import AdventureCard from "./AdventureCard";
import {AdventureModal} from "./AdventureModal";
import {responsive} from "../Consts";
import {BsFillPlusCircleFill} from "react-icons/bs";
import {Button, Container} from "react-bootstrap";


export default function AdventureCarousel({adventures, add, ownerId}) {
    const [show, setShow] = useState(false);
    let editable;
    editable = add;

    return (<div className="m-5" id="adventures">
        <Container className='d-flex p-0'>
        <Carousel className="w-100 h-100 " responsive={responsive} interval="25000">
                {adventures.map((adventure) => {
                        return <AdventureCard key={adventure.id} adventure={adventure} editable={editable}/>
                    }
                )}



            </Carousel>
            {
                add &&
                <>
                    <Button className="btn btn-light add border-radius-lg align-self-center"
                            onClick={() => setShow(true)}>
                        <BsFillPlusCircleFill viewBox='0 0 16 16' size={25} fill="#7d7d7d"/>
                    </Button>
                    <AdventureModal show={show} setShow={setShow} ownerId={ownerId}/>
                </>
            }
        </Container>
        </div>

    )
}


