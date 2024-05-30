package timetuner.views;

import javafx.scene.layout.HBox;

public class ComponentNavbar extends HBox {
    SceneMain sceneMain;
    public ComponentNavbar(SceneMain sceneMain){
        super();
        this.sceneMain = sceneMain;
        this.setStyle("-fx-background-color: -color-card;");
    }
    
}
