package timetuner.views;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import timetuner.models.User;

public interface InterfacePageProject {
    HBox projectStatus();
    VBox teamStatus();
    VBox budgetStatus();
    VBox teamList();
    HBox teamMember(User user);
    VBox budgetList();
    void addNewTeamMember();
    void refreshTeamList();
    void addNewBudgetHandler();
    void refreshBudgetList();
}
