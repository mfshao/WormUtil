/*
 * Copyright (C) 2016 MSHAO1
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
package statUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Application;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 *
 * @author MSHAO1
 */
class Result {

    ArrayList<int[]> resultList;
    int maxValue;

    public Result(ArrayList<int[]> resultList, int maxValue) {
        this.resultList = resultList;
        this.maxValue = maxValue;
    }

    public ArrayList<int[]> getResultList() {
        return resultList;
    }

    public int getMaxValue() {
        return maxValue;
    }
}

public class CameraMovementAnalysis {

    private static Result readCSV(String csvFile) {
        String line;
        String cvsSplitBy = ",";
        int max = Integer.MIN_VALUE;
        ArrayList<int[]> result = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] values = line.split(cvsSplitBy);
                int[] moves = new int[values.length];
                for (int i = 0; i < values.length; i++) {
                    moves[i] = Integer.parseInt(values[i]);
                    if (moves[i] > max) {
                        max = moves[i];
                    }
                }
                result.add(moves);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Result(result, max);
    }

    private static void calculateDS(ArrayList<int[]> result, String s) {
        int size = result.get(0).length;
        int[] count = new int[size];
        int[] aggrate = new int[size];
        for (int i = 0; i < result.size(); i++) {
            int[] temp = result.get(i);
            for (int j = 0; j < size; j++) {
                if (temp[j] != -1) {
                    count[j]++;
                    aggrate[j] += temp[j];
                }
            }
        }

        double[] aggavg = new double[size];
        System.out.println("========== Aggrated result of " + s + " ==========");
        for (int j = 0; j < size; j++) {
            aggavg[j] = (double) aggrate[j] / (double) count[j];
//            aggavg[j] = (double) aggrate[j];
//            System.out.print(aggavg[j] + " ");
        }
        System.out.println();

        DescriptiveStatistics ds = new DescriptiveStatistics(aggavg);
        System.out.println("========== Statistics result of " + s + " ==========");
        System.out.println("Mean: " + ds.getMean() + ", Stddev: " + ds.getStandardDeviation() + ", Kurtosis: " + ds.getKurtosis() + ", Skew: " + ds.getSkewness());
        System.out.println("============================================");
        System.out.println();
        System.out.println();

        BarChartDS.setData(aggavg, s);
        Application.launch(BarChartDS.class);
    }

    public static void calculateMC(ArrayList<int[]> result, int max, String s) {
        int size = result.get(0).length;
        max = 32;
        int[] count = new int[max + 1];
        int[] divide = new int[max + 1];
        double total = 0;
        for (int i = 0; i < result.size(); i++) {
            int[] temp = result.get(i);
            for (int j = 0; j < size; j++) {
                if (temp[j] != -1) {
                    count[temp[j]]++;
                    total++;
                    if (divide[temp[j]] < i + 1) {
                        divide[temp[j]]++;
                    }
                }

            }
        }
        //double[] doubles = Arrays.stream(count).asDoubleStream().toArray();
        double[] doubles = new double[max + 1];
        System.out.println("========== Statistics result of " + s + " ==========");
        for (int j = 0; j < divide.length; j++) {
//            if (divide[j] != 0) {
//                doubles[j] = (double) count[j] / (double) divide[j];
//            } 
            doubles[j] = (double) count[j] / total * 100.0;
//            System.out.print(count[j]+ " "+ doubles[j] + " ; " );
        }
        System.out.print(total);
        System.out.println();

        DescriptiveStatistics ds = new DescriptiveStatistics(Arrays.copyOfRange(doubles, 1, 13));
        System.out.println("========== Statistics result of " + s + " ==========");
        System.out.println("Mean: " + ds.getMean() + ", Stddev: " + ds.getStandardDeviation() + ", Kurtosis: " + ds.getKurtosis() + ", Skew: " + ds.getSkewness());
        System.out.println("============================================");
        System.out.println();
        System.out.println();

        BarChartMC.setData(doubles, s);
        Application.launch(BarChartMC.class);
    }

    public static void main(String[] args) {
//        if (args.length < 1) {
//            System.out.println("no input");
//            exit(1);
//        }
//        String[] fileNames = {"CHE2_LR_F", "CHE2_LR_NF_q", "CHE2_HR_F", "CHE2_HR_NF"};
//        String[] fileNames = {"CHE2_HR_NF"};

//        String[] fileNames = {"AIB_LR_NF", "AIB_HR_NF", "AIB_HR_F"};
        String[] fileNames = {"AIB_HR_NF"};
        for (String s : fileNames) {
            Result result = readCSV("C:\\Users\\MSHAO1\\Desktop\\" + s + ".csv");
            ArrayList<int[]> resultList = result.getResultList();
//            calculateDS(resultList, s);
            calculateMC(result.getResultList(), result.getMaxValue(), s);
        }

    }
}
