package timetuner.views;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import javafx.util.StringConverter;
import timetuner.App;
import timetuner.SelfUtils;
import timetuner.controllers.BudgetController;
import timetuner.controllers.ProjectController;
import timetuner.controllers.TeamMemberController;
import timetuner.controllers.UserController;
import timetuner.models.Budget;
import timetuner.models.Project;
import timetuner.models.User;
import java.util.ArrayList;
import java.util.List;

public class PageAddProject extends VBox implements InterfacePageProject  {
    VBox budgetListVBox = new VBox();
    VBox teamListVBox = new VBox();
    Label remainingBudget;
    List<Budget> budgets = new ArrayList<>();
    List<User> users = new ArrayList<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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
        saveButton.getStyleClass().add("btn");
        saveButton.setOnAction(event -> saveBtnHandler());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox subTitle = new HBox(subTitleAddNewProject, spacer, saveButton);
        subTitle.getStyleClass().add("container");
    
        this.getChildren().addAll(subTitle, hBox);
    }
    
    @Override
    public HBox projectStatus(){
        HBox hbox = new HBox(10);
        hbox.getStyleClass().add("card");

        field_due_date.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return formatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, formatter);
                } else {
                    return null;
                }
            }
        });

        field_project_name.setPromptText("Project");
        field_due_date.getEditor().setPromptText("yyyy-MM-dd");
        field_due_date.getStyleClass().add("text-field");


        Label labelProjectName = new Label("Project Name");
        labelProjectName.getStyleClass().add("h5");
        VBox vboxName = new VBox(labelProjectName, field_project_name);

        Label labelDueDate = new Label("Due Date");
        labelDueDate.getStyleClass().add("h5");
        VBox vboxDueDate = new VBox(labelDueDate, field_due_date);


        hbox.getChildren().addAll(vboxName, vboxDueDate);
        return hbox;
    }

    @Override
    public VBox teamStatus() {
        VBox teamStatus = new VBox();
        HBox field = new HBox();
        teamStatus.getStyleClass().add("card");

        Label subTitleLabel = new Label("Add Team Member:");
        subTitleLabel.getStyleClass().add("h4");

        usernameField.setPromptText("Enter Username");

        Button addMemberBtn = new Button();
        addMemberBtn.getStyleClass().add("btn-icon");
        Image image = new Image(getClass().getResourceAsStream("/icons/user-plus-solid-240.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        addMemberBtn.setGraphic(imageView);
        addMemberBtn.setOnAction(event -> addNewTeamMember());

        field.getChildren().addAll(addMemberBtn,usernameField);

        teamStatus.getChildren().addAll(subTitleLabel, field, teamList());
        return teamStatus;
    }

    @Override
    public VBox budgetStatus(){
        VBox budgetStatus = new VBox();
        budgetStatus.getStyleClass().add("card");
        field_total_budget.setPromptText("Total Budget");
        field_total_budget.getStyleClass().add("text-field");
        HBox headHBox = new HBox(field_total_budget);

        budgetNameField.setPromptText("Budget Name");
        budgetPriceField.setPromptText("Value");

        Button addBudgetBtn = new Button();
        addBudgetBtn.getStyleClass().add("btn-icon");
        Image image = new Image(getClass().getResourceAsStream("/icons/list-plus-regular-240.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        addBudgetBtn.setGraphic(imageView);

        addBudgetBtn.setOnAction(event -> addNewBudgetHandler());

        HBox addBudget = new HBox(addBudgetBtn, budgetNameField, budgetPriceField);
        remainingBudget = new Label("Remaining Budget : " + SelfUtils.intToRupiah(SelfUtils.calculateBudget(project_budget, budgets)));
        remainingBudget.getStyleClass().add("h5-thin");

        budgetStatus.getChildren().addAll(headHBox, addBudget, budgetList(), remainingBudget);
        return budgetStatus;
    }

    @Override
    public VBox teamList(){
        teamListVBox.getChildren().clear();

        for (User user : users) {
            teamListVBox.getChildren().add(teamMember(user));
        }
        return teamListVBox;
    }

    @Override
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

    @Override
    public VBox budgetList(){
        budgetListVBox.getChildren().clear();

        for (Budget budget : budgets) {
            Label budgetLabel = new Label(budget.getBudget_name() + " : " + SelfUtils.intToRupiah(budget.getPrice()));
            budgetListVBox.getChildren().add(budgetLabel);
        }
        return budgetListVBox;
    }

    public void addNewTeamMember() {
        String username = usernameField.getText();

        if (username == null || username.isEmpty()) {
            usernameField.clear();
            usernameField.getStyleClass().add("error");
            usernameField.setPromptText("Username is required");
            usernameField.setOnKeyTyped(event -> usernameField.getStyleClass().remove("error"));
            return;
        }

        User user = UserController.findUser(username);
        if (user != null) {
            users.add(user);
            refreshTeamList();
        } else {
            usernameField.clear();
            usernameField.getStyleClass().add("error");
            usernameField.setPromptText("User Not Found");
            usernameField.setOnKeyTyped(event -> usernameField.getStyleClass().remove("error"));
        }
    }

    public void refreshTeamList() {
        teamList();
    }

    public void addNewBudgetHandler() {
        boolean isEmptyField = false;
    
        if (budgetNameField.getText().isEmpty()) {
            budgetNameField.getStyleClass().add("error");
            budgetNameField.setPromptText("Budget name is required");
            isEmptyField = true;
        }
    
        if (budgetPriceField.getText().isEmpty()) {
            budgetPriceField.getStyleClass().add("error");
            budgetPriceField.setPromptText("Budget price is required");
            isEmptyField = true;
        } else {
            try {
                Integer.parseInt(budgetPriceField.getText());
            } catch (NumberFormatException e) {
                budgetPriceField.getStyleClass().add("error");
                budgetPriceField.clear();
                budgetPriceField.setPromptText("Must be a number");
                isEmptyField = true;
            }
        }
    
        if (isEmptyField) {
            budgetNameField.setOnKeyTyped(event -> budgetNameField.getStyleClass().remove("error"));
            budgetPriceField.setOnKeyTyped(event -> budgetPriceField.getStyleClass().remove("error"));
            return;
        }
        Budget newBudget = new Budget(0, 0, budgetNameField.getText(), Integer.parseInt(budgetPriceField.getText()));
        budgets.add(newBudget);
        refreshBudgetList();
    
        budgetNameField.setText("");
        budgetPriceField.setText("");
        budgetNameField.clear();
        budgetPriceField.clear();
    } 

    public void refreshBudgetList() {
        budgetList();
        try {
            project_budget = Integer.parseInt(field_total_budget.getText());
            int updatedRemainingBudget = SelfUtils.calculateBudget(project_budget, budgets);
            remainingBudget.setText("Remaining Budget : " + SelfUtils.intToRupiah(updatedRemainingBudget));
        } catch (NumberFormatException e) {

        }
    }

    private void saveBtnHandler() {
        boolean isEmptyField = false;
        boolean isWrongDataType = false;
    
        if (field_project_name.getText().isEmpty()) {
            field_project_name.getStyleClass().add("error");
            field_project_name.setPromptText("Project name is required");
            isEmptyField = true;
        }
    
        if (field_due_date.getValue() == null || field_due_date.getEditor() == null) {
            field_due_date.getStyleClass().add("error");
            field_due_date.getEditor().clear();
            field_due_date.setPromptText("Due date is required");
            isEmptyField = true;
        }
    
        if (field_total_budget.getText().isEmpty()) {
            field_total_budget.getStyleClass().add("error");
            field_total_budget.setPromptText("Budget is required");
            isEmptyField = true;
        } else {
            try {
                Integer.parseInt(field_total_budget.getText());
            } catch (NumberFormatException e) {
                field_total_budget.getStyleClass().add("error");
                field_total_budget.clear();
                field_total_budget.setPromptText("Must be a number");
                isEmptyField = true;
            }
        }
    
        if (isEmptyField || isWrongDataType) {
            field_project_name.setOnKeyTyped(event -> field_project_name.getStyleClass().remove("error"));
            field_total_budget.setOnKeyTyped(event -> field_total_budget.getStyleClass().remove("error"));
            return;
        }
    
        project_name = field_project_name.getText();
        due_date = field_due_date.getValue().format(formatter);
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
            ComponentSidebar.btnDashboard.fire();
        } else {
            field_project_name.getStyleClass().add("error");
            field_due_date.getStyleClass().add("error");
            field_total_budget.getStyleClass().add("error");
            field_project_name.clear();
            field_due_date.setValue(null);
            field_project_name.setPromptText("Add Project Failed");
            field_due_date.setPromptText("Add Project Failed");
            field_total_budget.clear();
            field_total_budget.setPromptText("Add Project Failed");
        }
    }    
    
    private void clearFields() {
        budgets.clear();
        users.clear();
        field_project_name.clear();
        field_due_date.getEditor().clear();
        field_total_budget.clear();
        budgetNameField.clear();
        budgetPriceField.clear();
        usernameField.clear();
        field_due_date.setValue(null);
        teamListVBox.getChildren().clear();
        budgetListVBox.getChildren().clear();
        remainingBudget.setText("Remaining Budget : " + SelfUtils.intToRupiah(0));
    }
}