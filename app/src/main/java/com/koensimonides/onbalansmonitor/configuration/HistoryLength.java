package com.koensimonides.onbalansmonitor.configuration;

public enum HistoryLength {

    HOUR_1(	"1 uur",	60),
    HOUR_4(	"4 uren",	60 * 4),
    HOUR_8(	"8 uren",	60 * 8),
    DAY_1(	"1 dag",	60 * 24),
    DAY_2(	"2 dagen",	60 * 24 * 2),
    DAY_5(	"5 dagen",	60 * 24 * 5);

    private static final String[] names;

    private final String name;

    private final int size;

    static {
        names = new String[values().length];

        for(int i = 0; i < names.length; i++)
            names[i] = values()[i].getName();
    }

    HistoryLength(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public static HistoryLength getHistoryLength(String name) {
        for(HistoryLength rf : values())
            if(rf.name.equalsIgnoreCase(name))
                return rf;
        return null;
    }

    public static String[] getNames() {
        return names;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

}