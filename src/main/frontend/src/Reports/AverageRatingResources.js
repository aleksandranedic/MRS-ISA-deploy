import React from 'react';
import { Card } from 'react-bootstrap';
import StarRatings from 'react-star-ratings';
import {backLink, frontLink } from '../Consts';

function AverageRatingResources({resources, type}) {

    const visitResource = (id) => {
        var link = frontLink + type + "/" + id
        window.location.href = link;
    }

    return (
        <div className='m-5'>
         <div className='d-flex ms-3 p-0 mb-2'>
            <div className="d-flex">
                <p className='lead p-0 m-0'>Ukupan broj resursa:</p>
                <p className='lead p-0 m-0 ms-1' > {resources.length}</p>
            </div>
         </div>
         {resources.map( (resource) => (
                <Card id="ResourceReportCard" className='m-3 d-flex flex-direction-row' key={resource.id} onClick={e => visitResource(resource.id)} style={{cursor:"pointer"}}>
                <div className='w-50 d-flex align-items-center'>
                    <Card.Img variant="left" src={backLink + resource.image.path} style={{minWidth:"10vh", maxWidth:"10vh", minHeight:"12vh", maxHeight:"12vh"}}/>
                    <p className='lead ps-4 m-0'>{resource.name}</p>
                </div>
                <div className='d-flex w-50 align-items-center justify-content-end p-0 m-0'>
                    <div className="pe-4">
                        <StarRatings rating={resource.rating} starDimension="20px" starSpacing="1px" starRatedColor="#f4b136"/>
                    </div>
                    <p className='lead m-0 ps-2 pt-0 pb-0 pt-1' style={{paddingRight:"18%"}}> {resource.rating}</p>
                </div>
            </Card>                                                
        ))}
       </div>
    );
}

export default AverageRatingResources;