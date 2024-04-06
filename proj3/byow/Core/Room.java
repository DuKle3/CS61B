package byow.Core;

import java.util.List;

public class Room {
    Position topLeft;
    Position bottomRight;
    List<Position> doors;
    public Room(Position topLeft, Position bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public void addDoors(Position door) {

    }

    // Return True if Room v and Room u are overlap.
    public static Boolean overLap (Room v, Room u) {
        return true;
    }


}