package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.List;
import java.util.Random;

public class WorldGenerator {
    private static List<Room> rooms;
    private static List<HallWay> hallWays;
    public static void addRoom (TETile[][] tiles, Room r) {
        // addWall
        int x1 = r.leftUp.x;
        int x2 = r.rightBottom.x;
        int y1 = r.leftUp.y;
        int y2 = r.rightBottom.y;

        for (int dx = x1; dx <= x2 ; dx++) {
            for (int dy = y1; dy >= y2; dy--) {
                if (dx == x1 || dx == x2 || dy == y1 || dy == y2) {
                    tiles[dx][dy] = Tileset.WALL;
                    continue;
                }
                tiles[dx][dy] = Tileset.FLOOR;
            }
        }
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
