package dataUtil;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 *
 * @author mshao1
 */
public class DatatsetHeadRemover {

    static String IMAGE_PATH = "\\\\MEDIXSRV\\Nematodes\\data\\*****\\input\\";
    static String LOG_PATH = "\\\\MEDIXSRV\\Nematodes\\data\\*****\\log\\";
    static int OFFSET = 250;
    static String OUTPUT_IMAGE_PATH = "\\\\MEDIXSRV\\Nematodes\\data\\*****\\newinput\\";
    static String OUTPUT_LOG_PATH = "\\\\MEDIXSRV\\Nematodes\\data\\*****\\newlog\\";

    static final String[] EXTENSIONS = new String[]{
        "jpeg", "jpg"
    };

    static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            for (final String ext : EXTENSIONS) {
                if (name.endsWith("." + ext)) {
                    return (true);
                }
            }
            return (false);
        }
    };

    public static void main(String[] args) throws IOException {
        String catagories = "N2_nf";
        int catNum = 19;

        String c = catagories + Integer.toString(catNum);
        String trackerDatPath = LOG_PATH.replace("*****", c) + "log.dat";
        String trackerLogPath = LOG_PATH.replace("*****", c) + "log.txt";
        new File(OUTPUT_LOG_PATH.replace("*****", c)).mkdir();
        new File(OUTPUT_IMAGE_PATH.replace("*****", c)).mkdir();
        String trackerDatOutputPath = OUTPUT_LOG_PATH.replace("*****", c) + "log.dat";
        String trackerLogOutputPath = OUTPUT_LOG_PATH.replace("*****", c) + "log.txt";
        String inputPath = IMAGE_PATH.replace("*****", c);
        File inputDir = new File(inputPath);
        int counter = 0;

        try {
            DataInputStream is = new DataInputStream(new FileInputStream(new File(trackerDatPath)));
            DataOutputStream os = new DataOutputStream(new FileOutputStream(new File(trackerDatOutputPath)));

            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(trackerLogPath)));
            FileWriter fw = new FileWriter(trackerLogOutputPath);

            String logLine = "";
            int frame = 0;
            long timeStamp = 0;
            int x = 0;
            int y = 0;
            int isMoving = 0;

            for (final File f : inputDir.listFiles(IMAGE_FILTER)) {
                if (counter < OFFSET) {
                    counter++;
                    br.readLine();
                    is.readInt();
                    is.readLong();
                    is.readInt();
                    is.readInt();
                    is.readInt();
                    continue;
                }

                int newIndex = counter - OFFSET;
                File nf = new File(OUTPUT_IMAGE_PATH.replace("*****", c) + String.format("%07d", newIndex) + ".jpeg");
                Files.copy(f.toPath(), nf.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println(f.getName());

                if ((logLine = br.readLine()) != null) {
                    String[] logLineSplit = logLine.split("\\s+");
                    frame = newIndex;
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

                    fw.write(String.format("%07d %d %d %d %d%n", frame, timeStamp, x, y, isMoving));
//                        System.out.println(counter);
                }
                counter++;
            }
            System.out.println(c + " has finished");
            fw.close();
            os.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

}
