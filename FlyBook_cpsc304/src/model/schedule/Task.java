package model.schedule;

/*CREATE TABLE contain_task (
    task_name varchar2(50),
    priority INTEGER,
    status INTEGER,
    schedule_id varchar2(20),
    PRIMARY KEY (schedule_id, task),
    FOREIGN KEY (schedule_id) REFERENCES schedule_record ON DELETE CASCADE);*/

public class Task {
    private final String taskName;
    private final int priority;
    private int status;
    private final ScheduleRecord schedule;

    public Task(String taskName, int priority, int status, ScheduleRecord schedule) {
        this.taskName = taskName;
        this.priority = priority;
        this.status = status;
        this.schedule = schedule;
    }

    public void setStatus(int i) {
        this.status = i;
    }

    public String getTaskName() {
        return taskName;
    }

    public int getPriority() {
        return priority;
    }

    public int getStatus() { return status; }

    public ScheduleRecord getSchedule() { return schedule; }
}

