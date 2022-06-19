import React, {useState} from 'react';
import { Form, Row, Col, InputGroup, Button } from 'react-bootstrap';
import { TagInfo } from '../Info';

function HouseForm({state, setState, imagesRef, reference, validated}) {
    const [tagText, setTagText] = useState('');
    function addButton() {
        if (tagText !== ''){
            setState( prevState => {
                return {...prevState, additionalServices:[...prevState.additionalServices, {id:prevState.additionalServices.at(-1).id+1, text:tagText}]}
            })
            setTagText('')
        }
    }
    const setName = (value) => {
        setState( prevState => {
           return {...prevState, name:value}
        })
    }
    const setPrice = (value) => {
        setState( prevState => {
           return {...prevState, price:value}
        })
    }
    const setDescription = (value) => {
        setState( prevState => {
           return {...prevState, description:value}
        })
    }
    const setRooms = (value) => {
        setState( prevState => {
           return {...prevState, numberOfRooms:value}
        })
    }
    const setCapacity = (value) => {
        setState( prevState => {
           return {...prevState, capacity:value}
        })
    }
    const setRulesAndRegulations = (value) => {
        setState( prevState => {
           return {...prevState, rulesAndRegulations:value}
        })
    }
    const setStreet = (value) => {
        setState( prevState => {
           return {...prevState, street:value}
        })
    }
    const setNumber = (value) => {
        setState( prevState => {
           return {...prevState, number:value}
        })
    }
    const setCity = (value) => {
        setState( prevState => {
           return {...prevState, city:value}
        })
    }
    const setCountry = (value) => {
        setState( prevState => {
           return {...prevState, country:value}
        })
    }
    const setCancellationFee = (value) => {
        setState( prevState => {
           return {...prevState, cancellationFee:value}
        })
    }
    return (
        <Form noValidate validated={validated} ref={reference} encType="multipart/form-data">
            <Row className="mb-3">
                <Form.Group as={Col} controlId="validationCustom01">
                    <Form.Label>Naziv</Form.Label>
                    <Form.Control required name="name" placeholder="Naziv vikendice" value={state.name} onChange={e => setName(e.target.value)} />
                    <Form.Control.Feedback type="invalid">Molimo Vas unesite naziv.</Form.Control.Feedback>
                </Form.Group>

                <Form.Group as={Col} controlId="formGridPrice">
                    <Form.Label>Cena</Form.Label>
                    <InputGroup>
                    <Form.Control required type="number" name="price" placeholder="Cena po noćenju" value={state.price} onChange={e => setPrice(e.target.value)} />
                    <InputGroup.Text>€</InputGroup.Text>
                    <Form.Control.Feedback type="invalid">Molimo Vas unesite cenu.</Form.Control.Feedback>               
                    </InputGroup>
                </Form.Group>
            </Row>

            <Form.Group className="mb-3" controlId="exampleForm.ControlTextarea1">
                <Form.Label>Opis</Form.Label>
                <Form.Control required as="textarea" name="description" rows={3} value={state.description} onChange={e => setDescription(e.target.value)}/>
                <Form.Control.Feedback type="invalid">Molimo Vas unesite opis.</Form.Control.Feedback>
            </Form.Group>

            <Row className="mb-3">
                <Form.Group as={Col} controlId="formGridRooms">
                    <Form.Label>Broj soba</Form.Label>
                    <Form.Control required type="number" min={1} name="numberOfRooms" placeholder="Broj soba" value={state.numberOfRooms} onChange={e => setRooms(e.target.value)}/>
                    <Form.Control.Feedback type="invalid">Molimo Vas unesite broj soba koji je veći od nule.</Form.Control.Feedback>
                </Form.Group>

                <Form.Group as={Col} controlId="formGridCapacity">
                    <Form.Label>Kapacitet</Form.Label>
                    <Form.Control required type="number" min={1} name='capacity' placeholder="Kapacitet vikendice" value={state.capacity} onChange={e => setCapacity(e.target.value)}/>
                    <Form.Control.Feedback type="invalid">Molimo Vas unesite kapacitet koji je veći od nule.</Form.Control.Feedback>
                </Form.Group>
            </Row>

            <Form.Group className="mb-3" controlId="formGridRules">
                <Form.Label>Pravila ponašanja</Form.Label>
                <Form.Control required placeholder="Dozvoljeno i zabranjeno" name="rulesAndRegulations" value={state.rulesAndRegulations} onChange={e => setRulesAndRegulations(e.target.value)}/>
                <Form.Control.Feedback type="invalid">Molimo Vas unesite pravila ponašanja.</Form.Control.Feedback>
            </Form.Group>

            <Row className="mb-3">
                <Form.Group as={Col} controlId="formGridAddress">
                <Form.Label>Ulica i broj</Form.Label>
                    <InputGroup>
                    <Form.Control required placeholder="Ulica" name="street" className="w-50" value={state.street} onChange={e => setStreet(e.target.value)}/>
                    <Form.Control required placeholder="Broj" name="number" className="w-50" value={state.number} onChange={e => setNumber(e.target.value)}/>
                    <Form.Control.Feedback type="invalid">Molimo Vas unesite ulicu i broj.</Form.Control.Feedback>
                    </InputGroup>
                </Form.Group>

                <Form.Group as={Col} controlId="formGridCity">
                    <Form.Label>Grad</Form.Label>
                    <Form.Control required placeholder="Grad" name="city" value={state.city} onChange={e => setCity(e.target.value)}/>
                    <Form.Control.Feedback type="invalid">Molimo Vas unesite grad.</Form.Control.Feedback>
                </Form.Group>

                <Form.Group as={Col} controlId="formGridCountry">
                    <Form.Label>Država</Form.Label>
                    <Form.Control required placeholder="Država" name="country" value={state.country} onChange={e => setCountry(e.target.value)}/>
                    <Form.Control.Feedback type="invalid">Molimo Vas unesite državu.</Form.Control.Feedback>
                </Form.Group>
            </Row>

            <Row className="mb-3">
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

            <Form.Group as={Col} controlId="formGridFee">
                    <Form.Label>Naknada za otkazivanje</Form.Label>
                    <InputGroup>
                    <Form.Control required type="number" placeholder='Naknada u procentima' name="cancellationFee" value={state.cancellationFee} onChange={e => setCancellationFee(e.target.value)}/>
                    <InputGroup.Text>%</InputGroup.Text>
                    <Form.Control.Feedback type="invalid">Molimo Vas unesite naknadu za otkazivanje.</Form.Control.Feedback>
                    </InputGroup>
                </Form.Group>
            </Row>
            <Form.Group controlId="formFileMultiple" className="mb-3">
                <Form.Label>Fotografije vikendice</Form.Label>
                <Form.Control required type="file" multiple name="fileImage" ref={imagesRef} accept="image/png, image/jpeg"/>
                <Form.Control.Feedback type="invalid">Molimo Vas unesite bar jednu fotografiju.</Form.Control.Feedback>
            </Form.Group>
            </Form>
    );
}

export default HouseForm;