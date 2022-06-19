import React, {useRef, useState} from "react";
import {Button, Col, Form, InputGroup, Modal} from "react-bootstrap";
import axios from "axios";
import {backLink, frontLink, missingDataErrors} from "../Consts";
import AdventureFormImages from "./AdventureFormImages";
import {TagInfo} from "../Info";
import {MessagePopupModal} from "../MessagePopupModal";


function getDto(formValues, formReference, imagesRef) {
    formValues.additionalServicesText = [];
    for (let index in formValues.additionalServices) {
        formValues.additionalServicesText.push(formValues.additionalServices.at(index).text);
    }
    formValues.fishingEquipmentText = [];
    for (let index in formValues.fishingEquipment) {
        formValues.fishingEquipmentText.push(formValues.fishingEquipment.at(index).text);
    }

    let dto = new FormData(formReference.current);
    dto.append("id", formValues.ownerId);
    dto.append("numberOfClients", formValues.numberOfClients);
    dto.append("ownerId", formValues.ownerId);
    dto.append("imagePaths", formValues.imagePaths)
    dto.append("cancellationFee", formValues.cancellationFee);
    dto.append("price", formValues.price);
    dto.append("title", formValues.title);
    dto.append("street", formValues.street);
    dto.append("place", formValues.place);
    dto.append("number", formValues.number);
    dto.append("country", formValues.country);
    dto.append("description", formValues.description);
    dto.append("rulesAndRegulations", formValues.rulesAndRegulations);
    dto.append("additionalServicesText", formValues.additionalServicesText);
    dto.append("fishingEquipmentText", formValues.fishingEquipmentText);
    dto.append("imagePaths", formValues.imagePaths)

    let files = imagesRef.current.files;
    let images = []
    for (let i = 0; i < files.length; i++) {
        images.push(files[i])
    }
    dto.append("fileImage", images);
    return dto;
}

