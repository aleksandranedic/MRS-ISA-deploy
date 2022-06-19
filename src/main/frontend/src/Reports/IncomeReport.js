import React, { useEffect, useState } from 'react';
import { addDays } from 'date-fns';
import { DateRangePicker } from 'react-date-range';
import { Button } from 'react-bootstrap';
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
} from 'chart.js';
import { Bar } from 'react-chartjs-2';

ChartJS.register(
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

function IncomeReport({type}) {
    const {id} = useParams();
    const [data, setData] = useState({})
    const [chartData, setChartData] = useState()
    const [state, setState] = useState([
        {
            startDate: new Date(),
            endDate: addDays(new Date(), 7),
            key: 'selection'
        }
    ]);

    const showReport = () => {
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
    
        var link;
        if (type==="adventure")
            link = backLink + "/fishinginstructor/getIncomeReport/" + id
        else if (type==="boat")
            link = backLink + "/boatowner/getIncomeReport/" + id
        else
            link = backLink + "/houseowner/getIncomeReport/" + id 
            
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
                    label: "Prihodi u €",
                    data: data.incomes,
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
            <div className='d-flex'>
                <DateRangePicker onChange={item => setState([item.selection])}  preventSnapRefocus={true} showSelectionPreview={true} moveRangeOnFirstSelection={false} months={2} ranges={state} direction="horizontal"/>
            </div>
            <div className='d-flex justify-content-end'>
                <Button className="btn btn-primary" onClick={e => showReport()}>Prikaži</Button>
            </div>
            <div className='d-flex justify-content-center'>
                { typeof data.dates !== "undefined" ?
                    <div className='w-100 h-100'>
                    <Bar className="w-100 h-100" id="barko" data={chartData} options={options}/>
                  </div>
                : <></>
                }
            </div>
        </>
    );
}

export default IncomeReport;