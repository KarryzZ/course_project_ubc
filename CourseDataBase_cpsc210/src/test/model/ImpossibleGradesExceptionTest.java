package model;

import org.junit.jupiter.api.BeforeEach;

public class ImpossibleGradesExceptionTest {
    private CourseList courselist;
    private SuperCourse course1;
    private SuperCourse course2;
    private SuperCourse course3;
    private SuperCourse course4;

    @BeforeEach
    public void setup() {
        courselist = new CourseList();
        course1 = new UndergrateCourse("Math", 341, "Josh", -30, 96.0, 59);
        course2 = new UndergrateCourse("Math", 342, "Josh", 70, 120, 59);
        course3 = new GraduateCourse("phil", 120, "Dora", 83.3, 98, -50);
        course4 = new GraduateCourse("phil", 220,  "Dora", 83.3, 100, 210);
        courselist.addCourse(course1);
        courselist.addCourse(course2);
        courselist.addCourse(course3);
    }
}
