package model.schedule;

import model.user.User;

import java.sql.Timestamp;

/*CREATE TABLE schedule_record (
    schedule_id varchar2(10),
    schedule_time TIMESTAMP,
    event varchar2(50),
    user_id varchar2(10),
    PRIMARY KEY (schedule_id),
    FOREIGN KEY (user_id) REFERENCES user_info ON DELETE CASCADE);*/

public class ScheduleRecord {
    private final String scheduleID;
    private final Timestamp time;
    private final String event;
    private final User user;

    public ScheduleRecord(String scheduleID, Timestamp time, String event, User user) {
        this.scheduleID = scheduleID;
        this.time = time;
        this.event = event;
        this.user = user;
    }

    public String getEvent() {
        return event;
    }

    public String getScheduleID() {
        return scheduleID;
    }

    public User getUser() {
        return user;
    }

    public Timestamp getTime() {
        return time;
    }
}

