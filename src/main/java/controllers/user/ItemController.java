package controllers.user;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ItemController {
    @FXML
    private Button delete;

    @FXML
    private Label f_name;

    @FXML
    private HBox itemC;
    @FXML
    private Label email;

    @FXML
    private Label l_name;

    @FXML
    private Label usename;

        public void setUserInformation(String usernames, String emails, String firstName, String lastName) {
            usename.setText(usernames);
            email.setText(emails);
            f_name.setText(firstName);
            l_name.setText(lastName);
        }
    }


