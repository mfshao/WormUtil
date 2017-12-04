package dataUtil;

import java.io.File;
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
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author MSHAO1
 */
public class DatabaseFileParser {

    static final String MASTER_FILE_PATH = "\\\\CDM-MEDIXSRV\\Nematodes\\data\\*****\\data\\masterFile.csv";
    static final String KYLE_PATH = "\\\\CDM-MEDIXSRV\\Nematodes\\data\\*****\\data";
    static final String OUTPUT_PATH = "\\\\CDM-MEDIXSRV\\Nematodes\\data\\*****\\dbtables";
    static final String CATAGORY_NAME = "N2_nf4 - Copy";
    static final String[] TABLE_NAMES = new String[]{"Straintype", "ImageInfo", "ExperimentalFeatures", "FeatureLog", "HeadTailInfo", "LogDat", "RawMovementFeatures", "MovementFeaturesBinned", "Occupancy", "PostureFeatures", "ReferenceFeatures", "SegmentCentroid", "SizeandShapeFeatures", "Tracker", "TrajectoryFeatures", "VideoInfo"};

    private ArrayList<String> frameIdList;
    private String strainTypeId;
    private CSVParser masterFileDataRecordParser;

    public DatabaseFileParser() {
        try {
            new File(OUTPUT_PATH.replace("*****", CATAGORY_NAME)).mkdir();
            strainTypeId = "";
            frameIdList = new ArrayList<>();
            final String masterFilePath = MASTER_FILE_PATH.replace("*****", CATAGORY_NAME);
            System.out.println("====== Start of reading in master file ======");
            Reader masterFileIn;
            masterFileIn = new FileReader(masterFilePath);
            masterFileDataRecordParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(masterFileIn);
            System.out.println("done!");
            System.out.println("====== End of reading in master file ======");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DatabaseFileParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DatabaseFileParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void resetMasterFileDataRecordParser() throws IOException {
        if (!masterFileDataRecordParser.isClosed()) {
            try {
                final String masterFilePath = MASTER_FILE_PATH.replace("*****", CATAGORY_NAME);
                System.out.println("====== Start of reseting MF parser ======");
                Reader masterFileIn;
                masterFileIn = new FileReader(masterFilePath);
                masterFileDataRecordParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(masterFileIn);
                System.out.println("done!");
                System.out.println("====== End of reseting MF parser ======");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DatabaseFileParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void createAllDBTables() {
        createStraintype();
        createImageInfo();
        createTablesFromMasterFile();
    }

    private void createStraintype() {
        try {
            final String[] outputHeaders = new String[]{"StrainTypeid", "WormType", "ResolutionType", "FoodCondition", "SIndex"};
            final String outputFileName = "Straintype.csv";
            final String outputFilePath = OUTPUT_PATH.replace("*****", CATAGORY_NAME) + "\\" + outputFileName;
            File outputFile = new File(outputFilePath);
            outputFile.createNewFile();

            final Appendable outputFileWriter = new FileWriter(outputFilePath);
            final CSVPrinter printer = CSVFormat.DEFAULT.withHeader(outputHeaders).print(outputFileWriter);

            String[] decompositedCName = CATAGORY_NAME.split("_");
            String wormType = "";
            String resolutionType = "";
            String foodCondition = "";
            String sIndex = "";

            if (decompositedCName.length > 0) {
                wormType = decompositedCName[0];

                String fCAndSI = "";
                if (decompositedCName.length == 2) {
                    resolutionType = "LR";
                    fCAndSI = decompositedCName[1];
                } else if (decompositedCName.length == 3) {
                    resolutionType = "HR";
                    fCAndSI = decompositedCName[2];
                }

                if (!fCAndSI.isEmpty()) {
                    foodCondition = fCAndSI.replaceAll("[^A-Za-z]", "");
                    sIndex = fCAndSI.replaceAll("[^0-9]", "");
                }

                strainTypeId = wormType + "_" + resolutionType + "_" + foodCondition + "_" + sIndex;
            } else {
                System.out.println("CATAGORY_NAME parse fails");
                return;
            }

            List<String> outputFileData = new ArrayList<>();
            outputFileData.add(strainTypeId);
            outputFileData.add(wormType);
            outputFileData.add(resolutionType);
            outputFileData.add(foodCondition);
            outputFileData.add(sIndex);
            printer.printRecord(outputFileData);
            printer.flush();
            printer.close();
            System.out.println("========= StrainTypeId creat completed ==========");
        } catch (IOException ex) {
            Logger.getLogger(DatabaseFileParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void createImageInfo() {
        try {
            final String[] outputHeaders = new String[]{"FrameId", "StrainTypeId", "ImageNumber", "TimeElapsed", "TimeDelta", "IMinute"};
            final String outputFileName = "Imageinfo.csv";
            final String outputFilePath = OUTPUT_PATH.replace("*****", CATAGORY_NAME) + "\\" + outputFileName;
            File outputFile = new File(outputFilePath);
            outputFile.createNewFile();

            final Appendable outputFileWriter = new FileWriter(outputFilePath);
            final CSVPrinter printer = CSVFormat.DEFAULT.withHeader(outputHeaders).print(outputFileWriter);

            String frameId = "";
            String imageNumberStr = "";
            String timeElapsedStr = "";
            String timeDelta = "";
            String iMinute = "";
            
            Iterator<CSVRecord> masterFileDataRecordItr = masterFileDataRecordParser.iterator(); 
            while (masterFileDataRecordItr.hasNext()) {
                CSVRecord record = masterFileDataRecordItr.next();
                imageNumberStr = record.get(0);
                frameId = strainTypeId + "_" + imageNumberStr;
                frameIdList.add(frameId);
                timeDelta = record.get(2);
                timeElapsedStr = record.get(1);
                double timeElapsed = Double.parseDouble(timeElapsedStr);
                iMinute = Long.toString(Math.round(Math.floor(timeElapsed / 60.0)));

                List<String> outputFileData = new ArrayList<>();
                outputFileData.add(frameId);
                outputFileData.add(strainTypeId);
                outputFileData.add(imageNumberStr);
                outputFileData.add(timeElapsedStr);
                outputFileData.add(timeDelta);
                outputFileData.add(iMinute);
                printer.printRecord(outputFileData);
                printer.flush();

                System.out.println(imageNumberStr);
            }
            printer.close();
            System.out.println("========= ImageInfo creation completed ==========");
        } catch (IOException ex) {
            Logger.getLogger(DatabaseFileParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void createTablesFromMasterFile() {
        try {
            resetMasterFileDataRecordParser();
            
            if (frameIdList.isEmpty()) {
                System.out.println("Empty frameid list found!");
                return;
            }

            String[] experimentalFeaturesOutputHeaders = new String[]{"FrameId", "InertiaXX", "InertiaYY", "InertiaXY"};
            String outputFileName = "ExperimentalFeatures.csv";
            CSVPrinter experimentalFeaturesPrinter = getOutputCSVPrinter(outputFileName, experimentalFeaturesOutputHeaders);

            String[] headTailInfoOutputHeaders = new String[]{"FrameId", "HeadTailId", "IntH", "IntT"};
            outputFileName = "HeadTailInfo.csv";
            CSVPrinter headTailInfoPrinter = getOutputCSVPrinter(outputFileName, headTailInfoOutputHeaders);

            String[] postureFeaturesOutputHeaders = new String[]{"FrameId", "HeadTailId", "MajorAxisLength", "MinorAxisLength", "Heywood", "Hydraulic", "RectBigSide", "RecRatio", "Posture", "SkewerAngle", "IsLoop", "HeadRow", "HeadCol", "TailRow", "TailCol", "SkelNumPixels", "LengthToPixels", "SktAmpRatio", "SktCmptFactor", "SktElgFactor", "Sktlxx", "Sktlyy", "SktAglAve", "Xsym", "Ysym", "XYSym", "TrackAmplitude", "TrackPeriod", "SktvAglAve", "SktvDisAveToLength", "SktvDisMaxToLength", "SktvDisMinToLength", "SktvAgIMax", "LclCentroidRow", "LclCentroidCol", "Elongation", "ComptFactor"};
            outputFileName = "PostureFeatures.csv";
            CSVPrinter postureFeaturesPrinter = getOutputCSVPrinter(outputFileName, postureFeaturesOutputHeaders);

            String[] rawMovementFeaturesOutputHeaders = new String[]{"FrameId", "Speed", "Acceleration", "Angle", "AngularVelocity"};
            outputFileName = "RawMovementFeatures.csv";
            CSVPrinter rawMovementFeaturesPrinter = getOutputCSVPrinter(outputFileName, rawMovementFeaturesOutputHeaders);

            String[] referenceFeaturesOutputHeaders = new String[]{"FrameId", "FrameNum", "NumRows", "NumCols", "Resol", "CameraStartRow", "CameraStartCol", "CameraStepRows", "CameraStepCols", "CameraOffsetRows", "CameraOffsetCols", "CropOffsetRows", "CropOffsetCols", "TotalOffsetRows", "TotalOffsetCols"};
            outputFileName = "ReferenceFeatures.csv";
            CSVPrinter referenceFeaturesPrinter = getOutputCSVPrinter(outputFileName, referenceFeaturesOutputHeaders);

            String[] segmentCentroidOutputHeaders = new String[]{"FrameId", "CentroidX", "CentroidY"};
            outputFileName = "SegmentCentroid.csv";
            CSVPrinter segmentCentroidPrinter = getOutputCSVPrinter(outputFileName, segmentCentroidOutputHeaders);

            String[] sizeandShapeFeaturesOutputHeaders = new String[]{"FrameId", "Perimeter", "MaxWidth", "Length", "HeadCurvPtRow", "HeadCurvPtCol", "CurvHead", "CurvTail", "Fatness", "Thickness", "Area"};
            outputFileName = "SizeandShapeFeatures.csv";
            CSVPrinter sizeandShapeFeaturesPrinter = getOutputCSVPrinter(outputFileName, sizeandShapeFeaturesOutputHeaders);

            String[] trajectoryFeaturesOutputHeaders = new String[]{"FrameId", "DirectionCode", "DeltaTime", "DeltaX", "DeltaY", "DeltaDist", "VectorAngle", "InstantVelocity", "InstantAccel", "CumDist", "Range", "GblCentroidRow", "GblCentroidCol", "SktpMovement"};
            outputFileName = "TrajectoryFeatures.csv";
            CSVPrinter trajectoryFeaturesPrinter = getOutputCSVPrinter(outputFileName, trajectoryFeaturesOutputHeaders);

            Iterator<String> frameIdItr = frameIdList.iterator();
            if(frameIdItr.hasNext()){
                System.out.println("FIhasnext");
            }
            Iterator<CSVRecord> masterFileDataRecordItr = masterFileDataRecordParser.iterator(); 
            if(masterFileDataRecordItr.hasNext()){
                System.out.println("MFhasnext");
            }
            while (masterFileDataRecordItr.hasNext()) {
                CSVRecord record = masterFileDataRecordItr.next();
                String frameId = frameIdItr.next();
                System.out.println(frameId);
                
                createExperimentalFeatures(experimentalFeaturesPrinter, record, frameId, experimentalFeaturesOutputHeaders);
            }
            
            experimentalFeaturesPrinter.close();
            headTailInfoPrinter.close();
            postureFeaturesPrinter.close();
            rawMovementFeaturesPrinter.close();
            referenceFeaturesPrinter.close();
            segmentCentroidPrinter.close();
            sizeandShapeFeaturesPrinter.close();
            trajectoryFeaturesPrinter.close();
            
            System.out.println("========= MF related tables creation completed ==========");
        } catch (IOException ex) {
            Logger.getLogger(DatabaseFileParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private CSVPrinter getOutputCSVPrinter(String fileName, String[] headers) throws IOException {
        String outputFilePath = OUTPUT_PATH.replace("*****", CATAGORY_NAME) + "\\" + fileName;
        File outputFile = new File(outputFilePath);
        outputFile.createNewFile();
        Appendable outputFileWriter = new FileWriter(outputFilePath);
        CSVPrinter outputCSVPrinter = CSVFormat.DEFAULT.withHeader(headers).print(outputFileWriter);
        return outputCSVPrinter;
    }

    private void createExperimentalFeatures(CSVPrinter printer, CSVRecord masterFileDataRecord, String frameId, String[] headers) throws IOException {
        List<String> outputFileData = new ArrayList<>();
        outputFileData.add(frameId);
        outputFileData.add(masterFileDataRecord.get("Ixx"));
        outputFileData.add(masterFileDataRecord.get("Iyy"));
        outputFileData.add(masterFileDataRecord.get("Ixy"));
        printer.printRecord(outputFileData);
        printer.flush();
    }

    public static void main(String[] args) {
        DatabaseFileParser dbp = new DatabaseFileParser();
        dbp.createAllDBTables();
    }
}
