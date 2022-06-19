import React, {useState} from 'react'
import Carousel from "react-multi-carousel";
import VacationHouseOwnerCard from "./VacationHouseOwnerCard";



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

export default function VacationHouseOwnerCarousel({vacationHouseOwners, add}) {
    const [show, setShow] = useState(false);
    let editable;
    editable = add === true;

    return (<div className="m-5">
            <Carousel responsive={responsive} interval="25000">
                {vacationHouseOwners.map((vacationHouseOwner) => {
                        return <VacationHouseOwnerCard key={vacationHouseOwner.id} vacationHouseOwner={vacationHouseOwner} editable={editable}/>
                    }
                )}
                {
                    add &&
                    <>
                        {/*<PlusCard onClick={() => setShow(true)}/>*/}
                        {/*<AdventureForm show={show} setShow={setShow}/>*/}
                    </>
                }


            </Carousel>
        </div>

    )
}


