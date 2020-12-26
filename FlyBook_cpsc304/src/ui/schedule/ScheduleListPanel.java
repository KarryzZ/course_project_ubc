package ui.schedule;

import main.Application;
import model.schedule.ScheduleRecord;
import ui.UI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ScheduleListPanel extends JPanel implements ItemListener {
    UI ui;

    JPanel cards;
    final static String TODAY = "Today";
    final static String WEEK = "Within a week";
    final static String ALL = "All";
    final static String PASSED = "Passed";

    public ScheduleListPanel(UI ui) {
        this.ui = ui;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);

        // a sub-panel stores the combobox
        JPanel comboBoxPane = new JPanel();
        comboBoxPane.setOpaque(false);
        String[] comboBoxItems = {TODAY, WEEK, ALL, PASSED};
        JComboBox cb = new JComboBox(comboBoxItems);
        cb.setEditable(false);
        cb.addItemListener(this);
        comboBoxPane.add(cb);

        // a sub-panel for cards
        cards = new JPanel(new CardLayout());
        cards.setOpaque(false);
        cards.add(card(TODAY), TODAY);
        cards.add(card(WEEK), WEEK);
        cards.add(card(ALL), ALL);
        cards.add(card(PASSED), PASSED);

        add(comboBoxPane, BorderLayout.PAGE_START);
        add(cards, BorderLayout.CENTER);
    }

    private JScrollPane card(String type) {
        JPanel card = new JPanel();
        JScrollPane scheduleList = new JScrollPane(card);
        scheduleList.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        // Get schedules based on different time period selected
        Application app = ui.getApplication();
        ScheduleRecord[] schedules;

        switch (type) {
            case TODAY:
                schedules = app.getSchedulesToday();
                break;
            case WEEK:
                schedules = app.getSchedulesThisWeek();
                break;
            case ALL:
                schedules = app.getSchedulesByID();
                break;
            default:
                schedules = app.getSchedulesPassed();
                break;
        }

        if (schedules.length == 0) {
            JLabel text = ui.generateLabel("There is no scheduled event in this time period.");
            card.add(text);
            text.setAlignmentX(CENTER_ALIGNMENT);
        } else {
            // Insert each schedule record
            for (ScheduleRecord scheduleRecord : schedules) {
                JPanel schedule = new ScheduleRecordPanel(ui, scheduleRecord);
                card.add(schedule);
                schedule.add(Box.createRigidArea(new Dimension(0, 20)));

                schedule.setAlignmentX(LEFT_ALIGNMENT);
                schedule.setAlignmentY(TOP_ALIGNMENT);
            }
        }

        return scheduleList;
    }

    public void itemStateChanged(ItemEvent evt) {
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, (String) evt.getItem());
    }
}