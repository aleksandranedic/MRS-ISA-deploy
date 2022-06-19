import React, {useEffect, useState} from "react";
import Banner from "../Banner";
import Navigation from "../Navigation/Navigation";
import axios from "axios";
import {useParams} from "react-router-dom";
import {backLink} from "../Consts";
import SearchResultsResources from "./SearchResultsItems";


export function SearchResultsPage() {

    const [boats, setBoats] = useState([]);
    const fetchBoats = () => {
        let array;
        axios.get(backLink + "/boat/entity",).then(res => {
            array = res.data;
            if (searchTerm !== "" && searchTerm !== "searchTerm" && searchTerm !== undefined) {
                array = array.filter(boat => boat.title.toString().toLowerCase().includes(searchTerm.toLowerCase()))
            }
            setBoats(array);
        });
    };
    const [vacationHouses, setVacationHouses] = useState([]);
    const fetchVacationHouses = () => {
        let array;
        axios.get(backLink + "/house/entity",).then(res => {
            array = res.data;
            if (searchTerm !== "" && searchTerm !== "searchTerm" && searchTerm !== undefined) {
                array = array.filter(boat => boat.title.toString().toLowerCase().includes(searchTerm.toLowerCase()))
            }
            setVacationHouses(array);
        });
    }
    const [adventures, setAdventures] = useState([]);
    const fetchAdventures = () => {
        let array;
        axios.get(backLink + "/adventure/entity",).then(res => {
            array = res.data;
            if (searchTerm !== "" && searchTerm !== "searchTerm" && searchTerm !== undefined) {
                array = array.filter(boat => boat.title.toString().toLowerCase().includes(searchTerm.toLowerCase()))
            }
            setAdventures(array);
        });
    };


    const {searchTerm} = useParams();

    useEffect(() => {
        fetchVacationHouses()
        fetchBoats()
        fetchAdventures()
    }, []);

    const updateResults = (formValues) => {
        const adventureFilter = {
            adventuresChecked: formValues.adventuresChecked,
            numberOfClients: formValues.numberOfClients,
            fishingInstructorName: formValues.fishingInstructorName,
            reviewRating: formValues.reviewRating,
            priceRange: formValues.priceRange,
            startDate: formValues.startDate.toString(),
            endDate: formValues.endDate.toString(),
            startTime: formValues.startTime.toString(),
            endTime: formValues.endTime.toString(),
            location: formValues.location,
            cancellationFee: formValues.cancellationFee
        }
        console.log(adventureFilter)
        axios.post(backLink + "/adventure/filterAdventures", adventureFilter).then(
            response => {
                setAdventures(response.data)
            }
        )
        const houseFilter = {
            vacationHousesChecked: formValues.vacationHousesChecked,
            numOfVacationHouseRooms: formValues.numOfVacationHouseRooms,
            numOfVacationHouseBeds: formValues.numOfVacationHouseBeds,
            vacationHouseOwnerName: formValues.vacationHouseOwnerName,
            reviewRating: formValues.reviewRating,
            priceRange: formValues.priceRange,
            startDate: formValues.startDate,
            endDate: formValues.endDate,
            startTime: formValues.startTime,
            endTime: formValues.endTime,
            location: formValues.location,
            cancellationFee: formValues.cancellationFee
        }
        console.log(houseFilter)
        axios.post(backLink + "/house/filterHouse", houseFilter).then(
            response => {
                setVacationHouses(response.data)
            }
        )
        const boatFilter = {
            boatsChecked: formValues.boatsChecked,
            boatType: formValues.boatType,
            boatEnginePower: formValues.boatEnginePower,
            boatEngineNum: formValues.boatEngineNum,
            boatMaxSpeed: formValues.boatMaxSpeed,
            boatCapacity: formValues.boatCapacity,
            boatOwnerName: formValues.boatOwnerName,
            reviewRating: formValues.reviewRating,
            priceRange: formValues.priceRange,
            startDate: formValues.startDate,
            endDate: formValues.endDate,
            startTime: formValues.startTime,
            endTime: formValues.endTime,
            location: formValues.location,
            cancellationFee: formValues.cancellationFee
        }
        axios.post(backLink + "/boat/filterBoats", boatFilter).then(
            response => {
                setBoats(response.data)
            }
        )
    }

    return (
        <>
            <Banner caption={"Rezultati pretrage"}/>
            <Navigation updateResults={updateResults} buttons={[]} editable={false} searchable={true}/>
            <div style={{marginLeft: "5%", marginRight: "10%"}}>
                <h4 className="fw-light m-3">Vikendice</h4>
                <hr/>
                <SearchResultsResources list={vacationHouses} name={"house"} />
                <h4 className="fw-light m-3">Brodovi</h4>
                <hr/>
                <SearchResultsResources list={boats} name={"boat"}/>
                <h4 className="fw-light m-3">Avanture</h4>
                <hr/>
                <SearchResultsResources list={adventures} name={"adventure"}/>
            </div>
        </>


    )
}