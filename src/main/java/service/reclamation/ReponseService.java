package service.reclamation;


import modeles.reclamation.*;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReponseService implements IReponse<Reponse> {

    Connection cnx = DBConnection.getInstance().getCnx();

    @Override
    public void ajouter(Reponse reponse) {
        try {
            String req = "INSERT INTO reponse(reclamation_id,text ) VALUES (?,?) ";
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(1, reponse.getReclamationId());
            pst.setString(2, reponse.getText());

            pst.executeUpdate();
            System.out.println(" Reponse  ajoutée !");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());

        }

    }

    @Override
    public void supprimer(Reponse reponse) {
        try {
            String req = "DELETE FROM reponse WHERE id =?";
            PreparedStatement pst =  cnx.prepareStatement(req);
            pst.setInt(1, reponse.getId());
            pst.executeUpdate();
            System.out.println("reposne suprimée !");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void modifier(Reponse reponse) {

        try{
            String req = "UPDATE reponse SET reclamation_id = ?, text = ? WHERE id = ?";
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(1, reponse.getReclamationId());
            pst.setString(2, reponse.getText());
            pst.setInt(3, reponse.getId());

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Reponse mise à jour !");
            } else {
                System.out.println("Aucune réclamation correspondante trouvée pour la mise à jour.");
            }
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public List<Reponse> afficher() {
        List<Reponse> list = new ArrayList<>();
        try {
            String req = "SELECT * FROM reponse";
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                Reponse r = new Reponse();
                r.setId(rs.getInt("id"));
                r.setReclamationId(rs.getInt("reclamation_id"));
                r.setText(rs.getString("text"));
                list.add(r);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return list;
    }

    public List<ReponseDto> afficherJointure() {
        List<ReponseDto> list = new ArrayList<>();
        try {
            String req = "SELECT rs.objet , rsp.text , rsp.id from reclamation as rs , reponse as rsp WHERE rs.id = rsp.reclamation_id";
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                ReponseDto r = new ReponseDto();
                r.setId(rs.getInt("id"));
                r.setObjet(rs.getString("objet"));
                r.setTextrespons(rs.getString("text"));
                list.add(r);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return list;
    }
}
