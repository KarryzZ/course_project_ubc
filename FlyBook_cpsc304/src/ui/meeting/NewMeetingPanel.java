package ui.meeting;

import main.Application;
import model.group.Group;
import model.meeting.MeetingRecord;
import ui.UI;
import ui.utilities.ErrorMessage;
import ui.utilities.Header;
import ui.utilities.SuccessMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

public class NewMeetingPanel extends JPanel implements ActionListener {
    UI ui;

    private final int TEXT_SPACE = 16;
    private final int ENTRY_SPACE = 10;

    JTextField topicField = new JTextField(10);
    JComboBox groupList;

    public NewMeetingPanel(UI ui) {
        this.ui = ui;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(new Header(ui, "New Meeting"));
        add(Box.createRigidArea(new Dimension(0,20)));

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.X_AXIS));
        info.setOpaque(false);
        info.add(textPanel());
        info.add(Box.createRigidArea(new Dimension(20,0)));
        info.add(entryPanel());

        add(info);
        info.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalGlue());
        add(Box.createRigidArea(new Dimension(0,415)));

        JPanel button = new JPanel();
        button.setLayout(new FlowLayout());
        button.setBackground(new Color(15, 85, 130));
        button.setMaximumSize(new Dimension(430, 40));
        button.setMinimumSize(new Dimension(430, 40));
        JButton submit = ui.generateChangedButton("submit");
        submit.addActionListener(this);
        button.add(submit);
        button.setAlignmentY(BOTTOM_ALIGNMENT);

        add(button);
    }

    private JPanel textPanel() {
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel topic = ui.generateLabel("Topic");
        topic.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.add(topic);
        textPanel.add(Box.createRigidArea(new Dimension(0, TEXT_SPACE)));

        JLabel group = ui.generateLabel("Group");
        group.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.add(group);

        return textPanel;
    }

    private JPanel entryPanel() {
        JPanel entryPanel = new JPanel();
        entryPanel.setLayout(new BoxLayout(entryPanel, BoxLayout.Y_AXIS));
        entryPanel.setOpaque(false);

        entryPanel.add(topicField);
        topicField.setMaximumSize(new Dimension(200,40));
        entryPanel.add(Box.createRigidArea(new Dimension(0, ENTRY_SPACE)));

        groupList = group();
        groupList.setMaximumSize(new Dimension(200,40));
        entryPanel.add(groupList);

        return entryPanel;
    }

    private JComboBox group() {
        Application application = ui.getApplication();
        Group[] groups = application.getAdminGroups();
        String[] names = new String[groups.length];
        for (int i = 0; i < groups.length; i++) {
            names[i] = groups[i].getGroupID() + ": " + groups[i].getName();
        }
        return new JComboBox(names);
    }

    private static ArrayList<String> splitOnSpace(String line) {
        String[] splits = line.split(": ");
        return new ArrayList<>(Arrays.asList(splits));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String topic = topicField.getText();
        String group = null;
        if (groupList.getSelectedItem() != null) {
            group = (String) groupList.getSelectedItem();
        }

        if (topic.isEmpty()) {
            new ErrorMessage("You must enter a meeting topic.");
        } else if (group == null) {
            new ErrorMessage("Only group administrators can initiate a meeting.");
        } else {
            ArrayList<String> partsOfLine = splitOnSpace(group);
            String gid = partsOfLine.get(0);
            int num = ui.getApplication().countMeetings() + 1;
            String mid = "M" + num;
            MeetingRecord meeting = new MeetingRecord(
                    mid,
                    0,
                    topic,
                    new Timestamp(System.currentTimeMillis()),
                    null,
                    ui.getApplication().getGroupByID(gid)
            );
            ui.getApplication().addMeeting(meeting);
            ui.getApplication().joinMeeting(meeting);
            new SuccessMessage("You just started a new meeting!");
            ui.switchPanel("Meeting");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ui.generateBackground(g);
    }
}
