import axios from 'axios';
import {React, useState, useEffect} from 'react';
import { MapContainer, TileLayer, Popup, Marker } from 'react-leaflet'
import { locationPlaceholder, NominatimBaseUrl } from './Consts';
import L from 'leaflet'

function Map({address}) {
  let [coordinates, setCoordinates] = useState([null,null])
  let [params, seParams] = useState({q: address, format: 'json', addressdetails: 1, polygon_geojson: 0 });
  
  const fetchCoordinates = () => {
    const queryStr = new URLSearchParams(params).toString();
    axios
    .get(NominatimBaseUrl + queryStr)
    .then(res => {
        var longitude = res.data[0].lon;
        var latitude = res.data[0].lat;
        setCoordinates([latitude, longitude])
    });
  }
  useEffect(() => {
    fetchCoordinates();
  }, []); 
  
  const icon = L.icon(locationPlaceholder)
  if (coordinates[0] !== null) {
    return (
      <MapContainer center={coordinates} zoom={17} scrollWheelZoom={false} className="w-100 h-100">
        <TileLayer
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          url="https://api.maptiler.com/maps/basic/256/{z}/{x}/{y}.png?key=YlGliPBoKrWl8zWz0FjU"
        />
        <Marker position={coordinates} icon={icon}>
          <Popup>
            {address}
          </Popup>
        </Marker>
      </MapContainer>
      );
  }
  else return <></>
  
}

export default Map;