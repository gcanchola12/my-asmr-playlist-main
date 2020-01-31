package edu.launchcode.asmrplaylist.backend.client;

import com.google.gson.Gson;
import edu.launchcode.asmrplaylist.backend.client.YoutubeObjects.YoutubeResponse;

import java.util.ArrayList;
import java.util.List;



public class YoutubeVideoIDs {

    private String apiString;
    private String json;
    private String id;
    private List<String> ids = new ArrayList<>();
    private int index = 0;

   // Calls Youtube DATA Api and returns a list of video Ids

    public YoutubeVideoIDs() {
    }

    public List getVideoIDs(String searchTerm) {
        YoutubeClient youtubeClient = new YoutubeClient();
        json = youtubeClient.getYoutubeVideos(searchTerm);
        Gson gson = new Gson();
        System.out.println(json);
        YoutubeResponse youtubeResponse = gson.fromJson(json, YoutubeResponse.class); // Using Gson to deserialize Json
        List items = youtubeResponse.getItems(); // grabs each item from response, which contain a video Id

        // loops through ea. item to pull the id and adds it to the ids list //

        for (Object item : items ) {
            id = youtubeResponse.getItems().get(index).getId().getVideoId(); // should I use this as an argument in ids.add(id)? Think about it
            ids.add(id);
            index ++; // can't remember why I needed a counter... ?
        }

        index = 0;
        return ids;
    }

}
