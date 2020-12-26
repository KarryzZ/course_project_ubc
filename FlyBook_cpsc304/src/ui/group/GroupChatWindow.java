package ui.group;

import model.group.Group;
import ui.UI;

import javax.swing.*;
import java.awt.*;

public class GroupChatWindow extends JFrame {
    public GroupChatWindow(UI ui, Group group) {
        setPreferredSize(new Dimension(430, 600));

        setContentPane(new GroupChatPanel(ui, this, group));

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    public void refresh(UI ui, Group group) {
        setContentPane(new GroupChatPanel(ui, this, group));
        validate();
    }
}
