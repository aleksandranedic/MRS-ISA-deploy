import {Button, Form, Modal, InputGroup} from "react-bootstrap";
import axios from "axios"; 
import React, {useState, useRef} from "react";
import { useParams } from "react-router-dom";
import { backLink, notifyError, notifySuccess } from "../Consts";
import {ToastContainer} from "react-toastify";

axios.interceptors.request.use(config => {
    config.headers.authorization = "Bearer " + localStorage.getItem('token')
    return config
}
)

export function BoatOwnerForm({show, setShow, owner, profileImg}) {
    const {id} = useParams();
    const form = useRef();
    const [showPassword, setShowPassword] = useState(false);
    const [validatedInfo, setValidatedInfo] = useState(false);
    const [boatOwner, setBoatOwner] = useState(owner);
    const [showDelete, setShowDelete] = useState(false);
    const handleShowDelete = () => setShowDelete(true);
    const handleShowPassword = () => setShowPassword(true);

    const setFirstName = (value) => {
        setBoatOwner( prevState => {
           return {...prevState, firstName:value}
        })
    }
    const setLastName = (value) => {
        setBoatOwner( prevState => {
           return {...prevState, lastName:value}
        })
    }
    const setPhoneNumber = (value) => {
        setBoatOwner( prevState => {
           return {...prevState, phoneNumber:value}
        })
    }

    const setStreet = (value) => {
        setBoatOwner( prevState => {
            var newaddress = boatOwner.address
            newaddress.street = value
            return {...prevState, address:newaddress}
        })
    }
    const setNumber = (value) => {
        setBoatOwner( prevState => {
            var newaddress = boatOwner.address
            newaddress.number = value
            return {...prevState, address:newaddress}
        })
    }
    const setCity = (value) => {
        setBoatOwner( prevState => {
            var newaddress = boatOwner.address
            newaddress.place = value
            return {...prevState, address:newaddress}
        })
    }
    const setCountry = (value) => {
        setBoatOwner( prevState => {
            var newaddress = boatOwner.address
            newaddress.country = value
            return {...prevState, address:newaddress}
        })
    }

    const opetFileExplorer = () => {
        document.getElementById("fileImage").click();
    }

    const setProfileImageView = () => {
        var file = document.getElementById("fileImage").files[0]
        document.getElementById("profPic").src = URL.createObjectURL(file);
    }

    const handleSubmit = e => {
        e.preventDefault()
        if (form.current.checkValidity() === false) {
            e.stopPropagation();
            setValidatedInfo(true);
        }
        else {
            var file = document.getElementById("fileImage").files[0];
            if (typeof file !== "undefined"){
                var files = document.getElementById("fileImage").files;
                var data = new FormData();
                var images = []
                for (let i=0; i < files.length; i++){
                    images.push(files[i])
                }
                data.append("fileImage",file);
                axios
                .post(backLink + "/boatowner/changeProfilePicture/" + id, data)
                .then(res => {
                       console.log(res.data)
                });
            }

            var data = new FormData(form.current);
            data.append("street", boatOwner.address.street)
            data.append("number", boatOwner.address.number)
            data.append("place", boatOwner.address.place)
            data.append("country", boatOwner.address.country)
            axios
            .post(backLink + "/boatowner/updateOwner/" + id, data)
            .then(res => {
                window.location.reload();
            });
        }
    }
    return <Modal size="lg" show={show} onHide={() => setShow(false)}>
        <Form ref={form} noValidate validated={validatedInfo}>
            <Modal.Header closeButton>
                <Modal.Title>Ažuriranje profila</Modal.Title>
            </Modal.Header>

            <Modal.Body>

                <div className="d-flex mb-3">
                    <div className="d-flex justify-content-center" style={{width:"28%"}}>
                        <img id="profPic" className="rounded-circle" style={{objectFit: "cover", maxWidth: "25vh", minWidth: "25vh", maxHeight: "25vh", minHeight: "25vh"}} src={profileImg}/>
                        <Form.Control id="fileImage" onChange={e => setProfileImageView()} className="d-none" type="file" name="fileImage" style={{position:"absolute", width:"25vh", top:"12vh"}}/>  {/*ref={imagesRef} */}
                        <p id="setNewProfileImage" className="d-flex justify-content-center align-items-center" onClick={e => opetFileExplorer()}><u>Postavite profilnu</u></p>
                    </div>
                    <div style={{width:"72%"}}>
                        <Form.Group className="mb-3 m-2" controlId="firstName">
                            <Form.Label>Ime</Form.Label>
                            <Form.Control required type="text" name="firstName" defaultValue={boatOwner.firstName} onChange={e => setFirstName(e.target.value)}/>
                            <Form.Control.Feedback type="invalid">Molimo Vas unesite ime.</Form.Control.Feedback>               
                        </Form.Group>

                        <Form.Group className="mb-3 m-2" controlId="lastName">
                            <Form.Label>Prezime</Form.Label>
                            <Form.Control required type="text" name="lastName" defaultValue={boatOwner.lastName} onChange={e => setLastName(e.target.value)}/>
                            <Form.Control.Feedback type="invalid">Molimo Vas unesite prezime.</Form.Control.Feedback>               
                        </Form.Group>
                    </div>
                </div>

                <Form.Group className="mb-3 m-2" controlId="phoneNumber">
                    <Form.Label>Broj telefona</Form.Label>
                    <Form.Control required type="text" name="phoneNumber" defaultValue={boatOwner.phoneNumber} onChange={e => setPhoneNumber(e.target.value)}/>
                    <Form.Control.Feedback type="invalid">Molimo Vas unesite broj telefona.</Form.Control.Feedback>               
                </Form.Group>

                <div className="d-flex">
                    <Form.Group className="mb-3 m-2">
                        <Form.Label>Ulica i broj</Form.Label>
                        <InputGroup>
                        <Form.Control required className='w-50' type="text" defaultValue={boatOwner.address.street} onChange={e => setStreet(e.target.value)}/>
                        <Form.Control required className='w-50' type="text" defaultValue={boatOwner.address.number} onChange={e => setNumber(e.target.value)}/>
                        <Form.Control.Feedback type="invalid">Molimo Vas unesite ulicu i broj.</Form.Control.Feedback>               
                        </InputGroup>
                    </Form.Group>

                    <Form.Group className="mb-3 m-2" controlId="place">
                        <Form.Label>Mesto</Form.Label>
                        <Form.Control required type="text" defaultValue={boatOwner.address.place} onChange={e => setCity(e.target.value)}/>
                        <Form.Control.Feedback type="invalid">Molimo Vas unesite mesto.</Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group className="mb-3 m-2" controlId="country">
                        <Form.Label>Drzava</Form.Label>
                        <Form.Control required type="text" defaultValue={boatOwner.address.country} onChange={e => setCountry(e.target.value)}/>
                        <Form.Control.Feedback type="invalid">Molimo Vas unesite državu.</Form.Control.Feedback>
                    </Form.Group>
                </div>
                <Button variant="link" onClick={handleShowPassword}>Promenite lozinku</Button>
                <ChangePassword  show={showPassword} setShow={setShowPassword}/>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="outline-danger" className="me-auto" onClick={handleShowDelete}> Obriši</Button>
                <DeleteAccount  show={showDelete} setShow={setShowDelete}/>
                <Button variant="secondary" onClick={()=>setShow(false)}> Otkazi  </Button>
                <Button variant="primary" onClick={handleSubmit}> Izmeni </Button>
            </Modal.Footer>
        </Form>
    </Modal>
}

