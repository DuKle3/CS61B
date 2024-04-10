package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.Queue;
import org.apache.commons.math3.random.RandomGenerator;

import java.util.*;

public class WorldGenerator {


    private static final String NORTH = "North";
    private static final String WEST = "West";
    private static final String SOUTH = "South";
    private static final String EAST = "East";
    private static List<Room> rooms = new ArrayList<Room>();
    private static List<Hallway> hallWays;
    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);


    /**
     * Add the Room r into TETile[][] tiles (world).
     */
    public static void addRoom(TETile[][] tiles, Room r) {
        // addWall
        int x1 = r.bottomLeft.x;
        int x2 = r.topRight.x;
        int y1 = r.bottomLeft.y;
        int y2 = r.topRight.y;

        for (int dx = x1; dx <= x2 ; dx++) {
            for (int dy = y1; dy <= y2; dy++) {
                if (dx == x1 || dx == x2 || dy == y1 || dy == y2) {
                    tiles[dx][dy] = Tileset.WALL;
                    continue;
                }
                tiles[dx][dy] = Tileset.FLOOR;
            }
            for (Door d: r.doors) {
                if (d.opened) {
                    tiles[d.opening.x][d.opening.y] = Tileset.FLOOR;
                }
            }
        }
    }

    /**
     * Open RANDOM number doors for Given Room r.
     * @param r
     */
    public static void openRandomNumberDoor(Room r, int... atLeast) {
        // number of doors
        int n = RANDOM.nextInt(4);
        if (atLeast.length > 0) {
            n = atLeast[0];
        }

        // Create the directions list.
        List<String> directions = new ArrayList<>();
        directions.add(NORTH);
        directions.add(EAST);
        directions.add(SOUTH);
        directions.add(WEST);

        // for each door, choose random direction.
        for (int i = 0; i < n; i++) {
            int index = RANDOM.nextInt(directions.size());
            String direction = directions.get(index);
            directions.remove(index);
            // for peculiar direction, add the door.
            openRandomDoorWithDirection(r, direction);
        }
    }

    /**
     * Open the RANDOM door for Given r and Given direction.
     * Add the door to r.doors
     */
    public static void openRandomDoorWithDirection(Room r, String direction) {
        int dx;
        Position p;
        switch (direction) {
            case NORTH:
                // -2 : minus the corner
                dx = RANDOM.nextInt(r.width - 2);
                // add the door int the wall
                p = new Position(r.bottomLeft.x + dx + 1, r.topRight.y);
                r.doors.add(new Door(p, direction));
                break;
            case EAST:
                int dy = RANDOM.nextInt(r.height - 2);
                p = new Position(r.topRight.x, r.bottomLeft.y + dy + 1);
                r.doors.add(new Door(p, direction));
                break;
            case SOUTH:
                dx = RANDOM.nextInt(r.width - 2);
                p = new Position(r.bottomLeft.x + dx + 1, r.bottomLeft.y);
                r.doors.add(new Door(p, direction));
                break;
            case WEST:
                dy = RANDOM.nextInt(r.width - 2);
                p = new Position(r.bottomLeft.x, r.bottomLeft.y + dy + 1);
                r.doors.add(new Door(p, direction));
                break;
        }
    }

    /**
     * Use the startingRoom to expand the world.
     * (Only Room for now)
     */
    public static void generateRooms(TETile[][] tiles, Room startingRoom) {
        Queue<Room> fringe = new Queue<>();
        // Initialize
        fringe.enqueue(startingRoom);

        while (!fringe.isEmpty() && rooms.size() < 4) {
            Room thisRoom = fringe.dequeue();
            for (Door d: thisRoom.doors) {
                // if the door is opened, skip.
                if (!d.opened && doorHaveSpace(tiles, d)) {
                    Room newRoom = randomRoomWithDoor(tiles, d);
                    // if the Room is outside the world, we skip the door.
                    if (newRoom == null) {
                        continue;
                    }
                    // if the Room is valid, add it to the list, queue, world.
                    d.opened = true;
                    fringe.enqueue(newRoom);
                    rooms.add(newRoom);
                    openRandomNumberDoor(newRoom);
                }
            }
        }
    }

    /**
     * Return a Random size of Room which connected with given Door d.
     * This Room has origin door versus d.
     */
    private static Room randomRoomWithDoor(TETile[][] tiles, Door d) {
        int width = RANDOM.nextInt( 6) + 3;
        int height = RANDOM.nextInt( 6) + 3;
        int topRightX = 0, topRightY = 0, bottomLeftX = 0, bottomLeftY = 0, dx, dy;
        Position newOpening = null;
        String newDirection = null;
        switch (d.direction) {
            case NORTH:
                newOpening = new Position(d.opening.x, d.opening.y + 1);
                topRightY = newOpening.y + height - 1;
                bottomLeftY = newOpening.y;
                dx = RANDOM.nextInt(width - 2) + 1;
                bottomLeftX = newOpening.x - dx;
                topRightX = bottomLeftX + width - 1;
                newDirection = SOUTH;
                break;
            case EAST:
                newOpening = new Position(d.opening.x + 1, d.opening.y);
                bottomLeftX = newOpening.x;
                topRightX = newOpening.x + width - 1;
                dy = RANDOM.nextInt( height - 2) + 1;
                bottomLeftY = newOpening.y - dy;
                topRightY = bottomLeftY + height - 1;
                newDirection = WEST;
                break;
            case SOUTH:
                newOpening = new Position(d.opening.x, d.opening.y - 1);
                topRightY = newOpening.y;
                bottomLeftY = topRightY - height + 1;
                dx = RANDOM.nextInt(width - 2) + 1;
                bottomLeftX = newOpening.x - dx;
                topRightX = bottomLeftX + width - 1;
                newDirection = NORTH;
                break;
            case WEST:
                newOpening = new Position(d.opening.x - 1, d.opening.y);
                bottomLeftX = newOpening.x - width + 1;
                topRightX = newOpening.x;
                dy = RANDOM.nextInt( height - 2) + 1;
                bottomLeftY = newOpening.y - dy;
                topRightY = bottomLeftY + height - 1;
                newDirection = EAST;
                break;
        }
        Position topRight = new Position(topRightX, topRightY);
        Position bottomLeft = new Position(bottomLeftX, bottomLeftY);

        // If the Position is outside the world.
        if (topRight.checkValid(tiles.length, tiles[0].length) || bottomLeft.checkValid(tiles.length, tiles[0].length)) {
            return null;
        }

        Room newRoom = new Room(bottomLeft, topRight);
        Door newDoor = new Door(newOpening, newDirection);
        newDoor.opened = true;
        // open the door to the newRoom.
        newRoom.doors.add(newDoor);
        return newRoom;
    }

    // Return true if the door d have space to span new room. (at least is not face something else)
    private static Boolean doorHaveSpace(TETile[][] tiles, Door d) {
        int x = 0;
        int y = 0;
        int width = tiles.length;
        int height = tiles[0].length;
        switch (d.direction) {
            case NORTH -> {
                x = d.opening.x;
                y = d.opening.y + 1;
            }
            case EAST -> {
                x = d.opening.x + 1;
                y = d.opening.y;
            }
            case SOUTH -> {
                x = d.opening.x;
                y = d.opening.y - 1;
            }
            case WEST -> {
                x = d.opening.x - 1;
                y = d.opening.y;
            }
        }
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return false;
        }

        if (tiles[x][y] == Tileset.NOTHING) {
            return true;
        }
        return false;
    }

    private Boolean overlapRoom(Room v) {
        return true;
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
        Position x1 = new Position(15, 15);
        Position x2 = new Position(20, 20);

        Room startingRoom = new Room(x1, x2);
        rooms.add(startingRoom);
        openRandomNumberDoor(startingRoom, 3);
        generateRooms(world, startingRoom);
        for (Room r: rooms) {
            addRoom(world, r);
        }
    }
}
