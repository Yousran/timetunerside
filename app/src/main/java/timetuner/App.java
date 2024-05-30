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
    //TODO:validasi field login
    //TODO:validasi field register
    //TODO:validasi field addmember
    //TODO:validasi field addbudget
    //TODO:validasi datepicker agar menjadi yyyy-mm-dd
    //TODO:validasi field addproject
    //TODO:dark mode
    //TODO:utils
    //TODO:intToRupiah
    //TODO:remove budget dengan membuat button di samping kiri budget yang akan muncul jika di hover
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(true);
        // primaryStage.setMaximized(true);
        primaryStage.setTitle(AppName);
        SceneLogin sceneLogin = new SceneLogin(primaryStage,1024,720);
        sceneLogin.show();
    }
}
