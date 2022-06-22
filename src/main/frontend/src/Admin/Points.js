import React, {useEffect, useState} from "react";
import {Sidebar} from "./Sidebar/Sidebar";
import {Input, Slider} from "@material-ui/core";
import {Button, FloatingLabel, Form} from "react-bootstrap";
import axios from "axios";
import {backLink} from "../Consts";


export function Points() {

    const [clientPoints, setClientPoints] = useState(5);
    const [vendorPoints, setVendorPoints] = useState(6);
    const [siteFee, setSiteFee] = useState(15);


    const fetchVendorPoints = () => {
        axios.get(backLink + "/pointlist/vendor").then(res => {
            setVendorPoints(res.data.numOfPoints);
        })
    }

    const fetchClientPoints = () => {
        axios.get(backLink + "/pointlist/client").then(res => {
            setClientPoints(res.data.numOfPoints);
        })
    }

    const fetchSiteFee = () => {
        axios.get(backLink + "/siteFee").then(res => {
            setSiteFee(res.data.percentage);
        })
    }

    useEffect(() => {
        fetchClientPoints();
        fetchVendorPoints();
        fetchSiteFee();
    }, [])


    const saveChanges = () => {
        axios.post(backLink + "/siteFee/add/" + siteFee).then(res => {
        });

        axios.post(backLink + "/pointlist/add/client/" + clientPoints).then(res =>{
        });

        axios.post(backLink + "/pointlist/add/vendor/" + vendorPoints).then(res =>{
        });

        window.location.reload();


    }


    return (<div className="d-flex" style={{height: "100vh"}}>
        <div className="w-25" style={{backgroundColor: "#f7f8f9"}}>
            <Sidebar/>
        </div>
        <div className="w-75 d-flex flex-column">
            <div className="display-6 m-5" style={{fontSize: "2rem"}}>Podešavanje dobijanja bodova</div>
            <div className="d-flex w-50 mt-5 ms-5 ">
                <div className="m-1">Broj bodova koje klijent osvaja pri rezervaciji</div>
                <div className="m-1 ms-auto">{clientPoints}</div>

            </div>
            <Slider
                className="w-50 m-1 ms-5 mt-0"
                value={clientPoints}
                onChange={(e, data) => setClientPoints(data)}
                valueLabelDisplay="auto"
                step={1}
                min={0}
                max={10}
                style={{color: "#0d6efc"}}
            />


            <div className="d-flex w-50 mt-5 ms-5 ">
                <div className="m-1">Broj bodova koje pružalac usluga osvaja pri rezervaciji</div>
                <div className="m-1 ms-auto">{vendorPoints}</div>

            </div>

            <Slider
                className="w-50 m-1 ms-5 mt-0"
                value={vendorPoints}
                onChange={(e, data) => setVendorPoints(data)}
                valueLabelDisplay="auto"
                step={1}
                min={0}
                max={10}
                style={{color: "#0d6efc"}}
            />

            <div className="d-flex w-50 mt-5 ms-5 ">
                <div className="m-1">Procenat koji sajt dobija od svake rezervacije</div>
                <div className="m-1 ms-auto">{siteFee + " %"}</div>

            </div>

            <Slider
                className="w-50 m-1 ms-5 mt-0"
                value={siteFee}
                onChange={(e, data) => setSiteFee(data)}
                valueLabelDisplay="auto"
                step={1}
                min={0}
                max={100}
                style={{color: "#0d6efc"}}
            />

            <div className=" m-5 d-flex w-50">
                <Button variant="outline-primary" onClick={saveChanges} className="w-50 me-1">Sačuvaj izmene</Button>
                <Button variant="outline-primary" className="w-50 ms-0"
                        onClick={() => window.location.reload()}
                >Otkaži izmene</Button>

            </div>

        </div>


    </div>)
}