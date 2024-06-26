package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.Queue;
import static byow.Core.Engine.WIDTH;
import static byow.Core.Engine.HEIGHT;

import java.util.*;

/**
 * A class that can generate random world.
 */
public class WorldGenerator {

    // World's width
    int width;
    // World's height
    int height;
    // Random
    Random random;
    // A list collect the rooms in the world
    List<Room> rooms;
    // The player in the starting Room
    Player player;
    private static final int ROOMSBOUND = 60;
    private static final String NORTH = "North";
    private static final String WEST = "West";
    private static final String SOUTH = "South";
    private static final String EAST = "East";

    /**
     * Add the Room r into TETile[][] tiles (world).
     */
    public static void addRoom(TETile[][] tiles, Room r) {
        // addWall
        int x1 = r.bottomLeft.x;
        int x2 = r.topRight.x;
        int y1 = r.bottomLeft.y;
        int y2 = r.topRight.y;

        for (int dx = x1; dx <= x2; dx++) {
            for (int dy = y1; dy <= y2; dy++) {
                if (dx == x1 || dx == x2 || dy == y1 || dy == y2) {
                    tiles[dx][dy] = Tileset.WALL;
                    continue;
                }
                tiles[dx][dy] = Tileset.FLOOR;
            }
            for (Door d : r.doors) {
                if (d.opened) {
                    tiles[d.opening.x][d.opening.y] = Tileset.FLOOR;
                }
            }
        }
    }

    /**
     * Open RANDOM number doors for Given Room r.
     * 
     * @param r
     */
    public void openRandomNumberDoor(Room r, int... atLeast) {
        // number of doors (1 ~ 4)
        int n = RandomUtils.uniform(random, 1, 5);
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
            int index = random.nextInt(directions.size());
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
    public void openRandomDoorWithDirection(Room r, String direction) {
        int dx, dy;
        Position p;
        switch (direction) {
            case NORTH:
                // -2 : minus the corner
                dx = random.nextInt(r.width - 2);
                // add the door int the wall
                p = new Position(r.bottomLeft.x + dx + 1, r.topRight.y);
                r.doors.add(new Door(p, direction));
                break;
            case EAST:
                dy = random.nextInt(r.height - 2);
                p = new Position(r.topRight.x, r.bottomLeft.y + dy + 1);
                r.doors.add(new Door(p, direction));
                break;
            case SOUTH:
                dx = random.nextInt(r.width - 2);
                p = new Position(r.bottomLeft.x + dx + 1, r.bottomLeft.y);
                r.doors.add(new Door(p, direction));
                break;
            case WEST:
                dy = random.nextInt(r.height - 2);
                p = new Position(r.bottomLeft.x, r.bottomLeft.y + dy + 1);
                r.doors.add(new Door(p, direction));
                break;
        }
    }

    /**
     * Use the startingRoom to expand the world.
     * (Only Room for now)
     */
    public void generateRooms(TETile[][] tiles, Room startingRoom) {
        Queue<Room> fringe = new Queue<>();
        // Initialize
        fringe.enqueue(startingRoom);

        // BFS order generate random rooms.
        while (!fringe.isEmpty() && rooms.size() < ROOMSBOUND) {
            Room thisRoom = fringe.dequeue();
            for (Door d : thisRoom.doors) {
                // if the door is opened, skip.
                if (!d.opened && doorHaveSpace(tiles, d)) {
                    Room newRoom;
                    int generateTimes = 0;

                    do {
                        // decide Room or Hallway, 0.4 for room.
                        boolean isRoom = RandomUtils.bernoulli(random, 0.4);
                        newRoom = randomRoomWithDoor(d, isRoom);
                        generateTimes += 1;
                        // if overlap with others rooms, regenerate.
                    } while ((newRoom == null || ifOverlap(newRoom)) && generateTimes < 5);

                    // if the Room is outside the world, we skip the door.
                    if (newRoom == null || generateTimes >= 5) {
                        continue;
                    }

                    // if the Room is valid, add it to the list, queue, world.
                    d.opened = true;
                    fringe.enqueue(newRoom);
                    // TODO: distinguish the hallway and room.
                    rooms.add(newRoom);
                    openRandomNumberDoor(newRoom);
                    // addRoom(tiles, newRoom);
                    // System.out.println(TETile.toString(tiles));
                }
            }
        }
    }

    /**
     * Return a Random size of Room which connected with given Door d.
     * This Room has origin door versus d.
     * and Return null if the room is outside the world.
     */
    private Room randomRoomWithDoor(Door d, Boolean isRoom) {
        int roomWidth, roomLength;
        if (isRoom) {
            // Room's width, length (4 ~ 9)
            roomWidth = RandomUtils.uniform(random, 4, 10);
            roomLength = RandomUtils.uniform(random, 4, 10);
        }
        else {
            // Hallway's width (3, 4), length (5 ~ 10);
            roomWidth = RandomUtils.uniform(random, 3, 5);
            roomLength = RandomUtils.uniform(random, 5, 11);
        }
        int topRightX = 0, topRightY = 0, bottomLeftX = 0, bottomLeftY = 0, dx, dy;
        Position newOpening = null;
        String newDirection = null;

        // Randomly choose the door position depend on the direction.
        switch (d.direction) {
            case NORTH:
                newOpening = new Position(d.opening.x, d.opening.y + 1);
                topRightY = newOpening.y + roomLength - 1;
                bottomLeftY = newOpening.y;
                dx = random.nextInt(roomWidth - 2) + 1;
                bottomLeftX = newOpening.x - dx;
                topRightX = bottomLeftX + roomWidth - 1;
                newDirection = SOUTH;
                break;
            case EAST:
                newOpening = new Position(d.opening.x + 1, d.opening.y);
                bottomLeftX = newOpening.x;
                topRightX = newOpening.x + roomLength - 1;
                dy = random.nextInt(roomWidth - 2) + 1;
                bottomLeftY = newOpening.y - dy;
                topRightY = bottomLeftY + roomWidth - 1;
                newDirection = WEST;
                break;
            case SOUTH:
                newOpening = new Position(d.opening.x, d.opening.y - 1);
                topRightY = newOpening.y;
                bottomLeftY = topRightY - roomLength + 1;
                dx = random.nextInt(roomWidth - 2) + 1;
                bottomLeftX = newOpening.x - dx;
                topRightX = bottomLeftX + roomWidth - 1;
                newDirection = NORTH;
                break;
            case WEST:
                newOpening = new Position(d.opening.x - 1, d.opening.y);
                bottomLeftX = newOpening.x - roomLength + 1;
                topRightX = newOpening.x;
                dy = random.nextInt(roomWidth - 2) + 1;
                bottomLeftY = newOpening.y - dy;
                topRightY = bottomLeftY + roomWidth - 1;
                newDirection = EAST;
                break;
        }
        Position topRight = new Position(topRightX, topRightY);
        Position bottomLeft = new Position(bottomLeftX, bottomLeftY);

        // If the Position is outside the world.
        if (topRight.checkValid(WIDTH, HEIGHT)
                || bottomLeft.checkValid(WIDTH, HEIGHT)) {
            return null;
        }

        Room newRoom = new Room(bottomLeft, topRight);
        Door newDoor = new Door(newOpening, newDirection);

        // open the door to the newRoom.
        newDoor.opened = true;
        newRoom.doors.add(newDoor);
        return newRoom;
    }

    // Return true if the door d have space to span new room. (at least is not face
    // something else)
    private Boolean doorHaveSpace(TETile[][] tiles, Door d) {
        int x = 0;
        int y = 0;
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
        // outside the world
        if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) {
            return false;
        }

        // have at least one space.
        if (tiles[x][y] == Tileset.NOTHING) {
            return true;
        }

        return false;
    }

