package ca.mcmaster.se2aa4.island.teamXXX;
import ca.mcmaster.se2aa4.island.teamXXX.Enums.Direction;
import ca.mcmaster.se2aa4.island.teamXXX.Enums.Turn;

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

    public Position getPosition(Direction currentDir, Turn turn) {
        int[] moves = turn.getMove(currentDir);
        Position pos = new Position(this.x + moves[0], this.y + moves[1]);
        return pos;

    }

    // removed hardleft and hardright functions
    // YAGNI

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
