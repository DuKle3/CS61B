package byow.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile AVATAR1 = new TETile('@', Color.white, Color.black, "you");
    public static final TETile AVATAR2 = new TETile('@', Color.MAGENTA, Color.black, "you");
    public static final TETile AVATAR3 = new TETile('@', Color.PINK, Color.black, "you");
    public static final TETile ENEMY = new TETile('$', Color.RED, Color.black, "enemy");
    public static final TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
            "wall");
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black, "floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");
    public static final TETile LIGHTOFF = new TETile('⚫', new Color(183, 202, 245), Color.black, "light");
    public static final TETile LIGHTON = new TETile('⚫', new Color(183, 202, 245), new Color(58, 81, 243), "light");
    public static final TETile FLOOR1 = new TETile('·', new Color(128, 192, 128), new Color(44, 62, 194), "floorLightON");
    public static final TETile FLOOR2 = new TETile('·', new Color(128, 192, 128), new Color(33, 46, 145), "floorLightON");
    public static final TETile FLOOR3 = new TETile('·', new Color(128, 192, 128), new Color(21, 29, 112), "floorLightON");
    public static final TETile FLOOR4 = new TETile('·', new Color(128, 192, 128), new Color(10, 18, 86), "floorLightON");
    public static final TETile FLOOR5 = new TETile('·', new Color(128, 192, 128), new Color(4, 9, 68), "floorLightON");
    public static final TETile FLOOR6 = new TETile('·', new Color(128, 192, 128), new Color(3, 8, 59), "floorLightON");
    public static final TETile FLOOR7 = new TETile('·', new Color(128, 192, 128), new Color(2, 4, 50), "floorLightON");
    public static final TETile FLOOR8 = new TETile('·', new Color(128, 192, 128), new Color(3, 3, 43), "floorLightON");
    public static final TETile MARKED = new TETile('%', Color.PINK, Color.BLACK, "marked");
    public static final TETile PATH = new TETile('·', Color.RED, Color.BLACK, "marked");

}


