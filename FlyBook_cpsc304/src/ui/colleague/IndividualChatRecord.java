package ui.colleague;

import model.user.IndividualChat;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class IndividualChatRecord extends JPanel {
    public IndividualChatRecord(IndividualChat chat, String currentUser) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setOpaque(false);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.X_AXIS));
        info.setOpaque(false);

        JLabel sender = new JLabel(chat.getSender().getName());
        sender.setFont(new Font("Helvetica", Font.BOLD, 15));
        if (currentUser.equals(chat.getSender().getUserID())) {
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