export function AdventureModal({adventure, show, setShow, ownerId}) {
    let initialState = {}

    initialState = getInitialAdventureState(adventure, ownerId);

    const imagesRef = useRef();
    const formReference = useRef();

    const [formValues, setFormValues] = useState(initialState);
    const [formErrors, setFormErrors] = useState({});
    const [showAlert, setShowAlert] = useState(false);

    const setField = (fieldName, value) => {
        setFormValues({
            ...formValues,
            [fieldName]: value
        })
        if (!!formErrors[fieldName]) {
            setFormErrors({
                ...formErrors,
                [fieldName]: null
            })
        }
    }

    function handleDelete() {
        axios.post(backLink + "/adventure/delete/" + adventure.id)
            .then(response => {
                console.log(response);
                window.location.href = frontLink + "fishingInstructor/" + adventure.owner.id;
                }
            ).catch(error => {
            setShowAlert(true);

        })
    }

    function addAdventure() {

        let dto = getDto(formValues, formReference, imagesRef);

        axios
            .post(backLink + "/adventure/add", dto)
            .then(response => {
                window.location.reload();
            })
            .catch(error => {
                console.log(error)
            })
        }
        
        function editAdventure() {
            
            let dto = getDto(formValues, formReference, imagesRef);
            let url = backLink + "/adventure/updateAdventure/" + adventure.id;
            
            axios
            .post(url, dto)
            .then(response => {
                window.location.reload();            
            })
            .catch(error => {
                alert(error);
            })
    }

    const handleSubmit = e => {
        e.preventDefault()
        let errors = validateForm()
        if (Object.keys(errors).length > 0) {
            setFormErrors(errors);
        } else {
            if (adventure) {
                editAdventure();
            } else {
                addAdventure();
            }
        }
    }

    const validateForm = () => {
        let errors = {}
        if (formValues.title === "") {
            errors.title = missingDataErrors.title;
        }
        if (formValues.description === "") {
            errors.description = missingDataErrors.description;
        }
        if (formValues.street === "") {
            errors.street = missingDataErrors.address.street;
        }
        if (formValues.number === "") {
            errors.number = missingDataErrors.address.number;
        }
        if (formValues.place === "") {
            errors.place = missingDataErrors.address.place;
        }
        if (formValues.country === "") {
            errors.country = missingDataErrors.address.country;
        }
        if (formValues.numberOfClients === "") {
            errors.numberOfClients = missingDataErrors.numberOfClients;
        }
        if (formValues.price === "") {
            errors.price = missingDataErrors.price;
        }
        if (formValues.cancellationFee === "") {
            errors.cancellationFee = missingDataErrors.cancellationFee;
        }
        if (formValues.rulesAndRegulations === "") {
            errors.rulesAndRegulations = missingDataErrors.rulesAndRegulations;
        }
        return errors;
    }


    return (
        <>
            <MessagePopupModal
                show={showAlert}
                setShow={setShowAlert}
                message="Resurs koji ste pokušali da obrišete sadrži rezervacije koje se još nisu ostvarile."
                heading="Zabranjeno brisanje"
            />

            <Modal show={show} onHide={() => setShow(false)} size="lg">
                <Form ref={formReference} encType="multipart/form-data">
                    <Modal.Header closeButton>
                        <Modal.Title>{adventure ? 'Izmena avanture' : 'Dodavanje avanture'}</Modal.Title>
                    </Modal.Header>

                    <Modal.Body>

                        <AdventureForm
                            formValues={formValues}
                            setFormValues={setFormValues}
                            formErrors={formErrors}
                            setField={setField}
                            imagesRef={imagesRef}
                        />
                    </Modal.Body>
                    <Modal.Footer>
                        {adventure &&
                            <Button className="me-auto" variant="secondary" onClick={handleDelete}>
                                Obriši
                            </Button>
                        }

                        <Button variant="secondary" onClick={() => setShow(false)}>
                            Otkaži
                        </Button>
                        <Button variant="primary" onClick={handleSubmit}>
                            {adventure ? 'Izmeni' : 'Dodaj'}
                        </Button>
                    </Modal.Footer>
                </Form>
            </Modal>
        </>
    );
}

