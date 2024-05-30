package timetuner.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import timetuner.config.DbConnect;
import timetuner.models.User;

public class TeamMemberController extends DbConnect{
    public static List<User> getUsers(int project_id) {
        List<User> users = new ArrayList<>();
        String query = "SELECT user_id FROM team_members WHERE project_id = ?";

        try {
            getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, project_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int user_id = resultSet.getInt("user_id");
                    User user = UserController.getUser(user_id);
                    if (user != null) {
                        users.add(user);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    public static boolean addMember(int project_id, int user_id) {
        String query = "INSERT INTO team_members (project_id, user_id) VALUES (?, ?)";
        try {
            getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, project_id);
            preparedStatement.setInt(2, user_id);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error adding member: " + e.getMessage());
            return false;
        }
    }
    
}
