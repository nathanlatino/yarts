package com.framework.enums;

public enum Orientations {

    SOUTH(0, 2, 225, 315),
    WEST(1, 1, 135, 225),
    EAST(2, 3, 225, 45),
    NORTH(3, 0, 45, 135);

    private final int index;        // arbitrary index
    private final int row;  // order on the sheet
    private final float start;   // start angle
    private final float end;     // end angle


    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    Orientations(int index, int row, float start, float end) {
        this.start = start;
        this.row = row;
        this.end = end;
        this.index = index;
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    /**
     * @param angle in degrees
     * @return
     */
    public boolean contains(float angle) {
        return angle >= this.start && angle < this.end;
    }

    /*------------------------------*\
    |*			  Getters
    \*------------------------------*/

    public int getIndex() {
        return index;
    }

    public int getRow() {
        return row;
    }
}