function AdventureForm({
                           formValues,
                           formErrors,
                           setField,
                           imagesRef,
                           setFormValues
                       }) {

    const [additionalServicesText, setAdditionalServicesText] = useState('');
    const [fishingEquipmentText, setFishingEquipmentText] = useState('');

    const setImages = (value) => {
        setFormValues({
                ...formValues,
                ["images"]: value
            }
        )
    }

    function addImages() {
        console.log("aaaa")
        let imageList = formValues.images;

        if (!imageList) {
            imageList = []
        }
        console.log(imageList);

        for (let file of imagesRef.current.files) {
            imageList.push(URL.createObjectURL(file))
        }
        document.getElementById("noImages").style.display = "none"
        setField("images", imageList)
    }

    function addAdditionalServicesTag() {
        let id = 0;
        if (formValues.additionalServices.length > 0) {
            id = formValues.additionalServices.at(-1).id + 1;
        }
        setFormValues({
            ...formValues,
            additionalServices: [...formValues.additionalServices, {id: id, text: additionalServicesText}]
        })

        setAdditionalServicesText('')
    }

    function addFishingEquipmentTag() {
        let id = 0;
        if (formValues.fishingEquipment.length > 0) {
            id = formValues.fishingEquipment.at(-1).id + 1;
        }
        setFormValues({
            ...formValues,
            fishingEquipment: [...formValues.fishingEquipment, {id: id, text: fishingEquipmentText}]
        })

        setFishingEquipmentText('')
    }

    return (
        <div>
            <Form.Group className="mb-3 m-2">
                <Form.Label>Naslov</Form.Label>
                <Form.Control type="text"
                              value={formValues.title}
                              onChange={(e) => setField("title", e.target.value)}
                              isInvalid={!!formErrors.title}
                />
                <Form.Control.Feedback type="invalid">
                    {formErrors.title}
                </Form.Control.Feedback>

            </Form.Group>
            <Form.Group className="mb-3 m-2">
                <Form.Label>Opis</Form.Label>
                <Form.Control as="textarea" rows={3}
                              value={formValues.description}
                              onChange={(e) => setField("description", e.target.value)}
                              isInvalid={!!formErrors.description}/>
                <Form.Control.Feedback type="invalid">
                    {formErrors.description}
                </Form.Control.Feedback>
            </Form.Group>

            <div className="d-flex" id="address">
                <Form.Group className="mb-3 m-2">
                    <Form.Label>Ulica</Form.Label>
                    <Form.Control type="text"
                                  value={formValues.street}
                                  onChange={(e) => setField("street", e.target.value)}
                                  isInvalid={!!formErrors.street}/>
                    <Form.Control.Feedback type="invalid">
                        {formErrors.street}
                    </Form.Control.Feedback>
                </Form.Group>
                <Form.Group className="mb-3 m-2 ">
                    <Form.Label>Broj</Form.Label>
                    <Form.Control type="text"
                                  value={formValues.number}
                                  onChange={(e) => setField("number", e.target.value)}
                                  isInvalid={!!formErrors.number}/>
                    <Form.Control.Feedback type="invalid">
                        {formErrors.number}
                    </Form.Control.Feedback>
                </Form.Group>
                <Form.Group className="mb-3 m-2">
                    <Form.Label>Mesto</Form.Label>
                    <Form.Control type="text"
                                  value={formValues.place}
                                  onChange={(e) => setField("place", e.target.value)}
                                  isInvalid={!!formErrors.place}/>
                    <Form.Control.Feedback type="invalid">
                        {formErrors.place}
                    </Form.Control.Feedback>
                </Form.Group>
                <Form.Group className="mb-3 m-2">
                    <Form.Label>Drzava</Form.Label>
                    <Form.Control type="text"
                                  value={formValues.country}
                                  onChange={(e) => setField("country", e.target.value)}
                                  isInvalid={!!formErrors.country}/>
                    <Form.Control.Feedback type="invalid">
                        {formErrors.country}
                    </Form.Control.Feedback>
                </Form.Group>
            </div>

            <Form.Group className="m-2">
                <Form.Label>Fotografije avanture</Form.Label>
                <AdventureFormImages images={formValues.images} setImages={setImages}/>
                <p id="noImages" style={{
                    color: "#dc3545",
                    fontSize: "0.875em",
                    marginLeft: "34%",
                    display: "none"
                }}>Molimo Vas postavite bar jednu fotografiju.</p>
                <InputGroup className="mb-3">                   
                    <Form.Control ref={imagesRef} type="file" multiple name="fileImage"/>
                    <Button variant="primary" id="button-addon2" onClick={e => addImages()}> Dodaj </Button>
                </InputGroup>
            </Form.Group>

            <div className="d-flex">

                <Form.Group className="mb-3 m-2 w-50">
                    <Form.Label>Broj klijenata</Form.Label>
                    <Form.Control type="number"
                                  value={formValues.numberOfClients}
                                  onChange={(e) => setField("numberOfClients", e.target.value)}
                                  isInvalid={!!formErrors.numberOfClients}/>
                    <Form.Control.Feedback type="invalid">
                        {formErrors.numberOfClients}
                    </Form.Control.Feedback>
                </Form.Group>

                <Form.Group className="mb-3 m-2 w-25">
                    <Form.Label>Cena</Form.Label>
                    <Form.Control type="number"
                                  value={formValues.price}
                                  onChange={(e) => setField("price", e.target.value)}
                                  isInvalid={!!formErrors.price}/>

                    <Form.Control.Feedback type="invalid">
                        {formErrors.price}
                    </Form.Control.Feedback>
                </Form.Group>

                <Form.Group className="mb-3 m-2 w-25">
                    <Form.Label>Naknada za otkazivanje</Form.Label>

                    <Form.Control type="number"
                                  value={formValues.cancellationFee}
                                  onChange={(e) => setField("cancellationFee", e.target.value)}
                                  isInvalid={!!formErrors.cancellationFee}/>

                    <Form.Control.Feedback type="invalid">
                        {formErrors.cancellationFee}
                    </Form.Control.Feedback>
                </Form.Group>

            </div>

            <div>

                <Form.Group as={Col} className="mb-3 m-2">
                    <Form.Label>Dodatne usluge</Form.Label>
                    <div className='d-flex justify-content-start align-items-center'>
                        <TagInfo tagList={formValues.additionalServices} edit={true}
                                 setState={setFormValues} entity="additionalServices"/>
                        <InputGroup className="p-0 mt-2 ms-auto"
                                    style={{maxWidth: "17vw", maxHeight: "4vh"}}>
                            <Form.Control aria-describedby="basic-addon2" placeholder='Dodaj tag'
                                          value={additionalServicesText}
                                          onChange={e => setAdditionalServicesText(e.target.value)}/>
                            <Button className="p-0 pe-2 ps-2" variant="primary" id="button-addon2"
                                    onClick={addAdditionalServicesTag}> + </Button>
                        </InputGroup>
                    </div>
                </Form.Group>

                <Form.Group as={Col} className="mb-3 m-2">
                    <Form.Label>Oprema za pecanje</Form.Label>
                    <div className='d-flex justify-content-start align-items-center'>
                        <TagInfo className="m-2" tagList={formValues.fishingEquipment} edit={true}
                                 setState={setFormValues} entity="fishingEquipment"/>
                        <InputGroup className="ps-2 mt-2 ms-auto"
                                    style={{maxWidth: "17vw", maxHeight: "4vh"}}>
                            <Form.Control aria-describedby="basic-addon2" placeholder='Dodaj tag'
                                          value={fishingEquipmentText}
                                          onChange={e => setFishingEquipmentText(e.target.value)}/>
                            <Button className="p-0 pe-2 ps-2" variant="primary" id="button-addon2"
                                    onClick={addFishingEquipmentTag}> + </Button>
                        </InputGroup>
                    </div>
                </Form.Group>

            </div>

            <Form.Group className="mb-3 m-2">
                <Form.Label>Pravila ponasanja</Form.Label>
                <Form.Control as="textarea" rows={3}
                              value={formValues.rulesAndRegulations}
                              onChange={(e) => setField("rulesAndRegulations", e.target.value)}
                              isInvalid={!!formErrors.rulesAndRegulations}/>
                <Form.Control.Feedback type="invalid">
                    {formErrors.rulesAndRegulations}
                </Form.Control.Feedback>
            </Form.Group>
        </div>
    )
}

function getInitialAdventureState(adventure, ownerId) {
    if (adventure) {
        let images = []
        for (let index in adventure.images) {
            images.push(backLink + adventure.images.at(index).path)
        }
  
        return {
            title: adventure.title,
            description: adventure.description,
            street: adventure.address.street,
            number: adventure.address.number,
            place: adventure.address.place,
            country: adventure.address.country,
            numberOfClients: adventure.numberOfClients,
            price: adventure.pricelist.price,
            cancellationFee: adventure.cancellationFee,
            additionalServices: [],
            fishingEquipment: adventure.fishingEquipment,
            rulesAndRegulations: adventure.rulesAndRegulations,
            ownerId: adventure.owner.id,
            imagePaths: [],
            images: images
        };


    } else {
        return {
            title: "",
            description: "",
            street: "",
            number: "",
            place: "",
            country: "",
            numberOfClients: "",
            price: "",
            cancellationFee: "",
            additionalServices: [],
            fishingEquipment: [],
            rulesAndRegulations: "",
            ownerId: ownerId,
            imagePaths: []
        };
    }
}