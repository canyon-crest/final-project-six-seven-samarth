/**
 * A single board tile.
 */
public class Tile {
    private int row;
    private int col;
    private String terrainType;

    /**
     * Creates a tile.
     * @param row row index
     * @param col column index
     * @param terrainType terrain label
     */
    public Tile(int row, int col, String terrainType) {
        this.row = row;
        this.col = col;
        this.terrainType = terrainType;
    }

    /**
     * @return row index
     */
    public int getRow() {
        return row;
    }

    /**
     * @return column index
     */
    public int getCol() {
        return col;
    }

    /**
     * @return terrain type
     */
    public String getTerrainType() {
        return terrainType;
    }
}
