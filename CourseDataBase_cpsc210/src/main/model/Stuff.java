package model;

import model.interfaces.CourselistObserver;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Stuff implements CourselistObserver {
    private int stuffid;
    private String password;
    private List<SuperCourse> courseformanage = new ArrayList<>();

    public Stuff(int id,String password) {
        this.stuffid = id;
        this.password = password;
    }

    public int getStuffid() {
        return stuffid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String newpassword) {
        this.password = newpassword;
    }

    //MODIFIES:this, SuperCourse c
    //EFFECTS: add the supercourse to the courselist and add this stuff to the course manage list.
    public void addCourse(SuperCourse c) {
        if (!this.courseformanage.contains(c)) {
            courseformanage.add(c);
            c.addStuff(this);
        }
    }

    //MODIFIES:this, SuperCourse c
    //EFFECTS: remove the supercourse to the courselist and remove this stuff to the course manage list.
    public void removeCourse(SuperCourse c) {
        if (this.courseformanage.contains(c)) {
            courseformanage.remove(c);
            c.removeStuff(this);
        }
    }

    //EFFECTS: return the string of all the course this stuff is managing.
    public String getmanagecourses() {
        StringBuilder toreturn = new StringBuilder();
        for (SuperCourse superCourse : courseformanage) {
            toreturn.append(superCourse.getName() + superCourse.getCoursenumber() + ",");
        }
        return toreturn.toString();
    }

    public List<SuperCourse> getcourseformanage() {
        return courseformanage;
    }

    @Override
    //EFFECTS: Let two objects to be equal if both ID and password is the same.
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Stuff stuff = (Stuff) o;
        return stuffid == stuff.stuffid
                && password.equals(stuff.password);
    }


    //EFFECTS: return the objects unique hashcoude.
    @Override
    public int hashCode() {
        return Objects.hash(stuffid, password);
    }

    @Override
    //EFFECTS: update the information when the course is added.
    public void update(SuperCourse a) {
        Date d = new Date();
        System.out.print("StuffID--" + stuffid + " says:");
        System.out.println(" The course " + a.getName() + "-" + a.getCoursenumber() + " has been added to"
                + " the system");
        System.out.println("Time record: " + d);
    }
}