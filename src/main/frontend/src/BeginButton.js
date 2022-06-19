import React from 'react';
import {Button} from 'react-bootstrap';
import {BsChevronUp} from "react-icons/bs"
import './BeginButton.css';

function BeginButton(props) {
    return (
        <div>
            <a href="#" className='begin'>
                <Button className="btn-dark font-monospace rounded-circle me-3"><BsChevronUp/></Button>
            </a>
        </div>
    );
}

export default BeginButton;