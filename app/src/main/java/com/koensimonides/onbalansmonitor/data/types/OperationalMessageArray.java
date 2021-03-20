package com.koensimonides.onbalansmonitor.data.types;

import java.util.ArrayList;
import java.util.List;

public class OperationalMessageArray {

    private final List<OperationalMessage> messages;

    {
        messages = new ArrayList<>();
    }

    public synchronized List<OperationalMessage> add(final List<OperationalMessage> rawMessages) {

        final List<OperationalMessage> newMessages = new ArrayList<>();

        if(rawMessages == null || rawMessages.size() == 0)
            return newMessages;

        for(OperationalMessage rm : rawMessages)
            if(canAdd(rm))
                newMessages.add(rm);

        for(int i = 0; i < newMessages.size(); i++)
            messages.add(i, newMessages.get(i));

        return newMessages;
    }

    private boolean canAdd(OperationalMessage message) {
        if(message == null)
            return false;

        for(OperationalMessage m : messages)
            if(m.getPublicationDate().equals(message.getPublicationDate()))
                return false;

         return true;
    }

    public OperationalMessage[] asArray() {
        return messages.toArray(new OperationalMessage[0]);
    }

}
