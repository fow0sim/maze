package eu.simmig.maze;

public class MazeTile {
    private Maze maze;
    private int line;
    private int column;
    private int color;

    public MazeTile(Maze maze, int line, int column) {
        this.maze = maze;
        this.line = line;
        this.column = column;
    }

    public boolean isSolved() {
        if (getColor() == -1) {
            return false;
        }
        for (int i = 0; i < 4; i += 1) {
            if (getWall(i) == -1) {
                return false;
            }
        }
        return true;
    }

    public void reset() {
        int dir = Maze.DIR_N;
        for (int ix = 0; ix < 4; ix += 1) {
            setWall(dir, -1);
            dir = Maze.rotateIndex(dir, Maze.DIR_NEXT);
        }
        setColor(-1);
    }

    public void fillAll() {
        drawWall(Maze.DIR_N, true);
        drawWall(Maze.DIR_E, true);
        drawWall(Maze.DIR_S, true);
        drawWall(Maze.DIR_W, true);
        setColor(0);
    }

    public boolean isInitial() {
        int dir = Maze.DIR_N;
        boolean valueFound = false;
        for (int ix = 0; ix < 4 && !valueFound; ix += 1) {
            if (getWall(dir) != -1) {
                valueFound = true;
            }
            dir = Maze.rotateIndex(dir, Maze.DIR_NEXT);
        }
        return !valueFound && (getColor() == -1);
    }

    public void drawWall(int dir, boolean on) {
        setWall(dir, (on) ? 1 : 0);
    }

    public void setWall(int dir, int value) {
        switch (dir) {
            case Maze.DIR_N:
                maze.setHorizontalLine(getColumn(), getLine(), value);
                break;
            case Maze.DIR_E:
                maze.setVerticalLine(getColumn() + 1, getLine(), value);
                break;
            case Maze.DIR_S:
                maze.setHorizontalLine(getColumn(), getLine() + 1, value);
                break;
            case Maze.DIR_W:
                maze.setVerticalLine(getColumn(), getLine(), value);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public int getWall(int dir) {
        switch (dir) {
            case Maze.DIR_N:
                return maze.getHorizontalLine(getColumn(), getLine());
            case Maze.DIR_E:
                return maze.getVerticalLine(getColumn() + 1, getLine());
            case Maze.DIR_S:
                return maze.getHorizontalLine(getColumn(), getLine() + 1);
            case Maze.DIR_W:
                return maze.getVerticalLine(getColumn(), getLine());
            default:
                throw new IllegalArgumentException();
        }
    }

    public boolean hasWall(int dir) {
        return (getWall(dir) == 1);
    }

    public int countWalls() {
        int count = 0;
        int dir = Maze.DIR_N;
        for (int ix = 0; ix < 4; ix += 1) {
            if (getWall(dir) == 1) {
                count += 1;
            }
            dir = Maze.rotateIndex(dir, Maze.DIR_NEXT);
        }
        return count;
    }

    public void setUnknownWalls(boolean on) {
        int dir = Maze.DIR_N;
        for (int ix = 0; ix < 4; ix += 1) {
            if (getWall(dir) == -1) {
                drawWall(dir, on);
            }
            dir = Maze.rotateIndex(dir, Maze.DIR_NEXT);
        }
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        if (color < -1 || color > 1) {
            throw new IllegalArgumentException();
        }
        this.color = color;
    }

    public int getReverseColor() {
        if (getColor() == -1) {
            return -1;
        } else {
            return (getColor() == 1) ? 0 : 1;
        }
    }

    public boolean setColorIfUnknown(int color) {
        if (getColor() != -1) {
            return false;
        }
        setColor(color);
        return true;
    }

    public MazeTile getTileAtPath(int dir) {
        switch (dir) {
            case Maze.DIR_N:
                return maze.getTileAt(getLine() - 1, getColumn());
            case Maze.DIR_E:
                return maze.getTileAt(getLine(), getColumn() + 1);
            case Maze.DIR_S:
                return maze.getTileAt(getLine() + 1, getColumn());
            case Maze.DIR_W:
                return maze.getTileAt(getLine(), getColumn() - 1);
            default:
                throw new IllegalArgumentException();
        }
    }

    public int getMazeIndex() {
        return (getLine() * maze.getColumns() + getColumn());
    }

    public boolean isFirstLine() {
        return (line == 0);
    }

    public boolean isFirstColumn() {
        return (column == 0);
    }

    public boolean isLastColumn() {
        return (column == maze.getColumns() - 1);
    }

    public boolean isLastLine() {
        return (line == maze.getLines() - 1);
    }
}

