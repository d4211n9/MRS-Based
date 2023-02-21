package dk.easv;

import dk.easv.presentation.controller.BaseController;
import dk.easv.presentation.model.AppModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("presentation/view/Login.fxml"));
        Parent root = loader.load();
        ((BaseController) loader.getController()).setModel(new AppModel());

        primaryStage.setTitle("Movie Recommendation System 0.01 Beta");
        // primaryStage.setFullScreen(true);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
