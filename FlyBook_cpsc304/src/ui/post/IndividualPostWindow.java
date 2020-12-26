package ui.post;

import main.Application;
import model.post.SharePost;
import ui.UI;
import ui.utilities.HeaderNoBack;

import javax.swing.*;
import java.awt.*;

// This panel shows all the posts posted by a specific user
public class IndividualPostWindow extends JFrame {
    UI ui;
    String uid;

    public IndividualPostWindow(UI ui, String uid) {
        this.ui = ui;
        this.uid = uid;

        setPreferredSize(new Dimension(430, 600));

        setContentPane(IndividualPostPanel());

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    private JPanel IndividualPostPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel header = new HeaderNoBack(ui, ui.getApplication().getUserByID(uid).getName());
        panel.add(header);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollPane scrPane = postList();
        panel.add(scrPane);
        scrPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        return panel;
    }

    private JScrollPane postList() {
        JPanel panel = new JPanel();
        JScrollPane postList = new JScrollPane(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        postList.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Application app = ui.getApplication();
        SharePost[] posts = app.getIndividualPost(uid);

        if (posts.length == 0) {
            JLabel title = ui.generateLabel(app.getUserByID(uid).getName() + " has not shared any post yet.");
            title.setAlignmentX(CENTER_ALIGNMENT);
            panel.add(title);
        } else {
            for (int i = posts.length - 1; i >= 0; i--) {
                JPanel post = new IndividualPostRecord(ui, this, posts[i]);
                panel.add(post);

                post.add(Box.createRigidArea(new Dimension(0, 20)));

                post.setAlignmentX(LEFT_ALIGNMENT);
                post.setAlignmentY(TOP_ALIGNMENT);
            }
        }

        return postList;
    }

    public void refresh() {
        setContentPane(IndividualPostPanel());
        validate();
    }
}
