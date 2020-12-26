package ui.group;

import main.Application;
import model.group.Group;
import model.group.GroupMember;
import ui.UI;
import ui.utilities.Header;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GroupInfoPanel extends JPanel implements ActionListener {
    UI ui;
    Group group;
    Application app;

    private final Integer TEXT_SPACE = 18;

    public GroupInfoPanel(UI ui, Group group) {
        this.ui = ui;
        this.group = group;
        this.app = ui.getApplication();

        setLayout(new BorderLayout());

        add(new Header(ui, group.getName()), BorderLayout.PAGE_START);
        add(Box.createRigidArea(new Dimension(0,20)));

        JPanel pane = new JPanel();
        pane.setOpaque(false);
        JScrollPane content = new JScrollPane(pane);
        content.setOpaque(false);
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.X_AXIS));
        info.setOpaque(false);
        info.add(textPanel());
        info.add(Box.createRigidArea(new Dimension(20,0)));
        info.add(entryPanel());
        info.add(changePanel());

        pane.add(info);
        info.setAlignmentX(LEFT_ALIGNMENT);

        JPanel admin = adminList();
        pane.add(admin);
        admin.setAlignmentX(LEFT_ALIGNMENT);
        pane.add(Box.createRigidArea(new Dimension(0,20)));

        JPanel member = memberList();
        pane.add(member);
        member.setAlignmentX(LEFT_ALIGNMENT);
        pane.add(Box.createRigidArea(new Dimension(0,20)));

        JPanel active = activeList();
        pane.add(active);
        active.setAlignmentX(LEFT_ALIGNMENT);
        pane.add(Box.createRigidArea(new Dimension(0,20)));

        JPanel hardworking = hardworkingList();
        pane.add(hardworking);
        hardworking.setAlignmentX(LEFT_ALIGNMENT);
        pane.add(Box.createRigidArea(new Dimension(0,20)));

        JPanel button = buttons();
        pane.add(button);
        button.setAlignmentX(LEFT_ALIGNMENT);

        add(content, BorderLayout.CENTER);
    }

    private JPanel buttons() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
        buttons.setOpaque(false);

        JButton leave = ui.generateButton("Leave", this);
        buttons.add(leave);

        if (app.isAdmin(group.getGroupID())) {
            JButton dismiss = ui.generateButton("Dismiss", this);
            buttons.add(dismiss);
        }

        panel.add(Box.createHorizontalGlue());
        panel.add(buttons);
        panel.add(Box.createHorizontalGlue());
        return panel;
    }

    private JPanel hardworkingList() {
        JPanel activeList = new JPanel();
        activeList.setLayout(new BoxLayout(activeList, BoxLayout.Y_AXIS));
        activeList.setOpaque(false);

        JLabel title = new JLabel("Hardworking Member");
        title.setFont(new Font("Avenir", Font.PLAIN, 17));
        title.setForeground(new Color(15, 85, 130));
        title.setAlignmentX(LEFT_ALIGNMENT);
        activeList.add(title);

        GroupMember[] members = app.getHardworkingMembers(group);
        if (members.length == 0) {
            activeList.add(ui.generateLabel("There is no meeting initiated in this group yet"));
        } else {
            for (GroupMember member : members) {
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
                panel.setOpaque(false);
                panel.add(ui.generateLabel("▸ "));
                if (member.getNickname() != null) {
                    panel.add(ui.generateLabel(member.getNickname() + " | "));
                }
                JLabel name = new JLabel(member.getUser().getName());
                name.setForeground(Color.GRAY);
                name.setFont(new Font("Avenir", Font.PLAIN, 12));
                panel.add(name);

                activeList.add(panel);
                panel.setAlignmentX(LEFT_ALIGNMENT);
                panel.add(Box.createRigidArea(new Dimension(0, 2)));
            }
        }

        return activeList;
    }

    private JPanel activeList() {
        JPanel activeList = new JPanel();
        activeList.setLayout(new BoxLayout(activeList, BoxLayout.Y_AXIS));
        activeList.setOpaque(false);

        JLabel title = new JLabel("Active Member");
        title.setFont(new Font("Avenir", Font.PLAIN, 17));
        title.setForeground(new Color(15, 85, 130));
        title.setAlignmentX(LEFT_ALIGNMENT);
        activeList.add(title);

        GroupMember[] members = app.getActiveMembers(group);
        if (members.length == 0) {
            activeList.add(ui.generateLabel("There is no active member in this group"));
        } else {
            for (GroupMember member : members) {
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
                panel.setOpaque(false);
                panel.add(ui.generateLabel("▸ "));
                if (member.getNickname() != null) {
                    panel.add(ui.generateLabel(member.getNickname() + " | "));
                }
                JLabel name = new JLabel(member.getUser().getName());
                name.setForeground(Color.GRAY);
                name.setFont(new Font("Avenir", Font.PLAIN, 12));
                panel.add(name);

                activeList.add(panel);
                panel.setAlignmentX(LEFT_ALIGNMENT);
                panel.add(Box.createRigidArea(new Dimension(0, 2)));
            }
        }

        return activeList;
    }

    private JPanel adminList() {
        JPanel adminList = new JPanel();
        adminList.setLayout(new BoxLayout(adminList, BoxLayout.Y_AXIS));
        adminList.setOpaque(false);

        JLabel title = new JLabel("Group Admins");
        title.setFont(new Font("Avenir", Font.PLAIN, 17));
        title.setForeground(new Color(15, 85, 130));
        title.setAlignmentX(LEFT_ALIGNMENT);
        adminList.add(title);

        GroupMember[] admins = app.getAdminByGroup(group.getGroupID());
        for (GroupMember admin : admins) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            panel.setOpaque(false);
            panel.add(ui.generateLabel("▸ "));
            if (admin.getNickname() != null) {
                panel.add(ui.generateLabel(admin.getNickname() + " | "));
            }
            JLabel name = new JLabel(admin.getUser().getName());
            name.setForeground(Color.GRAY);
            name.setFont(new Font("Avenir", Font.PLAIN, 12));
            panel.add(name);

            adminList.add(panel);
            panel.setAlignmentX(LEFT_ALIGNMENT);
            panel.add(Box.createRigidArea(new Dimension(0, 2)));
        }

        return adminList;
    }

    private JPanel memberList() {
        JPanel memberList = new JPanel();
        memberList.setLayout(new BoxLayout(memberList, BoxLayout.Y_AXIS));
        memberList.setOpaque(false);

        JLabel title = new JLabel("Group Members");
        title.setFont(new Font("Avenir", Font.PLAIN, 17));
        title.setForeground(new Color(15, 85, 130));
        title.setAlignmentX(LEFT_ALIGNMENT);
        memberList.add(title);

        GroupMember[] members = app.getMembersByGroup(group.getGroupID());
        if (members.length == 0) {
            memberList.add(ui.generateLabel("There is no other member in this group"));
        } else {
            for (GroupMember member : members) {
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
                panel.setOpaque(false);
                panel.add(ui.generateLabel("▸ "));
                if (member.getNickname() != null) {
                    panel.add(ui.generateLabel(member.getNickname() + " | "));
                }
                JLabel name = new JLabel(member.getUser().getName());
                name.setForeground(Color.GRAY);
                name.setFont(new Font("Avenir", Font.PLAIN, 12));
                panel.add(name);

                memberList.add(panel);
                panel.setAlignmentX(LEFT_ALIGNMENT);
                panel.add(Box.createRigidArea(new Dimension(0, 2)));
            }
        }

        return memberList;
    }

    private JPanel textPanel() {
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        textPanel.add(Box.createRigidArea(new Dimension(0, 7)));
        JLabel name = ui.generateLabel("Group Name");
        name.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.add(name);
        textPanel.add(Box.createRigidArea(new Dimension(0, TEXT_SPACE)));

        JLabel city = ui.generateLabel("Your Nickname");
        city.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.add(city);

        textPanel.setAlignmentY(TOP_ALIGNMENT);

        return textPanel;
    }

    private JPanel entryPanel() {
        JPanel entryPanel = new JPanel();
        entryPanel.setLayout(new BoxLayout(entryPanel, BoxLayout.Y_AXIS));
        entryPanel.setOpaque(false);

        entryPanel.add(Box.createRigidArea(new Dimension(0, 7)));
        JLabel name = ui.generateText(group.getName());
        name.setAlignmentX(Component.LEFT_ALIGNMENT);
        entryPanel.add(name);
        entryPanel.add(Box.createRigidArea(new Dimension(0, TEXT_SPACE)));

        JLabel nickname = ui.generateText(app.getGroupMemberByID(app.getCurrentUserID(), group.getGroupID()).getNickname());
        name.setAlignmentX(Component.LEFT_ALIGNMENT);
        entryPanel.add(nickname);

        entryPanel.setAlignmentY(TOP_ALIGNMENT);
        entryPanel.setMinimumSize(new Dimension(155, 90));
        entryPanel.setMaximumSize(new Dimension(155, 90));

        return entryPanel;
    }

    private JPanel changePanel() {
        JPanel changePanel = new JPanel();
        changePanel.setLayout(new BoxLayout(changePanel, BoxLayout.Y_AXIS));
        changePanel.setOpaque(false);

        if (app.isAdmin(group.getGroupID())) {
            JButton changeName = ui.generateChangedButton("edit");
            changeName.addActionListener(e -> {
                String name = (String)JOptionPane.showInputDialog(ui, "Enter a new group name:",
                        "Change group name",
                        JOptionPane.PLAIN_MESSAGE,
                        null, null, null);
                app.updateGroupName(group.getGroupID(), name);
                group.setName(name);
                ui.setContentPane(new GroupInfoPanel(ui, group));
                ui.revalidate();
            });
            changePanel.add(changeName);
            changeName.setAlignmentX(Component.LEFT_ALIGNMENT);
            changePanel.add(Box.createRigidArea(new Dimension(0,6)));
        } else {
            changePanel.add(Box.createRigidArea(new Dimension(0, 40)));
        }

        JButton changeNickname = ui.generateChangedButton("edit");
        changeNickname.addActionListener(e -> {
            String name = (String)JOptionPane.showInputDialog(ui, "Enter a new nickname:",
                    "Change nickname",
                    JOptionPane.PLAIN_MESSAGE,
                    null, null, null);
            app.updateNickname(group.getGroupID(), name);
            ui.setContentPane(new GroupInfoPanel(ui, group));
            ui.revalidate();
        });
        changePanel.add(changeNickname);
        changeNickname.setAlignmentX(Component.LEFT_ALIGNMENT);

        changePanel.setAlignmentY(TOP_ALIGNMENT);

        return changePanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Leave")) {
            Object[] options = {"Yes",
                    "Cancel"};
            int n = JOptionPane.showOptionDialog(ui,
                    "Do you want to leave this group?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[1]);
            if (n == 0) {
                app.leaveGroup(group);
                ui.switchPanel("Group");
            }
        } else if (e.getActionCommand().equals("Dismiss")) {
            Object[] options = {"Yes",
                    "Cancel"};
            int n = JOptionPane.showOptionDialog(ui,
                    "Do you want to dismiss this group?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[1]);
            if (n == 0) {
                app.deleteGroup(group);
                ui.switchPanel("Group");
            }
        }
    }

    // Customize the background
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ui.generateBackground(g);
    }
}
