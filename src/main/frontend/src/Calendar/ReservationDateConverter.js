import star from "react-star-ratings/build/star";

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


export function compareDates(reservation1, reservation2) {
    let start1 = convertToDate(reservation1.appointments.at(0).startTime);
    let start2 = convertToDate(reservation2.appointments.at(0).startTime);
    return start1 > start2;
}

export function compareDuration(reservation1, reservation2) {
    let start1 = convertToDate(reservation1.appointments.at(0).startTime);
    let end1 = convertToDate(reservation1.appointments.at(reservation1.appointments.length - 1).endTime)
    let start2 = convertToDate(reservation2.appointments.at(0).startTime);
    let end2 = convertToDate(reservation2.appointments.at(reservation2.appointments.length - 1).endTime)

    return (end1 - start1) > (end2 - start2);
}

export function compareDateStrings(start, end) {

    if (start === "" || end === "") {
        return false;
    }

    let startDate = new Date(start.split("-")[0],start.split("-")[1]-1,start.split("-")[2]);
    let endDate = new Date(end.split("-")[0],end.split("-")[1]-1,end.split("-")[2]);
    return startDate >= endDate;

}

export function compareDateStringToToday(string) {
    if (string === "") {
        return false;
    }

    let date = new Date(string.split("-")[0],string.split("-")[1]-1,string.split("-")[2]);

    return new Date() >= date;

}

export function compareTimeStrings(start, end) {

    if (start === "" || end === "") {
        return false;
    }

    let startTime = new Date(1,1,1, start.split(":")[0],start.split(":")[1]);
    let endTime = new Date(1,1,1, end.split(":")[0],end.split(":")[1]);

    return startTime >= endTime;

}