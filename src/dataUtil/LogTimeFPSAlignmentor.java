/*
 * Copyright (C) 2017 MSHAO1
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
 * @author MSHAO1
 */
public class LogTimeFPSAlignmentor {

    static String PATH = "\\\\CDM-MEDIXSRV\\Nematodes\\data\\*****\\log";

    public static void main(String[] args) throws IOException {
        String name = "RIM_HR_nf7";
        double FPS = 26;
        long interval = Math.round(1000 / FPS);

        String trackerAlignedDatPath = PATH.replace("*****", name) + "\\log_a.dat";
        String trackerAlignedLogPath = PATH.replace("*****", name) + "\\log_a.txt";
        String trackerLogPath = PATH.replace("*****", name) + "\\log.txt";
        try {
            DataOutputStream os = new DataOutputStream(new FileOutputStream(new File(trackerAlignedDatPath)));
            FileWriter fw = new FileWriter(trackerAlignedLogPath);

            BufferedReader trackerBr = new BufferedReader(new InputStreamReader(new FileInputStream(trackerLogPath)));
            String logLine = trackerBr.readLine();
            String[] logLineSplit = logLine.split("\\s+");
            int frame = Integer.parseInt(logLineSplit[0]);
            long prevTimeStamp = Long.parseLong(logLineSplit[1]);
            int x = Integer.parseInt(logLineSplit[2]);
            int y = Integer.parseInt(logLineSplit[3]);
            int isMoving = Integer.parseInt(logLineSplit[4]);

            os.writeInt(frame);
            os.writeLong(prevTimeStamp);
            os.writeInt(x);
            os.writeInt(y);
            os.writeInt(isMoving);
            os.flush();

            fw.write(String.format("%07d %d %d %d %d%n", frame, prevTimeStamp, x, y, isMoving));

            while ((logLine = trackerBr.readLine()) != null) {
                logLineSplit = logLine.split("\\s+");
                frame = Integer.parseInt(logLineSplit[0]);
                long timeStamp = prevTimeStamp + interval;
                prevTimeStamp = timeStamp;
                x = Integer.parseInt(logLineSplit[2]);
                y = Integer.parseInt(logLineSplit[3]);
                isMoving = Integer.parseInt(logLineSplit[4]);

                os.writeInt(frame);
                os.writeLong(timeStamp);
                os.writeInt(x);
                os.writeInt(y);
                os.writeInt(isMoving);
                os.flush();

                fw.write(String.format("%07d %d %d %d %d%n", frame, timeStamp, x, y, isMoving));
                //               System.out.println(frame);
            }
            os.close();
            fw.close();
            System.out.println(name + " has finished log alignment");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
