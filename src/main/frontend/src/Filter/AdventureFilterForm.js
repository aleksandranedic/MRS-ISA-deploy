import {Form} from "react-bootstrap";
import {useState} from "react";
import {Typeahead} from "react-bootstrap-typeahead";



export function AdventureFilterForm({filterData, setFilterData}) {
    const [selectedInstructor, setSelectedInstructor] = useState();
    const [numberOfClients, setNumberOfClients] = useState();

    function changeSelectedInstructor(text) {
        setSelectedInstructor(text);

        setFilterData({
            ...filterData,
            ["fishingInstructor"]: selectedInstructor
        })
        console.log(filterData);
    }
    function changeNumberOfClients(num) {
        setNumberOfClients(num);

        setFilterData({
            ...filterData,
            ["numberOfClients"]: numberOfClients
        })
        console.log(filterData);
    }

    return (<Form>

        <Form.Group>
            <Form.Label>Broj klijenata</Form.Label>
            <Form.Control type="number" value={numberOfClients}/>
        </Form.Group>

        <Form.Group>
            <Form.Label>Instruktor</Form.Label>
            <Typeahead
                id="basic-typeahead-single"
                labelKey="name"
                options={['Petar Jovanovic', 'Mile']}
                selected={selectedInstructor}
            />
        </Form.Group>

    </Form>)
}