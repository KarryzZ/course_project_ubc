package model;

public class GraduateCourse extends SuperCourse {


    //EFFECT: construct Graduate course
    public GraduateCourse(String name, int num,  String prof, double ag, double hg,int numstu) {
        super(name,num,prof,ag,hg,numstu);
    }




    @Override
    public String toString() {
        return "<html>GraduateCourse:" + this.getName() + this.getCoursenumber()
                + "<br/>Professor:" + this.getProf()
                + "<br/>Average:" + this.getAve_grade()
                + "<br/>Highest Score:" + this.getH_grade()
                + "<br/>Number of student:" + this.getN_student()
                + "<br/>Is it a \"easy\" course? " + (this.isEasy() ? "Yes" : "No")
                + "<html>";
    }

    //EFFECTS: the information for a added new Graduate course.
    @Override
    public String update() {
        return "A graduate " + getName() + " course has been added.";
    }


}
