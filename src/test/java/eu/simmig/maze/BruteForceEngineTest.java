package eu.simmig.maze;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class BruteForceEngineTest {
    private ArrayList<String> list;

    @Test
    public void testSetMax() throws Exception {
        BruteForceEngine instance = new BruteForceEngine(1, 4);
        assertEquals(instance.getMax(), 1);
        instance.setMax(42);
        assertEquals(instance.getMax(), 42);
    }

    @Test
    public void testSetLength() throws Exception {

    }

    @Test
    public void testRun() throws Exception {
        BruteForceEngine instance = new BruteForceEngine(1, 4);
        list = new ArrayList<String>();
        instance.run(new BruteForceRunner() {
            @Override
            public boolean executeStep(int[] state) {
                list.add(BruteForceEngine.printState(state));
                return true;
            }
        });
        assertEquals(16, list.size());
        assertEquals("0000", list.get(0));
        assertEquals("1111", list.get(list.size() - 1));
    }
}