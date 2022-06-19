import React from 'react';
import {Card} from 'react-bootstrap'
import { frontLink } from '../Consts';

function ChooseIncomeCard({setReportType}) {
    var linkHouse = frontLink + "/images/houseIncomeCopy.jpg"
    var linkBoat = frontLink + "/images/boatIncomeCopy.jpg"
    var linkAdventure = frontLink + "/images/adventureIncome.jpg"
    var linkIncome = frontLink + "/images/allIncome.jpg"
    
    const Select = (idItem) => {
        var ids = ["house", "boat", "adventure", "all"];
        for (var id of ids){
            if (id === idItem) {
                document.getElementById(id).classList.remove("unselected");
                document.getElementById(id).classList.add("selected");
                setReportType(id);
            }
            else {
                document.getElementById(id).classList.add("unselected");  
                document.getElementById(id).classList.remove("selected");              
            }
        }
    }

    return (
        <div className='d-flex m-0 p-0 justify-content-between adminIncome'>
            <Card id="house" className="bg-white text-white h-100 border-0 m-2 ms-4" style={{borderRadius: "15PX"}} onClick={e => Select("house")}>
            <Card.Img src={linkHouse} alt="Card image" style={{objectFit:"cover",borderRadius: "15PX",maxWidth:"37vh", minWidth:"37vh",maxHeight:"15vh", minHeight:"15vh", opacity:"0.6"}}/>
            <Card.ImgOverlay className="d-flex justify-content-center align-items-center">
                <Card.Title className="display-6" style={{ fontWeight:"500"}}>Vikendice</Card.Title>                            
            </Card.ImgOverlay>
            </Card>
            <Card id="boat" className="bg-white text-white h-100 border-0 m-2" style={{borderRadius: "15PX"}} onClick={e => Select("boat")}>
            <Card.Img src={linkBoat} alt="Card image" style={{objectFit:"cover",borderRadius: "15PX",maxWidth:"37vh", minWidth:"37vh",maxHeight:"15vh", minHeight:"15vh", opacity:"0.6"}}/>
            <Card.ImgOverlay className="d-flex justify-content-center align-items-center">
                <Card.Title className="display-6" style={{ fontWeight:"500"}}>Brodovi</Card.Title>                            
            </Card.ImgOverlay>
            </Card>
            <Card id="adventure" className="bg-white text-white h-100 border-0 m-2" style={{borderRadius: "15PX"}} onClick={e => Select("adventure")}>
            <Card.Img src={linkAdventure} alt="Card image" style={{objectFit:"cover",borderRadius: "15PX",maxWidth:"37vh", minWidth:"37vh",maxHeight:"15vh", minHeight:"15vh", opacity:"0.7"}}/>
            <Card.ImgOverlay className="d-flex justify-content-center align-items-center">
                <Card.Title className="display-6" style={{ fontWeight:"500"}}>Avanture</Card.Title>                            
            </Card.ImgOverlay>
            </Card>
            <Card id="all" className="bg-white text-white h-100 border-0 m-2 me-4" style={{borderRadius: "15PX"}} onClick={e => Select("all")}>
            <Card.Img src={linkIncome} alt="Card image" style={{objectFit:"cover",borderRadius: "15PX",maxWidth:"37vh", minWidth:"37vh",maxHeight:"15vh", minHeight:"15vh", opacity:"0.7"}}/>
            <Card.ImgOverlay className="d-flex justify-content-center align-items-center">
                <Card.Title className="display-6" style={{ fontWeight:"500"}}>Svi prihodi</Card.Title>                            
            </Card.ImgOverlay>
            </Card>
        </div>
    );
}

export default ChooseIncomeCard;