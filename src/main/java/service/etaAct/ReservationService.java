package service.etaAct;


import utils.etatAct.DatabaseConnection;
import modeles.etaAct.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationService {

    private static ReservationService instance;
    PreparedStatement preparedStatement;
    Connection connection;

    public ReservationService() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public static ReservationService getInstance() {
        if (instance == null) {
            instance = new ReservationService();
        }
        return instance;
    }

    public List<Reservation> getAll() {
        List<Reservation> listReservation = new ArrayList<>();
        try {

            String query = "SELECT * FROM `reservation` AS x "
                    + "RIGHT JOIN `etablissements` AS y1 ON x.etablissements_id = y1.id "
                    + "WHERE  x.etablissements_id = y1.id  ";
            preparedStatement = connection.prepareStatement(query);


            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(resultSet.getInt("id"));
                reservation.setDate(resultSet.getDate("date_reservation") != null ? resultSet.getDate("date_reservation").toLocalDate() : null);
                reservation.setNombreDePersonnes(resultSet.getInt("nombre_de_personnes"));

                Etablissement etablissement = new Etablissement();
                etablissement.setId(resultSet.getInt("y1.id"));
                etablissement.setNom(resultSet.getString("y1.nom"));
                reservation.setEtablissement(etablissement);

                listReservation.add(reservation);
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) reservation : " + exception.getMessage());
        }
        return listReservation;
    }

    public boolean add(Reservation reservation) {
        String request = "INSERT INTO `reservation`(`date_reservation`, `nombre_de_personnes`, `etablissements_id`) VALUES(?, ?, ?)";

        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setDate(1, Date.valueOf(reservation.getDate()));
            preparedStatement.setInt(2, reservation.getNombreDePersonnes());
            preparedStatement.setInt(3, reservation.getEtablissement().getId());

            preparedStatement.executeUpdate();
            System.out.println("Reservation added");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (add) reservation : " + exception.getMessage());
        }
        return false;
    }

    public boolean edit(Reservation reservation) {

        String request = "UPDATE `reservation` SET `date_reservation` = ?, `nombre_de_personnes` = ?, `etablissements_id` = ? WHERE `id` = ?";

        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setDate(1, Date.valueOf(reservation.getDate()));
            preparedStatement.setInt(2, reservation.getNombreDePersonnes());

            preparedStatement.setInt(3, reservation.getEtablissement().getId());

            preparedStatement.setInt(4, reservation.getId());

            preparedStatement.executeUpdate();
            System.out.println("Reservation edited");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (edit) reservation : " + exception.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM `reservation` WHERE `id`=?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("Reservation deleted");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (delete) reservation : " + exception.getMessage());
        }
        return false;
    }
}
