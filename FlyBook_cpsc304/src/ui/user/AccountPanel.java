package ui.user;

import main.Application;
import model.user.User;
import ui.UI;
import ui.utilities.Header;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// This page shows all the user info and allows the user to edit the profile
public class AccountPanel extends JPanel implements ActionListener {
    UI ui;
    User user;

    private final int TEXT_SPACE = 18;

    public AccountPanel(UI ui, User user) {
        this.ui = ui;
        this.user = user;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // add header
        JPanel header = new Header(ui, "Account");
        header.setMaximumSize(new Dimension(430, 40));
        header.setMinimumSize(new Dimension(430, 40));
        add(header);
        add(Box.createRigidArea(new Dimension(0,20)));

        // a sub-panel contains the info
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.X_AXIS));
        info.setOpaque(false);
        info.add(labelPanel());
        info.add(Box.createRigidArea(new Dimension(20,0)));
        info.add(infoPanel());
        info.add(changePanel());

        add(info);
        info.setAlignmentX(Component.CENTER_ALIGNMENT);

        // a sub-panel contains the buttons
        JPanel button = new JPanel();
        button.setLayout(new BoxLayout(button, BoxLayout.Y_AXIS));
        button.setOpaque(false);
        button.add(ui.generateButton("Reset Password",this));
        button.add(ui.generateButton("Log Out",this));
        button.add(ui.generateButton("Delete Account",this));


        add(button);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    // Show the labels of the personal profile
    private JPanel labelPanel() {
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        labelPanel.setOpaque(false);

        JLabel userid = ui.generateLabel("User ID");
        labelPanel.add(userid);
        userid.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelPanel.add(Box.createRigidArea(new Dimension(0, TEXT_SPACE)));

        JLabel name = ui.generateLabel("Name");
        labelPanel.add(name);
        labelPanel.add(Box.createRigidArea(new Dimension(0, TEXT_SPACE)));

        JLabel city = ui.generateLabel("City");
        labelPanel.add(city);
        labelPanel.add(Box.createRigidArea(new Dimension(0, TEXT_SPACE)));

        JLabel timeZone = ui.generateLabel("Time Zone");
        labelPanel.add(timeZone);
        labelPanel.add(Box.createRigidArea(new Dimension(0, TEXT_SPACE)));

        JLabel email = ui.generateLabel("Email");
        labelPanel.add(email);

        labelPanel.setAlignmentY(TOP_ALIGNMENT);

        return labelPanel;
    }

    // Show the detailed info of the personal profile
    private JPanel infoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel userid = ui.generateText(user.getUserID());
        userid.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(userid);
        infoPanel.add(Box.createRigidArea(new Dimension(0, TEXT_SPACE)));

        JLabel name = ui.generateText(user.getName());
        infoPanel.add(name);
        infoPanel.add(Box.createRigidArea(new Dimension(0, TEXT_SPACE)));

        JLabel city = ui.generateText(user.getTimezone().getCity());
        infoPanel.add(city);
        infoPanel.add(Box.createRigidArea(new Dimension(0, TEXT_SPACE)));

        JLabel timeZone = ui.generateText(user.getTimezone().getZoneCode());
        infoPanel.add(timeZone);
        infoPanel.add(Box.createRigidArea(new Dimension(0, TEXT_SPACE)));

        JLabel email = ui.generateText(user.getEmail());
        infoPanel.add(email);

        infoPanel.setAlignmentY(TOP_ALIGNMENT);
        infoPanel.setMinimumSize(new Dimension(195, 200));
        infoPanel.setMaximumSize(new Dimension(195, 200));

        return infoPanel;
    }

    // Shows all the buttons used for changing profile
    private JPanel changePanel() {
        JPanel changePanel = new JPanel();
        changePanel.setLayout(new BoxLayout(changePanel, BoxLayout.Y_AXIS));
        changePanel.setOpaque(false);

        changePanel.add(Box.createRigidArea(new Dimension(0,33)));
        JButton name = ui.generateChangedButton("edit");
        name.addActionListener(e -> {
            String name1 = (String)JOptionPane.showInputDialog(ui, "Enter your name:",
                    "Change name",
                    JOptionPane.PLAIN_MESSAGE,
                    null, null, null);
            if (name1 != null) {
                ui.getApplication().changeName(name1);
                ui.switchPanel("Account");
            }
        });
        changePanel.add(name);
        changePanel.add(Box.createRigidArea(new Dimension(0,5)));

        JButton city = ui.generateChangedButton("edit");
        city.addActionListener(e -> {
            Object[] options = ui.getApplication().getCities();
            String city1 = (String)JOptionPane.showInputDialog(ui, "Choose your city:",
                    "Change city",
                    JOptionPane.PLAIN_MESSAGE,
                    null, options, ui.getApplication().getCurrentUser().getTimezone().getCity());
            if (city1 != null) {
                ui.getApplication().changeCity(city1);
                ui.switchPanel("Account");
            }
        });
        changePanel.add(city);
        changePanel.add(Box.createRigidArea(new Dimension(0,44)));

        JButton email = ui.generateChangedButton("edit");
        email.addActionListener(e -> {
            String email1 = (String)JOptionPane.showInputDialog(ui, "Enter your email:",
                    "Change email",
                    JOptionPane.PLAIN_MESSAGE,
                    null, null, null);
            if (email1 != null) {
                ui.getApplication().changeEmail(email1);
                ui.switchPanel("Account");
            }
        });
        changePanel.add(email);

        changePanel.setAlignmentY(TOP_ALIGNMENT);
        changePanel.setPreferredSize(new Dimension(80, 200));

        return changePanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Back":
                ui.switchPanel("Main");
                break;
            case "Reset Password":
                ui.switchPanel("Reset Password");
                break;
            case "Log Out":
                ui.switchPanel("Login");
                break;
            default:
                Object[] options = {"Yes",
                        "Cancel"};
                int n = JOptionPane.showOptionDialog(ui,
                        "Do you want to delete this account?",
                        "Confirm",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options,
                        options[1]);
                if (n == 0) {
                    Application application = ui.getApplication();
                    application.deleteAccount();
                    ui.switchPanel("Login");
                }
                break;
        }
    }

    // Customize the background
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ui.generateBackground(g);
    }
}
