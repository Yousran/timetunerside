package timetuner.views;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javafx.beans.binding.StringBinding;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import timetuner.App;
import timetuner.controllers.BudgetController;
import timetuner.controllers.ProjectController;
import timetuner.controllers.TeamMemberController;
import timetuner.controllers.UserController;
import timetuner.models.Budget;
import timetuner.models.Project;
import timetuner.models.User;
import java.util.ArrayList;
import java.util.List;

public class PageAddProject extends VBox  {
    VBox budgetListVBox = new VBox();
    VBox teamListVBox = new VBox();
    Label remainingBudget;
    List<Budget> budgets = new ArrayList<>();
    List<User> users = new ArrayList<>();

    TextField field_project_name = new TextField();
    DatePicker field_due_date = new DatePicker();
    TextField field_total_budget = new TextField();
    TextField budgetNameField = new TextField();
    TextField budgetPriceField = new TextField();
    TextField usernameField = new TextField();

    String project_name, due_date, budget_name;
    int budget_price, project_budget;

    public PageAddProject() {
        super();
        HBox hBox = new HBox();
        VBox vBox = new VBox(projectStatus(), budgetStatus());
        vBox.setStyle("-fx-spacing: 1em;");
    
        vBox.prefWidthProperty().bind(hBox.widthProperty().multiply(0.70));
        teamStatus().prefWidthProperty().bind(hBox.widthProperty().multiply(0.30));
    
        hBox.getStyleClass().add("container");
        hBox.getChildren().addAll(vBox, teamStatus());
    
        Label subTitleAddNewProject = new Label("Add New Project");
        subTitleAddNewProject.getStyleClass().add("h3");
        Button saveButton = new Button("Save");
        saveButton.getStyleClass().add("button");
        saveButton.setOnAction(event -> saveBtnHandler());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox subTitle = new HBox(subTitleAddNewProject, spacer, saveButton);
        subTitle.getStyleClass().add("container");
    
        this.getChildren().addAll(subTitle, hBox);
    }
    

    public VBox teamList(){
        teamListVBox.getChildren().clear();

        for (User user : users) {
            teamListVBox.getChildren().add(teamMember(user));
        }
        return teamListVBox;
    }

    public VBox teamStatus() {
        VBox teamStatus = new VBox();
        HBox field = new HBox();
        teamStatus.getStyleClass().add("card");

        Label subTitleLabel = new Label("Add Team Member:");
        subTitleLabel.getStyleClass().add("h4");

        usernameField.setPromptText("Enter Username");
        Button addMemberBtn = new Button("Add Member");
        addMemberBtn.setOnAction(event -> addNewTeamMember(usernameField.getText()));

        field.getChildren().addAll(addMemberBtn,usernameField);

        teamStatus.getChildren().addAll(subTitleLabel, field, teamList());
        return teamStatus;
    }

    public HBox teamMember(User user){
        Image image = new Image(getClass().getResourceAsStream("/icons/user-circle-regular-240.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        Label username = new Label(user.getUsername());
        username.getStyleClass().add("h5");
        HBox teamMember = new HBox(imageView, username);
        teamMember.getStyleClass().add("container");
        return teamMember;
    }

    //TODO: add new member perlu feedback jika username tidak ditemukan
    private void addNewTeamMember(String username) {
        User user = UserController.findUser(username);
        if (user != null) {
            users.add(user);
            refreshTeamList();
        } else {
            System.out.println("User not found");
        }
    }

    private void refreshTeamList() {
        teamList();
    }

    public HBox projectStatus(){
        HBox hbox = new HBox(10);
        hbox.getStyleClass().add("card");

        VBox vboxName = new VBox(new Label("Project Name"), field_project_name);
        VBox vboxDueDate = new VBox(new Label("Due Date"), field_due_date);

        hbox.getChildren().addAll(vboxName, vboxDueDate);
        return hbox;
    }

    public VBox budgetStatus(){
        VBox budgetStatus = new VBox();
        budgetStatus.getStyleClass().add("card");
        field_total_budget.setPromptText("Total Budget");
        HBox headHBox = new HBox(field_total_budget);

        budgetNameField.setPromptText("Budget Name");
        budgetPriceField.setPromptText("Value");
        Button addBudgetBtn = new Button("+");
        addBudgetBtn.setOnAction(event -> addNewBudgetHandler(budgetNameField.getText(), Integer.parseInt(budgetPriceField.getText())));

        HBox addBudget = new HBox(addBudgetBtn, budgetNameField, budgetPriceField);
        remainingBudget = new Label("Remaining Budget : " + calculateBudget(project_budget, budgets));

        budgetStatus.getChildren().addAll(headHBox, addBudget, budgetList(), remainingBudget);
        return budgetStatus;
    }

    private VBox budgetList(){
        budgetListVBox.getChildren().clear();

        for (Budget budget : budgets) {
            Label budgetLabel = new Label(budget.getBudget_name() + " : " + budget.getPrice());
            budgetListVBox.getChildren().add(budgetLabel);
        }
        return budgetListVBox;
    }

    private void addNewBudgetHandler(String budget_name, int price){
        Budget newBudget = new Budget(0, 0, budget_name, price);
        budgets.add(newBudget);
        refreshBudgetList();
    }

    private void refreshBudgetList() {
        budgetList();
        project_budget = Integer.parseInt(field_total_budget.getText());
        int updatedRemainingBudget = calculateBudget(project_budget, budgets);
        remainingBudget.setText("Remaining Budget : " + updatedRemainingBudget);
    }

    private int calculateBudget(int totalBudget, List<Budget> budgets) {
        int totalSpent = 0;
        for (Budget budget : budgets) {
            totalSpent += budget.getPrice();
        }
        return totalBudget - totalSpent;
    }

    private void saveBtnHandler(){
        project_name = field_project_name.getText();
        due_date = field_due_date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        project_budget = Integer.parseInt(field_total_budget.getText());
    
        Project newProject = ProjectController.addProject(project_name, due_date, project_budget, null);
        if (newProject != null) {
            for (Budget budget : budgets) {
                BudgetController.addBudget(newProject.getId(), budget.getBudget_name(), budget.getPrice());
            }
            TeamMemberController.addMember(newProject.getId(), App.loggedUser.getId());
            for (User user : users) {
                TeamMemberController.addMember(newProject.getId(), user.getId());
            }
            clearFields();
            refreshTeamList();
            refreshBudgetList();
            System.out.println("Project added successfully");
        } else {
            System.out.println("Failed to add the project");
        }
    }
    
    private void clearFields() {
        budgets.clear();
        users.clear();
        field_project_name.clear();
        field_total_budget.clear();
        budgetNameField.clear();
        budgetPriceField.clear();
        usernameField.clear();
        field_due_date.setValue(null);
        teamListVBox.getChildren().clear();
        budgetListVBox.getChildren().clear();
        remainingBudget.setText("Remaining Budget : " + 0);
    }
    
}
