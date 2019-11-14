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



    public YoutubeVideoIDs() {
    }

    public List getVideoIDs(String searchTerm) {
        YoutubeClient youtubeClient = new YoutubeClient();
        json = youtubeClient.getYoutubeVideos(searchTerm);
        Gson gson = new Gson();
        System.out.println(json);
        YoutubeResponse youtubeResponse = gson.fromJson(json, YoutubeResponse.class);
        List items = youtubeResponse.getItems();

        // loops through ea. item //

        for (Object item : items ) {
            id = youtubeResponse.getItems().get(index).getId().getVideoId();
            ids.add(id);
            index ++;
            System.out.println(item);
            System.out.println(id);
        }

        System.out.println(ids);
        return ids;
    }

}
