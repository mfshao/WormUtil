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
package graphicsUtil;

/**
 *
 * @author MSHAO1
 */
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class MyImage {

    public static void main(String args[]) throws IOException {
        int width = 1280;    //width of the image
        int height = 960;   //height of the image
        BufferedImage image = null;
        File f = null;

        //read image
        try {
            int count = 0;
            f = new File("D:\\testdata\\0027913.jpeg"); //image file path
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            image = ImageIO.read(f);
            

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    //get pixel value
                    int p = image.getRGB(i, j);

                    //get alpha
                    int a = (p >> 24) & 0xff;

                    //get red
                    int r = (p >> 16) & 0xff;

                    //get green
                    int g = (p >> 8) & 0xff;

                    //get blue
                    int b = p & 0xff;
                    int gray = (r + g + b) / 3;
                    if(gray < 255*0.6){
                    System.out.print(gray);
                    count++;
                    System.out.print(" ");
                    }else{
//                        System.out.print("***");
                    }
//                    System.out.print(" ");
                }
                System.out.println();
            }

            System.out.println("Count = "+ count);
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }

    }//main() ends here
}//class ends here
