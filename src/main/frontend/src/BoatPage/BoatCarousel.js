import React, {useState} from 'react'
import Carousel from "react-multi-carousel";
import "react-multi-carousel/lib/styles.css";
import AdventureCard from "./BoatCard";
import UpdateBoatForm, {AdventureForm} from "./UpdateBoatForm";
import PlusCard from "../PlusCard";
import BoatCard from "./BoatCard";


export const responsive = {
    superLargeDesktop: {
        // the naming can be any, depends on you.
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

export default function BoatCarousel({boats, add}) {
    const [show, setShow] = useState(false);
    let editable;
    editable = add === true;

    return (<div className="m-5">
            <Carousel responsive={responsive} interval="25000">
                {boats.map((boat) => {
                        return <BoatCard key={boat.id} boat={boat} editable={editable}/>
                    }
                )}
                {
                    add &&
                    <>
                        <PlusCard onClick={() => setShow(true)}/>
                        <UpdateBoatForm show={show} setShow={setShow}/>
                    </>
                }


            </Carousel>
        </div>

    )
}


