import {IScheduler, SchedRoom, SchedSection, TimeSlot} from "./IScheduler";

export function getEnrollmentSize(section: SchedSection) {
    return section.courses_pass + section.courses_fail + section.courses_audit;
}

export function getCourse(section: SchedSection) {
    return section.courses_dept + section.courses_id;
}

export function getRoomSeats(room: SchedRoom) {
    return room.rooms_seats;
}

// EFFECT: sort the sections in the decreasing of enrollment size
export function sortSections(sections: SchedSection[]) {
    return sortInDecreasingOrder(sections, getEnrollmentSize);
}

export function sortRooms(rooms: SchedRoom[]): SchedRoom[] {
    return sortInDecreasingOrder(rooms, getRoomSeats);
}

// general sort helper for rooms and sections
function sortInDecreasingOrder(unsorted: any[], callback: any) {
    return unsorted.sort((s1, s2) => {
        if (callback(s1) < callback(s2)) {
            return 1;
        } else if (callback(s1) > callback(s2)) {
            return -1;
        } else {
            return 0;
        }
    });
}

export function getIndex (sections: SchedSection[], room: SchedRoom) {
    for (let index: number = 0; index < sections.length; index++) {
        if (checkCapacity(sections[index], room)) {
            return index;
        }
    }
    return -1;
}

export function checkCapacity(section: SchedSection, room: SchedRoom) {
    if (section) {
        return getEnrollmentSize(section) <= getRoomSeats(room);
    } else {
        return true;
    }
}

// REFERENCE (given from 310 web page): https://www.movable-type.co.uk/scripts/latlong.html
export function getDistance (room1: SchedRoom, room2: SchedRoom) {
    let lat1 = room1.rooms_lat;
    let lat2 = room2.rooms_lat;
    let lon1 = room1.rooms_lon;
    let lon2 = room2.rooms_lon;

    const R = 6371e3; // metres
    const φ1 = lat1 * Math.PI / 180; // φ, λ in radians
    const φ2 = lat2 * Math.PI / 180;
    const Δφ = (lat2 - lat1) * Math.PI / 180;
    const Δλ = (lon2 - lon1) * Math.PI / 180;

    const a = Math.sin(Δφ / 2) * Math.sin(Δφ / 2) +
        Math.cos(φ1) * Math.cos(φ2) *
        Math.sin(Δλ / 2) * Math.sin(Δλ / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return R * c; // in metres
}


