package statUtil;

import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class BarChartMC extends Application {

    static double[] data = null;
    static String title;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Camera Movement Count");
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis(0.0,25.0,1.0);
//        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String, Number> bc
                = new BarChart<String, Number>(xAxis, yAxis);
        bc.setAnimated(false);
        bc.setTitle("Camera Movement Count");
        xAxis.setLabel("Number of Camera Moverments per Min");
        yAxis.setLabel("Percentage of Total Video Length %");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName(title);
            for (int i = 1; i < 13; i++) {
            series1.getData().add(new XYChart.Data(Integer.toString(i), data[i]));
        }

        Scene scene = new Scene(bc, 800, 600);
        bc.getData().addAll(series1);
        stage.setScene(scene);
        stage.show();
        WritableImage image = bc.snapshot(new SnapshotParameters(), null);
        File file = new File(title + "_MC_total1.png");

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            // TODO: handle exception here
        }
    }

    public static void setData(double[] d, String s) {
        data = d;
        title = s;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
