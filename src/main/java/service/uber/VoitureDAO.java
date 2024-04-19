package service;

import modeles.Chauffeur;
import modeles.Voiture;
import utils.DBConnection;
import view.IVoiture;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoitureDAO implements IVoiture {

    private final Connection connection;

    public VoitureDAO() {
        this.connection = DBConnection.getInstance().getCnx();
    }

    @Override
    public Voiture findById(int id) {
        Voiture voiture = null;
        String query = "SELECT * FROM voiture WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                voiture = mapResultSetToVoiture(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voiture;
    }

    @Override
    public List<Voiture> findAll() {
        List<Voiture> voitures = new ArrayList<>();
        String query = "SELECT * FROM voiture";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Voiture voiture = mapResultSetToVoiture(resultSet);
                voitures.add(voiture);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voitures;
    }

    @Override
    public void save(Voiture voiture) {
        // Vérifier si la matricule est unique
        if (!isMatriculeUnique(voiture.getMatricule())) {
            System.out.println("La matricule '" + voiture.getMatricule() + "' existe déjà dans la base de données.");
            return;
        }

        String query = "INSERT INTO voiture (matricule, modele, disponibilite, chauffeur_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, voiture.getMatricule());
            statement.setString(2, voiture.getModele());
            statement.setBoolean(3, voiture.isDisponibilite());
            statement.setInt(4, voiture.getChauffeur().getId());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                voiture.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Voiture voiture) {
        // Vérifier si la matricule est unique, en excluant la voiture actuelle
        if (!isMatriculeUniqueExceptCurrent(voiture.getMatricule(), voiture.getId())) {
            System.out.println("La matricule '" + voiture.getMatricule() + "' existe déjà dans la base de données.");
            return;
        }

        String query = "UPDATE voiture SET matricule = ?, modele = ?, disponibilite = ?, chauffeur_id = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, voiture.getMatricule());
            statement.setString(2, voiture.getModele());
            statement.setBoolean(3, voiture.isDisponibilite());
            statement.setInt(4, voiture.getChauffeur().getId());
            statement.setInt(5, voiture.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour vérifier si la matricule est unique, en excluant la voiture actuelle
    private boolean isMatriculeUniqueExceptCurrent(String matricule, int currentId) {
        String query = "SELECT COUNT(*) AS count FROM voiture WHERE matricule = ? AND id != ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, matricule);
            statement.setInt(2, currentId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void delete(int id) {
        String query = "DELETE FROM voiture WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Voiture getAvailableVehicle(String typeVehicule) {
        Voiture voiture = null;
        String query = "SELECT * FROM voiture WHERE disponibilite = true AND modele = ? LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, typeVehicule);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                voiture = mapResultSetToVoiture(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voiture;
    }

    // Méthode pour mapper un ResultSet à un objet Voiture
    private Voiture mapResultSetToVoiture(ResultSet resultSet) throws SQLException {
        Voiture voiture = new Voiture();
        voiture.setId(resultSet.getInt("id"));
        voiture.setMatricule(resultSet.getString("matricule"));
        voiture.setModele(resultSet.getString("modele"));
        voiture.setDisponibilite(resultSet.getBoolean("disponibilite"));
        int chauffeurId = resultSet.getInt("chauffeur_id");
        if (chauffeurId != 0) {
            ChauffeurDAO chauffeurDAO = new ChauffeurDAO();
            Chauffeur chauffeur = chauffeurDAO.findById(chauffeurId);
            voiture.setChauffeur(chauffeur);
        }
        return voiture;
    }

    private boolean isMatriculeUnique(String matricule) {
        String query = "SELECT COUNT(*) AS count FROM voiture WHERE matricule = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, matricule);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
