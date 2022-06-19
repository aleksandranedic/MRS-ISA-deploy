import React from 'react';
import {Container, Image} from'react-bootstrap'
import Carousel from 'react-multi-carousel'
import {BsTrashFill} from 'react-icons/bs'
import {backLink} from "../Consts";


function AdventureFormImages({images, setImages}) {
    if (!images) {
        images = [];
    }

    const responsive = {
        superLargeDesktop: {
            breakpoint: {max: 4000, min: 3000},
            items: 3
        },
        desktop: {
            breakpoint: {max: 3000, min: 1400},
            items: 3
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
    function removePhoto(event, image){
        const imageList = images.filter((img) => img !== image);
        setImages(imageList);
    }




    return (
        <Container className='d-flex p-0 house-images-main-container'>
            <Carousel responsive={responsive} interval="250000" className='carousel w-100 h-100' autoPlay={false} autoPlaySpeed={9000000}>
                {images.map( (image) => (
                    <div className='image-card mb-2' key={image} style={{width: "16vw", height: "25vh"}}>
                        <BsTrashFill className="delete-photo" style={{cursor: "pointer"}} onClick={event => removePhoto(event, image)}/>
                        <Image src={image} style={{width: "100%", height: "100%",objectFit: "cover", transform: "translateY(-4vh)"}}/>
                    </div>
                ))}
            </Carousel>
        </Container>
    );
}

export default AdventureFormImages;