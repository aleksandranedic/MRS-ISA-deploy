import React from 'react';
import {Container} from "react-bootstrap";
import ImageGallery from 'react-image-gallery'
import './ImageGallery.css';

export default function ImagesGallery({images}) {
  return (
    <Container className= "p-0 m-0 w-100" id="gallery">
       <ImageGallery items={images} />
    </Container>
  );
}