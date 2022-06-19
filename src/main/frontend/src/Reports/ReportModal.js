import axios from 'axios'
import {React, useState, useEffect} from 'react';
import {Modal, Tabs, Tab, Button} from 'react-bootstrap'
import { ReservationsTable } from '../Calendar/ReservationsTable';
import {useParams} from 'react-router-dom'
import { backLink } from '../Consts';
import AverageRatingResources from './AverageRatingResources';
import IncomeReport from './IncomeReport';
import AttendanceReport from './AttendanceReport';

function ReportModal({closeModal, showModal, type}) {
    const [reservations, setReservations] = useState([]);
    const [resources, setResources] = useState([])
    const {id} = useParams();

    const fetchReservations = () => {
        var link;
        if (type==="adventure")
            link = backLink + "/adventure/reservation/fishingInstructor/" + id
        else if (type==="boat")
            link = backLink + "/boat/reservation/boatOwner/" + id
        else
            link = backLink + "/house/reservation/vacationHouseOwner/" + id
        axios.get(link).then(res => {        
            setReservations(res.data);
        })
    }

    const fetchResources = () => {
        axios.get(backLink + "/" + type + "/getOwnerResources/" + id).then(res => {       
            setResources(res.data);
        })
    }

    useEffect(() => {
        fetchReservations();
        fetchResources();
    }, []);

    return (
        <Modal  show={showModal} onHide={closeModal} size="xl">  
        <Button className='btn-close' aria-label='Close' id="closeReportsModal" onClick={closeModal}/>
        <Modal.Body  id="reports-modal">
            <Tabs defaultActiveKey="IstorijaRezervacija" id="uncontrolled-tab-example" className="mb-3">
                <Tab eventKey="IstorijaRezervacija" title="Istorija rezervacija"> 
                    <ReservationsTable reservations={reservations} showResource={false}/> 
                </Tab>
                <Tab  eventKey="ProsecnaOcena" title="Prosečne ocene">
                    <AverageRatingResources resources={resources} type={type}/>
                </Tab>
                <Tab eventKey="Prihodi" title="Prihodi">
                    <IncomeReport type={type}/>
                </Tab>
                <Tab eventKey="Posecenost" title="Posećenost">
                    <AttendanceReport type={type}/>
                </Tab>              
            </Tabs>
        </Modal.Body>    
      </Modal>
    );
}

export default ReportModal;