package sidescroller;

import javax.swing.JFrame;

/**
 * @author Mohammed Ibrahim
 */
public class GameMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame window = new JFrame("2D Scroller");
        new Assets();
        GamePanel game = new GamePanel();

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(game);
        window.pack();
//        window.setLocation(70, 50);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setVisible(true);

        window.setAlwaysOnTop(true);
    }

}
