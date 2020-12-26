package ui.group;

import main.Application;
import model.group.Group;
import model.group.GroupMember;
import ui.utilities.SuccessMessage;
import ui.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;

public class SubGroupListPanel extends JPanel implements ActionListener {
    UI ui;
    Group group;

    public SubGroupListPanel(UI ui, Group group) {
        this.ui = ui;
        this.group = group;

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);

        JLabel id = ui.generateLabel(group.getGroupID() + " | ");
        add(id);
        JLabel name = ui.generateText(group.getName());
        add(name);

        Application app = ui.getApplication();
        if (app.isMember(group.getGroupID())) {
            add(Box.createHorizontalGlue());
            JButton open = ui.generateButton("Open", this);
            add(open);
            resizeButton(open);
            add(Box.createRigidArea(new Dimension(2,0)));
            JButton about = ui.generateButton("About", this);
            add(about);
            resizeButton(about);
        } else {
            add(Box.createHorizontalGlue());
            JButton join = ui.generateButton("Join", this);
            add(join);
            resizeButton(join);
        }
    }

    private void resizeButton(JButton b) {
        b.setFont(new Font("Avenir", Font.BOLD, 12));
        b.setMinimumSize(new Dimension(70,30));
        b.setPreferredSize(new Dimension(70,30));
        b.setMaximumSize(new Dimension(70,30));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Application app = ui.getApplication();
        if (e.getActionCommand().equals("Open")) {
            new GroupChatWindow(ui, group);
        } else if (e.getActionCommand().equals("About")) {
            ui.setContentPane(new GroupInfoPanel(ui, group));
            ui.revalidate();
        } else {
            GroupMember member = new GroupMember(
                    new Timestamp(System.currentTimeMillis()),
                    app.getCurrentUser(),
                    group,
                    (String)JOptionPane.showInputDialog(ui, "Choose a nickname:",
                            "Join group",
                            JOptionPane.PLAIN_MESSAGE,
                            null, null, null));
            app.joinGroup(member);
            new SuccessMessage("You joined " + group.getName() + " successfully!");
            ui.switchPanel("Group");
        }
    }
}
