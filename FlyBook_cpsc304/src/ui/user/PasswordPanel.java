package ui.user;

import main.Application;
import ui.utilities.ErrorMessage;
import ui.UI;
import ui.utilities.Header;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PasswordPanel extends JPanel implements ActionListener {
    UI ui;

    private final int TEXT_SPACE = 16;
    private final int ENTRY_SPACE = 9;

    JPasswordField passwordField = new JPasswordField(10);
    JPasswordField reEnterField = new JPasswordField(10);

    public PasswordPanel(UI ui) {
        this.ui = ui;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // add header
        add(new Header(ui, "Change Password"));
        add(Box.createRigidArea(new Dimension(0,20)));

        // a sub-panel contains the entry area
        JPanel entry = new JPanel();
        entry.setLayout(new BoxLayout(entry, BoxLayout.X_AXIS));
        entry.setOpaque(false);
        entry.add(textPanel());
        entry.add(Box.createRigidArea(new Dimension(20,0)));
        entry.add(entryPanel());

        add(entry);
        entry.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalGlue());
        add(Box.createRigidArea(new Dimension(0,415)));

        // a sub-panel for submission
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

    // Show the labels
    private JPanel textPanel() {
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel password = ui.generateLabel("New Password");
        textPanel.add(password);
        password.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.add(Box.createRigidArea(new Dimension(0, TEXT_SPACE)));

        JLabel reEnter = ui.generateLabel("Re-enter New Password");
        textPanel.add(reEnter);
        reEnter.setAlignmentX(Component.LEFT_ALIGNMENT);

        return textPanel;
    }

    private JPanel entryPanel() {
        JPanel entryPanel = new JPanel();
        entryPanel.setLayout(new BoxLayout(entryPanel, BoxLayout.Y_AXIS));
        entryPanel.setOpaque(false);

        passwordField.setEchoChar('*');
        passwordField.setMaximumSize(new Dimension(100,40));
        entryPanel.add(passwordField);
        entryPanel.add(Box.createRigidArea(new Dimension(0, ENTRY_SPACE)));

        reEnterField.setEchoChar('*');
        reEnterField.setMaximumSize(new Dimension(100,40));
        entryPanel.add(reEnterField);

        return entryPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String password = String.valueOf(passwordField.getPassword());
        String reEnter = String.valueOf(reEnterField.getPassword());

        if (password.isEmpty()) {
            new ErrorMessage("Password cannot be empty.");
        } else if (reEnter.isEmpty()) {
            new ErrorMessage("You must re-enter the password.");
        } else if (!(password.equals(reEnter))) {
            new ErrorMessage("The two passwords do not match.");
        } else {
            Application application = ui.getApplication();
            application.resetPassword(password);
            ui.switchPanel("Account");
        }
    }

    // Customize the background
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ui.generateBackground(g);
    }
}
