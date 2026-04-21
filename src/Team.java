import java.util.ArrayList;
import java.util.List;

/**
 * Represents a team of units.
 */
public class Team {
    private ArrayList<GameUnit> units;

    /**
     * Creates an empty team.
     */
    public Team() {
        units = new ArrayList<GameUnit>();
    }

    /**
     * Adds a unit.
     * @param unit unit to add
     */
    public void addUnit(GameUnit unit) {
        units.add(unit);
    }

    /**
     * @return all units on the team
     */
    public List<GameUnit> getUnits() {
        return units;
    }

    /**
     * Removes dead units logically by checking if any remain alive.
     * @return true if the team still has living units
     */
    public boolean hasLivingUnits() {
        for (GameUnit unit : units) {
            if (unit.isAlive()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Resets the team's units for a new turn.
     */
    public void resetTurn() {
        for (GameUnit unit : units) {
            if (unit.isAlive()) {
                unit.resetTurn();
            }
        }
    }

    /**
     * @return true if all living units are done acting
     */
    public boolean allUnitsFinished() {
        for (GameUnit unit : units) {
            if (unit.isAlive() && unit.canActThisTurn()) {
                return false;
            }
        }
        return true;
    }
}
