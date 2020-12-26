package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestCourseList {
    private CourseList courselist;
    private UndergrateCourse course1;
    private SuperCourse testcourseforload1;
    private SuperCourse testcourseforload2;

    @BeforeEach
    public void setup() {
        courselist = new CourseList();
        course1 = new UndergrateCourse("Math" ,341,"Josh",70.4,96.0,59);
    }

    @Test
    public void testcourses() {
//        assertFalse(courses.containcourse("Math",341));
//        assertEquals(null, courses.find_course("Math",341));
        courselist.addCourse(course1);
        assertTrue(courselist.containcourse("Math",341));
        assertFalse(courselist.containcourse("EOSC",341));
        assertFalse(courselist.containcourse("Math",199));
        assertEquals(course1, courselist.find_course("Math",341));
        assertEquals(null, courselist.find_course("EOSC",320));
        assertEquals(null, courselist.find_course("Math",320));
        UndergrateCourse course2 = new UndergrateCourse("Math",320,"Karry",65.1,98.0,57);
        courselist.addCourse(course2);
        assertEquals(course2, courselist.find_course("Math",320));
        assertEquals(null, courselist.find_course("Math",399));
        assertEquals(2, courselist.size());
        assertSame(course1, courselist.get(0));
        SuperCourse course3 = new GraduateCourse("Math",612,"Tony",68.1,97.0,40);
        courselist.addCourse(course3);
//        assertEquals("A graduate course has been added to the system", courselist.get(2).update());
//        assertEquals("An undergrate course has been added to the system", courselist.get(1).update());
        courselist.remove(course3);
        assertFalse(courselist.contain(course3));
    }

    @Test
    public void testSave() throws IOException {
        SuperCourse course2 = new GraduateCourse("Cpsc" ,531,"Ying",79.2,95.0,13);
        courselist.addCourse(course1);
        courselist.addCourse(course2);
        File file = new File("./data/Courses_testsave.txt");
        courselist.save(file);
        List<String> lines =Files.readAllLines(Paths.get("./data/Courses_testsave.txt"));
        assertEquals("Cpsc,531,Ying,79.2,95.0,13", lines.get(courselist.size() -1 ));
    }

    @Test
    public void testLoad() throws IOException {
        File file = new File("./data/Courses_testload.txt");
        List<String> lines =new ArrayList<>();
        lines.add("Cpsc,510,Elisa,72.0,100.0,172");
        lines.add("Cpsc,310,George,71.0,96.0,121");
        SuperCourse testcourse1 = new GraduateCourse("Cpsc",510, "Elisa",72.0,100.0,172);
        SuperCourse testcourse2 = new UndergrateCourse("Cpsc",310, "George",71.0,96.0,121);
        Files.write(file.toPath(),lines);
        courselist.load(file);
        assertEquals(testcourse1.getName(), courselist.get(0).getName());
        assertEquals(testcourse1.getCoursenumber(), courselist.get(0).getCoursenumber());
        assertEquals(testcourse1.getProf(), courselist.get(0).getProf());
        assertEquals(testcourse1.getAve_grade(), courselist.get(0).getAve_grade());
        assertEquals(testcourse1.getH_grade(), courselist.get(0).getH_grade());
        assertEquals(testcourse1.getN_student(), courselist.get(0).getN_student());

        assertEquals(testcourse2.getName(), courselist.get(1).getName());
        assertEquals(testcourse2.getCoursenumber(), courselist.get(1).getCoursenumber());
        assertEquals(testcourse2.getProf(), courselist.get(1).getProf());
        assertEquals(testcourse2.getAve_grade(), courselist.get(1).getAve_grade());
        assertEquals(testcourse2.getH_grade(), courselist.get(1).getH_grade());
        assertEquals(testcourse2.getN_student(), courselist.get(1).getN_student());
    }

    @Test
    public void testdup() {
        SuperCourse dupcourse = new UndergrateCourse("Math",341,"Josh",65,97,53);
        assertFalse(courselist.isInlist(course1));
        courselist.addCourse(course1);
        assertTrue(courselist.isInlist(dupcourse));
    }

    @Test
    public void testAddRemoveObserver() {
        Stuff stuff1 = new Stuff(123,"karry123");
        Stuff stuff2 = new Stuff(124,"dora124");
        courselist.addObserver(stuff1);
        courselist.addObserver(stuff2);
        courselist.addObserver(stuff1);
        assertEquals(stuff1,courselist.courselistObservers.get(0));
        assertEquals(stuff2,courselist.courselistObservers.get(1));
        courselist.removeObserver(stuff1);
        courselist.removeObserver(stuff1);
        assertEquals(stuff2,courselist.courselistObservers.get(0));
    }
}
