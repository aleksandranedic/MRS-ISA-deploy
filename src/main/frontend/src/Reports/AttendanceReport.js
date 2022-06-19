import React, { useEffect, useRef, useState } from 'react';
import { addDays } from 'date-fns';
import { DateRangePicker } from 'react-date-range';
import { Button, Form } from 'react-bootstrap';
import 'react-date-range/dist/styles.css'; 
import 'react-date-range/dist/theme/default.css'; 
import { backLink } from '../Consts';
import axios from 'axios';
import { useParams } from 'react-router-dom';

import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend,
    PointElement,
    LineElement,
} from 'chart.js';
import { Bar, Line } from 'react-chartjs-2';

ChartJS.register(
    LineElement,
    PointElement,
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend
);

export const options = {
responsive: true,
plugins: {
    legend: {
    position: 'top',
    },
    title: {
    display: true,
    
    },
},
};

function AttendanceReport({type}) {
    const {id} = useParams();
    const [data, setData] = useState({})
    const [chartData, setChartData] = useState(
       
      )
    const [state, setState] = useState([
        {
            startDate: new Date(),
            endDate: addDays(new Date(), 7),
            key: 'selection'
        }
    ]);

    const showReport = () => {
        document.getElementById("radioButtonGroupError").style.display = "none"
        var levels = document.getElementsByName("group1");
        var checked = false;
        var item;
        for (var level of levels){
            if (level.checked === true){
                checked = true;
                item = level.id;       
            }
        }
        if (checked === false){
            document.getElementById("radioButtonGroupError").style.display = "block"
            return;
        }
        var sdday = state[0].startDate
        var edday = state[0].endDate

        const monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
        var days = (sdday.getDate()<10?'0':'') + sdday.getDate();  
        var month = sdday.getMonth() + 1;
        var monthInt = (month<10?'0':'') + month;
        var newStartDateInt =  days + "." + monthInt + "." + sdday.getFullYear() + ".";

        var days = (edday.getDate()<10?'0':'') + edday.getDate();  
        var month = edday.getMonth() + 1;
        var monthInt = (month<10?'0':'') + month;
        var newEndDateInt =  days + "." + monthInt + "." + edday.getFullYear() + ".";
    
        var formData = new FormData();
        formData.append("startDate", newStartDateInt )
        formData.append("endDate", newEndDateInt )
        formData.append("level", item);
        var link;
        if (type==="adventure")
            link = backLink + "/fishinginstructor/getAttendanceReport/" + id
        else if (type==="boat")
            link = backLink + "/boatowner/getAttendanceReport/" + id
        else
            link = backLink + "/houseowner/getAttendanceReport/" + id 
            
       axios.post(link, formData).then(res => {       
            setData(res.data);
        })
    }

    useEffect(() => { 
        setChartData(
            {
                labels: data.dates,
                datasets: [
                  {
                    label: "Posećenost",
                    data: data.incomes,
                    fill: true,
                    backgroundColor: [
                      "#0d6efd"
                    ]
                  }
                ]
                }
        )
    }, [data]);

   
    return (
        <>
        <Form className="w-100">
            <div key={`inline-${"radio"}`} className="mb-3 w-100 d-flex justify-content-between ps-3 pe-3">
                <Form.Check inline label="Nedeljni nivo" name="group1" type={"radio"} id="weekly"/>              
                <Form.Check inline label="Mesečni nivo" name="group1" type={"radio"} id="monthly" />
                <Form.Check inline label="Godišnji nivo" name="group1" type={"radio"} id="yearly" /> 
             </div>
             <p id="radioButtonGroupError" style={{color:"#dc3545", fontSize: "0.875em", marginLeft:"34%", display:"none"}}>Molimo Vas izaberite na kom nivou želite prikazati izveštaj.</p>
        </Form>
            <div className='d-flex'>
                <DateRangePicker onChange={item => setState([item.selection])}  preventSnapRefocus={true} showSelectionPreview={true} moveRangeOnFirstSelection={false} months={2} ranges={state} direction="horizontal"/>
            </div>
            <div className='d-flex justify-content-end'>
                <Button className="btn btn-primary" onClick={e => showReport()}>Prikaži</Button>
            </div>
            <div className='d-flex justify-content-center'>
                { typeof data.dates !== "undefined" ?
                    <div className='w-100 h-100'>
                    <Bar className="w-100 h-100" data={chartData} options={options}/>
                  </div>
                : <></>
                }
            </div>
        </>
    );
}

export default AttendanceReport;