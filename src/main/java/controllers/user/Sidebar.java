package controllers.user;

import controllers.reclamation.ReclamationController;
import controllers.reclamation.ReponseController;
import controllers.reclamation.ReponsebackController;
import controllers.uber.AdminController;
import controllers.uber.HomeUber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Sidebar implements Initializable {
    @FXML
    private Button Bact;

    @FXML
    private Button Betab;

    @FXML
    private ImageView Bevent;

    @FXML
    private Button HomePage;

    @FXML
    private Button reclaB;

    @FXML
    private Button uberB;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    @FXML
    private void redirectToO(ActionEvent event) throws IOException {
        Button clickedButton = (Button) event.getSource();
        String buttonId = clickedButton.getId();
        System.out.println(buttonId);
        switch (buttonId) {
            case "uberB" :

                FXMLLoader loa = new FXMLLoader(getClass().getResource("/uber/front/Course/homeUber.fxml"));
                Parent ro = null;
                try {
                    ro = loa.load();
                    HomeUber dashboardController = loa.getController();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Stage ps = new Stage();
                ps.setScene(new Scene(ro));

                ps.initStyle(StageStyle.UNDECORATED);



                ps.show();
                ((Node) (event.getSource())).getScene().getWindow().hide();
                break;


            case "Betab":

                break;
            case "Bevent":
                // Redirection vers la page des événements
                // Exemple : goToEventPage();
                break;
            case "reclaB":


                FXMLLoader loader = new FXMLLoader(getClass().getResource("/reclamation/reponse.fxml"));
                Parent root = loader.load();
                ReponseController reclamationController = loader.getController();

                Scene scene = new Scene(root);
                Stage stage;
                stage = (Stage) reclaB.getScene().getWindow();
                stage.setScene(scene);
                stage.show();

                break;
            case "Bact":




                break;
            case "HomePage":
                FXMLLoader l = new FXMLLoader(getClass().getResource("/user/HomePage.fxml"));
                Parent r = l.load();
                HomeController HomeController = l.getController();

                Scene s = new Scene(r);
                Stage st;
                st = (Stage) HomePage.getScene().getWindow();
                st.setScene(s);
                st.show();



                break;
            default :
                FXMLLoader la = new FXMLLoader(getClass().getResource("/user/HomePage.fxml"));
                Parent ra = la.load();
                ReponsebackController home = la.getController();

                Scene sa = new Scene(ra);
                Stage sta;
                sta = (Stage) HomePage.getScene().getWindow();
                sta.setScene(sa);
                sta.show();
                break;
        }
    }
}
