package eu.simmig.maze;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MazeTest {
    private Maze maze;
    private int lines = 7;
    private int columns = 11;


    @Before
    public void setUp() throws Exception {
        maze = new Maze(lines, columns);

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetLines() throws Exception {
        assertEquals(maze.getLines(), lines);
    }

    @Test
    public void testGetColumns() throws Exception {
        assertEquals(maze.getColumns(), columns);
    }

    @Test
    public void testReset() throws Exception {
        maze.reset();
        for (MazeTile tile : maze.getTiles()) {
            assertTrue(tile.isInitial());
        }
    }

    @Test
    public void testIsSolved() throws Exception {
        maze.reset();
        assertFalse(maze.isSolved());
        maze.fillAll();
        assertTrue(maze.isSolved());
    }

    @Test
    public void testGetTileAt() throws Exception {
        MazeTile tile;
        for (int ix = 0; ix < lines; ix += 1) {
            for (int jx = 0; jx < columns; jx += 1) {
                tile = maze.getTileAt(ix, jx);
                assertEquals(tile.getLine(), ix);
                assertEquals(tile.getColumn(), jx);
            }
        }
        tile = maze.getTileAt(88, 99);
        assertNull(tile);
        tile = maze.getTileAt(-1, -1);
        assertNull(tile);
    }

    @Test
    public void testGetSquares() throws Exception {
        int ix = 0;
        for (MazeTile square: maze.getTiles()) {
            int line = ix / maze.getColumns();
            int col = ix % maze.getColumns();
            assertEquals(square.getLine(), line);
            assertEquals(square.getColumn(), col);
            ix += 1;
        }
    }

    @Test
    public void testDrawLine() throws Exception {

    }

    @Test
    public void testDrawWall() throws Exception {

    }

    @Test
    public void testSetAndGetColor() throws Exception {
        MazeTile tile;
        tile = maze.getTileAt(0, 0);
        maze.setColor(0, 0, 0);
        assertEquals(0, tile.getColor());
        maze.setColor(0, 0, 1);
        assertEquals(1, tile.getColor());
        int x = maze.getColumns() - 1;
        int y = maze.getLines() - 1;
        tile = maze.getTileAt(y, x);
        maze.setColor(x, y, 1);
        assertEquals(1, tile.getColor());
    }

    @Test
    public void testSetAndGetHorizontalLine() throws Exception {
        MazeTile tile;
        int x = 0;
        int y = 0;
        tile = maze.getTileAt(y, x);
        tile.fillAll();
        assertEquals(1, maze.getHorizontalLine(x, y));
        assertEquals(1, maze.getHorizontalLine(x, y + 1));
        maze.setHorizontalLine(x, y + 1, 0);
        assertEquals(0, tile.getWall(Maze.DIR_S));
    }

    @Test
    public void testSetAndGetVerticalLine() throws Exception {
        MazeTile tile;
        int x = 0;
        int y = 0;
        tile = maze.getTileAt(y, x);
        tile.fillAll();
        assertEquals(1, maze.getVerticalLine(x, y));
        assertEquals(1, maze.getVerticalLine(x + 1, y));
        maze.setVerticalLine(x, y, 0);
        assertEquals(0, tile.getWall(Maze.DIR_W));
    }

    @Test
    public void testRotateIndex() throws Exception {
        int result[] = {0, 1, 2, 3};
        for (int ix = 0; ix < 8; ix += 1) {
            assertEquals(Maze.rotateIndex(0, ix), result[ix % 4]);
            assertEquals(Maze.rotateIndex(0, -ix), result[(4 - (ix % 4)) % 4]);
        }
    }

    @Test
    public void testReverseIndex() throws Exception {
        int result[] = {2, 3, 0, 1};
        for (int ix = 0; ix < 4; ix += 1) {
            assertEquals(Maze.reverseIndex(ix), result[ix]);
        }
    }

    @Test
    public void testXOffset() throws Exception {

    }

    @Test
    public void testYOffset() throws Exception {

    }

    @Test
    public void testTranslateDirection() throws Exception {
        char request[] = {'n', 'N', 's', 'S', 'e', 'E', 'w', 'W'};
        int result[] = {Maze.DIR_N, Maze.DIR_S, Maze.DIR_E, Maze.DIR_W};
        for (int ix = 0; ix < 8; ix += 1) {
            int jx = ix / 2;
            assertEquals(Maze.translateDirection(request[ix]), result[jx]);
        }

    }
}