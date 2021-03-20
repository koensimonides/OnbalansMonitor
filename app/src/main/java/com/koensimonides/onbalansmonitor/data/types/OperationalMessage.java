package com.koensimonides.onbalansmonitor.data.types;

public class OperationalMessage {

    private String title, link, publicationDate;

    public OperationalMessage() {
        title = link = publicationDate = "";
    }

    public void apply(String attribute, String value) {
        if(attribute.equalsIgnoreCase("title"))
            title = value;
        else if(attribute.equalsIgnoreCase("link"))
            link = value;
        else if(attribute.equalsIgnoreCase("pubDate"))
            publicationDate = value;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

}
