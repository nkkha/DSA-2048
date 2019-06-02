package dsa.project.gui;

import dsa.project.game.DrawUtils;
import dsa.project.game.Game;

import java.awt.*;
import java.awt.event.ActionEvent;

public class MainMenuPanel extends GuiPanel {

    private Font titleFont = Game.main.deriveFont(120f);
    private Font creatorFont = Game.main.deriveFont(24f);
    private String title = "2048";
    private String sign = "Project DSA";
    private int buttonWidth = 220;
    //    private ScoreManager scores;
    private int a = 310;
    public GuiButton Resume;
    public GuiButton playButton;
    public GuiButton scoresButton;
    public GuiButton quitButton;

    public MainMenuPanel() {
        super();
        playButton = new GuiButton(Game.WIDTH / 2 - buttonWidth / 2, a, buttonWidth, 60);
        scoresButton = new GuiButton(Game.WIDTH / 2 - buttonWidth / 2, a + 90, buttonWidth, 60);
        quitButton = new GuiButton(Game.WIDTH / 2 - buttonWidth / 2, a + 180, buttonWidth, 60);

        playButton.setText("Play");
        scoresButton.setText("Scores");
        quitButton.setText("Quit");

        playButton.addActionListener((ActionEvent e) -> {
            GuiScreen.getInstance().setCurrentPanel("Play");
        });
        add(playButton);

        scoresButton.addActionListener((ActionEvent e) -> {
            GuiScreen.getInstance().setCurrentPanel("Leaderboards");
        });
        add(scoresButton);

        quitButton.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
        add(quitButton);
    }

    @Override
    public void update() {
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);
        g.setFont(titleFont);
        g.setColor(Color.RED);
        g.drawString(title, Game.WIDTH / 2 - DrawUtils.getMessageWidth(title, titleFont, g) / 2, 200);
        g.setFont(creatorFont);
        g.drawString(sign, 20, Game.HEIGHT - 10);
    }

}
