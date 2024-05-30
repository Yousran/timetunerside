package timetuner.views;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import timetuner.App;
import timetuner.controllers.UserController;

public class SceneLogin {
    private Stage primaryStage;
    private StackPane root;
    private Scene scene;
    private TextField emailField;
    private TextField passwordField;
    private double screenWidth;
    private double screenHeight;

    public SceneLogin(Stage primaryStage, double screenWidth, double screenHeight) {
        this.primaryStage = primaryStage;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    
        root = new StackPane();
        scene = new Scene(root, screenWidth, screenHeight);
        
        scene.getStylesheets().add(getClass().getResource(App.style).toExternalForm());
    
        emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.getStyleClass().add("text-field");
    
        passwordField = new TextField();
        passwordField.setPromptText("Enter your password");
        passwordField.getStyleClass().add("text-field");
    
        VBox fieldContainer = new VBox(emailField, passwordField);
        fieldContainer.getStyleClass().add("container");
    
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> loginButtonHandler());
        loginButton.getStyleClass().add("button");
        
        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> new SceneRegister(primaryStage, screenWidth, screenHeight).show());
        loginButton.getStyleClass().add("button");
    
        HBox buttonContainer = new HBox(loginButton, registerButton);
        buttonContainer.getStyleClass().add("container");
    
        VBox mainContainer = new VBox(fieldContainer, buttonContainer);
        mainContainer.getStyleClass().addAll("form");
        StackPane.setAlignment(mainContainer, Pos.CENTER);
        root.getChildren().add(mainContainer);
        root.getStyleClass().add("background");
    }    

    public void show() {
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loginButtonHandler() {
        boolean isEmptyField = false;
    
        if (emailField.getText().isEmpty()) {
            emailField.getStyleClass().add("error");
            emailField.setPromptText("Email is required");
            isEmptyField = true;
        }
    
        if (passwordField.getText().isEmpty()) {
            passwordField.getStyleClass().add("error");
            passwordField.setPromptText("Password is required");
            isEmptyField = true;
        }
    
        if (isEmptyField) {
            emailField.setOnKeyTyped(event -> emailField.getStyleClass().remove("error"));
            passwordField.setOnKeyTyped(event -> passwordField.getStyleClass().remove("error"));
            return;
        }
    
        boolean loginSuccess = UserController.login(emailField.getText(), passwordField.getText());
        if (!loginSuccess) {
            emailField.getStyleClass().add("error");
            passwordField.getStyleClass().add("error");
            emailField.clear();
            passwordField.clear();
            emailField.setPromptText("Login failed, try again");
            passwordField.setPromptText("Login failed, try again");
        } else {
            System.out.println(App.loggedUser.getUsername() + " Berhasil Login");
            emailField.clear();
            passwordField.clear();
            SceneMain sceneMain = new SceneMain(primaryStage, screenWidth, screenHeight);
            sceneMain.show();
        }
        emailField.setOnKeyTyped(event -> emailField.getStyleClass().remove("error"));
        passwordField.setOnKeyTyped(event -> passwordField.getStyleClass().remove("error"));
    }    
    
}