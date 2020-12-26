package ui;

import model.exceptions.ImpossibleGradesException;
import model.exceptions.ImpossibleNumberException;
import model.GraduateCourse;
import model.SuperCourse;
import model.UndergrateCourse;
import model.CourseList;
import ui.exception.DuplicateCourseException;
import ui.exception.NotOnlyAlphException;

import javax.swing.*;
import java.util.Scanner;


public class CoursesInput {
    private CourseList courselist;
    private Scanner scanner;

    private String coursefield;
    private int courselevel;
    private String professor;
    private double averagescore;
    private double highestscore;
    private int numberofstudent;


    public CoursesInput(CourseList courselist) {
        this.courselist = courselist;
        scanner = new Scanner(System.in);
    }



//    public void inputData() throws NotOnlyAlphException,ImpossibleNumberException {
//        userinput();
//        SuperCourse course;
//        if (courselevel >= 500) {
//            course = new GraduateCourse(coursefield, courselevel,professor,averagescore,
//            highestscore, numberofstudent);
//        } else {
//            course = new UndergrateCourse(coursefield, courselevel, professor, averagescore,
//                    highestscore, numberofstudent);
//        }
//        if (courselist.isInlist(course)) {
//            System.out.println("Addition fail, the course is already in the system.");
//        } else {
//            courselist.addCourse(course);
////            System.out.println(course.update());
//        }
//    }

    //REQUIRES:
    //MODIFIES: this
    //EFFECTS: load all the input information on course.
    public void inputdata(String text) throws DuplicateCourseException,
            NullPointerException, NotOnlyAlphException, ImpossibleNumberException {
        userinputdata(text);
        checkexpection();
        SuperCourse course;
        if (courselevel >= 500) {
            course = new GraduateCourse(coursefield, courselevel,professor,averagescore, highestscore, numberofstudent);
        } else {
            course = new UndergrateCourse(coursefield, courselevel, professor, averagescore,
                    highestscore, numberofstudent);
        }
        if (courselist.isInlist(course)) {
            throw new DuplicateCourseException();
        } else {
            courselist.addCourse(course);
            JOptionPane.showMessageDialog(null, course.update());
        }
    }

    private void userinputdata(String text) {
        String[] input = text.split(",");
        coursefield = input[0];
        courselevel = Integer.parseInt(input[1]);
        professor = input[2];
        averagescore = Double.parseDouble(input[3]);
        highestscore = Double.parseDouble(input[4]);
        numberofstudent = Integer.parseInt(input[5]);
    }

    private void inputcoursename() throws ImpossibleNumberException {
        System.out.println("please enter the name of the course(e.g. Math,100):");
        String coursewithname = scanner.next();
        String[] input = coursewithname.split(",");
        coursefield = input[0].toLowerCase();

        courselevel = Integer.parseInt(input[1]);
        if (courselevel <= 0) {
            throw new ImpossibleNumberException();
        }
    }

    private void checkexpection() throws NotOnlyAlphException, ImpossibleNumberException {
        boolean matcher = professor.matches("^[a-zA-Z]+$");
        if (!matcher) {
            throw new NotOnlyAlphException();
        }
        if (averagescore > 100.0 || averagescore < 0.0) {
            throw new ImpossibleGradesException();
        }
        if (highestscore > 100.0 || highestscore < 0.0) {
            throw new ImpossibleGradesException();
        }
        if (numberofstudent <= 0) {
            throw new ImpossibleNumberException();
        }
    }



}

