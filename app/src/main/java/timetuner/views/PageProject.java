package timetuner.views;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import timetuner.controllers.BudgetController;
import timetuner.controllers.TeamMemberController;
import timetuner.controllers.UserController;
import timetuner.models.Budget;
import timetuner.models.Project;
import timetuner.models.User;

public class PageProject extends VBox {
    Project project;
    VBox budgetListVBox = new VBox();
    VBox teamListVBox = new VBox();
    Label remainingBudget;

    public PageProject(Project project){
        super();
        this.project = project;
        HBox hBox = new HBox();
        VBox vBox = new VBox(projectStatus(), budgetStatus());
        vBox.setStyle("-fx-spacing: 1em;");
        remainingBudget.getStyleClass().add("h5");

        vBox.prefWidthProperty().bind(hBox.widthProperty().multiply(0.70));
        teamStatus().prefWidthProperty().bind(hBox.widthProperty().multiply(0.30));
    
        hBox.getStyleClass().add("container");
        hBox.getChildren().addAll(vBox, teamStatus());

        Label subTitleAddNewProject = new Label("Detail Project");
        subTitleAddNewProject.getStyleClass().add("h3");

        HBox subTitle = new HBox(subTitleAddNewProject);
        subTitle.getStyleClass().add("container");

        this.getChildren().addAll(subTitle, hBox);
    }

    public VBox teamList(){
        teamListVBox.getChildren().clear();
        List<User> users = TeamMemberController.getUsers(project.getId());

        for (User user : users) {
            teamListVBox.getChildren().add(teamMember(user));
        }
        return teamListVBox;
    }

    public VBox teamStatus() {
        VBox teamStatus = new VBox();
        teamStatus.getStyleClass().add("card");

        Label subTitleLabel = new Label("Team Members:");
        subTitleLabel.getStyleClass().add("h4");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter Username");
        Button addMemberBtn = new Button("Add Member");
        addMemberBtn.setOnAction(event -> addNewTeamMember(usernameField.getText()));

        HBox field = new HBox(addMemberBtn, usernameField);
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
            TeamMemberController.addMember(project.getId(), user.getId());
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

        hbox.getChildren().addAll(
            createPropertySection("Project Name", project.getProject_name()),
            createPropertySection("Due Date", project.getDue_date()),
            createPropertySection("Time Left", calculateTimeLeft(project.getDue_date()))
        );
        return hbox;
    }
    private HBox createPropertySection(String title, String value) {
        HBox hbox = new HBox();

        VBox vbox = new VBox();
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("h5");
        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("h5-thin");

        vbox.getChildren().addAll(titleLabel, valueLabel);

        Region spacerLeft = new Region();
        HBox.setHgrow(spacerLeft, Priority.ALWAYS);
        Region spacerRight = new Region();
        HBox.setHgrow(spacerRight, Priority.ALWAYS);

        hbox.getChildren().addAll(spacerLeft, vbox, spacerRight);
        return hbox;
    }

    public VBox budgetStatus(){
        VBox budgetStatus = new VBox();
        budgetStatus.getStyleClass().add("card");

        Label totalBudget = new Label("Budget : " + project.getBudget());
        totalBudget.getStyleClass().add("h5");
        HBox headHBox = new HBox(totalBudget);

        TextField budgetNameField = new TextField();
        budgetNameField.setPromptText("Budget Name");
        TextField budgetPriceField = new TextField();
        budgetPriceField.setPromptText("Value");
        Button addBudgetBtn = new Button("+");
        addBudgetBtn.setOnAction(event -> addNewBudgetHandler(budgetNameField.getText(), Integer.parseInt(budgetPriceField.getText())));

        HBox addBudget = new HBox(addBudgetBtn, budgetNameField, budgetPriceField);
        remainingBudget = new Label("Remaining Budget : " + calculateBudget(project.getBudget(), BudgetController.getBudgets(project.getId())));

        budgetStatus.getChildren().addAll(headHBox, addBudget, budgetList(), remainingBudget);
        return budgetStatus;
    }

    private VBox budgetList(){
        budgetListVBox.getChildren().clear();
        List<Budget> budgets = BudgetController.getBudgets(project.getId());
        for (Budget budget : budgets) {
            Label budgetLabel = new Label(budget.getBudget_name() + " : " + budget.getPrice());
            budgetListVBox.getChildren().add(budgetLabel);
        }
        return budgetListVBox;
    }

    private void addNewBudgetHandler(String budget_name, int price){
        BudgetController.addBudget(project.getId(), budget_name, price);
        refreshBudgetList();
    }

    private void refreshBudgetList() {
        budgetList();
        int updatedRemainingBudget = calculateBudget(project.getBudget(), BudgetController.getBudgets(project.getId()));
        remainingBudget.setText("Remaining Budget : " + updatedRemainingBudget);
    }

    private int calculateBudget(int totalBudget, List<Budget> budgets) {
        int totalSpent = 0;
        for (Budget budget : budgets) {
            totalSpent += budget.getPrice();
        }
        return totalBudget - totalSpent;
    }

    private String calculateTimeLeft(String dueDate) {
        try {
            LocalDate endDate = LocalDate.parse(dueDate, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate today = LocalDate.now();
            long daysBetween = ChronoUnit.DAYS.between(today, endDate);
            return daysBetween + " days";
        } catch (Exception e) {
            return "Invalid date";
        }
    }
}
