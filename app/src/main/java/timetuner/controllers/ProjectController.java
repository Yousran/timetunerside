package timetuner.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import timetuner.App;
import timetuner.config.DbConnect;
import timetuner.models.Project;

public class ProjectController extends DbConnect{

    public static Project addProject(String project_name, String due_date, int budget, String team_name) {
        query = "INSERT INTO project (project_name, due_date, budget, team_name) VALUES (?, ?, ?, ?)";
        ResultSet rs = null;
        Project newProject = null;
        try {
            getConnection();
            preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, project_name);
            preparedStatement.setString(2, due_date);
            preparedStatement.setInt(3, budget);
            preparedStatement.setString(4, team_name);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                rs = preparedStatement.getGeneratedKeys();
                if (rs.next()) {
                    int projectId = rs.getInt(1);
                    newProject = new Project(projectId, project_name, due_date, budget, team_name);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return newProject;
    }   

    public static Project getProject(int project_id){
        String query = "SELECT * FROM project WHERE id = ?";
        try {
            getConnection();
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, project_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String project_name = resultSet.getString("project_name");
                    String due_date = resultSet.getString("due_date");
                    int budget = resultSet.getInt("budget");
                    String team_name = resultSet.getString("team_name");
                    return new Project(id, project_name, due_date, budget, team_name);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Project> getAllProjects() {
        if (App.loggedUser == null) {
            return new ArrayList<>();
        }

        int user_id = App.loggedUser.getId();
        List<Project> projects = new ArrayList<>();
        String query = "SELECT project_id FROM team_members WHERE user_id = ?";

        try {
            getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
             
            preparedStatement.setInt(1, user_id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int project_id = resultSet.getInt("project_id");
                Project project = getProject(project_id);
                if (project != null) {
                    projects.add(project);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }
}
