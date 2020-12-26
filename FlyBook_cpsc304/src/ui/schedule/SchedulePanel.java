package ui.schedule;

import ui.UI;
import ui.utilities.Header;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

public class SchedulePanel extends JPanel implements ActionListener {
    UI ui;

    public SchedulePanel(UI ui) {
        this.ui = ui;

        setLayout(new BorderLayout());

        // add header
        add(new Header(ui, "Schedule"), BorderLayout.PAGE_START);

        // add tabbed pane
        JTabbedPane scheduleList = scheduleList();
        scheduleList.setOpaque(false);
        add(scheduleList, BorderLayout.CENTER);

        // a sub-panel for submission
        JPanel button = new JPanel();
        button.setLayout(new FlowLayout());
        button.setBackground(new Color(15, 85, 130));
        button.setMaximumSize(new Dimension(430, 40));
        button.setMinimumSize(new Dimension(430, 40));
        JButton newSchedule = ui.generateChangedButton("new");
        newSchedule.addActionListener(this);
        button.add(newSchedule);
        button.setAlignmentY(BOTTOM_ALIGNMENT);

        add(button, BorderLayout.PAGE_END);
    }

    private JTabbedPane scheduleList() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setOpaque(false);

        JPanel schedule = new ScheduleListPanel(ui);
        schedule.setOpaque(false);
        tabbedPane.addTab("My schedule", schedule);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        JScrollPane availability = availability();
        availability.setOpaque(false);
        tabbedPane.addTab("Availabilities", availability);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        return tabbedPane;
    }

    private JScrollPane availability() {
        JPanel pane = new JPanel();
        JScrollPane availability = new JScrollPane(pane);
        availability.setOpaque(false);
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.setOpaque(false);
        pane.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        String[] todos = ui.getApplication().todoToday();

        if (todos.length == 0) {
            JLabel title = ui.generateLabel("Everyone is available today with no task.");
            pane.add(title);
        } else {
            for (String s : todos) {
                ArrayList<String> partsOfLine = splitOnSpace(s);
                JLabel todo = ui.generateLabel(partsOfLine.get(0));
                todo.setFont(new Font("Avenir", Font.BOLD, 13));
                pane.add(todo);
                todo.setAlignmentX(LEFT_ALIGNMENT);
                todo.setAlignmentY(TOP_ALIGNMENT);
                JLabel task = ui.generateLabel(partsOfLine.get(1));
                task.setFont(new Font("Avenir", Font.BOLD, 13));
                pane.add(task);
                task.setAlignmentX(LEFT_ALIGNMENT);
                task.setAlignmentY(TOP_ALIGNMENT);
                pane.add(Box.createRigidArea(new Dimension(0, 5)));
            }
            pane.add(Box.createRigidArea(new Dimension(0,10)));
            JLabel title = ui.generateText("Everyone else is available today with no task.");
            pane.add(title);
        }

        return availability;
    }

    private static ArrayList<String> splitOnSpace(String line) {
        String[] splits = line.split(" , ");
        return new ArrayList<>(Arrays.asList(splits));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ui.setContentPane(new NewSchedulePanel(ui));
        ui.validate();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ui.generateBackground(g);
    }
}
