package timetuner.views;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import timetuner.App;

public class ComponentNavbar extends HBox {
    SceneMain sceneMain;
    public ComponentNavbar(SceneMain sceneMain){
        super();
        this.sceneMain = sceneMain;
        this.setStyle("-fx-background-color: -color-card; -fx-padding:2em;");

        Label hello = new Label("Hello, " + App.loggedUser.getUsername());
        hello.getStyleClass().add("h2");
        hello.setStyle("-fx-alignment: CENTER-LEFT;");

        Image profileImage = new Image(getClass().getResourceAsStream("/icons/user-circle-solid-240.png"));
        ImageView profileImageView = new ImageView(profileImage);
        profileImageView.setFitHeight(40);
        profileImageView.setFitWidth(40);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        this.getChildren().addAll(hello, spacer, profileImageView);
        this.setAlignment(Pos.CENTER_LEFT);
    }
    
}
