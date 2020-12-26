package ui.user;

import main.Application;
import ui.utilities.ErrorMessage;
import ui.UI;
import ui.utilities.Header;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class RegisterPanel extends JPanel implements ActionListener {
    UI ui;

    private final int TEXT_SPACE = 16;
    private final int ENTRY_SPACE = 9;

    JTextField useridField = new JTextField(10);
    JPasswordField passwordField = new JPasswordField(10);
    JPasswordField reEnterField = new JPasswordField(10);
    JTextField nameField = new JTextField(10);
    JComboBox cityField;
    JTextField emailField = new JTextField(10);

    public RegisterPanel(UI ui) {
        this.ui = ui;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // add header
        add(new Header(ui, "Register"));
        add(Box.createRigidArea(new Dimension(0,20)));

        // a sub-panel contains the registration info
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.X_AXIS));
        info.setOpaque(false);
        info.add(textPanel());
        info.add(Box.createRigidArea(new Dimension(20,0)));
        info.add(entryPanel());

        add(info);
        info.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalGlue());
        add(Box.createRigidArea(new Dimension(0,275)));

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

    private JPanel textPanel() {
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel userid = ui.generateLabel("User ID");
        userid.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.add(userid);
        textPanel.add(Box.createRigidArea(new Dimension(0, TEXT_SPACE)));

        JLabel password = ui.generateLabel("Password");
        password.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.add(password);
        textPanel.add(Box.createRigidArea(new Dimension(0, TEXT_SPACE)));

        JLabel reEnter = ui.generateLabel("Re-enter Password");
        reEnter.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.add(reEnter);
        textPanel.add(Box.createRigidArea(new Dimension(0, TEXT_SPACE)));

        JLabel name = ui.generateLabel("Name");
        name.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.add(name);
        textPanel.add(Box.createRigidArea(new Dimension(0, TEXT_SPACE)));

        JLabel city = ui.generateLabel("City");
        city.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.add(city);
        textPanel.add(Box.createRigidArea(new Dimension(0, TEXT_SPACE)));

        JLabel email = ui.generateLabel("E-mail");
        email.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.add(email);

        return textPanel;
    }

    private JPanel entryPanel() {
        JPanel entryPanel = new JPanel();
        entryPanel.setLayout(new BoxLayout(entryPanel, BoxLayout.Y_AXIS));
        entryPanel.setOpaque(false);

        useridField.setMaximumSize(new Dimension(115,40));
        entryPanel.add(useridField);
        entryPanel.add(Box.createRigidArea(new Dimension(0, ENTRY_SPACE)));

        passwordField.setEchoChar('*');
        passwordField.setMaximumSize(new Dimension(115,40));
        entryPanel.add(passwordField);
        entryPanel.add(Box.createRigidArea(new Dimension(0, ENTRY_SPACE)));

        reEnterField.setEchoChar('*');
        reEnterField.setMaximumSize(new Dimension(115,40));
        entryPanel.add(reEnterField);
        entryPanel.add(Box.createRigidArea(new Dimension(0, ENTRY_SPACE)));

        nameField.setMaximumSize(new Dimension(115,40));
        entryPanel.add(nameField);
        entryPanel.add(Box.createRigidArea(new Dimension(0, ENTRY_SPACE)));

        cityField = city();
        cityField.setMaximumSize(new Dimension(120,40));
        entryPanel.add(cityField);
        entryPanel.add(Box.createRigidArea(new Dimension(0, ENTRY_SPACE)));

        emailField.setMaximumSize(new Dimension(115,40));
        entryPanel.add(emailField);

        return entryPanel;
    }

    private JComboBox city() {
        Application application = ui.getApplication();
        String[] cities = application.getCities();
        return new JComboBox(cities);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String userid = useridField.getText();
        String password = String.valueOf(passwordField.getPassword());
        String reEnter = String.valueOf(reEnterField.getPassword());
        String name = nameField.getText();
        String city = (String) Objects.requireNonNull(cityField.getSelectedItem());
        String email = emailField.getText();

        if (userid.isEmpty()) {
            new ErrorMessage("You must create a user ID.");
        } else if (password.isEmpty()) {
            new ErrorMessage("You must create a password.");
        } else if (reEnter.isEmpty()) {
            new ErrorMessage("You must re-enter the password.");
        } else if (!(password.equals(reEnter))) {
            new ErrorMessage("The two passwords do not match.");
        } else if (name.isEmpty()) {
            new ErrorMessage("You must enter your name.");
        } else if (city.isEmpty()) {
            new ErrorMessage("You must select your city.");
        } else if (email.isEmpty()) {
            new ErrorMessage("You must enter your email.");
        }
        else {
            Application app = ui.getApplication();
            app.userRegister(userid, password, name, city, email);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ui.generateBackground(g);
    }
}
