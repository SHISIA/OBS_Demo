module org.platform.obs_demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires Java.WebSocket;
    requires org.apache.commons.codec;
    requires com.google.gson;


    opens org.platform.obs_demo to javafx.fxml;
    exports org.platform.obs_demo;
}