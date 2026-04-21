/**
 * Heavier unit with high health and damage.
 */
public class Knight extends GameUnit {
    /**
     * Creates a knight.
     * @param row starting row
     * @param col starting column
     * @param enemy true if enemy unit
     */
    public Knight(int row, int col, boolean enemy) {
        super(enemy ? "Enemy Knight" : "Knight", 16, 5, 1, 1, row, col, enemy);
    }

    /**
     * @return damage dealt by this knight
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
        return "K";
    }
}
