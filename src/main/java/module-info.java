module com.example.brixx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.xml;
    requires java.logging;

    opens com.example.brixx to javafx.fxml;
    exports com.example.brixx;
    exports com.example.brixx.models;
    opens com.example.brixx.models to javafx.fxml;
    exports com.example.brixx.models.brick;
    opens com.example.brixx.models.brick to javafx.fxml;
    exports com.example.brixx.models.wallbuilder;
    opens com.example.brixx.models.wallbuilder to javafx.fxml;
}