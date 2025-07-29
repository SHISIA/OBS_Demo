module org.platform.obs_demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.platform.obs_demo to javafx.fxml;
    exports org.platform.obs_demo;
}