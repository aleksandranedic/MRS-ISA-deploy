import {convertToDate} from "./Calendar/ReservationDateConverter";


export function processReservationsForResources(reservations, myPage) {
    let newEvents = [];


    for (let index in reservations) {
        let r = reservations.at(index);

        if (r.appointments.length>0) {
            let start = convertToDate(r.appointments.at(0).startTime);
            start.setHours(start.getHours() + 2);
            let end = convertToDate(r.appointments.at(r.appointments.length - 1).endTime);
            end.setHours(end.getHours() + 2);

            if (r.busyPeriod === true) {
                newEvents.push({
                    title: "",
                    start: start,
                    end: end,
                    backgroundColor: "rgb(224,48,22)"
                })
            }
            else {
                let clientName = "";
                if (myPage) {
                    clientName = r.client.firstName + " " + r.client.lastName;
                }
                newEvents.push({
                    title: clientName,
                    start: start,
                    end: end,
                    backgroundColor: "rgb(34,215,195)"
                })
            }
        }

    }
    return newEvents;
}

export function processReservationsForUsers(reservations) {
    let newEvents = [];

    for (let index in reservations) {
        let r = reservations.at(index);

        let start = convertToDate(r.appointments.at(0).startTime);
        start.setHours(start.getHours() + 2);
        let end = convertToDate(r.appointments.at(r.appointments.length - 1).endTime);
        end.setHours(end.getHours() + 2);

        if (r.busyPeriod === true) {
            newEvents.push({
                title: "",
                start: start,
                end: end,
                backgroundColor: "rgb(224,48,22)"
            })
        }
        else {

            let title = r.resourceTitle;

            newEvents.push({
                title: title,
                start: start,
                end: end,
                backgroundColor: "rgb(34,215,195)"
            })
        }

    }
    return newEvents;
}