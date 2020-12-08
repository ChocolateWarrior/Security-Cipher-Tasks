package com.security.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class Client {
    private static final String URL = "http://95.217.177.249/casino/";
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private static final HttpClient client = HttpClient.newHttpClient();

    public static void createAcc(final String playerId) throws Exception {
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "createacc?id=" + playerId))
                .build();

        final HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 201) {
            System.out.println("Account already created");
        } else {
            final Account account = objectMapper.readValue(response.body(), Account.class);
            System.out.println(account.toString());
        }
    }

    public static Optional<Bet> createBet(final String mode,
                                          final String playerId,
                                          final long amountOfMoney,
                                          final long number) throws Exception {
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + "play" + mode + "?id=" + playerId + "&bet=" + amountOfMoney + "&number=" + number))
                .build();

        final HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        return response.statusCode() == 200 ? Optional.of(objectMapper.readValue(response.body(), Bet.class)) : Optional.empty();
    }
}
