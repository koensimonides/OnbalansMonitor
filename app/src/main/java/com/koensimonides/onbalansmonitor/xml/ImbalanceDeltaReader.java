package com.koensimonides.onbalansmonitor.xml;

import com.koensimonides.onbalansmonitor.data.types.UnprocessedRecord;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ImbalanceDeltaReader extends WebXMLReader {

    final ArrayList<UnprocessedRecord> records;

    private ImbalanceDeltaReader(URL url) {
        super(url);

        records = new ArrayList<>();
    }

    public static ImbalanceDeltaReader of(String url) {
        URL realUrl;

        try {
            realUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        ImbalanceDeltaReader reader = new ImbalanceDeltaReader(realUrl);

        return reader.isAvailable() ? reader : null;
    }

    public List<UnprocessedRecord> load() {
        if (startProcess())
            return null;

        return records;
    }

    @Override
    protected boolean process(XmlPullParser parser) throws IOException, XmlPullParserException {
        records.clear();

        UnprocessedRecord currentRecord = null;

        String currentTag = null;

        int eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {

            if(eventType == XmlPullParser.START_TAG) {

                if(parser.getName().contentEquals("RECORD"))
                    currentRecord = new UnprocessedRecord();
                else
                    currentTag = parser.getName();

            } else if(eventType == XmlPullParser.END_TAG) {

                if(parser.getName().contentEquals("RECORD"))
                    records.add(currentRecord);
                else
                    currentTag = null;

            } else if(eventType == XmlPullParser.TEXT) {

                if(currentRecord != null && currentTag != null)
                    currentRecord.apply(currentTag, parser.getText());

            }

            eventType = parser.next();
        }

        return true;
    }

}
