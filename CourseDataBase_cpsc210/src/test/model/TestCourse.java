package model;


import model.exceptions.ImpossibleNumberException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCourse {
    private UndergrateCourse course1;
    private GraduateCourse course2;

    @BeforeEach
    public void setup() {
        course1 = new UndergrateCourse("Math", 341, "Josh", 70.0, 96.0, 59);
        course2 = new GraduateCourse("Math", 226, "LABA", 85, 100.0, 120);
    }

    @Test

    public void testcourse() throws ImpossibleNumberException {
        assertEquals(96.0, course1.getH_grade());
        course1.update_hgrade(98.0);
        assertEquals("Math", course1.getName());
        assertEquals(341, course1.getCoursenumber());
        assertEquals("Josh", course1.getProf());
        assertEquals(70.0, course1.getAve_grade());
        assertEquals(98.0, course1.getH_grade());
        assertEquals(59, course1.getN_student());
        assertTrue(course2.isEasy());
        assertFalse(course1.isEasy());
    }

    @Test
    public void testcoursetoString() {
        assertEquals("<html>UndergraduateCourse:Math341<br/>Professor:" +
                "Josh<br/>Average:70.0<br/>Highest Score:96.0<br/>Number of student:59" +
                        "<br/>Is it a \"easy\" course? No<html>", course1.toString());
        assertEquals("<html>GraduateCourse:Math226<br/>Professor:LABA<br/>" +
                "Average:85.0<br/>Highest Score:100.0<br/>Number of student:120" +
                        "<br/>Is it a \"easy\" course? Yes<html>", course2.toString());
    }

    @Test
    public void testIsEasy() throws ImpossibleNumberException {
        SuperCourse course3 = new UndergrateCourse("phil",220, "Dora", 76, 96.0, 220);
        SuperCourse course4 = new UndergrateCourse("phil", 320, "Dora", 85, 99, 99);
        SuperCourse course5 = new UndergrateCourse("phil", 420, "Dora", 79, 100, 99);
        SuperCourse course6 = new UndergrateCourse("phil", 130, "Dora", 79, 100, 101);
        SuperCourse course7 = new UndergrateCourse("phil", 230, "Dora", 85, 100, 99);
        SuperCourse course8 = new UndergrateCourse("phil", 330, "Dora", 83, 96, 140);
        assertFalse(course3.isEasy());
        assertFalse(course4.isEasy());
        assertFalse(course5.isEasy());
        assertFalse(course6.isEasy());
        assertFalse(course7.isEasy());
        assertTrue(course8.isEasy());
    }

    @Test
    public void testAddStuff() {
        Stuff stuff1 = new Stuff(1234,"karry");
        Stuff stuff2 = new Stuff(5678,"dora");
        course1.addStuff(stuff1);
        course1.addStuff(stuff2);
        assertEquals("1234,5678,",course1.getstuff());

    }

    @Test
    public void testhascode() {
        assertEquals(-1925594206,course1.hashCode());
    }

}
