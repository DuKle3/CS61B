package byow.Core;

import java.util.ArrayList;
import java.util.List;

public class Room {
    Position topRight;
    Position bottomLeft;
    int width;
    int height;
    List<Door> doors;
    public Room (Position bottomLeft, Position topRight) {
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.width = topRight.x - bottomLeft.x + 1;
        this.height = topRight.y - bottomLeft.y + 1;
        this.doors = new ArrayList<>();
    }

    // Return True if Room v and Room u are overlap.
    public Boolean overLap (Room v) {
        // check on the Right
        if (this.bottomLeft.isOnRightOf(v.topRight)) {
            return false;
        }
        // check on the Top
        if (this.bottomLeft.isOnTopOf(v.topRight)) {
            return false;
        }
        // check on the Left
        if (this.topRight.isOnLeftOf(v.bottomLeft)) {
            return false;
        }
        // check on the Down
        if (this.topRight.isOnDownOf(v.bottomLeft)) {
            return false;
        }
        return true;
    }
}
