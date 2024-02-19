package byow.lab12;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    private static void fillBoardWithNothing(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    /**
     * Draw a row of tiles to the board start with given Position.
     */
    public static void drawRow(TETile[][] tiles, Position p, TETile type, int length) {
        for (int dx = 0; dx < length; dx ++) {
            tiles[p.x + dx][p.y] = type;
        }
    }

    /**
     * Adds a hexagon to the world at position P of size size.
     */
    public static void addHexagon(TETile[][] tiles, Position p, TETile type, int size) {
        if (size < 2) {
            return;
        }
        addHexagonHelper(tiles, p, type, size, size - 1, size);
    }

    /**
     * Helper method for addHexagon.
     */
    private static void addHexagonHelper(TETile[][] tiles, Position p, TETile type, int n, int blank, int t) {
        // Draw this row
        Position startOfRow = p.shift(blank, 0);
        drawRow(tiles, startOfRow, type, t);

        // Draw remaining rows recursively
        if (blank > 0) {
            Position nextOfRow = p.shift(0, -1);
            addHexagonHelper(tiles, nextOfRow, type, n - 1, blank - 1, t + 2);
        }

        // Draw this row again to be the reflection
        Position startOfReflectedRow = startOfRow.shift(0, -(2 * n - 1));
        drawRow(tiles, startOfReflectedRow, type, t);
    }

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(5);
        switch (tileNum) {
            case 0: return Tileset.GRASS;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.SAND;
            case 3: return Tileset.MOUNTAIN;
            case 4: return Tileset.TREE;
            default: return Tileset.GRASS;
        }
    }

    /**
     * Draw a column of Hexagon with int size and int num to the world at Position p with TETile type.
     */
    public static void addHexCol(TETile[][] tiles, Position p, int size, int num) {
        // Draw this Hexagon
        addHexagon(tiles, p, randomTile(), size);
        // Draw other Hexagon below it
        if (num > 1) {
            Position bottomNeighbor = p.getBottomNeighbor(size);
            addHexCol(tiles, bottomNeighbor, size, num - 1);
        }
    }

    private static class Position {
        int x;
        int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public Position shift(int dx, int dy) {
            return new Position(this.x + dx, this.y + dy);
        }

        public Position getBottomNeighbor(int hexSize) {
            return new Position(this.x, this.y - 2*hexSize);
        }

        public Position getTopRightNeighbor(int hexSize) {
            return new Position(this.x + 2*hexSize - 1, this.y + hexSize);
        }
        public Position getBottomRightNeighbor(int hexSize) {
            return new Position(this.x + 2*hexSize - 1, this.y - hexSize);
        }
    }

    /**
     * Draws the hexagonal world
     */
    public static void drawWorld(TETile[][] tiles, Position p, int hexSize, int tessSize) {

        addHexCol(tiles, p, hexSize, tessSize);

        // Draw the right and up hexagon
        for (int i = 1; i < tessSize; i++) {
            p = p.getTopRightNeighbor(hexSize);
            addHexCol(tiles, p, hexSize, tessSize + i);
        }
        // Draw the right and down hexagon
        for (int i = 1; i < tessSize; i++) {
            p = p.getBottomRightNeighbor(hexSize);
            addHexCol(tiles, p, hexSize, 2*tessSize - 1 - i);
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] hexWorld = new TETile[WIDTH][HEIGHT];
        fillBoardWithNothing(hexWorld);

        Position p = new Position(5, 35);
        drawWorld(hexWorld, p, 4, 3);

        ter.renderFrame(hexWorld);
    }
}
