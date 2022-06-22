import axios from 'axios';
import React, {useState, useRef, useEffect} from 'react';
import {Modal, Button} from 'react-bootstrap'
import UpdateHouseForm from './UpdateHouseForm';
import { useParams } from "react-router-dom";
import { backLink, frontLink, notifyError, notifySuccess } from '../Consts';
import {isBoatOwner, isClient, isFishingInstructor, isLoggedIn, isVacationHouseOwner} from "../Autentification";
import {ToastContainer} from "react-toastify";

function UpdateHouse({showModal, closeModal, vacationHouse}) {
  const form = useRef();
  const imagesRef = useRef();
  const {id} = useParams();
  const [validated, setValidated] = useState(false);
  const [state, setState] = useState({name:'', price:'', description:'', numberOfRooms:'', capacity:'', rulesAndRegulations:'', street:'', number:'', city:'', country:'', additionalServices:[{id:'', text:''}], cancellationFee:'', imagePaths:['']});
    
  useEffect(() => {
      setState(vacationHouse);
    }, []);

    const deleteVacationHouse = () => {
      var profileLink;
      if (isLoggedIn()) {
        if (isClient()) {
            profileLink = frontLink + "client/" + localStorage.getItem("userId");
        } else if (isVacationHouseOwner()) {
            profileLink = frontLink + "houseOwner/" + localStorage.getItem("userId");
        } else if (isBoatOwner()) {
            profileLink = frontLink + "boatOwner/" + localStorage.getItem("userId");
        } else if (isFishingInstructor()) {
            profileLink = frontLink + "fishingInstructor/" + localStorage.getItem("userId");
        }
  
      }
      axios
      .get(backLink + "/house/delete/" + id)
      .then(res => {
        notifySuccess("Uspešno ste obrisali vikendicu.")
        setTimeout(window.location.href = profileLink, 1500);
      } )
      .catch(function (error) {
        notifyError(error.response.data)
    });
    }  

  const submit = e => {
    e.preventDefault()
    if (form.current.checkValidity() === false || state.imagePaths.length === 0) {
      document.getElementById("noImages").style.display = "block"
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
      state.tagsText = []
      for (let i=0; i < state.additionalServices.length; i++){
        if (state.additionalServices[i].text !== ''){
          state.tagsText.push(state.additionalServices[i].text)
        }
      }
      var imgs = []
      for (let i=0; i<state.imagePaths.length;i++){
        if (!state.imagePaths[i].includes("blob")){
          imgs.push(state.imagePaths[i].replace(backLink, ''));
        }
      }
      data.append("name", state.name)
      data.append("price", state.price)
      data.append("description", state.description)
      data.append("capacity", state.capacity)
      data.append("numberOfRooms", state.numberOfRooms)
      data.append("rulesAndRegulations", state.rulesAndRegulations)
      data.append("city", state.city)
      data.append("number", state.number)
      data.append("street", state.street)
      data.append("country", state.country)
      data.append("cancellationFee", state.cancellationFee)
      data.append("imagePaths", imgs)
      data.append("tagsText", state.tagsText)
      data.append("fileImage",images);
      axios
      .post(backLink + "/house/updateHouse/" + id, data)
      .then(res => {
        window.location.reload();
      });
      closeModal();
    }
  }
  function close(){
    Reset();
    closeModal();
  }
  const Reset = () => {
    setValidated(false);
    setState(vacationHouse);
  }
    return (
        <Modal show={showModal} onHide={close} size="lg">
        <Modal.Header>
          <Modal.Title>Ažuriranje vikendice</Modal.Title>
        </Modal.Header>
      
        <Modal.Body>
          <UpdateHouseForm state={state} setState={setState} reference={form} imagesRef={imagesRef} validated={validated}/>
        </Modal.Body>
      
        <Modal.Footer className="justify-content-between">
          <Button variant="outline-danger" onClick={e => deleteVacationHouse()} >Obriši</Button>
          <div>
          <Button className="me-2" variant="secondary" onClick={close}>Nazad</Button>
          <Button variant="primary" onClick={submit} >Sačuvaj</Button>
          </div>
        </Modal.Footer>
        <ToastContainer
                position="top-center"
                autoClose={3000}
                hideProgressBar={false}
                newestOnTop={true}
                closeOnClick
                rtl={false}
                pauseOnFocusLoss
                draggable
                pauseOnHover
                theme={"colored"}
                
                />
      </Modal>
    );
}

export default UpdateHouse;