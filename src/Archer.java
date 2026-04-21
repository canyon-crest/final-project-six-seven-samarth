/**
 * Ranged unit with longer attack range.
 */
public class Archer extends GameUnit {
    /**
     * Creates an archer.
     * @param row starting row
     * @param col starting column
     * @param enemy true if enemy unit
     */
    public Archer(int row, int col, boolean enemy) {
        super(enemy ? "Enemy Archer" : "Archer", 9, 3, 2, 3, row, col, enemy);
    }

    /**
     * @return damage dealt by this archer
     */
    @Override
    public int calculateDamage() {
        return getAttackPower() + 1;
    }

    /**
     * @return label for drawing
     */
    @Override
    public String getTypeLabel() {
        return "A";
    }
}
