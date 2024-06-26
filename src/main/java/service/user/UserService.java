package service.user;

import javafx.scene.control.Alert;
import modeles.user.UserModel;
import utils.DBConnection;
import utils.user.DataUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class UserService implements IService <UserModel>{
    private final Connection connection;

    public static UserModel getCurrentlyLoggedInUser() {
        return currentlyLoggedInUser;
    }

    public static void setCurrentlyLoggedInUser(UserModel currentlyLoggedInUser) {
        UserService.currentlyLoggedInUser = currentlyLoggedInUser;
    }

    public static UserModel currentlyLoggedInUser = null;

    private Preferences prefs = Preferences.userNodeForPackage(UserService.class);
    public void rememberUser(String username, String password) {
        prefs.put("username", username);
        prefs.put("password", password);
    }
    public void clearRememberedUser() {
        prefs.remove("username");
        prefs.remove("password");
    }
    public String autoLogin() {
        String username = prefs.get("username", null);
       // Assuming you store a hashed token instead of a plain password.

        if (username != null ) {
            try {
                UserModel user = readUser(username);

                // Here checkPassword should compare the hashed token with the hashed password stored in the database.
                if (user != null ) {
  UserService.setCurrentlyLoggedInUser(user);

                    return  user.getUsername();
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Login Error", "An error occurred while trying to auto-login: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }


    public UserModel readUser(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UserModel u=   new UserModel(
                            rs.getInt("id_user"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("firstName"),
                            rs.getString("lastName"),
                            rs.getString("password")




                    ); u.setImg(rs.getString("img"));
                    u.setRole(rs.getString("role"));
                    return u;
                }
            }
        }
        return null;
    }
    public UserModel readUserE(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                  UserModel u=   new UserModel(
                            rs.getInt("id_user"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("firstName"),
                            rs.getString("lastName"),
                            rs.getString("password")




                    ); u.setImg(rs.getString("img"));
                    u.setRole(rs.getString("role"));
                    return u;
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
        String query = "INSERT INTO users (username, email, firstName, lastName, password,role,img) VALUES (?, ?, ?, ?, ?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userModel.getUsername());
            userModel.setPassword(PasswordHasher.hashPassword(userModel.getPassword()));
            statement.setString(5, userModel.getPassword());
            statement.setString(2, userModel.getEmail());
            statement.setString(3, userModel.getFirstName());
            statement.setString(4, userModel.getLastName());
            statement.setString(6, userModel.getRole());
            statement.setString(7, userModel.getImg());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // If any rows were affected, the user was created successfully
        } catch (SQLException e) {
            System.err.println("Error while creating user: " + e.getMessage());
            return false;
        }
    }
    public boolean checkIfUsernameExists(String username) {
        String query = "SELECT username FROM users WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();


            return !resultSet.next();

        } catch (SQLException e) {
            System.err.println("Error while checking if username exists: " + e.getMessage());
            return false;
        }
    }
    public boolean checkIfEmailExists(String email) {
        String query = "SELECT email FROM users WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();


            return resultSet.next();

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
    public void resetPassword(String newPassword)throws SQLException{
            String hashedPassword = PasswordHasher.hashPassword(newPassword);
            String sql = "UPDATE users SET password = ? WHERE id_user = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, hashedPassword);
                ps.setInt(2, currentlyLoggedInUser.getuserId());
                ps.executeUpdate();
            }
    }

    public UserService() {

            this.connection = DBConnection.getInstance().getCnx();
            System.out.println("Connexion réussie!");




    }

    @Override
    public void update(UserModel userModel) throws SQLException {
        String sql = "update users set firstname = ?, lastname = ?, password = ?, email = ?, username = ? ,role = ?, img = ? where id_user= ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, userModel.getFirstName());
        ps.setString(2, userModel.getLastName());
        ps.setString(3, userModel.getPassword());
        ps.setString(4, userModel.getEmail());
        ps.setString(5, userModel.getUsername());
        ps.setString(6, userModel.getRole());
        ps.setString(7, userModel.getImg());
        ps.setInt(8, userModel.getuserId());
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
