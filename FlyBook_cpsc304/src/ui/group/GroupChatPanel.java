package ui.group;

import main.Application;
import model.group.Group;
import model.group.GroupChat;
import ui.UI;
import ui.utilities.HeaderNoBack;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;

public class GroupChatPanel extends JPanel {
    UI ui;
    GroupChatWindow window;
    Group group;

    public GroupChatPanel(UI ui, GroupChatWindow window, Group group) {
        this.ui = ui;
        this.group = group;
        this.window = window;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);

        JPanel header = new HeaderNoBack(ui, group.getName());
        add(header);
        header.setAlignmentX(LEFT_ALIGNMENT);

        JScrollPane scrPane = chatHistory();
        add(scrPane);
        scrPane.setOpaque(false);
        scrPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel entry = new GroupChatEntry(this);
        add(entry);
        entry.setAlignmentX(LEFT_ALIGNMENT);
    }

    private JScrollPane chatHistory() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        JScrollPane chatHistory = new JScrollPane(panel);
        chatHistory.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Application app = ui.getApplication();
        GroupChat[] history = app.getGroupChatHistory(group.getGroupID());

        for (int i = 0; i < history.length; i++) {
            JPanel chat = new GroupChatRecord(history[i], app.getCurrentUser().getUserID());
            panel.add(chat);
            chat.setOpaque(false);

            chat.setAlignmentX(LEFT_ALIGNMENT);
            chat.setAlignmentY(TOP_ALIGNMENT);

            chat.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        return chatHistory;
    }

    public void addEntry(String content) {
        Application application = ui.getApplication();

        GroupChat record = new GroupChat(
                new Timestamp(System.currentTimeMillis()),
                application.getGroupMemberByID(application.getCurrentUser().getUserID(), group.getGroupID()),
                content,
                group);

        application.addGroupChat(record);
        window.refresh(ui, group);
    }
}
