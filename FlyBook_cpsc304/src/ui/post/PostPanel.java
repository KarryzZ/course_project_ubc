package ui.post;

import main.Application;
import model.post.SharePost;
import ui.UI;
import ui.utilities.Header;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PostPanel extends JPanel implements ActionListener {
    UI ui;

    public PostPanel(UI ui) {
        this.ui = ui;

        setLayout(new BorderLayout());

        add(new Header(ui, "Post"), BorderLayout.PAGE_START);

        JScrollPane scrPane = postList();
        add(scrPane, BorderLayout.CENTER);

        // a sub-panel for submission
        JPanel button = new JPanel();
        button.setLayout(new FlowLayout());
        button.setBackground(new Color(15, 85, 130));
        button.setMaximumSize(new Dimension(430, 40));
        button.setMinimumSize(new Dimension(430, 40));
        JButton newSchedule = ui.generateChangedButton("new");
        newSchedule.addActionListener(this);
        button.add(newSchedule);

        add(button, BorderLayout.PAGE_END);
    }

    private JScrollPane postList() {
        JPanel panel = new JPanel();
        JScrollPane postList = new JScrollPane(panel);
        panel.setOpaque(false);
        postList.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Application app = ui.getApplication();
        SharePost[] posts = app.getPosts();

        if (posts.length == 0) {
            JLabel title = ui.generateLabel("There is no post shared.");
            title.setAlignmentX(CENTER_ALIGNMENT);
            panel.add(title);
        } else {
            for (int i = posts.length - 1; i >= 0; i--) {
                JPanel post = new IndividualPostRecord(ui, null, posts[i]);
                panel.add(post);

                post.add(Box.createRigidArea(new Dimension(0, 20)));

                post.setAlignmentX(LEFT_ALIGNMENT);
                post.setAlignmentY(TOP_ALIGNMENT);
            }
        }

        return postList;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ui.switchPanel("New Post");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ui.generateBackground(g);
    }
}
