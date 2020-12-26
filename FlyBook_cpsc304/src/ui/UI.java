package ui;

import main.Application;
import ui.colleague.ColleaguePanel;
import ui.group.GroupListPanel;
import ui.meeting.MeetingListPanel;
import ui.post.NewPostPanel;
import ui.post.PostPanel;
import ui.schedule.SchedulePanel;
import ui.user.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

// This class is used to handle the transition between difference interfaces
public class UI extends JFrame {
    Application application;

    public UI(Application application) {
        super("FlyBook");
        this.application = application;
    }

    // Show the frame and the login panel as the first panel
    public void showFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(430, 600));

        // Start with the login panel
        JPanel loginPane = new LoginPanel(this);
        setContentPane(loginPane);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    // Switch to different panels according to the keyword
    public void switchPanel(String s) {
        switch (s) {
            case "Register":
                setContentPane(new RegisterPanel(this));
                break;
            case "Login":
                setContentPane(new LoginPanel(this));
                break;
            case "Main":
                setContentPane(new MainPanel(this));
                break;
            case "Colleague":
                setContentPane(new ColleaguePanel(this));
                break;
            case "Group":
                setContentPane(new GroupListPanel(this));
                break;
            case "Post":
                setContentPane(new PostPanel(this));
                break;
            case "Meeting":
                setContentPane(new MeetingListPanel(this));
                break;
            case "Schedule":
                setContentPane(new SchedulePanel(this));
                break;
            case "Account":
                setContentPane(new AccountPanel(this, application.getCurrentUser()));
                break;
            case "Reset Password":
                setContentPane(new PasswordPanel(this));
                break;
            case "New Post":
                setContentPane(new NewPostPanel(this));
                break;
        }
        revalidate();
    }

    public Application getApplication() {
        return application;
    }

    // Generate label
    public JLabel generateLabel(String s) {
        JLabel text = new JLabel(s);
        text.setFont(new Font("Avenir", Font.BOLD, 14));
        text.setForeground(Color.BLACK);
        return text;
    }

    // Generate text
    public JLabel generateText(String s) {
        JLabel text = new JLabel(s);
        text.setFont(new Font("Avenir", Font.PLAIN, 14));
        text.setForeground(new Color(15, 85, 130));
        return text;
    }

    // Generate the icon button
    public JButton generateChangedButton(String buttonType) {
        ImageIcon i1 = new ImageIcon("images" + "/" + buttonType + ".png");
        ImageIcon i2 = new ImageIcon(i1.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        JButton change = new JButton(i2);

        change.setBorderPainted(false);
        change.setOpaque(false);
        change.setContentAreaFilled(false);

        return change;
    }

    // Customize text button
    public JButton generateButton(String s, ActionListener al) {
        JButton button = new JButton(s);

        button.setFont(new Font("Avenir", Font.BOLD, 14));
        button.setForeground(Color.DARK_GRAY);
        button.setMinimumSize(new Dimension(170, 35));
        button.setPreferredSize(new Dimension(170, 35));
        button.setMaximumSize(new Dimension(170, 35));
        button.addActionListener(al);
        button.setActionCommand(s);

        return button;
    }

    public void generateBackground(Graphics g) {
        try {
            Image i = ImageIO.read(new File("images/background.png"));
            g.drawImage(i, 0, 0, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
