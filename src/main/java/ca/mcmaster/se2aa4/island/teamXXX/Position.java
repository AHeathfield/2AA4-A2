package ca.mcmaster.se2aa4.island.teamXXX;
import ca.mcmaster.se2aa4.island.teamXXX.Enums.Direction;

// This is holds x and y coordinates, NOTE down is +y
public class Position {
    public int x;
    public int y;


    // [0,0] Constructor
    public Position() {
        setPosition(0, 0);
    }
    // Custom constructor
    public Position(int x, int y) {
        setPosition(x, y);
    }

    // For setting new position (1 line instead of 2)
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // This gets the forward position based on the drone direction
    public Position getForwardPosition(Direction currentDir) {
        Position pos = new Position();
        switch (currentDir) {
            case Direction.EAST:
                pos = new Position(this.x + 1, this.y);
                break;
            case Direction.WEST:
                pos = new Position(this.x - 1, this.y);
                break;
            case Direction.NORTH:
                pos = new Position(this.x, this.y - 1);
                break;
            case Direction.SOUTH:
                pos = new Position(this.x, this.y + 1);
                break;
        }

        return pos;
    }

    // This is the position Behind the drone
    public Position getBackwardPosition(Direction currentDir) {
        Position pos = new Position();
        switch (currentDir) {
            case Direction.EAST:
                pos = new Position(this.x - 1, this.y);
                break;
            case Direction.WEST:
                pos = new Position(this.x + 1, this.y);
                break;
            case Direction.NORTH:
                pos = new Position(this.x, this.y + 1);
                break;
            case Direction.SOUTH:
                pos = new Position(this.x, this.y - 1);
                break;
        }

        return pos;
    }

    // This is the position if you were HEADING right
    public Position getRightPosition(Direction currentDir) {
        Position pos = new Position();
        switch (currentDir) {
            case Direction.EAST:
                pos = new Position(this.x + 1, this.y + 1);
                break;
            case Direction.WEST:
                pos = new Position(this.x - 1, this.y - 1);
                break;
            case Direction.NORTH:
                pos = new Position(this.x + 1, this.y - 1);
                break;
            case Direction.SOUTH:
                pos = new Position(this.x - 1, this.y + 1);
                break;
        }

        return pos;
    }
    
    // This is the position if you were HEADING Left
    public Position getLeftPosition(Direction currentDir) {
        Position pos = new Position();
        switch (currentDir) {
            case Direction.EAST:
                pos = new Position(this.x + 1, this.y - 1);
                break;
            case Direction.WEST:
                pos = new Position(this.x - 1, this.y + 1);
                break;
            case Direction.NORTH:
                pos = new Position(this.x - 1, this.y - 1);
                break;
            case Direction.SOUTH:
                pos = new Position(this.x + 1, this.y + 1);
                break;
        }

        return pos;
    }

    // This is the position if you were right to move directly right
    public Position getHardRightPosition(Direction currentDir) {
        Position pos = new Position();
        switch (currentDir) {
            case Direction.EAST:
                pos = new Position(this.x, this.y + 1);
                break;
            case Direction.WEST:
                pos = new Position(this.x, this.y - 1);
                break;
            case Direction.NORTH:
                pos = new Position(this.x + 1, this.y);
                break;
            case Direction.SOUTH:
                pos = new Position(this.x - 1, this.y);
                break;
        }

        return pos;
    }
    
    // This is the position if you were to move directly left
    public Position getHardLeftPosition(Direction currentDir) {
        Position pos = new Position();
        switch (currentDir) {
            case Direction.EAST:
                pos = new Position(this.x, this.y - 1);
                break;
            case Direction.WEST:
                pos = new Position(this.x, this.y + 1);
                break;
            case Direction.NORTH:
                pos = new Position(this.x - 1, this.y);
                break;
            case Direction.SOUTH:
                pos = new Position(this.x + 1, this.y);
                break;
        }

        return pos;
    }

    // For comparing positions
    @Override
    public boolean equals(Object obj) {
        // If references are the same
        if (this == obj) return true;

        // Checks to see if their the same class if not I just want to throw an error
        if (obj == null) { 
            throw new NullPointerException("The object being compared cannot be NULL");
        }
        else if (getClass() == obj.getClass()) {
            Position pos = (Position) obj;
            return this.x == pos.x && this.y == pos.y;
        }
        else {
            return false;
        }
    }


    // This creates hashcodes based on x, y values to be used in hashmaps
    @Override
    public int hashCode() {
        return 31 * this.x + this.y;  // 31 is a prime number which is best practice to reduce hash collisions
    }

    // For creating deep copies
    public Position deepCopy() {
        return new Position(this.x, this.y);
    }
}
