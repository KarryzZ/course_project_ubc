package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestStuff {
        private Stuff teststuff;

        @BeforeEach
        public void setup() {
            teststuff = new Stuff(123, "aaa");
        }

        @Test
        public void testconstructor() {
            assertEquals(123, teststuff.getStuffid());
            assertEquals("aaa", teststuff.getPassword());
        }

        @Test
        public void testsetpassword() {
            assertEquals("aaa", teststuff.getPassword());
            teststuff.setPassword("def");
            assertEquals("def", teststuff.getPassword());
        }

        @Test
        public void testaddItem() {
            SuperCourse course1 = new UndergrateCourse("Math",
                    341, "Josh", 70.0, 96.0, 59);
            SuperCourse course2 = new GraduateCourse("Math", 226, "LABA", 85, 100.0, 120);
            teststuff.addCourse(course1);
            teststuff.addCourse(course2);
            assertEquals("Math341,Math226,",teststuff.getmanagecourses());
            assertEquals("aaa", course1.getstufforcourse().get(0).getPassword());
            assertEquals(123, course2.getstufforcourse().get(0).getStuffid());
            teststuff.removeCourse(course2);
            assertFalse(teststuff.getcourseformanage().contains(course2));
            assertFalse(course2.getstufforcourse().contains(teststuff));
        }

        @Test
        public void testequals() {
            assertFalse(teststuff.equals(null));
            assertFalse(teststuff.equals(new CourseList()));
        }
    }
