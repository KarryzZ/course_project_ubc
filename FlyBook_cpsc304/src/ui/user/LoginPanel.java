package ui.user;

import main.Application;
import ui.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel implements ActionListener {
    UI ui;

    JTextField usernameField;
    JPasswordField passwordField;

    private final int TEXT_SPACE = 16;
    private final int ENTRY_SPACE = 9;

    public LoginPanel(UI ui) {
        this.ui = ui;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel entry = new JPanel();
        entry.setLayout(new BoxLayout(entry, BoxLayout.X_AXIS));
        entry.setOpaque(false);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        JLabel useridLabel = ui.generateLabel("User ID");
        textPanel.add(useridLabel);
        useridLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.add(Box.createRigidArea(new Dimension(0, TEXT_SPACE)));
        JLabel passwordLabel = ui.generateLabel("Password");
        textPanel.add(passwordLabel);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel entryPanel = new JPanel();
        entryPanel.setLayout(new BoxLayout(entryPanel, BoxLayout.Y_AXIS));
        entryPanel.setOpaque(false);
        usernameField = new JTextField(10);
        usernameField.setMaximumSize(new Dimension(100,40));
        entryPanel.add(usernameField);
        entryPanel.add(Box.createRigidArea(new Dimension(0, ENTRY_SPACE)));
        passwordField = new JPasswordField(10);
        passwordField.setMaximumSize(new Dimension(100,40));
        passwordField.setEchoChar('*');
        entryPanel.add(passwordField);

        entry.add(textPanel);
        entry.add(Box.createRigidArea(new Dimension(20,0)));
        entry.add(entryPanel);

        add(Box.createVerticalGlue());
        add(entry);
        entry.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createRigidArea(new Dimension(0,20)));

        JPanel button = new JPanel();
        button.setLayout(new BoxLayout(button, BoxLayout.Y_AXIS));
        button.setOpaque(false);
        button.add(ui.generateButton("Log In", this));
        button.add(ui.generateButton("Register", this));

        add(button);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalGlue());
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Log In")) {
            String userid = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());
            Application app = ui.getApplication();
            app.userLogin(userid, password);
        } else {
            ui.switchPanel("Register");
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ui.generateBackground(g);
    }
}
