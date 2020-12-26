package ui.group;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GroupChatEntry extends JPanel implements ActionListener {
    JTextField textEntry = new JTextField(27);
    GroupChatPanel panel;

    public GroupChatEntry(GroupChatPanel panel) {
        this.panel = panel;
        setLayout(new FlowLayout());
        setOpaque(false);

        add(textEntry);

        JButton send = new JButton("Send");
        send.setFont(new Font("Avenir", Font.BOLD, 12));
        send.addActionListener(this);
        add(send);
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String content = textEntry.getText();
        if (!(content.isEmpty())) {
            panel.addEntry(content);
        }
    }
}
