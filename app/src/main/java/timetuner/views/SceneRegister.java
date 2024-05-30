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

public class SceneRegister {
    private Stage primaryStage;
    private StackPane root;
    private Scene scene;
    private TextField usernameField;
    private TextField emailField;
    private TextField passwordField;
    private TextField confirmPasswordField;
    private double screenWidth;
    private double screenHeight;

    public SceneRegister(Stage primaryStage, double screenWidth, double screenHeight) {
        this.primaryStage = primaryStage;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        root = new StackPane();
        scene = new Scene(root, screenWidth, screenHeight);
        scene.getStylesheets().add(getClass().getResource(App.style).toExternalForm());

        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.getStyleClass().add("text-field");
        
        emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.getStyleClass().add("text-field");
        
        passwordField = new TextField();
        passwordField.setPromptText("Enter your password");
        passwordField.getStyleClass().add("text-field");
        
        confirmPasswordField = new TextField();
        confirmPasswordField.setPromptText("Confirm your password");
        confirmPasswordField.getStyleClass().add("text-field");

        VBox fieldContainer = new VBox();
        fieldContainer.getChildren().addAll(usernameField, emailField, passwordField, confirmPasswordField);
        fieldContainer.getStyleClass().add("container");
        
        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> registerButtonHandler());
        
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> new SceneLogin(primaryStage, screenWidth, screenHeight).show());
        
        HBox buttonContainer = new HBox(registerButton, cancelButton);
        buttonContainer.getStyleClass().addAll("container");
        
        VBox mainContainer = new VBox(fieldContainer, buttonContainer);
        mainContainer.getStyleClass().addAll("form","container");
        StackPane.setAlignment(mainContainer, Pos.CENTER);
        root.getChildren().add(mainContainer);
        root.getStyleClass().add("background");
    }

    public void show() {
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void registerButtonHandler() {
        boolean isEmptyField = false;
    
        if (usernameField.getText().isEmpty()) {
            usernameField.getStyleClass().add("error");
            usernameField.setPromptText("Username is required");
            isEmptyField = true;
        }
    
        if (emailField.getText().isEmpty()) {
            emailField.getStyleClass().add("error");
            emailField.setPromptText("Email is required");
            isEmptyField = true;
        }
        
        if (UserController.cekEmail(emailField.getText())) {
            emailField.getStyleClass().add("error");
            emailField.clear();
            emailField.setPromptText("Email Already Exist");
            isEmptyField = true;
        }
    
        if (passwordField.getText().isEmpty()) {
            passwordField.getStyleClass().add("error");
            passwordField.setPromptText("Password is required");
            isEmptyField = true;
        }
    
        if (confirmPasswordField.getText().isEmpty()) {
            confirmPasswordField.getStyleClass().add("error");
            confirmPasswordField.setPromptText("Confirm password is required");
            isEmptyField = true;
        }
    
        if (isEmptyField) {
            usernameField.setOnKeyTyped(event -> usernameField.getStyleClass().remove("error"));
            emailField.setOnKeyTyped(event -> emailField.getStyleClass().remove("error"));
            passwordField.setOnKeyTyped(event -> passwordField.getStyleClass().remove("error"));
            confirmPasswordField.setOnKeyTyped(event -> confirmPasswordField.getStyleClass().remove("error"));
            return;
        }
    
        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            passwordField.getStyleClass().add("error");
            confirmPasswordField.getStyleClass().add("error");
            passwordField.clear();
            confirmPasswordField.clear();
            passwordField.setPromptText("Passwords do not match");
            confirmPasswordField.setPromptText("Passwords do not match");
            passwordField.setOnKeyTyped(event -> passwordField.getStyleClass().remove("error"));
            confirmPasswordField.setOnKeyTyped(event -> confirmPasswordField.getStyleClass().remove("error"));
            return;
        }
    
        if (!UserController.register(usernameField.getText(),emailField.getText(),passwordField.getText(),null)) {
            usernameField.getStyleClass().add("error");
            emailField.getStyleClass().add("error");
            passwordField.getStyleClass().add("error");
            confirmPasswordField.getStyleClass().add("error");
            emailField.clear();
            usernameField.clear();
            passwordField.clear();
            confirmPasswordField.clear();
            emailField.setPromptText("Registration failed, try again");
            usernameField.setPromptText("Registration failed, try again");
            passwordField.setPromptText("Registration failed, try again");
            confirmPasswordField.setPromptText("Registration failed, try again");
        } else {
            new SceneLogin(primaryStage, screenWidth, screenHeight).show();
        }
    
        usernameField.setOnKeyTyped(event -> usernameField.getStyleClass().remove("error"));
        emailField.setOnKeyTyped(event -> emailField.getStyleClass().remove("error"));
        passwordField.setOnKeyTyped(event -> passwordField.getStyleClass().remove("error"));
        confirmPasswordField.setOnKeyTyped(event -> confirmPasswordField.getStyleClass().remove("error"));
    }    
}