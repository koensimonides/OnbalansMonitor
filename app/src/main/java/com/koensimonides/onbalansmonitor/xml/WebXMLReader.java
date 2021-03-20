package com.koensimonides.onbalansmonitor.xml;

import java.io.IOException;
import java.net.URL;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public abstract class WebXMLReader {

    private final XmlPullParserFactory factory;

    private final URL url;

    public WebXMLReader(URL url) {
        this.url = url;

        XmlPullParserFactory factory = null;

        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        this.factory = factory;
    }

    protected boolean isAvailable() {
        return factory != null && url != null;
    }

    protected boolean startProcess() {
        if(!isAvailable())
            return true;

        XmlPullParser parser;

        try {

            parser = factory.newPullParser();
            parser.setInput(url.openStream(), null);

            return !process(parser);

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
            return true;
        }
    }

    protected abstract boolean process(XmlPullParser parser) throws IOException, XmlPullParserException;

}