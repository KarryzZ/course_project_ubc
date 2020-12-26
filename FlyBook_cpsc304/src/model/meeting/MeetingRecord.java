package model.meeting;

import model.group.Group;

import java.sql.Timestamp;

/*CREATE TABLE meeting_record (
    meeting_id varchar2(10),
    attendance INTEGER,
    topic varchar2(50),
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    group_id varchar2(10) NOT NULL,
    PRIMARY KEY (meeting_id),
    FOREIGN KEY (group_id) REFERENCES group_record ON DELETE CASCADE);*/

public class MeetingRecord {
    private final String meetingID;
    private final int attendance;
    private final String topic;
    private final Timestamp startTime;
    private final Timestamp endTime;
    private final Group group;

    public MeetingRecord (String meetingID, int attendance, String topic, Timestamp startTime, Timestamp endTime, Group group) {
        this.meetingID = meetingID;
        this.attendance = attendance;
        this.topic = topic;
        this.startTime = startTime;
        this.endTime = endTime;
        this.group = group;
    }

    public String getMeetingID() {
        return meetingID;
    }

    public int getAttendance() {
        return attendance;
    }

    public String getTopic() {
        return topic;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public Group getGroup() {
        return group;
    }
}

