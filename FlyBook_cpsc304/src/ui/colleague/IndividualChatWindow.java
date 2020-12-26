package ui.colleague;

import ui.UI;

import javax.swing.*;
import java.awt.*;

public class IndividualChatWindow extends JFrame {
    public IndividualChatWindow(UI ui, String uid1, String uid2) {
        setPreferredSize(new Dimension(430, 600));

        setContentPane(new IndividualChatPanel(ui, this, uid1, uid2));

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    public void refresh(UI ui, String uid1, String uid2) {
        setContentPane(new IndividualChatPanel(ui, this, uid1, uid2));
        validate();
    }
}
