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

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MSHAO1
 */
public class PostureLoopCount {
    private static final String FILE_NAME = "\\\\MEDIXSRV\\Nematodes\\data\\*****\\matlab\\ContourAndSkel.csv";
    
    public static void main(String[] args) {
        try {
            String catagory = "AIB_HR_nf10";
            String matPath = FILE_NAME.replace("*****", catagory);
            CSVReader csvr = new CSVReader(new FileReader(matPath));
            int[] counter = new int[4];
            String[] row = null;
            csvr.readNext();
            boolean prevIsLoop = false;
            boolean isLoop = false;
            int count = 0;
            
            while ((row = csvr.readNext())!=null) {
                double elapsedTime = 0.0;
                try {
                    elapsedTime    = Double.parseDouble(row[2]);
                }catch(Exception e) {
                    
                }
                
                if (row[36].equalsIgnoreCase("1")){
                    isLoop = true;
                } else {
                    isLoop = false;
                }
                
                if( !isLoop && prevIsLoop) {
                    count++;
//                    System.out.println(row[36]);
                    if (elapsedTime < 300) {
                        counter[0] = count;
                        counter[1] = count;
                        counter[2] = count;
                    } else if (elapsedTime < 600) {
                        counter[1] = count;
                        counter[2] = count;
                    } else if (elapsedTime < 900) {
                        counter[2] = count;
                    }
                }
                counter[3] = count;
                if (!row[36].isEmpty()){
                    prevIsLoop = isLoop;
                } 
            }
            
            for(int i : counter) {
                System.out.print(i+" ");
                
            }
            System.out.println();
            
        } catch (IOException ex) {
            Logger.getLogger(LogPostureAnnotation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println();
    }
}
