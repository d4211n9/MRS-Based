package dk.easv.presentation.controller;

import dk.easv.entities.*;
import dk.easv.presentation.model.AppModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.net.URL;
import java.util.*;

public class AppController extends BaseController implements Initializable {
    ArrayList<Button> buttons = new ArrayList<>();

    ArrayList<VBox> movies = new ArrayList<>();

    @FXML
    private ImageView ivlogo, ivUserAvatar;
    @FXML
    private Label lblTitle;
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
    @FXML
    private Button btnHome, btnAllMovies, btnRatings, btnMyratings;


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
        super.setModel(model);
        lblUserName.setText(model.getUsername());

        startTimer("Load users");
        model.loadUsers();
        stopTimer();


        model.loadData(model.getObsLoggedInUser());
        ObservableList<Movie> movieList = model.getObsTopMovieNotSeen();
        buildHome();
    }

    private void buildHome()
    {
        for (TopMovie m: model.getObsTopMoviesSimilarUsers()) {
            String titleAndRating = m.getTitle() + ", " + String.format("%.2f",m.getAverageRating());

            VBox newCard = createMovieVbox(titleAndRating);
            fpDisplay.getChildren().addAll(newCard);
            fpDisplay.setVgap(10);
            fpDisplay.setHgap(10);
        }
        resetButtons();
        lblTitle.setText("Rrecommended:");
        btnHome.getStyleClass().add("selectedButton");
    }

    private VBox createMovieVbox(String movie)
    {
       File file = new File("data/unavalible.png");
        Image image = new Image(file.toURI().toString());
        VBox mov = new VBox();
        mov.getChildren().add(new ImageView(image));
        mov.getChildren().add(new Label(movie));
        movies.add(mov);
        return mov;
    }

    private void resetFlowPane()
    {
        for (VBox v: movies)
        {
            fpDisplay.getChildren().remove(v);
        }
        resetMoviesList();
    }

    private void resetMoviesList()
    {
        Iterator<VBox> itr = movies.iterator();
        while (itr.hasNext())
        {
            VBox i =itr.next();
            itr.remove();
        }
    }

    private void resetButtons()
    {
        for (Button b: buttons)
        {
            if(b.getStyleClass().toString().contains("selectedButton"))
            {
                b.getStyleClass().remove("selectedButton");
            }
        }
    }


    public void handelGetAllMovies(ActionEvent event)
    {
        resetFlowPane();
        resetButtons();
        for (Movie m: model.getObsTopMovieNotSeen())
        {
            String movie = m.getTitle() + ", " + String.format("%.2f",m.getAverageRating());
            fpDisplay.getChildren().add(createMovieVbox(movie));
        }
        btnAllMovies.getStyleClass().add("selectedButton");
        btnHome.getStyleClass().add("lblSelectedButton");
    }

    public void handleHome(ActionEvent event)
    {
        resetFlowPane();
        buildHome();
    }

    public void handleMyratings(ActionEvent event)
    {
        resetFlowPane();
        resetButtons();
        for (Movie m:model.getObsTopMovieSeen())
        {
            String movie = m.getTitle() + ", " + String.format("%.2f",m.getAverageRating());
            fpDisplay.getChildren().add(createMovieVbox(movie));
        }
        lblTitle.setText("My Ratings:");
        btnMyratings.getStyleClass().add("selectedButton");
    }

    public void handleGetRating(ActionEvent event)
    {
        resetFlowPane();
        resetButtons();
        for (Movie m: model.getObsTopMovieSeen())
        {
            String movie = m.getTitle() + ", " + String.format("%.2f",m.getAverageRating());
            fpDisplay.getChildren().add(createMovieVbox(movie));
        }
        lblTitle.setText("Top Rated:");
        btnRatings.getStyleClass().add("selectedButton");
    }
}
