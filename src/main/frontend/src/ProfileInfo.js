import React from 'react';

function ProfileInfo({icon, label, text}) {
    return (
        <div className='w-100 d-flex justify-content-around pt-4 infoDiv' style={{width:"100%"}}>
            <div>
                <p className="lead fw-normal m-0 p-0">
                    {React.createElement(icon, {'viewBox': '0 0 19 19'})}
                    {label}
                </p>
                <p className="fw-light mb-4">{text}</p>
            </div>
        </div>
    );
}

export default ProfileInfo;