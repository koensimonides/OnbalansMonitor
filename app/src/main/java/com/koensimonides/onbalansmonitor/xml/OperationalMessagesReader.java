package com.koensimonides.onbalansmonitor.xml;

import com.koensimonides.onbalansmonitor.data.types.OperationalMessage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class OperationalMessagesReader extends WebXMLReader {

    private final ArrayList<OperationalMessage> messages;

    private OperationalMessagesReader(URL url) {
        super(url);

        messages = new ArrayList<>();
    }

    public static OperationalMessagesReader of(String url) {
        URL realUrl;

        try {
            realUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        OperationalMessagesReader reader = new OperationalMessagesReader(realUrl);

        return reader.isAvailable() ? reader : null;
    }

    public List<OperationalMessage> load() {
        if (startProcess())
            return null;

        return messages;
    }

    @Override
    protected boolean process(XmlPullParser parser) throws IOException, XmlPullParserException {
        messages.clear();

        OperationalMessage currentMessage = null;

        String currentTag = null;

        int eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {

            if(eventType == XmlPullParser.START_TAG) {

                if(parser.getName().contentEquals("item"))
                    currentMessage = new OperationalMessage();
                else
                    currentTag = parser.getName();

            } else if(eventType == XmlPullParser.END_TAG) {

                if(parser.getName().contentEquals("item"))
                    messages.add(currentMessage);
                else
                    currentTag = null;

            } else if(eventType == XmlPullParser.TEXT) {

                if(currentMessage != null && currentTag != null)
                    currentMessage.apply(currentTag, parser.getText());

            }

            eventType = parser.next();
        }

        return true;
    }

    /*
    NOT USED
    public List<OperationalMessage> getOperationalMessages() {
        return messages;
    }
     */

}
