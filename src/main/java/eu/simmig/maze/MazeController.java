package eu.simmig.maze;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class MazeController {
    Properties config;

    private void loadConfiguration() {
        config = new Properties();
        String configFileName = "maze.properties";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(configFileName);
        if (inputStream != null) {
            try {
                config.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        MazeController instance = new MazeController();
        instance.run(args);
    }

    public void run(String[] args) {
        loadConfiguration();
        String s = config.getProperty("eu.simmig.maze.lines", "7");
        int lines = Integer.parseInt(s);
        s = config.getProperty("eu.simmig.maze.columns", "11");
        int columns = Integer.parseInt(s);
        String filename = null;
        if (args.length > 0) {
            filename = args[0];
        }

        System.out.println("Building maze " + lines + " x " + columns + ", " + filename);
        Maze maze = new Maze(lines, columns);
        if (filename != null && filename.length() != 0) {
            loadMaze(maze, filename);
        }
        System.out.println("Initial Maze");
        printMaze(maze);
        System.out.println("");
        MazeSolver solver = new MazeSolver(maze);
        boolean b = solver.solve();
        System.out.println("");
        System.out.println("Patterns applied");
        printMaze(maze);
    }

    public void printMaze(Maze maze) {
        MazeTile square = null;
        String wall_n;
        String box;
        String wall_s;
        System.out.println("       0    1    2    3    4    5    6    7    8    9   10");
        for (int ix = 0; ix < maze.getLines(); ix += 1) {
            wall_n = "    ";
            box = "";
            wall_s = "    ";
            for (int jx = 0; jx < maze.getColumns(); jx += 1) {
                square = maze.getTileAt(ix, jx);
                if (square.isFirstColumn()) {
                    box += "  " + ix + " ";
                }
                wall_n += formatWallString(square, Maze.DIR_N);
                box += formatColorString(square);
                if (square.isLastLine()) {
                    wall_s += formatWallString(square, Maze.DIR_S);
                }
            }
            System.out.println(wall_n);
            System.out.println(box);
            if (wall_s.length() > 4) {
                System.out.println(wall_s);
            }
        }
    }

    public String formatWallString(MazeTile square, int dir) {
        String str;
        boolean showUnknown = Boolean.parseBoolean(config.getProperty("eu.simmig.maze.displayUnknown", "false"));
        switch (square.getWall(dir)) {
            case 1:
                str = "* -- ";
                break;
            case -1:
                str = (showUnknown) ? "* .. " : "*    ";
                break;
            case 0:
            default:
                str = "*    ";
                break;
        }
        if (square.isLastColumn()) {
            str += "*";
        }
        return str;
    }

    public String formatWallStringWE(int value) {
        boolean showUnknown = Boolean.parseBoolean(config.getProperty("eu.simmig.maze.displayUnknown", "false"));
        switch (value) {
            case 1:
                return "|";
            case -1:
                return (showUnknown) ? ":" : " ";
        }
        return " ";
    }

    public String formatColorString(MazeTile square) {
        String str = formatWallStringWE(square.getWall(Maze.DIR_W));
        boolean showUnknown = Boolean.parseBoolean(config.getProperty("eu.simmig.maze.displayUnknown", "false"));
        switch (square.getColor()) {
            case 0:
                str += "    ";
                break;
            case 1:
                str += " ## ";
                break;
            case -1:
            default:
                str += (showUnknown) ? " ?? " : "    ";
                break;
        }
        if (square.isLastColumn()) {
            str += formatWallStringWE(square.getWall(Maze.DIR_E));
        }
        return str;
    }

    public void loadMaze(Maze maze, String filename) {
        int ix = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line = br.readLine();
            while (line != null) {
                ix += 1;
                line = line.trim();
                if (line.length() > 0 && line.charAt(0) != '#') {
                    String delimiter = "[, ]+";
                    String[] tokens = line.split(delimiter);
                    if (tokens.length != 4) {
                        continue;
                    }
                    int x = Integer.parseInt((tokens[2]));
                    int y = Integer.parseInt((tokens[1]));
                    for (int jx = 0; jx < tokens[3].length(); jx += 1) {
                        switch (tokens[0].toLowerCase().charAt(0)) {
                            case 'w':
                                int dir = Maze.translateDirection(tokens[3].toLowerCase().charAt(jx));
                                maze.drawWall(y, x, dir, true);
                                break;
                            case 'l':
                                dir = Maze.translateDirection(tokens[3].toLowerCase().charAt(jx));
                                maze.drawLine(y, x, dir, true);
                                break;
                            case 'c':
                                dir = Integer.parseInt(tokens[3]);
                                maze.setColor(x, y, dir);
                                break;
                        }
                    }
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void fillMaze(Maze maze) {
        maze.fillAll();
    }
}
