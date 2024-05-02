package controller.user;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.sql.SQLException;
import java.util.Optional;

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


