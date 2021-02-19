package Sudoku.problemdomain;

import java.util.Objects;

/**
 * Convenience class for storing the location of a given tile in the Sudoku puzzle in a Hashmap.
 */

public class Coordinates {
    private final int x;
    private final int y;

    public Coordinates(int x, int y){
        this.x = x;
        this.y = y;
    }

    // Getter for x
    public int getX() {
        return x;
    }

    // Getter for y
    public int getY() {
        return y;
    }

    @Override
    // Gonna place coordinates in a hashmap later
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;

        return x == that.x && y == that.y;
    }

    @Override
    // Create unique ID for x and y
    public int hashCode(){
        // generate unique ID from x and y value of these specific coordinates
        return Objects.hash(x,y);
    }
}
