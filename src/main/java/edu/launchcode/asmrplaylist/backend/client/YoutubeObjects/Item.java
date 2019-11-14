package edu.launchcode.asmrplaylist.backend.client.YoutubeObjects;

public class Item {
    private String kind;
    private String etag;
    private Id id;

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

