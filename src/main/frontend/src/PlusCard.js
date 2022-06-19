import React from 'react'
import {} from 'bootstrap'
import {Button, Card} from 'react-bootstrap'
import {BsFillFilePlusFill} from "react-icons/bs";

export default function PlusCard({onClick}) {
    return (<div>
            <Card style={{width: '18rem', height: '25rem'}}>


                <div style={{width: '18rem', height: '25rem'}} className="d-flex justify-content-center align-items-center">
                    <Button style={{width: '8rem', height: '10rem', backgroundColor: 'white', border: 'none'}}
                            className="d-flex justify-content-center align-items-center"
                            onClick={onClick}>
                        <BsFillFilePlusFill variant="primary" style={{width: '8rem', height: '10rem', color:"lightgray"}}/>
                    </Button>
                </div>


            </Card>


        </div>


    )
}