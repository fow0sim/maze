package eu.simmig.maze;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class MazeQuartetTest {
    private Maze maze;
    private int lines = 7;
    private int columns = 11;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        maze = new Maze(lines, columns);
    }

    @Test
    public void testGetTile() throws Exception {
        MazeQuartet quartet = maze.getQuartet(0, 0);
        assertNull(quartet.getTile(0));
        assertNull(quartet.getTile(-1));
        assertNull(quartet.getTile(4));
        assertNotNull(quartet.getTile(2));
    }

    @Test
    public void testNextSquare() throws Exception {
        MazeQuartet quartet = maze.getQuartet(0, 1);
        MazeTile square = quartet.getTile(1);
        assertNotNull(square);
        assertNotNull(quartet.nextTile(1));
        assertNull(quartet.nextTile(2));
        assertNull(quartet.nextTile(1, 2));
        assertNotNull(quartet.nextTile(3, 3));
    }

    @Test
    public void testDrawLine() throws Exception {
        int col = 3;
        int line = 2;
        maze.reset();
        MazeQuartet quartet = maze.getQuartet(col, line);
        int dir = Maze.DIR_N;
        for (int ix = 0; ix < 4; ix += 1) {
            quartet.drawLine(dir, true);
            dir = Maze.rotateIndex(dir, Maze.DIR_NEXT);
        }
        assertTrue(maze.getTileAt(line - 1, col - 1).hasWall(Maze.DIR_E));
        assertTrue(maze.getTileAt(line - 1, col - 1).hasWall(Maze.DIR_S));
        assertTrue(maze.getTileAt(line - 1, col).hasWall(Maze.DIR_W));
        assertTrue(maze.getTileAt(line - 1, col).hasWall(Maze.DIR_S));
        assertTrue(maze.getTileAt(line, col).hasWall(Maze.DIR_N));
        assertTrue(maze.getTileAt(line, col).hasWall(Maze.DIR_W));
        assertTrue(maze.getTileAt(line, col - 1).hasWall(Maze.DIR_N));
        assertTrue(maze.getTileAt(line, col - 1).hasWall(Maze.DIR_E));
        quartet.drawLine(Maze.DIR_S, false);
        assertFalse(maze.getTileAt(line, col - 1).hasWall(Maze.DIR_E));
        assertFalse(maze.getTileAt(line, col).hasWall(Maze.DIR_W));
        exception.expect(IllegalArgumentException.class);
        quartet.drawLine(4, false);
        quartet.drawLine(-1, false);
    }

    @Test
    public void testGetLine() throws Exception {
        maze.reset();
        MazeQuartet q1 = maze.getQuartet(0, 0);
        MazeQuartet q2 = maze.getQuartet(3, 2);
        int dir = Maze.DIR_N;
        for (int ix = 0; ix < 4; ix += 1) {
            assertEquals(q2.getLine(dir), -1);
            dir = Maze.rotateIndex(dir, Maze.DIR_NEXT);
        }
        dir = Maze.DIR_N;
        for (int ix = 0; ix < 4; ix += 1) {
            q2.setLine(dir, 0);
            assertEquals(q2.getLine(dir), 0);
            dir = Maze.rotateIndex(dir, Maze.DIR_NEXT);
        }
        dir = Maze.DIR_N;
        for (int ix = 0; ix < 4; ix += 1) {
            q2.setLine(dir, 1);
            assertEquals(q2.getLine(dir), 1);
            dir = Maze.rotateIndex(dir, Maze.DIR_NEXT);
        }
    }

    @Test
    public void testCountValues() throws Exception {
        maze.reset();
        MazeQuartet quartet = maze.getQuartet(3,5);
        assertEquals(quartet.countLineValues(-1), 4);
        assertEquals(quartet.countLineValues(0), 0);
        assertEquals(quartet.countLineValues(1), 0);
        quartet.drawLine(Maze.DIR_N, true);
        assertEquals(quartet.countLineValues(-1), 3);
        assertEquals(quartet.countLineValues(0), 0);
        assertEquals(quartet.countLineValues(1), 1);
        quartet.drawLine(Maze.DIR_W, false);
        assertEquals(quartet.countLineValues(-1), 2);
        assertEquals(quartet.countLineValues(0), 1);
        assertEquals(quartet.countLineValues(1), 1);
        quartet = maze.getQuartet(0,0);
        assertEquals(quartet.countLineValues(-1), 2);
        assertEquals(quartet.countLineValues(0), 2);
        assertEquals(quartet.countLineValues(1), 0);
    }

    @Test
    public void testIsValidDirection() throws Exception {
        for (int ix = 0; ix <= lines; ix += 1) {
            for (int jx = 0; jx <= columns; jx += 1) {
                MazeQuartet quartet = maze.getQuartet(jx, ix);
                if (ix == 0) {
                    assertFalse(quartet.isValidDirection(Maze.DIR_N));
                    assertTrue(quartet.isValidDirection(Maze.DIR_S));
                }
                if (jx == 0) {
                    assertFalse(quartet.isValidDirection(Maze.DIR_W));
                    assertTrue(quartet.isValidDirection(Maze.DIR_E));
                }
                if (ix == lines) {
                    assertFalse(quartet.isValidDirection(Maze.DIR_S));
                    assertTrue(quartet.isValidDirection(Maze.DIR_N));
                }
                if (jx == columns) {
                    assertFalse(quartet.isValidDirection(Maze.DIR_E));
                    assertTrue(quartet.isValidDirection(Maze.DIR_W));
                }
            }
        }

    }

    @Test
    public void testCountConnectionsOnPath() throws Exception {
        maze.reset();
        MazeQuartet quartet = maze.getQuartet(0, 0);
        assertEquals(quartet.countConnectionsOnPath(Maze.DIR_N), 0);
        assertEquals(quartet.countConnectionsOnPath(Maze.DIR_E), 0);
        quartet.drawLine(Maze.DIR_E, true);
        assertEquals(quartet.countConnectionsOnPath(Maze.DIR_E), 1);
        quartet.drawLine(Maze.DIR_E, false);
        assertEquals(quartet.countConnectionsOnPath(Maze.DIR_E), 0);
        MazeTile square = maze.getTileAt(0, 1);
        square.drawWall(Maze.DIR_N, true);
        square.drawWall(Maze.DIR_W, true);
        assertEquals(quartet.countConnectionsOnPath(Maze.DIR_E), 2);
    }

    @Test
    public void testCountSquares() throws Exception {
        MazeQuartet quartet = maze.getQuartet(0, 0);
        assertEquals(quartet.countTiles(), 1);
        quartet = maze.getQuartet(0, 1);
        assertEquals(quartet.countTiles(), 2);
        quartet = maze.getQuartet(1, 1);
        assertEquals(quartet.countTiles(), 4);
    }

    @Test
    public void testIsInnerQuartet() throws Exception {
        MazeQuartet quartet = maze.getQuartet(4, 2);
        assertTrue(quartet.isInnerQuartet());
        quartet = maze.getQuartet(0, 0);
        assertFalse(quartet.isInnerQuartet());
    }
}