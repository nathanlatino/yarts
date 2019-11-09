package com.framework.enums;

public enum FilterType {

    SENSOR((short) 0x0001),
    ENTITY((short) 0x0002),
    SOLID((short) 0x0004), // powers of 2
    LIGHTS((short) 0x0008);

    private short bits;

    FilterType(short bits) {
        this.bits = bits;
    }

    public short getBits() {
        return bits;
    }

    public static short getSolidMask() {
        return FilterType.ENTITY.getBits();
    }

    public static short getLightsMask() {
        return (short)(FilterType.SOLID.getBits() | FilterType.ENTITY.getBits());
    }
}
