package byow.Core;

import java.util.List;

public class Room {
    Position leftUp;
    Position rightBottom;
    List<Position> doors;
    public Room(Position leftUp, Position rightBottom) {
        this.leftUp = leftUp;
        this.rightBottom = rightBottom;
    }

    // Return True if Room v and Room u are overlap.
    public Boolean overLap (Room v, Room u) {
        return true;
    }
}