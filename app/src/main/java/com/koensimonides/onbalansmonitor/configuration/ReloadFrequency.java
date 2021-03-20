package com.koensimonides.onbalansmonitor.configuration;

public enum ReloadFrequency {

    SEC_15("15 seconden", 15),
    SEC_30("30 seconden", 30),
    MIN_01("1 minuut", 60),
    MIN_02("2 minuten", 60 * 2),
    MIN_05("5 minuten", 60 * 5),
    MIN_10("10 minuten", 60 * 10),
    MIN_30("30 minuten", 60 * 30);

    private static final String[] names;

    private final String name;

    private final int seconds;

    static {
        names = new String[values().length];

        for(int i = 0; i < names.length; i++)
            names[i] = values()[i].getName();
    }

    ReloadFrequency(String name, int seconds) {
        this.name = name;
        this.seconds = seconds;
    }

    public static ReloadFrequency getReloadFrequency(String name) {
        for(ReloadFrequency rf : values())
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

    public int getSeconds() {
        return seconds;
    }

}
