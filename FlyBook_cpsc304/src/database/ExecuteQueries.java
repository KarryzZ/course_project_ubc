package database;

import model.user.IndividualChat;
import model.group.Group;
import model.group.GroupChat;
import model.group.GroupMember;
import model.meeting.MeetingRecord;
import model.post.Media;
import model.post.SharePost;
import model.schedule.ScheduleRecord;
import model.schedule.Task;
import model.user.TimeZone;
import model.user.User;

import java.sql.*;
import java.util.ArrayList;

public class ExecuteQueries {
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private final Connection connection;

    public ExecuteQueries(Connection connection) {
        this.connection = connection;
    }

    private void rollbackConnection() {
        try  {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }


    /*
     * ==================================================================
     *
     * Below are the methods used to generate queries or projections that
     * the application needs
     *
     * ==================================================================
     * */

    //Return all cities listed in time_zone
    public String[] getTimeZoneCities() {
        ArrayList<String> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT city FROM time_zone");

            while(rs.next()) {
                result.add(rs.getString("city"));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new String[result.size()]);
    }

    // return timezone given city name
    public TimeZone getTimeZoneByCity(String city) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM time_zone WHERE city = \'" + city + "\'");

            while(rs.next()) {
                return new TimeZone(rs.getString("city"), rs.getString("time_zone"));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return null;
    }

    // Get all users' info combined with time_zone info
    public User[] getUser() {
        ArrayList<User> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM user_info, " +
                    "time_zone WHERE user_info.city = time_zone.city ORDER BY user_id");

            while(rs.next()) {
                TimeZone timezone = new TimeZone(rs.getString("city"),rs.getString("time_zone"));
                User user = new User(rs.getString("user_id"),
                        rs.getString("password"),
                        rs.getString("name"),
                        timezone,
                        rs.getString("email"));
                result.add(user);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new User[result.size()]);
    }

