package eu.simmig.maze;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MazeRuleTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testIsViolated() throws Exception {
        MazeRule rule = new MazeRule(MazeRule.COLOR_BOTH_NOT_BLACK);
        assertTrue(rule.isViolated(1, 1));
        assertFalse(rule.isViolated(0, 1));
        rule = new MazeRule(MazeRule.COLOR_BOTH_NOT_WHITE);
        assertTrue(rule.isViolated(0, 0));
        assertFalse(rule.isViolated(0, 1));
        rule = new MazeRule(MazeRule.COLOR_NOT_EQUAL);
        assertTrue(rule.isViolated(0, 0));
        assertFalse(rule.isViolated(1, 0));
        rule = new MazeRule(MazeRule.COLOR_EQUAL);
        assertTrue(rule.isViolated(0, 1));
        assertFalse(rule.isViolated(1, 1));
        rule = new MazeRule(MazeRule.COLOR_NOT_BLACK_WHITE);
        assertTrue(rule.isViolated(1, 0));
        assertFalse(rule.isViolated(1, 1));
        rule = new MazeRule(MazeRule.COLOR_NOT_WHITE_BLACK);
        assertTrue(rule.isViolated(0, 1));
        assertFalse(rule.isViolated(0, 0));
    }
}