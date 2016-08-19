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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Travis Shao
 */
public class LogTimeCorrector {

    static String HR_PATH = "\\\\MEDIXSRV\\Nematodes\\data\\che2_HR_nf3\\log";
    static String DS_PATH = "\\\\MEDIXSRV\\Nematodes\\data\\che2_DS_nf3\\log";
    static String DS_NEW_PATH = DS_PATH + "\\new_log";

    public static void main(String[] args) throws IOException {
        try {
//            String trackerDatPath = PATH + "\\log.dat";
//            DataOutputStream os = new DataOutputStream(new FileOutputStream(new File(trackerDatPath)));

            String trackerHRLogPath = HR_PATH + "\\log.txt";
            BufferedReader trackerHRBr = new BufferedReader(new InputStreamReader(new FileInputStream(trackerHRLogPath)));
            String trackerDSLogPath = DS_PATH + "\\log.txt";
            BufferedReader trackerDSBr = new BufferedReader(new InputStreamReader(new FileInputStream(trackerDSLogPath)));

            new File(DS_NEW_PATH).mkdirs();

            String trackerLogPath = DS_NEW_PATH + "\\log.txt";
            FileWriter fw = new FileWriter(trackerLogPath);
            String trackerDatPath = DS_NEW_PATH + "\\log.dat";
            DataOutputStream os = new DataOutputStream(new FileOutputStream(new File(trackerDatPath)));

            String logHRLine;
            String logDSLine;
            int frame = 0;
            long hrTimeStamp = 0;
            int x = 0;
            int y = 0;
            int isMoving = 0;

            while ((logHRLine = trackerHRBr.readLine()) != null) {
                String[] logHRLineSplit = logHRLine.split("\\s+");
                hrTimeStamp = Long.parseLong(logHRLineSplit[1]);

                logDSLine = trackerDSBr.readLine();
                if (logDSLine != null) {
                    String[] logDSLineSplit = logDSLine.split("\\s+");
                    frame = Integer.parseInt(logDSLineSplit[0]);
                    x = Integer.parseInt(logDSLineSplit[2]);
                    y = Integer.parseInt(logDSLineSplit[3]);
                    isMoving = Integer.parseInt(logDSLineSplit[4]);
                }

                fw.write(String.format("%07d %d %d %d %d%n", frame, hrTimeStamp, x, y, isMoving));
                os.writeInt(frame);
                os.writeLong(hrTimeStamp);
                os.writeInt(x);
                os.writeInt(y);
                os.writeInt(isMoving);
                os.flush();
                System.out.println(frame);
            }
            fw.close();

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
