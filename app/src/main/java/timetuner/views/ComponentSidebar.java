package timetuner.views;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ComponentSidebar extends VBox {
    private Button btnDashboard, btnAddNewProject, btnSettings, btnLogout;
    private SceneMain sceneMain;

    public ComponentSidebar(SceneMain sceneMain){
        super();
        this.sceneMain = sceneMain;
        System.out.println(sceneMain.toString());
        this.getStyleClass().add("sidebar");
        loadBtn();
        btnDashboard.fire();
    }

    private void loadBtn() {
        btnDashboard = createBtn("home-regular-240.png", "Dashboard");
        btnAddNewProject = createBtn("calendar-plus-regular-240.png", "Add New Project");
        btnSettings = createBtn("cog-regular-240.png", "Settings");
        btnLogout = createBtn("log-out-circle-regular-240.png", "Logout");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        this.getChildren().addAll(btnDashboard, btnAddNewProject, btnSettings, spacer, btnLogout);
    }

    private Button createBtn(String icon, String text){
        Button btn = new Button(text);

        Image image = new Image(getClass().getResourceAsStream("/icons/" + icon));
        
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        
        btn.setGraphic(imageView);
        btn.setContentDisplay(ContentDisplay.LEFT);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setOnAction(event -> BtnClickHandler(btn));
        btn.getStyleClass().add("button");

        return btn;
    }

    //TODO: state active masih belum berjalan
    private void BtnClickHandler(Button clickedButton) {
        btnSetFalse();
        sceneMain.updatePage(clickedButton.getText());
        clickedButton.getStyleClass().add("button-active");
    }
    private void btnSetFalse() {
        btnDashboard.getStyleClass().remove("button-active");
        btnAddNewProject.getStyleClass().remove("button-active");
        btnSettings.getStyleClass().remove("button-active");
        btnLogout.getStyleClass().remove("button-active");
    }
}
