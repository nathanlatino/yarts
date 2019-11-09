package com.framework.enums;

public enum EntityType {
    RANGED(9),
    SOLDIER(10),
    WORKER(11),

    BUILDING(12),
    RESSOURCE(13),

    EMPTY(14),
    DECORATION(15),

    PROJECTILE(16);



    private final int id;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    EntityType(int id) {
        this.id = id;
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    public int getId() {
        return id;
    }
}
