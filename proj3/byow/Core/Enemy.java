package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;

import static byow.Core.Engine.HEIGHT;
import static byow.Core.Engine.WIDTH;

public class Enemy {
    int x;
    int y;
    TETile tile;
    public Enemy(int x, int y, TETile tile) {
        this.x = x;
        this.y = y;
        this.tile = tile;
    }
}
