import java.util.List;

/**
 * Controls enemy turns using a simple scoring algorithm.
 */
public class EnemyAI {
    /**
     * Takes the full AI turn.
     * @param board game board
     * @param enemies enemy units
     * @param players player units
     * @return log message describing AI actions
     */
    public String takeTurn(Board board, List<GameUnit> enemies, List<GameUnit> players) {
        StringBuilder log = new StringBuilder();

        for (GameUnit enemy : enemies) {
            if (!enemy.isAlive()) {
                continue;
            }

            GameUnit immediateTarget = chooseAttackTarget(enemy, players);
            if (immediateTarget != null) {
                int damage = enemy.calculateDamage();
                immediateTarget.takeDamage(damage);
                enemy.setHasAttacked(true);
                enemy.setHasMoved(true);
                log.append(enemy.getName()).append(" attacked ")
                   .append(immediateTarget.getName()).append(" for ")
                   .append(damage).append(" damage. ");
                continue;
            }

            GameUnit target = chooseClosestLivingTarget(enemy, players);
            if (target == null) {
                continue;
            }

            int[] bestMove = chooseBestMove(board, enemy, target, enemies, players);
            if (bestMove[0] != enemy.getRow() || bestMove[1] != enemy.getCol()) {
                enemy.move(bestMove[0], bestMove[1]);
                log.append(enemy.getName()).append(" moved to (")
                   .append(bestMove[0]).append(", ").append(bestMove[1]).append("). ");
            }

            GameUnit postMoveTarget = chooseAttackTarget(enemy, players);
            if (postMoveTarget != null) {
                int damage = enemy.calculateDamage();
                postMoveTarget.takeDamage(damage);
                enemy.setHasAttacked(true);
                log.append(enemy.getName()).append(" attacked ")
                   .append(postMoveTarget.getName()).append(" for ")
                   .append(damage).append(" damage. ");
            }

            if (!enemy.hasAttacked()) {
                enemy.performTurnAction();
            }
        }

        if (log.length() == 0) {
            return "Enemy turn ended.";
        }
        return log.toString();
    }

    /**
     * Picks the weakest target that can be attacked now.
     * @param attacker enemy unit
     * @param players player units
     * @return chosen target or null
     */
    public GameUnit chooseAttackTarget(GameUnit attacker, List<GameUnit> players) {
        GameUnit bestTarget = null;
        int bestHealth = Integer.MAX_VALUE;
        for (GameUnit player : players) {
            if (attacker.canAttack(player) && player.getHealth() < bestHealth) {
                bestHealth = player.getHealth();
                bestTarget = player;
            }
        }
        return bestTarget;
    }

    /**
     * Chooses the closest living player unit.
     * @param enemy enemy unit
     * @param players player units
     * @return target or null
     */
    public GameUnit chooseClosestLivingTarget(GameUnit enemy, List<GameUnit> players) {
        GameUnit bestTarget = null;
        int bestDistance = Integer.MAX_VALUE;
        for (GameUnit player : players) {
            if (!player.isAlive()) {
                continue;
            }
            int distance = distance(enemy.getRow(), enemy.getCol(), player.getRow(), player.getCol());
            if (distance < bestDistance) {
                bestDistance = distance;
                bestTarget = player;
            }
        }
        return bestTarget;
    }

    /**
     * Scores all possible movement spaces and chooses the best one.
     * @param board game board
     * @param enemy moving unit
     * @param target desired target
     * @param enemies enemy units
     * @param players player units
     * @return row and column of best move
     */
    public int[] chooseBestMove(Board board, GameUnit enemy, GameUnit target,
                                List<GameUnit> enemies, List<GameUnit> players) {
        int bestRow = enemy.getRow();
        int bestCol = enemy.getCol();
        int bestScore = evaluateMove(enemy, target, enemy.getRow(), enemy.getCol());

        for (int r = 0; r < board.getRows(); r++) {
            for (int c = 0; c < board.getCols(); c++) {
                int moveDistance = distance(enemy.getRow(), enemy.getCol(), r, c);
                if (!board.isWithinBounds(r, c) || moveDistance > enemy.getMovementRange()) {
                    continue;
                }
                if ((r != enemy.getRow() || c != enemy.getCol())
                        && board.isTileOccupied(r, c, mergeLists(enemies, players))) {
                    continue;
                }

                int score = evaluateMove(enemy, target, r, c);
                if (score > bestScore) {
                    bestScore = score;
                    bestRow = r;
                    bestCol = c;
                }
            }
        }
        return new int[]{bestRow, bestCol};
    }

    /**
     * Evaluates a move for the AI.
     * Higher is better.
     * @param enemy moving unit
     * @param target chosen target
     * @param row candidate row
     * @param col candidate col
     * @return score of the move
     */
    private int evaluateMove(GameUnit enemy, GameUnit target, int row, int col) {
        int targetDistance = distance(row, col, target.getRow(), target.getCol());
        int score = 100 - targetDistance * 10;
        if (targetDistance <= enemy.getAttackRange()) {
            score += 60;
        }
        if (enemy instanceof Archer && targetDistance == 1) {
            score -= 15;
        }
        if (enemy instanceof Knight && targetDistance <= 1) {
            score += 10;
        }
        return score;
    }

    /**
     * Computes Manhattan distance.
     * @param r1 row 1
     * @param c1 col 1
     * @param r2 row 2
     * @param c2 col 2
     * @return distance
     */
    private int distance(int r1, int c1, int r2, int c2) {
        return Math.abs(r1 - r2) + Math.abs(c1 - c2);
    }

    /**
     * Merges two lists without duplicates mattering.
     * @param a first list
     * @param b second list
     * @return array of all units
     */
    private java.util.ArrayList<GameUnit> mergeLists(List<GameUnit> a, List<GameUnit> b) {
        java.util.ArrayList<GameUnit> merged = new java.util.ArrayList<GameUnit>();
        merged.addAll(a);
        merged.addAll(b);
        return merged;
    }
}
