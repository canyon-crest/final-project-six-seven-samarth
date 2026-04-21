import javax.swing.JFrame;

/**
 * Main window for Grid Wars.
 */
public class GameFrame extends JFrame {
    /**
     * Creates and shows the game window.
     */
    public GameFrame() {
        setTitle("Grid Wars");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(new GamePanel());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
