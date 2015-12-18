package eu.simmig.maze;

public class MazeRule {
    public static final int COLOR_BOTH_NOT_BLACK = 1;
    public static final int COLOR_BOTH_NOT_WHITE = 2;
    public static final int COLOR_NOT_EQUAL = 4;
    public static final int COLOR_EQUAL = 8;
    public static final int COLOR_NOT_BLACK_WHITE =16;
    public static final int COLOR_NOT_WHITE_BLACK =32;
    private MazeTile square[] = new MazeTile[2];
    private int mapIndex[] = new int[2];
    private int ruleId;

    MazeRule(int ruleId) {
        this.ruleId = ruleId;
    }

    MazeRule(MazeTile square1, MazeTile square2, int ruleId) {
        if (square1.getMazeIndex() < square2.getMazeIndex()) {
            this.square[0] = square1;
            this.square[1] = square2;
            this.ruleId = ruleId;
        } else {
            this.square[0] = square2;
            this.square[1] = square1;
            if (ruleId == COLOR_NOT_BLACK_WHITE) {
                this.ruleId = COLOR_NOT_WHITE_BLACK;
            } else if (ruleId == COLOR_NOT_WHITE_BLACK) {
                this.ruleId = COLOR_NOT_BLACK_WHITE;
            } else {
                this.ruleId = ruleId;
            }
        }
        setMapIndex(0, -1);
        setMapIndex(1, -1);
    }

    public int getRuleId() {
        return ruleId;
    }

    public MazeTile getSquare(int ix) {
        if (ix >= 0 && ix <= 1) {
            return square[ix];
        }
        return null;
    }

    public int getMapIndex(int ix) {
        if (ix >= 0 && ix <= 1) {
            return mapIndex[ix];
        }
        return -1;
    }

    public void setMapIndex(int ix, int value) {
        if (ix >= 0 && ix <= 1) {
            mapIndex[ix] = value;
        }
    }

    public void mapRule(MazeTile square, int ix) {
        if (getSquare(0).equals(square)) {
            setMapIndex(0, ix);
        } else if (getSquare(1).equals(square)) {
            setMapIndex(1, ix);
        }
    }

    public boolean isMapped() {
        return getMapIndex(0) >= 0 && getMapIndex(1) >= 0;
    }

    @Override
    public boolean equals(Object other) {
        MazeRule rule = (MazeRule) other;
        return     getSquare(0).getMazeIndex() == rule.getSquare(0).getMazeIndex()
                && getSquare(1).getMazeIndex() == rule.getSquare(1).getMazeIndex()
                && getRuleId() == rule.getRuleId();
    }

    @Override
    public int hashCode() {
        return (41 * (41 + getSquare(0).getMazeIndex()) + getSquare(1).getMazeIndex());
    }

    public boolean affects(MazeTile square) {
        return (square.getMazeIndex() == getSquare(0).getMazeIndex()
                || square.getMazeIndex() == getSquare(1).getMazeIndex());
    }

    public boolean isViolated(MazeTile q1, MazeTile q2) {
        if (!affects(q2) || !affects(q1)) {
            return false;
        }
        return isViolated(q1.getColor(), q2.getColor());
    }

    public boolean isViolated(int color1, int color2) {
        switch (ruleId) {
            case COLOR_BOTH_NOT_BLACK:
                return (color1 == 1 && color2 == 1);
            case COLOR_BOTH_NOT_WHITE:
                return (color1 == 0 && color2 == 0);
            case COLOR_NOT_EQUAL:
                return (color1 == color2);
            case COLOR_EQUAL:
                return (color1 != color2);
            case COLOR_NOT_BLACK_WHITE:
                return (color1 == 1 && color2 == 0);
            case COLOR_NOT_WHITE_BLACK:
                return (color1 == 0 && color2 == 1);
        }
        return false;
    }

    public String toString() {
        String str = "";
        if (square[0] != null) {
            str += "(" + square[0].getLine() + "," + square[0].getColumn() + ")";
        }
        str += " - " ;
        if (square[1] != null) {
            str += "(" + square[1].getLine() + "," + square[1].getColumn() + ")";
        }
        str += ": ";
        switch (ruleId) {
            case COLOR_BOTH_NOT_BLACK:
                str += "Can't be both black";
                break;
            case COLOR_BOTH_NOT_WHITE:
                str += "Can't be both white";
                break;
            case COLOR_NOT_EQUAL:
                str += "Must be not equal";
                break;
            case COLOR_EQUAL:
                str += "Must be equal";
                break;
            case COLOR_NOT_BLACK_WHITE:
                str += "Can't be 1st black and 2nd white";
                break;
            case COLOR_NOT_WHITE_BLACK:
                str += "Can't be 1st white and 2nd black";
                break;
        }
        return str;
    }
}
