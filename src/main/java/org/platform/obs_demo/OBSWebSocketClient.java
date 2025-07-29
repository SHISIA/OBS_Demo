package org.platform.obs_demo;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class OBSWebSocketClient extends WebSocketClient {

    private boolean connected = false;

    public OBSWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    // Connect to OBS WebSocket server confirmation
    public boolean isConnectedAndReady() {
        return connected && super.isOpen(); // extra safety
    }

    // Handle connection open
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to OBS");

        // Send Identify message (required by OBS WebSocket 5.x)
        String identifyPayload = """
        {
          "op": 1,
          "d": {
            "rpcVersion": 1
          }
        }
        """;
        this.send(identifyPayload);
    }


    // Handle incoming messages
    @Override
    public void onMessage(String message) {
        System.out.println("Received: " + message);
        if (message.contains("\"op\":2")) {
            System.out.println("✅ Successfully identified with OBS!");
            connected = true; // allow sending commands now
        }
    }


    // Handle connection close
    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed: " + reason);
        connected = false;
    }

    // Handle errors
    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}
