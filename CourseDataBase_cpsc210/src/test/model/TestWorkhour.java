package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestWorkhour {
    private Workhour workhourtest;
    private Stuff teststuff1;

    @BeforeEach
    public void setup() {
        workhourtest = Workhour.getInstance();
        teststuff1 = new Stuff(123,"abc");
    }

    @Test
    public void test() {
        workhourtest.addStuffhour(teststuff1,20);
        assertTrue(workhourtest.contain(20));
        assertEquals(20,workhourtest.getWorkhour(teststuff1));
        workhourtest.addWorkhour(teststuff1,21);
        assertEquals(41,workhourtest.getWorkhour(teststuff1));
    }
}
