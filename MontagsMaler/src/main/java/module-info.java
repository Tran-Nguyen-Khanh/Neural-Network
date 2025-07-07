module GUI {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;

    opens GUI to javafx.fxml;
    exports GUI;
}