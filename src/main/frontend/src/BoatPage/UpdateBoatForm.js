import React, {useRef, useState} from 'react';
import { Form, Row, Col, InputGroup, Button } from 'react-bootstrap';
import UpdateBoatImages from './UpdateBoatImages';
import { TagInfo } from '../Info';

function UpdateBoatForm({state, setState, validated, imagesRef, reference}) {
    const [tagText, setTagText] = useState('');
    const [tagAdditionalServicesText, setTagAdditionalServicesText] = useState('');
    const [tagFishingEquipText, setTagFishingEquipText] = useState('');
    function addButton(entity) {
        if (tagText !== '' && entity === "navigationEquipment"){
            setState( prevState => {
                return {...prevState, navigationEquipment:[...prevState.navigationEquipment, {id:prevState.navigationEquipment.at(-1).id+1, text:tagText}]}
            })
            setTagText('')
        }
        if (tagAdditionalServicesText !== '' && entity === "additionalServices"){
            setState( prevState => {
                return {...prevState, additionalServices:[...prevState.additionalServices, {id:prevState.additionalServices.at(-1).id+1, text:tagAdditionalServicesText}]}
            })
            setTagAdditionalServicesText('')
        }
        if (tagFishingEquipText !== '' && entity === "fishingEquipment"){
            setState( prevState => {
                return {...prevState, fishingEquipment:[...prevState.fishingEquipment, {id:prevState.fishingEquipment.at(-1).id+1, text:tagFishingEquipText}]}
            })
            setTagFishingEquipText('')
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
    const setType = (value) => {
        setState( prevState => {
           return {...prevState, type:value}
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
    const setEngineStrength = (value) => {
        setState( prevState => {
           return {...prevState, engineStrength:value}
        })
    }
    const setTopSpeed = (value) => {
        setState( prevState => {
           return {...prevState, topSpeed:value}
        })
    }
    const setLength = (value) => {
        setState( prevState => {
           return {...prevState, length:value}
        })
    }
    const setEngineNumber = (value) => {
        setState( prevState => {
           return {...prevState, engineNumber:value}
        })
    }
    const setImages = (value) => {
        setState( prevState => {
            return {...prevState, imagePaths:value}
        })
    }
    function addImages() {
        var files = imagesRef.current.files
        const imageList = state.imagePaths;
        for (var file of files) {
            imageList.push(URL.createObjectURL(file))
        }
        document.getElementById("noImages").style.display = "none"
        setImages(imageList)
    }
    return (
        <Form noValidate validated={validated} ref={reference} encType="multipart/form-data">
            <Row className="mb-3">
                <Form.Group as={Col} controlId="formGridTitle">
                    <Form.Label>Naziv</Form.Label>
                    <Form.Control required type="title" defaultValue={state.name} onChange={e => setName(e.target.value)} />
                    <Form.Control.Feedback type="invalid">Molimo Vas unesite naziv.</Form.Control.Feedback>
                </Form.Group>

                <Form.Group as={Col} controlId="formGridTitle">
                    <Form.Label>Tip broda</Form.Label>
                    <Form.Control required type="type" defaultValue={state.type} onChange={e => setType(e.target.value)} />
                    <Form.Control.Feedback type="invalid">Molimo Vas unesite tip.</Form.Control.Feedback>
                </Form.Group>

                <Form.Group as={Col} controlId="formGridTitle">
                    <Form.Label>Dužina broda</Form.Label>
                    <InputGroup>
                    <Form.Control required type="title" defaultValue={state.length} onChange={e => setLength(e.target.value)} />
                    <InputGroup.Text>m</InputGroup.Text>
                    <Form.Control.Feedback type="invalid">Molimo Vas unesite dužinu broda.</Form.Control.Feedback>
                    </InputGroup>
                </Form.Group>
            </Row>

            <Form.Group className="mb-3" controlId="exampleForm.ControlTextarea1">
                <Form.Label>Opis</Form.Label>
                <Form.Control required as="textarea" rows={3} defaultValue={state.description} onChange={e => setDescription(e.target.value)}/>
                <Form.Control.Feedback type="invalid">Molimo Vas unesite opis.</Form.Control.Feedback>
            </Form.Group>

            <Row className="mb-3">
            <Form.Group as={Col} controlId="formGridPrice">
                    <Form.Label>Cena</Form.Label>
                    <InputGroup>
                    <Form.Control required type="number" name="price" placeholder="Cena jedne vožnje" value={state.price} onChange={e => setPrice(e.target.value)} />
                    <InputGroup.Text>€</InputGroup.Text>
                    <Form.Control.Feedback type="invalid">Molimo Vas unesite cenu.</Form.Control.Feedback>               
                    </InputGroup>
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

            <Form.Group className="mb-3" controlId="formGridRules">
                <Form.Label>Pravila ponašanja</Form.Label>
                <Form.Control required defaultValue={state.rulesAndRegulations} onChange={e => setRulesAndRegulations(e.target.value)}/>
                <Form.Control.Feedback type="invalid">Molimo Vas unesite pravila ponašanja.</Form.Control.Feedback>
            </Form.Group>

            <Row className="mb-3">
                <Form.Group as={Col} controlId="formGridAddress">
                <Form.Label>Broj motora</Form.Label>
                <Form.Control required placeholder="Broj motora" name="engineNumber" value={state.engineNumber} onChange={e => setEngineNumber(e.target.value)}/>
                <Form.Control.Feedback type="invalid">Molimo Vas unesite broj motora.</Form.Control.Feedback>
                </Form.Group>

                <Form.Group as={Col} controlId="formGridCity">
                    <Form.Label>Snaga motora</Form.Label>
                    <InputGroup>
                    <Form.Control required type="number" min={1} placeholder="Snaga motora" name="engineStrength" value={state.engineStrength} onChange={e => setEngineStrength(e.target.value)}/>
                    <InputGroup.Text>W</InputGroup.Text>
                    <Form.Control.Feedback type="invalid">Molimo Vas unesite snagu motora koja je veća od nule.</Form.Control.Feedback>
                    </InputGroup>
                </Form.Group>
            </Row>

            <Row className="mb-3">
                <Form.Group as={Col} controlId="formGridAddress">
                <Form.Label>Maksimalna brzina broda</Form.Label>
                    <InputGroup>
                    <Form.Control required type="number" min={1} placeholder="Brzina broda" name="topSpeed" value={state.topSpeed} onChange={e => setTopSpeed(e.target.value)}/>
                    <InputGroup.Text>km/h</InputGroup.Text>
                    <Form.Control.Feedback type="invalid">Molimo Vas unesite maksimalnu brzinu motora koja je veća od nule.</Form.Control.Feedback>
                    </InputGroup>
                </Form.Group>

                <Form.Group as={Col} controlId="formGridCapacity">
                    <Form.Label>Kapacitet</Form.Label>
                    <Form.Control required type="number" min={1} name='capacity' placeholder="Kapacitet broda" value={state.capacity} onChange={e => setCapacity(e.target.value)}/>
                    <Form.Control.Feedback type="invalid">Molimo Vas unesite kapacitet koji je veći od nule.</Form.Control.Feedback>
                </Form.Group>
            </Row>

            <Row className="mb-3">
                <Form.Group as={Col} controlId="formGridAddress">
                    <Form.Label>Ulica i broj</Form.Label>
                    <InputGroup>
                    <Form.Control required className="w-50" defaultValue={state.street} onChange={e => setStreet(e.target.value)}/>
                    <Form.Control required className="w-50" defaultValue={state.number} onChange={e => setNumber(e.target.value)}/>
                    <Form.Control.Feedback type="invalid">Molimo Vas unesite ulicu i broj.</Form.Control.Feedback>
                    </InputGroup>
                </Form.Group>

                <Form.Group as={Col} controlId="formGridCity">
                    <Form.Label>Grad</Form.Label>
                    <Form.Control required defaultValue={state.city} onChange={e => setCity(e.target.value)}/>
                    <Form.Control.Feedback type="invalid">Molimo Vas unesite grad.</Form.Control.Feedback>
                </Form.Group>

                <Form.Group as={Col} controlId="formGridCountry">
                    <Form.Label>Država</Form.Label>
                    <Form.Control required defaultValue={state.country} onChange={e => setCountry(e.target.value)}/>
                    <Form.Control.Feedback type="invalid">Molimo Vas unesite državu.</Form.Control.Feedback>
                </Form.Group>
            </Row>

            <Row className="mb-3">
                <Form.Group as={Col} controlId="formGridServices">
                <Form.Label>Navigaciona oprema</Form.Label>
                        <div className='d-flex justify-content-start'>
                            <TagInfo tagList={state.navigationEquipment} edit={true} setState={setState} entity="navigationEquipment"/>
                            <InputGroup className="p-0 mt-2" style={{maxWidth:"17vh", minWidth:"17vh"}}>
                                <Form.Control style={{height:"4vh"}} aria-describedby="basic-addon2" placeholder='Dodaj tag' value={tagText} onChange={e => setTagText(e.target.value)}/>        
                                <Button className="p-0 pe-2 ps-2" style={{height:"4vh"}} variant="primary" id="button-addon2" onClick={e => addButton("navigationEquipment")}> + </Button>
                            </InputGroup>
                        </div>
                </Form.Group>

                <Form.Group as={Col} controlId="formGridServices">
                <Form.Label>Oprema za pecanje</Form.Label>
                        <div className='d-flex justify-content-start'>
                            <TagInfo tagList={state.fishingEquipment} edit={true} setState={setState} entity="fishingEquipment"/>
                            <InputGroup className="p-0 mt-2" style={{maxWidth:"17vh", minWidth:"17vh"}}>
                                <Form.Control style={{height:"4vh"}} aria-describedby="basic-addon2" placeholder='Dodaj tag' value={tagFishingEquipText} onChange={e => setTagFishingEquipText(e.target.value)}/>        
                                <Button className="p-0 pe-2 ps-2" style={{height:"4vh"}} variant="primary" id="button-addon2" onClick={e => addButton("fishingEquipment")}> + </Button>
                            </InputGroup>
                        </div>
                </Form.Group>

                <Form.Group as={Col} controlId="formGridServices">
                <Form.Label>Dodatne usluge</Form.Label>
                        <div className='d-flex justify-content-start'>
                            <TagInfo tagList={state.additionalServices} edit={true} setState={setState} entity="additionalServices"/>
                            <InputGroup className="p-0 mt-2" style={{maxWidth:"17vh", minWidth:"17vh"}}>
                                <Form.Control style={{height:"4vh"}} aria-describedby="basic-addon2" placeholder='Dodaj tag' value={tagAdditionalServicesText} onChange={e => setTagAdditionalServicesText(e.target.value)}/>        
                                <Button className="p-0 pe-2 ps-2" style={{height:"4vh"}} variant="primary" id="button-addon2" onClick={e => addButton("additionalServices")}> + </Button>
                            </InputGroup>
                        </div>
                </Form.Group>      
            </Row>

            <Form.Group controlId="formFileMultiple" className="mb-3">
                <Form.Label>Fotografije broda</Form.Label>
                <UpdateBoatImages images={state.imagePaths} setImages={setImages}/>
                <p id="noImages" style={{color:"#dc3545", fontSize: "0.875em", marginLeft:"34%", display:"none"}}>Molimo Vas postavite bar jednu fotografiju.</p>
                <InputGroup className="mb-3">
                    <Form.Control ref={imagesRef} type="file" multiple className='w-75' name="fileImage" />
                    <Button variant="primary" id="button-addon2" onClick = {e => addImages()}> Dodaj </Button>
                </InputGroup>
            </Form.Group>
            </Form>
    );
}

export default UpdateBoatForm;