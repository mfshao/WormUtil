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

        ArrayList<Double[]> csvData;
        Double minX;
        Double maxX;
        Double minY;
        Double maxY;
        Double maxSpd;
        Double minSpd;
    }

    public static Data getCSVData(String trackerCSVLoc, String centroidCSVLoc) throws IOException {
        Data data = new Data();
        data.csvData = new ArrayList<>();
        data.minX = 0.0;
        data.maxX = 0.0;
        data.minY = 0.0;
        data.maxY = 0.0;
        data.maxSpd = 0.0;
        data.minSpd = 0.0;

        return getCSVData(trackerCSVLoc, centroidCSVLoc, data);
    }

    private static Data getCSVData(String trackerCSVLoc, String mfCSVLoc, Data data) throws IOException {
        try {
            CSVReader trackerCSV = new CSVReader(new FileReader(trackerCSVLoc));
            CSVReader mfCSV = new CSVReader(new FileReader(mfCSVLoc));
            String[] trackerRow = null;
            String[] mfRow = null;

            if (((trackerRow = trackerCSV.readNext()) == null) || (mfRow = mfCSV.readNext()) == null) {
                return data;
            }

            while (true) {
                if (Integer.parseInt(trackerRow[0]) < Integer.parseInt(mfRow[0])) {
                    trackerRow = trackerCSV.readNext();
                } else if (Integer.parseInt(trackerRow[0]) > Integer.parseInt(mfRow[0])) {
                    mfRow = mfCSV.readNext();
                } else {
                    Double centroidX = Double.parseDouble(mfRow[3]);
                    Double centroidY = Double.parseDouble(mfRow[4]);
                    boolean isMoving = Boolean.parseBoolean(trackerRow[5]);
                    Double isMovingVal = (isMoving) ? 1.0 : 0.0;
                    Double speed = Double.parseDouble(mfRow[5]);
                    data.csvData.add(new Double[]{centroidX, centroidY, isMovingVal, speed});

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

                    if (speed > data.maxSpd) {
                        data.maxSpd = speed;
                    } else if (speed < data.minSpd) {
                        data.minSpd = speed;
                    }

                    trackerRow = trackerCSV.readNext();
                    mfRow = mfCSV.readNext();
                }
                if (trackerRow == null || mfRow == null) {
                    break;
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return data;
    }

    public static Data getCSVData(String CSVLoc) throws IOException {
        Data data = new Data();
        data.csvData = new ArrayList<>();
        data.minX = 0.0;
        data.maxX = 0.0;
        data.minY = 0.0;
        data.maxY = 0.0;
        data.maxSpd = 0.0;
        data.minSpd = 0.0;

        return getCSVData(CSVLoc, data);
    }

    private static Data getCSVData(String CSVLoc, Data data) throws IOException {
        try {
            CSVReader csv = new CSVReader(new FileReader(CSVLoc));
            String[] row = null;
            csv.readNext();

            if ((row = csv.readNext()) == null) {
                return data;
            }

            while (true) {
                Double centroidX = Double.parseDouble(row[4]);
                Double centroidY = Double.parseDouble(row[5]);
                Integer isTuring = Integer.parseInt(row[12]);
                Double isTuringVal = isTuring.doubleValue();
                Double timeStamp = Double.parseDouble(row[2]);
//                    Double speed = Double.parseDouble(mfRow[5]);
                data.csvData.add(new Double[]{centroidX, centroidY, isTuringVal, timeStamp});

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

                row = csv.readNext();

                if (row == null) {
                    break;
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return data;
    }
}
