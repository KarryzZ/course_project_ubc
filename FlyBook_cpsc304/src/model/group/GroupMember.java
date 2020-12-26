package model.group;

import model.user.User;

import java.sql.Timestamp;

/*CREATE TABLE group_joins (
    join_time TIMESTAMP,
    user_id varchar2(10),
    group_id varchar2(10),
    nickname varchar2(20),
    PRIMARY KEY (user_id, group_id),
    FOREIGN KEY (user_id) REFERENCES user_info ON DELETE CASCADE,
    FOREIGN KEY (group_id) REFERENCES group_record ON DELETE CASCADE)*/

public class GroupMember {
    private final Timestamp joinTime;
    private final User user;
    private final Group group;
    private final String nickname;

    public GroupMember(Timestamp joinTime, User user, Group group, String nickname) {
        this.joinTime = joinTime;
        this.user = user;
        this.group = group;
        this.nickname = nickname;
    }

    public Timestamp getJoinTime() {
        return joinTime;
    }

    public User getUser() {
        return user;
    }

    public Group getGroup() { return group; }

    public String getNickname() {
        return nickname;
    }
}
