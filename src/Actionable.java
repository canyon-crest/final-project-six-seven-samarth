public interface Actionable {
    void performTurnAction();
    boolean canAttack(GameUnit target);
}
