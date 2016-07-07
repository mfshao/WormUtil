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
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Travis Shao
 */
public class LogTxtToDatConverter {

    static String PATH = "\\\\MEDIXSRV\\Nematodes\\data\\_high_resolution\\AIB_NF_1_062416_1440";
//    static String PATH = "C:\\Users\\Travis Shao\\Desktop";

    public static void main(String[] args) throws IOException {
        try {
            String trackerDatPath = PATH + "\\log.dat";
            DataOutputStream os = new DataOutputStream(new FileOutputStream(new File(trackerDatPath)));

            String trackerLogPath = PATH + "\\log.txt";
            BufferedReader trackerBr = new BufferedReader(new InputStreamReader(new FileInputStream(trackerLogPath)));
            String logLine;
            int frame = 0;
            long timeStamp = 0;
            int x = 0;
            int y = 0;
            int isMoving = 0;

            while ((logLine = trackerBr.readLine()) != null) {
                String[] logLineSplit = logLine.split("\\s+");
                frame = Integer.parseInt(logLineSplit[0]);
                timeStamp = Long.parseLong(logLineSplit[1]);
                x = Integer.parseInt(logLineSplit[2]);
                y = Integer.parseInt(logLineSplit[3]);
                isMoving = Integer.parseInt(logLineSplit[4]);

                os.writeInt(frame);
                os.writeLong(timeStamp);
                os.writeInt(x);
                os.writeInt(y);
                os.writeInt(isMoving);
                os.flush();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
