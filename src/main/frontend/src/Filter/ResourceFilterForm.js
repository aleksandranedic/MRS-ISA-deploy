import React, {useState} from 'react'
import {Slider} from "@material-ui/core"
import {Form} from "react-bootstrap";

export function ResourceFilterForm({minimumValue, maximumValue, setField}) {

    const [priceRangeValue, setPriceRangeValue] = useState([minimumValue, maximumValue]);

    const [cancellationFee, setCancellationFee] = useState(false)
    const updatePriceRangeValue = (e, data) => {
        setPriceRangeValue(data)
        setField('priceRange', data)
    }
    const updateCancellationFee = () => {
        setCancellationFee(!cancellationFee)
        setField('cancellationFee', cancellationFee)
    }

    return (<div className="m-3">

        <Form.Group>
            <Form.Label>Cena</Form.Label>
            <Slider
                value={priceRangeValue}
                onChange={updatePriceRangeValue}
                valueLabelDisplay="auto"
                min={minimumValue}
                max={maximumValue}
                style={{color: "#0d6efc"}}
            />
        </Form.Group>
        <Form.Check
            type='switch'
            id="adventure-switch"
            label="Bez naknade za otkazivanje"
            checked={cancellationFee}
            onChange={
                () => updateCancellationFee()
            }
        />


    </div>)
}