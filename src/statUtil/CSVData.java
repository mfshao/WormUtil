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

import com.opencsv.CSVReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Travis Shao
 */
public class CSVData {

    static class Data {

        ArrayList<Integer[]> csvData;
        int minX;
        int maxX;
        int minY;
        int maxY;
    }

    public static Data getCSVData(String trackerCSVLoc, String centroidCSVLoc) throws IOException {
        Data data = new Data();
        data.csvData = new ArrayList<>();
        data.minX = 0;
        data.maxX = 0;
        data.minY = 0;
        data.maxY = 0;

        return getCSVData(trackerCSVLoc, centroidCSVLoc, data);
    }

    private static Data getCSVData(String trackerCSVLoc, String centroidCSVLoc, Data data) throws IOException {
        try {
            CSVReader trackerCSV = new CSVReader(new FileReader(trackerCSVLoc));
            CSVReader centroidCSV = new CSVReader(new FileReader(centroidCSVLoc));
            String[] trackerRow = null;
            String[] centroidRow = null;

            if (((trackerRow = trackerCSV.readNext()) == null) || (centroidRow = centroidCSV.readNext()) == null) {
                return data;
            }

            while (true) {
                if (Integer.parseInt(trackerRow[0]) < Integer.parseInt(centroidRow[0])) {
                    trackerRow = trackerCSV.readNext();
                } else if (Integer.parseInt(trackerRow[0]) > Integer.parseInt(centroidRow[0])) {
                    centroidRow = centroidCSV.readNext();
                } else {
                    int centroidX = (int) Math.round(Double.parseDouble(centroidRow[3]));
                    int centroidY = (int) Math.round(Double.parseDouble(centroidRow[4]));
                    boolean isMoving = Boolean.parseBoolean(trackerRow[5]);
                    int isMovingVal = (isMoving) ? 1 : 0;
                    data.csvData.add(new Integer[]{centroidX, centroidY, isMovingVal});

                    if (centroidX > data.maxX) {
                        data.maxX = centroidX;
                    } else if (centroidX < data.minX) {
                        data.minX = centroidX;
                    }

                    if (centroidY > data.maxY) {
                        data.maxY = centroidY;
                    } else if (centroidY < data.minY) {
                        data.minY = centroidY;
                    }

                    trackerRow = trackerCSV.readNext();
                    centroidRow = centroidCSV.readNext();
                }
                if (trackerRow == null || centroidRow == null) {
                    break;
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return data;
    }
}
