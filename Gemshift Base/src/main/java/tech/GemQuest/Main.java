package tech.GemQuest;

import javax.swing.*;

public class Main {

    public static JFrame window;

    public static void main(String[] args) {
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("GemQuest");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

     // This will be used in the future to load configs. Currently, no txt.//
          gamePanel.getConfig().loadConfig();

        if (gamePanel.isFullScreenOn()) {
            window.setUndecorated(true);
        }

        window.pack(); // Use the JPanel component to determine window configuration

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.setUpGame();
        gamePanel.startGameThread();
    }
}
