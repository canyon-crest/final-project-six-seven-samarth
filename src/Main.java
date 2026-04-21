import javax.swing.SwingUtilities;

/**
 * Launches the Grid Wars application.
 */
public class Main {
    /**
     * Program entry point.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GameFrame();
            }
        });
    }
}
