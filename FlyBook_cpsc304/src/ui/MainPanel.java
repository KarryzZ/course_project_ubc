package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel extends JPanel implements ActionListener {
    UI ui;

    public MainPanel(UI ui) {
        this.ui = ui;

        setLayout(new GridLayout(0, 2));
        add(generateButton("Colleague"));
        add(generateButton("Group"));
        add(generateButton("Post"));
        add(generateButton("Meeting"));
        add(generateButton("Schedule"));
        add(generateButton("Account"));
    }

    // Generate the icon button
    public JButton generateButton(String buttonType) {
        ImageIcon i1 = new ImageIcon("images" + "/" + buttonType + ".png");
        ImageIcon i2 = new ImageIcon(i1.getImage().getScaledInstance(120, 120, Image.SCALE_DEFAULT));
        JButton button = new JButton(i2);

        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setActionCommand(buttonType);
        button.addActionListener(this);

        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ui.switchPanel(e.getActionCommand());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ui.generateBackground(g);
    }
}
