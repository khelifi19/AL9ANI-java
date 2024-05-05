package service.serviceEvenement;

import modeles.evenement.*;
import service.serviceEvenement.*;

import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePass implements CRUD<Pass> {

    private Connection cnx;

    public ServicePass() {
        cnx = DBConnection.getInstance().getCnx();
    }

    @Override
    public void insertOne(Pass pass) throws SQLException {
        String req = "INSERT INTO pass (id, prixPass, type, evenement_id) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setInt(1, pass.getId());
        ps.setInt(2, pass.getPrixPass());
        ps.setString(3, pass.getType());
        ps.setInt(4, pass.getEvenement().getId());

        ps.executeUpdate();
        System.out.println("Pass ajouté !");
    }


    @Override
    public void updateOne(Pass pass) throws SQLException {
        String req = "UPDATE pass SET prixPass = ?, type = ? WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setInt(1, pass.getPrixPass());
        ps.setString(2, pass.getType());
        ps.setInt(3, pass.getId());

        ps.executeUpdate();
        System.out.println("Pass modifié !");
    }

    @Override
    public void deleteOne(Pass pass) throws SQLException {
        String req = "DELETE FROM pass WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, pass.getId());

        ps.executeUpdate();
        System.out.println("Pass supprimé !");
    }

    @Override
    public List<Pass> selectAll() throws SQLException {
        List<Pass> passList = new ArrayList<>();

        String req = "SELECT p.id, p.prixPass, p.type, e.id AS event_id, e.nom AS event_nom, e.type AS event_type " +
                "FROM pass p " +
                "JOIN evenement e ON p.evenement_id = e.id";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            int id = rs.getInt("id");
            int prixPass = rs.getInt("prixPass");
            String type = rs.getString("type");

            int eventId = rs.getInt("event_id");
            String eventNom = rs.getString("event_nom");
            String eventType = rs.getString("event_type");
            Evenement evenement = new Evenement(eventId, eventNom, null, 0, null, null);

            Pass pass = new Pass(id, prixPass, type, evenement);
            passList.add(pass);
        }

        return passList;
    }

}
