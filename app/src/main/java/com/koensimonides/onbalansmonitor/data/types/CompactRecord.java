package com.koensimonides.onbalansmonitor.data.types;

public final class CompactRecord {

    public static final CompactRecord NULL_RECORD;

    static final int UNIX_FACTOR = 60000;

    private final int unix;

    private final float price;

    private final boolean emergencyPower, hasPrice;

    static {
        NULL_RECORD = new CompactRecord(-1, 0f, false, false);
    }

    CompactRecord(int unix, float price, boolean emergencyPower, boolean hasPrice) {
        this.unix = unix;
        this.price = price;
        this.emergencyPower = emergencyPower;
        this.hasPrice = hasPrice;
    }

    public int getUnix() {
        return unix;
    }

    public long getFullUnix() {
        return (long) unix * UNIX_FACTOR;
    }

    public float getPrice() {
        return price;
    }

    public boolean isEmergencyPower() {
        return emergencyPower;
    }

    public boolean hasPrice() {
        return hasPrice;
    }

}
