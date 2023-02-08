package dk.easv.presentation.controller;

import dk.easv.entities.*;
import dk.easv.presentation.model.AppModel;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.File;
import java.net.URL;
import java.util.*;

public class AppController implements Initializable {
    @FXML
    private ListView<User> lvUsers;
    @FXML
    private ListView<Movie> lvTopForUser;
    @FXML
    private ListView<Movie> lvTopAvgNotSeen;

    @FXML
    private Label lblUserName;

    @FXML
    private FlowPane fpDisplay;
    @FXML
    private ListView<UserSimilarity> lvTopSimilarUsers;
    @FXML
    private ListView<TopMovie> lvTopFromSimilar;


    private AppModel model;
    private long timerStartMillis = 0;
    private String timerMsg = "";

    private void startTimer(String message){
        timerStartMillis = System.currentTimeMillis();
        timerMsg = message;
    }

    private void stopTimer(){
        System.out.println(timerMsg + " took : " + (System.currentTimeMillis() - timerStartMillis) + "ms");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setModel(AppModel model) {
        this.model = model;
        lblUserName.setText(model.getUsername());
        //lvUsers.setItems(model.getObsUsers());
        //lvTopForUser.setItems(model.getObsTopMovieSeen());

        startTimer("Load users");
        model.loadUsers();
        stopTimer();


        model.loadData(model.getObsLoggedInUser());
        ObservableList<Movie> movieList = model.getObsTopMovieNotSeen();
        for (Movie m: model.getObsTopMovieSeen()) {
            String titleAndRating = m.getTitle() + ", " + String.format("%.2f",m.getAverageRating());

            VBox newCard = createMovieVbox(titleAndRating);
            fpDisplay.getChildren().addAll(newCard);
        }

        //lvTopAvgNotSeen.setItems(model.getObsTopMovieNotSeen());
        //lvTopSimilarUsers.setItems(model.getObsSimilarUsers());
        //lvTopFromSimilar.setItems(model.getObsTopMoviesSimilarUsers());


        // Select the logged-in user in the listview, automagically trigger the listener above
        //lvUsers.getSelectionModel().select(model.getObsLoggedInUser());
    }


    private VBox createMovieVbox(String movie)
    {
        File file = new File("C:/Users/andre/OneDrive/Skrivebord/unnamed.jpg");
        Image image = new Image(file.toURI().toString());
        VBox mov = new VBox();
        mov.getChildren().add(new ImageView(image));
        mov.getChildren().add(new Label(movie));
        mov.setSpacing(100);


        return mov;
    }
}
