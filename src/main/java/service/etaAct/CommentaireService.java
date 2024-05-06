package service.etaAct;


//import com.example.al9ani.entities.User;

import modeles.user.UserModel;
import service.user.UserService;
import utils.etatAct.DatabaseConnection;
import modeles.etaAct.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentaireService {

    private static CommentaireService instance;
    PreparedStatement preparedStatement;
    Connection connection;

    public CommentaireService() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public static CommentaireService getInstance() {
        if (instance == null) {
            instance = new CommentaireService();
        }
        return instance;
    }

    public List<Commentaire> getAll() {
        List<Commentaire> listCommentaire = new ArrayList<>();
        try {

            String query = "SELECT * FROM `commentaire` AS x "
                    + "RIGHT JOIN `post` AS y1 ON x.post_id = y1.id "
                    + "RIGHT JOIN `users` AS y2 ON x.user_id = y2.id_user "
                    + "WHERE  x.post_id = y1.id  AND   x.user_id = y2.id_user ";
            preparedStatement = connection.prepareStatement(query);


            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Commentaire commentaire = new Commentaire();
                commentaire.setId(resultSet.getInt("id"));
                commentaire.setDescription(resultSet.getString("description"));
                commentaire.setDate(resultSet.getDate("datecom") != null ? resultSet.getDate("datecom").toLocalDate() : null);

                Post post = new Post();
                post.setId(resultSet.getInt("y1.id"));
                post.setTitre(resultSet.getString("y1.titre"));
                commentaire.setPost(post);
                UserModel user = UserService.getCurrentlyLoggedInUser();

                commentaire.setUser(user);

                listCommentaire.add(commentaire);
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) commentaire : " + exception.getMessage());
        }
        return listCommentaire;
    }

    public List<UserModel> getAllUsers() {
        List<UserModel> listUsers = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM `users`");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                UserModel user = new UserModel();

                user.setId(resultSet.getInt("id_user"));
                user.setFirstName(resultSet.getString("firstName"));
                listUsers.add(user);
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) users : " + exception.getMessage());
        }
        return listUsers;
    }


    public boolean add(Commentaire commentaire) {


        String request = "INSERT INTO `commentaire`(`description`, `datecom`, `post_id`, `user_id`) VALUES(?, ?, ?, ?)";

        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setString(1, commentaire.getDescription());
            preparedStatement.setDate(2, Date.valueOf(commentaire.getDate()));

            preparedStatement.setInt(3, commentaire.getPost().getId());
            preparedStatement.setInt(4, UserService.getCurrentlyLoggedInUser().getuserId());


            preparedStatement.executeUpdate();
            System.out.println("Commentaire added");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (add) commentaire : " + exception.getMessage());
        }
        return false;
    }

    public boolean edit(Commentaire commentaire) {

        String request = "UPDATE `commentaire` SET `description` = ?, `datecom` = ?, `post_id` = ?, `user_id` = ? WHERE `id` = ?";

        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setString(1, commentaire.getDescription());
            preparedStatement.setDate(2, Date.valueOf(commentaire.getDate()));

            preparedStatement.setInt(3, commentaire.getPost().getId());
            preparedStatement.setInt(4, UserService.getCurrentlyLoggedInUser().getuserId());

            preparedStatement.setInt(5, commentaire.getId());

            preparedStatement.executeUpdate();
            System.out.println("Commentaire edited");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (edit) commentaire : " + exception.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM `commentaire` WHERE `id`=?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("Commentaire deleted");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (delete) commentaire : " + exception.getMessage());
        }
        return false;
    }
}
