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

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MSHAO1
 */
public class LogNewToLegacyConverter {

    static String PATH = "\\\\CDM-MEDIXSRV\\Nematodes\\data\\*****\\log";
//    static String PATH = "D:\\log";
    static float PPS = 13.913f;
    static float PPUM = 0.07f;
    static float STEPPUM = 200.0f;
    static int COL = 640;
    static int ROW = 480;

    private static void prepareHeader(FileWriter fw, long firstTimeStamp, String fileName) {
        try {
            fw.write(assembleString(firstTimeStamp - 10L, "INFO", "easyEBB", String.format("Resolution is [%.1f, %.1f]:", (float) COL, (float) ROW), null, null));
            fw.write(assembleString(firstTimeStamp - 10L, "INFO", "easyEBB", "Size in pix:", String.format("col %d", COL), String.format("row %d", ROW)));
            fw.write(assembleString(firstTimeStamp - 10L, "WARNING", "easyEBB", "1 um = ? pixels", String.format("col %.3f", PPUM), String.format("row %.3f", PPUM)));
            fw.write(assembleString(firstTimeStamp - 10L, "INFO", "easyEBB", String.format("One step is %.3f pixels", PPS), null, null));
            fw.write(assembleString(firstTimeStamp - 10L, "INFO", "easyEBB", String.format("1 step = %.4f um", STEPPUM), null, null));
            fw.write(assembleString(firstTimeStamp - 5L, "WARNING", "capture", String.format("Start Writing Video: %s.avi", fileName), null, null));
        } catch (IOException ex) {
            Logger.getLogger(LogNewToLegacyConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void prepareBody(DataInputStream dis, FileWriter fw, int startFrame) {
        try {
            int frame = 0;
            long timeStamp = 0;
            int x = 0;
            int y = 0;
            int moving = 0;
            boolean isMoving = false;

            while (dis.available() > 0) {
                frame = dis.readInt();
                timeStamp = dis.readLong();
                x = dis.readInt();
                y = dis.readInt();
                moving = dis.readInt();

                if (frame >= startFrame) {
                    if (moving == 1 && isMoving == false) {
                        isMoving = true;
                        int stepx = (COL / 2 - x) / (Math.round(PPS));
                        int stepy = (ROW / 2 - y) / (Math.round(PPS));
                        fw.write(assembleString(timeStamp, "WARNING", "easyEBB", String.format("From (col,row): (%d, %d) to center | steps: (%d, %d)", x, y, stepx, stepy), null, null));
                    }

                    if (moving == 0 && isMoving == true) {
                        isMoving = false;
                    }

                    fw.write(assembleString(timeStamp, "WARNING", "capture", "wrote frame", null, null));
                }
            }
            fw.write(assembleString(timeStamp + 10L, "WARNING", "capture", "Stop Writing Video", null, null));
        } catch (IOException ex) {
            Logger.getLogger(LogNewToLegacyConverter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static String assembleString(long time, String level, String emitter, String msg1, String msg2, String msg3) {
        StringBuilder sb = new StringBuilder();
        sb.append(convertTime(time));
        sb.append('\t');
        sb.append(level);
        sb.append('\t');
        sb.append(emitter);
        sb.append('\t');
        sb.append('\t');
        sb.append(msg1);
        sb.append('\t');
        if (msg2 != null) {
            sb.append(msg2);
            sb.append('\t');
        }
        if (msg3 != null) {
            sb.append(msg3);
            sb.append('\t');
        }
        sb.append('\n');

        return sb.toString();
    }

    private static String convertTime(long time) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS", Locale.US);

        Instant instant = Instant.ofEpochMilli(time);
        ZoneId zoneId = ZoneId.of("America/Chicago");
        ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, zoneId);
        return zdt.format(dtf);
    }

    private static void initParas(String catagory) {
        if (catagory.contains("HR")) {
            COL = 1280;
            ROW = 960;
            PPUM = 0.14f;
            PPS = 27.826f;
        } else {
            COL = 640;
            ROW = 480;
            PPUM = 0.07f;
            PPS = 13.913f;
        }
    }

    public static void main(String[] args) throws IOException {
        String catagories = "AIY_HR_f";
        int startCat = 5;
        int startFrame = 0;
        int numInCat = 1;

        for (int i = startCat; i < (startCat + numInCat); i++) {
            String c = catagories + Integer.toString(i);
            String trackerDatPath = PATH.replace("*****", c) + "\\log.dat";
            String trackerLegacyLogPath = PATH.replace("*****", c) + "\\log.log";
            initParas(c);

            FileWriter legacyFw;
            FileInputStream fis = new FileInputStream(trackerDatPath);
            DataInputStream dis = new DataInputStream(fis);
            legacyFw = new FileWriter(trackerLegacyLogPath);
            dis.readInt();
            long firstTimeStamp = dis.readLong();
            prepareHeader(legacyFw, firstTimeStamp, c);
            fis.getChannel().position(0);
            dis = new DataInputStream(fis);
            prepareBody(dis, legacyFw, startFrame);

            System.out.println(c + " has finished legacy parsing");
            legacyFw.close();
        }
    }
}
