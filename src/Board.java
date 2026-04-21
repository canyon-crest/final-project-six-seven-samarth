import java.util.List;

/**
 * Stores board dimensions and tiles.
 */
public class Board {
    private Tile[][] grid;
    private int rows;
    private int cols;

    /**
     * Creates a board with alternating terrain labels.
     * @param rows number of rows
     * @param cols number of columns
     */
    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        grid = new Tile[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                String terrain = ((r + c) % 2 == 0) ? "Forest" : "Plain";
                grid[r][c] = new Tile(r, c, terrain);
            }
        }
    }

    /**
     * Checks whether a location is on the board.
     * @param row row index
     * @param col column index
     * @return true if inside the board
     */
    public boolean isWithinBounds(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    /**
     * Gets a tile.
     * @param row row index
     * @param col column index
     * @return tile or null if out of bounds
     */
    public Tile getTile(int row, int col) {
        if (!isWithinBounds(row, col)) {
            return null;
        }
        return grid[row][col];
    }

    /**
     * Finds the unit on a given tile.
     * @param row row index
     * @param col column index
     * @param units all units to search
     * @return unit on the tile or null
     */
    public GameUnit getUnitAt(int row, int col, List<GameUnit> units) {
        for (GameUnit unit : units) {
            if (unit.isAlive() && unit.getRow() == row && unit.getCol() == col) {
                return unit;
            }
        }
        return null;
    }

    /**
     * Checks whether a tile is occupied.
     * @param row row index
     * @param col column index
     * @param units units to search
     * @return true if occupied
     */
    public boolean isTileOccupied(int row, int col, List<GameUnit> units) {
        return getUnitAt(row, col, units) != null;
    }

    /**
     * @return number of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * @return number of columns
     */
    public int getCols() {
        return cols;
    }
}
