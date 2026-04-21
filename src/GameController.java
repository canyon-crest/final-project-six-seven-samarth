import java.util.ArrayList;
import java.util.List;

/**
 * Coordinates the game state and rules.
 */
public class GameController {
    public static final int MENU_SCREEN = 0;
    public static final int INSTRUCTIONS_SCREEN = 1;
    public static final int GAME_SCREEN = 2;
    public static final int END_SCREEN = 3;

    private Board board;
    private Team playerTeam;
    private Team enemyTeam;
    private EnemyAI ai;
    private GameUnit selectedUnit;
    private boolean playerTurn;
    private int turnNumber;
    private int screenState;
    private String statusMessage;
    private String endMessage;

    /**
     * Creates a controller and starts on the menu screen.
     */
    public GameController() {
        screenState = MENU_SCREEN;
        statusMessage = "Welcome to Grid Wars!";
        endMessage = "";
    }

    /**
     * Sets up a new game.
     */
    public void startGame() {
        board = new Board(8, 8);
        playerTeam = new Team();
        enemyTeam = new Team();
        ai = new EnemyAI();
        selectedUnit = null;
        playerTurn = true;
        turnNumber = 1;
        screenState = GAME_SCREEN;
        endMessage = "";

        playerTeam.addUnit(new Soldier(6, 1, false));
        playerTeam.addUnit(new Archer(7, 2, false));
        playerTeam.addUnit(new Knight(6, 3, false));

        enemyTeam.addUnit(new Soldier(1, 6, true));
        enemyTeam.addUnit(new Archer(0, 5, true));
        enemyTeam.addUnit(new Knight(1, 4, true));

        playerTeam.resetTurn();
        enemyTeam.resetTurn();
        statusMessage = "Player turn: select a unit.";
    }

    /**
     * Handles clicks inside the board area.
     * @param row clicked row
     * @param col clicked col
     */
    public void handleTileClick(int row, int col) {
        if (screenState != GAME_SCREEN || !playerTurn || !board.isWithinBounds(row, col)) {
            return;
        }

        ArrayList<GameUnit> allUnits = getAllUnits();
        GameUnit clickedUnit = board.getUnitAt(row, col, allUnits);

        if (selectedUnit == null) {
            if (clickedUnit != null && !clickedUnit.isEnemy() && clickedUnit.isAlive() && clickedUnit.canActThisTurn()) {
                selectedUnit = clickedUnit;
                statusMessage = selectedUnit.getName() + " selected.";
            } else {
                statusMessage = "Select one of your available units.";
            }
            return;
        }

        if (clickedUnit != null && !clickedUnit.isEnemy() && clickedUnit != selectedUnit) {
            if (clickedUnit.canActThisTurn()) {
                selectedUnit = clickedUnit;
                statusMessage = selectedUnit.getName() + " selected.";
            } else {
                statusMessage = "That unit already finished its turn.";
            }
            return;
        }

        if (clickedUnit != null && clickedUnit.isEnemy()) {
            attemptAttack(clickedUnit);
        } else {
            attemptMove(row, col);
        }

        if (selectedUnit != null && !selectedUnit.canActThisTurn()) {
            selectedUnit = null;
            if (playerTeam.allUnitsFinished()) {
                endPlayerTurn();
            }
        }
        checkWinCondition();
    }

    /**
     * Attempts to move the selected unit.
     * @param row destination row
     * @param col destination col
     */
    public void attemptMove(int row, int col) {
        if (selectedUnit == null) {
            return;
        }
        if (selectedUnit.hasMoved()) {
            statusMessage = "This unit already moved.";
            return;
        }
        int distance = manhattan(selectedUnit.getRow(), selectedUnit.getCol(), row, col);
        if (distance <= selectedUnit.getMovementRange()
                && !board.isTileOccupied(row, col, getAllUnits())) {
            selectedUnit.move(row, col);
            statusMessage = selectedUnit.getName() + " moved.";
        } else {
            statusMessage = "Invalid move.";
        }
    }

    /**
     * Attempts to attack a target.
     * @param target target unit
     */
    public void attemptAttack(GameUnit target) {
        if (selectedUnit == null) {
            return;
        }
        if (selectedUnit.canAttack(target)) {
            int damage = selectedUnit.calculateDamage();
            target.takeDamage(damage);
            selectedUnit.setHasAttacked(true);
            if (!selectedUnit.hasMoved()) {
                selectedUnit.setHasMoved(true);
            }
            statusMessage = selectedUnit.getName() + " attacked " + target.getName()
                    + " for " + damage + " damage.";
        } else {
            statusMessage = "Target out of range or attack already used.";
        }
    }

    /**
     * Ends the player's turn and runs the AI turn.
     */
    public void endPlayerTurn() {
        if (screenState != GAME_SCREEN) {
            return;
        }
        selectedUnit = null;
        playerTurn = false;
        enemyTeam.resetTurn();
        statusMessage = ai.takeTurn(board, enemyTeam.getUnits(), playerTeam.getUnits());
        removeDeadSelections();
        checkWinCondition();
        if (screenState == GAME_SCREEN) {
            turnNumber++;
            playerTurn = true;
            playerTeam.resetTurn();
            statusMessage += " Player turn " + turnNumber + " begins.";
        }
    }

    /**
     * Removes a stale selected unit if it died.
     */
    private void removeDeadSelections() {
        if (selectedUnit != null && !selectedUnit.isAlive()) {
            selectedUnit = null;
        }
    }

    /**
     * Checks whether either team has won.
     */
    public void checkWinCondition() {
        if (!enemyTeam.hasLivingUnits()) {
            screenState = END_SCREEN;
            endMessage = "You Win!";
            statusMessage = "All enemy units defeated.";
        } else if (!playerTeam.hasLivingUnits()) {
            screenState = END_SCREEN;
            endMessage = "Game Over";
            statusMessage = "Your team was defeated.";
        }
    }

    /**
     * Returns all units in one list.
     * @return combined list
     */
    public ArrayList<GameUnit> getAllUnits() {
        ArrayList<GameUnit> units = new ArrayList<GameUnit>();
        units.addAll(playerTeam.getUnits());
        units.addAll(enemyTeam.getUnits());
        return units;
    }

    /**
     * @return board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * @return player units
     */
    public List<GameUnit> getPlayerUnits() {
        return playerTeam.getUnits();
    }

    /**
     * @return enemy units
     */
    public List<GameUnit> getEnemyUnits() {
        return enemyTeam.getUnits();
    }

    /**
     * @return selected unit or null
     */
    public GameUnit getSelectedUnit() {
        return selectedUnit;
    }

    /**
     * @return true if it is the player's turn
     */
    public boolean isPlayerTurn() {
        return playerTurn;
    }

    /**
     * @return turn number
     */
    public int getTurnNumber() {
        return turnNumber;
    }

    /**
     * @return active screen state
     */
    public int getScreenState() {
        return screenState;
    }

    /**
     * Sets the current screen.
     * @param screenState new screen state
     */
    public void setScreenState(int screenState) {
        this.screenState = screenState;
    }

    /**
     * @return status message
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * @return end message
     */
    public String getEndMessage() {
        return endMessage;
    }

    /**
     * Calculates Manhattan distance.
     * @param r1 row 1
     * @param c1 col 1
     * @param r2 row 2
     * @param c2 col 2
     * @return distance
     */
    private int manhattan(int r1, int c1, int r2, int c2) {
        return Math.abs(r1 - r2) + Math.abs(c1 - c2);
    }
}
