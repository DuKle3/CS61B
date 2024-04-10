package byow.Core;

import java.util.ArrayList;
import java.util.List;

public class Room {
    Position topRight;
    Position bottomLeft;
    int width;
    int height;
    List<Door> doors;
    public Room(Position bottomLeft, Position topRight) {
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.width = topRight.x - bottomLeft.x + 1;
        this.height = topRight.y - bottomLeft.y + 1;
        this.doors = new ArrayList<>();
    }


    // Return True if Room v and Room u are overlap.
    public static Boolean overLap (Room v, Room u) {
        return true;
    }
}