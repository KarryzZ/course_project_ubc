package model;

import model.interfaces.CourselistObserver;

import java.util.ArrayList;
import java.util.List;

public abstract class Subject {
    protected List<CourselistObserver> courselistObservers;

    public Subject() {
        courselistObservers = new ArrayList<>();
    }

    //MODIFIES: this
    //EFFECTS:if the courselistobserver does not contain the CourselistObserver, add it to the list.
    public void addObserver(CourselistObserver o) {
        if (!courselistObservers.contains(o)) {
            courselistObservers.add(o);
        }
    }

    //MODIFIES: this
    //EFFECTS:if the courselistobserver does contain the CourselistObserver, remove it to the list.
    public void removeObserver(CourselistObserver o) {
        courselistObservers.remove(o);
    }

    //MODIFIES: nothing
    //EFFECTS: notify all the observers when a new course was added.
    public void notifyObservers(SuperCourse c) {
        for (CourselistObserver o : courselistObservers) {
            o.update(c);
        }
    }
}
