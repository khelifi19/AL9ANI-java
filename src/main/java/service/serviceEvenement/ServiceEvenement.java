package service.serviceEvenement;

import modeles.evenement.Evenement;
import service.serviceEvenement.CRUD;
import utils.DBConnection;

import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEvenement implements CRUD<Evenement> {

    private Connection cnx;

    public ServiceEvenement() {
        cnx = DBConnection.getInstance().getCnx();
    }

    public void insertOne(Evenement evenement) throws SQLException {
        String req = "INSERT INTO `evenement`(`id`, `nom`, `type`, `participants`, `dateDebut`, `dateFin`) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setInt(1, evenement.getId());
        ps.setString(2, evenement.getNom());
        ps.setString(3, evenement.getType());
        ps.setInt(4, evenement.getParticipants());
        ps.setDate(5, Date.valueOf(evenement.getDateDebut()));
        ps.setDate(6, Date.valueOf(evenement.getDateFin()));

        ps.executeUpdate();
        System.out.println("Evenement ajouté !");
    }

    @Override
    public void updateOne(Evenement evenement) throws SQLException {
        String req = "UPDATE `evenement` SET `nom`=?, `type`=?, `participants`=?, `dateDebut`=?, `dateFin`=? WHERE `id`=?";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, evenement.getNom());
        ps.setString(2, evenement.getType());
        ps.setInt(3, evenement.getParticipants());
        ps.setDate(4, Date.valueOf(evenement.getDateDebut()));
        ps.setDate(5, Date.valueOf(evenement.getDateFin()));
        ps.setInt(6, evenement.getId());

        ps.executeUpdate();
        System.out.println("Evenement modifié !");
    }

    @Override
    public void deleteOne(Evenement evenement) throws SQLException {
        String req = "DELETE FROM `evenement` WHERE `id`=?";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setInt(1, evenement.getId());

        ps.executeUpdate();
        System.out.println("Evenement supprimé !");
    }

    @Override
    public List<Evenement> selectAll() throws SQLException {
        List<Evenement> evenementList = new ArrayList<>();

        String req = "SELECT * FROM `evenement`";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            int id = rs.getInt("id");
            String nom = rs.getString("nom");
            String type = rs.getString("type");
            int participants = rs.getInt("participants");
            LocalDate dateDebut = rs.getDate("dateDebut").toLocalDate();
            LocalDate dateFin = rs.getDate("dateFin").toLocalDate();

            Evenement e = new Evenement(id, nom, type, participants, dateDebut, dateFin);
            evenementList.add(e);
        }

        return evenementList;
    }


}
