package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;

import static byow.Core.Engine.HEIGHT;
import static byow.Core.Engine.WIDTH;

public class DepthFirstPaths {
    private boolean[][] marked;
    private Position[][] edgeTo;

    public DepthFirstPaths(TETile[][] world, Player player, Player enemy) {
        marked = new boolean[WIDTH][HEIGHT];
        edgeTo = new Position[WIDTH][HEIGHT];
        Position playerP = new Position(player.x, player.y);
        Position enemyP = new Position(enemy.x, enemy.y);

        dfs(world, playerP, enemyP);
        System.out.println("dfsDone");
    }

    private void dfs(TETile[][] world, Position destination, Position p) {
        marked[p.x][p.y] = true;
        for (Position ap: tileAroundPosition(world, p)) {
            if (!marked[ap.x][ap.y]) {
                edgeTo[ap.x][ap.y] = new Position(p.x, p.y);
                if (world[ap.x][ap.y].description().equals("you")) {
                    return;
                }
                dfs(world, destination, ap);
            }
        }
    }

    public void replacePath(TETile[][] world, TETile[][] copyWorld, Player player) {
        int x = player.x;
        int y = player.y;
        Position next = edgeTo[x][y];

        while (!world[next.x][next.y].description().equals("enemy")) {
            world[next.x][next.y] = Tileset.PATH;
            copyWorld[next.x][next.y] = Tileset.PATH;
            next = edgeTo[next.x][next.y];
        }
    }

    private void initializeMarked(boolean[][] marked) {
        for (int i = 0; i < marked.length; i++) {
            for (int j = 0; j < marked[0].length; j++) {
                marked[i][j] = false;
            }
        }
    }

    private List<Position> tileAroundPosition(TETile[][] world, Position p) {
        List<Position> l = new ArrayList<>();
        if (Game.isWalkable(world, p.x + 1, p.y) || world[p.x + 1][p.y].description().equals("you")) {
            l.add(new Position(p.x + 1, p.y));
        }
        if (Game.isWalkable(world, p.x - 1, p.y) || world[p.x - 1][p.y].description().equals("you")) {
            l.add(new Position(p.x - 1, p.y));
        }
        if (Game.isWalkable(world, p.x, p.y + 1) || world[p.x][p.y + 1].description().equals("you")) {
            l.add(new Position(p.x, p.y + 1));
        }
        if (Game.isWalkable(world, p.x, p.y - 1) || world[p.x][p.y - 1].description().equals("you")) {
            l.add(new Position(p.x, p.y - 1));
        }

        return l;
    }
}
