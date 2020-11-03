package com.longhdi.component.video;

import com.longhdi.qualifier.Id;
import com.longhdi.component.MayTransform;
import com.longhdi.entity.Video;
import com.longhdi.qualifier.YouTube;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.logging.Logger;

@YouTube
public class YouTubeUrlVideoFactory implements VideoFactory<String> {

    @Inject
    private HttpClient httpClient;

    @Inject
    @YouTube @Id
    private MayTransform<String> idExtractor;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public Optional<Video> buildVideo(String url) {
        Optional<String> possibleVideoId = idExtractor.apply(url);
        if (possibleVideoId.isPresent()) {
            String youtubeVideoId = possibleVideoId.get();
            URI uri = buildRestEndPoint(youtubeVideoId);
            HttpRequest request = HttpRequest.newBuilder(uri).build();
            try {
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                JsonReader reader = Json.createReader(new StringReader(response.body()));
                JsonObject jsonObject = reader.readObject();
                JsonObject snippet = jsonObject.getJsonArray("items").get(0).asJsonObject().getJsonObject("snippet");
                Video video = new Video();
                video.setUrl(url);
                video.setPartnerId(youtubeVideoId);
                video.setTitle(snippet.getString("title"));
                video.setDescription(snippet.getString("description"));
                return Optional.of(video);
            } catch (Exception e) {
                logger.severe(String.format("Cannot build video because: %s", e.getMessage()));
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    private URI buildRestEndPoint(String videoId) {
        String apiKey = System.getenv("GOOGLE_API_KEY");
        return URI.create(new StringBuilder()
                .append("https://www.googleapis.com/youtube/v3/videos?part=id%2C%20snippet&id=")
                .append(videoId)
                .append("&key=")
                .append(apiKey)
                .toString());
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

}
