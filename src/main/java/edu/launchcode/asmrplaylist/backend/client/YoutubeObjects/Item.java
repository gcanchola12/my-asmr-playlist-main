package edu.launchcode.asmrplaylist.backend.client.YoutubeObjects;

// each item object contains all of the information for a video

public class Item {
    private String kind;
    private String etag;
    private Id id; // object that contains the video Id that will be used in controller

    public Item(String kind, String etag, Id id) {
        this.kind = kind;
        this.etag = etag;
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

}

