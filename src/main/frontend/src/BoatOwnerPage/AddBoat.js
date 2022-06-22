import React, {useState, useRef} from 'react';
import {Modal, Button} from 'react-bootstrap'
import AddBoatForm from './AddBoatForm';
import axios from "axios";  
import { useParams } from "react-router-dom";
import { backLink } from '../Consts';

function AddBoat({showModal, closeModal}) {
  const {id} = useParams();
  const statePlaceHolder = {name:'', price:'', description:'', type:'', engineStrength:'', topSpeed:'', length:'', engineNumber:'', capacity:'', rulesAndRegulations:'', street:'', number:'', city:'', country:'', navigationEquipment:[{id:0, text:''}], fishingEquipment:[{id:0, text:''}], additionalServices:[{id:0, text:''}], cancellationFee:'', imagePaths:[]};
  let [state, setState] = useState(statePlaceHolder)
  const form = useRef();
  const imagesRef = useRef();
  const [validated, setValidated] = useState(false);

  const submit = e => {
    e.preventDefault()

    if (form.current.checkValidity() === false) {
      e.stopPropagation();
      setValidated(true);
    }
    else {
      var files = imagesRef.current.files;
      var data = new FormData(form.current);
      var images = []
      for (let i=0; i < files.length; i++){
        images.push(files[i])
      }
      data.append("fileImage",images);
      state.address = state.street + " " + state.number + ", " + state.city + ", " + state.country;
      state.quickReservations = [];
      
      state.tagsText = []
      for (let i=0; i < state.navigationEquipment.length; i++){
        if (state.navigationEquipment[i].text !== ''){
          state.tagsText.push(state.navigationEquipment[i].text)
        }
      }
      data.append("tagsText", state.tagsText)

      state.tagsFishingEquipText = []
      for (let i=0; i < state.fishingEquipment.length; i++){
        if (state.fishingEquipment[i].text !== ''){
          state.tagsFishingEquipText.push(state.fishingEquipment[i].text)
        }
      }
      data.append("tagsFishingEquipText", state.tagsFishingEquipText)

      state.tagsAdditionalServicesText = []
      for (let i=0; i < state.additionalServices.length; i++){
        if (state.additionalServices[i].text !== ''){
          state.tagsAdditionalServicesText.push(state.additionalServices[i].text)
        }
      }
      data.append("tagsAdditionalServicesText", state.tagsAdditionalServicesText)

      data.append("address", state.address)
      axios
      .post(backLink + "/boat/createBoat", data)
      .then(res => {
            var boatID = res.data
            axios.post(backLink + "/boatowner/addBoat/" + id, boatID).then( res => {window.location.reload();});
      });
      close();
    }
  
  }
  function close(){
    Reset();
    closeModal();
  }
  const Reset = () => {
    setValidated(false);
    setState(statePlaceHolder);
  }

    return (
        <Modal show={showModal} onHide={close} size="lg">
        <Modal.Header>
          <Modal.Title>Dodavanje broda</Modal.Title>
        </Modal.Header>
      
        <Modal.Body>
          <AddBoatForm state={state} setState={setState} reference={form} imagesRef = {imagesRef} validated={validated}/>
        </Modal.Body>
      
        <Modal.Footer>
          <Button variant="secondary" onClick={close}>Zatvori</Button>
          <Button variant="primary" onClick={submit}>Dodaj</Button>
        </Modal.Footer>
      </Modal>
    );
}

export default AddBoat;