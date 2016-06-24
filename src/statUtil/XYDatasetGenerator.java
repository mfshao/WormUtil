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

import java.util.ArrayList;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Travis Shao
 */
public class XYDatasetGenerator {

    public static XYDataset generateXYDataset(ArrayList<Integer[]> csvData) {
        final XYSeries centroidXY = new XYSeries("centroidXY", false);
        final XYSeries cameraXY = new XYSeries("cameraXY", false);
        for (Integer[] csvRow : csvData) {
            centroidXY.add(csvRow[0], csvRow[1]);
            if (csvRow[2] == 1) {
                cameraXY.add(csvRow[0], csvRow[1]);
            }
        }
        final XYSeriesCollection xyData = new XYSeriesCollection();
        xyData.addSeries(centroidXY);
        xyData.addSeries(cameraXY);
        return xyData;
    }
}
