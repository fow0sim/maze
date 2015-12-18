package eu.simmig.maze;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MazeSolverTest {
    private Maze maze;
    private MazeSolver solver;
    private int lines = 7;
    private int columns = 11;


    @Before
    public void setUp() throws Exception {
        maze = new Maze(lines, columns);
        solver = new MazeSolver(maze);
    }

    @Test
    public void testCheckConnectionsOnPath() throws Exception {
        maze.reset();
        MazeQuartet quartet = maze.getQuartet(3, 3);
        MazeTile square = maze.getTileAt(2, 4);
        square.drawWall(Maze.DIR_W, true);
        square.drawWall(Maze.DIR_S, true);
        int count = solver.checkConnectionsOnPath(quartet);
        assertEquals(count, 1);
        assertEquals(quartet.getLine(Maze.DIR_E), 0);
    }

    @Test
    public void testCheckWallsPerSquare() throws Exception {
        maze.reset();
        MazeQuartet quartet = maze.getQuartet(3, 3);
        MazeTile square = quartet.getTile(0);
        square.drawWall(Maze.DIR_N, true);
        square.drawWall(Maze.DIR_E, true);
        square.drawWall(Maze.DIR_S, true);
        int count = solver.checkWallsPerSquare(quartet);
        assertEquals(count, 1);
        assertEquals(square.getWall(Maze.DIR_W), 0);
    }

    @Test
    public void testCheckConnectionsAtPoint() throws Exception {
        maze.reset();
        MazeQuartet quartet = maze.getQuartet(3, 4);
        quartet.drawLine(Maze.DIR_N, true);
        quartet.drawLine(Maze.DIR_E, false);
        quartet.drawLine(Maze.DIR_S, false);
        int count = solver.checkConnectionsAtPoint(quartet);
        assertEquals(count, 1);
        assertEquals(quartet.getLine(Maze.DIR_W), 1);
    }

    @Test
    public void testCheckLineAndColorPairs() throws Exception {
        maze.reset();
        MazeQuartet quartet = maze.getQuartet(3, 4);
        quartet.drawLine(Maze.DIR_N, true);
        quartet.getTile(0).setColor(1);
        quartet.getTile(2).setColor(1);
        int count = solver.checkLineAndColorPairs(quartet);
        assertEquals(count, 2);
        assertEquals(quartet.getTile(0).getColor(), 1);
        assertEquals(quartet.getLine(Maze.DIR_E), 1);
    }
}