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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Travis Shao
 */
public class logConverter {

    static String PATH = "\\\\MEDIXSRV\\Nematodes\\data\\_high_resolution\\*";

    public static void main(String[] args) throws IOException {
        try {
            String trackerDatPath = PATH + "\\log.dat";
            DataOutputStream os = new DataOutputStream(new FileOutputStream(new File(trackerDatPath)));

            String trackerLogPath = PATH + "\\log.txt";
            File trackerLog = new File(trackerLogPath);
            Scanner trackerSc = new Scanner(trackerLog);
            trackerSc.useDelimiter(" ");
            int frame = 0;
            long timeStamp = 0;

            while (trackerSc.hasNext()) {
                frame = trackerSc.nextInt();
                timeStamp = trackerSc.nextLong();
                trackerSc.nextInt();
                trackerSc.nextInt();
                trackerSc.nextInt();

                os.writeInt(frame);
                os.writeLong(timeStamp);
                os.writeInt(trackerSc.nextInt());
                os.writeInt(trackerSc.nextInt());
                os.writeInt(trackerSc.nextInt());
                if (frame % 30 == 0) {
                    os.flush();
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
