package ui.group;

import main.Application;
import model.group.Group;
import model.group.GroupMember;
import ui.UI;
import ui.utilities.Header;
import ui.utilities.SuccessMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;

public class GroupListPanel extends JPanel implements ActionListener {
    UI ui;

    public GroupListPanel(UI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());

        add(new Header(ui, "Group"), BorderLayout.PAGE_START);

        JTabbedPane groupList = groupList();
        groupList.setOpaque(false);
        add(groupList, BorderLayout.CENTER);

        JPanel button = new JPanel();
        button.setLayout(new FlowLayout());
        button.setBackground(new Color(15, 85, 130));
        button.setMaximumSize(new Dimension(430, 40));
        button.setMinimumSize(new Dimension(430, 40));
        JButton newGroup = ui.generateChangedButton("new");
        newGroup.addActionListener(this);
        button.add(newGroup);
        button.setAlignmentY(BOTTOM_ALIGNMENT);

        add(button, BorderLayout.PAGE_END);
    }

    private JTabbedPane groupList() {
        JTabbedPane tabbedPane = new JTabbedPane();

        JScrollPane myGroups = myGroups();
        myGroups.setOpaque(false);
        tabbedPane.addTab("My groups", myGroups);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        JScrollPane allGroups = allGroups();
        allGroups.setOpaque(false);
        tabbedPane.addTab("All groups", allGroups);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        return tabbedPane;
    }

    private JScrollPane myGroups() {
        JPanel panel = new JPanel();
        JScrollPane myGroup = new JScrollPane(panel);
        myGroup.setOpaque(false);
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        Group[] groups = ui.getApplication().getCurrentUsersGroups();

        if (groups.length == 0) {
            JLabel title = ui.generateLabel("You have not joined any group yet.");
            title.setAlignmentX(CENTER_ALIGNMENT);
            panel.add(title);
        } else {
            for (Group value : groups) {
                JPanel group = new SubGroupListPanel(ui, value);
                panel.add(group);
                group.add(Box.createRigidArea(new Dimension(0, 20)));

                group.setAlignmentX(LEFT_ALIGNMENT);
                group.setAlignmentY(TOP_ALIGNMENT);
            }
        }

        return myGroup;
    }

    private JScrollPane allGroups() {
        JPanel panel = new JPanel();
        JScrollPane myGroup = new JScrollPane(panel);
        myGroup.setOpaque(false);
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        Group[] groups = ui.getApplication().getGroups();

        if (groups.length == 0) {
            JLabel title = ui.generateLabel("There is no group available.");
            title.setAlignmentX(CENTER_ALIGNMENT);
            panel.add(title);
        } else {
            for (Group value : groups) {
                JPanel group = new SubGroupListPanel(ui, value);
                panel.add(group);
                group.add(Box.createRigidArea(new Dimension(0, 20)));

                group.setAlignmentX(LEFT_ALIGNMENT);
                group.setAlignmentY(TOP_ALIGNMENT);
            }
        }

        return myGroup;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Application app = ui.getApplication();

        int num = app.countGroups() + 1;
        while (app.getGroupByID("G" + num) != null) {
            num++;
        }
        String gid = "G" + num;
        String name = (String)JOptionPane.showInputDialog(ui, "Enter a group name:",
                "New group",
                JOptionPane.PLAIN_MESSAGE,
                null, null, null);
        if (name != null) {
            Group group = new Group(
                    gid,
                    new Timestamp(System.currentTimeMillis()),
                    name,
                    app.getCurrentUser());
            app.addGroup(group);

            GroupMember member = new GroupMember(
                    new Timestamp(System.currentTimeMillis()),
                    app.getCurrentUser(),
                    group,
                    (String) JOptionPane.showInputDialog(ui, "Choose a nickname:",
                            "Join group",
                            JOptionPane.PLAIN_MESSAGE,
                            null, null, null));
            app.joinGroup(member);

            app.becomeAdmin(app.getCurrentUser(), group);

            new SuccessMessage("You created and joined <" + group.getName() + "> successfully!");
            ui.switchPanel("Group");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ui.generateBackground(g);
    }
}
