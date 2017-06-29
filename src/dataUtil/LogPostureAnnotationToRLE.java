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

    private static final String IN_FILE_NAME = "D:\\Smooth_N2_nf4.csv";
    private static final String OUT_FILE_NAME = "D:\\Smooth_N2_nf4_RLEFeatures.csv";
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
    
    private static ArrayList<RLEFeature> normalizeRLEFeatures(ArrayList<RLEFeature> rleFeatureList) {
        float[] maxs = new float[11];
        float[] mins = new float[11];
        
        for (int i = 0 ; i < 11; i++) {
            maxs[i] = Float.MIN_VALUE;
            mins[i] = Float.MAX_VALUE;
        }
        
        rleFeatureList.stream().map((rf) -> rf.getRawValues()).forEach((rawValueList) -> {
            for (int i = 0; i < 11; i++) {
                if (maxs[i] < rawValueList.get(i)) {
                    maxs[i] = rawValueList.get(i);
                }
                if (mins[i] > rawValueList.get(i)) {
                    mins[i] = rawValueList.get(i);
                }
            }
        });
        
        rleFeatureList.stream().forEach((rf) -> {
            rf.setSre((rf.getSre() - mins[0]) / (maxs[0] - mins[0]));
            rf.setLre((rf.getLre()- mins[1]) / (maxs[1] - mins[1]));
            rf.setLare((rf.getLare() - mins[2]) / (maxs[2] - mins[2]));
            rf.setHare((rf.getHare() - mins[3]) / (maxs[3] - mins[3]));
            rf.setSrlae((rf.getSrlae() - mins[4]) / (maxs[4] - mins[4]));
            rf.setSrhae((rf.getSrhae() - mins[5]) / (maxs[5] - mins[5]));
            rf.setLrlae((rf.getLrlae() - mins[6]) / (maxs[6] - mins[6]));
            rf.setLrhae((rf.getLrhae() - mins[7]) / (maxs[7] - mins[7]));
            rf.setAln((rf.getAln() - mins[8]) / (maxs[8] - mins[8]));
            rf.setRln((rf.getRln() - mins[9]) / (maxs[9] - mins[9]));
            rf.setRp((rf.getRp() - mins[10]) / (maxs[10] - mins[10]));
        });
        
        return rleFeatureList;
    }

    public static void main(String[] args) {

        try {
            CSVWriter csv = new CSVWriter(new FileWriter(OUT_FILE_NAME), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
        String[] row = new String[0];

        ArrayList<RLEFeature> rleFeatureList = new ArrayList();
        ArrayList<PostureAnnotation> csvData = loadData();
        
        for (PostureAnnotation pa : csvData) {
            System.out.println(pa.getTitle()+"  "+pa.getValue().size());
        }

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
        }
        
//        rleFeatureList = normalizeRLEFeatures(rleFeatureList);
        
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
