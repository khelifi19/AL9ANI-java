package service.uber;

import modeles.uber.Chauffeur;
import utils.DBConnection;
import view.uber.IChauffeur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChauffeurDAO implements IChauffeur {

    private final Connection connection;

    public ChauffeurDAO() {
        this.connection = DBConnection.getInstance().getCnx();
    }

    @Override
    public Chauffeur findById(int id) {
        Chauffeur chauffeur = null;
        String query = "SELECT * FROM chauffeur WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                chauffeur = new Chauffeur();
                chauffeur.setId(resultSet.getInt("id"));
                chauffeur.setNom(resultSet.getString("nom"));
                chauffeur.setPrenom(resultSet.getString("prenom"));
                chauffeur.setVille(resultSet.getString("ville"));
                chauffeur.setSalaire(resultSet.getDouble("salaire"));
                chauffeur.setAge(resultSet.getInt("age"));
                chauffeur.setNumero(resultSet.getString("numero"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chauffeur;
    }

    @Override
    public List<Chauffeur> findAll() {
        List<Chauffeur> chauffeurs = new ArrayList<>();
        String query = "SELECT * FROM chauffeur";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Chauffeur chauffeur = new Chauffeur();
                chauffeur.setId(resultSet.getInt("id"));
                chauffeur.setNom(resultSet.getString("nom"));
                chauffeur.setPrenom(resultSet.getString("prenom"));
                chauffeur.setVille(resultSet.getString("ville"));
                chauffeur.setSalaire(resultSet.getDouble("salaire"));
                chauffeur.setAge(resultSet.getInt("age"));
                chauffeur.setNumero(resultSet.getString("numero"));
                chauffeurs.add(chauffeur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chauffeurs;
    }

    @Override
    public void save(Chauffeur chauffeur) {
        String query = "INSERT INTO chauffeur (nom, prenom, ville, salaire, age, numero) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, chauffeur.getNom());
            statement.setString(2, chauffeur.getPrenom());
            statement.setString(3, chauffeur.getVille());
            statement.setDouble(4, chauffeur.getSalaire());
            statement.setInt(5, chauffeur.getAge());
            statement.setString(6, chauffeur.getNumero());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Chauffeur chauffeur) {
        String query = "UPDATE chauffeur SET nom = ?, prenom = ?, ville = ?, salaire = ?, age = ?, numero = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, chauffeur.getNom());
            statement.setString(2, chauffeur.getPrenom());
            statement.setString(3, chauffeur.getVille());
            statement.setDouble(4, chauffeur.getSalaire());
            statement.setInt(5, chauffeur.getAge());
            statement.setString(6, chauffeur.getNumero());
            statement.setInt(7, chauffeur.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM chauffeur WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

