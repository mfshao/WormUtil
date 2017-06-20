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

import java.util.ArrayList;

/**
 *
 * @author MSHAO1
 */
public class RLEFeature {

    private String state;
    private int[][] pMatrix;
    private final ArrayList<RLEData> rleList;

    private int nr;
    private int np;
    private int maxCount;

    // Features
    private float sre;
    private float lre;
    private float lare;
    private float hare;
    private float srlae;
    private float srhae;
    private float lrlae;
    private float lrhae;
    private float aln;
    private float rln;
    private float rp;
    
    // Output
    private String[] output = new String[0];

    public RLEFeature(String title, ArrayList<RLEData> rleList) {
        this.state = title;
        this.rleList = rleList;

        calculateMaxCount();
        calculateNP();
        calculatePMatrix();
        calculateNR();
        calculateAll();
    }

    private void calculateMaxCount() {
        for (RLEData r : rleList) {
            if (maxCount < r.getCount()) {
                maxCount = r.getCount();
            }
        }
        maxCount++;
    }

    private void calculatePMatrix() {
        this.pMatrix = new int[5][maxCount];
        for (RLEData r : rleList) {
            pMatrix[Integer.parseInt(r.getPosture())][r.getCount()]++;
        }
    }

    private void calculateNR() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < maxCount; j++) {
                if (pMatrix[i][j] != 0) {
                    nr++;
                }
            }
        }
    }

    private void calculateNP() {
        np = rleList.size();
    }

    private void calculateAll() {
        calculateSRE();
        calculateLRE();
        calculateLARE();
        calculateHARE();
        calculateSRLAE();
        calculateSRHAE();
        calculateLRLAE();
        calculateLRHAE();
        calculateALN();
        calculateRLN();
        calculateRP();
    }

    public void calculateSRE() {
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= maxCount; j++) {
                if (pMatrix[i-1][j-1] != 0) {
                    sre += pMatrix[i-1][j-1] / Math.pow((double) j, 2.0);
                }
            }
        }
        sre /= nr;
    }

    public void calculateLRE() {
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= maxCount; j++) {
                if (pMatrix[i-1][j-1] != 0) {
                    lre += pMatrix[i-1][j-1] * Math.pow((double) j, 2.0);
                }
            }
        }
        lre /= nr;
    }

    public void calculateLARE() {
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= maxCount; j++) {
                if (pMatrix[i-1][j-1] != 0) {
                    lare += pMatrix[i-1][j-1] / Math.pow((double) i, 2.0);
                }
            }
        }
        lare /= nr;
    }

    public void calculateHARE() {
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= maxCount; j++) {
                if (pMatrix[i-1][j-1] != 0) {
                    hare += pMatrix[i-1][j-1] * Math.pow((double) i, 2.0);
                }
            }
        }
        hare /= nr;
    }

    public void calculateSRLAE() {
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= maxCount; j++) {
                if (pMatrix[i-1][j-1] != 0) {
                    srlae += pMatrix[i-1][j-1] / (Math.pow((double) i, 2.0) * Math.pow((double) j, 2.0));
                }
            }
        }
        srlae /= nr;
    }

    public void calculateSRHAE() {
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= maxCount; j++) {
                if (pMatrix[i-1][j-1] != 0) {
                    srhae += (pMatrix[i-1][j-1] * Math.pow((double) i, 2.0)) / Math.pow((double) j, 2.0);
                }
            }
        }
        srhae /= nr;
    }
    
    public void calculateLRLAE() {
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= maxCount; j++) {
                if (pMatrix[i-1][j-1] != 0) {
                    lrlae += (pMatrix[i-1][j-1] * Math.pow((double) j, 2.0)) / Math.pow((double) i, 2.0);
                }
            }
        }
        lrlae /= nr;
    }
    
    public void calculateLRHAE() {
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= maxCount; j++) {
                if (pMatrix[i-1][j-1] != 0) {
                    lrhae += pMatrix[i-1][j-1] * Math.pow((double) i, 2.0) * Math.pow((double) j, 2.0);
                }
            }
        }
        lrhae /= nr;
    }
    
    public void calculateALN() {
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= maxCount; j++) {
                if (pMatrix[i-1][j-1] != 0) {
                    aln += Math.pow((double) pMatrix[i-1][j-1], 2.0);
                }
            }
        }
        aln /= nr;
    }
    
    public void calculateRLN() {
        for (int j = 1; j <= maxCount; j++) {
            for (int i = 1; i <= 5; i++) {
                if (pMatrix[i-1][j-1] != 0) {
                    rln += Math.pow((double) pMatrix[i-1][j-1], 2.0);
                }
            }
        }
        rln /= nr;
    }
    
    public void calculateRP() {
        rp = nr / np;
    }
    
    public String[] toOutput() {
        ArrayList<String> outputList = new ArrayList();
        outputList.add(state);
        outputList.add(Float.toString(sre));
        outputList.add(Float.toString(lre));
        outputList.add(Float.toString(lare));
        outputList.add(Float.toString(hare));
        outputList.add(Float.toString(srlae));
        outputList.add(Float.toString(srhae));
        outputList.add(Float.toString(lrlae));
        outputList.add(Float.toString(lrhae));
        outputList.add(Float.toString(aln));
        outputList.add(Float.toString(rln));
        outputList.add(Float.toString(rp));
        output = outputList.toArray(output);
        return output;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int[][] getpMatrix() {
        return pMatrix;
    }

    public void setpMatrix(int[][] pMatrix) {
        this.pMatrix = pMatrix;
    }

    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public int getNp() {
        return np;
    }

    public void setNp(int np) {
        this.np = np;
    }

    public float getSre() {
        return sre;
    }

    public void setSre(float sre) {
        this.sre = sre;
    }

    public float getLre() {
        return lre;
    }

    public void setLre(float lre) {
        this.lre = lre;
    }

    public float getLare() {
        return lare;
    }

    public void setLare(float lare) {
        this.lare = lare;
    }

    public float getHare() {
        return hare;
    }

    public void setHare(float hare) {
        this.hare = hare;
    }

    public float getSrlae() {
        return srlae;
    }

    public void setSrlae(float srlae) {
        this.srlae = srlae;
    }

    public float getSrhae() {
        return srhae;
    }

    public void setSrhae(float srhae) {
        this.srhae = srhae;
    }

    public float getLrlae() {
        return lrlae;
    }

    public void setLrlae(float lrlae) {
        this.lrlae = lrlae;
    }

    public float getLrhae() {
        return lrhae;
    }

    public void setLrhae(float lrhae) {
        this.lrhae = lrhae;
    }

    public float getAln() {
        return aln;
    }

    public void setAln(float aln) {
        this.aln = aln;
    }

    public float getRln() {
        return rln;
    }

    public void setRln(float rln) {
        this.rln = rln;
    }

    public float getRp() {
        return rp;
    }

    public void setRp(float rp) {
        this.rp = rp;
    }

}
