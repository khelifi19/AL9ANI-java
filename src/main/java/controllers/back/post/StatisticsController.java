package controllers.back.post;


import service.etaAct.PostService;
import modeles.etaAct.Post;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StatisticsController implements Initializable {

    @FXML
    public PieChart mostUsedLocalisationChart;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<Post> listPost = PostService.getInstance().getAll();

        List<String> localisations = new ArrayList<>();

        for (Post post : listPost) {
            if (!localisations.contains(post.getLocalisation().toLowerCase())) {
                localisations.add(post.getLocalisation());
            }
        }

        List<PieChart.Data> pieChartData = new ArrayList<>();

        for (String localisation : localisations) {
            int count = 0;
            for (Post post : listPost) {
                if (post.getLocalisation().equalsIgnoreCase(localisation)) {
                    count++;
                }
            }
            pieChartData.add(new PieChart.Data(localisation, count));
        }

        mostUsedLocalisationChart.getData().addAll(pieChartData);
    }
}
