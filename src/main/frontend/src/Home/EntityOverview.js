import Banner from "../Banner";
import Navigation from "../Navigation/Navigation";
import {Button, Card} from "react-bootstrap";
import React, {useState} from "react";
import {backLink, frontLink} from "../Consts";
import StarRatings from "react-star-ratings";
import {BsGeoAlt} from "react-icons/bs";
import "../Admin/OverviewCard.css"

export function EntityOverview({entities, caption}) {

    const [sortBy, setSortBy] = useState("");


    function sortByPrice() {
        setSortBy("price");
    }

    function sortByRating() {
        setSortBy("rating");
    }

    function sortByTitle() {
        setSortBy("title");
    }

    let html;
    if (typeof entities !== "undefined") {
        html = <>

            <Banner caption={caption}/>

            <Navigation buttons={[]}
                        editable={false} searchable={true}
            />

            <div className="d-flex align-items-center mt-3" style={{marginLeft: "15%", marginRight: "15%"}}>

                <div className="ms-auto d-flex align-items-center">Sortiraj po:</div>
                <Button onClick={sortByPrice} variant="secondary" className="m-2">Cena</Button>
                <Button onClick={sortByRating} variant="secondary" className="m-2">Ocena</Button>
                <Button onClick={sortByTitle} variant="secondary" className="m-2">Naziv</Button>
            </div>
            {sortBy === "" &&
                <Entities entities={entities}/>
            }
            {sortBy === "price" &&
                <Entities
                    entities={entities.sort((a,b) => (a.price > b.price) ? 1 : ((b.price > a.price) ? -1 : 0))}
                />
            }
            {sortBy === "rating" &&
                <Entities
                    entities={entities.sort((a,b) => (a.rating < b.rating) ? 1 : ((b.rating < a.rating) ? -1 : 0))}
                />
            }
            {sortBy === "title" &&
                <Entities
                    entities={entities.sort((a,b) => (a.title > b.title) ? 1 : ((b.title > a.title) ? -1 : 0))}
                />
            }

        </>;
    }

    return html
}



function Entities({entities}) {
    return <div style={{marginLeft: "15%", marginRight: "15%"}}>
        {entities.map((item, index) => {
            return <Card key={index} className="d-flex m-3"
                         style={{borderRadius: "0", border: "none", margin: "5vh"}}>

                <Card.Img className="overview-image" src={backLink + item.image.path}
                />

                <Card.ImgOverlay className="overlay">
                    <div className="d-flex m-5 mb-0">
                        <a className="text-decoration-none d-flex "
                           href={frontLink + item.entityType + "/" + item.entityId}>
                            <h1 className="title-link text-light ">{item.title}</h1>

                        </a>

                    </div>

                    <div className="m-5">
                        <StarRatings rating={item.rating} numberOfStars={5} starDimension="30px"
                                     starSpacing="2px" starRatedColor="#f4b136"/>
                    </div>

                    <div className="d-flex ms-5 me-5 align-items-center">
                        <div className="me-auto d-flex align-items-center" style={{color: "#ffffff", fontSize: "20px"}}>
                            <BsGeoAlt className="me-2" style={{color: "#f4b136"}}/>
                            {item.address.street} {item.address.number}, {item.address.place}, {item.address.country}
                        </div>

                        <div className="d-flex align-items-center" style={{color: "#ffffff", fontSize: "20px"}}>Cena {item.entityType === "house" ? "po danu" : "po satu"}:
                            <div className="ms-3 p-1" style={{background: "#f4b136", borderRadius: "4px",fontSize: "20px", fontWeight: "500", color: "white"}}>{item.price}€</div>
                        </div>
                    </div>

                </Card.ImgOverlay>

            </Card>
        })}
    </div>;
}