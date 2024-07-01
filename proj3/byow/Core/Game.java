package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.List;

import static byow.Core.Engine.HEIGHT;
import static byow.Core.Engine.WIDTH;

public class Game {
    private int gameState;
    private Player player;
    private Player enemy;
    private long seed;

    // The display / interaction world with AVATAR
    private TETile[][] world;

    // The same world without AVATAR
    private TETile[][] copyWorld;
    private List<Room> rooms;

    // start new
    public Game(long seed, TETile avatar) {
        this.seed = seed;
        initGame(seed, avatar);
    }

    /**
     * Create World. and Set variables.
     */
    public void initGame(long seed, TETile avatar) {
        WorldGenerator g = new WorldGenerator(WIDTH, HEIGHT, seed);
        // Generate World
        world = g.generateWorld();

        // init parameter
        rooms = g.rooms;
        player = g.player;
        enemy = g.enemy;
        player.tile = avatar;

        copyWorld = TETile.copyOf(world);
        copyWorld[g.player.x][g.player.y] = Tileset.FLOOR;

        world[player.x][player.y] = player.tile;
        world[enemy.x][enemy.y] = enemy.tile;

        DepthFirstPaths dfs = new DepthFirstPaths(world, player, enemy);
        dfs.replacePath(world, copyWorld, player);
    }

    /**
     * Perform Interaction.
     */
    public void interaction(Character input) {
        switch (input) {
            case 'W' -> characterMoveTo(player.x, player.y + 1, player);
            case 'A' -> characterMoveTo(player.x - 1, player.y, player);
            case 'S' -> characterMoveTo(player.x, player.y - 1, player);
            case 'D' -> characterMoveTo(player.x + 1, player.y, player);
            case 'T' -> switchLight();
        }
        // Enemy Move
    }

    public void characterMoveTo(int x, int y, Player character) {
        if (isWalkable(world, x, y)) {
            world[x][y] = character.tile;
            world[character.x][character.y] = copyWorld[character.x][character.y];
            character.x = x;
            character.y = y;
        }
    }

    public static boolean isWalkable(TETile[][] world, int x, int y) {
        String des = world[x][y].description();
        return (des.equals("floor") || des.equals("unlocked door") || des.equals("floorLightON") || des.equals("light"));
    }

    /**
     * Turn On / Off the light.
     */
    public void switchLight() {
        for (Room r: rooms) {
            if (r.isRoom) {
                // Turn On
                if (copyWorld[r.light.x][r.light.y].equals(Tileset.LIGHTOFF)) {
                    turnONLight(r);
                }
                // Turn Off
                else {
                    turnOFFLight(r);
                }
            }
        }
    }

    private void turnONLight(Room r) {
        // if avatar is right in the light position, display avatar
        if (!world[r.light.x][r.light.y].description().equals("you")) {
            world[r.light.x][r.light.y] = Tileset.LIGHTON;
        }
        copyWorld[r.light.x][r.light.y] = Tileset.LIGHTON;
        switchLightHelper(r, 1, Tileset.FLOOR1, "floor");
        switchLightHelper(r, 2, Tileset.FLOOR2, "floor");
        switchLightHelper(r, 3, Tileset.FLOOR3, "floor");
        switchLightHelper(r, 4, Tileset.FLOOR4, "floor");
        switchLightHelper(r, 5, Tileset.FLOOR5, "floor");
        switchLightHelper(r, 6, Tileset.FLOOR6, "floor");
        switchLightHelper(r, 7, Tileset.FLOOR7, "floor");
        switchLightHelper(r, 8, Tileset.FLOOR8, "floor");
    }

    private void turnOFFLight(Room r) {
        // if avatar is right in the light position, display avatar
        if (!world[r.light.x][r.light.y].description().equals("you")) {
            world[r.light.x][r.light.y] = Tileset.LIGHTOFF;
        }
        copyWorld[r.light.x][r.light.y] = Tileset.LIGHTOFF;
        for (int i = 1; i < 9; i++) {
            switchLightHelper(r, i, Tileset.FLOOR, "floorLightON");
        }
    }

    /**
     * change the tile around r.light if the description is equal replace
     */
    private void switchLightHelper(Room r, int n, TETile tile, String replace) {
        for (int dx = -n; dx <= n; dx++) {
            for (int dy = -n; dy <= n; dy++) {
                Position source = r.light;
                Position p = new Position(source.x + dx, source.y + dy);
                if (p.checkValid(WIDTH, HEIGHT) && copyWorld[p.x][p.y].description().equals(replace) && p.isInRoom(r)) {
                    if (p.x == source.x + n || p.x == source.x - n || p.y == source.y + n || p.y == source.y - n) {
                        if (!world[p.x][p.y].description().equals("you")) {
                            world[p.x][p.y] = tile;
                        }
                        copyWorld[p.x][p.y] = tile;
                    }
                }
            }
        }
    }

//    /**
//     * Saves game to .txt file at root dir.
//     * Game state (line by line):
//     * 1. seed
//     * 2. player's x-axis
//     * 3. player's y-axis
//     * 4. .....
//     */
//    public void saveGame() {
//        try (PrintWriter writer = new PrintWriter(new FileWriter("lastGame.txt"))) {
//            writer.println(this.seed);
//            writer.println(this.player.x);
//            writer.println(this.player.y);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public TETile[][] getWorld() {
        return this.world;
    }

    public Player getPlayer() {
        return this.player;
    }
}
