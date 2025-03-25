package ca.mcmaster.se2aa4.island.teamXXX.Enums;

public enum Turn {
    // {xMove, yMove} for each
    LEFT(new int[]{1, -1}, new int[]{-1, 1}, new int[]{-1, -1}, new int[]{1, 1}), 
    RIGHT(new int[]{1, 1}, new int[]{-1, -1}, new int[]{1, -1}, new int[]{-1, 1}), 
    BACKWARDS(new int[]{-1, 0}, new int[]{1, 0}, new int[]{0, 1}, new int[]{0, -1}), 
    FORWARDS(new int[]{1, 0}, new int[]{-1, 0}, new int[]{0, -1}, new int[]{0, 1});

    // going up is minus Y

    private int[] eastMove;
    private int[] westMove;
    private int[] northMove;
    private int[] southMove;

    Turn (int[] eastMove, int[] westMove, int[] northMove, int[] southMove) {
        this.eastMove = eastMove;
        this.westMove = westMove;
        this.northMove = northMove;
        this.southMove = southMove;
    }

    public int[] getMove(Direction dir) {
        int[] moves = null;
        switch (dir) {
            case Direction.EAST:
                moves = eastMove;
                break;
            case Direction.WEST:
                moves = westMove;
                break;
            case Direction.NORTH:
                moves = northMove;
                break;
            case Direction.SOUTH:
                moves = southMove;
                break;
            default:
                throw new IllegalArgumentException("Direction DNE: " + dir);
        }
        return moves;
    }

}