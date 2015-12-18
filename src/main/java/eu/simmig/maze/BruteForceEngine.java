package eu.simmig.maze;

import java.util.Arrays;

public class BruteForceEngine implements BruteForceRunner {
    private int length;
    private int max;
    private int[] values;

    BruteForceEngine(int max, int length) {
        this.max = max;
        this.length = length;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void run() {
        run(this);
    }

    public void run(BruteForceRunner runner) {
        values = new int[getLength() + 1];
        Arrays.fill(values, 0);
        Boolean resumeExecution = true;
        while (values[0] == 0 && resumeExecution) {
            resumeExecution = runner.executeStep(values);
            increment();
        }
    }

    public void increment() {
        for (int i = values.length - 1; i >= 0; i -= 1) {
            if (values[i] < max) {
                values[i]++;
                return;
            }
            values[i] = 0;
        }
    }

    public boolean executeStep(int state[]) {
        printState(state);
        return true;
    }

    public static String printState(int[] state) {
        String s = "";
        for (int i = 1; i < state.length; i++) {
            s += state[i];
        }
        return s;
    }
}
