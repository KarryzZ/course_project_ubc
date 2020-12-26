package main;

import database.ExecuteQueries;
import database.DatabaseConnection;
import model.group.Group;
import model.group.GroupChat;
import model.group.GroupMember;
import model.meeting.MeetingRecord;
import model.post.Media;
import model.post.SharePost;
import model.schedule.ScheduleRecord;
import model.schedule.Task;
import model.user.IndividualChat;
import model.user.TimeZone;
import model.user.User;
import ui.utilities.ErrorMessage;
import ui.utilities.SuccessMessage;
import ui.UI;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Application {
    private final DatabaseConnection dbConnection;
    private UI ui;
    private String currentUser; // user ID of the current user
    private ExecuteQueries queries;

    public Application() {
        dbConnection = new DatabaseConnection();
    }

    public static void main(String[] args) {
        Application app = new Application();
        app.oracleLogin();
        app.start();
    }

    public String getCurrentUserID() {
        return currentUser;
    }

    // Start the program by opening the application UI frame
    private void start() {
        ui = new UI(this);
        ui.showFrame();
    }

    // Login into the Oracle server
    private void oracleLogin() {
        boolean connect = dbConnection.login();

        if (connect) {
            dbConnection.dropTables(); // drop all tables
            dbConnection.databaseSetUp(); // setup database
            dbConnection.loadData(); // load pre-set data
            queries = dbConnection.executeQueries(); // initiate ExecuteQueries
        } else {
            System.out.println("Login failed.");
        }
    }


    /**
     * Queries related to Users
     * */

    public void userLogin(String userid, String password) {
        if (queries.containLoginInfo(userid, password)) {
            User user = queries.getUserByID(userid);
            new SuccessMessage("Welcome, <" + user.getName() + ">!");
            currentUser = userid;
            ui.switchPanel("Main");
        } else {
            new ErrorMessage("User ID or password is incorrect.");
        }
    }

    public void userRegister(String userid, String password, String name, String city, String email) {
        if (queries.userExist(userid)) {
            new ErrorMessage("This user ID has been used.");
        } else {
            TimeZone timezone = queries.getTimeZoneByCity(city);
            User user = new User(userid, password, name, timezone, email);
            dbConnection.insertUser(user);
            new SuccessMessage("You registered an account successfully!");
            ui.switchPanel("Login");
        }
    }

    public User getCurrentUser() { return queries.getUserByID(currentUser); }

    public User getUserByID(String uid) { return queries.getUserByID(uid); }

    public User[] getUserList() {
        return queries.getUser();
    }

    public String[] getCities() { return queries.getTimeZoneCities(); }

    public void resetPassword(String password) {
        queries.updatePassword(currentUser, password);
        new SuccessMessage("You reset your password successfully!");
    }

    public void deleteAccount() {
        queries.updateGroupCreator(currentUser);
        Group[] groups = queries.getGroupsByAdmin(currentUser);
        for (Group group : groups) {
            joinGroup(new GroupMember(
                    new Timestamp(System.currentTimeMillis()),
                    getUserByID("0000"),
                    group,
                    null
            ));
            queries.updateAdmin("0000", group, currentUser);
        }
        queries.deleteAccount(currentUser);
        new SuccessMessage("The account is deleted. You will return back to the log in menu.");
    }

    public void changeCity(String city) {
        queries.updateUserCity(currentUser, city);
    }

    public void changeName(String name) {
        queries.updateUserName(currentUser, name);
    }

    public void changeEmail(String email) {
        queries.updateUserEmail(currentUser, email);
    }

    public IndividualChat[] getIndividualChatHistory(String uid1, String uid2) {
        return queries.getIndividualChatHistory(uid1, uid2);
    }

    public void addIndividualChat(IndividualChat record) {
        dbConnection.insertIndividualChat(record);
    }


    /**
     * Queries related to Posts and Media
     * */

    public SharePost[] getIndividualPost(String uid) {
        return queries.getIndividualPost(uid);
    }

    public SharePost[] getPosts() {
        return queries.getPosts();
    }

    public SharePost getPostByID(String pid) {
        return queries.getPostByID(pid);
    }

    public void deletePost(SharePost post) {
        queries.deletePost(post);
    }

    public Media getMediaByUrl(String url) {
        return queries.getMediaByUrl(url);
    }

    public void addPost(SharePost post) {
        dbConnection.insertSharePost(post);
    }

    public void addMedia(Media media) {
        dbConnection.insertMedia(media);
    }


    /**
     * Queries related to Meetings
     * */

    public void joinMeeting(MeetingRecord meeting) {
        if (!(queries.hasJoined(meeting.getMeetingID(), currentUser))) {
            dbConnection.insertMeetingJoins(meeting, getCurrentUser());
        }
    }

    public void endMeeting(MeetingRecord meeting) {
        int attendance = queries.countAttendance(meeting.getMeetingID());
        queries.updateMeetingInfo(new Timestamp(System.currentTimeMillis()), attendance, meeting.getMeetingID());
    }

    public int countMeetings() {
        return queries.countMeetings();
    }

    public void addMeeting(MeetingRecord meeting) {
        dbConnection.insertMeetingRecord(meeting);
    }

    public MeetingRecord[] getPastMeetingsByID() {
        return queries.getPastMeetingsByID(currentUser);
    }

    public MeetingRecord[] getCurrentMeetingsByID() {
        return queries.getCurrentMeetingsByID(currentUser);
    }


    /**
     * Queries related to Schedules and Tasks
     * */

    public ScheduleRecord[] getSchedulesByID() {
        return queries.getSchedulesByID(currentUser);
    }

    public ScheduleRecord[] getSchedulesThisWeek() {
        Calendar cal = Calendar.getInstance();
        String today = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()) + " 00:00:00";
        cal.add(Calendar.DATE, 7);
        String after = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()) + " 00:00:00";
        return queries.getSchedulesWithinPeriod(Timestamp.valueOf(today), Timestamp.valueOf(after), currentUser);
    }

    public ScheduleRecord[] getSchedulesToday() {
        Calendar cal = Calendar.getInstance();
        String today = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()) + " 00:00:00";
        cal.add(Calendar.DATE, 1);
        String tomorrow = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()) + " 00:00:00";
        return queries.getSchedulesWithinPeriod(Timestamp.valueOf(today), Timestamp.valueOf(tomorrow), currentUser);
    }

    public ScheduleRecord[] getSchedulesPassed() {
        Calendar cal = Calendar.getInstance();
        String today = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()) + " 00:00:00";
        return queries.getSchedulesPassed(Timestamp.valueOf(today), currentUser);
    }

    public Task[] getTasksBySchedule(ScheduleRecord schedule) {
        return queries.getTasksBySchedule(schedule.getScheduleID());
    }

    public void updateTaskStatus(Task task, int i) {
        queries.updateTaskStatus(task, i);
    }

    public int countSchedules() {
        return queries.countSchedules();
    }

    public void addSchedule(ScheduleRecord schedule) {
        dbConnection.insertScheduleRecord(schedule);
    }

    public void deleteTask(Task task) {
        queries.deleteTask(task);
    }

    public void addTask(Task task) {
        dbConnection.insertContainTask(task);
    }

    public String[] todoToday() {
        Calendar cal = Calendar.getInstance();
        String today = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()) + " 00:00:00";
        cal.add(Calendar.DATE, 1);
        String tomorrow = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()) + " 00:00:00";
        return queries.getTaskInfoToday(Timestamp.valueOf(today), Timestamp.valueOf(tomorrow));
    }

    public void deleteSchedule(ScheduleRecord schedule) {
        queries.deleteSchedule(schedule);
    }


    /**
     * Queries related to Groups
     * */

    public Group[] getGroups() {
    return queries.getGroups();
}

    public Group getGroupByID(String gid) {
        return queries.getGroupByID(gid);
    }

    public int countGroups() {
        return queries.countGroups();
    }

    public void addGroup(Group group) {
        dbConnection.insertGroupRecord(group);
    }

    public void updateGroupName(String gid, String name) {
        queries.updateGroupName(gid, name);
    }

    public void updateNickname(String gid, String name) {
        queries.updateNickname(gid, currentUser, name);
    }

    public Group[] getCurrentUsersGroups() {
        return queries.getGroupsByUser(currentUser);
    }

    public boolean isAdmin(String gid) {
        return queries.isAdmin(currentUser, gid);
    }

    public boolean isMember(String gid) {
        GroupMember[] members = queries.getGroupMembers(gid);
        for (GroupMember member : members) {
            if (member.getUser().getUserID().equals(currentUser)) {
                return true;
            }
        }
        return false;
    }

    public GroupChat[] getGroupChatHistory(String gid) {
        return queries.getGroupChatHistory(gid);
    }

    public GroupMember getGroupMemberByID(String uid, String gid) {
        return queries.getGroupMemberByID(uid, gid);
    }

    public void addGroupChat(GroupChat record) {
        dbConnection.insertGroupChat(record);
    }

    public void joinGroup(GroupMember member) {
        dbConnection.insertGroupJoins(member);
    }

    public void becomeAdmin(User currentUser, Group group) {
        dbConnection.insertGroupAdmin(currentUser, group);
    }

    public Group[] getAdminGroups() {
        return queries.getAdminGroups(currentUser);
    }

    public GroupMember[] getAdminByGroup(String gid) {
        return queries.getAdminByGroup(gid);
    }

    public GroupMember[] getMembersByGroup(String gid) {
        return queries.getMembersByGroup(gid);
    }

    public GroupMember[] getActiveMembers(Group group) {
        return queries.getMostActiveMember(group);
    }

    public GroupMember[] getHardworkingMembers(Group group) {
        return queries.getHardworkingMember(group);
    }

    public Group getGroupWithMostMeetingByID() {
        return queries.getGroupWithMostMeetings(currentUser);
    }

    public void leaveGroup(Group group) {
        if (isAdmin(group.getGroupID())) {
            queries.updateGroupCreator(currentUser);
            queries.updateAdmin("0000", group, currentUser);
            joinGroup(new GroupMember(
                    new Timestamp(System.currentTimeMillis()),
                    getUserByID("0000"),
                    group,
                    null
            ));
        }
        queries.deleteGroupChat(currentUser, group.getGroupID());
        queries.leaveGroup(currentUser, group.getGroupID());
    }

    public void deleteGroup(Group group) {
        queries.deleteGroup(group.getGroupID());
    }
}
