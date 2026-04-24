import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import java.awt.GradientPaint;
import java.awt.BasicStroke;

/**
 * Draws the game and handles mouse clicks.
 */
public class GamePanel extends JPanel {
    private static final int TILE_SIZE = 55;
    private static final int BOARD_X = 40;
    private static final int BOARD_Y = 70;
    private static final int SIDEBAR_X = 590;
    private final Color CR_BLUE = new Color(0, 102, 204);
    private final Color CR_DARK_STONE = new Color(45, 45, 45);

    private GameController controller;

    private Rectangle startButton;
    private Rectangle instructionsButton;
    private Rectangle exitButton;
    private Rectangle backButton;
    private Rectangle endTurnButton;
    private Rectangle playAgainButton;
    private Rectangle menuButton;
    private BufferedImage soldierImage;
    private BufferedImage archerImage;
    private BufferedImage knightImage;
    
    private Clip backgroundMusic;

    /**
     * Creates the panel.
     */
    public GamePanel() {
        controller = new GameController();
        setPreferredSize(new Dimension(900, 620));
        setBackground(new Color(33, 100, 144));
        //setBackground(new Color(38, 127, 181));

        startButton = new Rectangle(330, 220, 240, 55);
        instructionsButton = new Rectangle(330, 295, 240, 55);
        exitButton = new Rectangle(330, 370, 240, 55);
        backButton = new Rectangle(330, 500, 240, 50);
        endTurnButton = new Rectangle(640, 500, 180, 45);
        playAgainButton = new Rectangle(300, 330, 140, 50);
        menuButton = new Rectangle(470, 330, 140, 50);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getX(), e.getY());
                repaint();
            }
        });
        loadImages();
    }

    /**
     * Handles all click interactions.
     * @param x x-coordinate
     * @param y y-coordinate
     */
        try {
            File soundFile = new File(soundFilePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }
    private void playBackgroundMusic(String filePath) {
        try {
            File musicFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
            
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY); 
            
            backgroundMusic.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void handleClick(int x, int y) {
        int screen = controller.getScreenState();
        

        if (screen == GameController.MENU_SCREEN) {
            if (startButton.contains(x, y)) {
            	playBackgroundMusic("sounds/background.wav");
                controller.startGame();
            } else if (instructionsButton.contains(x, y)) {
                controller.setScreenState(GameController.INSTRUCTIONS_SCREEN);
            } else if (exitButton.contains(x, y)) {
                System.exit(0);
            }
        } else if (screen == GameController.INSTRUCTIONS_SCREEN) {
            if (backButton.contains(x, y)) {
                controller.setScreenState(GameController.MENU_SCREEN);
            }
        } else if (screen == GameController.GAME_SCREEN) {
            if (endTurnButton.contains(x, y)) {
                controller.endPlayerTurn();
                return;
            }
            int row = (y - BOARD_Y) / TILE_SIZE;
            int col = (x - BOARD_X) / TILE_SIZE;
            if (x >= BOARD_X && x < BOARD_X + TILE_SIZE * 9
                    && y >= BOARD_Y && y < BOARD_Y + TILE_SIZE * 9) {
                controller.handleTileClick(row, col);
            }
        } else if (screen == GameController.END_SCREEN) {
            if (playAgainButton.contains(x, y)) {
                controller.startGame();
            } else if (menuButton.contains(x, y)) {
                controller.setScreenState(GameController.MENU_SCREEN);
            }
        }
    }

    /**
     * Paints the correct screen.
     * @param g graphics context
     */
    
    private void loadImages() {
    	 try {
    		 soldierImage = ImageIO.read(new File("images/soldier.jpg"));
    	        archerImage = ImageIO.read(new File("images/archer.jpg"));
    	        knightImage = ImageIO.read(new File("images/knight.jpg"));
    	        
    	    } catch (IOException e) {
    	        e.printStackTrace();
    	    }
    }
    
    private BufferedImage getUnitImage(GameUnit unit) {
    	if (unit instanceof Soldier) {
    	return soldierImage;
    	} else if (unit instanceof Archer) {
    	return archerImage;
    	} else if (unit instanceof Knight) {
    	return knightImage;
    	}
    	return null;
    	}
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (controller.getScreenState() == GameController.MENU_SCREEN) {
            drawMenu(g2);
        } else if (controller.getScreenState() == GameController.INSTRUCTIONS_SCREEN) {
            drawInstructions(g2);
        } else if (controller.getScreenState() == GameController.GAME_SCREEN) {
            drawGame(g2);
        } else if (controller.getScreenState() == GameController.END_SCREEN) {
            drawEndScreen(g2);
        }
        
    }


    private void drawMenu(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 42));
        g2.drawString("Tile Fight", 330, 120);

        g2.setFont(new Font("SansSerif", Font.PLAIN, 18));
        g2.drawString("If your parents won't let you download clash royale", 250, 160);

        drawButton(g2, startButton, "Start Game");
        drawButton(g2, instructionsButton, "How to Play");
        drawButton(g2, exitButton, "Exit");
    }

    private void drawInstructions(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 34));
        g2.drawString("How to Play", 330, 90);

        g2.setFont(new Font("SansSerif", Font.PLAIN, 18));
        int y = 150;
        g2.drawString("1. Click one of your units to select it.", 110, y);
        g2.drawString("2. Click an empty tile within its move range to move.", 110, y + 35);
        g2.drawString("3. Click an enemy within range to attack.", 110, y + 70);
        g2.drawString("4. Each unit may move once and attack once per turn.", 110, y + 105);
        g2.drawString("5. When your team is done, press End Turn.", 110, y + 140);
        g2.drawString("6. Defeat all enemy units before they defeat yours.", 110, y + 175);
        g2.drawString("Unit Types: Soldier = balanced, Archer = ranged, Knight = strong tank.", 110, y + 230);

        drawButton(g2, backButton, "Back to Menu");
    }

    private void drawGame(Graphics2D g2) {
        Board board = controller.getBoard();
        if (board == null) {
            return;
        }

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 28));
        g2.drawString("Grid Wars", 40, 40);

        drawBoard(g2, board);
        drawUnits(g2, controller.getPlayerUnits());
        drawUnits(g2, controller.getEnemyUnits());
        drawSidebar(g2);
    }

    private void drawBoard(Graphics2D g2, Board board) {
        for (int r = 0; r < board.getRows(); r++) {
            for (int c = 0; c < board.getCols(); c++) {
                int x = BOARD_X + c * TILE_SIZE;
                int y = BOARD_Y + r * TILE_SIZE;
                String terrain = board.getTile(r, c).getTerrainType();
                if (terrain.equals("Forest")) {
                	g2.setColor(new Color(0, 220, 0));
                } else if (terrain.equals("Plain")) {
                	g2.setColor(new Color(102,255,102));
                } else if (terrain.equals("Bridge")) {
                	g2.setColor(new Color(150,107,74));
                } else if (terrain.equals("River")) {
                	g2.setColor(new Color(45,116,250));
                }
                
                g2.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                g2.setColor(Color.DARK_GRAY);
                g2.drawRect(x, y, TILE_SIZE, TILE_SIZE);

                GameUnit selected = controller.getSelectedUnit();
                if (selected != null) {
                    int dist = Math.abs(selected.getRow() - r) + Math.abs(selected.getCol() - c);
                    if (!selected.hasMoved() && dist <= selected.getMovementRange()) {
                    	boolean crossing = (selected.getRow() < 4 && r > 4) || (selected.getRow() > 4 && r < 4);
                        boolean landing = (r == 4);
                        boolean bridge = (c == 2 || c == 6);
                        
                        if (!( (crossing || landing) && !bridge )) {
                            g2.setColor(new Color(0, 0, 225, 50));
                            g2.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                        }
                    }
                }
            }
        }

        GameUnit selected = controller.getSelectedUnit();
        if (selected != null) {
            int sx = BOARD_X + selected.getCol() * TILE_SIZE;
            int sy = BOARD_Y + selected.getRow() * TILE_SIZE;
            g2.setColor(Color.YELLOW);
            g2.drawRect(sx + 2, sy + 2, TILE_SIZE - 4, TILE_SIZE - 4);
            g2.drawRect(sx + 4, sy + 4, TILE_SIZE - 8, TILE_SIZE - 8);
        }
       
    }

    private void drawUnits(Graphics2D g2, List<GameUnit> units) {
//        for (GameUnit unit : units) {
//            if (!unit.isAlive()) {
//                continue;
//            }
//            int x = BOARD_X + unit.getCol() * TILE_SIZE;
//            int y = BOARD_Y + unit.getRow() * TILE_SIZE;
//            g2.setColor(unit.isEnemy() ? new Color(180, 70, 70) : new Color(60, 110, 210));
//            g2.fillOval(x + 10, y + 10, TILE_SIZE - 20, TILE_SIZE - 20);
//            g2.setColor(Color.WHITE);
//            g2.setFont(new Font("SansSerif", Font.BOLD, 18));
//            g2.drawString(unit.getTypeLabel(), x + 26, y + 38);
//            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
//            g2.drawString(String.valueOf(unit.getHealth()), x + 5, y + 15);
//        }
    
    		for (GameUnit unit : units) {
    		if (!unit.isAlive()) {
    		continue;
    		}

    		int x = BOARD_X + unit.getCol() * TILE_SIZE;
    		int y = BOARD_Y + unit.getRow() * TILE_SIZE;

    		g2.setColor(unit.isEnemy() ? new Color(225, 0, 0) : new Color(0, 0, 225));
    		//g2.fillOval(x + 10, y + 10, TILE_SIZE - 20, TILE_SIZE - 20);
    		g2.fillRect(x+10, y+10, TILE_SIZE-20, TILE_SIZE-20);

    		BufferedImage unitImage = getUnitImage(unit);
    		if (unitImage != null) {
    		g2.drawImage(unitImage, x + 12, y + 12, TILE_SIZE - 24, TILE_SIZE - 24, null);
    		}

    		//g2.setColor((unit.isEnemy() ? new Color(100,0,0) : new Color(0,0,100)));
    		g2.setColor(Color.WHITE);
    		g2.setFont(new Font("HelvetiaNeue", Font.BOLD, 12));
    		g2.drawString(String.valueOf(unit.getHealth()), x + 5, y + 15);
    		}
    		

    }

    private void drawSidebar(Graphics2D g2) {
        g2.setColor(new Color(50, 55, 75));
        g2.fillRoundRect(590, 70, 260, 450, 20, 20);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        g2.drawString("Status", SIDEBAR_X + 20, 105);

        g2.setFont(new Font("SansSerif", Font.PLAIN, 15));
        g2.drawString("Turn: " + controller.getTurnNumber(), SIDEBAR_X + 20, 140);
        g2.drawString("Current side: " + (controller.isPlayerTurn() ? "Player" : "Enemy"), SIDEBAR_X + 20, 165);

        drawWrappedText(g2, controller.getStatusMessage(), SIDEBAR_X + 20, 205, 220, 20);

        GameUnit selected = controller.getSelectedUnit();
        g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2.drawString("Selected Unit", SIDEBAR_X + 20, 305);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 15));
        if (selected == null) {
            g2.drawString("None", SIDEBAR_X + 20, 335);
        } else {
            g2.drawString(selected.getName(), SIDEBAR_X + 20, 335);
            g2.drawString("HP: " + selected.getHealth() + "/" + selected.getMaxHealth(), SIDEBAR_X + 20, 360);
            g2.drawString("Move: " + selected.getMovementRange(), SIDEBAR_X + 20, 385);
            g2.drawString("Range: " + selected.getAttackRange(), SIDEBAR_X + 20, 410);
            g2.drawString("Damage: " + selected.calculateDamage(), SIDEBAR_X + 20, 435);
        }

        drawButton(g2, endTurnButton, "End Turn");
    }

    private void drawEndScreen(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 42));
        g2.drawString(controller.getEndMessage(), 310, 210);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 22));
        g2.drawString(controller.getStatusMessage(), 260, 265);

        drawButton(g2, playAgainButton, "Play Again");
        drawButton(g2, menuButton, "Menu");
    }

