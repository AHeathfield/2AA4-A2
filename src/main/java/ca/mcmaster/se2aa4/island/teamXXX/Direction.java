package ca.mcmaster.se2aa4.island.teamXXX;

public enum Direction {
    NORTH(null, null),
    EAST(null, null), 
    SOUTH(null, null), 
    WEST(null, null);

    private Direction rightDirection;
    private Direction leftDirection;

    Direction (Direction rightDirection, Direction leftDirection) {

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

}