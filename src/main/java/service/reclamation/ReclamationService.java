package service.reclamation;


import modeles.user.UserModel;
import service.user.UserService;
import utils.DBConnection;
import modeles.reclamation.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ReclamationService implements IReclamation<Reclamation> {
    Connection cnx = DBConnection.getInstance().getCnx();

    @Override
    public void ajouter(Reclamation reclamation) {
        try {
            String req = "INSERT INTO reclamation(user_id,objet,text,etat ) VALUES (?,?,?,?) ";
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(1, UserService.getCurrentlyLoggedInUser().getuserId());
            pst.setString(2, reclamation.getObjet());
            pst.setString(3, reclamation.getText());
            pst.setInt(4, reclamation.getEtat());

            pst.executeUpdate();
            System.out.println(" Reclamatio  ajoutée !");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());

        }
    }
    @Override
    public void supprimer(Reclamation reclamation) {
        try {
            String req = "DELETE FROM reclamation WHERE id =?";
            PreparedStatement pst =  cnx.prepareStatement(req);
            pst.setInt(1, reclamation.getId());
            pst.executeUpdate();
            System.out.println("reclamation suprimée !");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void modifier(Reclamation reclamation) {
        try {
            String req = "UPDATE reclamation SET user_id = ?, objet = ?, text = ?, etat = ? WHERE id = ?";
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(1, UserService.getCurrentlyLoggedInUser().getuserId());
            pst.setString(2, reclamation.getObjet());
            pst.setString(3, reclamation.getText());
            pst.setInt(4, reclamation.getEtat());
            pst.setInt(5, reclamation.getId());

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Reclamation mise à jour !");
            } else {
                System.out.println("Aucune réclamation correspondante trouvée pour la mise à jour.");
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la mise à jour de la réclamation : " + ex.getMessage());
        }
    }


    @Override
    public List<Reclamation> afficher() {
        List<Reclamation> list = new ArrayList<>();
        try {
            String req = "SELECT * FROM reclamation";
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while(rs.next()) {
                list.add(new Reclamation(rs.getInt("id"), rs.getInt("user_id"), rs.getString("objet"), rs.getString("text"), rs.getInt("etat")));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return list;

    }
    public Reclamation getReclamationById(int id) {
        Reclamation reclamation = null;
        try {
            String req = "SELECT * FROM reclamation WHERE id = ?";
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                reclamation = new Reclamation(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("objet"),
                        rs.getString("text"),
                        rs.getInt("etat")
                );
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la récupération de la réclamation : " + ex.getMessage());
        }
        return reclamation;
    }
    public List<Reclamation> triText() {

        List<Reclamation> list1 = new ArrayList<>();
        List<Reclamation> list2 = afficher();

        list1 = list2.stream().sorted(Comparator.comparing(Reclamation::getText)).collect(Collectors.toList());
        return list1;

    }
    public List<Reclamation> triObjet() {

        List<Reclamation> list1 = new ArrayList<>();
        List<Reclamation> list2 = afficher();

        list1 = list2.stream().sorted(Comparator.comparing(Reclamation::getObjet)).collect(Collectors.toList());
        return list1;

    }
    public List<Reclamation> search(String t) {
        List<Reclamation> filteredList = new ArrayList<>();
        List<Reclamation> allreclamation = afficher(); // Assuming affichage() returns a list of livraisonDTO objects

        for (Reclamation Reclamation : allreclamation) {
            if (Reclamation.getObjet().startsWith(t) ||
                    Reclamation.getText().startsWith(t)) {
                filteredList.add(Reclamation);
            }
        }

        return filteredList;
    }


}
