package ui.colleague;

import main.Application;
import model.user.IndividualChat;
import ui.UI;
import ui.utilities.HeaderNoBack;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;

public class IndividualChatPanel extends JPanel {
    UI ui;
    IndividualChatWindow window;
    String uid1; // current user
    String uid2; // another colleague

    public IndividualChatPanel(UI ui, IndividualChatWindow window, String uid1, String uid2) {
        this.ui = ui;
        this.uid1 = uid1;
        this.uid2 = uid2;
        this.window = window;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(new HeaderNoBack(ui, ui.getApplication().getUserByID(uid2).getName()));

        JScrollPane scrPane = chatHistory();
        add(scrPane);
        scrPane.setOpaque(false);
        scrPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel entry = new IndividualChatEntry(this);
        add(entry);
        entry.setAlignmentX(CENTER_ALIGNMENT);
    }

    private JScrollPane chatHistory() {
        JPanel panel = new JPanel();
        JScrollPane chatHistory = new JScrollPane(panel);
        panel.setOpaque(false);
        chatHistory.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Application app = ui.getApplication();
        IndividualChat[] history = app.getIndividualChatHistory(uid1, uid2);

        for (IndividualChat individualChat : history) {
            JPanel chat = new IndividualChatRecord(individualChat, uid1);
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

        IndividualChat record = new IndividualChat(
                application.getUserByID(uid1),
                application.getUserByID(uid2),
                content,
                new Timestamp(System.currentTimeMillis()));

        application.addIndividualChat(record);
        window.refresh(ui, uid1, uid2);
    }
}
