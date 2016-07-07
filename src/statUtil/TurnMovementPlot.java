/* 
 * Copyright (C) 2016 Travis Shao
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package statUtil;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Shape;
import java.io.File;
import java.io.IOException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ShapeUtilities;
import statUtil.CSVData.Data;

/**
 *
 * @author Travis Shao
 */
public class TurnMovementPlot extends ApplicationFrame {

//    private static final String FILE_PATH = "E:\\DePaul\\RA\\Celegans\\AIB_data\\AIB_nf1\\data";
    private static final String FILE_PATH = "C:\\Users\\Travis Shao\\Desktop";
//    private static final String TRACKER_CSV_LOG = FILE_PATH + "\\tracker.csv";
//    private final String centroidCSVLoc = filePath + "\\centroid.csv";
//    private static final String MF_CSV_LOG = FILE_PATH + "\\movementFeatures.csv";
    private static final String TURN_CSV_LOG = FILE_PATH + "\\n2_nf4Turns_sampled_60.csv";

    public TurnMovementPlot(String title) throws IOException {
        super(title);
        Data data = CSVData.getCSVData(TURN_CSV_LOG);
        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                "X",
                "Y",
                XYDatasetGenerator.generateXYDataset(data.csvData));
        final XYPlot xyPlot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesStroke(0, new BasicStroke(3f));
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesLinesVisible(1, false);
        renderer.setSeriesShapesVisible(1, true);
        Shape cross = ShapeUtilities.createDiagonalCross(2f, 0.5f);
        renderer.setSeriesShape(1, cross);
        xyPlot.setRenderer(renderer);
        xyPlot.setQuadrantOrigin(new Point(0, 0));

        int i = 0;
        for (Double[] csvRow : data.csvData) {
            if (i % 20 == 1) {
                final XYTextAnnotation annotation = new XYTextAnnotation(Double.toString(csvRow[3]), csvRow[0], csvRow[1]);
                annotation.setFont(new Font("SansSerif", Font.PLAIN, 10));
                xyPlot.addAnnotation(annotation);
            }
            i++;
        }

        int width = (int) Math.round(data.maxX - data.minX) + 50;
        int height = (int) Math.round(data.maxY - data.minY) + 50;
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(width, height));
        setContentPane(chartPanel);
        File XYChart = new File(FILE_PATH + "\\TurnMovementPlot.png");
        ChartUtilities.saveChartAsPNG(XYChart, chart, width, height);
    }

    public static void main(String[] args) {
//        try {
        try {
            TurnMovementPlot tmPlot = new TurnMovementPlot("TurnMovementPlot");
            tmPlot.pack();
            RefineryUtilities.centerFrameOnScreen(tmPlot);
            tmPlot.setVisible(true);
//            Data data = CSVData.getCSVData(TRACKER_CSV_LOG, MF_CSV_LOG);
//            StdDraw.setCanvasSize((int) Math.round((data.maxX - data.minX)), (int) Math.round(data.maxY - data.minY));
//            StdDraw.setXscale(data.minX, data.maxX);
//            StdDraw.setYscale(data.minY, data.maxY);
//            StdDraw.setPenRadius(0.005);
//            int i = 0;
//            for (Double[] csvRow : data.csvData) {
//                Double normalizedSpd = (csvRow[3] - data.minSpd) * (255.0 / (data.maxSpd - data.minSpd));
//                if (normalizedSpd < 0.0) {
//                    normalizedSpd = 0.0;
//                }
//                if (normalizedSpd > 255.0) {
//                    normalizedSpd = 255.0;
//                }
//                StdDraw.setPenColor((int) Math.round(normalizedSpd), 0, 255 - (int) Math.round(normalizedSpd));
//                StdDraw.point(csvRow[0], csvRow[1]);
////                StdDraw.point(1.0, 1.0);
//                System.out.println(i);
//                i++;
//            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
//        } catch (IOException ex) {
//            Logger.getLogger(CameraMovementPlot.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

}
