package controllers.uber;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import modeles.uber.Chauffeur;
import modeles.uber.Voiture;
import service.uber.ChauffeurDAO;
import service.uber.VoitureDAO;


import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VoitureController {

    @FXML
    private ToggleButton btnCourse;
    @FXML
    private ToggleButton btnChauffeur;


    @FXML
    private ToggleButton btnVehicule;

    @FXML
    private Button ajouterButton;

    @FXML
    private TableColumn<Voiture, Void> tActions;
    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private TableColumn<Voiture, String> tDisponibilite;
    @FXML
    private Button btnRetour;

    @FXML
    private TableColumn<Voiture, String> tMatricule;

    @FXML
    private TableColumn<Voiture, String> tChauffeur;

    @FXML
    private TableColumn<Voiture, String> tModele;
    @FXML
    private TableView<Voiture> tableViewVoitures;

    @FXML
    private TextField search;

    private VoitureDAO voitureDAO;
    private ObservableList<Voiture> voituresList = FXCollections.observableArrayList();

    private ChauffeurDAO chauffeurDAO;
    private ObservableList<Chauffeur> chauffeursList = FXCollections.observableArrayList();

    public VoitureController() {
        this.voitureDAO = new VoitureDAO();
        this.chauffeurDAO = new ChauffeurDAO();
    }

    private void setupActionButtonsCellFactory() {
        // Configure les actions des boutons dans la colonne "Actions"
        tActions.setCellFactory(param -> new TableCell<>() {
            private final Button modifierButton = new Button();
            private final Button supprimerButton = new Button();
            private final ImageView modifierIcon = new javafx.scene.image.ImageView(new Image(getClass().getResourceAsStream("/dash/Img/update.png")));
            private final ImageView supprimerIcon = new javafx.scene.image.ImageView(new Image(getClass().getResourceAsStream("/dash/Img/supprimer.png")));

            {
                modifierIcon.setFitWidth(49);
                modifierIcon.setFitHeight(25);
                modifierButton.setGraphic(modifierIcon);
                supprimerIcon.setFitWidth(49);
                supprimerIcon.setFitHeight(25);
                supprimerButton.setGraphic(supprimerIcon);

                modifierButton.setOnAction(event -> {
                    Voiture voiture = getTableView().getItems().get(getIndex());
                    afficherPopupModifierVoiture(voiture);
                });

                supprimerButton.setOnAction(event -> {
                    Voiture voiture = getTableView().getItems().get(getIndex());
                    boolean confirmDelete = showAlert("Confirmation", "Êtes-vous sûr de vouloir supprimer cette voiture ?");
                    if (confirmDelete) {
                        voitureDAO.delete(voiture.getId());
                        refreshTableView();
                        System.out.println("Voiture supprimée : " + voiture);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(modifierButton, supprimerButton);
                    hbox.setSpacing(5); // Ajoutez un espacement entre les boutons si nécessaire
                    setGraphic(hbox);
                }
            }
        });
    }


    @FXML
    public void initialize() {
        // Initialise la TableView et configure les actions des boutons
        setupTableView();

        // Rafraîchit la TableView avec les données actuelles
        refreshTableView();

        // Configurations supplémentaires
        configureButtons();

        recherche();

        comboBox.setItems(FXCollections.observableArrayList(
                "Tous", "Voiture", "Bus", "Disponible", "Non disponible"
        ));
        comboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Appeler la méthode de filtrage en fonction de la nouvelle valeur sélectionnée dans la ComboBox
                filtrerVoitures(comboBox.getValue());
            }
        });


        btnCourse.setOnAction(event -> redirectToCourse());
        btnChauffeur.setOnAction(event -> redirectToChauffeur());
        btnRetour.setOnAction(event -> redirectToAccueil());
    }
    private void setupTableView() {
        // Initialise la TableView avec les données de la base de données
        initializeTableView();

        // Configure la colonne "Chauffeur" avec une usine de cellules personnalisée
        setupChauffeurColumn();

        // Configure les actions des boutons dans la colonne "Actions"
        setupActionButtonsCellFactory();
    }


    private boolean showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }
    public class DisponibiliteValueFactory implements Callback<TableColumn.CellDataFeatures<Voiture, String>, ObservableValue<String>> {
        @Override
        public ObservableValue<String> call(TableColumn.CellDataFeatures<Voiture, String> param) {
            Voiture voiture = param.getValue();
            String disponibilite = voiture.isDisponibilite() ? "Disponible" : "Non disponible";
            return new SimpleStringProperty(disponibilite);
        }
    }

    private void initializeTableView() {
        // Initialise la TableView avec les données de la base de données
        List<Voiture> allVoitures = voitureDAO.findAll();
        voituresList.addAll(allVoitures);
        tableViewVoitures.setItems(voituresList);

        // Configure les autres colonnes de la TableView
        tMatricule.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        tModele.setCellValueFactory(new PropertyValueFactory<>("modele"));
        tDisponibilite.setCellValueFactory(new DisponibiliteValueFactory());
    }



    private void refreshTableView() {
        // Rafraîchit la TableView avec les données actuelles de la base de données
        voituresList.clear();
        voituresList.addAll(voitureDAO.findAll());
        tableViewVoitures.setItems(voituresList);
    }

    private void configureButtons() {
        // Configure les actions du bouton "Ajouter"
        ajouterButton.setOnAction(event -> afficherPopupAjoutVoiture());
    }

    private void setupChauffeurColumn() {
        // Configure la colonne "Chauffeur" avec une usine de cellules personnalisée
        tChauffeur.setCellValueFactory(cellData -> {
            Voiture voiture = cellData.getValue();
            Chauffeur chauffeur = voiture.getChauffeur();
            return new SimpleStringProperty(chauffeur != null ? chauffeur.getNom() + " " + chauffeur.getPrenom() : "Non attribué");
        });
    }

    private void afficherPopupModifierVoiture(Voiture voiture) {
        // Créer une boîte de dialogue pour modifier les données de la voiture
        Dialog<Voiture> dialog = new Dialog<>();
        dialog.setTitle("Modifier Voiture");

        // Récupérer la liste de tous les chauffeurs depuis la base de données
        List<Chauffeur> chauffeurs = chauffeurDAO.findAll();

        // Create the ComboBox for selecting the driver
        ComboBox<String> chauffeurComboBox = new ComboBox<>();

        // Créer une liste de noms complets des chauffeurs
        List<String> nomPrenomChauffeurs = chauffeurs.stream()
                .map(chauffeur -> chauffeur.getNom() + " " + chauffeur.getPrenom())
                .collect(Collectors.toList());

        // Ajouter les noms complets des chauffeurs au ComboBox
        chauffeurComboBox.getItems().addAll(nomPrenomChauffeurs);

        // Récupérer le nom complet du chauffeur associé à la voiture
        String nomPrenomChauffeur = voiture.getChauffeur().getNom() + " " + voiture.getChauffeur().getPrenom();
        // Définir la valeur sélectionnée du ComboBox comme le nom complet du chauffeur associé à la voiture
        chauffeurComboBox.setValue(nomPrenomChauffeur);

        // Créer les champs de saisie pour les autres données de la voiture
        TextField tfMatricule = new TextField(voiture.getMatricule());
        ComboBox<String> modeleComboBox = new ComboBox<>();
        modeleComboBox.getItems().addAll("Voiture", "Bus");
        modeleComboBox.setValue(voiture.getModele());
        CheckBox cbDisponibilite = new CheckBox("Disponible");
        cbDisponibilite.setSelected(voiture.isDisponibilite());

        // Ajouter les champs à la boîte de dialogue
        dialog.getDialogPane().setContent(new VBox(10, new Label("Matricule:"), tfMatricule,
                new Label("Modèle:"), modeleComboBox, new Label("Chauffeur:"), chauffeurComboBox, cbDisponibilite));

        // Ajouter les boutons OK et Annuler
        ButtonType btnTypeOK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnTypeCancel = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(btnTypeOK, btnTypeCancel);

        // Résultat de la boîte de dialogue
        dialog.setResultConverter(buttonType -> {
            if (buttonType == btnTypeOK) {
                // Vérifier si les champs obligatoires sont vides
                if (tfMatricule.getText().isEmpty() || modeleComboBox.getValue() == null) {
                    showError("Erreur", "Veuillez remplir tous les champs obligatoires.");
                    return null;
                }
                // Vérifier le format de la matricule
                String matricule = tfMatricule.getText();
                if (!matricule.matches("\\d+ TU \\d+")) {
                    showError("Erreur", "Le format de la matricule est incorrect.");
                    return null;
                }
                // Récupérer le nom complet du chauffeur sélectionné dans le ComboBox
                String nomPrenomChauffeurM = chauffeurComboBox.getValue();

                // Rechercher l'objet Chauffeur correspondant dans la liste des chauffeurs
                Chauffeur selectedChauffeur = chauffeurs.stream()
                        .filter(chauffeur -> (chauffeur.getNom() + " " + chauffeur.getPrenom()).equals(nomPrenomChauffeurM))
                        .findFirst()
                        .orElse(null);

                // Vérifier si un chauffeur correspondant a été trouvé
                if (selectedChauffeur != null) {
                    // Créer et retourner un nouvel objet Voiture avec les données saisies
                    return new Voiture(voiture.getId(), matricule, modeleComboBox.getValue(), cbDisponibilite.isSelected(), selectedChauffeur);
                } else {
                    // Afficher un message d'erreur ou prendre une autre action appropriée
                    showError("Erreur", "Chauffeur non trouvé.");
                    return null;
                }
            }
            return null;
        });

        // Afficher la boîte de dialogue et attendre la saisie de l'utilisateur
        Optional<Voiture> result = dialog.showAndWait();
        result.ifPresent(voitureModifiee -> {
            // Mettre à jour la voiture dans la base de données
            voitureDAO.update(voitureModifiee);
            // Rafraîchir la TableView
            refreshTableView();
        });
    }


    private void afficherPopupAjoutVoiture() {
        // Créer une nouvelle fenêtre de dialogue (popup)
        Dialog<Voiture> dialog = new Dialog<>();
        dialog.setTitle("Ajouter une voiture");

        // Récupérer la liste de tous les chauffeurs depuis la base de données
        List<Chauffeur> chauffeurs = chauffeurDAO.findAll();

        // Créer le ComboBox pour sélectionner le chauffeur
        ComboBox<String> chauffeurComboBox = new ComboBox<>();

// Créer une liste de noms complets des chauffeurs
        List<String> nomPrenomChauffeurs = chauffeurs.stream()
                .map(chauffeur -> chauffeur.getNom() + " " + chauffeur.getPrenom())
                .collect(Collectors.toList());

// Ajouter les noms complets des chauffeurs au ComboBox
        chauffeurComboBox.getItems().addAll(nomPrenomChauffeurs);

        // Configurer les boutons de la fenêtre de dialogue
        ButtonType ajouterButtonType = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ajouterButtonType, ButtonType.CANCEL);

        // Créer les champs pour les détails de la voiture dans la fenêtre de dialogue
        TextField matriculeField = new TextField();
        ComboBox<String> modeleComboBox = new ComboBox<>();
        modeleComboBox.getItems().addAll("Voiture", "Bus");
        CheckBox disponibiliteCheckBox = new CheckBox("Disponible");

        // Ajouter les champs à la fenêtre de dialogue
        dialog.getDialogPane().setContent(new VBox(8, new Label("Matricule:"), matriculeField,
                new Label("Modèle:"), modeleComboBox, new Label("Chauffeur:"), chauffeurComboBox, disponibiliteCheckBox));

        // Définir le résultat lorsque le bouton "Ajouter" est cliqué
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ajouterButtonType) {
                // Vérifier si les champs obligatoires sont vides
                if (matriculeField.getText().isEmpty() || modeleComboBox.getValue() == null || chauffeurComboBox.getValue() == null) {
                    showError("Erreurs de saisie", "Veuillez remplir tous les champs obligatoires.");
                    return null; // Retourner null pour garder la fenêtre de dialogue ouverte
                }
                // Vérifier le format de la matricule
                String matricule = matriculeField.getText();
                if (!matricule.matches("\\d+ TU \\d+")) {
                    showError("Erreurs de saisie", "Le format de la matricule est incorrect.");
                    return null; // Retourner null pour garder la fenêtre de dialogue ouverte
                }
                // Récupérer le nom complet du chauffeur sélectionné dans le ComboBox
                String nomPrenomChauffeur = chauffeurComboBox.getValue();

// Rechercher l'objet Chauffeur correspondant dans la liste des chauffeurs
                Chauffeur selectedChauffeur = chauffeurs.stream()
                        .filter(chauffeur -> (chauffeur.getNom() + " " + chauffeur.getPrenom()).equals(nomPrenomChauffeur))
                        .findFirst()
                        .orElse(null);

                // Vérifier si un chauffeur correspondant a été trouvé
                if (selectedChauffeur != null) {
                    // Créer et retourner un nouvel objet Voiture avec les données saisies
                    return new Voiture(0, matricule, modeleComboBox.getValue(), disponibiliteCheckBox.isSelected(), selectedChauffeur);
                } else {
                    // Afficher un message d'erreur ou prendre une autre action appropriée
                    showError("Erreur", "Chauffeur non trouvé.");
                    return null;
                }
            }
            return null; // Retourner null pour garder la fenêtre de dialogue ouverte si le bouton "Annuler" est cliqué
        });

        // Afficher la fenêtre de dialogue et attendre la réponse de l'utilisateur
        Optional<Voiture> result = dialog.showAndWait();

        // Traiter la réponse de l'utilisateur si elle est présente
        result.ifPresent(voiture -> {
            // Ajouter la voiture à la base de données ou effectuer d'autres opérations nécessaires
            voitureDAO.save(voiture);
          refreshTableView();
            System.out.println("Voiture ajoutée : " + voiture);
        });
    }

    private void recherche() {
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrerVoitureParMatricule(newValue);
        });
    }
    private void refrechRecherche() {
        initializeTableView();
        search.setText("");
    }

    private void filtrerVoitureParMatricule(String matriculeRecherche) {
        ObservableList<Voiture> voituresFiltres = FXCollections.observableArrayList();
        if (matriculeRecherche.isEmpty()) {
            // Afficher toutes les voitures si le champ de recherche est vide
            tableViewVoitures.setItems(voituresList);
        } else {
            // Filtrer les voitures par matricule
            for (Voiture voiture : voituresList) {
                if (voiture.getMatricule().toLowerCase().contains(matriculeRecherche.toLowerCase())) {
                    voituresFiltres.add(voiture);
                }
            }
            tableViewVoitures.setItems(voituresFiltres);


        }
        setupActionButtonsCellFactory();
    }

    private void filtrerVoitures(String filter) {



        ObservableList<Voiture> filteredList = FXCollections.observableArrayList();

        for (Voiture voiture : voituresList) {
            boolean matchAvailability = filter.equals("Tous") ||
                    (filter.equals("Disponible") && voiture.isDisponibilite()) ||
                    (filter.equals("Non disponible") && !voiture.isDisponibilite());

            boolean matchType =filter.equals("Tous") ||
                    (filter.equals("Voiture") && voiture.getModele().equals("Voiture")) ||
                    (filter.equals("Bus") && voiture.getModele().equals("Bus"));

            if (matchAvailability || matchType) {
                filteredList.add(voiture);
            }
        }

        tableViewVoitures.setItems(filteredList);
        setupActionButtonsCellFactory();
    }


    private void redirectToChauffeur() {
        System.out.println("Redirection vers l accueil...");

        try {
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("/uber/dash/chauffeur/chauffeur.fxml"));
            Parent root = loader.load();
           ChauffeurController chauffeurController = loader.getController();
            if (chauffeurController == null) {
                System.out.println("Erreur: Impossible de charger le contrôleur de la page de course.");
                return;
            }

            Scene scene = new Scene(root);
            Stage stage;
            if ( btnChauffeur != null) {
                stage = (Stage)  btnChauffeur.getScene().getWindow();
            }
            else {
                System.out.println("Erreur: Impossible de récupérer la scène actuelle.");
                return;
            }

            stage.setScene(scene);
            stage.show();
            System.out.println("Redirection réussie !");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la redirection: " + e.getMessage());
        }
    }


    private void redirectToCourse() {
        System.out.println("Redirection vers l accueil...");

        try {
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("/uber/dash/course/course.fxml"));

            Parent root = loader.load();
           CourseController  courseController = loader.getController();
            if (courseController == null) {
                System.out.println("Erreur: Impossible de charger le contrôleur de la page de course.");
                return;
            }

            Scene scene = new Scene(root);
            Stage stage;
            if ( btnCourse != null) {
                stage = (Stage)  btnCourse.getScene().getWindow();
            }
            else {
                System.out.println("Erreur: Impossible de récupérer la scène actuelle.");
                return;
            }

            stage.setScene(scene);
            stage.show();
            System.out.println("Redirection réussie !");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la redirection: " + e.getMessage());
        }
    }

    private void redirectToAccueil() {
        System.out.println("Redirection vers la page d'accueil...");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/uber/dash/admin.fxml"));
            Parent root = loader.load();
            AdminController accueilController = loader.getController();
            if (accueilController == null) {
                System.out.println("Erreur: Impossible de charger le contrôleur de la page d'accueil.");
                return;
            }

            Scene scene = new Scene(root);
            Stage stage;
            if (btnRetour != null) {
                stage = (Stage) btnRetour.getScene().getWindow();
            } else {
                System.out.println("Erreur: Impossible de récupérer la scène actuelle.");
                return;
            }

            stage.setScene(scene);
            stage.show();
            System.out.println("Redirection réussie !");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la redirection: " + e.getMessage());
        }
    }


}



