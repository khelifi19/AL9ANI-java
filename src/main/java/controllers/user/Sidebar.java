package controllers.user;

import controllers.evenement.EvenementControllerFront;
import controllers.evenement.EvenementControllerUser;
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
import service.user.UserService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Sidebar implements Initializable {
    @FXML
    private Button Bact;

    @FXML
    private Button Betab;

    @FXML
    private Button Bevent;

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
                System.out.println(UserService.getCurrentlyLoggedInUser().getRole());
                if(UserService.getCurrentlyLoggedInUser().getRole().equals("GERANT")){
                    System.out.println(UserService.getCurrentlyLoggedInUser().getRole());
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenement/EvenementFront.fxml"));
                    Parent root = loader.load();
                    EvenementControllerFront evenementControllerUser = loader.getController();

                    Scene scene = new Scene(root);
                    Stage sa= new Stage();

                    sa.setScene(scene);
                    sa.show();
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                }else{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenement/EvenementUser.fxml"));
                Parent root = loader.load();
                EvenementControllerUser evenementControllerUser = loader.getController();

                Scene scene = new Scene(root);
                Stage sa= new Stage();

                sa.setScene(scene);
                sa.show();
                ((Node) (event.getSource())).getScene().getWindow().hide();
                break;}
                break;

            case "reclaB":


                FXMLLoader lr = new FXMLLoader(getClass().getResource("/reclamation/reponse.fxml"));
                Parent r = lr.load();
                ReponseController reclamationController = lr.getController();

                Scene sc = new Scene(r);
                Stage stage;
                stage = (Stage) reclaB.getScene().getWindow();
                stage.setScene(sc);
                stage.show();

                break;
            case "Bact":




                break;
            case "HomePage":
                FXMLLoader l = new FXMLLoader(getClass().getResource("/user/HomePage.fxml"));
                Parent rx = l.load();
                HomeController HomeController = l.getController();

                Scene s = new Scene(rx);
                Stage st;
                st = (Stage) HomePage.getScene().getWindow();
                st.setScene(s);
                st.show();



                break;
            default :
                FXMLLoader la = new FXMLLoader(getClass().getResource("/user/HomePage.fxml"));
                Parent ra = la.load();
                ReponsebackController home = la.getController();

                Scene sal = new Scene(ra);
                Stage sta;
                sta = (Stage) HomePage.getScene().getWindow();
                sta.setScene(sal);
                sta.show();
                break;
        }
    }
}
