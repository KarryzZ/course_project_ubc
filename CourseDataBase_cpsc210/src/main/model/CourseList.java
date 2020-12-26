/*
   @Source: https://github.students.cs.ubc.ca/CPSC210/IntegerSetLecLab model.IntegerSet
**/

package model;

import model.interfaces.Loadable;
import model.interfaces.Savable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CourseList extends Subject implements Loadable, Savable {
    private List<SuperCourse> courseList;

    //EFFECTS: construct a new  to be an ArrayList and create twcourseso files.
    public CourseList() {
        courseList = new ArrayList<>();
    }

    //EFFECTS: Check whether the given name of the course contained in courses.
    public boolean containcourse(String name, int num) {
        for (int i = 0; i < courseList.size(); i++) {
            String s = courseList.get(i).getName();
            int n = courseList.get(i).getCoursenumber();
            if (s.equals(name) && n == num) {
                return true;
            }
        }
        return false;
    }


    //REQUIRES:the courses not empty
    //EFFECTS: Find the given course in courses with the given course name.
    public SuperCourse find_course(String name, int num) {
        for (SuperCourse cours : courseList) {
            String s = cours.getName();
            int n = cours.getCoursenumber();
            if (s.equals(name) && n == num) {
                return cours;
            }
        }
        return null;
    }

    //REQUIRES:
    //MODIFIES: this and the course parameter.
    //EFFECTS: add course to the courses and notify the observers.
    public void addCourse(SuperCourse c) {
        courseList.add(c);
        notifyObservers(c);
    }


    //REQUIRES:
    //MODIFIES: this and the course parameter.
    //EFFECTS: remove course to the courses.
    public void remove(SuperCourse c) {
        courseList.remove(c);
    }

    public boolean contain(SuperCourse c) {
        return courseList.contains(c);
    }

    //MODIFIES: File f
    //EFFECTS: save the coursesinput course(s) to Courses_save.txt
    @Override
    public void save(File f) throws IOException {
        List<String> linesofcourse = new ArrayList<>();
        for (SuperCourse c : courseList) {
            linesofcourse.add(c.getName() + ","
                    + c.getCoursenumber() + ","
                    + c.getProf() + ","
                    + c.getAve_grade() + ","
                    + c.getH_grade() + ","
                    + c.getN_student());
            Files.write(f.toPath(), linesofcourse);
        }
    }

    //MODIFIES: this
    //EFFECTS: load all the courses in the Courses_save.txt file.
    @Override
    public void load(File f) throws IOException {
        List<String> linesofcourse = Files.readAllLines(f.toPath());
        for (String s : linesofcourse) {
            String[] input = s.split(",");
            if (Integer.parseInt(input[1]) >= 500) {
                SuperCourse course = new GraduateCourse(input[0], Integer.parseInt(input[1]),
                        input[2], Double.parseDouble(input[3]), Double.parseDouble(input[4]),
                        Integer.parseInt(input[5]));
                courseList.add(course);
            } else {
                SuperCourse course = new UndergrateCourse(input[0], Integer.parseInt(input[1]),
                        input[2], Double.parseDouble(input[3]), Double.parseDouble(input[4]),
                        Integer.parseInt(input[5]));
                courseList.add(course);
            }
        }
    }



    //EFFECTS: return the course in the courses at index i.
    public SuperCourse get(int i) {
        return courseList.get(i);
    }

    //EFFECTS: return the size of the courses.
    public int size() {
        return courseList.size();
    }

    //EFFECTS: return true if the given Course(with the same name and coursenumber) is in this list.
    public boolean isInlist(SuperCourse c) {
        boolean hasduplicate = false;
        for (SuperCourse course : courseList) {
            if (c.equals(course)) {
                hasduplicate = true;
                break;
            }
        }
        return hasduplicate;
    }

    //MODIFIE:Nothing
    //EFFECTS: return true if the given Course(with the same name and coursenumber  is in this list.
    public void loadStuffasObserver(Stufflist stufflist) {
        Iterator<Stuff> iter = stufflist.iterator();
        while (iter.hasNext()) {
            addObserver(iter.next());
        }
    }
}


