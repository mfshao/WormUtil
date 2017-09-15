package statUtil;

import com.opencsv.CSVReader;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A simple demonstration of the {@link XYLineAndShapeRenderer} class.
 */
public class YCoordinatesPlot extends ApplicationFrame {

    /**
     * Constructs the demo application.
     *
     * @param title  the frame title.
     */
    public YCoordinatesPlot(final String title) {

        super(title);
        XYDataset dataset = createDataset();
        JFreeChart chart = ChartFactory.createXYLineChart(
            title,
            "Frame Number",
            "Y Coordinate Value",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            false,
            false
        );
        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        Rectangle rect = new Rectangle(2, 2);
        renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShape(0, rect);
        renderer.setSeriesLinesVisible(1, false);
        renderer.setSeriesShapesVisible(1, true);   
        renderer.setSeriesShape(1, rect);
        renderer.setSeriesLinesVisible(2, false);
        renderer.setSeriesShapesVisible(2, true);  
        renderer.setSeriesShape(2, rect);
        plot.setRenderer(renderer);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(1000, 600));
        setContentPane(chartPanel);
    }
    
    /**
     * Creates a sample dataset.
     * 
     * @return A dataset.
     */
    private XYDataset createDataset() {
        XYSeries series1 = new XYSeries("Output of WormSeg (Java)");
        XYSeries series2 = new XYSeries("Output of MATLAB");
        XYSeries series3 = new XYSeries("Original tracker log");
        
        try {
            CSVReader dataCSV = new CSVReader(new FileReader("D:\\data.csv"));
            String[] dataRow = null;
            
            for (int i = 0; i<1500; i++) {
//            while ((dataRow = dataCSV.readNext()) != null) {
                dataRow = dataCSV.readNext();
                    series1.add(Integer.parseInt(dataRow[0]), Double.parseDouble(dataRow[4]));
                    series2.add(Integer.parseInt(dataRow[0]), Double.parseDouble(dataRow[6]));
                    series3.add(Integer.parseInt(dataRow[0]), Double.parseDouble(dataRow[2]));
//            }
            }
        } catch (FileNotFoundException fnfe) {
            Logger.getLogger(YCoordinatesPlot.class.getName()).log(Level.SEVERE, null, fnfe);
        } catch (IOException ioe) {
            Logger.getLogger(YCoordinatesPlot.class.getName()).log(Level.SEVERE, null, ioe);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        dataset.addSeries(series3);
        return dataset;
    }

    public static void main(final String[] args) {

        final YCoordinatesPlot demo = new YCoordinatesPlot("Y Coordinates from Different Sources");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}
