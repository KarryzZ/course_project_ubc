package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class SuperCourse {
    private String coursename;
    private int numberofstudent;
    private String prof;
    private double avegrade;
    private double highestgrade;
    private int coursenumber;

    private List<Stuff> stuffforcourse = new ArrayList<>();



    public SuperCourse(String name, int num, String prof, double ag, double hg, int numstu) {
        this.coursename = name;
        this.coursenumber = num;
        this.numberofstudent = numstu;
        this.prof = prof;
        this.avegrade = ag;
        this.highestgrade = hg;
    }

    //EFFECTS: return the name of the course.
    public String getName() {
        return coursename;
    }

    //EFFECTS: return the course number.
    public int getCoursenumber() {
        return coursenumber;
    }

    //EFFECTS: return the name of the professor in this course.
    public String getProf() {
        return prof;
    }

    //EFFECTS: return the average grade of this course.
    public double getAve_grade() {
        return avegrade;
    }

    //EFFECTS: return the highest grade of this course.
    public double getH_grade() {
        return highestgrade;
    }

    //EFFECTS: return the number of student in this course.
    public int getN_student() {
        return numberofstudent;
    }

    //MODIFIES:this
    //EFFECTS: set the new highest grade in this course.
    public void update_hgrade(double hg) {
        this.highestgrade = hg;
    }

    public List<Stuff> getstufforcourse() {
        return stuffforcourse;
    }

    public abstract String update();

    //Requires: the average and highest grades is below 100,
    //EFFECTS: return true if the course is consider a easy course with certain criteria.
    public boolean isEasy() {
        return (highestgrade >= 90 && numberofstudent >= 100 && avegrade >= 80);
    }

    //EFFECTS: return the string of the listofcourse that the observer manage.
    public String getstuff() {
        StringBuilder toreturn = new StringBuilder();
        for (int t = 0; t < stuffforcourse.size(); t++) {
            toreturn.append(stuffforcourse.get(t).getStuffid() + ",");
        }
        return toreturn.toString();
    }

    //MODIFIES:the string representation of the Course object.
    //EFFECTS: override the toString to allow print the course datatype.
    @Override
    public abstract String toString();

    //MODIFIES: this, stuff s
    //EFFECTS: if the list does not contain stuff s
    //add the stuff to the stuffforcourse list and stuff to courseformanage
    public void addStuff(Stuff s) {
        if (!this.stuffforcourse.contains(s)) {
            stuffforcourse.add(s);
            s.addCourse(this);
        }
    }

    //MODIFIES: this, stuff s
    //EFFECTS: if list contain stuff s,
    //remove the stuff to the stuffforcourse list and stuff to courseformanage
    public void removeStuff(Stuff s) {
        if (this.stuffforcourse.contains(s)) {
            stuffforcourse.remove(s);
            s.removeCourse(this);
        }
    }

    //MODIFIES: object o
    //EFFECTS: override equals to make object Supercourse with same course number and course name to be same.
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SuperCourse that = (SuperCourse) o;
        return coursenumber == that.coursenumber
                && coursename.equals(that.coursename)
                && prof.equals(that.prof);
    }

    @Override
    //EFFECTS: return the hashcode.
    public int hashCode() {
        return Objects.hash(coursename, prof, coursenumber);
    }
}

