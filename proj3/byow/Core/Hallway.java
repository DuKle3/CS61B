package byow.Core;

public class Hallway {
    Position startPoint;
    int width;
    int length;
    String direction;

    public Hallway(Position startPoint, int width, int length, String direction) {
        this.direction = direction;
        this.startPoint = startPoint;
        this.length = length;
        this.width = width;
    }

    /**
     * Return True if this Hallway overlap with Hallway v.
     */
    public static boolean overLap(Hallway v) {
        return true;
    }
}
