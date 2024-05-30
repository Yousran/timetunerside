package timetuner.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import timetuner.config.DbConnect;
import timetuner.models.Budget;

public class BudgetController extends DbConnect {
    public static List<Budget> getBudgets(int project_id) {
        List<Budget> budgets = new ArrayList<>();
        String query = "SELECT * FROM budgets WHERE project_id = ?";
        
        try {
            getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, project_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String budget_name = resultSet.getString("budget_name");
                    int price = resultSet.getInt("price");
                    budgets.add(new Budget(id, project_id, budget_name, price));
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
        
        return budgets;
    }

    public static void addBudget(int project_id, String budget_name, int price) {
        String query = "INSERT INTO budgets (project_id, budget_name, price) VALUES (?, ?, ?)";
        
        try {
            getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, project_id);
            preparedStatement.setString(2, budget_name);
            preparedStatement.setInt(3, price);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Budget added successfully.");
            } else {
                System.out.println("No rows affected.");
            }
        } catch (SQLException e) {
            System.err.println("Error inserting budget: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
