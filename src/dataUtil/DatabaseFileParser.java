
package dataUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 *
 * @author MSHAO1
 */


public class DatabaseFileParser {
    static final String MASTER_FILE_PATH = "\\\\CDM-MEDIXSRV\\Nematodes\\data\\*****\\matlab\\masterFile.csv";
    static final String KYLE_PATH = "\\\\CDM-MEDIXSRV\\Nematodes\\data\\*****\\data";
    static final String OUTPUT_PATH = "\\\\CDM-MEDIXSRV\\Nematodes\\data\\*****\\dbtables";
    static final String CATAGORY_NAME = "N2_nf4";
    static final String[] TABLE_NAMES = new String[] {"Straintype", "ImageInfo", "ExperimentalFeatures", "FeatureLog", "HeadTailInfo", "LogDat", "RawMovementFeatures", "MovementFeaturesBinned", "Occupancy", "PostureFeatures", "ReferenceFeatures", "SegmentCentroid", "SizeandShapeFeatures", "Tracker", "TrajectoryFeatures", "VideoInfo"};
    
    public DatabaseFileParser() {
        new File(OUTPUT_PATH.replace("*****", CATAGORY_NAME)).mkdir();
    }
    
    private void createAllDBTables() {
        createStraintype();
        createImageInfo();
    }
    
    private void createStraintype() {
        try {
            final String[] outputHeaders = new String[] {"StrainTypeid", "WormType", "ResolutionType", "FoodCondition", "SIndex"};
            final String outputFileName = "Straintype.csv";
            final String outputFilePath = OUTPUT_PATH.replace("*****", CATAGORY_NAME) + "\\" + outputFileName;
            File outputFile = new File(outputFilePath);
            outputFile.createNewFile();

            final Appendable outputFileWriter = new FileWriter(outputFilePath);
            final CSVPrinter printer = CSVFormat.DEFAULT.withHeader(outputHeaders).print(outputFileWriter);
            
            String[] decompositedCName = CATAGORY_NAME.split("_");
            String strainTypeid = "";
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
                
                strainTypeid = wormType + "_" + resolutionType + "_" + foodCondition + "_" + sIndex;
            } else {
                System.out.println("CATAGORY_NAME parse fails");
                return;
            }
            
            List<String> outputFileData = new ArrayList<>();
            outputFileData.add(strainTypeid);
            outputFileData.add(wormType);
            outputFileData.add(resolutionType);
            outputFileData.add(foodCondition);
            outputFileData.add(sIndex);
            printer.printRecord(outputFileData);
            printer.flush();
            
            System.out.println("========= StrainTypeId creat completed ==========");
        } catch (IOException ex) {
            Logger.getLogger(DatabaseFileParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void createImageInfo() {
        
    }
    
    public static void main(String[] args) {
        DatabaseFileParser dbp = new DatabaseFileParser();
        dbp.createAllDBTables();
    }
}
