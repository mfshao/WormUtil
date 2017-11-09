
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
        new File("\\\\CDM-MEDIXSRV\\Nematodes\\data\\*****\\dbtables").mkdir();
    }
    
    private void createAllDBTables() {
        createStraintype();
    }
    
    private void createStraintype() {
        try {
            final String[] outputHeaders = new String[] {"StrainTypeid", "WormType", "ResolutionType", "FoodCondition", "SIndex"};
            final String outputFileName = "Straintype.csv";
            final String outputFilePath = OUTPUT_PATH.replace("*****", CATAGORY_NAME) + "\\" + outputFileName;

            final Appendable outputFileWriter = new FileWriter(outputFilePath);
            final CSVPrinter printer = CSVFormat.DEFAULT.withHeader(MasterFileCreater.MasterFileHeaders.class).print(outputFileWriter);
            
            String[] decompositedCName = CATAGORY_NAME.split("_");
            String StrainTypeid = "";
            String WormType = "";
            String ResolutionType = "";
            String FoodCondition = "";
            String SIndex = "";
            
            List<String> outputFileData = new ArrayList<>();
            outputFileData = Arrays.asList(outputHeaders);
            printer.printRecord(outputFileData);
            printer.flush();
            outputFileData.clear();
            
        } catch (IOException ex) {
            Logger.getLogger(DatabaseFileParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        DatabaseFileParser dbp = new DatabaseFileParser();
        dbp.createAllDBTables();
    }
}
