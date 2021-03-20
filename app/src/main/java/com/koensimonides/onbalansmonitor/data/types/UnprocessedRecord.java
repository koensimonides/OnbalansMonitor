package com.koensimonides.onbalansmonitor.data.types;

import android.annotation.SuppressLint;

public final class UnprocessedRecord {

    private short number, sequenceNumber;

    private String formattedTime;

    private int upwardDispatch, downwardDispatch, reserveUpwardDispatch, reserveDownwardDispatch;

    private boolean emergencyPower;

    private Float maxPrice, minPrice;

    public UnprocessedRecord() {
        formattedTime = "";
        maxPrice = minPrice = null;
    }

    public void apply(String attribute, String value) {
        try {

            if(attribute.equalsIgnoreCase("NUMBER"))
                number = Short.parseShort(value);
            else if(attribute.equalsIgnoreCase("SEQUENCE_NUMBER"))
                sequenceNumber = Short.parseShort(value);
            else if(attribute.equalsIgnoreCase("TIME"))
                formattedTime = value;
            else if(attribute.equalsIgnoreCase("UPWARD_DISPATCH"))
                upwardDispatch = Integer.parseInt(value);
            else if(attribute.equalsIgnoreCase("DOWNWARD_DISPATCH"))
                downwardDispatch = Integer.parseInt(value);
            else if(attribute.equalsIgnoreCase("RESERVE_UPWARD_DISPATCH"))
                reserveUpwardDispatch = Integer.parseInt(value);
            else if(attribute.equalsIgnoreCase("RESERVE_DOWNWARD_DISPATCH"))
                reserveDownwardDispatch = Integer.parseInt(value);
            else if(attribute.equalsIgnoreCase("EMERGENCY_POWER"))
                emergencyPower = value.contentEquals("1");
            else if(attribute.equalsIgnoreCase("MAX_PRICE"))
                maxPrice = Float.valueOf(value);
            else if(attribute.equalsIgnoreCase("MIN_PRICE"))
                minPrice = Float.valueOf(value);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public short getNumber() {
        return number;
    }

    public short getSequenceNumber() {
        return sequenceNumber;
    }

    public String getFormattedTime() {
        return formattedTime;
    }

    public int getUpwardDispatch() {
        return upwardDispatch;
    }

    public int getDownwardDispatch() {
        return downwardDispatch;
    }

    public int getReserveUpwardDispatch() {
        return reserveUpwardDispatch;
    }

    public int getReserveDownwardDispatch() {
        return reserveDownwardDispatch;
    }

    public boolean isEmergencyPower() {
        return emergencyPower;
    }

    public Float getMaxPrice() {
        return maxPrice;
    }

    public Float getMinPrice() {
        return minPrice;
    }

    public Float getPrice() {
        if(maxPrice != null && minPrice != null)
            return Math.round((maxPrice + minPrice) * 50f) / 100.f;
        else if(maxPrice != null)
            return maxPrice;
        else
            return minPrice;
    }

    public boolean hasPrice() {
        return maxPrice != null || minPrice != null;
    }

}