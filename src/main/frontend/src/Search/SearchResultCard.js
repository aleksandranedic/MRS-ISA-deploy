import {Card} from "react-bootstrap";
import {backLink, frontLink} from "../Consts";
import StarRatings from "react-star-ratings";
import {BsGeoAlt} from "react-icons/bs"


export function SearchResultCard({item}) {
    return (<Card className="m-3" style={{width: '100%', height: '10%'}}>
        <div className="d-flex" >
            <a href={frontLink + item.entityType + "/" + item.entityId}>
                <Card.Img src={backLink + item.image.path} style={{width: '20vw', height:"20vh", objectFit: 'cover'}}/>

            </a>
            <Card.Body className="d-flex ms-3 align-items-end">
                <div  style={{width: '90%', height: '100%'}}>
                    <div className="mb-3">
                        <a href={frontLink + item.entityType + "/" + item.entityId} style={{textDecoration: "none", color: "black"}}>
                            <Card.Title className="me-5">{item.title}</Card.Title>

                        </a>
                        <StarRatings rating={item.rating} numberOfStars={5} starDimension="20px"
                                     starSpacing="1px" starRatedColor="#f4b136"/>
                    </div>

                    <div className="d-flex w-100 align-items-center">
                        <div className="me-auto d-flex align-items-center">
                            <BsGeoAlt className="me-2" style={{color: "#f4b136"}}/>
                            {item.address.street} {item.address.number}, {item.address.place}, {item.address.country}
                        </div>

                        <div className="d-flex align-items-center">Cena {item.entityType === "house" ? "po danu" : "po satu"}:
                            <div className="ms-3 p-1" style={{background: "#f4b136", borderRadius: "4px", fontWeight: "500", color: "white"}}>{item.price}€</div>
                        </div>
                    </div>
                </div>
            </Card.Body>
        </div>

    </Card>);
}