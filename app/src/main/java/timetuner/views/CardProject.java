package timetuner.views;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import timetuner.SelfUtils;
import timetuner.models.Project;
import javafx.scene.control.Label;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class CardProject extends Button {
        public CardProject(Project project) {
        this.getStyleClass().add("card-btn");

        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(
            createPropertySection("Project Name", project.getProject_name()),
            createPropertySection("Due Date", project.getDue_date()),
            createPropertySection("Time Left", calculateTimeLeft(project.getDue_date())),
            createPropertySection("Budget", SelfUtils.intToRupiah(project.getBudget()))
        );

        this.setGraphic(hbox);
        this.setOnAction(event -> BtnClickHandler(project));
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

    private void BtnClickHandler(Project project){
        System.out.println("Project " + project.getProject_name() + " selected");
        SceneMain.content.getChildren().clear();
        SceneMain.content.getChildren().add(new PageProject(project));
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
