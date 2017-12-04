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

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Travis Shao
 */
public class LogDatToTxtConverter {

    //static String PATH = "\\\\MEDIXSRV\\Nematodes\\data\\AIY_HR_nf4\\log";
    static String PATH = "\\\\MEDIXSRV\\Nematodes\\data\\*****\\log";
//    static String PATH = "D:\\OTPT";

    public static void main(String[] args) throws IOException {
        String catagories = "N2_nf";
        int startNum = 32;
        int numInCat = 1;
        
        for (int i = startNum; i < startNum+numInCat; i++) {
            String c = catagories + Integer.toString(i);
            String trackerDatPath = PATH.replace("*****", c) + "\\log.dat";
            String trackerLogPath = PATH.replace("*****", c) + "\\log.txt";
        try {
            DataInputStream is = new DataInputStream(new FileInputStream(new File(trackerDatPath)));

            FileWriter fw = new FileWriter(trackerLogPath);
            int frame = 0;
            long timeStamp = 0;
            int x = 0;
            int y = 0;
            int isMoving = 0;

            while (is.available() > 0) {
                frame = is.readInt();
                timeStamp = is.readLong();
                x = is.readInt();
                y = is.readInt();
                isMoving = is.readInt();

                fw.write(String.format("%07d %d %d %d %d%n", frame, timeStamp, x, y, isMoving));
            }
            System.out.println(c + " has finished DAT to TXT parsing");
            fw.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    }
}
