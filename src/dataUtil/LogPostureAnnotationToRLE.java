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
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MSHAO1
 */
public class LogPostureAnnotationToRLE {

    private static final String IN_FILE_NAME = "D:\\ContourAndSkel.csv";
    private static final String OUT_FILE_NAME = "D:\\ContourAndSkel_output_RLEFeatures.csv";
    private static final String[] STATES = {"Forward-NTD", "Forward-Sharp", "Forward-Shallow", "Backward-ReverseShort", "Backward-ReverseLong", "Stopped-ReverseShort", "Stopped-ReverseLong", "Stopped-Stop"};

    private static ArrayList<PostureAnnotation> loadData() {
        try {
            PostureCSVData.Data data = PostureCSVData.getCSVData(IN_FILE_NAME);
            ArrayList<PostureAnnotation> csvData = data.csvData;
            return csvData;
        } catch (IOException ex) {
            Logger.getLogger(LogPostureAnnotation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void main(String[] args) {
//        HashMap<String, ArrayList<RLEData>> allRLEList = new HashMap();
//        HashMap<String, Integer> maxCount = new HashMap();
//        int nr = 0;
//        int np = 0;

        try {
            CSVWriter csv = new CSVWriter(new FileWriter(OUT_FILE_NAME), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
        String[] row = new String[0];

        ArrayList<RLEFeature> rleFeatureList = new ArrayList();
        ArrayList<PostureAnnotation> csvData = loadData();

        for (PostureAnnotation pa : csvData) {
            String title = pa.getTitle();
            ArrayList<String> value = pa.getValue();
            ArrayList<RLEData> rleList = new ArrayList();
            String pValue = "";

            for (String v : value) {
                if (!v.equalsIgnoreCase(pValue)) {
                    RLEData rleData = new RLEData(v, 1);
                    rleList.add(rleData);
                } else {
                    RLEData rleData = rleList.get(rleList.size() - 1);
                    rleData.setCount(rleData.getCount() + 1);
                }
                pValue = v;
            }

            rleFeatureList.add(new RLEFeature(title, rleList));

            ArrayList<String> result = new ArrayList();
            result.add(title);

            for (RLEData r : rleList) {
                result.add(r.getPosture());
                result.add(Integer.toString(r.getCount()));
            }

            row = result.toArray(new String[0]);
            
            
//                csv.writeNext(row);
        }
//            csv.close();

//        } catch (IOException ex) {
//            Logger.getLogger(LogPostureAnnotation.class.getName()).log(Level.SEVERE, null, ex);
//        }
        for (RLEFeature f : rleFeatureList) {
            String[] output = f.toOutput();
            csv.writeNext(output);
//            for(String s : output) {
//                System.out.print(s + "  ");
//            }
//            System.out.println();
//            System.out.println("=======================================================================");
        }
            csv.close();
        } catch (IOException ex) {
            Logger.getLogger(LogPostureAnnotation.class.getName()).log(Level.SEVERE, null, ex);
        }
//        for (String state : STATES) {
//            if (maxCount.get(state) != null) {
//                maxCount.put(state, maxCount.get(state) + 1);
//
//                System.out.println("Processing " + state);
//
//                // Building P matrix
//                int[][] pMatrix = new int[5][maxCount.get(state)];
//                ArrayList<RLEData> rleList = allRLEList.get(state);
//                for (RLEData r : rleList) {
//                    pMatrix[Integer.parseInt(r.getPosture())][r.getCount()]++;
//                }
//                System.out.println("Printing pMatrix");
//                for (int i = 0; i < 5; i++) {
//                    for (int j = 0; j < maxCount.get(state); j++) {
//                        System.out.print(pMatrix[i][j] + " ");
//                    }
//                    System.out.print("\n");
//                }
//                System.out.print("\n");
//
//                // Claculating nr
//                System.out.println("Printing nr and np");
//                for (int i = 0; i < 5; i++) {
//                    for (int j = 0; j < maxCount.get(state); j++) {
//                        if (pMatrix[i][j] != 0) {
//                            nr++;
//                        }
//                    }
//                }
//                System.out.println("nr: " + nr + ", np: " + np);
//                System.out.print("\n");
//
//                // Calculating SRE
//                System.out.println("Printing SRE");
//                float SRE = 0.0f;
//                for (int i = 0; i < 5; i++) {
//                    for (int j = 0; j < maxCount.get(state); j++) {
//                        if (pMatrix[i][j] != 0) {
//                            SRE += pMatrix[i][j] / Math.pow((double) j, 2.0);
//                        }
//                    }
//                }
//                SRE /= nr;
//                System.out.println("SRE: " + SRE);
//                System.out.print("\n");
//
//                // Calculating LRE
//                System.out.println("Printing LRE");
//                float LRE = 0.0f;
//                for (int i = 0; i < 5; i++) {
//                    for (int j = 0; j < maxCount.get(state); j++) {
//                        if (pMatrix[i][j] != 0) {
//                            LRE += pMatrix[i][j] * Math.pow((double) j, 2.0);
//                        }
//                    }
//                }
//                LRE /= nr;
//                System.out.println("LRE: " + LRE);
//                System.out.print("\n");
//
//                System.out.println("Ended");
//            }
//
//            System.out.println("==========================================================");
//            System.out.print("\n");
//        }
    }
}
