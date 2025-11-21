module cs102groupproject {
    requires javafx.controls;
    requires javafx.fxml;

    opens cs102groupproject to javafx.fxml;
    exports cs102groupproject;
}
