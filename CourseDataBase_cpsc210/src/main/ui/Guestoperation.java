package ui;

import model.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Guestoperation extends JFrame implements ActionListener {
    private JLabel courselabel;
    private JLabel numberlabel;
    private JLabel courseinfolabel;
    private JTextField coursefields;
    private JTextField numberfields;

    private JTextField textFieldforadd;

    private JTextArea jta;

    private Scanner scanner;
    private CoursesInput demo;
    CourseList courselist;
    Stufflist stufflist;
    Stuff stuff;
    Workhour workhour;
    boolean isStuff;

    Guestoperation() throws IOException {
        super("Find Course");
        scanner = new Scanner(System.in);
        courselist = new CourseList();
        stufflist = new Stufflist();
        workhour = Workhour.getInstance();
        stufflist.load(Filemanager.stufffile());
        courselist.load(Filemanager.coursefile());
        courselist.loadStuffasObserver(stufflist);
        demo = new CoursesInput(courselist);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(400, 180));
        setLayout(new FlowLayout());


        //Set up find operation on GUI
        JButton find = new JButton("Find");
        find.setActionCommand("myfind");
        find.addActionListener(this);

        courselabel = new JLabel("Course Name");
        numberlabel = new JLabel("Course Number");
        courseinfolabel = new JLabel("");
        coursefields = new JTextField(5);
        numberfields = new JTextField(5);
        add(courselabel);
        add(coursefields);
        add(numberlabel);
        add(numberfields);
        add(find);
        add(courseinfolabel);

        //set up back buttom
        JButton back = new JButton("back");
        back.setActionCommand("myback");
        back.addActionListener(this);
        add(back,BorderLayout.AFTER_LAST_LINE);


        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);


    }

    //MODEFIES: ActionEvent e
    //EFFECTS:this is the method that runs when Swing registers an action on an element
    //for which this class is an ActionListener
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("myfind")) {
            playSound("./data/Mouse_click.wav");
            myfindaction();
        } else {
            playSound("./data/Mouse_click.wav");
            dispose();
        }
    }

    private void myfindaction() {
        String coursename = coursefields.getText().toLowerCase();
        String coursenumber = numberfields.getText();
        courseinfolabel.setText(findcourse(coursename, coursenumber));
    }


    private String findcourse(String coursename,String coursenumber) {
        if (courselist.containcourse(coursename,Integer.parseInt(coursenumber))) {
            SuperCourse c = courselist.find_course(coursename,Integer.parseInt(coursenumber));
            return c.toString();
        } else {
            return "No such course.";
        }
    }

    private void playSound(String soundName) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }
}
