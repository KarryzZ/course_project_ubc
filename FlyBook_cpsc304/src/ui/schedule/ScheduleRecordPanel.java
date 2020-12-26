package ui.schedule;

import model.schedule.ScheduleRecord;
import model.schedule.Task;
import ui.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class ScheduleRecordPanel extends JPanel implements ActionListener {
    UI ui;
    ScheduleRecord schedule;
    JButton status;
    JButton finish;
    JButton todo;

    public ScheduleRecordPanel(UI ui, ScheduleRecord schedule) {
        this.ui = ui;
        this.schedule = schedule;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);

        // a sub-panel contains the time info
        JPanel timePanel = timePanel(schedule);
        add(timePanel);
        timePanel.setAlignmentX(LEFT_ALIGNMENT);

        // the event detail
        JLabel event = ui.generateLabel("Event: " + schedule.getEvent());
        add(event);
        event.setAlignmentX(LEFT_ALIGNMENT);

        // a sub-panel contains all the tasks under the event
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);
        panel.add(Box.createRigidArea(new Dimension(20, 0)));
        JPanel tasksPanel = new JPanel();
        tasksPanel.setLayout(new BoxLayout(tasksPanel, BoxLayout.Y_AXIS));
        tasksPanel.setOpaque(false);

        Task[] tasks = ui.getApplication().getTasksBySchedule(schedule);

        for (Task value : tasks) {
            JPanel task = taskRecord(value);
            tasksPanel.add(task);
            task.setAlignmentX(LEFT_ALIGNMENT);
            task.setAlignmentY(TOP_ALIGNMENT);
        }

        JButton newTask = ui.generateChangedButton("newtask");
        newTask.addActionListener(e -> {
            ui.setContentPane(new NewTaskPanel(ui, schedule));
            ui.validate();
        });
        tasksPanel.add(newTask);
        newTask.setAlignmentX(LEFT_ALIGNMENT);

        panel.add(tasksPanel);

        add(panel);
        panel.setAlignmentX(LEFT_ALIGNMENT);
    }

    // Show the time info
    private JPanel timePanel(ScheduleRecord schedule) {
        JPanel timePanel = new JPanel();
        timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.X_AXIS));
        timePanel.setOpaque(false);

        // Generate the time string
        String timestamp = new SimpleDateFormat("yyyy.MM.dd hh:mm").format(schedule.getTime());
        ArrayList<String> partsOfLine = splitOnSpace(timestamp);

        JLabel date = new JLabel(partsOfLine.get(0) + " | ");
        date.setFont(new Font("Avenir", Font.PLAIN, 17));
        date.setForeground(new Color(15, 85, 130));
        timePanel.add(date);
        date.setAlignmentY(BOTTOM_ALIGNMENT);

        JLabel time = new JLabel(partsOfLine.get(1));
        time.setFont(new Font("Avenir", Font.PLAIN, 14));
        time.setForeground(Color.darkGray);
        timePanel.add(time);
        time.setAlignmentY(BOTTOM_ALIGNMENT);

        JButton delete = ui.generateChangedButton("delete");
        delete.addActionListener(e -> deleteSchedule());
        timePanel.add(delete);
        delete.setAlignmentY(BOTTOM_ALIGNMENT);

        return timePanel;
    }

    private void deleteSchedule() {
        ui.getApplication().deleteSchedule(schedule);
        this.removeAll();
        this.revalidate();
        this.repaint();
    }

    // Initiate the panel to store task record
    private JPanel taskRecord(Task task) {
        JPanel taskPanel = new JPanel();

        setTaskRecord(task, taskPanel);

        return taskPanel;
    }

    // Add contents to task record
    private void setTaskRecord(Task task, JPanel taskPanel) {
        taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.X_AXIS));
        taskPanel.setOpaque(false);

        finish = ui.generateChangedButton("check");
        finish.addActionListener(e -> changeStatus(taskPanel, task,0));
        todo = ui.generateChangedButton("unchecked");
        todo.addActionListener(e -> changeStatus(taskPanel, task,1));

        if (task.getStatus() == 0) {
            status = todo;
        } else {
            status = finish;
        }

        taskPanel.add(status);

        JLabel name = ui.generateLabel("Priority " + task.getPriority() + " | " + task.getTaskName());
        taskPanel.add(name);

        JButton delete = ui.generateChangedButton("delete");
        delete.addActionListener(e -> {
            ui.getApplication().deleteTask(task);
            taskPanel.removeAll();
            taskPanel.revalidate();
            taskPanel.repaint();
        });
        taskPanel.add(delete);
    }

    private void changeStatus(JPanel taskPanel, Task task, int i) {
        ui.getApplication().updateTaskStatus(task, i);
        task.setStatus(i);

        taskPanel.removeAll();

        setTaskRecord(task, taskPanel);

        taskPanel.revalidate();
        taskPanel.repaint();
    }

    private static ArrayList<String> splitOnSpace(String line) {
        String[] splits = line.split(" ");
        return new ArrayList<>(Arrays.asList(splits));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
