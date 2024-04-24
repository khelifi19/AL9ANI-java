package service.user;

import javafx.scene.control.Alert;
import model.user.UserModel;
import utils.user.DataUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class UserService implements IService <UserModel>{
    private Connection connection;
    private Preferences prefs = Preferences.userNodeForPackage(UserService.class);
    public void rememberUser(String username, String password) {
        prefs.put("username", username);
        prefs.put("password", password);
    }
    public void clearRememberedUser() {
        prefs.remove("username");
        prefs.remove("password");
    }

    public UserService(Connection connection) {
        try {
            this.connection= DataUtils.getConnection();
            System.out.println("Connexion réussie!");



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public UserModel readUser(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new UserModel(
                            rs.getInt("id_user"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("firstName"),
                            rs.getString("lastName"),
                            rs.getString("password")


                    );
                }
            }
        }
        return null;
    }
    public void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @Override
    public boolean create(UserModel userModel) throws SQLException {
        String query = "INSERT INTO users (username, password, email, firstName, lastName) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userModel.getUsername());
            userModel.setPassword(PasswordHasher.hashPassword(userModel.getPassword()));
            statement.setString(2, userModel.getPassword());
            statement.setString(3, userModel.getEmail());
            statement.setString(4, userModel.getFirstName());
            statement.setString(5, userModel.getLastName());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // If any rows were affected, the user was created successfully
        } catch (SQLException e) {
            System.err.println("Error while creating user: " + e.getMessage());
            return false;
        }
    }
    public boolean checkIfUsernameExists(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            System.out.println(resultSet.next());
            return resultSet.next(); // Return true if username exists, false otherwise
        } catch (SQLException e) {
            System.err.println("Error while checking if username exists: " + e.getMessage());
            return false;
        }
    }
    public boolean checkCredentials(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next() && PasswordHasher.checkPassword(password, resultSet.getString("password"))) {
                return true;
            } else {
                return false;
            } // Return true if there's at least one matching user
        } catch (SQLException e) {
            System.err.println("Error while checking credentials: " + e.getMessage());
            return false;
        }
    }

    public UserService() {
        try {
            this.connection= DataUtils.getConnection();
            System.out.println("Connexion réussie!");



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(UserModel userModel) throws SQLException {
        String sql = "update users set firstname = ?, lastname = ?, password = ?, email = ?, username = ? where id_user= ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, userModel.getFirstName());
        ps.setString(2, userModel.getLastName());
        ps.setString(3, userModel.getPassword());
        ps.setString(4, userModel.getEmail());
        ps.setString(5, userModel.getUsername());
        ps.setInt(6, userModel.getuserId());
        ps.executeUpdate();
    }


    @Override
    public void delete(String username) throws SQLException {
        String sql = "delete from users where username = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, username);
        ps.executeUpdate();

    }

    @Override
    public List<UserModel> read() throws SQLException {
        String sql = "SELECT * FROM users";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<UserModel> users = new ArrayList<>();
        while (rs.next()) {
            UserModel user = new UserModel(
                    rs.getInt("id_user"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("password")
            );
            users.add(user);
        }
        return users;
    }

}
