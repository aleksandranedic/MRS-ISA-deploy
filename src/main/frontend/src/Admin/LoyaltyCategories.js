import React, {useEffect, useState} from 'react';
import {Sidebar} from "./Sidebar/Sidebar";
import {Badge, Button, ButtonGroup, Card, Col, FloatingLabel, Form, Row, ToggleButton} from "react-bootstrap";
import {MdStars} from "react-icons/md";
import {TiDeleteOutline} from "react-icons/ti";
import "./LoyaltyCategories.css"
import {backLink, missingDataErrors} from "../Consts";
import axios from "axios";

const loyaltyColorPalette = {
    'pink-blue': "linear-gradient( 64.5deg,  rgba(245,116,185,1) 14.7%, rgba(89,97,223,1) 88.7% )",
    'green-blue': "linear-gradient( 109.6deg,  rgba(61,245,167,1) 11.2%, rgba(9,111,224,1) 91.1% )",
    'pink-purple': "radial-gradient( circle farthest-corner at 10.2% 55.8%,  rgba(252,37,103,1) 0%, rgba(250,38,151,1) 46.2%, rgba(186,8,181,1) 90.1% )",
    'green': "radial-gradient( circle farthest-corner at 10% 20%,  rgba(14,174,87,1) 0%, rgba(12,116,117,1) 90% )",
    'blue-purple': "linear-gradient( 83.2deg,  rgba(150,93,233,1) 10.8%, rgba(99,88,238,1) 94.3% )",
    'purple-orange': "linear-gradient( 358.4deg,  rgba(249,151,119,1) -2.1%, rgba(98,58,162,1) 90% )",
    'blue': "linear-gradient( 109.6deg,  rgba(62,161,219,1) 11.2%, rgba(93,52,236,1) 100.2% )",

}

