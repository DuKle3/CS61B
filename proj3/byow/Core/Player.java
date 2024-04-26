package byow.Core;

import byow.TileEngine.TETile;

public class Player {
    int x;
    int y;
    TETile tile;

    public Player(int x, int y, TETile tile) {
        this.x = x;
        this.y = y;
        this.tile = tile;
    }
}
