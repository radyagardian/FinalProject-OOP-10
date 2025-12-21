package com.finpro.kel10.Frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

public class DatabaseManager {

    private static final String BASE_URL = "http://localhost:8081/api";
    private final Json json;

    public static String loggedInPlayerId = null;

    public DatabaseManager() {
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
    }

    public void loginOrRegister(String username) {
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();

        String requestContent = "{\"username\":\"" + username + "\"}";

        Net.HttpRequest httpRequest = requestBuilder.newRequest()
            .method(Net.HttpMethods.POST)
            .url(BASE_URL + "/players")
            .header("Content-Type", "application/json")
            .content(requestContent)
            .build();

        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                String response = httpResponse.getResultAsString();

                PlayerResponse player = json.fromJson(PlayerResponse.class, response);

                loggedInPlayerId = player.playerId; // SIMPAN ID INI!
                System.out.println("Login Success! ID: " + loggedInPlayerId);
            }

            @Override
            public void failed(Throwable t) {
                System.out.println("Login Failed: " + t.getMessage());
            }

            @Override
            public void cancelled() { }
        });
    }

    public void submitScore(int scoreValue) {
        if (loggedInPlayerId == null) {
            System.out.println("Error: Belum login, tidak bisa upload score.");
            return;
        }

        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();

        String requestContent = "{"
            + "\"playerId\":\"" + loggedInPlayerId + "\","
            + "\"value\":" + scoreValue
            + "}";

        Net.HttpRequest httpRequest = requestBuilder.newRequest()
            .method(Net.HttpMethods.POST)
            .url(BASE_URL + "/scores")
            .header("Content-Type", "application/json")
            .content(requestContent)
            .build();

        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                System.out.println("Score Uploaded! Status: " + httpResponse.getStatus().getStatusCode());
            }

            @Override
            public void failed(Throwable t) {
                System.out.println("Score Upload Failed: " + t.getMessage());
            }

            @Override
            public void cancelled() { }
        });
    }

    private static class PlayerResponse {
        public String playerId;
        public String username;
    }
}
