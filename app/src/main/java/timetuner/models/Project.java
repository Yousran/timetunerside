package timetuner.models;

public class Project extends Model{
    private String project_name;
    private String due_date;
    private int budget;
    private String team_name;
    public Project(int id, String project_name, String due_date, int budget, String team_name) {
        super(id);
        this.project_name = project_name;
        this.due_date = due_date;
        this.budget = budget;
        this.team_name = team_name;
    }
    public String getProject_name() {
        return project_name;
    }
    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }
    public String getDue_date() {
        return due_date;
    }
    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }
    public int getBudget() {
        return budget;
    }
    public void setBudget(int budget) {
        this.budget = budget;
    }
    public String getTeam_name() {
        return team_name;
    }
    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    

}