//    private void drawButton(Graphics2D g2, Rectangle rect, String label) {
//        g2.setColor(new Color(85, 120, 220));
//        g2.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 18, 18);
//        g2.setColor(Color.WHITE);
//        g2.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 18, 18);
//        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
//        int textWidth = g2.getFontMetrics().stringWidth(label);
//        int textX = rect.x + (rect.width - textWidth) / 2;
//        int textY = rect.y + rect.height / 2 + 7;
//        g2.drawString(label, textX, textY);
//    }
    private void drawButton(Graphics2D g2, Rectangle rect, String label) {
        g2.setColor(new Color(180, 90, 0)); 
        g2.fillRoundRect(rect.x, rect.y + 5, rect.width, rect.height, 20, 20);


        GradientPaint goldGradient = new GradientPaint(
            rect.x, rect.y, new Color(255, 215, 0), 
            rect.x, rect.y + rect.height, new Color(255, 140, 0)
        );
        g2.setPaint(goldGradient);
        g2.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 20, 20);

        g2.setStroke(new BasicStroke(2));
        g2.setColor(new Color(0, 0, 0, 255)); 
        g2.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 20, 20);

     // 4. Draw the Text with a Thick Outline/Shadow Combo
        g2.setFont(new Font("SansSerif", Font.BOLD, 22));
        int textWidth = g2.getFontMetrics().stringWidth(label);
        int textX = rect.x + (rect.width - textWidth) / 2;
        int textY = rect.y + rect.height / 2 + 8;

        // --- DRAW THE BLACK OUTLINE/DEPTH ---
        g2.setColor(Color.BLACK);

        // Drawing the text slightly shifted in every direction creates the "outline"
        g2.drawString(label, textX - 1, textY - 1); // Top-left
        g2.drawString(label, textX + 1, textY - 1); // Top-right
        g2.drawString(label, textX - 1, textY + 1); // Bottom-left
        g2.drawString(label, textX + 1, textY + 1); // Bottom-right

        // Drawing it 2-3 pixels down creates that "heavy" bottom shadow look
        g2.drawString(label, textX, textY + 2); 
        g2.drawString(label, textX, textY + 3);

        // --- DRAW THE MAIN WHITE TEXT ON TOP ---
        g2.setColor(Color.WHITE);
        g2.drawString(label, textX, textY);
    }

    private void drawWrappedText(Graphics2D g2, String text, int x, int y, int width, int lineHeight) {
        String[] words = text.split(" ");
        String line = "";
        int currentY = y;
        for (String word : words) {
            String testLine = line.isEmpty() ? word : line + " " + word;
            if (g2.getFontMetrics().stringWidth(testLine) > width) {
                g2.drawString(line, x, currentY);
                currentY += lineHeight;
                line = word;
            } else {
                line = testLine;
            }
        }
        if (!line.isEmpty()) {
            g2.drawString(line, x, currentY);
        }
    }
}
