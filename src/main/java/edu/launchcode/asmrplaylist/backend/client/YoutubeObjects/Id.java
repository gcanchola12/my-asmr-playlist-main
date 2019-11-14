package edu.launchcode.asmrplaylist.backend.client.YoutubeObjects;

public class Id {
    private String kind;
    private String videoId;


    public String getVideoId() {
        return videoId;
    }

    @Override
    public String toString() {
        return getVideoId();
    }

}
