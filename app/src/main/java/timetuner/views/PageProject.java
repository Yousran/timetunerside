package timetuner.views;


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
import timetuner.SelfUtils;
import timetuner.controllers.BudgetController;
import timetuner.controllers.TeamMemberController;
import timetuner.controllers.UserController;
import timetuner.models.Budget;
import timetuner.models.Project;
import timetuner.models.User;

public class PageProject extends VBox implements InterfacePageProject{
    Project project;
    VBox budgetListVBox = new VBox();
    VBox teamListVBox = new VBox();
    Label remainingBudget;
    TextField usernameField = new TextField();
    TextField budgetNameField = new TextField();
    TextField budgetPriceField = new TextField();

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

    @Override
    public HBox projectStatus() {
        HBox hbox = new HBox(10);
        hbox.getStyleClass().add("card");
        hbox.getChildren().addAll(
            createPropertySection("Project Name", project.getProject_name()),
            createPropertySection("Due Date", project.getDue_date()),
            createPropertySection("Time Left", SelfUtils.calculateTimeLeft(project.getDue_date()))
        );
        return hbox;
    }

    @Override
    public VBox teamStatus() {
        VBox teamStatus = new VBox();
        teamStatus.getStyleClass().add("card");

        Label subTitleLabel = new Label("Team Members:");
        subTitleLabel.getStyleClass().add("h4");

        usernameField.setPromptText("Enter Username");
        usernameField.getStyleClass().add("text-field");

        Button addMemberBtn = new Button();
        addMemberBtn.getStyleClass().add("btn-icon");
        Image image = new Image(getClass().getResourceAsStream("/icons/user-plus-solid-240.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        addMemberBtn.setGraphic(imageView);
        addMemberBtn.setOnAction(event -> addNewTeamMember());

        HBox field = new HBox(addMemberBtn, usernameField);
        teamStatus.getChildren().addAll(subTitleLabel, field, teamList());
        return teamStatus;
    }

    @Override
    public VBox budgetStatus() {
        VBox budgetStatus = new VBox();
        budgetStatus.getStyleClass().add("card");

        Label totalBudget = new Label("Budget : " + SelfUtils.intToRupiah(project.getBudget()));
        totalBudget.getStyleClass().add("h5");
        HBox headHBox = new HBox(totalBudget);

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
        remainingBudget = new Label("Remaining Budget : " + SelfUtils.intToRupiah(SelfUtils.calculateBudget(project.getBudget(), BudgetController.getBudgets(project.getId()))));

        budgetStatus.getChildren().addAll(headHBox, addBudget, budgetList(), remainingBudget);
        return budgetStatus;
    }

    @Override
    public VBox teamList() {
        teamListVBox.getChildren().clear();
        List<User> users = TeamMemberController.getUsers(project.getId());
        for (User user : users) {
            teamListVBox.getChildren().add(teamMember(user));
        }
        return teamListVBox;
    }

    @Override
    public HBox teamMember(User user) {
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
    public VBox budgetList() {
        budgetListVBox.getChildren().clear();
        List<Budget> budgets = BudgetController.getBudgets(project.getId());
        for (Budget budget : budgets) {
            Label budgetLabel = new Label(budget.getBudget_name() + " : " + SelfUtils.intToRupiah(budget.getPrice()));
            budgetListVBox.getChildren().add(budgetLabel);
        }
        return budgetListVBox;
    }

    @Override
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
            TeamMemberController.addMember(project.getId(), user.getId());
            refreshTeamList();
        } else {
            usernameField.clear();
            usernameField.getStyleClass().add("error");
            usernameField.setPromptText("User Not Found");
            usernameField.setOnKeyTyped(event -> usernameField.getStyleClass().remove("error"));
        }
    }

    @Override
    public void refreshTeamList() {
        teamList();
    }

    @Override
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

        BudgetController.addBudget(project.getId(), budgetNameField.getText(), Integer.parseInt(budgetPriceField.getText()));
        refreshBudgetList();

        budgetNameField.clear();
        budgetPriceField.clear();
    }

    @Override
    public void refreshBudgetList() {
        budgetList();
        int updatedRemainingBudget = SelfUtils.calculateBudget(project.getBudget(), BudgetController.getBudgets(project.getId()));
        remainingBudget.setText("Remaining Budget : " + SelfUtils.intToRupiah(updatedRemainingBudget));
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

}
