package service.etaAct;


import modeles.etaAct.*;
import utils.etatAct.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostService {

    private static PostService instance;
    PreparedStatement preparedStatement;
    Connection connection;

    public PostService() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public static PostService getInstance() {
        if (instance == null) {
            instance = new PostService();
        }
        return instance;
    }

    public List<Post> getAll() {
        List<Post> listPost = new ArrayList<>();
        try {

            preparedStatement = connection.prepareStatement("SELECT * FROM `post`");


            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Post post = new Post();
                post.setId(resultSet.getInt("id"));
                post.setTitre(resultSet.getString("titre"));
                post.setDescription(resultSet.getString("description"));
                post.setLocalisation(resultSet.getString("localisation"));
                post.setDate(resultSet.getDate("datepost") != null ? resultSet.getDate("datepost").toLocalDate() : null);
                post.setImage(resultSet.getString("image"));


                listPost.add(post);
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) post : " + exception.getMessage());
        }
        return listPost;
    }


    public boolean add(Post post) {


        String request = "INSERT INTO `post`(`titre`, `description`, `localisation`, `datepost`, `image`) VALUES(?, ?, ?, ?, ?)";

        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setString(1, post.getTitre());
            preparedStatement.setString(2, post.getDescription());
            preparedStatement.setString(3, post.getLocalisation());
            preparedStatement.setDate(4, Date.valueOf(post.getDate()));
            preparedStatement.setString(5, post.getImage());


            preparedStatement.executeUpdate();
            System.out.println("Post added");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (add) post : " + exception.getMessage());
        }
        return false;
    }

    public boolean edit(Post post) {

        String request = "UPDATE `post` SET `titre` = ?, `description` = ?, `localisation` = ?, `datepost` = ?, `image` = ? WHERE `id` = ?";

        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setString(1, post.getTitre());
            preparedStatement.setString(2, post.getDescription());
            preparedStatement.setString(3, post.getLocalisation());
            preparedStatement.setDate(4, Date.valueOf(post.getDate()));
            preparedStatement.setString(5, post.getImage());


            preparedStatement.setInt(6, post.getId());

            preparedStatement.executeUpdate();
            System.out.println("Post edited");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (edit) post : " + exception.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM `post` WHERE `id`=?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("Post deleted");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (delete) post : " + exception.getMessage());
        }
        return false;
    }
}
