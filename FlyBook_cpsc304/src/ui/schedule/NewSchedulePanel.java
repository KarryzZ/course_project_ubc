package ui.schedule;

import model.schedule.ScheduleRecord;
import ui.UI;
import ui.utilities.ErrorMessage;
import ui.utilities.Header;
import ui.utilities.SuccessMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.Objects;

public class NewSchedulePanel extends JPanel implements ActionListener {
    UI ui;

    private final int TEXT_SPACE = 16;
    private final int ENTRY_SPACE = 10;
    int monthVal;

    JComboBox yearEntry;
    JComboBox monthEntry;
    JComboBox dateEntry;
    DefaultComboBoxModel date1 = new DefaultComboBoxModel(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
            "21", "22", "23", "24", "25", "26", "27", "28", "29"});
    DefaultComboBoxModel date2 = new DefaultComboBoxModel(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
            "21", "22", "23", "24", "25", "26", "27", "28"});
    DefaultComboBoxModel date3 = new DefaultComboBoxModel(new String[]{"1","2","3","4","5","6","7","8","9","10",
            "11","12","13","14","15","16","17","18","19","20",
            "21","22","23","24","25","26","27","28","29","30","31"});
    DefaultComboBoxModel date4 = new DefaultComboBoxModel(new String[]{"1","2","3","4","5","6","7","8","9","10",
            "11","12","13","14","15","16","17","18","19","20",
            "21","22","23","24","25","26","27","28","29","30"});

    JTextField hourEntry = new JTextField(10);
    JTextField minuteEntry = new JTextField(10);
    JTextField eventField = new JTextField(10);

    public NewSchedulePanel(UI ui) {
        this.ui = ui;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // add header
        add(new Header(ui, "New Schedule"));
        add(Box.createRigidArea(new Dimension(0,20)));

        // a sub-panel for schedule detail
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.X_AXIS));
        info.setOpaque(false);
        info.add(textPanel());
        info.add(Box.createRigidArea(new Dimension(20,0)));
        info.add(entryPanel());

        add(info);
        info.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalGlue());
        add(Box.createRigidArea(new Dimension(0,380)));

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

        JLabel date = ui.generateLabel("Date");
        date.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.add(date);
        textPanel.add(Box.createRigidArea(new Dimension(0, TEXT_SPACE)));

        JLabel time = ui.generateLabel("Time");
        time.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.add(time);
        textPanel.add(Box.createRigidArea(new Dimension(0, TEXT_SPACE)));

        JLabel event = ui.generateLabel("Event");
        event.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.add(event);

        return textPanel;
    }

    private JPanel entryPanel() {
        JPanel entryPanel = new JPanel();
        entryPanel.setLayout(new BoxLayout(entryPanel, BoxLayout.Y_AXIS));
        entryPanel.setOpaque(false);

        JPanel date = new JPanel();
        date.setLayout(new BoxLayout(date, BoxLayout.X_AXIS));
        date.setOpaque(false);
        yearEntry = new JComboBox(new String[]{"2020","2021"});
        yearEntry.setMaximumSize(yearEntry.getPreferredSize());
        date.add(yearEntry);
        monthEntry = new JComboBox(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12"});
        monthEntry.addActionListener(e -> {
            String s = (String) Objects.requireNonNull(monthEntry.getSelectedItem());
            monthVal = Integer.parseInt(s);
            if (monthVal == 2) {
                if (Objects.requireNonNull(yearEntry.getSelectedItem()).equals("2020")) {
                    dateEntry.setModel(date1);
                } else {
                    dateEntry.setModel(date2);
                }
            } else if (monthVal == 1 || monthVal == 3 || monthVal == 5 || monthVal == 7 || monthVal == 8 || monthVal == 10 || monthVal == 12) {
                dateEntry.setModel(date3);
            } else {
                dateEntry.setModel(date4);
            }
            dateEntry.repaint();
            dateEntry.revalidate();
        });
        monthEntry.setMaximumSize(monthEntry.getPreferredSize());
        dateEntry = new JComboBox(new String[]{"1","2","3","4","5","6","7","8","9","10",
                "11","12","13","14","15","16","17","18","19","20",
                "21","22","23","24","25","26","27","28","29","30","31"});
        dateEntry.setMaximumSize(dateEntry.getPreferredSize());
        date.add(monthEntry);
        date.add(dateEntry);

        entryPanel.add(date);
        date.setAlignmentX(LEFT_ALIGNMENT);
        entryPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        JPanel time = new JPanel();
        time.setLayout(new BoxLayout(time, BoxLayout.X_AXIS));
        time.setOpaque(false);
        hourEntry.setMaximumSize(new Dimension(40,40));
        time.add(hourEntry);
        time.add(ui.generateLabel(":"));
        minuteEntry.setMaximumSize(new Dimension(40,40));
        time.add(minuteEntry);

        entryPanel.add(time);
        time.setAlignmentX(LEFT_ALIGNMENT);
        entryPanel.add(Box.createRigidArea(new Dimension(0, ENTRY_SPACE)));

        eventField.setMaximumSize(new Dimension(157,40));
        entryPanel.add(eventField);
        eventField.setAlignmentX(LEFT_ALIGNMENT);

        return entryPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String year = (String) Objects.requireNonNull(yearEntry.getSelectedItem());
        String month = (String) Objects.requireNonNull(monthEntry.getSelectedItem());
        String date = (String) Objects.requireNonNull(dateEntry.getSelectedItem());
        String hour = hourEntry.getText();
        String minute = minuteEntry.getText();
        String event = eventField.getText();

        if (event.isEmpty()) {
            new ErrorMessage("Please enter the event detail.");
        } else {
            try {
                String time = year + "-" + month + "-" + date + " " + hour + ":" + minute + ":00";
                Timestamp timestamp = Timestamp.valueOf(time);
                int num = ui.getApplication().countSchedules() + 1;
                while (ui.getApplication().getPostByID("S" + num) != null) {
                    num++;
                }
                String sid = "S" + num;
                ScheduleRecord schedule = new ScheduleRecord (
                        sid,
                        timestamp,
                        event,
                        ui.getApplication().getCurrentUser()
                );
                ui.getApplication().addSchedule(schedule);
                new SuccessMessage("The scheduled event is created!");
                ui.switchPanel("Schedule");
            } catch (IllegalArgumentException exception) {
                new ErrorMessage("The event time is invalid.");
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ui.generateBackground(g);
    }
}
