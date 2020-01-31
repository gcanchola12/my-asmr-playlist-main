package edu.launchcode.asmrplaylist.backend.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static edu.launchcode.asmrplaylist.backend.client.YoutubeConstants.API_KEY;
import static edu.launchcode.asmrplaylist.backend.client.YoutubeConstants.YOUTUBE_URL;

// creates connection to Youtube Data API which returns youtube videos as Json data

@Service
public class YoutubeClient {

    public YoutubeClient() {
    }

    public String getYoutubeVideos(String queryParam) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        String uri = YOUTUBE_URL + queryParam + API_KEY;

        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        return response.getBody();
    }
}
