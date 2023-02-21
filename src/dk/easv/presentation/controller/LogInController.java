package dk.easv.presentation.controller;

import dk.easv.entities.User;
import dk.easv.presentation.model.AppModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.security.Key;
import java.util.ResourceBundle;

public class LogInController implements Initializable {
    @FXML private PasswordField passwordField;
    @FXML private TextField userId;
    private AppModel model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new AppModel();
    }

    public void logIn(ActionEvent actionEvent) {
        startApp();
    }

    private void startApp() {
        getModel().loadUsers();
        getModel().loginUserFromUsername(userId.getText());
        getModel().saveUsername(userId.getText());
        if(getModel().getObsLoggedInUser()!=null){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/presentation/view/App.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Movie Recommendation System 0.01 Beta");
                stage.show();
                AppController controller = loader.getController();

                controller.setModel(getModel());

                ((Stage) userId.getScene().getWindow()).close();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load App.fxml");
                alert.showAndWait();
            }

        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "Wrong username or password");
            alert.showAndWait();
        }
    }

    public void signUp(MouseEvent actionEvent) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/SignUp.fxml"));
        Parent root = loader.load();
        Scene signUpScene = new Scene(root);

        Stage stage = new Stage();
        stage.setScene(signUpScene);

        ((Stage) userId.getScene().getWindow()).close();

        stage.show();

        ((BaseController) loader.getController()).setModel(getModel());
    }

    public void onEnterPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) startApp();
    }
}