export function LoyaltyCategories() {

    const [formValues, setFormValues] = useState({
        minimumPoints: "",
        maximumPoints: "",
        discount: "",
        name: ""
    });

    const [formErrors, setFormErrors] = useState({});

    const [type, setType] = useState("CLIENT");
    const [colorId, setColorId] = useState(null);

    const [userCategories, setUserCategories] = useState([]);

    const [vendorCategories, setVendorCategories] = useState([]);

    const validateForm = () => {
        let errors = {}
        if (formValues.name === "") {
            errors.name = missingDataErrors.title;
        }
        if (formValues.discount === "") {
            errors.discount = missingDataErrors.discount;
        }
        if (formValues.minimumPoints === "") {
            errors.minimumPoints = missingDataErrors.points;
        }
        if (formValues.maximumPoints === "") {
            errors.name = missingDataErrors.points;
        }
        if (colorId === null) {
            errors.color = missingDataErrors.color;
        }
        return errors;
    }

    const handleSubmit = e => {
        e.preventDefault()
        let errors = validateForm()
        if (Object.keys(errors).length > 0) {
            setFormErrors(errors);
        } else {
            addCategory();
        }
    }

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

    const addCategory = () => {
        let dto = {
            name: formValues.name,
            minimumPoints: formValues.minimumPoints,
            maximumPoints: formValues.maximumPoints,
            discount: formValues.discount,
            color: loyaltyColorPalette[colorId],
            type: type
        }

        console.log(dto);
        axios.post(backLink + "/category/add", dto).then(res => {
            window.location.reload();
        })
    }


    const fetchUserCategories = () => {
        axios.get(backLink + "/category/client").then(res => {
            setUserCategories(res.data);
        })
    }

    const fetchVendorCategories = () => {
        axios.get(backLink + "/category/vendor").then(res => {
            setVendorCategories(res.data);
        })
    }

    useEffect(() => {
        fetchUserCategories();
        fetchVendorCategories();
    }, [])

    function setSelectedColor(elementId) {

        let buttons = document.getElementsByClassName("color-button")
        for (let i = 0; i < buttons.length; i++) {
            if (buttons[i].classList.contains("selected-color")) {
                buttons[i].classList.remove("selected-color");
            }
        }

        setColorId(elementId);

        let button = document.getElementById(elementId);
        button.addClass("selected-color");
    }

    const deleteCategory = (id) => {

        axios.post(backLink + "/category/delete/" + id).then(res => {
            window.location.reload();
        })
    }

    return (<div className="d-flex" style={{height: "100vh"}}>
        <div className="w-25" style={{backgroundColor: "#f7f8f9"}}>
            <Sidebar/>
        </div>


        <div className="w-75" style={{overflow: " scroll"}}>

            <div className="m-4">
                <div className="display-6" style={{fontSize: "2rem"}}>Nova kategorija</div>

                <Form className="mt-3">
                    <Form.Group>
                        <FloatingLabel
                            controlId="floatingInput"
                            label="Naziv"
                            className="mb-2"
                        >
                            <Form.Control type="text" placeholder="Title" value={formValues.name}
                                          isInvalid={!!formErrors.name}
                                          onChange={(e) => setField("name", e.target.value)}/>
                            <Form.Control.Feedback type="invalid">
                                {formErrors.name}
                            </Form.Control.Feedback>
                        </FloatingLabel>

                    </Form.Group>

                    <div className="d-flex">
                        <Form.Group className="w-25 me-2">
                            <FloatingLabel
                                controlId="floatingInput"
                                label="Minimalan broj poena"
                                className="mb-2"
                            >
                                <Form.Control type="number" placeholder="Title" isInvalid={!!formErrors.minimumPoints}
                                              onChange={(e) => setField("minimumPoints", e.target.value)}/>
                                <Form.Control.Feedback type="invalid">
                                    {formErrors.minimumPoints}
                                </Form.Control.Feedback>
                            </FloatingLabel>
                        </Form.Group>
                        <Form.Group className="w-25">
                            <FloatingLabel
                                controlId="floatingInput"
                                label="Maksimalan broj poena"
                                className="mb-2"
                            >
                                <Form.Control type="number" placeholder="Title" isInvalid={!!formErrors.maximumPoints}
                                              onChange={(e) => setField("maximumPoints", e.target.value)}/>
                                <Form.Control.Feedback type="invalid">
                                    {formErrors.maximumPoints}
                                </Form.Control.Feedback>
                            </FloatingLabel>
                        </Form.Group>

                        <Form.Group className="w-50 ms-2">
                            <FloatingLabel
                                controlId="floatingInput"
                                label="Popust (%)"
                                className="mb-2"
                            >
                                <Form.Control type="number" placeholder="Title" isInvalid={!!formErrors.discount}
                                              onChange={(e) => setField("discount", e.target.value)}/>
                                <Form.Control.Feedback type="invalid">
                                    {formErrors.discount}
                                </Form.Control.Feedback>
                            </FloatingLabel>
                        </Form.Group>
                    </div>

                    <div className="d-flex">

                        <ButtonGroup className="m-2 ms-0">

                            <ToggleButton
                                type="radio"
                                variant="outline-secondary"
                                name="radio"
                                onClick={() => setType("CLIENT")}
                                checked={type === "CLIENT"}>
                                Klijent
                            </ToggleButton>

                            <ToggleButton
                                type="radio"
                                variant="outline-secondary"
                                name="radio"
                                onClick={() => setType("VENDOR")}
                                checked={type === "VENDOR"}>
                                Pružalac usluga
                            </ToggleButton>

                        </ButtonGroup>

                        <div className="d-flex flex-column">
                            <div className="d-flex">
                                <Button onClick={() => setSelectedColor("purple-orange")} id="purple-orange"
                                        className="color-button"
                                        style={{background: loyaltyColorPalette["purple-orange"]}}/>
                                <Button onClick={() => setSelectedColor("blue-purple")} id="blue-purple"
                                        className="color-button"
                                        style={{background: loyaltyColorPalette["blue-purple"]}}/>
                                <Button onClick={() => setSelectedColor("green-blue")} id="green-blue"
                                        className="color-button"
                                        style={{background: loyaltyColorPalette["green-blue"]}}/>
                                <Button onClick={() => setSelectedColor("green")} id="green" className="color-button"
                                        style={{background: loyaltyColorPalette["green"]}}/>
                                <Button onClick={() => setSelectedColor("blue")} id="blue" className="color-button"
                                        style={{background: loyaltyColorPalette["blue"]}}/>
                                <Button onClick={() => setSelectedColor("pink-blue")} id="pink-blue"
                                        className="color-button"
                                        style={{background: loyaltyColorPalette["pink-blue"]}}/>
                                <Button onClick={() => setSelectedColor("pink-purple")} id="pink-purple"
                                        className="color-button"
                                        style={{background: loyaltyColorPalette["pink-purple"]}}/>


                            </div>
                            {formErrors.color && <p style={{color: "red"}}>Morate odabrati jednu od boja.</p>}

                        </div>


                        <Button onClick={handleSubmit} className="ms-auto mt-2 mb-2"
                                variant="outline-secondary">Dodaj</Button>

                    </div>


                </Form>
            </div>

            <div className="display-6 m-4" style={{fontSize: "1.5rem"}}>Kategorije klijenata</div>
            <hr className="m-3"/>

            <Row xs={1} md={4} className="g-4 ms-2">
                {userCategories.map((category, index) => (<Col key={index}>
                    <Card key={index} className="m-2"
                          style={{background: category.color, width: "17vw", height: "21.5vw"}}>

                        <Card.Body className="w-100 m-2">
                            <Card.Title className="text-light">{category.name}</Card.Title>
                            <div className="d-flex">
                                <MdStars style={{color: "white", height: "1.5rem", width: "1.5rem"}}/>
                                <div
                                    className="text-light align-center p-0 m-0 ms-2">{category.minimumPoints} - {category.maximumPoints}</div>
                            </div>

                        </Card.Body>

                        <Card.Footer style={{backgroundColor: "rgba(0,0,0,0.1)"}} className="d-flex">
                            <Button size="sm" variant="outline-light"
                                    className="d-flex align-items-center justify-content-center"
                                    onClick={() => deleteCategory(category.id)}
                            >

                                <TiDeleteOutline style={{color: "white", height: "1.2rem", width: "1.2rem"}}/>
                            </Button>
                            {category.priceAlteration > 0 && <Badge bg="light"
                                                                    className="ms-auto d-flex align-items-center justify-content-end w-25">
                                <div className="text-secondary h-100 align-center p-0 m-0 ms-2"
                                     style={{
                                         fontSize: "1.25rem", fontWeight: "400"
                                     }}>{category.priceAlteration}%
                                </div>
                            </Badge>}
                        </Card.Footer>
                    </Card>
                </Col>))}
            </Row>

            <div className="display-6 m-4" style={{fontSize: "1.5rem"}}>Kategorije pružaoca usluga</div>
            <hr className="m-3"/>
            <Row xs={1} md={4} className="g-4 ms-2">
                {vendorCategories.map((category, index) => (<Col key={index}>
                    <Card key={index} className="m-2"
                          style={{background: category.color, width: "17vw", height: "21.5vw"}}>

                        <Card.Body className="w-100 m-2">
                            <Card.Title className="text-light">{category.name}</Card.Title>
                            <div className="d-flex">


                                <MdStars style={{color: "white", height: "1.5rem", width: "1.5rem"}}/>
                                <div
                                    className="text-light align-center p-0 m-0 ms-2">{category.minimumPoints} - {category.maximumPoints}</div>

                            </div>
                        </Card.Body>

                        <Card.Footer style={{backgroundColor: "rgba(0,0,0,0.1)"}} className="d-flex">
                            <Button size="sm" variant="outline-light"
                                    className="d-flex align-items-center justify-content-center">

                                <TiDeleteOutline style={{color: "white", height: "1.2rem", width: "1.2rem"}}/>
                            </Button>
                            {category.priceAlteration > 0 && <Badge bg="light"
                                                                    className="ms-auto d-flex align-items-center justify-content-end w-25">
                                <div className="text-secondary h-100 align-center p-0 m-0 ms-2"
                                     style={{
                                         fontSize: "1.25rem", fontWeight: "400"
                                     }}>{category.priceAlteration}%
                                </div>
                            </Badge>}
                        </Card.Footer>
                    </Card>
                </Col>))}
            </Row>
        </div>


    </div>)
}
