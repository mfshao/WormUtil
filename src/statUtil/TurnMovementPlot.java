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

import edu.princeton.cs.algs4.StdDraw;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Shape;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private static final String FILE_PATH = "\\\\MEDIXSRV\\Nematodes\\data\\N2_nf4\\data";
//    private static final String FILE_PATH = "D:\\WTData";
    private static final String TRACKER_CSV_LOG = FILE_PATH + "\\tracker.csv";
    private static final String MF_CSV_LOG = FILE_PATH + "\\movementFeatures.csv";
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
        File XYChart = new File(FILE_PATH + "\\TurnMovementPlotJFree.png");
        ChartUtilities.saveChartAsPNG(XYChart, chart, width, height);
    }
//
//    public static void main(String[] args) {
//        try {
//            TurnMovementPlot tmPlot = new TurnMovementPlot("TurnMovementPlot");
//            tmPlot.pack();
//            RefineryUtilities.centerFrameOnScreen(tmPlot);
//            tmPlot.setVisible(true);
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
//

    public static void main(String[] args) {
        try {
            Data data = CSVData.getCSVData(MF_CSV_LOG);
            StdDraw.setCanvasSize((int) Math.round(data.maxX - data.minX), (int) Math.round(data.maxY - data.minY));
            StdDraw.setXscale(data.minX - 50, data.maxX + 50);
            StdDraw.setYscale(data.minY - 50, data.maxY + 50);
            StdDraw.setPenRadius(0.005);
            int i = 0;
            Double meanSpd = (data.maxSpd + data.minSpd) / 2;
            Double normalizedMeanSpd = (meanSpd - data.minSpd) * (255.0 / (data.maxSpd - data.minSpd));
            for (Double[] csvRow : data.csvData) {
                if (i == data.csvData.size() - 1) {
                    break;
                }

                Double[] nextRow = data.csvData.get(i + 1);
                Double normalizedSpd = (csvRow[4] - data.minSpd) * (255.0 / (data.maxSpd - data.minSpd));
                if (normalizedSpd < 0.0) {
                    normalizedSpd = 0.0;
                }
                if (normalizedSpd > 255.0) {
                    normalizedSpd = 255.0;
                }

                if (normalizedSpd < normalizedMeanSpd) {
                    StdDraw.setPenColor((int) Math.round(normalizedSpd), 255, 0);
                } else {
                    StdDraw.setPenColor(255, 255 - (int) Math.round(normalizedSpd), 0);
                }

                StdDraw.line(csvRow[0], csvRow[1], nextRow[0], nextRow[1]);
//                StdDraw.point(csvRow[0], csvRow[1]);
                System.out.println(i);
                i++;
            }
            StdDraw.save("C:\\Users\\MSHAO1\\Desktop\\TurnMovementPlotStdDraw.png");
            System.out.println("Finished!");
        } catch (IOException ex) {
            Logger.getLogger(CameraMovementPlot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
