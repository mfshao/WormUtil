/*
 * Copyright (C) 2016 MSHAO1
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

import com.opencsv.CSVWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author MSHAO1
 */
public class LogComponentSizeCount {

    static String PATH = "D:\\New folder\\";
    static String TXTPOST = "\\input\\log.txt";
    static String DATPOST = "\\input\\log.dat";
    static String TXTFILE = "\\log\\size.txt";
    static String CSVFILE = "\\log\\size.csv";

    public static void main(String[] args) throws IOException {
        String[] str = {"AIB_HR_f6"};

        for (String s : str) {
            String filePath = PATH + s + DATPOST;
            String outPath = PATH + s + TXTFILE;
            String outCsvPath = PATH + s + CSVFILE;
//            String filePath = PATH + DATPOST;
//            String outPath = PATH + TXTFILE;
//            String outCsvPath = PATH  + CSVFILE;
//            BufferedReader trackerBr = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            DataInputStream is = new DataInputStream(new FileInputStream(new File(filePath)));
            FileWriter fw = new FileWriter(outPath);
            CSVWriter writer = new CSVWriter(new FileWriter(outCsvPath));

            long timeStamp = 0;
            int moving = 0;
            int frame = 0;
            int min = 0;
            long tempTime = 0;
            int moveCount = 0;
            int size = 0;
            boolean isMoving = false;
            String[] result = new String[2];

            while (is.available() > 0) {
                frame = is.readInt();
                timeStamp = is.readLong();
                is.readInt();
                is.readInt();
                is.readInt();
                size = is.readInt();

                result[0] = Integer.toString(frame);
                result[1] = Integer.toString(size);

                writer.writeNext(result);
                fw.write(String.format("%d %d%n", frame, size));
            }

            fw.close();
            writer.close();
            System.out.println(s + " has finished!");
        }
    }
}
