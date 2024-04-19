package service;

import modeles.Course;
import modeles.Voiture;
import utils.DBConnection;
import view.ICourse;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO implements ICourse {

    private final Connection connection;

    public CourseDAO() {
        this.connection = DBConnection.getInstance().getCnx();
    }

    @Override
    public Course findById(int id) {
        Course course = null;
        String query = "SELECT c.*, v.matricule FROM course c JOIN voiture v ON c.voiture_id = v.id WHERE c.id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                course = new Course();
                course.setId(resultSet.getInt("id"));
                course.setDestination(resultSet.getString("destination"));
                course.setDepart(resultSet.getString("depart"));
                course.setNbPersonne(resultSet.getInt("nb_personne"));
                Timestamp timestamp = resultSet.getTimestamp("date");
                course.setDate(timestamp.toLocalDateTime());
                course.setPrix(resultSet.getDouble("prix"));

                // Création de l'objet Voiture et définition du matricule
                Voiture voiture = new Voiture();
                voiture.setMatricule(resultSet.getString("matricule"));
                course.setVoiture(voiture);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return course;
    }

    @Override
    public List<Course> findAll() {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT c.*, v.matricule FROM course c JOIN voiture v ON c.voiture_id = v.id";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Course course = new Course();
                course.setId(resultSet.getInt("id"));
                course.setDestination(resultSet.getString("destination"));
                course.setDepart(resultSet.getString("depart"));
                course.setNbPersonne(resultSet.getInt("nb_personne"));
                Timestamp timestamp = resultSet.getTimestamp("date");
                course.setDate(timestamp.toLocalDateTime());
                course.setPrix(resultSet.getDouble("prix"));

                // Création de l'objet Voiture et définition du matricule
                Voiture voiture = new Voiture();
                voiture.setMatricule(resultSet.getString("matricule"));
                course.setVoiture(voiture);

                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }


    @Override
    public void save(Course course) {
        String query = "INSERT INTO course (destination, depart, nb_personne, date, prix, voiture_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, course.getDestination());
            statement.setString(2, course.getDepart());
            statement.setInt(3, course.getNbPersonne());
            statement.setTimestamp(4, Timestamp.valueOf(course.getDate()));
            statement.setDouble(5, course.getPrix());
            statement.setInt(6, course.getVoiture().getId()); // Ajout de l'ID de la voiture
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Course course) {
        String query = "UPDATE course SET destination = ?, depart = ?, nb_personne = ?, date = ?, prix = ?, voiture_id = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, course.getDestination());
            statement.setString(2, course.getDepart());
            statement.setInt(3, course.getNbPersonne());
            statement.setTimestamp(4, Timestamp.valueOf(course.getDate()));
            statement.setDouble(5, course.getPrix());
            statement.setInt(6, course.getVoiture().getId()); // Mise à jour de l'ID de la voiture
            statement.setInt(7, course.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM course WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Course> findCurrentCourses() {
        List<Course> currentCourses = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        String query = "SELECT c.*, v.matricule FROM course c JOIN voiture v ON c.voiture_id = v.id WHERE c.date >= ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, Timestamp.valueOf(now));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Course course = new Course();
                course.setId(resultSet.getInt("id"));
                course.setDestination(resultSet.getString("destination"));
                course.setDepart(resultSet.getString("depart"));
                course.setNbPersonne(resultSet.getInt("nb_personne"));
                Timestamp timestamp = resultSet.getTimestamp("date");
                course.setDate(timestamp.toLocalDateTime());
                course.setPrix(resultSet.getDouble("prix"));

                Voiture voiture = new Voiture();
                voiture.setMatricule(resultSet.getString("matricule"));
                course.setVoiture(voiture);

                currentCourses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currentCourses;
    }





    public List<Course> findHistoricalCourses() {
        List<Course> historicalCourses = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        String query = "SELECT c.*, v.matricule FROM course c JOIN voiture v ON c.voiture_id = v.id WHERE c.date < ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, Timestamp.valueOf(now));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Course course = new Course();
                course.setId(resultSet.getInt("id"));
                course.setDestination(resultSet.getString("destination"));
                course.setDepart(resultSet.getString("depart"));
                course.setNbPersonne(resultSet.getInt("nb_personne"));
                Timestamp timestamp = resultSet.getTimestamp("date");
                course.setDate(timestamp.toLocalDateTime());
                course.setPrix(resultSet.getDouble("prix"));

                Voiture voiture = new Voiture();
                voiture.setMatricule(resultSet.getString("matricule"));
                course.setVoiture(voiture);

                historicalCourses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historicalCourses;
    }

}

