package dk.easv.presentation.controller;

import dk.easv.entities.*;
import dk.easv.presentation.model.AppModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.*;

public class AppController extends BaseController implements Initializable {
    ArrayList<Button> buttons = new ArrayList<>();

    ArrayList<VBox> movies = new ArrayList<>();

    ArrayList<VBox> searchResults = new ArrayList<>();

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

    @FXML
    private TextField txtSearch;


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
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        buttons.add(btnHome);
        buttons.add(btnAllMovies);
        buttons.add(btnMyratings);
        buttons.add(btnRatings);

    }

    public void setModel(AppModel model) {
        super.setModel(model);
        lblUserName.setText(model.getUsername());
        File file = new File("data/logo.png");
        Image image = new Image(file.toURI().toString());
        File pfFile = new File("data/avatar.png");
        Image PfImage = new Image(pfFile.toURI().toString());

        ivlogo.setImage(image);
        ivUserAvatar.setImage(PfImage);

        startTimer("Load users");
        getModel().loadUsers();
        stopTimer();


        getModel().loadData(getModel().getObsLoggedInUser());
        ObservableList<Movie> movieList = getModel().getObsTopMovieNotSeen();
        buildHome();
    }

    private void buildHome()
    {
        int timesRun = 0;
        for (TopMovie m: getModel().getObsTopMoviesSimilarUsers()) {
            String titleAndRating = m.getTitle() + "\n" + String.format("%.2f",m.getAverageRating());

            VBox newCard = createMovieVbox(titleAndRating);
            fpDisplay.getChildren().addAll(newCard);
            fpDisplay.setVgap(10);
            fpDisplay.setHgap(10);
            timesRun++;
            if (timesRun == 82)
            {
                break;
            }
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
        int timesRun = 0;
        resetFlowPane();
        resetsearchResults();
        resetButtons();
       for (Movie m: getModel().getObsTopMovieNotSeen())
        {
           String movie = m.getTitle() + "\n" + String.format("%.2f",m.getAverageRating());
            fpDisplay.getChildren().add(createMovieVbox(movie));
            timesRun++;
            if (timesRun == 82)
            {
                break;
            }
        }
       lblTitle.setText("All Movies:");
        btnAllMovies.getStyleClass().add("selectedButton");
        btnHome.getStyleClass().add("lblSelectedButton");
    }

    public void handleHome(ActionEvent event)
    {
        resetFlowPane();
        resetsearchResults();
        buildHome();
    }

    public void handleMyratings(ActionEvent event)
    {
        resetFlowPane();
        resetsearchResults();
        resetButtons();
        for (Movie m:getModel().getObsTopMovieSeen())
        {
            String movie = m.getTitle() + "\n" + String.format("%.2f",m.getAverageRating());
            fpDisplay.getChildren().add(createMovieVbox(movie));
        }
        lblTitle.setText("My Ratings:");
        btnMyratings.getStyleClass().add("selectedButton");
    }

    public void handleGetRating(ActionEvent event)
    {
        resetFlowPane();
        resetsearchResults();
        resetButtons();
        for (Movie m: getModel().getObsTopMovieSeen())
        {
            String movie = m.getTitle() + "\n" + String.format("%.2f",m.getAverageRating());
            fpDisplay.getChildren().add(createMovieVbox(movie));
        }
        lblTitle.setText("Top Rated:");
        btnRatings.getStyleClass().add("selectedButton");
    }

    private void search(String qury)
    {
        for (VBox v: movies)
        {
            for (Node n: v.getChildren() ) {
                if (n instanceof Label) {
                    Label current = (Label) n;
                    String title = ((Label) n).getText();
                    if(title.toLowerCase().contains(qury.toLowerCase()))
                    {
                        searchResults.add((VBox) n.getParent());
                        resetFlowPane();
                        fpDisplay.getChildren().addAll(searchResults);
                        lblTitle.setText("Search Results:");
                    }
                }
            }
        }
    }

    private void resetsearchResults()
    {
        Iterator<VBox> itr = searchResults.iterator();
        while (itr.hasNext())
        {
            VBox i = itr.next();
            itr.remove();
        }
        txtSearch.clear();
    }

    public void handleSearch(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER)
        {
            String qury = txtSearch.getText();
            search(qury);
            if(txtSearch.getText().isEmpty())
            {
                buildHome();
            }
        }
    }
}
