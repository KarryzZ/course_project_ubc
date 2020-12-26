package ui.meeting;

import main.Application;
import model.group.Group;
import model.meeting.MeetingRecord;
import ui.UI;
import ui.utilities.Header;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class MeetingListPanel extends JPanel implements ActionListener {
    UI ui;

    public MeetingListPanel(UI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());

        add(new Header(ui, "Meeting"), BorderLayout.PAGE_START);

        JTabbedPane meetingList = meetingList();
        add(meetingList, BorderLayout.CENTER);

        // a sub-panel for submission
        JPanel button = new JPanel();
        button.setLayout(new FlowLayout());
        button.setBackground(new Color(15, 85, 130));
        button.setMaximumSize(new Dimension(430, 40));
        button.setMinimumSize(new Dimension(430, 40));
        JButton newSchedule = ui.generateChangedButton("new");
        newSchedule.addActionListener(this);
        button.add(newSchedule);
        button.setAlignmentY(BOTTOM_ALIGNMENT);

        add(button, BorderLayout.PAGE_END);
    }

    private JTabbedPane meetingList() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setOpaque(false);

        JScrollPane current = currentMeetings();
        current.setOpaque(false);
        tabbedPane.addTab("Current meetings", current);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        JScrollPane past = pastMeetings();
        past.setOpaque(false);
        tabbedPane.addTab("Past meetings", past);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        return tabbedPane;
    }

    private JScrollPane pastMeetings() {
        JPanel panel = new JPanel();
        JScrollPane pastMeetings = new JScrollPane(panel);
        panel.setOpaque(false);
        pastMeetings.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        Application app = ui.getApplication();
        MeetingRecord[] meetings = app.getPastMeetingsByID();
        Group mostgroup = app.getGroupWithMostMeetingByID();

        if (meetings.length == 0) {
            JLabel title = ui.generateLabel("There is no past meeting.");
            title.setAlignmentX(CENTER_ALIGNMENT);
            panel.add(title);
        } else {
            for (int i = meetings.length - 1; i >= 0; i--) {
                JPanel meeting = new SubMeetingListPanel(ui, meetings[i], mostgroup);
                panel.add(meeting);
                panel.add(Box.createRigidArea(new Dimension(0, 20)));

                meeting.setAlignmentX(LEFT_ALIGNMENT);
                meeting.setAlignmentY(TOP_ALIGNMENT);
            }
        }

        return pastMeetings;
    }

    private JScrollPane currentMeetings() {
        JPanel panel = new JPanel();
        JScrollPane currentMeetings = new JScrollPane(panel);
        panel.setOpaque(false);
        currentMeetings.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        Application app = ui.getApplication();
        MeetingRecord[] meetings = app.getCurrentMeetingsByID();
        Group mostgroup = app.getGroupWithMostMeetingByID();

        if (meetings.length == 0) {
            JLabel title = ui.generateLabel("There is no current meeting.");
            title.setAlignmentX(CENTER_ALIGNMENT);
            panel.add(title);
        } else {
            for (int i = meetings.length - 1; i >= 0; i--) {
                JPanel meeting = new SubMeetingListPanel(ui, meetings[i], mostgroup);
                panel.add(meeting);
                meeting.add(Box.createRigidArea(new Dimension(0, 20)));

                meeting.setAlignmentX(LEFT_ALIGNMENT);
                meeting.setAlignmentY(TOP_ALIGNMENT);
            }
        }

        return currentMeetings;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ui.setContentPane(new NewMeetingPanel(ui));
        ui.revalidate();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ui.generateBackground(g);
    }
}
