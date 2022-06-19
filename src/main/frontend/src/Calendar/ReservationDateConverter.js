export function getStartTime(reservation) {
    if (reservation.appointments.length == 0) {
        return "";
    }

    let dateArray = reservation.appointments.at(0).startTime;
    return getDateFromArray(dateArray);
}

function getDateFromArray(dateArray) {
    let d = new Date(dateArray.at(0), dateArray.at(1) - 1, dateArray.at(2), dateArray.at(3), dateArray.at(4));

    return d.getDate().toString().padStart(2, '0') + "-" + (d.getMonth() + 1).toString().padStart(2, '0') + "-" + d.getFullYear() + " " +
        d.getHours().toString().padStart(2, '0') + ":" + d.getMinutes().toString().padStart(2, '0');
}

export function getEndTime(reservation) {
    if (reservation.appointments.length == 0) {
        return "";
    }
    let dateArray = reservation.appointments.at(reservation.appointments.length - 1).endTime;
    return getDateFromArray(dateArray);
}

export function convertToDate(dateArray) {
    return new Date(dateArray.at(0), dateArray.at(1) - 1, dateArray.at(2), dateArray.at(3), dateArray.at(4));
}