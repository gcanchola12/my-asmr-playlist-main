package edu.launchcode.asmrplaylist.backend.client.YoutubeObjects;

public class Id {
    private String kind;
    private String videoId; // the string that is pulled in YoutubeVideoIDs


    public String getVideoId() {
        return videoId;
    }

    @Override
    public String toString() {
        return getVideoId();
    }

}
