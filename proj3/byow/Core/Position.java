package byow.Core;

public class Position {
    int x;
    int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Return true if the Position is outside the Bound. (width, height of the 2D array.)
    public Boolean checkValid(int xBound, int yBound) {
        if (x < 0 || y < 0 || x >= xBound || y >= yBound) {
            return true;
        }
        return false;
    }
}
