package com.vaatu.bots.dixtro.service;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class YoutubeService {

    @Value("${apiKey}")
    private String apiKey;
    final YouTube youtube;

    public YoutubeService() {
        youtube = new YouTube(new NetHttpTransport(), new JacksonFactory(), httpRequest -> {});
    }

    public String getVideoUrl(String keyword) throws IOException {
        YouTube.Search.List search = youtube.search().list("id,snippet");

        search.setKey(apiKey);
        search.setQ(keyword);

        search.setType("video");

        search.setMaxResults(5L);

        SearchListResponse searchResponse = search.execute();
        List<SearchResult> searchResultList = searchResponse.getItems();
        SearchResult firstResult = searchResultList.getFirst();
        return firstResult.getId().getVideoId();
    }

}
