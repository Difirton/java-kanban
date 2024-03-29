package service;

import error.KVTaskClientException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class KVTaskClient {
    private final URI serverURI;
    private final HttpClient kvHttpTaskClient;
    private final String apiToken;
    private final HttpResponse.BodyHandler<String> handler;

    public KVTaskClient(URI serverURI) {
        try {
            this.serverURI = serverURI;
            HttpRequest authRequest = HttpRequest.newBuilder()
                    .uri(new URI(serverURI + "/register"))
                    .timeout(Duration.ofSeconds(20L))
                    .GET()
                    .build();
            kvHttpTaskClient = HttpClient.newHttpClient();
            handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = kvHttpTaskClient.send(authRequest, handler);
            apiToken = response.body();
        } catch (URISyntaxException exception) {
            throw new KVTaskClientException("Invalid URI, check server address, port number " + exception.getMessage());
        } catch (IOException exception) {
            throw new KVTaskClientException("Error in data access when getting a token on the server " +
                    exception.getMessage());
        } catch (InterruptedException exception) {
            throw new KVTaskClientException("Error while sending message to server in thread of execution " +
                    exception.getMessage());
        }
    }

    public void put(String key, String json) {
        try {
            HttpRequest saveRequest = HttpRequest.newBuilder()
                    .uri(new URI(serverURI + "/save/" + key + "?API_TOKEN="+ apiToken))
                    .timeout(Duration.ofSeconds(20L))
                    .headers("Content-Type", "text/plain;charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            kvHttpTaskClient.send(saveRequest, handler);
        } catch (URISyntaxException exception) {
            throw new KVTaskClientException("Invalid URI, check server address, port number " + exception.getMessage());
        } catch (IOException exception) {
            throw new KVTaskClientException("Error in data access when save json of manager in the server " +
                    exception.getMessage());
        } catch (InterruptedException exception) {
            throw new KVTaskClientException("Error while sending message to server in thread of execution " +
                    exception.getMessage());
        }
    }

    public String load(String key) {
        try {
            HttpRequest loadRequest = HttpRequest.newBuilder()
                    .uri(new URI(serverURI + "/load/" + key + "?API_TOKEN="+ apiToken))
                    .timeout(Duration.ofSeconds(20L))
                    .GET()
                    .build();
            HttpResponse<String> response = kvHttpTaskClient.send(loadRequest, handler);
            return response.body();
        } catch (URISyntaxException exception) {
            throw new KVTaskClientException("Invalid URI, check server address, port number " + exception.getMessage());
        } catch (IOException exception) {
            throw new KVTaskClientException("Error in data access when load of manager from the server " +
                    exception.getMessage());
        } catch (InterruptedException exception) {
            throw new KVTaskClientException("Error while sending message to server in thread of execution " +
                    exception.getMessage());
        }
    }
}
