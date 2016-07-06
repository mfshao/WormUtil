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
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.ui.ApplicationFrame;
import statUtil.CSVData.Data;

/**
 *
 * @author Travis Shao
 */
public class CameraMovementPlot extends ApplicationFrame {

    private static final String FILE_PATH = "E:\\DePaul\\RA\\Celegans\\AIB_data\\AIB_nf1\\data";
    private static final String TRACKER_CSV_LOG = FILE_PATH + "\\tracker.csv";
//    private final String centroidCSVLoc = filePath + "\\centroid.csv";
    private static final String MF_CSV_LOG = FILE_PATH + "\\movementFeatures.csv";

    public CameraMovementPlot(String title) throws IOException {
        super(title);
//        Data data = CSVData.getCSVData(trackerCSVLoc, mfCSVLoc);
//        JFreeChart chart = ChartFactory.createXYLineChart(
//                title,
//                "X",
//                "Y",
//                XYDatasetGenerator.generateXYDataset(data.csvData));
//        final XYPlot xyPlot = chart.getXYPlot();
//        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
//        renderer.setSeriesLinesVisible(0, true);
//        renderer.setSeriesShapesVisible(0, false);
//        renderer.setSeriesLinesVisible(1, false);
//        renderer.setSeriesShapesVisible(1, true);
//        Shape cross = ShapeUtilities.createDiagonalCross(0.3f, 0.1f);
//        renderer.setSeriesShape(1, cross);
//        xyPlot.setRenderer(renderer);
//        xyPlot.setQuadrantOrigin(new Point(0, 0));
//        
//        int width = data.maxX - data.minX + 50;
//        int height = data.maxY - data.minY + 50;
//        ChartPanel chartPanel = new ChartPanel(chart);
//        chartPanel.setPreferredSize(new Dimension(width, height));
//        setContentPane(chartPanel);
//        File XYChart = new File("CameraMovementPlot.jpeg");
//        ChartUtilities.saveChartAsJPEG(XYChart, chart, width, height);
    }

    public static void main(String[] args) {
        try {
            //        try {
//            CameraMovementPlot cmPlot = new CameraMovementPlot("CameraMovementPlot");
//            cmPlot.pack();
//            RefineryUtilities.centerFrameOnScreen(cmPlot);
//            cmPlot.setVisible(true);
            Data data = CSVData.getCSVData(TRACKER_CSV_LOG, MF_CSV_LOG);
            StdDraw.setCanvasSize((int) Math.round((data.maxX - data.minX)), (int) Math.round(data.maxY - data.minY));
            StdDraw.setXscale(data.minX, data.maxX);
            StdDraw.setYscale(data.minY, data.maxY);
            StdDraw.setPenRadius(0.005);
            int i = 0;
            for (Double[] csvRow : data.csvData) {
                Double normalizedSpd = (csvRow[3] - data.minSpd) * (255.0 / (data.maxSpd - data.minSpd));
                if (normalizedSpd < 0.0) {
                    normalizedSpd = 0.0;
                }
                if (normalizedSpd > 255.0) {
                    normalizedSpd = 255.0;
                }
                StdDraw.setPenColor((int) Math.round(normalizedSpd), 0, 255 - (int) Math.round(normalizedSpd));
                StdDraw.point(csvRow[0], csvRow[1]);
//                StdDraw.point(1.0, 1.0);
                System.out.println(i);
                i++;
            }

//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
        } catch (IOException ex) {
            Logger.getLogger(CameraMovementPlot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
