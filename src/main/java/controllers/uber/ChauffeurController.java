package controllers.uber;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import modeles.uber.Chauffeur;
import service.uber.ChauffeurDAO;

import java.util.List;
import java.util.Optional;

public class ChauffeurController {

    @FXML
    private Button ajouterButton;

    @FXML
    private ToggleButton btnChauffeur;

    @FXML
    private ToggleButton btnCourse;

    @FXML
    private ToggleButton btnVehicule;

    @FXML
    private TextField search;

    @FXML
    private TableColumn<Chauffeur, Void> tActions;

    @FXML
    private TableColumn<Chauffeur, Integer> tAge;

    @FXML
    private TableColumn<Chauffeur, String> tNom;

    @FXML
    private TableColumn<Chauffeur, String> tPrenom;

    @FXML
    private TableColumn<Chauffeur, Double> tSalaire;

    @FXML
    private TableColumn<Chauffeur, String> tVille;

    @FXML
    private TableColumn<Chauffeur, String> tNumero;

    @FXML
    private TableView<Chauffeur> tableViewChauffeurs;

    private ChauffeurDAO chauffeurDAO;
    private ObservableList<Chauffeur> chauffeursList = FXCollections.observableArrayList();

    public ChauffeurController() {
        this.chauffeurDAO = new ChauffeurDAO();
    }

    @FXML
    public void initialize() {
        this.chauffeursList = FXCollections.observableArrayList(chauffeurDAO.findAll());
        initializeTableView();
        rafraichirTableView();
        recherche();

        ajouterButton.setOnAction(event -> {
            afficherPopupAjoutChauffeur();
        });
    }

