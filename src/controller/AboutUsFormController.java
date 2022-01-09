package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.Window;

public class AboutUsFormController {
    public Button btnAboutUs;

    public void btnAboutUsOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) btnAboutUs.getScene().getWindow();
        stage.close();
    }
}
