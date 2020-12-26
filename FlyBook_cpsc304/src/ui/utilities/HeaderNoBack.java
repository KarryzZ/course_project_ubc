package ui.utilities;

import ui.UI;

import javax.swing.*;
import java.awt.*;

public class HeaderNoBack extends Header {
    public HeaderNoBack(UI ui, String heading) {
        this.ui = ui;
        this.heading = heading;

        setBackground(new Color(15, 85, 130));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        JLabel title = new JLabel(heading + "    ");
        title.setForeground(Color.white);
        title.setFont(new Font("Avenir", Font.BOLD, 20));

        add(Box.createHorizontalGlue());
        add(title);

        setMaximumSize(new Dimension(430, 40));
        setPreferredSize(new Dimension(430, 40));
        setMinimumSize(new Dimension(430, 40));
    }
}
