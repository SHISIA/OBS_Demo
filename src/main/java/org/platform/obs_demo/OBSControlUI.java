package org.platform.obs_demo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URI;

public class OBSControlUI extends Application {

    private OBSWebSocketClient client;

    @Override
    public void start(Stage stage) throws Exception {
        URI uri = new URI("ws://localhost:4455");
        client = new OBSWebSocketClient(uri);
        client.connect();

        // UI Elements
        Label statusLabel = new Label("🔄 Connecting to OBS...");
        Button testButton = new Button("Test");
        Button passButton = new Button("Pass");

        testButton.setDisable(true);
        passButton.setDisable(true);

        // Button Actions
        testButton.setOnAction(e -> {
            if (client.isConnectedAndReady()) {
                sendSceneSwitch("Test", statusLabel);
            } else {
                statusLabel.setText("❌ Not connected to OBS.");
            }
        });

        passButton.setOnAction(e -> {
            if (client.isConnectedAndReady()) {
                sendSceneSwitch("Pass", statusLabel);
            } else {
                statusLabel.setText("❌ Not connected to OBS.");
            }
        });

        // Enable buttons when WebSocket is ready
        new Thread(() -> {
            while (!client.isConnectedAndReady()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {}
            }
            Platform.runLater(() -> {
                testButton.setDisable(false);
                passButton.setDisable(false);
                statusLabel.setText("✅ Connected to OBS.");
            });
        }).start();

        // Layout
        VBox root = new VBox(15, statusLabel, testButton, passButton);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        // Scene
        Scene scene = new Scene(root, 300, 180);
        stage.setScene(scene);
        stage.setTitle("OBS WebSocket Control");
        stage.show();

        // Close WebSocket connection on application exit
        stage.setOnCloseRequest(e-> {
            if (client != null && client.isOpen()) {
                client.close();
            }
        });
    }

    // Method to send scene switch command
    // This method constructs the JSON payload and sends it to OBS
    // It updates the status label based on success or failure
    private void sendSceneSwitch(String sceneName, Label statusLabel) {
        String requestId = String.valueOf(System.currentTimeMillis());
        String json = "{\n" +
                "  \"op\": 6,\n" +
                "  \"d\": {\n" +
                "    \"requestType\": \"SetCurrentProgramScene\",\n" +
                "    \"requestId\": \"" + requestId + "\",\n" +
                "    \"requestData\": {\n" +
                "      \"sceneName\": \"" + sceneName + "\"\n" +
                "    }\n" +
                "  }\n" +
                "}";

        try {
            client.send(json);
            statusLabel.setText("➡️ Switched to scene: " + sceneName);
        } catch (Exception ex) {
            statusLabel.setText("❌ Failed to switch to scene: " + sceneName);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
