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
import com.opencsv.CSVWriter;
import graphicsUtil.RLEFeatureBoxPlot;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author MSHAO1
 */
class RLEFeatureDataArray {

    private String title;
    private double SRE;
    private double LRE;
    private double LARE;
    private double HARE;
    private double SRLAE;
    private double SRHAE;
    private double LRLAE;
    private double LRHAE;
    private double ALN;
    private double RLN;
    private double RP;

    public RLEFeatureDataArray(String title) {
        this.title = title;
    }

    public RLEFeatureDataArray(String title, double SRE, double LRE, double LARE, double HARE, double SRLAE, double SRHAE, double LRLAE, double LRHAE, double ALN, double RLN, double RP) {
        this.title = title;
        this.SRE = SRE;
        this.LRE = LRE;
        this.LARE = LARE;
        this.HARE = HARE;
        this.SRLAE = SRLAE;
        this.SRHAE = SRHAE;
        this.LRLAE = LRLAE;
        this.LRHAE = LRHAE;
        this.ALN = ALN;
        this.RLN = RLN;
        this.RP = RP;
    }

    public String getTitle() {
        return title;
    }

    public double getSRE() {
        return SRE;
    }

    public void setSRE(double SRE) {
        this.SRE = SRE;
    }

    public double getLRE() {
        return LRE;
    }

    public void setLRE(double LRE) {
        this.LRE = LRE;
    }

    public double getLARE() {
        return LARE;
    }

    public void setLARE(double LARE) {
        this.LARE = LARE;
    }

    public double getHARE() {
        return HARE;
    }

    public void setHARE(double HARE) {
        this.HARE = HARE;
    }

    public double getSRLAE() {
        return SRLAE;
    }

    public void setSRLAE(double SRLAE) {
        this.SRLAE = SRLAE;
    }

    public double getSRHAE() {
        return SRHAE;
    }

    public void setSRHAE(double SRHAE) {
        this.SRHAE = SRHAE;
    }

    public double getLRLAE() {
        return LRLAE;
    }

    public void setLRLAE(double LRLAE) {
        this.LRLAE = LRLAE;
    }

    public double getLRHAE() {
        return LRHAE;
    }

    public void setLRHAE(double LRHAE) {
        this.LRHAE = LRHAE;
    }

    public double getALN() {
        return ALN;
    }

    public void setALN(double ALN) {
        this.ALN = ALN;
    }

    public double getRLN() {
        return RLN;
    }

    public void setRLN(double RLN) {
        this.RLN = RLN;
    }

    public double getRP() {
        return RP;
    }

    public void setRP(double RP) {
        this.RP = RP;
    }
}

public class RLEFeatureStatistics {

