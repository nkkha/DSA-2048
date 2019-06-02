package dsa.project.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Tile {

    public static final int WIDTH = 80;
    public static final int HEIGHT = 80;
    public static final int SLIDE_SPEED = 20;
    public static final int ARC_WIDTH = 15;
    public static final int ARC_HEIGHT = 15;

    private int value;
    private BufferedImage tileImage;
    private Color background;
    private Color text;
    private Font font;
    private Point slideTo;
    private int x;
    private int y;

    private boolean beginningAnimation = true;
    private double scaleFirst = 0.1;
    private BufferedImage beginningImage;

    private boolean combineAnimation = false;
    private double scaleCombine = 1.2;
    private BufferedImage combineImage;
    private boolean canCombine = true;

    public Tile(int value, int x, int y) {
        this.value = value;
        this.x = x;
        this.y = y;
        slideTo = new Point(x, y);
        tileImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        beginningImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        combineImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        drawImage();
    }

    private void drawImage() {
        Graphics2D g = (Graphics2D) tileImage.getGraphics();
        if (value == 2) {
            background = Color.WHITE;
            text = Color.BLACK;
        } else if (value == 4) {
            background = Color.PINK;
            text = Color.BLACK;
        } else if (value == 8) {
            background = Color.CYAN;
            text = Color.BLACK;
        } else if (value == 16) {
            background = Color.ORANGE;
            text = Color.WHITE;
        } else if (value == 32) {
            background = Color.GREEN;
            text = Color.WHITE;
        } else if (value == 64) {
            background = Color.RED;
            text = Color.WHITE;
        } else if (value == 128) {
            background = Color.MAGENTA;
            text = Color.WHITE;
        } else if (value == 256) {
            background = Color.BLUE;
            text = Color.WHITE;
        } else if (value == 512) {
            background = new Color(0xf5e455);
            text = Color.WHITE;
        } else if (value == 1024) {
            background = new Color(0xf7e12c);
            text = Color.WHITE;
        } else if (value == 2048) {
            background = new Color(0xffe400);
            text = Color.WHITE;
        } else {
            background = Color.BLUE;
            text = Color.WHITE;
        }

        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(background);
        g.fillRoundRect(0, 0, WIDTH, HEIGHT, ARC_WIDTH, ARC_HEIGHT);

        g.setColor(text);

        if (value <= 64) {
            font = Game.main.deriveFont(36f);
        } else {
            font = Game.main;
        }
        g.setFont(font);

        int drawX = WIDTH /2 - DrawUtils.getMessageWidth("" + value, font, g) / 2;
        int drawY = HEIGHT / 2 + DrawUtils.getMessageHeight("" + value, font, g) / 2;
        g.drawString("" + value, drawX, drawY);
        g.dispose();
    }

    public void update() {

    }

    public void render(Graphics2D g) {
        g.drawImage(tileImage, x, y, null);
    }

    public int getValue() {
        return value;
    }

    public void setValue (int value) {
        this.value = value;
        drawImage();
    }

    public boolean CanCombine() {
        return canCombine;
    }

    public void setCanCombine(boolean canCombine) {
        this.canCombine = canCombine;
    }

    public Point getSlideTo() {
        return slideTo;
    }

    public void setSlideTo(Point slideTo) {
        this.slideTo = slideTo;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

}
