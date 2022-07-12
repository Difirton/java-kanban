package service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class KVTaskClient {
    private final URL serverURL;
    private final HttpClient kvTaskClient;
    private final String API_TOKEN;
    private final HttpResponse.BodyHandler<String> handler;

    public KVTaskClient(URL serverURL) throws URISyntaxException, IOException, InterruptedException {
        this.serverURL = serverURL;
        HttpRequest authRequest = HttpRequest.newBuilder()
                .uri(new URI(serverURL + "/register"))
                .timeout(Duration.ofSeconds(20L))
                .GET()
                .build();
        kvTaskClient = HttpClient.newHttpClient();
        handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = kvTaskClient.send(authRequest, handler);
        API_TOKEN = response.body();
    }

    public void put(String json) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest saveRequest = HttpRequest.newBuilder()
                .uri(new URI(serverURL + "/save/" + API_TOKEN))
                .timeout(Duration.ofSeconds(20L))
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        kvTaskClient.send(saveRequest, handler);
    }

    public String load() throws URISyntaxException {
        HttpRequest loadRequest = HttpRequest.newBuilder()
                .uri(new URI(serverURL + "/load"))
                .timeout(Duration.ofSeconds(20L))
                .GET()
                .build();
    }
}
