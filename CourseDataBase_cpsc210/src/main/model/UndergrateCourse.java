package model;


public class UndergrateCourse extends SuperCourse {

    //EFFECTS:construct the undergrate course
    public UndergrateCourse(String name, int num, String prof,  double ag, double hg, int numstu) {
        super(name,num, prof, ag, hg, numstu);
    }


    @Override
    //EFFECTS: change the sout print for the object to print the courseinformation.
    public String toString() {
        return "<html>UndergraduateCourse:" + this.getName() + this.getCoursenumber()
                + "<br/>Professor:" + this.getProf()
                + "<br/>Average:" + this.getAve_grade()
                + "<br/>Highest Score:" + this.getH_grade()
                + "<br/>Number of student:" + this.getN_student()
                + "<br/>Is it a \"easy\" course? " + (this.isEasy() ? "Yes" : "No")
                + "<html>";
    }

    //EFFECTS: the information for a added new undergrad course.
    public String update() {
        return "A undergraduate " + getName() + " course has been added.";
    }
}
