package ui.post;

import main.Application;
import model.post.Media;
import model.post.SharePost;
import ui.utilities.ErrorMessage;
import ui.utilities.Header;
import ui.utilities.SuccessMessage;
import ui.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Objects;

public class NewPostPanel extends JPanel implements ActionListener {
    UI ui;

    private final int TEXT_SPACE = 18;
    private final int ENTRY_SPACE = 9;

    JTextArea postEntry = new JTextArea(1,10);
    JTextField mediaEntry = new JTextField(22);
    JComboBox mediaType;

    public NewPostPanel(UI ui) {
        this.ui = ui;

        setLayout(new BorderLayout());

        add(new Header(ui, "New Post"), BorderLayout.PAGE_START);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel content = ui.generateLabel("Share your thoughts...");
        panel.add(content);
        content.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(postEntry);
        postEntry.setAlignmentX(LEFT_ALIGNMENT);

        panel.add(Box.createRigidArea(new Dimension(0,10)));
        JLabel mediaTitle = ui.generateLabel("Media (optional):");
        panel.add(mediaTitle);
        mediaTitle.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(Box.createRigidArea(new Dimension(0,5)));

        JPanel media = new JPanel(new FlowLayout());
        media.setOpaque(false);
        media.add(textPanel());
        media.add(Box.createRigidArea(new Dimension(20,0)));
        media.add(entryPanel());
        panel.add(media);
        media.setAlignmentX(LEFT_ALIGNMENT);

        add(panel, BorderLayout.CENTER);

        // a sub-panel for submission
        JPanel button = new JPanel();
        button.setLayout(new FlowLayout());
        button.setBackground(new Color(15, 85, 130));
        button.setMaximumSize(new Dimension(430, 40));
        button.setMinimumSize(new Dimension(430, 40));
        JButton submit = ui.generateChangedButton("submit");
        submit.addActionListener(this);
        button.add(submit);
        button.setAlignmentY(BOTTOM_ALIGNMENT);

        add(button, BorderLayout.PAGE_END);
    }

    private JPanel entryPanel() {
        JPanel entryPanel = new JPanel();
        entryPanel.setLayout(new BoxLayout(entryPanel, BoxLayout.Y_AXIS));
        entryPanel.setOpaque(false);

        String[] types = {"", "Image", "Video", "Music"};
        mediaType = new JComboBox(types);
        entryPanel.add(mediaType);
        mediaType.setAlignmentX(LEFT_ALIGNMENT);
        entryPanel.add(Box.createRigidArea(new Dimension(0, ENTRY_SPACE)));
        entryPanel.add(mediaEntry);
        mediaEntry.setAlignmentX(LEFT_ALIGNMENT);

        return entryPanel;
    }

    private JPanel textPanel() {
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel type = ui.generateLabel("▸  Media Type");
        type.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.add(type);
        textPanel.add(Box.createRigidArea(new Dimension(0, TEXT_SPACE)));

        JLabel url = ui.generateLabel("▸  URL");
        url.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.add(url);

        return textPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String content = postEntry.getText();
        String type = (String) Objects.requireNonNull(mediaType.getSelectedItem());
        String url = mediaEntry.getText();

        Application application = ui.getApplication();

        if (content.isEmpty()) {
            new ErrorMessage("You can not post an empty message.");
        } else {
            SharePost post;
            Media media = null;
            int numPost = application.getPosts().length + 1;

            if (!(url.isEmpty()) && !(type.isEmpty())) {
                try {
                    new URL(url);

                    if (application.getMediaByUrl(url) != null) {
                        media = application.getMediaByUrl(url);
                    } else {
                        media = new Media(type, url);
                        application.addMedia(media);
                    }
                } catch (MalformedURLException exception) {
                    new ErrorMessage("The url you add is invalid.");
                    return;
                }
            }

            while (application.getPostByID("P" + numPost) != null) {
                numPost++;
            }
            String pid = "P" + numPost;
            post = new SharePost(
                    pid,
                    new Timestamp(System.currentTimeMillis()),
                    content,
                    application.getCurrentUser(),
                    media);
            application.addPost(post);
            new SuccessMessage("The post is shared!");
            ui.switchPanel("Post");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ui.generateBackground(g);
    }
}
