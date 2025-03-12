package ca.mcmaster.se2aa4.island.teamXXX;

public enum Direction {
    NORTH(null, null, "N"),
    EAST(null, null, "E"), 
    SOUTH(null, null, "S"), 
    WEST(null, null, "W");

    private Direction rightDirection;
    private Direction leftDirection;
    private String abbreviation;

    Direction (Direction rightDirection, Direction leftDirection, String abbreviation) {
        this.abbreviation = abbreviation;
    }

    static {
        NORTH.rightDirection = EAST;
        NORTH.leftDirection = WEST;

        EAST.rightDirection = SOUTH;
        EAST.leftDirection = NORTH;

        SOUTH.rightDirection = WEST;
        SOUTH.leftDirection = EAST;

        WEST.rightDirection = NORTH;
        WEST.leftDirection = SOUTH;
    }

    public Direction getRightDirection() {
        return rightDirection;
    }
    public Direction getLeftDirection() {
        return leftDirection;
    }
    @Override
    public String toString() {
        return this.abbreviation;
    }

}