package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import modeles.Course;
import modeles.Voiture;
import service.CourseDAO;

import java.time.LocalDateTime;
import java.util.Optional;

public class CourseController {

        @FXML
        private ToggleButton btnCourse;

        @FXML
        private ToggleButton btnVehicule;

        @FXML
        private TableColumn<Course, Void> tActions;

        @FXML
        private TableColumn<Course, LocalDateTime> tDate;

        @FXML
        private TableColumn<Course, String> tDepart;

        @FXML
        private TableColumn<Course, String> tDestination;

        @FXML
        private TableColumn<Course, Integer> tNbPersonne;

        @FXML
        private TableColumn<Course, Double> tPrix;

        @FXML
        private TableColumn<Course, Voiture> tVoiture;

        @FXML
        private TableView<Course> tableViewCourses;

        private CourseDAO courseDAO;

        public CourseController() {
                this.courseDAO = new CourseDAO();
        }

        @FXML
        private void initialize() {
                initializeTableView();
                setupActionButtonsCellFactory();
        }

        private void initializeTableView() {
                tDate.setCellValueFactory(new PropertyValueFactory<>("date"));
                tDepart.setCellValueFactory(new PropertyValueFactory<>("depart"));
                tDestination.setCellValueFactory(new PropertyValueFactory<>("destination"));
                tNbPersonne.setCellValueFactory(new PropertyValueFactory<>("nbPersonne"));
                tPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
                tVoiture.setCellValueFactory(new PropertyValueFactory<>("voiture"));

                tVoiture.setCellFactory(column -> {
                        return new TableCell<Course, Voiture>() {
                                @Override
                                protected void updateItem(Voiture voiture, boolean empty) {
                                        super.updateItem(voiture, empty);

                                        if (voiture == null || empty) {
                                                setText(null);
                                        } else {
                                                setText(voiture.getMatricule());
                                        }
                                }
                        };
                });



                tableViewCourses.setItems(FXCollections.observableArrayList(courseDAO.findAll()));
        }

        private void setupActionButtonsCellFactory() {
                tActions.setCellFactory(new Callback<TableColumn<Course, Void>, TableCell<Course, Void>>() {
                        @Override
                        public TableCell<Course, Void> call(TableColumn<Course, Void> param) {
                                return new TableCell<Course, Void>() {
                                        private final Button supprimerButton = new Button("Supprimer");

                                        {
                                                // Définir le comportement du bouton "Action"
                                                supprimerButton.setOnAction(event -> {
                                                        Course course = getTableView().getItems().get(getIndex());
                                                        boolean confirmDelete = showAlert("Confirmation", "Êtes-vous sûr de vouloir supprimer cette Course ?");
                                                        if (confirmDelete) {
                                                                courseDAO.delete(course.getId());
                                                                rafraichirTableView();
                                                                System.out.println("Course supprimée : " + course);
                                                        }
                                                });
                                        }

                                        @Override
                                        protected void updateItem(Void item, boolean empty) {
                                                super.updateItem(item, empty);
                                                if (empty) {
                                                        setGraphic(null);
                                                } else {
                                                        HBox buttonsContainer = new HBox(supprimerButton);
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

        private void rafraichirTableView() {
                tableViewCourses.setItems(FXCollections.observableArrayList(courseDAO.findAll()));
        }
}
