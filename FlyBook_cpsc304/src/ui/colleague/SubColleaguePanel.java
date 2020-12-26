package ui.colleague;

import model.user.User;
import ui.UI;
import ui.post.IndividualPostWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SubColleaguePanel extends JPanel implements ActionListener {
    UI ui;
    User user;
    String currentUser;

    public SubColleaguePanel(UI ui, User user, String currentUser) {
        this.ui = ui;
        this.user = user;
        this.currentUser = currentUser;

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);

        JPanel info = infoPanel();
        info.setOpaque(false);
        add(info);
        info.setAlignmentY(Component.CENTER_ALIGNMENT);

        add(Box.createHorizontalGlue());

        JPanel button = buttonPanel();
        button.setOpaque(false);
        add(button);
        button.setAlignmentY(Component.CENTER_ALIGNMENT);
    }

    private JPanel infoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JPanel userInfo = new JPanel();
        userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.X_AXIS));
        userInfo.setOpaque(false);
        JLabel name = ui.generateText(user.getName());
        name.setFont(new Font("Avenir", Font.BOLD, 14));
        userInfo.add(name);
        name.setAlignmentY(BOTTOM_ALIGNMENT);
        userInfo.add(Box.createRigidArea(new Dimension(5, 0)));
        JLabel city = new JLabel(user.getTimezone().getCity() + " | " + user.getTimezone().getZoneCode());
        city.setFont(new Font("Avenir", Font.BOLD, 12));
        city.setForeground(Color.GRAY);
        userInfo.add(city);
        city.setAlignmentY(BOTTOM_ALIGNMENT);

        infoPanel.add(userInfo);
        userInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JLabel email = ui.generateLabel(user.getEmail());
        infoPanel.add(email);
        email.setAlignmentX(Component.LEFT_ALIGNMENT);

        return infoPanel;
    }

    private void resizeButton(JButton b) {
        b.setFont(new Font("Avenir", Font.BOLD, 12));
        b.setMinimumSize(new Dimension(70,30));
        b.setPreferredSize(new Dimension(70,30));
        b.setMaximumSize(new Dimension(70,30));
    }

    private JPanel buttonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));

        if (!(currentUser.equals(user.getUserID()))) {
            JButton chat = ui.generateButton("Chat",this);
            resizeButton(chat);
            buttonPanel.add(chat);
            chat.setAlignmentX(Component.CENTER_ALIGNMENT);
        } else {
            JLabel me = ui.generateLabel("Me");
            me.setFont(new Font("Avenir", Font.BOLD, 12));
            buttonPanel.add(me);
            me.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        JButton posts = ui.generateButton("Posts",this);
        resizeButton(posts);
        buttonPanel.add(posts);
        posts.setAlignmentX(Component.CENTER_ALIGNMENT);

        return buttonPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Posts")) {
            new IndividualPostWindow(ui, user.getUserID());
        } else {
            new IndividualChatWindow(ui, currentUser, user.getUserID());
        }
    }
}
