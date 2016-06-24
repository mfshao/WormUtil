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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Shape;
import java.io.File;
import java.io.IOException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
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
public class CameraMovementPlot extends ApplicationFrame {
    
    private final String filePath = "E:\\DePaul\\RA\\Celegans\\AIB_data\\AIB_nf1\\data";
    private final String trackerCSVLoc = filePath + "\\tracker.csv";
    private final String centroidCSVLoc = filePath + "\\centroid.csv";
    
    public CameraMovementPlot(String title) throws IOException {
        super(title);
        Data data = CSVData.getCSVData(trackerCSVLoc, centroidCSVLoc);
        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                "X",
                "Y",
                XYDatasetGenerator.generateXYDataset(data.csvData));
        final XYPlot xyPlot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesLinesVisible(1, false);
        renderer.setSeriesShapesVisible(1, true);
        Shape cross = ShapeUtilities.createDiagonalCross(0.3f, 0.1f);
        renderer.setSeriesShape(1, cross);
        xyPlot.setRenderer(renderer);
        xyPlot.setQuadrantOrigin(new Point(0, 0));
        
        int width = data.maxX - data.minX + 50;
        int height = data.maxY - data.minY + 50;
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(width, height));
        setContentPane(chartPanel);
        File XYChart = new File("CameraMovementPlot.jpeg");
        ChartUtilities.saveChartAsJPEG(XYChart, chart, width, height);
    }
    
    public static void main(String[] args) {
        try {
            CameraMovementPlot cmPlot = new CameraMovementPlot("CameraMovementPlot");
            cmPlot.pack();
            RefineryUtilities.centerFrameOnScreen(cmPlot);
            cmPlot.setVisible(true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
