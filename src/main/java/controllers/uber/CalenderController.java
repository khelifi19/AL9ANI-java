package controllers.uber;

import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import modeles.uber.Course;
import service.uber.CourseDAO;

public class CalenderController {

    ZonedDateTime dateFocus;
    ZonedDateTime today;

    @FXML
    private Text year;

    @FXML
    private Button btnRetour;

    @FXML
    private Text month;

    @FXML
    private FlowPane calendar;

    public void initialize() {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();

        btnRetour.setOnAction(event -> redirectToAccueil());
    }

    @FXML
    void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    private void drawCalendar() {
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        Map<Integer, List<Course>> calendarCourseMap = getCoursesOfMonth(dateFocus);

        int monthMaxDate = dateFocus.getMonth().maxLength();
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1, 0, 0, 0, 0, dateFocus.getZone()).getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight / 6) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j + 1) + (7 * i);
                if (calculatedDate > dateOffset) {
                    int currentDate = calculatedDate - dateOffset;
                    if (currentDate <= monthMaxDate) {
                        Text date = new Text(String.valueOf(currentDate));
                        double textTranslationY = -(rectangleHeight / 2) * 0.75;
                        date.setTranslateY(textTranslationY);
                        stackPane.getChildren().add(date);

                        List<Course> courses = calendarCourseMap.get(currentDate);
                        if (courses != null) {
                            createCalendarCourses(courses, rectangleHeight, rectangleWidth, stackPane);
                        }
                    }
                    if (today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate) {
                        rectangle.setStroke(Color.BLUE);
                    }
                }
                calendar.getChildren().add(stackPane);
            }
        }
    }

    private void createCalendarCourses(List<Course> courses, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        int maxCoursesToShow = 2; // Nombre maximum de cours à afficher avant de montrer "..."
        int columnColorIndex = 0; // Index pour sélectionner la couleur de la colonne

        for (int k = 0; k < courses.size(); k++) {
            if (k >= maxCoursesToShow) {
                // Si le nombre de cours dépasse le maximum autorisé, afficher "..."
                Text moreCourses = new Text("...");
                stackPane.getChildren().add(moreCourses);
                moreCourses.setOnMouseClicked(mouseEvent -> {
                    // On clique sur "..." affichez tous les cours pour la date donnée
                    System.out.println(courses);
                });
                break;
            }

            Course course = courses.get(k);
            Text text = new Text("voiture: " + course.getVoiture().getMatricule());

            // Colorer la colonne en fonction de l'index de la colonne
            text.setFill(getColumnColor(columnColorIndex));
            stackPane.getChildren().add(text);

            // Incrémenter l'index de la couleur pour la prochaine colonne
            columnColorIndex = (columnColorIndex + 1) % COLUMN_COLORS.length;

            text.setOnMouseClicked(mouseEvent -> {
                // Lorsque le texte est cliqué, affichez les détails du cours
                System.out.println(course);
            });
        }
    }

    // Définir les couleurs pour les colonnes
    private final Color[] COLUMN_COLORS = {Color.RED, Color.GREEN, Color.BLUE, Color.ORANGE, Color.PURPLE};

    // Méthode pour obtenir la couleur de la colonne en fonction de l'index
    private Color getColumnColor(int index) {
        return COLUMN_COLORS[index % COLUMN_COLORS.length];
    }

    private Map<Integer, List<Course>> getCoursesOfMonth(ZonedDateTime dateFocus) {
        CourseDAO courseDAO = new CourseDAO();
        List<Course> courses = courseDAO.findCurrentCoursesForMonth(dateFocus); // Récupérer les cours réservés pour le mois en cours
        return createCourseMap(courses);
    }

    private Map<Integer, List<Course>> createCourseMap(List<Course> courses) {
        Map<Integer, List<Course>> courseMap = new HashMap<>();

        for (Course course : courses) {
            int day = course.getDate().getDayOfMonth();
            courseMap.computeIfAbsent(day, k -> new ArrayList<>()).add(course);
        }
        return courseMap;
    }

    private void redirectToAccueil() {
        System.out.println("Redirection vers la page d'accueil...");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/uber/dash/course/course.fxml"));
            Parent root = loader.load();
            CourseController accueilController = loader.getController();
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
