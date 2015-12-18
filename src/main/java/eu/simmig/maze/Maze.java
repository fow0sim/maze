package eu.simmig.maze;

import java.util.ArrayList;
import java.util.List;

public class Maze {
    public static final int DIR_N = 0;
    public static final int DIR_E = 1;
    public static final int DIR_S = 2;
    public static final int DIR_W = 3;
    public static final int DIR_NEXT = 1;
    public static final int DIR_PREV = -1;

    private int lines;
    private int columns;
    MazeTile tiles[][];
    private int hLines[][];
    private int vLines[][];

    public Maze(int lines, int columns) {
        this.lines = lines;
        this.columns = columns;
        hLines = new int [lines + 1][columns];
        vLines = new int [lines][columns + 1];
        initTiles();
    }

    public int getLines() {
        return this.lines;
    }

    public int getColor(int x, int y) {
        return getTileAt(y, x).getColor();
    }

    public void setColor(int x, int y, int color) {
        getTileAt(y, x).setColor(color);
    }

    public int getHorizontalLine(int x, int y) {
        return hLines[y][x];
    }

    public void setHorizontalLine(int x, int y, int value) {
        hLines[y][x] = value;
    }

    public int getVerticalLine(int x, int y) {
        return vLines[y][x];
    }

    public void setVerticalLine(int x, int y, int value) {
        vLines[y][x] = value;
    }

    public int getColumns() {
        return this.columns;
    }

    private void initTiles() {
        tiles = new MazeTile[lines][columns];
        for (int ix = 0; ix < getLines(); ix += 1) {
            for (int jx = 0; jx < getColumns(); jx += 1) {
                tiles[ix][jx] = new MazeTile(this, ix, jx);
            }
        }
        reset();
    }

    public void reset() {
        for (MazeTile[] row : tiles) {
            for (MazeTile tile : row) {
                tile.reset();
            }
        }
    }

    public void fillAll() {
        for (MazeTile[] row : tiles) {
            for (MazeTile tile : row) {
                tile.fillAll();
            }
        }
    }

    public boolean isSolved() {
        for (MazeTile tile : getTiles()) {
            if (!tile.isSolved()) {
                return false;
            }
        }
        return true;
    }

    public MazeTile getTileAt(int line, int col) {
        if (line < 0 || col < 0 || line >= lines || col >= columns) {
            return null;
        }
        return tiles[line][col];
    }

    public List<MazeTile> getTiles() {
        ArrayList<MazeTile> list = new ArrayList<MazeTile>();
        for (int ix = 0; ix < getLines(); ix += 1) {
            for (int jx = 0; jx < getColumns(); jx += 1) {
                list.add(getTileAt(ix, jx));
            }
        }
        return list;
    }

    public ArrayList<MazeTile> getUnknownColors() {
        ArrayList<MazeTile> list = new ArrayList<MazeTile>();
        for (int ix = 0; ix < getLines(); ix += 1) {
            for (int jx = 0; jx < getColumns(); jx += 1) {
                MazeTile tile = getTileAt(ix, jx);
                if (tile.getColor() == -1) {
                    list.add(tile);
                }
            }
        }
        return list;
    }

    public List<MazeQuartet> getQuartets() {
        ArrayList<MazeQuartet> list = new ArrayList<MazeQuartet>();
        for (int ix = 0; ix <= getLines(); ix += 1) {
            for (int jx = 0; jx <= getColumns(); jx += 1) {
                list.add(getQuartet(jx, ix));
            }
        }
        return list;
    }

    public List<MazeQuartet> getInnerQuartets() {
        ArrayList<MazeQuartet> list = new ArrayList<MazeQuartet>();
        for (int ix = 1; ix < getLines(); ix += 1) {
            for (int jx = 1; jx < getColumns(); jx += 1) {
                list.add(getQuartet(jx, ix));
            }
        }
        return list;
    }

    public MazeQuartet getQuartet(int x, int y) {
        return new MazeQuartet(this, x, y);
    }

    public void drawLine(int line, int col, int dir, boolean on) {
        getQuartet(col, line).drawLine(dir, on);
    }

    public void drawWall(int line, int col, int dir, boolean on) {
        if (line < 0 || col < 0 || line >= lines || col >= columns) {
            return;
        }
        int value = (on) ? 1 : 0;
        getTileAt(line, col).setWall(dir, value);
    }

    public static int rotateIndex(int dir, int count) {
        if (count < 0) {
            count = 4 + (count % 4);
        }
        return (dir + count) % 4;
    }

    public static int reverseIndex(int dir) {
        return rotateIndex(dir, Maze.DIR_NEXT * 2);
    }

    public static int translateDirection(char c) {
        switch (c) {
            case 'N':
            case 'n':
                return DIR_N;
            case 'E':
            case 'e':
                return DIR_E;
            case 'S':
            case 's':
                return DIR_S;
            case 'W':
            case 'w':
                return DIR_W;
        }
        return -1;
    }

    public static boolean isValidDirection(int dir) {
        return dir >= DIR_N && dir <= DIR_W;
    }

    public static boolean isValidValue(int value) {
        return value >= -1 && value <= 1;
    }
}
