/*
 * Copyright (C) 2017 MSHAO1
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
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
 
/**
 *
 * @author MSHAO1
 */
public class LogPostureAnnotation {
    private static final String FILE_NAME = "D:\\ContourAndSkel.csv";
    private static final String OUT_FILE_NAME = "D:\\ContourAndSkel_output.csv";
    
    public static void main(String[] args) {
        try {
            
            PostureCSVData.Data data = PostureCSVData.getCSVData(FILE_NAME);
            System.out.println();
            CSVWriter csv = new CSVWriter(new FileWriter(OUT_FILE_NAME));
            String[] row = new String[0];
            
            ArrayList<PostureAnnotation> csvData = data.csvData;
            
            for(PostureAnnotation pa : csvData){
                pa.getValue().add(0, pa.getTitle());
                row = pa.getValue().toArray(new String[0]);
                csv.writeNext(row);
            }
            csv.close();
            
        } catch (IOException ex) {
            Logger.getLogger(LogPostureAnnotation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println();
    }
}