    // Get a user's info by user ID
    public User getUserByID(String userid) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM user_info, time_zone " +
                    "WHERE user_id = \'" + userid + "\' AND user_info.city = time_zone.city");

            while(rs.next()) {
                TimeZone timezone = new TimeZone(rs.getString("city"),rs.getString("time_zone"));
                return new User(rs.getString("user_id"),
                        rs.getString("password"),
                        rs.getString("name"),
                        timezone,
                        rs.getString("email"));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return null;
    }

    // Check if the userid and password match what have been stored when login
    public boolean containLoginInfo(String userid, String password) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select user_id, password from user_info");

            while(rs.next()) {
                if (userid.equals(rs.getString("user_id"))) {
                    if (password.equals(rs.getString("password"))) {
                        return true;
                    }
                }
            }

            rs.close();
            stmt.close();

            return false;
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return false;
        }
    }

    // Check if a user exists
    public boolean userExist(String userid) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select user_id from user_info");

            while(rs.next()) {
                if (userid.equals(rs.getString("user_id"))) {
                    return true;
                }
            }

            rs.close();
            stmt.close();

            return false;
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return false;
        }
    }

    public void updatePassword(String userid, String password) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE user_info SET password = ? WHERE user_id = ?");
            ps.setString(1, password);
            ps.setString(2, userid);

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updateUserName(String uid, String input) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE user_info SET name = ? WHERE user_id = ?");
            ps.setString(1, input);
            ps.setString(2, uid);

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updateUserCity(String uid, String input) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE user_info SET city = ? WHERE user_id = ?");
            ps.setString(1, input);
            ps.setString(2, uid);

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updateUserEmail(String uid, String input) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE user_info SET email = ? WHERE user_id = ?");
            ps.setString(1, input);
            ps.setString(2, uid);

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    // Delete a user
    public void deleteAccount(String userid) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM user_info WHERE user_id = ?");
            ps.setString(1, userid);

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    // Get all the individual chat records between two users
    public IndividualChat[] getIndividualChatHistory(String uid1, String uid2) {
        ArrayList<IndividualChat> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM individual_chat, user_info " +
                    "WHERE ((sender = \'" + uid1 + "\' AND receiver = \'" + uid2 + "\') " +
                    "OR (sender = \'" + uid2 + "\' AND receiver = \'" + uid1 + "\')) " +
                    "AND (sender = user_id) ORDER BY time");

            while(rs.next()) {
                IndividualChat chat = new IndividualChat(
                        getUserByID(rs.getString("sender")),
                        getUserByID(rs.getString("receiver")),
                        rs.getString("content"),
                        Timestamp.valueOf(rs.getString("time")));
                result.add(chat);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new IndividualChat[result.size()]);
    }

    // Get all the posts posted by a user
    public SharePost[] getIndividualPost(String uid) {
        ArrayList<SharePost> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM share_post " +
                    "WHERE user_id = \'" + uid + "\' ORDER BY time");

            while(rs.next()) {
                SharePost post = new SharePost(
                        rs.getString("post_id"),
                        Timestamp.valueOf(rs.getString("time")),
                        rs.getString("content"),
                        getUserByID(rs.getString("user_id")),
                        getMediaByUrl(rs.getString("media_url")));
                result.add(post);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Debug: getIndividualPost()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new SharePost[result.size()]);
    }

    // Get all the posts in share_post table
    public SharePost[] getPosts() {
        ArrayList<SharePost> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM share_post ");

            while(rs.next()) {
                SharePost post = new SharePost(
                        rs.getString("post_id"),
                        Timestamp.valueOf(rs.getString("time")),
                        rs.getString("content"),
                        getUserByID(rs.getString("user_id")),
                        getMediaByUrl(rs.getString("media_url")));
                result.add(post);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new SharePost[result.size()]);
    }

    // Get a post by post_id
    public SharePost getPostByID(String post_id) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM share_post " +
                    "WHERE post_id = \'" + post_id + "\'");

            while(rs.next()) {
                SharePost post = new SharePost(
                        rs.getString("post_id"),
                        Timestamp.valueOf(rs.getString("time")),
                        rs.getString("content"),
                        getUserByID(rs.getString("user_id")),
                        getMediaByUrl(rs.getString("media_url")));
                return post;
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return null;
    }

    // Delete a post
    public void deletePost(SharePost post) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM share_post WHERE post_id = ?");
            ps.setString(1, post.getPostID());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    // Get all the media
    public Media[] getMedia() {
        ArrayList<Media> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM media ");

            while(rs.next()) {
                Media media = new Media(
                        rs.getString("media_type"),
                        rs.getString("url"));
                result.add(media);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new Media[result.size()]);
    }

    // Get a media given its url
    public Media getMediaByUrl(String url) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM media " +
                    "WHERE url = \'" + url + "\'");

            while(rs.next()) {
                Media media = new Media(
                        rs.getString("media_type"),
                        rs.getString("url"));
                return media;
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return null;
    }

    // get all the groups
    public Group[] getGroups() {
        ArrayList<Group> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT group_id FROM group_record");

            while(rs.next()) {
                result.add(getGroupByID(rs.getString("group_id")));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Debug: getGroups()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new Group[result.size()]);
    }

    // get all the groups that a user joined
    public Group[] getGroupsByUser(String userid) {
        ArrayList<Group> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT group_id FROM group_joins " +
                    "WHERE user_id = \'" + userid + "\'");

            while(rs.next()) {
                result.add(getGroupByID(rs.getString("group_id")));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Debug: getGroupsByUser()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new Group[result.size()]);
    }

    // get a group given the group ID
    public Group getGroupByID(String gid) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM group_record, group_creates " +
                    "WHERE group_record.group_id = \'" + gid + "\'");

            while(rs.next()) {
                Group group = new Group(
                        gid,
                        Timestamp.valueOf(rs.getString("create_time")),
                        rs.getString("group_name"),
                        getUserByID(rs.getString("user_id")));
                return group;
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Debug: getGroupByID()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return null;
    }

    public void updateGroupName(String gid, String name) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE group_record SET group_name = ? WHERE group_id = ?");
            ps.setString(1, name);
            ps.setString(2, gid);

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println("Debug: updateGroupName()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    // get all the group members in the group given the group ID
    public GroupMember[] getGroupMembers(String gid) {
        ArrayList<GroupMember> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM group_joins " +
                    "WHERE group_id = \'" + gid + "\'");

            while(rs.next()) {
                GroupMember member = new GroupMember(
                        Timestamp.valueOf(rs.getString("join_time")),
                        getUserByID(rs.getString("user_id")),
                        getGroupByID(rs.getString("group_id")),
                        rs.getString("nickname")
                );
                result.add(member);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Debug: getGroupMembers()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new GroupMember[result.size()]);
    }

    // Get a group member from a group given the group ID and the user ID
    public GroupMember getGroupMemberByID(String uid, String gid) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM group_joins " +
                    "WHERE group_id = \'" + gid + "\' " +
                    "AND user_id = \'" + uid + "\'");

            while(rs.next()) {
                GroupMember member = new GroupMember(
                        Timestamp.valueOf(rs.getString("join_time")),
                        getUserByID(rs.getString("user_id")),
                        getGroupByID(rs.getString("group_id")),
                        rs.getString("nickname"));
                return member;
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Debug: getGroupMemberByID()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return null;
    }

    public void updateNickname(String gid, String uid, String name) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE group_joins SET nickname = ? WHERE group_id = ? AND user_id = ?");
            ps.setString(1, name);
            ps.setString(2, gid);
            ps.setString(3, uid);

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println("Debug: updateNickname()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    // check if a user is the admin of the group
    public boolean isAdmin(String uid, String gid) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT group_id FROM group_admin " +
                    "WHERE user_id = \'" + uid + "\'" +
                    "AND group_id = \'" + gid + "\'");

            while(rs.next()) {
                return true;
            }

            rs.close();
            stmt.close();

            return false;
        } catch (SQLException e) {
            System.out.println("Debug: isAdmin()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return false;
        }
    }

    // Get all the groups which the user is an admin
    public Group[] getAdminGroups(String uid) {
        ArrayList<Group> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM group_admin " +
                    "WHERE user_id = \'" + uid + "\'");

            while(rs.next()) {
                result.add(getGroupByID(rs.getString("group_id")));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Debug: getAdminGroups()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new Group[result.size()]);
    }

    // get all the chat records in the group given the group ID
    public GroupChat[] getGroupChatHistory(String gid) {
        ArrayList<GroupChat> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM group_chat " +
                    "WHERE group_id = \'" + gid + "\'");

            while(rs.next()) {
                GroupChat chat = new GroupChat(
                        Timestamp.valueOf(rs.getString("time")),
                        getGroupMemberByID(rs.getString("user_id"), rs.getString("group_id")),
                        rs.getString("content"),
                        getGroupByID(rs.getString("group_id"))
                );
                result.add(chat);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Debug: getGroupChatHistory()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new GroupChat[result.size()]);
    }

    // Get all the past meetings the user has joined
    public MeetingRecord[] getPastMeetingsByID(String uid) {
        ArrayList<MeetingRecord> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM meeting_record, meeting_joins " +
                    "WHERE user_id = \'" + uid + "\'" +
                    "AND attendance > 0" +
                    "AND meeting_record.meeting_id = meeting_joins.meeting_id");

            while(rs.next()) {
                MeetingRecord meeting = new MeetingRecord(
                        rs.getString("meeting_id"),
                        Integer. valueOf(rs.getString("attendance")),
                        rs.getString("topic"),
                        Timestamp.valueOf(rs.getString("start_time")),
                        Timestamp.valueOf(rs.getString("end_time")),
                        getGroupByID(rs.getString("group_id"))
                );
                result.add(meeting);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Debug: getPastMeetingsByID()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new MeetingRecord[result.size()]);
    }

    // Get all the current meetings the user could join
    public MeetingRecord[] getCurrentMeetingsByID(String uid) {
        ArrayList<MeetingRecord> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM meeting_record, group_joins " +
                    "WHERE (user_id = \'" + uid + "\'" +
                    "AND attendance = 0)" +
                    "AND meeting_record.group_id = group_joins.group_id");

            while(rs.next()) {
                MeetingRecord meeting = new MeetingRecord(
                        rs.getString("meeting_id"),
                        Integer. valueOf(rs.getString("attendance")),
                        rs.getString("topic"),
                        Timestamp.valueOf(rs.getString("start_time")),
                        null,
                        getGroupByID(rs.getString("group_id"))
                );
                result.add(meeting);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Debug: getCurrentMeetingsByID()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new MeetingRecord[result.size()]);
    }

    // Check if the user has already joined the meeting
    public boolean hasJoined(String mid, String uid) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM meeting_joins " +
                    "WHERE user_id = \'" + uid + "\'" +
                    "AND meeting_id = \'" + mid + "\'");

            while(rs.next()) {
                return true;
            }

            rs.close();
            stmt.close();

            return false;
        } catch (SQLException e) {
            System.out.println("Debug: hasJoined()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return false;
        }
    }

    // Count the number of people attending the meeting
    public int countAttendance(String mid) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS \"number\" FROM meeting_joins " +
                    "WHERE meeting_id = \'" + mid + "\'");

            while(rs.next()) {
                return Integer.valueOf(rs.getString("number"));
            }

            rs.close();
            stmt.close();

            return 0;
        } catch (SQLException e) {
            System.out.println("Debug: hasJoined()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return 0;
        }
    }

    public void updateMeetingInfo(Timestamp endTime, int attendance, String mid) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE meeting_record SET attendance = ?, end_time = ? WHERE meeting_id = ?");
            ps.setTimestamp(2, endTime);
            ps.setInt(1, attendance);
            ps.setString(3, mid);

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println("Debug: updateMeetingInfo()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    // Get a meeting record given the meeting ID
    public MeetingRecord getMeetingByID(String mid) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM meeting_record " +
                    "WHERE meeting_id = \'" + mid + "\'");

            while(rs.next()) {
                MeetingRecord meeting = new MeetingRecord(
                        rs.getString("meeting_id"),
                        Integer. valueOf(rs.getString("attendance")),
                        rs.getString("topic"),
                        Timestamp.valueOf(rs.getString("start_time")),
                        Timestamp.valueOf(rs.getString("end_time")),
                        getGroupByID(rs.getString("group_id"))
                );
                return meeting;
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Debug: getPastMeetingsByID()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return null;
    }

    // Count total number of meetings
    public int countMeetings() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS \"number\" FROM meeting_record ");

            while(rs.next()) {
                return Integer.valueOf(rs.getString("number"));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Debug: countGroups()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return 0;
    }

    // Get a user's schedules within a certain time period
    public ScheduleRecord[] getSchedulesWithinPeriod(Timestamp today, Timestamp after, String uid) {
        ArrayList<ScheduleRecord> result = new ArrayList<>();

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM schedule_record " +
                    "WHERE user_id = ? AND schedule_time >= ? AND schedule_time < ?");
            ps.setString(1, uid);
            ps.setTimestamp(2, today);
            ps.setTimestamp(3, after);

            ResultSet rs = ps.executeQuery();


            while(rs.next()) {
                ScheduleRecord schedule = new ScheduleRecord(
                        rs.getString("schedule_id"),
                        Timestamp.valueOf(rs.getString("schedule_time")),
                        rs.getString("event"),
                        getUserByID(rs.getString("user_id"))
                );
                result.add(schedule);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Debug: getSchedulesThisWeek()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new ScheduleRecord[result.size()]);
    }

    // Get a user's all schedules
    public ScheduleRecord[] getSchedulesByID(String uid) {
        ArrayList<ScheduleRecord> result = new ArrayList<>();

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM schedule_record " +
                    "WHERE user_id = ?");
            ps.setString(1, uid);

            ResultSet rs = ps.executeQuery();


            while(rs.next()) {
                ScheduleRecord schedule = new ScheduleRecord(
                        rs.getString("schedule_id"),
                        Timestamp.valueOf(rs.getString("schedule_time")),
                        rs.getString("event"),
                        getUserByID(rs.getString("user_id"))
                );
                result.add(schedule);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Debug: getSchedulesByID()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new ScheduleRecord[result.size()]);
    }

    // Get a user's schedules that have passed
    public ScheduleRecord[] getSchedulesPassed(Timestamp today, String uid) {
        ArrayList<ScheduleRecord> result = new ArrayList<>();

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM schedule_record " +
                    "WHERE user_id = ? AND schedule_time < ?");
            ps.setString(1, uid);
            ps.setTimestamp(2, today);

            ResultSet rs = ps.executeQuery();


            while(rs.next()) {
                ScheduleRecord schedule = new ScheduleRecord(
                        rs.getString("schedule_id"),
                        Timestamp.valueOf(rs.getString("schedule_time")),
                        rs.getString("event"),
                        getUserByID(rs.getString("user_id"))
                );
                result.add(schedule);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Debug: getSchedulesPassed()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new ScheduleRecord[result.size()]);
    }

    // Get tasks associated with a schedule
    public Task[] getTasksBySchedule(String sid) {
        ArrayList<Task> result = new ArrayList<>();

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM contain_task " +
                    "WHERE schedule_id = ? ORDER BY priority");
            ps.setString(1, sid);

            ResultSet rs = ps.executeQuery();


            while(rs.next()) {
                Task task = new Task(
                        rs.getString("task_name"),
                        Integer.valueOf(rs.getString("priority")),
                        Integer.valueOf(rs.getString("status")),
                        getScheduleByID(rs.getString("schedule_id"))
                );
                result.add(task);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Debug: getTasksBySchedule()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new Task[result.size()]);
    }

    // Get a schedule record by its ID
    public ScheduleRecord getScheduleByID(String sid) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM schedule_record " +
                    "WHERE schedule_id = \'" + sid + "\'");

            while(rs.next()) {
                ScheduleRecord schedule = new ScheduleRecord(
                        rs.getString("schedule_id"),
                        Timestamp.valueOf(rs.getString("schedule_time")),
                        rs.getString("event"),
                        getUserByID(rs.getString("user_id"))
                );
                return schedule;
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Debug: getPastMeetingsByID()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return null;
    }

    public void updateTaskStatus(Task task, int i) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE contain_task SET status = ? WHERE schedule_id = ? AND task_name = ?");
            ps.setInt(1, i);
            ps.setString(2, task.getSchedule().getScheduleID());
            ps.setString(3, task.getTaskName());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println("Debug: updateTaskStatus()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    // Count the total number of all schedules
    public int countSchedules() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS \"number\" FROM schedule_record");

            while(rs.next()) {
                return Integer.valueOf(rs.getString("number"));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Debug: countSchedules()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return 0;
    }

    // Change the group creator to admin after the former creator deleting the account
    public void updateGroupCreator(String currentUser) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE group_creates SET user_id = \'0000\' WHERE user_id = ?");
            ps.setString(1, currentUser);

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println("Debug: updateGroupCreator()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void deleteTask(Task task) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM contain_task WHERE schedule_id = ? AND task_name = ?");
            ps.setString(1, task.getSchedule().getScheduleID());
            ps.setString(2, task.getTaskName());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println("Debug: deleteTask()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    // Get the print-out info of everyone's number of unfinished tasks today
    public String[] getTaskInfoToday(Timestamp today, Timestamp tomorrow) {
        ArrayList<String> result = new ArrayList<>();

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT user_id, COUNT(*) AS \"number\", MIN(priority) AS \"min\" " +
                    "FROM schedule_record, contain_task " +
                    "WHERE schedule_record.schedule_id = contain_task.schedule_id " +
                    "AND schedule_time >= ? AND schedule_time < ? AND status = ? " +
                    "GROUP BY user_id");
            ps.setTimestamp(1, today);
            ps.setTimestamp(2, tomorrow);
            ps.setInt(3, 0);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                String info;
                if (rs.getString("number").equals("1")) {
                    info = getUserByID(rs.getString("user_id")).getName() +
                            " has " + rs.getString("number") +
                            " task today with , the most emergent task having a priority value of " +
                            rs.getString("min") + ".";
                } else {
                    info = getUserByID(rs.getString("user_id")).getName() +
                            " has " + rs.getString("number") +
                            " tasks today with , the most emergent task having a priority value of " +
                            rs.getString("min") + ".";
                }
                result.add(info);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Debug: getTaskInfoToday()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new String[result.size()]);
    }

    public void deleteSchedule(ScheduleRecord schedule) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM schedule_record WHERE schedule_id = ?");
            ps.setString(1, schedule.getScheduleID());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println("Debug: deleteSchedule()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    // Count the total number of groups
    public int countGroups() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS \"number\" FROM group_record");

            while(rs.next()) {
                return Integer.valueOf(rs.getString("number"));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Debug: countGroups()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return 0;
    }

    public Group[] getGroupsByAdmin(String uid) {
        ArrayList<Group> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM group_admin WHERE user_id = \'" + uid + "\'");

            while(rs.next()) {
                result.add(getGroupByID(rs.getString("group_id")));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Debug: getGroupsByAdmin()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new Group[result.size()]);
    }

    public void updateAdmin(String admin, Group group, String user) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE group_admin SET user_id = ? WHERE group_id = ? AND user_id = ?");
            ps.setString(1, admin);
            ps.setString(2, group.getGroupID());
            ps.setString(3, user);

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println("Debug: updateAdmin()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    //Find the group that each user has joined with most meetings
    public Group getGroupWithMostMeetings(String uid) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT user_id, g.group_id " +
                    "FROM group_joins g, meeting_record m " +
                    "WHERE g.group_id = m.group_id " +
                    "GROUP BY user_id, g.group_id " +
                    "HAVING COUNT(meeting_id) >= ALL (" +
                    "SELECT COUNT(meeting_id) " +
                    "FROM group_joins g2, meeting_record m2 " +
                    "WHERE g2.group_id = m2.group_id " +
                    "AND g2.user_id = g.user_id " +
                    "GROUP BY g2.group_id)");

            while(rs.next()) {
                if (rs.getString("user_id").equals(uid)) {
                    return getGroupByID(rs.getString("group_id"));
                }
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Debug: getGroupWithMostMeetings()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return null;
    }

    // Find the most active user (chats the most) in a group
    public GroupMember[] getMostActiveMember(Group group) {
        ArrayList<GroupMember> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            String gid = group.getGroupID();
            String s1 = "WITH temp(user_id, total) AS (SELECT user_id, COUNT(*) AS \"total\" FROM group_chat WHERE group_id = \'"+ gid + "\' GROUP BY user_id) " +
                    "SELECT user_id FROM temp WHERE total = (SELECT MAX(total) FROM temp)";
            ResultSet rs = stmt.executeQuery(s1);

            while(rs.next()) {
                result.add(getGroupMemberByID(rs.getString("user_id"), gid));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Debug: test2()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new GroupMember[result.size()]);
    }

    // Find group member who has joined every meeting that group had
    public GroupMember[] getHardworkingMember(Group group) {
        ArrayList<GroupMember> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            String gid = group.getGroupID();
            String s1 = "WITH members(user_id) AS (SELECT user_id FROM group_joins WHERE group_id = \'" + gid + "\') ";
            String s2 = "SELECT members.user_id FROM members WHERE NOT EXISTS(";
            String s3 = "(SELECT mc.meeting_id FROM meeting_record mc WHERE mc.group_id = \'" + gid + "\') ";
            String s4 = "MINUS (SELECT mj.meeting_id FROM meeting_joins mj WHERE mj.user_id = members.user_id))";
            ResultSet rs = stmt.executeQuery(s1+s2+s3+s4);

            while(rs.next()) {
                result.add(getGroupMemberByID(rs.getString("user_id"), gid));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Debug: getHardworkingMember()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new GroupMember[result.size()]);
    }

    public GroupMember[] getAdminByGroup(String gid) {
        ArrayList<GroupMember> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM group_admin WHERE group_id = \'" + gid + "\'");

            while(rs.next()) {
                result.add(getGroupMemberByID(rs.getString("user_id"), gid));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Debug: getAdminByGroup()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new GroupMember[result.size()]);
    }

    public GroupMember[] getMembersByGroup(String gid) {
        ArrayList<GroupMember> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM group_joins WHERE group_id = \'" + gid + "\'");

            while(rs.next()) {
                if (!(isAdmin(rs.getString("user_id"), rs.getString("group_id")))) {
                    result.add(getGroupMemberByID(rs.getString("user_id"), gid));
                }
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Debug: getAdminByGroup()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new GroupMember[result.size()]);
    }

    public void leaveGroup(String uid, String gid) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM group_joins WHERE group_id = ? AND user_id = ?");
            ps.setString(1, gid);
            ps.setString(2, uid);

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println("Debug: leaveGroup()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void deleteGroupChat(String gid, String uid) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM group_chat WHERE group_id = ? AND user_id = ?");
            ps.setString(1, gid);
            ps.setString(2, uid);

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println("Debug: deleteGroupChat()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void deleteGroup(String gid) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM group_record WHERE group_id = ?");
            ps.setString(1, gid);

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println("Debug: deleteGroup()");
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }
}
