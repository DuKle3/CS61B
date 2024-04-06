package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.List;
import java.util.Random;

public class WorldGenerator {


    private static final String NORTH = "North";
    private static final String WEST = "West";
    private static final String SOUTH = "South";
    private static final String EAST = "East";
    private static List<Room> rooms;
    private static List<Hallway> hallWays;
    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    /**
     * Return a randomRoom which
     * @return
     */
    public static Room randomRoom(TETile[][] tiles) {
        int width = tiles.length;
        int height = tiles[0].length;
    }

    public static Position randomPosition(int xBound, int yBound) {
        int x = RANDOM.nextInt(xBound);
        int y = RANDOM.nextInt(yBound);
        return new Position(x, y);
    }

    /**
     * Add the Room r into TETile[][] tiles (world).
     */
    public static void addRoom (TETile[][] tiles, Room r) {
        // addWall
        int x1 = r.topLeft.x;
        int x2 = r.bottomRight.x;
        int y1 = r.topLeft.y;
        int y2 = r.bottomRight.y;

        for (int dx = x1; dx <= x2 ; dx++) {
            for (int dy = y1; dy >= y2; dy--) {
                if (dx == x1 || dx == x2 || dy == y1 || dy == y2) {
                    tiles[dx][dy] = Tileset.WALL;
                    continue;
                }
                tiles[dx][dy] = Tileset.FLOOR;
            }
        }
        for (Position door : r.doors) {
            tiles[door.x][door.y] = Tileset.FLOOR;
        }
    }
    /**
     * Add the given Hallway into the world.
     * WARNING : Only for one width.
     */
    public static void addHallWay (TETile[][] tiles, Hallway h) {
        // add the path
        addLine(tiles, h.startPoint, h.length, h.direction, Tileset.FLOOR);

        // add the wall
        Position wallStart = h.startPoint;
        Position wallStart1 = h.startPoint;
        if (h.direction == EAST || h.direction == WEST) {
            wallStart = new Position(h.startPoint.x, h.startPoint.y + 1);
            wallStart1 = new Position(h.startPoint.x, h.startPoint.y - 1);
        }
        else {
            wallStart = new Position(h.startPoint.x + 1, h.startPoint.y);
            wallStart1 = new Position(h.startPoint.x - 1, h.startPoint.y);
        }

        addLine(tiles, wallStart, h.length, h.direction, Tileset.WALL);
        addLine(tiles, wallStart1, h.length, h.direction, Tileset.WALL);
    }

    private static void addLine (TETile[][] tiles, Position startPoint, int length, String direction, TETile tileType) {
        Position vector = directionVector(direction);
        for (int i = 0; i < length; i++) {
            tiles[startPoint.x + vector.x * i][startPoint.y + vector.y * i] = tileType;
        }
    }

    /**
     * Return a Position which represent a vector, depend on the given direction.
     */
    private static Position directionVector(String direction) {
        // dx, dy depends on the HallWay's direction.
        int dx = 0;
        int dy = 0;
        switch (direction) {
            case "North" -> dy = 1;
            case "East" -> dx = 1;
            case "West" -> dx = -1;
            case "South" -> dy = -1;
        }
        return new Position(dx, dy);
    }

    public static void fillBoardWithNothing(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    public static void generateWorld(TETile[][] world) {
        Position x1 = new Position(10, 40);
        Position x2 = new Position(30, 15);

        Room r = new Room(x1, x2);
        addRoom(world, r);
    }
}
