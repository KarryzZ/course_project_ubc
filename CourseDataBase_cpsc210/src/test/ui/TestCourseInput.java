package ui;


import model.CourseList;
import model.SuperCourse;
import model.UndergrateCourse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestCourseInput {
    private CoursesInput coursesInput;
    private CourseList courselist;

    @BeforeEach
    public void setup() {
        courselist = new CourseList();
        SuperCourse course = new UndergrateCourse("Math", 341, "Josh", 70.0, 96.0, 59);
        courselist.addCourse(course);
    }

    @Test
    public void testconstructor() {
        coursesInput = new CoursesInput(courselist);
    }
}
