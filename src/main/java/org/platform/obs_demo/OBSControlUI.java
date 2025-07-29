package org.platform.obs_demo;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URI;

public class OBSControlUI extends Application {

    private OBSWebSocketClient client;

    @Override
    public void start(Stage primaryStage) throws Exception {
        client = new OBSWebSocketClient(new URI("ws://localhost:4455"));
        client.connect();

        Button testBtn = new Button("Test");
        Button passBtn = new Button("Pass");

        testBtn.setOnAction(e -> client.sendCommand("test"));
        passBtn.setOnAction(e -> client.sendCommand("pass"));

        HBox layout = new HBox(10, testBtn, passBtn);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 100);
        primaryStage.setTitle("OBS Control");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
