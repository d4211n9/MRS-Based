package dk.easv.presentation.controller;

import dk.easv.presentation.model.AppModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SignUpController extends BaseController {
    @FXML
    private TextField userId;

    public void login(MouseEvent mouseEvent) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Login.fxml"));
        Parent root = loader.load();
        Scene loginScene = new Scene(root);

        Stage stage = new Stage();
        stage.setScene(loginScene);

        ((Stage) userId.getScene().getWindow()).close();

        stage.show();

        ((BaseController) loader.getController()).setModel(getModel());
    }
}
