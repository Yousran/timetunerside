/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package timetuner;

import javafx.application.Application;
import javafx.stage.Stage;
import timetuner.models.User;
import timetuner.views.SceneLogin;

public class App extends Application {
    public static User loggedUser = null;
    public static String AppName = "Time Tuner";
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(true);
        // primaryStage.setMaximized(true);
        primaryStage.setTitle(AppName);
        SceneLogin sceneLogin = new SceneLogin(primaryStage,1024,720);
        sceneLogin.show();
    }
}
