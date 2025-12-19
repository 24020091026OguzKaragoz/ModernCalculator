module org.t3tracon.moderncalculator {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.scripting;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens org.t3tracon.moderncalculator to javafx.fxml;

    exports org.t3tracon.moderncalculator;
}