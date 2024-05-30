package timetuner.views;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import timetuner.controllers.ProjectController;
import timetuner.models.Project;

public class PageDashboard extends VBox {
    
    public PageDashboard(){
        super();
        this.getStyleClass().add("container");
        Label subTitle = new Label("Project List");
        subTitle.getStyleClass().add("h3");
        this.getChildren().add(subTitle);
        for (Project project : ProjectController.getAllProjects()) {
            CardProject card = new CardProject(project);
            this.getChildren().add(card);
        }

    }
}
