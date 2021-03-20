package com.koensimonides.onbalansmonitor.data.types;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class RecordArray {

    private CompactRecord[] records;

    private long lastUpdate;

    private int firstValued, firstNonNull, unixMinute;

    public RecordArray(final int size) {
        records = new CompactRecord[size];
        Arrays.fill(records, null);
        firstValued = firstNonNull = unixMinute = -1;
    }

    public synchronized void add(final List<UnprocessedRecord> rawRecords) {
        lastUpdate = System.currentTimeMillis();

        unixMinute = (int) (lastUpdate / CompactRecord.UNIX_FACTOR);

        // shift
        for(int i = records.length - 1; i >= 0; i--) {
            if(records[i] == null)
                continue;

            final int newIndex = i - (records[i].getUnix() - unixMinute);

            if(newIndex < records.length)
                records[newIndex] = records[i];

            records[i] = null;
        }

        // compact and insert new records
        if(rawRecords != null && rawRecords.size() > 0) {
            final CompactRecord[] compacted = compact(rawRecords);

            for(CompactRecord r : compacted)
                records[unixMinute - r.getUnix()] = r;
        }

        // recalculate first indexes
        firstValued = firstNonNull = -1;

        for(int i = 0; firstValued == -1 && i < records.length; i++)
            if(records[i] != null) {
                if(firstNonNull == -1)
                    firstNonNull = i;

                if(records[i].hasPrice())
                    firstValued = i;
            }
    }

    public synchronized void resize(final int size) {
        if(size <= 0) {
            records = new CompactRecord[0];
            return;
        }

        final CompactRecord[] newArray = new CompactRecord[size];

        for(int i = 0; i < newArray.length && i < records.length; i++)
            newArray[i] = records[i];

        if(firstNonNull >= newArray.length)
            firstValued = firstNonNull = -1;
        else if(firstValued >= newArray.length)
            firstValued = -1;

        records = newArray;
    }

    public boolean hasValues() {
        return firstNonNull != -1;
    }

    public CompactRecord getFirstNonNull() {
        return firstNonNull == -1 ? null : records[firstNonNull];
    }

    public CompactRecord getFirstValued() {
        return firstValued == -1 ? null : records[firstValued];
    }

    public CompactRecord[] get() {
        return records;
    }

    /* UNUSED
    public CompactRecord[] get(final int amount) {
        if(amount < 0)
            return new CompactRecord[0];

        if(amount > records.length)
            return records;

        return Arrays.copyOfRange(records, 0, amount, CompactRecord[].class);
    }
     */

    public CompactRecord getFirstValuedSince(final int index) {
        for(int i = index; i < records.length; i++)
            if(records[i] != null && records[i].hasPrice())
                    return records[i];
        return null;
    }

    public CompactRecord[] getValueFilled(int amount) {
        if(amount < 0)
            return new CompactRecord[0];

        if(amount > records.length)
            amount = records.length;

        final CompactRecord[] result = new CompactRecord[amount];

        final CompactRecord filler = getFirstValuedSince(amount - 1);

        float fillPrice = filler == null ? 0f : filler.getPrice();
        int fillUnix = unixMinute - amount;

        for(int i = amount - 1; i >= 0; i--) {
            final CompactRecord current = (records[i] != null && records[i].hasPrice()) ? records[i] :
                    new CompactRecord(fillUnix, fillPrice, false, true);

            result[i] = current;

            fillPrice = current.getPrice();
            fillUnix = (records[i] != null ? records[i].getUnix() : fillUnix) + 1;
        }

        return result;
    }

    public int offsetOf(@NonNull final CompactRecord record, boolean force) {
        return (force ? unixMinute = (int) (System.currentTimeMillis() / CompactRecord.UNIX_FACTOR)
                : unixMinute) - record.getUnix() + 1;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    private CompactRecord[] compact(final List<UnprocessedRecord> rawRecords) {
        final Calendar now = Calendar.getInstance();
        final int dayMinute = (now.get(Calendar.HOUR_OF_DAY) * 60) + now.get(Calendar.MINUTE);

        final CompactRecord[] result = new CompactRecord[rawRecords.size()];

        for(int i = 0; i < result.length; i++) {
            final UnprocessedRecord current = rawRecords.get(i);

            if(current == null) {
                result[i] = null;
                continue;
            }

            final int timeShift = dayMinute - (current.getSequenceNumber() > dayMinute ?
                    (current.getSequenceNumber() - 1440) : current.getSequenceNumber());
            final boolean hasPrice = current.hasPrice();

            result[i] = new CompactRecord(unixMinute - timeShift,
                    hasPrice ? current.getPrice() : 0f, current.isEmergencyPower(), hasPrice);
        }

        return result;
    }

}
