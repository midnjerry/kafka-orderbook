package games.crusader.orderbook.socket;

import com.github.signalr4j.client.ConnectionState;
import com.github.signalr4j.client.hubs.HubConnection;
import com.github.signalr4j.client.hubs.HubProxy;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

class SocketClient {
    private String _url;
    private HubConnection _hubConnection;
    private HubProxy _hubProxy;

    public SocketClient(String url) {
        _url = url;
        _hubConnection = new HubConnection(_url);
        _hubProxy = _hubConnection.createHubProxy("c3");
    }

    public Boolean connect() throws ExecutionException, InterruptedException {
        _hubConnection.start().get();
        return _hubConnection.getState() == ConnectionState.Connected;
    }

    public SocketResponse authenticate(String apiKey, String apiKeySecret)
            throws ExecutionException, InterruptedException {
        var timestamp = System.currentTimeMillis();
        var randomContent = UUID.randomUUID().toString();
        var content = "" + timestamp + randomContent.toString();
        var signedContent = "";
        try {
            signedContent = createSignature(apiKeySecret, content);
        } catch (Exception e) {
            return new SocketResponse(false, "COULD_NOT_CREATE_SIGNATURE");
        }
        var result = _hubProxy.invoke(SocketResponse.class, "Authenticate", apiKey, timestamp, randomContent, signedContent)
                .get();
        return result;
    }

    public void setMessageHandler(Object handler) {
        _hubProxy.subscribe(handler);
    }

    private static String createSignature(String apiSecret, String data)
            throws NoSuchAlgorithmException, InvalidKeyException {
        var apiKeySpec = new SecretKeySpec(apiSecret.getBytes(), "HmacSHA512");
        var mac = Mac.getInstance("HmacSHA512");
        mac.init(apiKeySpec);
        return toHexString(mac.doFinal(data.getBytes()));
    }

    private static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    public SocketResponse[] subscribe(String[] channels)
            throws ExecutionException, InterruptedException {
        return _hubProxy.invoke(SocketResponse[].class, "Subscribe", (Object) channels).get();
    }
}