export function ChangePassword({show, setShow}) {
    const formPassword = useRef();
    const {id} = useParams();
    const [validatedPassword, setvalidatedPassword] = useState(false);
    const [passwords, setPasswords] = useState({oldPassword:'', newPassword:''});
    const handleClose = () => setShow(false);
    
    const changePassword = e => {
        document.getElementById("invalidOldPassword").style.display = "none"
        e.preventDefault()
        if (formPassword.current.checkValidity() === false) {
            e.stopPropagation();
            setvalidatedPassword(true);
        }
        else {
            var fd = new FormData(formPassword.current);
            fd.append("oldPassword", passwords.oldPassword)
            fd.append("newPassword", passwords.newPassword)
            var dto = {
                oldPassword: passwords.oldPassword,
                newPassword: passwords.newPassword
            }

            axios
            .post(backLink + "/user/changePassword", dto, { headers: {"Content-Type": "application/json"} })
            .then( res => {
                localStorage.setItem('token', res.data)
                notifySuccess("Uspešno ste promenili šifru");
                setTimeout(function(){setShow(false)}, 2500);
            })
            .catch(function (error) {
                notifyError(error.response.data)
            });        
        }
    }
    
    const setOldPassword = (value) => {
        setPasswords( prevState => {
           return {...prevState, oldPassword:value}
        })
    }
    const setNewPassword = (value) => {
        setPasswords( prevState => {
           return {...prevState, newPassword:value}
        })
    }
    return <Modal show={show} onHide={() => setShow(false)}>
        <Form noValidate validated={validatedPassword} ref={formPassword}>
            <Modal.Header closeButton>
                <Modal.Title>Promena lozinke</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form.Group className="mb-3 m-2" controlId="oldPassword">
                    <Form.Label>Stara lozinka</Form.Label>
                    <Form.Control name="oldPassword" required type="password" defaultValue={passwords.oldPassword} onChange={e => setOldPassword(e.target.value)}/>
                    <Form.Control.Feedback type="invalid">Molimo Vas unesite staru lozinku.</Form.Control.Feedback>
                    <p id="invalidOldPassword" style={{color:"#dc3545", fontSize: "0.875em", marginLeft:"0%", display:"none"}}>Trenutna lozinka je pogrešno uneta.</p>
                </Form.Group>

                <Form.Group className="mb-3 m-2" controlId="newPassword">
                    <Form.Label>Nova lozinka</Form.Label>
                    <Form.Control name="newPassword" required type="password" defaultValue={passwords.newPassword} onChange={e => setNewPassword(e.target.value)}/>
                    <Form.Control.Feedback type="invalid">Molimo Vas unesite novu lozinku.</Form.Control.Feedback>
                </Form.Group>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}> Otkazi </Button>
                <Button variant="primary" onClick={changePassword}> Izmeni </Button>
            </Modal.Footer>
        </Form>
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
}
export function DeleteAccount({show, setShow}) {
    const formDelete = useRef();
    const handleClose = () => setShow(false);
    const [validatedReason, setValidatedReason] = useState(false);
    const {id} = useParams();
    const [deleteReason, setDeleteReason] = useState(false);

    const deleteUser = e => {
        e.preventDefault()
        if (formDelete.current.checkValidity() === false) {
            e.stopPropagation();
            setValidatedReason(true);
        }
        else {          
            axios
            .delete(backLink + "/deletionRequests/boatowner/" + id, {
                headers: {"Content-Type": "text/plain"},
                data: deleteReason,
            })
            .then(res => {
                notifySuccess(res.data)
                setTimeout(function(){setShow(false)}, 2500);
                })
                .catch(function (error) {
                    notifyError(error.response.data)
                });
        }
    }
          
    return <Modal show={show} onHide={() => setShow(false)}>
        <Form noValidate validated={validatedReason} ref={formDelete}>
            <Modal.Header closeButton>
                <Modal.Title>Brisanje naloga</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form.Group className="mb-3 m-2" controlId="oldPassword">
                    <Form.Label>Unesite razlog brisanja naloga</Form.Label>
                    <Form.Control name="deletingReason" required as="textarea" rows={3} onChange={e => setDeleteReason(e.target.value)}/>
                    <Form.Control.Feedback type="invalid">Molimo Vas unesite razlog brisanja naloga.</Form.Control.Feedback>                  
                </Form.Group>
         
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}> Otkazi </Button>
                <Button variant="primary" onClick={deleteUser}> Obriši </Button>
            </Modal.Footer>
        </Form>
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
}
export default BoatOwnerForm;