    /**
     * Return true if Room r is overlapping with others Room.
     */
    public Boolean ifOverlap(Room r) {
        for (Room v: rooms) {
            if (r.overLap(v)) {
                return true;
            }
        }
        return false;
    }

    private void fillBoardWithNothing(TETile[][] tiles) {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    public Room randomStartingRoom() {
        int x1 = RandomUtils.uniform(random, WIDTH / 2 - WIDTH / 10, WIDTH / 2);
        int y1 = RandomUtils.uniform(random, HEIGHT / 2 - HEIGHT / 10, HEIGHT / 2);
        int width = RandomUtils.uniform(random, 4, 9);
        int height = RandomUtils.uniform(random, 4, 9);
        Position bottomLeft = new Position(x1, y1);
        Position topRight = new Position(x1 + width, y1 + height);
        return new Room (bottomLeft, topRight);
    }


    public Player generatePlayer(Room startingRoom) {
        int x = (startingRoom.topRight.x + startingRoom.bottomLeft.x) / 2;
        int y = (startingRoom.topRight.y + startingRoom.bottomLeft.y) / 2;
        return new Player(x, y, Tileset.AVATAR);
    }


    public WorldGenerator(int width, int height, long seed) {
        this.random = new Random(seed);
        this.rooms = new ArrayList<>();
    }

    /**
     * Return pseudo Random World, and set the avatar in the starting room.
     */
    public TETile[][] generateWorld() {
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        // initialize
        fillBoardWithNothing(world);
        Room startingRoom = randomStartingRoom();
        rooms.add(startingRoom);
        player = generatePlayer(startingRoom);
        openRandomNumberDoor(startingRoom, 4);
        generateRooms(world, startingRoom);
        for (Room r : rooms) {
            addRoom(world, r);
        }
        world[player.x][player.y] = player.tile;
        return world;
    }

}
