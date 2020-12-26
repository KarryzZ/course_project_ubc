package ui.group;

import model.group.GroupChat;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class GroupChatRecord extends JPanel {
    public GroupChatRecord(GroupChat chat, String currentUser) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setOpaque(false);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.X_AXIS));
        info.setOpaque(false);

        String name;
        if (chat.getSender().getNickname() != null) {
            name = chat.getSender().getNickname();
        } else {
            name = chat.getSender().getUser().getName();
        }
        JLabel sender = new JLabel(name);
        sender.setFont(new Font("Helvetica", Font.BOLD, 16));
        sender.setAlignmentY(BOTTOM_ALIGNMENT);
        if (currentUser.equals(chat.getSender().getUser().getUserID())) {
            sender.setForeground(new Color(15, 85, 130));
        }
        String timestamp = new SimpleDateFormat("yyyy.MM.dd hh:mm").format(chat.getTime());
        JLabel time = new JLabel(timestamp);
        time.setForeground(Color.GRAY);

        info.add(sender);
        sender.setAlignmentY(BOTTOM_ALIGNMENT);
        info.add(Box.createRigidArea(new Dimension(5, 0)));
        info.add(time);
        time.setAlignmentY(BOTTOM_ALIGNMENT);

        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.LINE_AXIS));
        text.setOpaque(false);
        JLabel content = new JLabel(chat.getContent());
        content.setFont(new Font("Avenir", Font.BOLD, 14));
        text.add(content);
        content.setAlignmentX(LEFT_ALIGNMENT);

        add(info);
        info.setAlignmentX(LEFT_ALIGNMENT);
        add(text);
        text.setAlignmentX(LEFT_ALIGNMENT);
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

}
