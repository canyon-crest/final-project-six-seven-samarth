/**
 * Balanced melee unit.
 */
public class Soldier extends GameUnit {
    /**
     * Creates a soldier.
     * @param row starting row
     * @param col starting column
     * @param enemy true if enemy unit
     */
    public Soldier(int row, int col, boolean enemy) {
        super(enemy ? "Enemy Soldier" : "Soldier", 12, 4, 2, 1, row, col, enemy);
    }

    /**
     * @return damage dealt by this soldier
     */
    @Override
    public int calculateDamage() {
        return getAttackPower();
    }

    /**
     * @return label for drawing
     */
    @Override
    public String getTypeLabel() {
        return "S";
    }
}
