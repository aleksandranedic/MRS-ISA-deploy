import {React, useState, useRef} from 'react';
import axios from "axios";  
import { useParams } from "react-router-dom";
import {Modal, Button, Form, Row, Col, InputGroup} from 'react-bootstrap'
import { TagInfo } from './Info';
import './material.css'
import {MessagePopupModal} from "./MessagePopupModal";

import { backLink, notifyError, notifySuccess } from './Consts';
import {ToastContainer} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css'

function AddQuickReservation({showModal, closeModal, entity, priceText, durationText, additionalServices}) {
    const statePlaceHolder = {startDate:'01.01.2022. 00:00', price:'', discount:'', numberOfPeople:'', duration:'', additionalServices:additionalServices};
    let [state, setState] = useState(statePlaceHolder)
    const [availableTags, setAvailableTags] = useState(state.additionalServices);
    const [tagText, setTagText] = useState('');
    const [validated, setValidated] = useState(false);
    const form = useRef();
    const {id} = useParams();
    const [showAlert, setShowAlert] = useState(false);

    const submit = e => {
        e.preventDefault() 
        if (form.current.checkValidity() === false) {
            e.stopPropagation();
            setValidated(true);
        }
        else {  
            var data = new FormData(form.current);   
            state.tagsText = []
            for (let i=0; i < state.additionalServices.length; i++){
                if (state.additionalServices[i].text !== ''){
                    state.tagsText.push(state.additionalServices[i].text)
                }
            } 
            data.append("tagsText", state.tagsText);
            data.append("startDate", state.startDate);
            axios
            .post(backLink+"/" + entity + "/addQuickReservation/" + id, data)
            .then(res => {
                if (res.data === true){
                    notifySuccess("Akcija uspešno dodata.")
                    setTimeout(function() {window.location.reload();}, 2000);  
                }
                else {
                    notifyError("Nespešno dodavanje. Molimo Vas pokušajte ponovo.")
                    close();
                }
            }).catch(error => {
                close();
                setShowAlert(true);
            })
        }
      
      }
      function close(){
        Reset();
        closeModal();
      }
      const Reset = () => {
        setValidated(false);
        setState(statePlaceHolder);
      }

    const setStartDate = (val) => {
        var sd = state.startDate;
        var date = val.split('-')
        var newStartDate = date[2] + " " + date[1] + " " + date[0] + " " + sd.split(" ")[3]
        setState( prevState => {
            return {...prevState, startDate:newStartDate}
        })
    }
    const setStartTime = (val) => {
        var sd = state.startDate;
        var date = sd.split(" ");
        var newStartDate = date[0] + " " + date[1] + " " + date[2] + " " + val;
        setState( prevState => {
            return {...prevState, startDate:newStartDate}})
    }
    const setDuration = (value) => {
        setState( prevState => {
           return {...prevState, duration:value}
        })
    }
    const setNumberOfPeople = (value) => {
        setState( prevState => {
           return {...prevState, numberOfPeople:value}
        })
    }
    
    const setPrice = (value) => {
        setState( prevState => {
           return {...prevState, price:value}
        })
    }

    function addButton() {
        if (tagText !== ''){
            var can=false;
            for (var item of availableTags){
                if (item.text === tagText){
                    can = true;
                    setState( prevState => {
                        return {...prevState, additionalServices:[...prevState.additionalServices, {id:prevState.additionalServices.at(-1).id+1, text:tagText}]}
                    })
                    setTagText('')
                }
            }
            if (can === false) {
                notifyError("Tag se može dodati samo iz postojećih dodatnih usluga entiteta.")
            }
        }
    }

    return (
        <Modal show={showModal} onHide={close} >
        <Form ref={form} noValidate validated={validated}>
            <Modal.Header>
                <Modal.Title>Dodavanje akcije</Modal.Title>
            </Modal.Header>
            <Modal.Body>

                <Row className="mb-3">

                    <Form.Group as={Col} >
                        <Form.Label>Datum početka važenja akcije</Form.Label>
                        <Form.Control required type="date" name="date" onChange={e => setStartDate(e.target.value)}/>
                        <Form.Control.Feedback type="invalid">Molimo Vas unesite početni datum za koje akcija važi.</Form.Control.Feedback>
                    </Form.Group>

                    {entity !== "house" &&
                    <Form.Group as={Col}>
                        <Form.Label>Vreme početka važenja akcije</Form.Label>
                        <Form.Control required type="time" name="time" onChange={e => setStartTime(e.target.value)}/>
                        <Form.Control.Feedback type="invalid">Molimo Vas unesite početno vreme za koje akcija važi.</Form.Control.Feedback>
                    </Form.Group>
                    }
                </Row>

                <Row className="mb-3">

                    <Form.Group as={Col} >
                        <Form.Label>Broj {durationText} za koje akcija važi</Form.Label>
                        <Form.Control required type="number" min={1} name="duration" onChange={e => setDuration(e.target.value)}/>
                        <Form.Control.Feedback type="invalid">Molimo Vas unesite broj {durationText} za koje akcija važi.</Form.Control.Feedback>
                    </Form.Group>

                    <Form.Group as={Col}>
                        <Form.Label>Maksimalan broj osoba</Form.Label>
                        <Form.Control required type="number" min={1} name="numberOfPeople" onChange={e => setNumberOfPeople(e.target.value)}/>
                        <Form.Control.Feedback type="invalid">Molimo Vas unesite broj ljudi za koje važi akcija..</Form.Control.Feedback>
                    </Form.Group>
                </Row>

             
                <Form.Group as={Col} >
                    <div className='d-flex'>
                    <Form.Label className="me-1">Cena</Form.Label>
                    <Form.Label>{priceText}</Form.Label>
                    </div>
                    <InputGroup>
                        <Form.Control required type="number" min={1} name="price" onChange={e => setPrice(e.target.value)}/>
                        <InputGroup.Text>€</InputGroup.Text>
                        <Form.Control.Feedback type="invalid">Molimo Vas unesite grad.</Form.Control.Feedback>
                    </InputGroup>
                </Form.Group>

                <Form.Group as={Col} controlId="formGridServices">
                <Form.Label>Dodatne usluge</Form.Label>
                    <div className='d-flex justify-content-start'>
                        <TagInfo tagList={state.additionalServices} edit={true} setState={setState} entity="additionalServices"/>
                        <InputGroup className="p-0 mt-2" style={{maxWidth:"17vh", minWidth:"17vh"}}>
                            <Form.Control style={{height:"4vh"}} aria-describedby="basic-addon2" placeholder='Dodaj tag' value={tagText} onChange={e => setTagText(e.target.value)}/>        
                            <Button className="p-0 pe-2 ps-2" style={{height:"4vh"}} variant="primary" id="button-addon2" onClick={addButton}> + </Button>
                        </InputGroup>
                    </div>
                </Form.Group>

            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={close}>Zatvori</Button>
                <Button variant="primary" onClick={submit}>Dodaj</Button>
            </Modal.Footer>
        </Form>

            <MessagePopupModal
                show={showAlert}
                setShow={setShowAlert}
                message="Termin za akciju koji ste pokušali da zauzmete nije dostupan. Pogledajte kalendar zauzetosti i postojaće akcije i pokušajte ponovo."
                heading="Zauzet termin"
            />
        <ToastContainer
                position="top-center"
                autoClose={5000}
                hideProgressBar={false}
                newestOnTop={false}
                closeOnClick
                rtl={false}
                pauseOnFocusLoss
                draggable
                pauseOnHover
                theme={"colored"}
            />
      </Modal>
    );
}

export default AddQuickReservation;