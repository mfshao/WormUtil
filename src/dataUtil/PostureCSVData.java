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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author MSHAO1
 */
public class PostureCSVData {
    static class Data {
        ArrayList<PostureAnnotation> csvData;
    }

    public static Data getCSVData(String CSVLoc) throws IOException {
        Data data = new Data();
        data.csvData = new ArrayList<>();

        return getCSVData(CSVLoc, data);
    }

    private static Data getCSVData(String CSVLoc, Data data) throws IOException {
        try {
            CSVReader csv = new CSVReader(new FileReader(CSVLoc));
            String[] row = null;
            csv.readNext();

            if ((row = csv.readNext()) == null) {
                return data;
            }
//            StringBuilder sb = new StringBuilder();
            ArrayList<String> value = new ArrayList();
            String pDescription = row[53];
            
            int i = 0;
            
            while (true) {
                Integer posture = 0;
                String description = "";
                
//                System.out.println(row.length);
                
                if (!row[36].equalsIgnoreCase("") && !row[36].equalsIgnoreCase("NA")){
                    posture = Integer.parseInt(row[36]);
                }
                
                if (!row[52].equalsIgnoreCase("") && !row[53].equalsIgnoreCase("NA")){
                    description = row[53];
                }
                
                if (pDescription.equalsIgnoreCase(description)){
                    value.add(Integer.toString(posture));
                } else {
                    if(!description.equalsIgnoreCase("NA")){
                        data.csvData.add(new PostureAnnotation(pDescription, value));
                    }
                    value = new ArrayList();
                }
                
                pDescription = description;

                row = csv.readNext();

                if (row == null) {
                    break;
                }
                
                i++;
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return data;
    }
}
