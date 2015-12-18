package eu.simmig.maze;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class MazeTileTest {
    private Maze maze;
    private int lines = 7;
    private int columns = 11;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @org.junit.Before
    public void setUp() throws Exception {
        maze = new Maze(lines, columns);
    }

    @Test
    public void testIsSolved() throws Exception {
        MazeTile square = maze.getTileAt(0, 0);
        square.reset();
        assertFalse(square.isSolved());
        square.setWall(Maze.DIR_N, 1);
        square.setWall(Maze.DIR_E, 0);
        square.setWall(Maze.DIR_S, 0);
        square.setWall(Maze.DIR_W, 1);
        square.setColor(1);
        assertTrue(square.isSolved());
    }

    @Test
    public void testReset() throws Exception {
        MazeTile square = maze.getTileAt(0, 0);
        square.reset();
        assertTrue(square.isInitial());
        square.setWall(Maze.DIR_E, 0);
        assertFalse(square.isInitial());
    }

    @Test
    public void testDrawWall() throws Exception {
        MazeTile square = maze.getTileAt(0, 0);
        square.reset();
        square.drawWall(Maze.DIR_N, true);
        assertEquals(square.getWall(Maze.DIR_N), 1);
        square.drawWall(Maze.DIR_E, false);
        assertEquals(square.getWall(Maze.DIR_E), 0);
        square.drawWall(Maze.DIR_E, true);
        assertEquals(square.getWall(Maze.DIR_E), 1);
    }

    @Test
    public void testSetWall() throws Exception {
        MazeTile tile = maze.getTileAt(0, 0);
        tile.setWall(Maze.DIR_W, 0);
        assertEquals(tile.getWall(Maze.DIR_W), 0);
        tile.setWall(Maze.DIR_W, 1);
        assertEquals(tile.getWall(Maze.DIR_W), 1);
        tile.setWall(Maze.DIR_W, -1);
        assertEquals(tile.getWall(Maze.DIR_W), -1);
        exception.expect(IllegalArgumentException.class);
        tile.setWall(Maze.DIR_W, 2);
        tile.setWall(4, 1);
    }

    @Test
    public void testHasWall() throws Exception {
        MazeTile square = maze.getTileAt(0, 0);
        square.setWall(Maze.DIR_E, 0);
        assertFalse(square.hasWall(Maze.DIR_E));
        square.setWall(Maze.DIR_E, 1);
        assertTrue(square.hasWall(Maze.DIR_E));
        square.setWall(Maze.DIR_E, -1);
        assertFalse(square.hasWall(Maze.DIR_E));
    }

    @Test
    public void testSetUnknownWalls() throws Exception {
        MazeTile square = maze.getTileAt(0, 0);
        square.reset();
        square.setUnknownWalls(true);
        for (int ix = Maze.DIR_N; ix < Maze.DIR_W; ix = Maze.rotateIndex(ix, Maze.DIR_NEXT)) {
            assertTrue(square.hasWall(ix));
        }
        square.setUnknownWalls(false);
        for (int ix = Maze.DIR_N; ix < Maze.DIR_W; ix = Maze.rotateIndex(ix, Maze.DIR_NEXT)) {
            assertTrue(square.hasWall(ix));
        }
        square.reset();
        square.setUnknownWalls(false);
        for (int ix = Maze.DIR_N; ix < Maze.DIR_W; ix = Maze.rotateIndex(ix, Maze.DIR_NEXT)) {
            assertFalse(square.hasWall(ix));
        }
    }

    @Test
    public void testCountWalls() throws Exception {
        MazeTile square = maze.getTileAt(0, 0);
        square.reset();
        assertEquals(square.countWalls(), 0);
        square.setUnknownWalls(true);
        assertEquals(square.countWalls(), 4);
    }

    @Test
    public void testReverseColor() throws Exception {
        MazeTile square = maze.getTileAt(0, 0);
        square.setColor(0);
        assertEquals(square.getReverseColor(), 1);
        square.setColor(1);
        assertEquals(square.getReverseColor(), 0);
        square.setColor(-1);
        assertEquals(square.getReverseColor(), -1);
    }

    @Test
    public void testSetColor() throws Exception {
        MazeTile square = maze.getTileAt(3, 3);
        square.setColor(-1);
        assertEquals(-1, square.getColor());
        square.setColor(1);
        assertEquals(1, square.getColor());
        exception.expect(IllegalArgumentException.class);
        square.setColor(2);
        square.setColor(-2);
    }

    @Test
    public void testSetColorIfUnknown() throws Exception {
        MazeTile square = maze.getTileAt(0, 0);
        square.setColor(-1);
        assertTrue(square.setColorIfUnknown(0));
        assertEquals(square.getColor(), 0);
        assertFalse(square.setColorIfUnknown(1));
        assertEquals(square.getColor(), 0);
        square.setColor(-1);
        assertTrue(square.setColorIfUnknown(1));
        assertEquals(square.getColor(), 1);
    }

    @Test
    public void testGetSquareAtPath() throws Exception {
        MazeTile tile = maze.getTileAt(0, 0);
        assertNull(tile.getTileAtPath(Maze.DIR_N));
        assertNull(tile.getTileAtPath(Maze.DIR_W));
        MazeTile path = tile.getTileAtPath(Maze.DIR_E);
        assertEquals(path.getLine(), 0);
        assertEquals(path.getColumn(), 1);
        path = tile.getTileAtPath(Maze.DIR_S);
        assertEquals(path.getLine(), 1);
        assertEquals(path.getColumn(), 0);
    }

    @Test
    public void testIsFirstLine() throws Exception {
        MazeTile square = maze.getTileAt(0, 0);
        assertTrue(square.isFirstLine());
        square = maze.getTileAt(0, columns - 1);
        assertTrue(square.isFirstLine());
        square = maze.getTileAt(1, columns - 1);
        assertFalse(square.isFirstLine());
    }

    @Test
    public void testIsFirstColumn() throws Exception {
        MazeTile square = maze.getTileAt(0, 0);
        assertTrue(square.isFirstColumn());
        square = maze.getTileAt(0, columns - 1);
        assertFalse(square.isFirstColumn());
        square = maze.getTileAt(1, 0);
        assertTrue(square.isFirstColumn());
    }

    @Test
    public void testIsLastColumn() throws Exception {
        MazeTile square = maze.getTileAt(0, 0);
        assertFalse(square.isLastColumn());
        square = maze.getTileAt(0, columns - 1);
        assertTrue(square.isLastColumn());
        square = maze.getTileAt(lines - 1, columns - 1);
        assertTrue(square.isLastColumn());
    }

    @Test
    public void testIsLastLine() throws Exception {
        MazeTile square = maze.getTileAt(0, 0);
        assertFalse(square.isLastLine());
        square = maze.getTileAt(lines - 1, 0);
        assertTrue(square.isLastLine());
        square = maze.getTileAt(lines - 1, columns - 1);
        assertTrue(square.isLastLine());
    }
}