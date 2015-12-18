package eu.simmig.maze;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        MazeTest.class,
        MazeQuartetTest.class,
        MazeTileTest.class,
        MazeSolverTest.class,
        MazeRuleTest.class,
        BruteForceEngineTest.class
        })

public class AllTests {}