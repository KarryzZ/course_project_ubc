package ui.schedule;

import main.Application;
import model.schedule.ScheduleRecord;
import model.schedule.Task;
import ui.UI;
import ui.utilities.ErrorMessage;
import ui.utilities.Header;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class NewTaskPanel extends JPanel implements ActionListener {
    UI ui;
    ScheduleRecord schedule;

    private final int TEXT_SPACE = 16;
    private final int ENTRY_SPACE = 10;

    JTextField nameField = new JTextField(10);
    JComboBox priorityField;

    public NewTaskPanel(UI ui, ScheduleRecord schedule) {
        this.ui = ui;
        this.schedule = schedule;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // add header
        add(new Header(ui, "New Task"));
        add(Box.createRigidArea(new Dimension(0,20)));

        // a sub-panel contains the registration info
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.X_AXIS));
        info.setOpaque(false);
        info.add(textPanel());
        info.add(Box.createRigidArea(new Dimension(20,0)));
        info.add(entryPanel());

        add(info);
        info.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalGlue());
        add(Box.createRigidArea(new Dimension(0,415)));

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

        add(button);
    }

    private JPanel textPanel() {
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel task = ui.generateLabel("Task");
        task.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.add(task);
        textPanel.add(Box.createRigidArea(new Dimension(0, TEXT_SPACE)));

        JLabel priority = ui.generateLabel("Priority");
        priority.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.add(priority);

        return textPanel;
    }

    private JPanel entryPanel() {
        JPanel entryPanel = new JPanel();
        entryPanel.setLayout(new BoxLayout(entryPanel, BoxLayout.Y_AXIS));
        entryPanel.setOpaque(false);

        nameField.setMaximumSize(new Dimension(115,40));
        entryPanel.add(nameField);
        entryPanel.add(Box.createRigidArea(new Dimension(0, ENTRY_SPACE)));

        priorityField = new JComboBox(new String[]{"1", "2", "3", "4", "5"});
        priorityField.setMaximumSize(new Dimension(120,40));
        entryPanel.add(priorityField);

        return entryPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String taskName = nameField.getText();
        String priorityEntry = (String) Objects.requireNonNull(priorityField.getSelectedItem());

        if (taskName.isEmpty()) {
            new ErrorMessage("You must enter the task detail.");
        } else if (priorityEntry.isEmpty()) {
            new ErrorMessage("You must select a priority.");
        } else {
            int priority = Integer.parseInt(priorityEntry);
            Application app = ui.getApplication();
            Task task = new Task(taskName, priority, 0, schedule);
            app.addTask(task);
            ui.switchPanel("Schedule");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ui.generateBackground(g);
    }
}
