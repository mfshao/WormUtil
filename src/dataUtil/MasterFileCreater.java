package dataUtil;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author MSHAO1
 */
public class MasterFileCreater {

    static String RONPATH = "\\\\CDM-MEDIXSRV\\Nematodes\\data\\*****\\matlab";
    static String KYLEPATH = "\\\\CDM-MEDIXSRV\\Nematodes\\data\\*****\\data";
    static String OUTPUTPATH = "\\\\CDM-MEDIXSRV\\Nematodes\\data\\*****\\data";
    static String CATAGORYNAME = "egl19_f1";
    
    public enum MasterFileHeaders {
        FrameNum, ElapsedTimeInLogFile, DeltaTimeInLogFile, CentroidX, CentroidY, Speed, Acceleration, Angle, AngularVelocity, ElapsedTimeInVideo, NumRows, NumCols, Resol, CameraStartRow, CameraStartCol, CameraStepRows, CameraStepCols, CameraOffsetRows, CameraOffsetCols, CropOffsetRows, CropOffsetCols, TotalOffsetRows, TotalOffsetCols, LclCentroidRow, LclCentroidCol, GblCentroidRow, GblCentroidCol, Area, MajorAxisLength, MinorAxisLength, Elongation, ComptFactor, Heywood, Hydraulic, RectBigSide, RectRatio, Perimeter, Ixx, Iyy, Ixy, MaxWidth, Posture, SkewerAngle, IsLoop, Length, HeadRow, HeadCol, TailRow, TailCol, HeadCurvPtRow, HeadCurvPtCol, TailCurvPtRow, TailCurvPtCol, CurvHead, CurvTail, IntH, IntT, SkelNumPixels, LengthToPixels, Fatness, Thickness, SegStatus, SktAmpRatio, SktCmptFactor, SktElgFactor, SktIxx, SktIyy, SktAglAve, Xsym, Ysym, XYsym, TrackAmplitude, TrackPeriod, SktvAglAve, SktvDisAveToLength, SktvDisMaxToLength, SktvDisMinToLength, SktvAglMax, SktpMovement, DirectionCode, GblCentroidColNew, GblCentroidRowNew, DeltaTimeInVideo, DeltaX, DeltaY, DeltaDist, VectorAngle, InstantVelocity, InstantAccel, CumDist, Range
    }

    public static void main(String[] args) {
        String pssDataPath = RONPATH.replace("*****", CATAGORYNAME) + "\\AllFeatures.csv";
        String trajectoryDataPath = KYLEPATH.replace("*****", CATAGORYNAME) + "\\movementFeatures.csv";
        String masterFilePath = OUTPUTPATH.replace("*****", CATAGORYNAME) + "\\masterFile.csv";

        try {
            boolean isFirstTime =  true;
            final Appendable masterFileWriter = new FileWriter(masterFilePath);
            final CSVPrinter printer;
            printer = CSVFormat.DEFAULT.withHeader(MasterFileHeaders.class).print(masterFileWriter);
            
            System.out.println("====== Start of parsing PSS file ======");
            Reader pssDataIn;
            pssDataIn = new FileReader(pssDataPath);
            Iterable<CSVRecord> pssDataRecords = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(pssDataIn);
            Iterator<CSVRecord> pssDataRecordsItr = pssDataRecords.iterator();
            System.out.println("done!");
            System.out.println("====== End of parsing PSS file ======");
            
            System.out.println("====== Start of parsing trajectory file ======");
            Reader trajectoryDataIn;
            trajectoryDataIn = new FileReader(trajectoryDataPath);
            Iterable<CSVRecord> trajectoryDataRecords = CSVFormat.DEFAULT.parse(trajectoryDataIn);
            Iterator<CSVRecord> trajectoryDataRecordsItr = trajectoryDataRecords.iterator();
            System.out.println("done!");
            System.out.println("====== End of parsing trajectory file ======");
            
            System.out.println("====== Start of creating master file ======");
            while(pssDataRecordsItr.hasNext() && trajectoryDataRecordsItr.hasNext()) {
                CSVRecord pssDataRecord = pssDataRecordsItr.next();
                if(isFirstTime && (pssDataRecord.size() != 84)) {
                    System.out.println("Old version PSS file found! No good!");
                    System.out.println("The length of the PSS is " + pssDataRecord.size());
                    return;
                }
                isFirstTime = false;
                CSVRecord trajectoryDataRecord = trajectoryDataRecordsItr.next();
                
                int pssDataRecordFrameID = Integer.parseInt(pssDataRecord.get(0)) - 1;
                int trajectoryDataRecordFrameID = Integer.parseInt(trajectoryDataRecord.get(0));
                
                while (pssDataRecordFrameID != trajectoryDataRecordFrameID) {
                    if (pssDataRecordFrameID < trajectoryDataRecordFrameID) {
                        pssDataRecord = pssDataRecordsItr.next();
                        pssDataRecordFrameID = Integer.parseInt(pssDataRecord.get(0)) - 1;
                    } else if (pssDataRecordFrameID > trajectoryDataRecordFrameID) {
                        trajectoryDataRecord = trajectoryDataRecordsItr.next();
                        trajectoryDataRecordFrameID = Integer.parseInt(trajectoryDataRecord.get(0));
                    }
                }
                
                List<String> masterFileData = new ArrayList<>();
                Iterator<String> trajectoryDataEntryItr = trajectoryDataRecord.iterator();
                while (trajectoryDataEntryItr.hasNext()) {
                    masterFileData.add(trajectoryDataEntryItr.next());
                }
                
                Iterator<String> pssDataEntryItr = pssDataRecord.iterator();
                pssDataEntryItr.next(); // skip SeqNum in Ron's file
                pssDataEntryItr.next(); // skip FrameNum in Ron's file
                while (pssDataEntryItr.hasNext()) {
                    masterFileData.add(pssDataEntryItr.next());
                }
                
                System.out.println(masterFileData.get(0));
                printer.printRecord(masterFileData);
                printer.flush();
            }
            
            System.out.println("done!");
            System.out.println("====== End of creating master file ======");
            pssDataIn.close();
            trajectoryDataIn.close();
            printer.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MasterFileCreater.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MasterFileCreater.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