    private void initializeTableView() {
        tNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        tPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        tVille.setCellValueFactory(new PropertyValueFactory<>("ville"));
        tSalaire.setCellValueFactory(new PropertyValueFactory<>("salaire"));
        tAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        tNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));

        setupActionButtonsCellFactory();
    }

    private void setupActionButtonsCellFactory() {
        tActions.setCellFactory(new Callback<TableColumn<Chauffeur, Void>, TableCell<Chauffeur, Void>>() {
            @Override
            public TableCell<Chauffeur, Void> call(TableColumn<Chauffeur, Void> param) {
                return new TableCell<Chauffeur, Void>() {
                    private final Button modifierButton = new Button("Modifier");
                    private final Button supprimerButton = new Button("Supprimer");

                    {
                        modifierButton.setOnAction(event -> {
                            Chauffeur chauffeur = getTableView().getItems().get(getIndex());
                        afficherPopupModificationChauffeur(chauffeur);
                        });

                        supprimerButton.setOnAction(event -> {
                            Chauffeur chauffeur = getTableView().getItems().get(getIndex());
                            boolean confirmDelete = showAlert("Confirmation", "Êtes-vous sûr de vouloir supprimer ce chauffeur ?");
                            if (confirmDelete) {
                                chauffeurDAO.delete(chauffeur.getId());
                                rafraichirTableView();
                                System.out.println("Chauffeur supprimé : " + chauffeur);
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox buttonsContainer = new HBox(modifierButton, supprimerButton);
                            setGraphic(buttonsContainer);
                        }
                    }
                };
            }
        });
    }

    private boolean showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void recherche() {
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrerChauffeurParNomPrenom(newValue);
        });
    }

    private void filtrerChauffeurParNomPrenom(String nomPrenomRecherche) {
        ObservableList<Chauffeur> chauffeursFiltres = FXCollections.observableArrayList();
        if (nomPrenomRecherche.isEmpty()) {
            tableViewChauffeurs.setItems(chauffeursList);
        } else {
            for (Chauffeur chauffeur : chauffeursList) {
                if (chauffeur.getNom().toLowerCase().contains(nomPrenomRecherche.toLowerCase()) ||
                        chauffeur.getPrenom().toLowerCase().contains(nomPrenomRecherche.toLowerCase())) {
                    chauffeursFiltres.add(chauffeur);
                }
            }
            tableViewChauffeurs.setItems(chauffeursFiltres);
        }
    }

    private void rafraichirTableView() {
        List<Chauffeur> chauffeursList = chauffeurDAO.findAll();
        ObservableList<Chauffeur> chauffeurs = FXCollections.observableArrayList(chauffeursList);
        tableViewChauffeurs.setItems(chauffeurs);
    }

    private void afficherPopupAjoutChauffeur() {
        // Créer une nouvelle fenêtre de dialogue (popup)
        Dialog<Chauffeur> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un chauffeur");

        // Configurer les boutons de la fenêtre de dialogue
        ButtonType ajouterButtonType = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ajouterButtonType, ButtonType.CANCEL);

        // Créer les champs pour les détails du chauffeur dans la fenêtre de dialogue
        TextField nomField = new TextField();
        TextField prenomField = new TextField();
        TextField ageField = new TextField(); // Champ pour l'âge
        ComboBox<String> villeComboBox = new ComboBox<>();
        villeComboBox.getItems().addAll("Tunis", "Sousse", "Sfax", "Djerba", "Nabeul", "Monastir"); // Exemple de villes tunisiennes
        TextField numeroField = new TextField();
        numeroField.setPromptText("Doit commencer par 9, 2 ou 5 et être composé de 8 chiffres");
        TextField salaireField = new TextField();
        salaireField.setPromptText("Salaire");

        // Ajouter les champs à la fenêtre de dialogue
        dialog.getDialogPane().setContent(new VBox(8,
                new Label("Nom:"), nomField,
                new Label("Prénom:"), prenomField,
                new Label("Âge:"), ageField,
                new Label("Ville:"), villeComboBox,
                new Label("Numéro:"), numeroField,
                new Label("Salaire:"), salaireField));

        // Définir le résultat lorsque le bouton "Ajouter" est cliqué
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ajouterButtonType) {
                // Vérifier si tous les champs sont remplis
                if (nomField.getText().isEmpty() || prenomField.getText().isEmpty() ||
                        ageField.getText().isEmpty() || villeComboBox.getValue() == null ||
                        numeroField.getText().isEmpty() || salaireField.getText().isEmpty()) {
                    showError("Erreurs de saisie", "Veuillez remplir tous les champs.");
                    return null; // Retourner null pour garder la fenêtre de dialogue ouverte
                }

                // Vérifier le format de l'âge
                int age;
                try {
                    age = Integer.parseInt(ageField.getText());
                } catch (NumberFormatException e) {
                    showError("Erreurs de saisie", "L'âge doit être un nombre entier.");
                    return null; // Retourner null pour garder la fenêtre de dialogue ouverte
                }

                // Vérifier le format du numéro de téléphone
                String numero = numeroField.getText();
                if (!numero.matches("[925]\\d{7}")) {
                    showError("Erreurs de saisie", "Le numéro de téléphone est invalide.");
                    return null; // Retourner null pour garder la fenêtre de dialogue ouverte
                }

                // Si tous les champs sont valides, créer un nouvel objet Chauffeur et le retourner
                return new Chauffeur(0, nomField.getText(), prenomField.getText(), age,
                        villeComboBox.getValue(), numeroField.getText(), Double.parseDouble(salaireField.getText()));
            }
            return null; // Retourner null si le bouton "Annuler" est cliqué
        });

        // Afficher la fenêtre de dialogue et attendre la réponse de l'utilisateur
        Optional<Chauffeur> result = dialog.showAndWait();

        // Traiter la réponse de l'utilisateur si elle est présente
        result.ifPresent(chauffeur -> {
            // Ajouter le chauffeur à la base de données ou effectuer d'autres opérations nécessaires
            chauffeurDAO.save(chauffeur);
            rafraichirTableView();
            System.out.println("Chauffeur ajouté : " + chauffeur);
        });
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }

    private void afficherPopupModificationChauffeur(Chauffeur chauffeur) {
        // Créer une nouvelle fenêtre de dialogue (popup)
        Dialog<Chauffeur> dialog = new Dialog<>();
        dialog.setTitle("Modifier un chauffeur");

        // Configurer les boutons de la fenêtre de dialogue
        ButtonType modifierButtonType = new ButtonType("Modifier", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(modifierButtonType, ButtonType.CANCEL);

        // Créer les champs pour les détails du chauffeur dans la fenêtre de dialogue
        TextField nomField = new TextField(chauffeur.getNom());
        TextField prenomField = new TextField(chauffeur.getPrenom());
        ComboBox<String> villeComboBox = new ComboBox<>();
        villeComboBox.getItems().addAll("Tunis", "Sousse", "Sfax", "Djerba", "Nabeul", "Monastir"); // Exemple de villes tunisiennes
        villeComboBox.setValue(chauffeur.getVille());
        TextField numeroField = new TextField(chauffeur.getNumero());
        TextField salaireField = new TextField(String.valueOf(chauffeur.getSalaire()));
        TextField ageField = new TextField(String.valueOf(chauffeur.getAge()));

        // Ajouter les champs à la fenêtre de dialogue
        dialog.getDialogPane().setContent(new VBox(8,
                new Label("Nom:"), nomField,
                new Label("Prénom:"), prenomField,
                new Label("Ville:"), villeComboBox,
                new Label("Numéro:"), numeroField,
                new Label("Salaire:"), salaireField,
                new Label("Âge:"), ageField));

        // Définir le résultat lorsque le bouton "Modifier" est cliqué
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == modifierButtonType) {
                // Vérifier si tous les champs sont remplis
                if (nomField.getText().isEmpty() || prenomField.getText().isEmpty() ||
                        villeComboBox.getValue() == null || numeroField.getText().isEmpty() ||
                        salaireField.getText().isEmpty() || ageField.getText().isEmpty()) {
                    showError("Erreurs de saisie", "Veuillez remplir tous les champs.");
                    return null; // Retourner null pour garder la fenêtre de dialogue ouverte
                }

                // Vérifier le format du numéro de téléphone
                String numero = numeroField.getText();
                if (!numero.matches("[925]\\d{7}")) {
                    showError("Erreurs de saisie", "Le numéro de téléphone est invalide.");
                    return null; // Retourner null pour garder la fenêtre de dialogue ouverte
                }

                // Si tous les champs sont valides, créer un nouvel objet Chauffeur et le retourner
                return new Chauffeur(chauffeur.getId(), nomField.getText(), prenomField.getText(),
                        Integer.parseInt(ageField.getText()), villeComboBox.getValue(), numeroField.getText(), Double.parseDouble(salaireField.getText()));
            }
            return null; // Retourner null si le bouton "Annuler" est cliqué
        });

        // Afficher la fenêtre de dialogue et attendre la réponse de l'utilisateur
        Optional<Chauffeur> result = dialog.showAndWait();

        // Traiter la réponse de l'utilisateur si elle est présente
        result.ifPresent(modifiedChauffeur -> {
            // Mettre à jour le chauffeur dans la base de données ou effectuer d'autres opérations nécessaires
            chauffeurDAO.update(modifiedChauffeur);
            rafraichirTableView();
            System.out.println("Chauffeur modifié : " + modifiedChauffeur);
        });
    }

}