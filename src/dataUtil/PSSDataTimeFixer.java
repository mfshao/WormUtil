package dataUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author MSHAO1
 */
public class PSSDataTimeFixer {

    static String LOG_DIR_PATH = "\\\\MEDIXSRV\\Nematodes\\data\\******\\log";
    static String MATLAB_PATH = "\\\\CDM-MEDIXSRV\\Nematodes\\data\\*****\\matlab";
    static String OUTPUT_PATH = "\\\\CDM-MEDIXSRV\\Nematodes\\data\\*****\\data";
    static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
    static int OFFSET = 7;

    public enum MasterFileHeaders {
        FrameNum, ElapsedTimeInLogFile, DeltaTimeInLogFile, CentroidX, CentroidY, Speed, Acceleration, Angle, AngularVelocity, ElapsedTimeInVideo, NumRows, NumCols, Resol, CameraStartRow, CameraStartCol, CameraStepRows, CameraStepCols, CameraOffsetRows, CameraOffsetCols, CropOffsetRows, CropOffsetCols, TotalOffsetRows, TotalOffsetCols, LclCentroidRow, LclCentroidCol, GblCentroidRow, GblCentroidCol, Area, MajorAxisLength, MinorAxisLength, Elongation, ComptFactor, Heywood, Hydraulic, RectBigSide, RectRatio, Perimeter, Ixx, Iyy, Ixy, MaxWidth, Posture, SkewerAngle, IsLoop, Length, HeadRow, HeadCol, TailRow, TailCol, HeadCurvPtRow, HeadCurvPtCol, TailCurvPtRow, TailCurvPtCol, CurvHead, CurvTail, IntH, IntT, SkelNumPixels, LengthToPixels, Fatness, Thickness, SegStatus, SktAmpRatio, SktCmptFactor, SktElgFactor, SktIxx, SktIyy, SktAglAve, Xsym, Ysym, XYsym, TrackAmplitude, TrackPeriod, SktvAglAve, SktvDisAveToLength, SktvDisMaxToLength, SktvDisMinToLength, SktvAglMax, SktpMovement, DirectionCode, GblCentroidColNew, GblCentroidRowNew, DeltaTimeInVideo, DeltaX, DeltaY, DeltaDist, VectorAngle, InstantVelocity, InstantAccel, CumDist, Range
    }

    public enum AllFeaturesHeaders {
        SeqNum, FrameNum, ElapsedTime, NumRows, NumCols, Resol, CameraStartRow, CameraStartCol, CameraStepRows, CameraStepCols, CameraOffsetRows, CameraOffsetCols, CropOffsetRows, CropOffsetCols, TotalOffsetRows, TotalOffsetCols, LclCentroidRow, LclCentroidCol, GblCentroidRow, GblCentroidCol, Area, MajorAxisLength, MinorAxisLength, Elongation, ComptFactor, Heywood, Hydraulic, RectBigSide, RectRatio, Perimeter, Ixx, Iyy, Ixy, MaxWidth, Posture, SkewerAngle, IsLoop, Length, HeadRow, HeadCol, TailRow, TailCol, HeadCurvPtRow, HeadCurvPtCol, TailCurvPtRow, TailCurvPtCol, CurvHead, CurvTail, IntH, IntT, SkelNumPixels, LengthToPixels, Fatness, Thickness, SegStatus, SktAmpRatio, SktCmptFactor, SktElgFactor, SktIxx, SktIyy, SktAglAve, Xsym, Ysym, XYsym, TrackAmplitude, TrackPeriod, SktvAglAve, SktvDisAveToLength, SktvDisMaxToLength, SktvDisMinToLength, SktvAglMax, SktpMovement, DirectionCode, GblCentroidColNew, GblCentroidRowNew, DeltaTimeInVideo, DeltaX, DeltaY, DeltaDist, VectorAngle, InstantVelocity, InstantAccel, CumDist, Range
    }

    public enum ContourAndSkelHeaders {
        SeqNum, FrameNum, ElapsedTime, NumRows, NumCols, Resol, CameraStartRow, CameraStartCol, CameraStepRows, CameraStepCols, CameraOffsetRows, CameraOffsetCols, CropOffsetRows, CropOffsetCols, TotalOffsetRows, TotalOffsetCols, LclCentroidRow, LclCentroidCol, GblCentroidRow, GblCentroidCol, Area, MajorAxisLength, MinorAxisLength, Elongation, ComptFactor, Heywood, Hydraulic, RectBigSide, RectRatio, Perimeter, Ixx, Iyy, Ixy, MaxWidth, Posture, SkewerAngle, IsLoop, Length, HeadRow, HeadCol, TailRow, TailCol, HeadCurvPtRow, HeadCurvPtCol, TailCurvPtRow, TailCurvPtCol, CurvHead, CurvTail, IntH, IntT, SkelNumPixels, LengthToPixels, Fatness, Thickness, SegStatus
    }

