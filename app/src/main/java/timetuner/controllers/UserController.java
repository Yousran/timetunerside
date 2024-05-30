package timetuner.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import timetuner.App;
import timetuner.config.DbConnect;
import timetuner.models.User;

public class UserController extends DbConnect{

    public static boolean register(String username, String email, String password, String profil_path){
        String query = "INSERT INTO user (username, email, password, profil_path) VALUES (?, ?, ?, ?) ";

        try {
            getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, profil_path);

            preparedStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println(e);
        }

        return false;
    }
    
    public static boolean login(String email, String password) {
        if (App.loggedUser != null) {
            return false;
        }
        String query = "SELECT id FROM user WHERE email=? AND password=?";
        try {
            getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int userId = resultSet.getInt("id");
                App.loggedUser = getUser(userId);
                resultSet.close();
                preparedStatement.close();
                return true;
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean cekEmail(String email){
        query = "SELECT email FROM user WHERE email=?";
        try {
            getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                boolean valid = resultSet.next();
                return valid;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static User getUser(int id) {
        String query = "SELECT * FROM user WHERE id=?";
        try {
            getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String profile_path = resultSet.getString("profil_path");
                User user = new User(id, username, email, password, profile_path);

                resultSet.close();
                preparedStatement.close();
                return user;
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User findUser(String username) {
        String query = "SELECT * FROM user WHERE username=?";
        try {
            getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String profile_path = resultSet.getString("profil_path");
                return new User(id, username, email, password, profile_path);
            }
        } catch (SQLException e) {
            System.err.println("Error finding user: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException ex) {
                System.err.println("Error closing resources: " + ex.getMessage());
            }
        }
        return null;
    }    
}
