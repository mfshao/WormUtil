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
package dataUtil;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Travis Shao
 */
public class LogTxtToCsvtConverter {

    //static String PATH = "\\\\MEDIXSRV\\Nematodes\\data\\AIB_HR_nf11\\log";
    static String PATH = "\\\\CDM-MEDIXSRV\\Nematodes\\data\\*****\\log";
//    static String PATH = "C:\\Users\\Travis Shao\\Desktop";

    public static void main(String[] args) throws IOException {
        String catagories = "N2_nf";
        int startNumber = 4;
        int numInCat = 1;

        for (int i = startNumber; i < startNumber+numInCat; i++) {
            String c = catagories + Integer.toString(i);
            String trackerLogPath = PATH.replace("*****", c) + "\\feature.log";
            String CSVLoc = PATH.replace("*****", c) + "\\feature_csv.csv";
            try {
                CSVWriter csvw = new CSVWriter(new FileWriter(CSVLoc));

                BufferedReader trackerBr = new BufferedReader(new InputStreamReader(new FileInputStream(trackerLogPath)));
                String logLine;

                while ((logLine = trackerBr.readLine()) != null) {
                    String[] logLineSplit = logLine.split("\\s+");
                    
                    csvw.writeNext(logLineSplit);
                }
                System.out.println(c + " has finished TXT to CSV parsing");
                csvw.close();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }
}
