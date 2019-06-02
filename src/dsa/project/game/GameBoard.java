package dsa.project.game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;

import com.sun.glass.events.KeyEvent;

public class GameBoard {

    public static final int ROWS = 5;
    public static final int COLS = 5;

    private final int startingTiles = 2;
    private Tile[][] board;
    private boolean dead;
    private boolean won;
    private BufferedImage gameBoard;
    private BufferedImage finalBoard;
    private int x;
    private int y;
    private int score;
    private int highScore;
    private Font scoreFont;

    private static int SPACING = 10;
    public static int BOARD_WIDTH = (COLS + 1) * SPACING + COLS * Tile.WIDTH;
    public static int BOARD_HEIGHT = (ROWS + 1) * SPACING + ROWS * Tile.HEIGHT;

    private boolean hasStarted;

    // Saving
    private String saveDataPath;
    private String fileName = "SaveData";

    public GameBoard(int x, int y) {
        try {
            saveDataPath = GameBoard.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        scoreFont = Game.main.deriveFont(28f);
        this.x = x;
        this.y = y;
        board = new Tile[ROWS][COLS];
        gameBoard = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT, BufferedImage.TYPE_INT_RGB);
        finalBoard = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT, BufferedImage.TYPE_INT_RGB);

        loadHighScore();
        createBoardImage();
        start();
    }

    private void createSaveData() {
        try {
            File file = new File(saveDataPath, fileName);

            FileWriter output = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(output);
            writer.write("" + 0);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadHighScore() {
        try {
            File f = new File(saveDataPath, fileName);
            if (!f.isFile()) {
                createSaveData();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            highScore = Integer.parseInt(reader.readLine());
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setHighScore() {
        FileWriter output = null;

        try {
            File f = new File(saveDataPath, fileName);
            output = new FileWriter(f);
            BufferedWriter writer = new BufferedWriter(output);

            writer.write("" + score);

            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createBoardImage() {
        Graphics2D g = (Graphics2D) gameBoard.getGraphics();
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int x = SPACING + SPACING * col + Tile.WIDTH * col;
                int y = SPACING + SPACING * row + Tile.HEIGHT * row;
                if (col == 2 && row == 2) {
                    g.setColor(Color.BLACK);
                    g.fillRoundRect(x, y, Tile.WIDTH, Tile.HEIGHT, Tile.ARC_WIDTH, Tile.ARC_HEIGHT);
                } else {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRoundRect(x, y, Tile.WIDTH, Tile.HEIGHT, Tile.ARC_WIDTH, Tile.ARC_HEIGHT);
                }
            }
        }
    }

    private void start() {
        for (int i = 0; i < startingTiles; i++) {
            spawnRandom();
        }
    }

    private void spawnRandom() {
        Random random = new Random();
        boolean notValid = true;

        while (notValid) {
            int location = random.nextInt(ROWS * COLS);
            int row = location / ROWS;
            int col = location % COLS;
            if (row == 2 && col == 2) {
                notValid = false;
            } else { Tile current = board[row][col];
                if (current == null) {
                    int value = random.nextInt(10) < 9 ? 2 : 4;
                    Tile tile = new Tile(value, getTileX(col), getTileY(row));
                    board[row][col] = tile;
                    notValid = false;
                }
            }
        }
    }

    public int getTileX(int col) {
        return SPACING + col * Tile.WIDTH + col * SPACING;
    }

    public int getTileY(int row) {
        return SPACING + row * Tile.HEIGHT + row * SPACING;
    }

    public void render(Graphics2D g) {
        Graphics2D g2d = (Graphics2D) finalBoard.getGraphics();
        g2d.drawImage(gameBoard, 0, 0, null);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Tile current = board[row][col];
                if (current == null)
                    continue;
                current.render(g2d);
            }
        }

        g.drawImage(finalBoard, x, y, null);
        g2d.dispose();

        g.setColor(Color.BLACK);
        g.setFont(scoreFont);
        g.drawString("Score: " + score, 30, 50);
        g.setColor(Color.RED);
        g.drawString("High Score: " + highScore, Game.WIDTH - DrawUtils.getMessageWidth("High Score" + highScore, scoreFont, g) - 50, 50);

    }

    public void update() {
        checkKeys();

        if (score > highScore) {
            highScore = score;
        }

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Tile current = board[row][col];
                if (current == null)
                    continue;
                current.update();
                resetPosition(current, row, col);
                if (current.getValue() == 2048) {
                    won = true;
                }
            }
        }
    }

    private void resetPosition(Tile current, int row, int col) {
        if (current == null)
            return;

        int x = getTileX(col);
        int y = getTileY(row);

        int distX = current.getX() - x;
        int distY = current.getY() - y;

        if (Math.abs(distX) < Tile.SLIDE_SPEED) {
            current.setX(current.getX() - distX);
        }

        if (Math.abs(distY) < Tile.SLIDE_SPEED) {
            current.setY(current.getY() - distY);
        }

        if (distX < 0) {
            current.setX(current.getX() + Tile.SLIDE_SPEED);
        }

        if (distY < 0) {
            current.setY(current.getY() + Tile.SLIDE_SPEED);
        }

        if (distX > 0) {
            current.setX(current.getX() - Tile.SLIDE_SPEED);
        }

        if (distY > 0) {
            current.setY(current.getY() - Tile.SLIDE_SPEED);
        }
    }

    private boolean move(int row, int col, int horizontalDirection, int verticalDirection, Direction dir) {
        boolean canMove = false;

        Tile current = board[row][col];
        if (current == null)
            return false;
        boolean move = true;
        int newCol = col;
        int newRow = row;
        while (move) {
            newCol += horizontalDirection;
            newRow += verticalDirection;
            if (checkBlock(newRow, newCol))
                break;
            if (checkOutOfBounds(dir, newRow, newCol))
                break;
            if (board[newRow][newCol] == null) {
                board[newRow][newCol] = current;
                board[newRow - verticalDirection][newCol - horizontalDirection] = null;
                board[newRow][newCol].setSlideTo(new Point(newRow, newCol));
                canMove = true;
            } else if (board[newRow][newCol].getValue() == current.getValue() && board[newRow][newCol].CanCombine()) {
                board[newRow][newCol].setCanCombine(false);
                board[newRow][newCol].setValue(board[newRow][newCol].getValue() * 2);
                canMove = true;
                board[newRow - verticalDirection][newCol - horizontalDirection] = null;
                board[newRow][newCol].setSlideTo(new Point(newRow, newCol));
                score += board[newRow][newCol].getValue();
            } else {
                move = false;
            }
        }
        return canMove;
    }

    private boolean checkBlock(int row, int col) {
        if (col == 2 && row == 2) return true;
        return false;
    }

    private boolean checkOutOfBounds(Direction dir, int row, int col) {
        if (dir == Direction.LEFT) {
            return col < 0;
        } else if (dir == Direction.RIGHT) {
            return col > COLS - 1;
        } else if (dir == Direction.UP) {
            return row < 0;
        } else if (dir == Direction.DOWN) {
            return row > ROWS - 1;
        }
        return false;
    }

    private void moveTiles(Direction dir) {
        boolean canMove = false;
        int horizontalDirection = 0;
        int verticalDirection = 0;

        if (dir == Direction.LEFT) {
            horizontalDirection = -1;
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (!canMove) {
                        canMove = move(row, col, horizontalDirection, verticalDirection, dir);
                    } else {
                        move(row, col, horizontalDirection, verticalDirection, dir);
                    }
                }
            }
        }

        else if (dir == Direction.RIGHT) {
            horizontalDirection = 1;
            for (int row = 0; row < ROWS; row++) {
                for (int col = COLS - 1; col >= 0; col--) {
                    if (!canMove) {
                        canMove = move(row, col, horizontalDirection, verticalDirection, dir);
                    } else {
                        move(row, col, horizontalDirection, verticalDirection, dir);
                    }
                }
            }
        }

        else if (dir == Direction.UP) {
            verticalDirection = -1;
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (!canMove) {
                        canMove = move(row, col, horizontalDirection, verticalDirection, dir);
                    } else {
                        move(row, col, horizontalDirection, verticalDirection, dir);
                    }
                }
            }
        }

        else if (dir == Direction.DOWN) {
            verticalDirection = 1;
            for (int row = ROWS - 1; row >= 0; row--) {
                for (int col = 0; col < COLS; col++) {
                    if (!canMove) {
                        canMove = move(row, col, horizontalDirection, verticalDirection, dir);
                    } else {
                        move(row, col, horizontalDirection, verticalDirection, dir);
                    }
                }
            }
        } else {
            System.out.println(dir + "is not a valid direction.");
        }

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Tile current = board[row][col];
                if (current == null)
                    continue;
                current.setCanCombine(true);
            }
        }

        if (canMove) {
            spawnRandom();
            checkDead();
        }

    }

    private void checkDead() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (board[row][col] == null)
                    return;
                if (checkSurroundingTiles(row, col, board[row][col])) {
                    return;
                }
            }
        }

        dead = true;
        setHighScore();
    }

    private boolean checkSurroundingTiles(int row, int col, Tile current) {
        if (row > 0) {
            Tile check = board[row - 1][col];
            if (check == null)
                return true;
            if (current.getValue() == check.getValue())
                return true;
        }
        if (row < ROWS - 1) {
            Tile check = board[row + 1][col];
            if (check == null)
                return true;
            if (current.getValue() == check.getValue())
                return true;
        }
        if (col > 0) {
            Tile check = board[row][col - 1];
            if (check == null)
                return true;
            if (current.getValue() == check.getValue())
                return true;
        }
        if (col < COLS - 1) {
            Tile check = board[row][col + 1];
            if (check == null)
                return true;
            if (current.getValue() == check.getValue())
                return true;
        }
        return false;

    }

    private void checkKeys() {
        if (Keyboard.typed(KeyEvent.VK_LEFT)) {
            moveTiles(Direction.LEFT);
            if (!hasStarted)
                hasStarted = true;
        }
        if (Keyboard.typed(KeyEvent.VK_RIGHT)) {
            moveTiles(Direction.RIGHT);
            if (!hasStarted)
                hasStarted = true;
        }
        if (Keyboard.typed(KeyEvent.VK_UP)) {
            moveTiles(Direction.UP);
            if (!hasStarted)
                hasStarted = true;
        }
        if (Keyboard.typed(KeyEvent.VK_DOWN)) {
            moveTiles(Direction.DOWN);
            if (!hasStarted)
                hasStarted = true;
        }
    }

}
