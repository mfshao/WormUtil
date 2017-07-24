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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.clustering.MultiKMeansPlusPlusClusterer;

public class RLEFeatureMultiKMPPClustering {

    public static void performAllClustering() {

        try {
            List<RLEDataWrapper> clusterInput = new ArrayList<>();

            CSVReader csv = new CSVReader(new FileReader("D:\\Smooth_N2_nf4_RLEFeatures.csv"), ',');
            String[] row;
            int count = 0;
            double[] mins = new double[11];
            double[] maxs = new double[11];
            List<RLEDataWrapper> forwardList = new ArrayList<>();

            while ((row = csv.readNext()) != null) {
                String title = row[0];
                if (title.contains("Min")) {
                    for (int i = 1; i < row.length; i++) {
                        mins[i - 1] = Double.parseDouble(row[i]);
                    }
                } else if (title.contains("Max")) {
                    for (int i = 1; i < row.length; i++) {
                        maxs[i - 1] = Double.parseDouble(row[i]);
                    }
                } else {
                    double[] points = new double[row.length - 1];
                    for (int i = 1; i < row.length; i++) {
                        points[i - 1] = Double.parseDouble(row[i]);
                    }
                    if (title.equals("Forward-NTD")) {
                        forwardList.add(new RLEDataWrapper(title, points));
                    } else {
                        clusterInput.add(new RLEDataWrapper(title, points));
                        count++;
                    }
                }
            }
            forwardList = sampling(forwardList, 50);
            clusterInput.addAll(forwardList);
            count+=50;
            clusterInput = normalizing(clusterInput, mins, maxs);

            KMeansPlusPlusClusterer<RLEDataWrapper> clusterer = new KMeansPlusPlusClusterer<>(8, 1000);
            MultiKMeansPlusPlusClusterer multi = new MultiKMeansPlusPlusClusterer(clusterer, 5);
            List<CentroidCluster<RLEDataWrapper>> clusterResults = multi.cluster(clusterInput);

// output the clusters
            System.out.println("======================Level 1======================");
            System.out.println();
            for (int i = 0; i < clusterResults.size(); i++) {
                System.out.println("Cluster " + i);
                for (RLEDataWrapper rdw : clusterResults.get(i).getPoints()) {
                    System.out.println(rdw.getTitle());

                }
                System.out.println();
            }
            System.out.println("===================================================");
            System.out.println();

            List<RLEDataWrapper> clusterInput2ndLevel = new ArrayList<>();
            for (RLEDataWrapper rdw : clusterResults.get(0).getPoints()) {
                clusterInput2ndLevel.add(rdw);
            }

            System.out.println("======================Level 2======================");
            System.out.println();
            clusterer = new KMeansPlusPlusClusterer<>(8, 1000);
            multi = new MultiKMeansPlusPlusClusterer(clusterer, 1000);
            List<CentroidCluster<RLEDataWrapper>> clusterResults2ndLevel = multi.cluster(clusterInput2ndLevel);
//            int count = 0;
            for (int i = 0; i < clusterResults2ndLevel.size(); i++) {
                System.out.println("Cluster " + i);
                for (RLEDataWrapper rdw : clusterResults2ndLevel.get(i).getPoints()) {
                    System.out.println(rdw.getTitle());
                }
                System.out.println();
            }
            System.out.println("===================================================");
            System.out.println();
        } catch (IOException ex) {
            Logger.getLogger(RLEFeatureMultiKMPPClustering.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void performCombinedClustering() {
        try {
            List<RLEDataWrapper> clusterInput = new ArrayList<>();

            CSVReader csv = new CSVReader(new FileReader("D:\\ContourAndSkel_output_RLEFeatures_EW_MinMax.csv"), ',');
            String[] row;
            int count = 0;
            double[] mins = new double[11];
            double[] maxs = new double[11];
            List<RLEDataWrapper> forwardList = new ArrayList<>();
            
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
                if (title.contains("Min")) {
                    for (int i = 1; i < row.length; i++) {
                        mins[i - 1] = Double.parseDouble(row[i]);
                    }
                } else if (title.contains("Max")) {
                    for (int i = 1; i < row.length; i++) {
                        maxs[i - 1] = Double.parseDouble(row[i]);
                    }
                } else {
                    double[] points = new double[row.length - 1];
                    for (int i = 1; i < row.length; i++) {
                        points[i - 1] = Double.parseDouble(row[i]);
                    }
                    if (title.contains("Forward")) {
                        forwardList.add(new RLEDataWrapper(title, points));
                    } else {
                        clusterInput.add(new RLEDataWrapper(title, points));
                        count++;
                    }
                }
            }
            forwardList = sampling(forwardList, 50);
            clusterInput.addAll(forwardList);
            count+=50;
            clusterInput = normalizing(clusterInput, mins, maxs);

            int cutoff = Double.valueOf(count * 0.7).intValue();
            System.out.println(cutoff);

            KMeansPlusPlusClusterer<RLEDataWrapper> clusterer = new KMeansPlusPlusClusterer<>(4, 1000);
            MultiKMeansPlusPlusClusterer multi = new MultiKMeansPlusPlusClusterer(clusterer, 1000);
            List<CentroidCluster<RLEDataWrapper>> clusterResults = multi.cluster(clusterInput);

// output the clusters
            int level = 1;
            System.out.println("======================Level " + level++ + "======================");
            System.out.println();
            int maxIndex = 0;
            int maxLen = 0;
            
            for (int i = 0; i < clusterResults.size(); i++) {
                System.out.println("Cluster " + i);
                if (clusterResults.get(i).getPoints().size() > maxLen) {
                    maxIndex = i;
                    maxLen = clusterResults.get(i).getPoints().size();
                }
                for (RLEDataWrapper rdw : clusterResults.get(i).getPoints()) {
                    System.out.println(rdw.getTitle());
                }
                System.out.println();
            }
            System.out.println("===================================================");
            System.out.println();

            while (maxLen > cutoff) {
                System.out.println(maxLen);

                List<RLEDataWrapper> clusterInput2ndLevel = new ArrayList<>();
                for (RLEDataWrapper rdw : clusterResults.get(maxIndex).getPoints()) {
                    clusterInput2ndLevel.add(rdw);
                }
                maxIndex = 0;
                maxLen = 0;

                System.out.println("======================Level " + level++ + "======================");
                System.out.println();
                clusterer = new KMeansPlusPlusClusterer<>(4, 1000);
                multi = new MultiKMeansPlusPlusClusterer(clusterer, 1000);
                clusterResults = multi.cluster(clusterInput2ndLevel);
//            int count = 0;
                for (int i = 0; i < clusterResults.size(); i++) {
                    System.out.println("Cluster " + i);
                    if (clusterResults.get(i).getPoints().size() > maxLen) {
                        maxIndex = i;
                        maxLen = clusterResults.get(i).getPoints().size();
                    }
                    for (RLEDataWrapper rdw : clusterResults.get(i).getPoints()) {
                        System.out.println(rdw.getTitle());
                    }
                    System.out.println();
                }
                System.out.println("===================================================");
                System.out.println();
            }
        } catch (IOException ex) {
            Logger.getLogger(RLEFeatureMultiKMPPClustering.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void performCombinedNoStopClustering() {
        try {
            List<RLEDataWrapper> clusterInput = new ArrayList<>();

            CSVReader csv = new CSVReader(new FileReader("D:\\ContourAndSkel_output_RLEFeatures_MinMax.csv"), ',');
            String[] row;
            int count = 0;
            double[] mins = new double[11];
            double[] maxs = new double[11];
            List<RLEDataWrapper> forwardList = new ArrayList<>();
            
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
                if (title.contains("Min")) {
                    for (int i = 1; i < row.length; i++) {
                        mins[i - 1] = Double.parseDouble(row[i]);
                    }
                } else if (title.contains("Max")) {
                    for (int i = 1; i < row.length; i++) {
                        maxs[i - 1] = Double.parseDouble(row[i]);
                    }
                } else {
                    double[] points = new double[row.length - 1];
                    for (int i = 1; i < row.length; i++) {
                        points[i - 1] = Double.parseDouble(row[i]);
                    }
                    if (title.contains("Forward")) {
                        forwardList.add(new RLEDataWrapper(title, points));
                    } else {
                        clusterInput.add(new RLEDataWrapper(title, points));
                        count++;
                    }
                }
            }
            forwardList = sampling(forwardList, 50);
            clusterInput.addAll(forwardList);
            count+=50;
            clusterInput = normalizing(clusterInput, mins, maxs);

            int cutoff = Double.valueOf(count * 0.7).intValue();
            System.out.println(cutoff);

            KMeansPlusPlusClusterer<RLEDataWrapper> clusterer = new KMeansPlusPlusClusterer<>(3, 1000);
            List<CentroidCluster<RLEDataWrapper>> clusterResults = clusterer.cluster(clusterInput);

// output the clusters
            int level = 1;
            System.out.println("======================Level " + level++ + "======================");
            System.out.println();
            int maxIndex = 0;
            int maxLen = 0;
            for (int i = 0; i < clusterResults.size(); i++) {
                System.out.println("Cluster " + i);
                if (clusterResults.get(i).getPoints().size() > maxLen) {
                    maxIndex = i;
                    maxLen = clusterResults.get(i).getPoints().size();
                }
                for (RLEDataWrapper rdw : clusterResults.get(i).getPoints()) {
                    System.out.println(rdw.getTitle());
                }
                System.out.println();
            }
            System.out.println("===================================================");
            System.out.println();

            while (maxLen > cutoff) {
                System.out.println(maxLen);

                List<RLEDataWrapper> clusterInput2ndLevel = new ArrayList<>();
                for (RLEDataWrapper rdw : clusterResults.get(maxIndex).getPoints()) {
                    clusterInput2ndLevel.add(rdw);
                }
                maxIndex = 0;
                maxLen = 0;

                System.out.println("======================Level " + level++ + "======================");
                System.out.println();
                clusterer = new KMeansPlusPlusClusterer<>(3, 1000);
                clusterResults = clusterer.cluster(clusterInput2ndLevel);
//            int count = 0;
                for (int i = 0; i < clusterResults.size(); i++) {
                    System.out.println("Cluster " + i);
                    if (clusterResults.get(i).getPoints().size() > maxLen) {
                        maxIndex = i;
                        maxLen = clusterResults.get(i).getPoints().size();
                    }
                    for (RLEDataWrapper rdw : clusterResults.get(i).getPoints()) {
                        System.out.println(rdw.getTitle());
                    }
                    System.out.println();
                }
                System.out.println("===================================================");
                System.out.println();
            }
        } catch (IOException ex) {
            Logger.getLogger(RLEFeatureMultiKMPPClustering.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static List<RLEDataWrapper> normalizing(List<RLEDataWrapper> original, double[] mins, double[] maxs) {
        
        for (RLEDataWrapper rledw : original) {
            double[] points = rledw.getPoint();
            for (int i = 0; i < points.length; i++) {
                points[i] = (points[i] - mins[i]) / (maxs[i] - mins[i]);
            }
            rledw.setPoints(points);
        }
        
        return original;
    }

    private static List<RLEDataWrapper> sampling(List<RLEDataWrapper> original, int n) {
        List<RLEDataWrapper> result = new ArrayList<>();
        if (n >= original.size()) {
            return original;
        }
        if (n <= 0) {
            return result;
        }
        Collections.shuffle(original);
        result = original.subList(0, n);
        return result;
    }

    public static void main(String[] args) {
//        performAllClustering();
        performCombinedClustering();
//        performCombinedNoStopClustering();
    }
}
