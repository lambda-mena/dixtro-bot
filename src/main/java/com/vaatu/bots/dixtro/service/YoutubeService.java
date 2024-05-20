package com.vaatu.bots.dixtro.service;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class YoutubeService {

    @Value("${API_KEY}")
    private String apiKey;
    final YouTube youtube;

    public YoutubeService() {
        youtube = new YouTube(new NetHttpTransport(), new JacksonFactory(), httpRequest -> {});
    }

    private String getVideoID(String videoURL) throws URISyntaxException {
        URI uri = new URI(videoURL);
        String[] videoChars;

        if (videoURL.contains("watch")) {
            videoChars = uri.getQuery().split("[=&]+");
        } else {
            videoChars = uri.getPath().split("/");
        }

        for (String videoChar: videoChars) {
            System.out.println(videoChar);
        }

        return videoChars[1];
    }

    public String getVideoTitle(String videoURL) {
        try {
            YouTube.Search.List search = youtube.search().list("id,snippet");

            search.setKey(apiKey);
            search.setQ(getVideoID(videoURL));

            search.setType("video");

            search.setMaxResults(1L);

            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            SearchResult firstVideo = searchResultList.getFirst();
            return firstVideo.getSnippet().getTitle();
        } catch (URISyntaxException e) {
            return "Invalid source.";
        } catch (IOException e) {
            return "Internal error.";
        }
    }

    public SearchResult getVideoUrl(String keyword) throws IOException {
        YouTube.Search.List search = youtube.search().list("id,snippet");

        search.setKey(apiKey);
        search.setQ(keyword);

        search.setType("video");

        search.setMaxResults(1L);

        SearchListResponse searchResponse = search.execute();
        List<SearchResult> searchResultList = searchResponse.getItems();
        return searchResultList.getFirst();
    }

}