    private static File[] logFilefinder(String dirName) {
        File dir = new File(dirName);
        return dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".log");
            }
        });

    }

    public static void main(String[] args) {
        String catagories = "RIM_HR_nf";
        int startNum = 1;
        int numInCat = 18;

        for (int i = startNum; i < startNum + numInCat; i++) {
            
            try {
                String c = catagories + Integer.toString(i);
                System.out.println("========="+c+"==================");
                String trackerLegencyLogDirPath = LOG_DIR_PATH.replace("******", c);
                String adDataPath = MATLAB_PATH.replace("*****", c) + "\\AllFeatures.csv";
                String adfDataPath = MATLAB_PATH.replace("*****", c) + "\\AllFeaturesFixed.csv";
                String csDataPath = MATLAB_PATH.replace("*****", c) + "\\ContourAndSkel.csv";
                String csfDataPath = MATLAB_PATH.replace("*****", c) + "\\ContourAndSkelFixed.csv";
                String mfDataPath = OUTPUT_PATH.replace("*****", c) + "\\masterFile.csv";
                String mffDataPath = OUTPUT_PATH.replace("*****", c) + "\\masterFileFixed.csv";
                File adFile = new File(adDataPath);
                File adfFile = new File(adfDataPath);
                File csFile = new File(csDataPath);
                File csfFile = new File(csfDataPath);
                File mfFile = new File(mfDataPath);
                File mffFile = new File(mffDataPath);

                final Appendable mffWriter = new FileWriter(mffDataPath);
                final CSVPrinter mffPrinter;
                mffPrinter = CSVFormat.DEFAULT.withHeader(MasterFileHeaders.class).print(mffWriter);

                final Appendable adfWriter = new FileWriter(adfDataPath);
                final CSVPrinter adfPrinter;
                adfPrinter = CSVFormat.DEFAULT.withHeader(AllFeaturesHeaders.class).print(adfWriter);

                final Appendable csfWriter = new FileWriter(csfDataPath);
                final CSVPrinter csfPrinter;
                csfPrinter = CSVFormat.DEFAULT.withHeader(ContourAndSkelHeaders.class).print(csfWriter);

                File[] existingLogs = logFilefinder(trackerLegencyLogDirPath);
                File logFile = null;
                for (File f : existingLogs) {
                    if (f.getName().contains("tracker.log")) {
                        logFile = f;
                    } else if (f.getName().contains("log.log")) {
                        logFile = f;
                    }
                }
                if (logFile == null) {
                    System.out.println("No .log files found!");
                    continue;
                }

                Scanner data = new Scanner(logFile);

                long epoch = 0;
                int counter = 1;
                String line = "";

                Reader adDataIn;
                adDataIn = new FileReader(adDataPath);
                Iterable<CSVRecord> adDataRecords = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(adDataIn);
                Iterator<CSVRecord> adDataRecordsItr = adDataRecords.iterator();

                Reader csDataIn;
                csDataIn = new FileReader(csDataPath);
                Iterable<CSVRecord> csDataRecords = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csDataIn);
                Iterator<CSVRecord> csDataRecordsItr = csDataRecords.iterator();

                Reader mfDataIn;
                mfDataIn = new FileReader(mfDataPath);
                Iterable<CSVRecord> mfDataRecords = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(mfDataIn);
                Iterator<CSVRecord> mfDataRecordsItr = mfDataRecords.iterator();

                while (adDataRecordsItr.hasNext() && csDataRecordsItr.hasNext() && data.hasNext()) {
                    line = data.nextLine();
//                    System.out.println(line);
                    if (line.contains("wrote frame")) {
                        String[] info = line.split("\t");
                        if (epoch == 0) {
                            epoch = parseTime(info[0]);
                        }
                        double timeElapsed = (parseTime(info[0]) - epoch) / 1000.0;
                        CSVRecord adDataRecord = adDataRecordsItr.next();
                        List<String> adfData = new ArrayList<>();

                        for (int j = 0; j < adDataRecord.size(); j++) {
                            if (j != 2) {
                                adfData.add(adDataRecord.get(j));
                            } else {
                                adfData.add(Double.toString(timeElapsed));
                            }
                        }

                        CSVRecord csDataRecord = csDataRecordsItr.next();
                        List<String> csfData = new ArrayList<>();

                        for (int j = 0; j < csDataRecord.size(); j++) {
                            if (j != 2) {
                                csfData.add(csDataRecord.get(j));
                            } else {
                                csfData.add(Double.toString(timeElapsed));
                            }
                        }

                        System.out.println(adfData.get(0));
                        adfPrinter.printRecord(adfData);
                        adfPrinter.flush();
                        csfPrinter.printRecord(csfData);
                        csfPrinter.flush();

                        if (counter > OFFSET && mfDataRecordsItr.hasNext()) {
                            CSVRecord mfDataRecord = mfDataRecordsItr.next();
                            List<String> mffData = new ArrayList<>();

                            for (int j = 0; j < mfDataRecord.size(); j++) {
                                if (j != 9) {
                                    mffData.add(mfDataRecord.get(j));
                                } else {
                                    mffData.add(Double.toString(timeElapsed));
                                }
                            }
                            mffPrinter.printRecord(mffData);
                            mffPrinter.flush();
                        } else {
                            counter++;
                        }
                    }
                }


                System.out.println("done!");
                System.out.println("====== End of fixing PSS files ======");
                adDataIn.close();
                csDataIn.close();
                mfDataIn.close();
                data.close();
                adfPrinter.close();
                csfPrinter.close();
                mffPrinter.close();
                
                adFile.delete();
                adfFile.renameTo(adFile);
                csFile.delete();
                csfFile.renameTo(csFile);
                mfFile.delete();
                mffFile.renameTo(mfFile);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static long parseTime(String time) throws ParseException {
        return format.parse(time).getTime();
    }
}