    public static void performAllStatistics() {
        try {
            List<RLEFeatureDataArray> rlefList = new ArrayList();
            String[] annotations = {"Forward-NTD", "Forward-Shallow", "Forward-Sharp", "Backward-ReverseShort", "Backward-ReverseLong", "Stopped-Stop", "Stopped-ReverseShort", "Stopped-ReverseLong"};

            CSVReader csv = new CSVReader(new FileReader("D:\\ContourAndSkel_output_RLEFeatures.csv"), ',');
            String[] row;
            while ((row = csv.readNext()) != null) {
                String title = row[0];
                RLEFeatureDataArray rlefda = new RLEFeatureDataArray(row[0], Double.parseDouble(row[1]), Double.parseDouble(row[2]), Double.parseDouble(row[3]), Double.parseDouble(row[4]), Double.parseDouble(row[5]), Double.parseDouble(row[6]), Double.parseDouble(row[7]), Double.parseDouble(row[8]), Double.parseDouble(row[9]), Double.parseDouble(row[10]), Double.parseDouble(row[11]));
                rlefList.add(rlefda);
            }
//
//            for (String a : annotations) {
//                ArrayList<ArrayList<Double>> featureList = new ArrayList();
//                ArrayList<Double> sreList = new ArrayList();
//                ArrayList<Double> lreList = new ArrayList();
//                ArrayList<Double> lareList = new ArrayList();
//                ArrayList<Double> hareList = new ArrayList();
//                ArrayList<Double> srlaeList = new ArrayList();
//                ArrayList<Double> srhaeList = new ArrayList();
//                ArrayList<Double> lrlaeList = new ArrayList();
//                ArrayList<Double> lrhaeList = new ArrayList();
//                ArrayList<Double> alnList = new ArrayList();
//                ArrayList<Double> rlnList = new ArrayList();
//                ArrayList<Double> rpList = new ArrayList();
//
//                for (RLEFeatureDataArray rlefda : rlefList) {
//                    if (rlefda.getTitle().equalsIgnoreCase(a)) {
//                        sreList.add(rlefda.getSRE());
//                        lreList.add(rlefda.getLRE());
//                        lareList.add(rlefda.getLARE());
//                        hareList.add(rlefda.getHARE());
//                        srlaeList.add(rlefda.getSRLAE());
//                        srhaeList.add(rlefda.getSRHAE());
//                        lrlaeList.add(rlefda.getLRLAE());
//                        lrhaeList.add(rlefda.getLRHAE());
//                        alnList.add(rlefda.getALN());
//                        rlnList.add(rlefda.getRLN());
//                        rpList.add(rlefda.getRP());
//                    }
//                }
//                featureList.add(sreList);
//                featureList.add(lreList);
//                featureList.add(lareList);
//                featureList.add(hareList);
//                featureList.add(srlaeList);
//                featureList.add(srhaeList);
//                featureList.add(lrlaeList);
//                featureList.add(lrhaeList);
//                featureList.add(alnList);
//                featureList.add(rlnList);
//                featureList.add(rpList);
//
//                System.out.println(a);
//
//                for (ArrayList<Double> feature : featureList) {
//                    DescriptiveStatistics stats = new DescriptiveStatistics();
//
//                    Double[] inputArray = new Double[feature.size()];
//                    inputArray = feature.toArray(inputArray);
//                    for (int i = 0; i < inputArray.length; i++) {
//                        stats.addValue(inputArray[i]);
//                    }
//
//                    System.out.println(stats.getMin());
//                    System.out.println(stats.getMean());
//                    System.out.println(stats.getMax());
//                    System.out.println(stats.getStandardDeviation());
//                    System.out.println();
//                }
//
//                System.out.println("=========================================");
//                System.out.println();
//            }

            ArrayList<ArrayList<Double>> featureList = new ArrayList();
            ArrayList<Double> sreList = new ArrayList();
            ArrayList<Double> lreList = new ArrayList();
            ArrayList<Double> lareList = new ArrayList();
            ArrayList<Double> hareList = new ArrayList();
            ArrayList<Double> srlaeList = new ArrayList();
            ArrayList<Double> srhaeList = new ArrayList();
            ArrayList<Double> lrlaeList = new ArrayList();
            ArrayList<Double> lrhaeList = new ArrayList();
            ArrayList<Double> alnList = new ArrayList();
            ArrayList<Double> rlnList = new ArrayList();
            ArrayList<Double> rpList = new ArrayList();

            for (RLEFeatureDataArray rlefda : rlefList) {
                sreList.add(rlefda.getSRE());
                lreList.add(rlefda.getLRE());
                lareList.add(rlefda.getLARE());
                hareList.add(rlefda.getHARE());
                srlaeList.add(rlefda.getSRLAE());
                srhaeList.add(rlefda.getSRHAE());
                lrlaeList.add(rlefda.getLRLAE());
                lrhaeList.add(rlefda.getLRHAE());
                alnList.add(rlefda.getALN());
                rlnList.add(rlefda.getRLN());
                rpList.add(rlefda.getRP());
            }
            featureList.add(sreList);
            featureList.add(lreList);
            featureList.add(lareList);
            featureList.add(hareList);
            featureList.add(srlaeList);
            featureList.add(srhaeList);
            featureList.add(lrlaeList);
            featureList.add(lrhaeList);
            featureList.add(alnList);
            featureList.add(rlnList);
            featureList.add(rpList);

            ArrayList<String> minList = new ArrayList<>();
            minList.add("Min");
            ArrayList<String> maxList = new ArrayList<>();
            maxList.add("Max");

            for (ArrayList<Double> feature : featureList) {
                DescriptiveStatistics stats = new DescriptiveStatistics();

                Double[] inputArray = new Double[feature.size()];
                inputArray = feature.toArray(inputArray);
                for (int i = 0; i < inputArray.length; i++) {
                    stats.addValue(inputArray[i]);
                }

                minList.add(Double.toString(stats.getMin()));
                maxList.add(Double.toString(stats.getMax()));
            }

             CSVReader csvr = new CSVReader(new FileReader("D:\\ContourAndSkel_output_RLEFeatures.csv"), ',');
//            CSVWriter csvw = new CSVWriter(new FileWriter("D:\\ContourAndSkel_output_RLEFeatures_MinMax.csv"), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
//            String[] line = minList.toArray(new String[0]);
//            csvw.writeNext(line);
//            line = maxList.toArray(new String[0]);
//            csvw.writeNext(line);
//            while ((line = csvr.readNext()) != null) {
//                csvw.writeNext(line);
//            }

        } catch (IOException ex) {
            Logger.getLogger(RLEFeatureKMPPClustering.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void performCombinedStatistics() {
        try {
            List<RLEFeatureDataArray> rlefList = new ArrayList();
            String[] annotations = {"Forward", "ReverseShort", "ReverseLong", "Stop"};

            CSVReader csv = new CSVReader(new FileReader("D:\\ContourAndSkel_output_RLEFeatures.csv"), ',');
            String[] row;

            HashMap<String, ArrayList<Double>> hm = new HashMap<>();

            while ((row = csv.readNext()) != null) {
                String title = row[0];
                if (title.contains("ReverseShort")) {
                    title = "ReverseShort";
                }
                if (title.contains("ReverseLong")) {
                    title = "ReverseLong";
                }
                if (title.contains("Forward")) {
                    title = "Forward";
                }
                if (title.equalsIgnoreCase("Stopped-Stop")) {
                    title = "Stop";
                }
                RLEFeatureDataArray rlefda = new RLEFeatureDataArray(title, Double.parseDouble(row[1]), Double.parseDouble(row[2]), Double.parseDouble(row[3]), Double.parseDouble(row[4]), Double.parseDouble(row[5]), Double.parseDouble(row[6]), Double.parseDouble(row[7]), Double.parseDouble(row[8]), Double.parseDouble(row[9]), Double.parseDouble(row[10]), Double.parseDouble(row[11]));
                rlefList.add(rlefda);
            }

            for (String a : annotations) {
                ArrayList<ArrayList<Double>> featureList = new ArrayList();
                ArrayList<Double> sreList = new ArrayList();
                ArrayList<Double> lreList = new ArrayList();
                ArrayList<Double> lareList = new ArrayList();
                ArrayList<Double> hareList = new ArrayList();
                ArrayList<Double> srlaeList = new ArrayList();
                ArrayList<Double> srhaeList = new ArrayList();
                ArrayList<Double> lrlaeList = new ArrayList();
                ArrayList<Double> lrhaeList = new ArrayList();
                ArrayList<Double> alnList = new ArrayList();
                ArrayList<Double> rlnList = new ArrayList();
                ArrayList<Double> rpList = new ArrayList();

                for (RLEFeatureDataArray rlefda : rlefList) {
                    if (rlefda.getTitle().equalsIgnoreCase(a)) {
                        sreList.add(rlefda.getSRE());
                        lreList.add(rlefda.getLRE());
                        lareList.add(rlefda.getLARE());
                        hareList.add(rlefda.getHARE());
                        srlaeList.add(rlefda.getSRLAE());
                        srhaeList.add(rlefda.getSRHAE());
                        lrlaeList.add(rlefda.getLRLAE());
                        lrhaeList.add(rlefda.getLRHAE());
                        alnList.add(rlefda.getALN());
                        rlnList.add(rlefda.getRLN());
                        rpList.add(rlefda.getRP());
                    }
                }
                featureList.add(sreList);
                featureList.add(lreList);
                featureList.add(lareList);
                featureList.add(hareList);
                featureList.add(srlaeList);
                featureList.add(srhaeList);
                featureList.add(lrlaeList);
                featureList.add(lrhaeList);
                featureList.add(alnList);
                featureList.add(rlnList);
                featureList.add(rpList);

                System.out.println(a);

                hm.put(a, hareList);

                for (ArrayList<Double> feature : featureList) {
                    DescriptiveStatistics stats = new DescriptiveStatistics();

                    Double[] inputArray = new Double[feature.size()];
                    inputArray = feature.toArray(inputArray);
                    for (int i = 0; i < inputArray.length; i++) {
                        stats.addValue(inputArray[i]);
                    }

                    System.out.println(stats.getMin());
                    System.out.println(stats.getMean());
                    System.out.println(stats.getMax());
                    System.out.println(stats.getStandardDeviation());
                    System.out.println();
                }

                System.out.println("=========================================");
                System.out.println();
            }

            RLEFeatureBoxPlot bp = new RLEFeatureBoxPlot("HARE S2 Box Plot", hm);
            bp.pack();
            RefineryUtilities.centerFrameOnScreen(bp);
            bp.setVisible(true);

        } catch (IOException ex) {
            Logger.getLogger(RLEFeatureKMPPClustering.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void performCombinedNoStopStatistics() {
        try {
            List<RLEFeatureDataArray> rlefList = new ArrayList();
            String[] annotations = {"Forward", "ReverseShort", "ReverseLong"};

            CSVReader csv = new CSVReader(new FileReader("D:\\ContourAndSkel_output_RLEFeatures.csv"), ',');
            String[] row;
            while ((row = csv.readNext()) != null) {
                String title = row[0];
                if (title.contains("Stopped")) {
                    continue;
                }
                if (title.contains("ReverseShort")) {
                    title = "ReverseShort";
                }
                if (title.contains("ReverseLong")) {
                    title = "ReverseLong";
                }
                if (title.contains("Forward")) {
                    title = "Forward";
                }
                RLEFeatureDataArray rlefda = new RLEFeatureDataArray(title, Double.parseDouble(row[1]), Double.parseDouble(row[2]), Double.parseDouble(row[3]), Double.parseDouble(row[4]), Double.parseDouble(row[5]), Double.parseDouble(row[6]), Double.parseDouble(row[7]), Double.parseDouble(row[8]), Double.parseDouble(row[9]), Double.parseDouble(row[10]), Double.parseDouble(row[11]));
                rlefList.add(rlefda);
            }

            for (String a : annotations) {
                ArrayList<ArrayList<Double>> featureList = new ArrayList();
                ArrayList<Double> sreList = new ArrayList();
                ArrayList<Double> lreList = new ArrayList();
                ArrayList<Double> lareList = new ArrayList();
                ArrayList<Double> hareList = new ArrayList();
                ArrayList<Double> srlaeList = new ArrayList();
                ArrayList<Double> srhaeList = new ArrayList();
                ArrayList<Double> lrlaeList = new ArrayList();
                ArrayList<Double> lrhaeList = new ArrayList();
                ArrayList<Double> alnList = new ArrayList();
                ArrayList<Double> rlnList = new ArrayList();
                ArrayList<Double> rpList = new ArrayList();

                for (RLEFeatureDataArray rlefda : rlefList) {
                    if (rlefda.getTitle().equalsIgnoreCase(a)) {
                        sreList.add(rlefda.getSRE());
                        lreList.add(rlefda.getLRE());
                        lareList.add(rlefda.getLARE());
                        hareList.add(rlefda.getHARE());
                        srlaeList.add(rlefda.getSRLAE());
                        srhaeList.add(rlefda.getSRHAE());
                        lrlaeList.add(rlefda.getLRLAE());
                        lrhaeList.add(rlefda.getLRHAE());
                        alnList.add(rlefda.getALN());
                        rlnList.add(rlefda.getRLN());
                        rpList.add(rlefda.getRP());
                    }
                }
                featureList.add(sreList);
                featureList.add(lreList);
                featureList.add(lareList);
                featureList.add(hareList);
                featureList.add(srlaeList);
                featureList.add(srhaeList);
                featureList.add(lrlaeList);
                featureList.add(lrhaeList);
                featureList.add(alnList);
                featureList.add(rlnList);
                featureList.add(rpList);

                System.out.println(a);

                for (ArrayList<Double> feature : featureList) {
                    DescriptiveStatistics stats = new DescriptiveStatistics();

                    Double[] inputArray = new Double[feature.size()];
                    inputArray = feature.toArray(inputArray);
                    for (int i = 0; i < inputArray.length; i++) {
                        stats.addValue(inputArray[i]);
                    }

                    System.out.println(stats.getMin());
                    System.out.println(stats.getMean());
                    System.out.println(stats.getMax());
                    System.out.println(stats.getStandardDeviation());
                    System.out.println();
                }

                System.out.println("=========================================");
                System.out.println();
            }

        } catch (IOException ex) {
            Logger.getLogger(RLEFeatureKMPPClustering.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
//        performAllStatistics();
        performCombinedStatistics();
//        performCombinedNoStopStatistics();
    }
}
