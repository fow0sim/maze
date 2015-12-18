package eu.simmig.maze;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class MazeBruteForceRunner implements BruteForceRunner {
    private ArrayList<MazeTile> squares;
    private LinkedHashSet<MazeRule> rules;
    private ArrayList<MazeRule> mappedRules;
    private ArrayList<String> result;
    private int count;
    private int invalidCount;
    private int zeroCount[];

    public void setSquares(ArrayList<MazeTile> list) {
        squares = list;
    }

    public void setRules(LinkedHashSet<MazeRule> list) {
        rules = list;
    }

    public int getCount() {
        return count;
    }

    public int getInvalidCount() {
        return invalidCount;
    }

    public void prepare() {
        int ix = 0;
        for (MazeTile square : squares) {
            for (MazeRule rule : rules) {
                rule.mapRule(square, ix + 1);
            }
            ix += 1;
        }
        mappedRules = new ArrayList<MazeRule>();
        for (MazeRule rule : rules) {
            if (rule.isMapped()) {
                mappedRules.add(rule);
            }
        }
        count = 0;
        invalidCount = 0;
        result = new ArrayList<String>();
        zeroCount = new int[squares.size()];
    }

    @Override
    public boolean executeStep(int[] state) {
        for (MazeRule rule : mappedRules) {
            int ix = rule.getMapIndex(0);
            int jx = rule.getMapIndex(1);
            if (rule.isViolated(state[ix], state[jx])) {
                invalidCount += 1;
                return true;
            }
        }
        result.add(saveAsString(state));
        for (int ix = 0; ix < zeroCount.length; ix += 1) {
            if (state[ix] == 0) {
                zeroCount[ix] += 1;
            }
        }
        count += 1;
        return true;
    }

    public String saveAsString(int state[]){
        String str = "";
        for (int i : state) {
            str += i;
        }
        return str;
    }

    public List<String> getResult() {
        return result;
    }

    public int[] getZeroCount() {
        return zeroCount;
    }

    public int applyColors() {
        int colorsApplied = 0;
        for (int ix = 1; ix < zeroCount.length; ix += 1) {
            if (zeroCount[ix] == getCount()) {
                squares.get(ix).setColor(0);
                colorsApplied += 1;
            }
            if (zeroCount[ix] == 0) {
                squares.get(ix).setColor(1);
                colorsApplied += 1;
            }
        }
        return colorsApplied;
    }
}
