import React from 'react';
import Banner from './Banner';
import Navigation from './Navigation/Navigation';

function PageNotFound(props) {
    return (
        <div>
            <Banner/>
            <Navigation buttons={ []} searchable={true} showProfile={true}/>
            <div className='d-flex justify-content-center m-5'>
                <h1 className='lead' style={{fontSize:"70px"}}>Error 404 - Page not found</h1>
            </div>    
        </div>
    );
}

export default PageNotFound;