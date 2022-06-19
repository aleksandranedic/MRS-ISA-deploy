import {AiOutlineCalendar, AiOutlineClockCircle} from "react-icons/ai";
import React from "react";

export function TimeFooter({reservation}) {

    if (reservation.appointments.length == 0) {
        return "";
    }

    let startDateArray = reservation.appointments.at(0).startTime;
    let endDateArray = reservation.appointments.at(reservation.appointments.length - 1).endTime;

    let startDate = startDateArray.at(2).toString().padStart(2, '0') +
        "."
        + startDateArray.at(1).toString().padStart(2, '0') +
        "."
        + startDateArray.at(0).toString() +
        ".";
    let startTime = startDateArray.at(3).toString().padStart(2, '0') +
        ":"
        + startDateArray.at(4).toString().padStart(2, '0');

    let endDate = endDateArray.at(2).toString().padStart(2, '0') +
        "."
        + endDateArray.at(1).toString().padStart(2, '0') +
        "."
        + endDateArray.at(0).toString() +
        ".";
    let endTime = endDateArray.at(3).toString().padStart(2, '0') +
        ":"
        + endDateArray.at(4).toString().padStart(2, '0');


        let html =
        <div className="p-0 m-0 d-flex justify-content-between ">

                <div className="d-flex align-items-center">
                    <AiOutlineCalendar className="ms-1 me-1"/>
                    {startDate}
                    <AiOutlineClockCircle className="ms-2 me-1"/>
                    {startTime}
                </div>
                -
                <div className="d-flex align-items-center">
                    <AiOutlineCalendar className="ms-1 me-1"/>
                    {endDate}
                    <AiOutlineClockCircle className="ms-2 me-1"/>
                    {endTime}
                </div>

        </div>;

    if (startDate === endDate) {
        html =
            <div className="p-0 m-0 d-flex justify-content-between">
                <div className="d-flex align-items-center">
                    <AiOutlineCalendar className="ms-1 me-1"/>
                    <p className="p-0 m-0">
                        {startDate}
                    </p>
                </div>
                <div className="d-flex align-items-center">
                    <AiOutlineClockCircle className="ms-1 me-1"/>
                    {startTime}-{endTime}
                </div>
            </div>
    }

    if (startTime === endTime) {
        html = <div className="p-0 m-0 d-flex justify-content-between">
            <div className="d-flex align-items-center">
                <AiOutlineCalendar className="ms-1 me-1"/>
                <p className="p-0 m-0">
                    {startDate}-{endDate}
                </p>
            </div>
            <div className="d-flex align-items-center">
                <AiOutlineClockCircle className="ms-1 me-1"/>
                {startTime}
            </div>
        </div>
    }

    return html;
}