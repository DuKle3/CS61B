package byow.Core;

public class Door {
    Position opening;
    String direction;
    Boolean opened;
    public Door(Position opening, String direction) {
        this.opening = opening;
        this.direction = direction;
        this.opened = false;
    }
}
