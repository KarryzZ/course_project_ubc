package ui.meeting;

import model.group.Group;
import model.meeting.MeetingRecord;
import ui.UI;
import ui.utilities.SuccessMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

public class SubMeetingListPanel extends JPanel implements ActionListener {
    private final UI ui;
    private final MeetingRecord meeting;
    private final Group mostAttendedGroup;

    public SubMeetingListPanel(UI ui, MeetingRecord meeting, Group group) {
        this.ui = ui;
        this.meeting = meeting;
        this.mostAttendedGroup = group;

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);

        JLabel topic = ui.generateText(meeting.getTopic());
        topic.setFont(new Font("Avenir", Font.BOLD, 16));
        info.add(topic);
        topic.setAlignmentX(LEFT_ALIGNMENT);

        JLabel groupInfo = getLabelWithStar(meeting);
        info.add(groupInfo);
        groupInfo.setAlignmentX(LEFT_ALIGNMENT);

        if (meeting.getAttendance() != 0) {
            JLabel attendance = generateText("Attendance: " + meeting.getAttendance());
            info.add(attendance);
            attendance.setAlignmentX(LEFT_ALIGNMENT);
        }

        String startTime = new SimpleDateFormat("yyyy.MM.dd hh:mm").format(meeting.getStartTime());
        JLabel start = generateText("Start Time: " + startTime);
        info.add(start);
        start.setAlignmentX(LEFT_ALIGNMENT);

        if (meeting.getEndTime() != null) {
            String endTime = new SimpleDateFormat("yyyy.MM.dd hh:mm").format(meeting.getEndTime());
            JLabel end = generateText("End Time: " + endTime);
            info.add(end);
            end.setAlignmentX(LEFT_ALIGNMENT);
        }

        add(info);
        add(Box.createHorizontalGlue());

        JPanel button = new JPanel();
        button.setLayout(new BoxLayout(button,BoxLayout.Y_AXIS));
        button.setOpaque(false);
        if (meeting.getAttendance() == 0) {
            JButton join = new JButton("Join Meeting");
            resizeButton(join);
            join.addActionListener(this);
            join.setActionCommand("Join");
            button.add(join);
            join.setAlignmentY(TOP_ALIGNMENT);

            if (ui.getApplication().isAdmin(meeting.getGroup().getGroupID())) {
                JButton end = new JButton("End Meeting");
                resizeButton(end);
                end.addActionListener(this);
                end.setActionCommand("End");
                button.add(end);
                end.setAlignmentY(TOP_ALIGNMENT);
            }
        }
        add(button);
    }

    private void resizeButton(JButton b) {
        b.setFont(new Font("Avenir", Font.BOLD, 12));
        b.setForeground(Color.DARK_GRAY);
        b.setMinimumSize(new Dimension(100,30));
        b.setPreferredSize(new Dimension(100,30));
        b.setMaximumSize(new Dimension(100,30));
    }

    private JLabel generateText(String s) {
        JLabel text = new JLabel(s);
        text.setFont(new Font("Avenir", Font.BOLD, 12));
        text.setForeground(Color.GRAY);
        return text;
    }

    private JLabel getLabelWithStar(MeetingRecord meeting) {
        String name = meeting.getGroup().getName();
        JLabel groupName =  ui.generateLabel("Group: " + name);
        if (mostAttendedGroup.getName().equals(name)) {
            groupName  = new JLabel("Group: " + name + "[*]");
        }
        return groupName;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Join")) {
            ui.getApplication().joinMeeting(meeting);
            new SuccessMessage("You just joined the meeting!");
        } else {
            ui.getApplication().endMeeting(meeting);
            new SuccessMessage("The meeting is ended.");
            ui.switchPanel("Meeting");
        }
    }
}
