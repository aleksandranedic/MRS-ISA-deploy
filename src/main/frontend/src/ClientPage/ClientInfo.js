import React from 'react'
import {AddressInfo, Info} from "../Info";

export default function ClientInfo({firstName, lastName, email, phoneNumber, address, biography}) {
  return (
    <div className="p-5 pt-3 mt-4">
    <h4 className="fw-light text-center">
        {biography}
    </h4>
    <div className="d-flex">
        <div id="left-column" className="w-50 pe-2">
            <Info title="Ime i prezime" text={firstName + " " + lastName}/>
            <Info title="E-mail adresa" text={email}/>
            <Info title="Broj telefona" text={phoneNumber}/>
        </div>

        <div id="right-column" className="w-50">
            <AddressInfo title="Adresa" address={address}/>
        </div>
    </div>
</div>
  )
}
