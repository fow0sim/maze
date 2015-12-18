package eu.simmig.maze;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class MazeSolver {
    private Maze maze;

    MazeSolver(Maze maze) {
        this.maze = maze;
    }

    public boolean solve() {
        boolean passSolved;
        boolean hasMorePotential;
        int count = 0;

        do {
            do {
                hasMorePotential = false;
                passSolved = false;
                passSolved = applyPatterns();
//                passSolved = applyGuesses();
                if (passSolved) {
                    hasMorePotential = true;
                }
            } while (hasMorePotential);
/*
            if (!maze.isSolved()) {
                hasMorePotential = runBruteForceEngine();
            }
*/
            count += 1;
        } while (hasMorePotential && count <=2);

        return maze.isSolved();
    }

    public boolean applyPatterns() {
        boolean didSolve = false;
        int patternsApplied = 0;
        int passCount = 0;
        do {
            patternsApplied = 0;
            for (MazeQuartet quartet : maze.getQuartets()) {
                patternsApplied += checkConnectionsOnPath(quartet);
                patternsApplied += checkWallsPerSquare(quartet);
                patternsApplied += checkConnectionsAtPoint(quartet);
                patternsApplied += checkLineAndColorPairs(quartet);
//                patternsApplied += checkAnkesTestPattern(quartet);
            }
            if (patternsApplied > 0) {
                didSolve = true;
            }
            passCount += 1;
            System.out.println(patternsApplied + " Patterns applied on pass " + passCount);
        } while (patternsApplied > 0);
        return didSolve;
    }

    public boolean applyGuesses() {
        boolean didSolve = false;
        int patternsApplied = 0;
        int passCount = 0;
        do {
            patternsApplied = 0;
            for (MazeQuartet quartet : maze.getQuartets()) {
                patternsApplied += guessWhiteColors(quartet);
            }
            if (patternsApplied > 0) {
                didSolve = true;
            }
            passCount += 1;
            System.out.println(patternsApplied + " Guesses applied on pass " + passCount);
        } while (patternsApplied > 0);
        return didSolve;
    }

    public int checkConnectionsOnPath(MazeQuartet quartet) {
        int patternsApplied = 0;
        int dir = Maze.DIR_N;
        for (int ix = 0; ix < 4; ix += 1) {
            if (quartet.getLine(dir) == -1 && quartet.countConnectionsOnPath(dir) == 2) {
                quartet.drawLine(dir, false);
                patternsApplied += 1;
            }
            dir = Maze.rotateIndex(dir, Maze.DIR_NEXT);
        }
        return patternsApplied;
    }

    public int checkWallsPerSquare(MazeQuartet quartet) {
        int patternsApplied = 0;
        int dir = Maze.DIR_N;
        for (int ix = 0; ix < 4; ix += 1) {
            MazeTile square = quartet.getTile(dir);
            if (square != null) {
                if (!square.isSolved() && square.countWalls() == 3) {
                    square.setUnknownWalls(false);
                    patternsApplied += 1;
                }
            }
            dir = Maze.rotateIndex(dir, Maze.DIR_NEXT);
        }
        return patternsApplied;
    }

    public int checkConnectionsAtPoint(MazeQuartet quartet) {
        int patternsApplied = 0;
        int unknownLines = quartet.countLineValues(-1);
        if (unknownLines != 0) {
            int connectedLines = quartet.countLineValues(1);
            if (connectedLines + unknownLines == 2) {
                quartet.setUnknownLines(true);
                patternsApplied += 1;
            }
        }
        return patternsApplied;
    }

    public int checkLineAndColorPairs(MazeQuartet quartet) {
        int patternsApplied = 0;
        int dir = Maze.DIR_N;
        for (int ix = 0; ix < 4; ix += 1) {
            boolean colorSet = false;
            MazeTile q1 = quartet.getTile(ix);
            MazeTile q2 = quartet.nextTile(ix);
            int lineValue = quartet.getLine(dir);
            int nullCount = 0;
            int colorCount = 0;
            MazeTile q = null;
            if (q1 == null) {
                nullCount += 1;
                q = q2;
            }
            if (q2 == null) {
                nullCount += 1;
                q = q1;
            }
            if (q1 != null && q1.getColor() != -1) {
                colorCount += 1;
            }
            if (q2 != null && q2.getColor() != -1) {
                colorCount += 1;
            }

            if (nullCount == 1 && lineValue == -1 && colorCount == 1) {
                quartet.setLine(dir, q.getColor());
                patternsApplied += 1;
            } else if (nullCount == 1 && lineValue != -1 && colorCount == 0) {
                q.setColor(quartet.getLine(dir));
                patternsApplied += 1;
            } else if (nullCount == 0 && lineValue == -1 && colorCount == 2) {
                quartet.setLine(dir, (q1.getColor() == q2.getColor()) ? 0 : 1);
                patternsApplied += 1;
            } else if (nullCount == 0 && lineValue != -1 && colorCount == 1) {
                if (q1.getColor() == -1) {
                    q1.setColor((lineValue == 1) ? q2.getReverseColor() : q2.getColor());
                } else {
                    q2.setColor((lineValue == 1) ? q1.getReverseColor() : q1.getColor());
                }
                patternsApplied += 1;
            }
            dir = Maze.rotateIndex(dir, Maze.DIR_NEXT);
        }
        return patternsApplied;
    }


    public int checkAnkesTestPattern(MazeQuartet quartet) {
        if (!quartet.isInnerQuartet()) {
            return 0;
        }
        int patternsApplied = 0;
        int dir = Maze.DIR_N;
        for (int ix = 0; ix < 4; ix += 1, dir = Maze.rotateIndex(dir, Maze.DIR_NEXT)) {
            MazeTile q1 = quartet.getTile(dir);
            MazeTile q2 = quartet.nextTile(dir);
            MazeTile q3 = quartet.nextTile(dir, 2);
            MazeTile q4 = quartet.nextTile(dir, 3);
            if (q1.getColor() == -1 || q2.getColor() == -1 || q1.getColor() != q2.getColor()) {
                continue;
            }
            if (!(q2.getWall(dir) == 1 && q1.getWall(dir) == 1)) {
                continue;
            }
            if (q3.getColor() == -1 && q3.getWall(Maze.reverseIndex(dir)) == 1) {
                q3.setColor(q2.getReverseColor());
                patternsApplied += 1;
            }
            if (q4.getColor() == -1 && q4.getWall(Maze.reverseIndex(dir)) == 1) {
                q4.setColor(q1.getReverseColor());
                patternsApplied += 1;
            }
        }
        return patternsApplied;
    }

    public int guessWhiteColors(MazeQuartet quartet) {
        int patternsApplied = 0;
        MazeTile square = quartet.getTile(2);
        if (square == null || square.getColor() != -1) {
            return 0;
        }
        int dir = maze.DIR_N;
        for (int ix = 0; ix < 2; ix += 1, dir = Maze.rotateIndex(dir, Maze.DIR_NEXT)) {
            int dir_n = Maze.rotateIndex(dir, Maze.DIR_NEXT);
            MazeTile q1 = square.getTileAtPath(dir);
            MazeTile q2 = square.getTileAtPath(Maze.reverseIndex(dir));
            MazeTile q3 = square.getTileAtPath(dir_n);
            MazeTile q4 = square.getTileAtPath(Maze.reverseIndex(dir_n));
            if (q1 == null || q2 == null || q1.getColor() != 1 || q2.getColor() != 1) {
                continue;
            }
            int color1 = (q3 == null) ? 0 : q3.getColor();
            int color2 = (q4 == null) ? 0 : q4.getColor();
            if ((color1 != 1) && (color2 != 1) && !(color1 == -1 && color2 == -1)) {
                square.setColor(0);
                patternsApplied += 1;
                System.out.println("Color guess on (" + square.getLine() + "," + square.getColumn() + ")");
            }
        }
        return patternsApplied;
    }

    public void printRules(LinkedHashSet<MazeRule> rules) {
        for (MazeRule rule : rules) {
            System.out.println(rule.toString());
        }
    }

    public void findRules(MazeQuartet quartet, LinkedHashSet<MazeRule> rules) {
        int dir = Maze.DIR_N;
        for (int ix = 0; ix < 4; ix += 1, dir = Maze.rotateIndex(dir, Maze.DIR_NEXT)) {
            MazeTile q1 = quartet.getTile(dir);
            MazeTile q2 = quartet.nextTile(dir);
            MazeTile q3 = quartet.nextTile(dir, 2);
            MazeTile q4 = quartet.nextTile(dir, 3);
            if (!(q1.getColor() == -1 && q2.getColor() == -1)) {
                continue;
            }
            if (quartet.getLine(dir) != -1) {
                if (quartet.getLine(dir) == 0) {
                    rules.add(new MazeRule(q1, q2, MazeRule.COLOR_EQUAL));
                } else {
                    rules.add(new MazeRule(q1, q2, MazeRule.COLOR_NOT_EQUAL));
                }
            }
            if (q3.getColor() != -1 && q4.getColor() != -1) {
                if (q3.getColor() == q4.getColor()) {
                    if (q3.getColor() == 0) {
                        rules.add(new MazeRule(q1, q2, MazeRule.COLOR_BOTH_NOT_WHITE));
                    } else {
                        rules.add(new MazeRule(q1, q2, MazeRule.COLOR_BOTH_NOT_BLACK));
                    }
                } else {
                    if (q3.getColor() == 0) {
                        rules.add(new MazeRule(q1, q2, MazeRule.COLOR_NOT_WHITE_BLACK));
                    } else {
                        rules.add(new MazeRule(q1, q2, MazeRule.COLOR_NOT_BLACK_WHITE));
                    }
                }
            }
        }
    }

    public boolean runBruteForceEngine() {
        boolean didSolve = false;
        LinkedHashSet<MazeRule> rules = new LinkedHashSet<MazeRule>();
        for (MazeQuartet quartet : maze.getInnerQuartets()) {
            findRules(quartet, rules);
        }
        printRules(rules);

        MazeBruteForceRunner runner = new MazeBruteForceRunner();
        ArrayList<MazeTile> squares = maze.getUnknownColors();
        runner.setRules(rules);
        runner.setSquares(squares);
        runner.prepare();
        BruteForceEngine engine = new BruteForceEngine(1, squares.size());
        System.out.println("Running Brute Force Engine");
        engine.run(runner);
        System.out.println("Patterns valid: " + runner.getCount() + ", invalid: " + runner.getInvalidCount());
        for (String s : runner.getResult()) {
            System.out.println(s);
        }
        for (int i : runner.getZeroCount()) {
            System.out.println(i);
        }
        didSolve = (runner.applyColors() > 0);
        return didSolve;
    }
}
