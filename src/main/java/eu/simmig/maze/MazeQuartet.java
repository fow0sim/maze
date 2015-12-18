package eu.simmig.maze;

public class MazeQuartet {
    private Maze maze;
    private int x;
    private int y;

    public MazeQuartet(Maze maze, int x, int y) {
        this.maze = maze;
        this.x = x;
        this.y = y;
    }

    public MazeTile getTile(int ix) {
        switch(ix) {
            case 0:
                return maze.getTileAt(getY() - 1, getX() - 1);
            case 1:
                return maze.getTileAt(getY() - 1, getX());
            case 2:
                return maze.getTileAt(getY(), getX());
            case 3:
                return maze.getTileAt(getY(), getX() - 1);
        }
        return null;
    }

    public MazeTile nextTile(int ix) {
        return nextTile(ix, 1);
    }

    public MazeTile nextTile(int ix, int step) {
        int i = step;
        if (step < 0) {
            i = 4 + (step % 4);
        }
        return getTile((ix + i) % 4);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void drawLine(int dir, boolean on) {
        setLine(dir, (on) ? 1 : 0);
    }

    public void setLine(int dir, int value) {
        if (!Maze.isValidDirection(dir) || !Maze.isValidValue(value)) {
            throw new IllegalArgumentException();
        }
        int ix = dir;
        MazeTile q1 = getTile(ix);
        MazeTile q2 = nextTile(ix);
        int jx = Maze.rotateIndex(dir, Maze.DIR_NEXT);
        if (q1 != null) {
            q1.setWall(jx, value);
        } else {
            if (q2 != null) {
                q2.setWall(Maze.reverseIndex(jx), value);
            }
        }
    }

    public int getLine(int dir) {
        int ix = dir;
        MazeTile q1 = getTile(ix);
        MazeTile q2 = nextTile(ix);
        int jx = Maze.rotateIndex(dir, Maze.DIR_NEXT);
        if (q1 != null) {
            return q1.getWall(jx);
        } else {
            if (q2 != null) {
                return q2.getWall(Maze.reverseIndex(jx));
            }
        }
        return 0;
    }

    public int countLineValues(int value) {
        int count = 0;
        int dir = Maze.DIR_N;
        for (int ix = 0; ix < 4; ix += 1) {
            if (getLine(dir) == value) {
                count += 1;
            }
            dir = Maze.rotateIndex(dir, Maze.DIR_NEXT);
        }
        return count;
    }

    public int countColorValues(int value) {
        int count = 0;
        for (int ix = 0; ix < 4 ; ix += 1) {
            MazeTile tile = getTile(ix);
            if (tile != null && tile.getColor() == value) {
                count += 1;
            }
        }
        return count;
    }

    public void setUnknownLines(boolean on) {
        int dir = Maze.DIR_N;
        for (int ix = 0; ix < 4; ix += 1) {
            if (getLine(dir) == -1) {
                drawLine(dir, on);
            }
            dir = Maze.rotateIndex(dir, Maze.DIR_NEXT);
        }
    }

    public int countConnectionsOnPath(int dir) {
        MazeQuartet quartet;
        switch (dir) {
            case Maze.DIR_N:
                quartet = maze.getQuartet(getX(), getY() - 1);
                break;
            case Maze.DIR_E:
                quartet = maze.getQuartet(getX() + 1, getY());
                break;
            case Maze.DIR_S:
                quartet = maze.getQuartet(getX(), getY() + 1);
                break;
            case Maze.DIR_W:
                quartet = maze.getQuartet(getX() - 1, getY());
                break;
            default:
                throw new IllegalArgumentException();
        }
        return (quartet == null) ? 0 : quartet.countLineValues(1);
    }

    public boolean isValidDirection(int dir) {
        return !(getTile(dir) == null && nextTile(dir) == null);
    }

    public boolean isInnerQuartet() {
        return getTile(0) != null && getTile(1) != null && getTile(2) != null && getTile(3) != null;
    }

    public int countTiles() {
        int count = 0;
        for (int ix = 0; ix < 4 ; ix += 1) {
            if (getTile(ix) != null) {
                count += 1;
            }
        }
        return count;
    }
}
