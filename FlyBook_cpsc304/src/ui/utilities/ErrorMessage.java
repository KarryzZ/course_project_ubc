package ui.utilities;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ErrorMessage extends JFrame {
    public ErrorMessage(String s) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));

        JLabel title = new JLabel("✖ Warning");
        title.setForeground(Color.RED);
        title.setFont(new Font("Avenir", Font.BOLD, 15));
        panel.add(title);
        JLabel label = new JLabel(s);
        label.setFont(new Font("Avenir", Font.PLAIN, 13));
        panel.add(label);

        setContentPane(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }
}
