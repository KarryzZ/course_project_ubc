import {IScheduler, SchedRoom, SchedSection, TimeSlot} from "./IScheduler";
import {sortSections, sortRooms, checkCapacity, getIndex, getCourse} from "./SchedulerHelper";


export default class Scheduler implements IScheduler {
    public schedule(sections: SchedSection[], rooms: SchedRoom[]): Array<[SchedRoom, SchedSection, TimeSlot]> {
        // set up:
        let result: Array<[SchedRoom, SchedSection, TimeSlot]> = [];
        let roomRecords: Map<SchedRoom, any[]> = new Map<SchedRoom, any[]>();
        let courseRecords: Map<string, Set<TimeSlot>> = new Map<string, Set<TimeSlot>>();
        let sortedSections: any[] = sortSections(sections);
        let sortedRooms: any[] = sortRooms(rooms);
        sortedRooms.forEach((room) => {
            let timeslots: any[] = ["MWF 0800-0900", "MWF 0900-1000", "MWF 1000-1100", "MWF 1100-1200", "MWF 1200-1300",
                "MWF 1300-1400", "MWF 1400-1500", "MWF 1500-1600", "MWF 1600-1700", "TR  0800-0930", "TR  0930-1100"
                , "TR  1100-1230", "TR  1230-1400", "TR  1400-1530", "TR  1530-1700"];
            roomRecords.set(room, timeslots);
        });
        sortedSections.forEach((section) => {
            let coursename = getCourse(section);
            let timeslotset = Scheduler.initializeSet();
            courseRecords.set(coursename, timeslotset);
        });
        // the smallest section exceed the biggest room capacity
        if (!checkCapacity(sortedSections[sortedSections.length - 1], sortedRooms[0])) {
            return result;
        }
        let index: number = getIndex(sortedSections, rooms[0]);
        // // when there's only <15 possible sections. we could just use one room
        // if (sortedSections.length < index + 15) {
        //     for (let i: number = index; i < sortedSections.length; i++) {
        //         result.push([rooms[0], sortedSections[i], timeslots[i]]);
        //     }
        //     return result;
        // }
        let toadd: any[] = [];
        sortedRooms.forEach((room) => {
            while (toadd.length !== 0) {
                sortedSections.unshift(toadd.pop());
            }
            // get the first index
            while (sortedSections.length !== 0) {
                let section = sortedSections.shift();
                if (checkCapacity(section, room)) {
                    let courseTimeslots = courseRecords.get(getCourse(section));
                    let roomTimeslots = roomRecords.get(room);

                    // this means the current room is full
                    if (roomTimeslots.length === 0) {
                        break;
                    }
                    if (courseTimeslots.size !== 0) {
                        let timeslot = roomTimeslots.shift();
                        if (courseTimeslots.has(timeslot)) {
                            courseTimeslots.delete(timeslot);
                            result.push([room, section, timeslot]);
                        } else {
                            toadd.push(section);
                        }
                    }
                }
            }
        });
        return result;
    }


    private static initializeSet(): Set<TimeSlot> {
        let timeslot: Set<TimeSlot> = new Set<TimeSlot>();
        timeslot.add("MWF 0800-0900");
        timeslot.add("MWF 0900-1000");
        timeslot.add("MWF 1000-1100");
        timeslot.add("MWF 1100-1200");
        timeslot.add("MWF 1200-1300");
        timeslot.add("MWF 1300-1400");
        timeslot.add("MWF 1400-1500");
        timeslot.add("MWF 1500-1600");
        timeslot.add("MWF 1600-1700");
        timeslot.add("TR  0800-0930");
        timeslot.add("TR  0930-1100");
        timeslot.add("TR  1100-1230");
        timeslot.add("TR  1230-1400");
        timeslot.add("TR  1400-1530");
        timeslot.add("TR  1530-1700");
        return timeslot;
    }
}

