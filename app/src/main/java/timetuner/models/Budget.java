package timetuner.models;

public class Budget extends Model {
    private int project_id;
    private String budget_name;
    private int price;

    public Budget(int id, int project_id, String budget_name, int price) {
        super(id);
        this.project_id = project_id;
        this.budget_name = budget_name;
        this.price = price;
    }
    
    public String getBudget_name() {
        return budget_name;
    }
    public void setBudget_name(String budget_name) {
        this.budget_name = budget_name;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

}
