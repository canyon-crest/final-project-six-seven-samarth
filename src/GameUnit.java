/**
 * Abstract base class for all units in the game.
 */
public abstract class GameUnit implements Actionable {
    private String name;
    private int health;
    private int maxHealth;
    private int attackPower;
    private int movementRange;
    private int attackRange;
    private int row;
    private int col;
    private boolean hasMoved;
    private boolean hasAttacked;
    private boolean enemy;

    /**
     * Creates a game unit.
     * @param name unit name
     * @param health starting and max health
     * @param attackPower base attack power
     * @param movementRange number of spaces the unit may move
     * @param attackRange number of spaces the unit may attack
     * @param row starting row
     * @param col starting column
     * @param enemy true if the unit belongs to the AI team
     */
    public GameUnit(String name, int health, int attackPower, int movementRange, int attackRange,
                    int row, int col, boolean enemy) {
        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.attackPower = attackPower;
        this.movementRange = movementRange;
        this.attackRange = attackRange;
        this.row = row;
        this.col = col;
        this.enemy = enemy;
        this.hasMoved = false;
        this.hasAttacked = false;
    }

    /**
     * Moves the unit.
     * @param newRow destination row
     * @param newCol destination column
     */
    public void move(int newRow, int newCol) {
        this.row = newRow;
        this.col = newCol;
        this.hasMoved = true;
    }

    /**
     * Applies damage to this unit.
     * @param amount amount of damage
     */
    public void takeDamage(int amount) {
        health -= amount;
        if (health < 0) {
            health = 0;
        }
    }

    /**
     * Heals the unit up to max health.
     * @param amount amount to heal
     */
    public void heal(int amount) {
        health += amount;
        if (health > maxHealth) {
            health = maxHealth;
        }
    }

    /**
     * @return true if the unit is alive
     */
    public boolean isAlive() {
        return health > 0;
    }

    /**
     * @return true if the unit can still do something this turn
     */

    
    public boolean canActThisTurn() {
        return isAlive() && (!hasMoved || !hasAttacked);
    }

    /**
     * Marks this unit as fully acted.
     */
    @Override
    public void performTurnAction() {
        hasMoved = true;
        hasAttacked = true;
    }

    /**
     * Resets turn flags.
     * @return nothing
     */
    public void resetTurn() {
        hasMoved = false;
        hasAttacked = false;
    }

    /**
     * Returns whether this unit can attack a target.
     * @param target target unit
     * @return true if target is in range and on the opposite team
     */
    @Override
    public boolean canAttack(GameUnit target) {
        if (target == null || !target.isAlive() || target.isEnemy() == enemy || hasAttacked) {
            return false;
        }
        int distance = Math.abs(row - target.getRow()) + Math.abs(col - target.getCol());
        return distance <= attackRange;
    }

    /**
     * Calculates unit-specific damage.
     * @return damage amount
     */
    public abstract int calculateDamage();

    /**
     * @return unit display symbol
     */
    public abstract String getTypeLabel();

    /**
     * @return unit name
     */
    public String getName() {
        return name;
    }

    /**
     * @return current health
     */
    public int getHealth() {
        return health;
    }

    /**
     * @return maximum health
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * @return base attack power
     */
    public int getAttackPower() {
        return attackPower;
    }

    /**
     * @return movement range
     */
    public int getMovementRange() {
        return movementRange;
    }

    /**
     * @return attack range
     */
    public int getAttackRange() {
        return attackRange;
    }

    /**
     * @return current row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return current column
     */
    public int getCol() {
        return col;
    }

    /**
     * @return true if enemy-controlled
     */
    public boolean isEnemy() {
        return enemy;
    }

    /**
     * @return true if the unit already moved this turn
     */
    public boolean hasMoved() {
        return hasMoved;
    }

    /**
     * @return true if the unit already attacked this turn
     */
    public boolean hasAttacked() {
        return hasAttacked;
    }

    /**
     * Sets movement flag.
     * @param moved new movement flag
     */
    public void setHasMoved(boolean moved) {
        hasMoved = moved;
    }

    /**
     * Sets attack flag.
     * @param attacked new attack flag
     */
    public void setHasAttacked(boolean attacked) {
        hasAttacked = attacked;
    }
}
