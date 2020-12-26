package ui.colleague;

import main.Application;
import model.user.User;
import ui.UI;
import ui.utilities.Header;

import javax.swing.*;
import java.awt.*;

public class ColleaguePanel extends JPanel {
    UI ui;

    public ColleaguePanel(UI ui) {
        this.ui = ui;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(new Header(ui, "Colleague"));

        JScrollPane scrPane = userList();
        add(scrPane);
        scrPane.setOpaque(false);
        scrPane.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private JScrollPane userList() {
        JPanel panel = new JPanel();
        JScrollPane userList = new JScrollPane(panel);
        panel.setOpaque(false);
        userList.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        Application app = ui.getApplication();
        User[] users = app.getUserList();
        String currentUser = app.getCurrentUserID();

        for (User value : users) {
            JPanel user = new SubColleaguePanel(ui, value, currentUser);
            panel.add(user);
            user.setAlignmentX(LEFT_ALIGNMENT);
            panel.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        return userList;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ui.generateBackground(g);
    }
}
