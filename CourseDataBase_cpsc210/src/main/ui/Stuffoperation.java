package ui;

import model.*;
import model.exceptions.ImpossibleNumberException;
import ui.exception.DuplicateCourseException;
import ui.exception.NotOnlyAlphException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Stuffoperation extends JFrame implements ActionListener {
    private JLabel courselabel;
    private JLabel numberlabel;
    private JLabel courseinfolabel;
    private JTextField coursefields;
    private JTextField numberfields;

    private JTextField textFieldforadd;

    private JTextArea textArea;


    private JScrollPane scrollPane;
    private JPanel mainPanel;
    private Scanner scanner;
    private CoursesInput demo;
    CourseList courselist;
    Stufflist stufflist;
    Stuff stuff;
    Workhour workhour;
    boolean isStuff;

    Stuffoperation() throws IOException {
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
        setPreferredSize(new Dimension(400, 450));
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

        //Set up the Add operation on GUI
        textFieldforadd = new JTextField(30);
        JLabel addlabel = new JLabel(
                "<html>Enter the Course information,Seprated with comma:"
                        + "<br/>(e.g. math,100,Karry,76.8,100.0,128)<html>");
        JButton add = new JButton("Add Course");
        add.setActionCommand("myadd");
        add.addActionListener(this);
        add(addlabel);
        add(textFieldforadd);
        add(add);


        //Set up the save opeartion on GUi
        JButton save = new JButton("save");
        save.setActionCommand("mysave");
        save.addActionListener(this);
        add(save);


        //set up Courselist display on GUI
        JButton check  = new JButton("Courselist");
        check.setActionCommand("mycheck");
        check.addActionListener(this);
        add(check);

        //set up course display on GUI.
        mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(10,10,10,10));
        add(mainPanel);
        scrollPane = new JScrollPane();
        mainPanel.add(scrollPane);
        textArea = new JTextArea(Filemanager.getcoursetxtfile());
        //scrollPane.add(textArea);
        scrollPane.setPreferredSize(new Dimension(220, 150));
        scrollPane.setViewportView(textArea);

        //set up return
        JButton back = new JButton("back");
        back.setActionCommand("myback");
        back.addActionListener(this);
        add(back,BorderLayout.AFTER_LAST_LINE);

//        JButton courselist = new JButton("Checklist");
//        courselist.setActionCommand("mycourselist");
//        courselist.addActionListener(this);
//        add(courselist);
        mainPanel.setVisible(false);
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
            myfindaction();
        } else if (e.getActionCommand().equals("myadd")) {
            myaddaction();
        } else if (e.getActionCommand().equals("mysave")) {
            mysaveaction();
        } else if (e.getActionCommand().equals("mycheck")) {
            mainPanel.setVisible(true);
        } else {
            playSound("./data/Mouse_click.wav");
            try {
                new Gui();
                dispose();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void myfindaction() {
        playSound("./data/Mouse_click.wav");
        String coursename = coursefields.getText().toLowerCase();
        String coursenumber = numberfields.getText();
        courseinfolabel.setText(findcourse(coursename, coursenumber));
    }

    private void myaddaction() {
        playSound("./data/Mouse_click.wav");
        try {
            demo.inputdata(textFieldforadd.getText());

        } catch (ArrayIndexOutOfBoundsException ex) {
            JOptionPane.showMessageDialog(null, "Please add accroding to the given Format.",
                    "Warnings",JOptionPane.WARNING_MESSAGE,null);
        } catch (DuplicateCourseException ex) {
            JOptionPane.showMessageDialog(null, "Course already in the system.",
                    "Warnings",JOptionPane.WARNING_MESSAGE,null);
        } catch (NotOnlyAlphException e) {
            JOptionPane.showMessageDialog(null, "Professor's name is invalid ",
                    "Warnings",JOptionPane.ERROR_MESSAGE,null);
        } catch (ImpossibleNumberException e) {
            JOptionPane.showMessageDialog(null, "some of the grades are impossible!",
                    "Warnings",JOptionPane.ERROR_MESSAGE,null);
        }
    }

    private void mysaveaction() {
        playSound("./data/Mouse_click.wav");
        try {
            courselist.save(Filemanager.coursefile());
            textArea.setText(Filemanager.getcoursetxtfile());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }



    private void processaccountaction() {
        while (true) {
            System.out.println("Enter here for workhour or worklist or quit");
            String operation = scanner.nextLine().toLowerCase();
            if (operation.equals("workhour")) {
                manageworkhour();
            } else if (operation.equals("worklist")) {
                manageAddRemoveCheck();
            } else if (operation.equals("quit")) {
                break;
            }
        }
    }


/*    protected void processaction() throws IOException {
        while (true) {
            printcoursemanage();
            String operation = scanner.nextLine().toLowerCase();
            if (operation.equals("quit")) {
                break;
            } else if (operation.equals("find")) {
                findcourse(courselist);
            } else if (operation.equals("add") && isStuff) {
                addoperation();
            } else if (operation.equals("save") && isStuff) {
                courselist.save(filemanager.coursefile());
            } else {
                System.out.println("Invalid Input!");
            }
        }
    }*/

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

    private String findcourse(String coursename,String coursenumber) {
        if (courselist.containcourse(coursename,Integer.parseInt(coursenumber))) {
            SuperCourse c = courselist.find_course(coursename,Integer.parseInt(coursenumber));
            return c.toString();
        } else {
            String nosuchcourse = "No such course.";
            return nosuchcourse;
        }
    }

    private void manageAddRemoveCheck() {
        while (true) {
            System.out.println("add or remove or check or quit:");

            String scan = scanner.nextLine();

            if (scan.equals("add") || scan.equals("remove")) {
                addremovesupervision(scan);
            } else if (scan.equals("check")) {
                System.out.println(stuff.getmanagecourses());
            } else if (scan.equals("quit")) {
                break;
            }
        }
    }



    private void addremovesupervision(String operation) {
        System.out.println("Please enter the course for " + operation + "supervising:");
        String coursename = scanner.nextLine();
        String[] input = coursename.split(" ");
        if (operation.equals("add")) {
            stuff.addCourse(courselist.find_course(input[0], Integer.parseInt(input[1])));
        } else if (operation.equals("remove")) {
            stuff.removeCourse(courselist.find_course(input[0], Integer.parseInt(input[1])));
        }
        System.out.println("The course has been " + operation + "ed.");
    }



    private void manageworkhour() {
        while (true) {
            System.out.println("Enter here for add or check or quit");
            String wh = scanner.nextLine().toLowerCase();
            if (wh.equals("add")) {
                System.out.println("add the working hour:");
                String hour = scanner.nextLine();
                workhour.addWorkhour(stuff,Integer.parseInt(hour));
            } else if (wh.equals("check")) {
                System.out.println("the total working hour:" + workhour.getWorkhour(stuff));
            } else if (wh.equals("quit")) {
                break;
            }
        }
    }
}
