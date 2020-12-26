package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestStufflist {
    private Stufflist teststufflist;
    private Stuff testStuff1;

    @BeforeEach
    public void setup2() throws IOException {
        teststufflist = new Stufflist();
        testStuff1 = new Stuff(123, "tonytony");
    }

    @Test
    public void teststufflist() {
        assertFalse(teststufflist.contain(123));
        assertFalse(teststufflist.isCorrect(123,"tonytony"));
        teststufflist.add(testStuff1);
        assertTrue(teststufflist.isCorrect(123,"tonytony"));
        assertEquals(1, teststufflist.size());
        assertEquals(testStuff1, teststufflist.get(0));
        assertTrue(teststufflist.contain(123));
        assertFalse(teststufflist.contain(000));
        assertTrue(teststufflist.contain(testStuff1));
    }



    @Test
    public void testSave() throws IOException {
        File file = new File("./data/StuffTestsave.txt");
        Stuff testStuff2 = new Stuff(345, "dwdw");
        Stuff testStuff3 = new Stuff(678, "xdd");
        teststufflist.add(testStuff2);
        teststufflist.add(testStuff3);
        teststufflist.save(file);
        List<String> lines = Files.readAllLines(Paths.get("./data/StuffTestsave.txt"));
        assertEquals("345 dwdw", lines.get(0));
        assertEquals("678 xdd", lines.get(1));
    }

    @Test
    public void testLoad() throws IOException {
        File file = new File("./data/StuffTestload.txt");
        List<String> lines = new ArrayList<>();
        lines.add("123,tonytony");
        lines.add("456,karrykarry");
        Files.write(file.toPath(), lines);
        teststufflist.load(file);
        assertEquals(123, teststufflist.get(0).getStuffid());
        assertEquals("tonytony", teststufflist.get(0).getPassword());
    }

    @Test
    public void testGetstuff() {
        assertEquals(null, teststufflist.getStuff(0,""));
        teststufflist.add(testStuff1);
        assertEquals(null, teststufflist.getStuff(0,""));
        testStuff1.equals(teststufflist.getStuff(123,"tonytony"));
    }


}
