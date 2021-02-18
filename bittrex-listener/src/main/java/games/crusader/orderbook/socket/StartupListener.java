package games.crusader.orderbook.socket;/*
 * Last tested 2020/09/24 OpenJDK Runtime Environment
 *   (build 11.0.6+10-post-Ubuntu-1ubuntu118.04.1)
 *
 * Note: This file is intended solely for testing purposes and may only be used
 *   as an example to debug and compare with your code. The 3rd party libraries
 *   used in this example may not be suitable for your production use cases.
 *   You should always independently verify the security and suitability of any
 *   3rd party library used in your code.
 *
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import games.crusader.orderbook.messaging.Producer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class StartupListener {
    static String API_URL = "https://socket-v3.bittrex.com/signalr";
    static String API_KEY = "";
    static String API_SECRET = "";
    private Producer producer;

    public StartupListener (Producer producer){
        this.producer = producer;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationStart() throws Exception {
        final var client = new SocketClient(API_URL);
        if (!connect(client)) {
            throw new Exception("Could not connect to server");
        }

        if (!API_SECRET.isEmpty()) {
            authenticateClient(client);
        } else {
            System.out.println("Authentication skipped because API key was not provided");
        }

        subscribe(client);
    }

    Boolean connect(SocketClient client) {
        var connected = false;
        try {
            connected = client.connect();
        } catch (Exception e) {
            System.out.println(e);
        }

        if (connected) {
            System.out.println("Connected");
        } else {
            System.out.println("Failed to connect");
        }
        return connected;
    }

    void authenticateClient(SocketClient client) {
        if (authenticate(client, API_KEY, API_SECRET)) {
            final var scheduler = Executors.newScheduledThreadPool(1);
            var authExpiringHandler = new Object() {
                public void authenticationExpiring() {
                    System.out.println("Authentication expiring...");
                    scheduler.schedule(new Runnable() {
                        @Override
                        public void run() {
                            authenticate(client, API_KEY, API_SECRET);
                        }
                    }, 1, TimeUnit.SECONDS);
                }
            };
            client.setMessageHandler(authExpiringHandler);
        }
    }

    Boolean authenticate(SocketClient client, String apiKey, String apiSecret) {
        try {
            var response = client.authenticate(apiKey, apiSecret);
            if (response.Success) {
                System.out.println("Authenticated");
            } else {
                System.out.println("Failed to authenticate: " + response.ErrorCode);
            }
            return response.Success;
        } catch (Exception e) {
            System.out.println("Failed to authenticate: " + e.toString());
            return false;
        }
    }

    void subscribe(SocketClient client) {
        var channels = new String[]{"heartbeat", "trade_DOGE-USD", "orderbook_DOGE-USD_25"};

        var msgHandler = new Object() {
            public void heartbeat() {
                System.out.println("<heartbeat>");
            }

            public void trade(String compressedData) {
                // If subscribed to multiple market's trade streams,
                // use the marketSymbol field in the message to differentiated
                // printSocketMessage("Trade", compressedData);
                producer.sendTradeUpdate(uncompressData(compressedData));
            }

            public void orderbook(String compressedData){
                producer.sendOrderUpdate(uncompressData(compressedData));
            }
        };

        client.setMessageHandler(msgHandler);
        try {
            var response = client.subscribe(channels);
            for (int i = 0; i < channels.length; i++) {
                System.out.println(channels[i] + ": " + (response[i].Success ? "Success" : response[i].ErrorCode));
            }
        } catch (Exception e) {
            System.out.println("Failed to subscribe: " + e.toString());
        }
    }

    private String uncompressData(String compressedData){
        try {
            JsonObject dataObject = DataConverter.decodeMessage(compressedData);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(dataObject);
        } catch (Exception e) {
            String errorMessage = "Error decompressing message - " + e.toString() + " - " + compressedData;
            System.out.println(errorMessage);
            throw new RuntimeException(errorMessage);
        }
    }

    void printSocketMessage(String msgType, String compressedData) {
        String text = uncompressData(compressedData);
        System.out.println(msgType + ": " + text);
    }
}



