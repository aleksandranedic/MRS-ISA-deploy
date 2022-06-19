import {Card} from "react-bootstrap";
import {backLink, frontLink} from "../Consts";
import StarRatings from "react-star-ratings";

export default function SubscriptionCard({entity}) {

    return <a href={frontLink + entity.entityType + "/" + entity.entityId}>

        <Card className="m-3 ms-5 me-5"

              style={{
                  height: "30vh",
                  width: "22vw",
                  border: "none"
              }} >

            <Card.Img src={backLink + entity.image.path} alt="Card image" style={{height: "30vh", width: "22vw"}}/>


            <Card.ImgOverlay>
                <div className="d-flex text-white flex-column align-items-center justify-content-center h-100">
                    <Card.Title style={{fontSize: "5vh"}}>{entity.title}</Card.Title>
                    <StarRatings rating={entity.rating} numberOfStars={5} starDimension="32px"
                                 starSpacing="1px" starRatedColor="#f4b136"/>
                </div>
            </Card.ImgOverlay>


    </Card></a>
}