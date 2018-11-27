/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imt;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Math.max;
import static java.lang.Math.min;
import java.text.NumberFormat;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author wu2588
 */
public class TEST {

    public double[][][] getDate(File input, int high, int width) {
        double ans[][][] = new double[3][high][width];
        try {
            BufferedImage bufImg = new BufferedImage(high, width, BufferedImage.TYPE_INT_RGB);
            bufImg = ImageIO.read(input);
            for (int x = 0; x < bufImg.getHeight(); x++) {
                for (int y = 0; y < bufImg.getWidth(); y++) {
                    Color c = new Color(bufImg.getRGB(x, y));
                    int red = c.getRed();
                    int green = c.getGreen();
                    int blue = c.getBlue();
                    ans[0][x][y] = red;
                    ans[1][x][y] = green;
                    ans[2][x][y] = blue;
                }
            }
            for (int d = 0; d < ans.length; d++) {
                double max=-999999999;
                double min=999999999;
                for (int x = 0; x < bufImg.getHeight(); x++) {
                    for (int y = 0; y < bufImg.getWidth(); y++) {
                         max =max(ans[d][x][y],max);
                         min=min(ans[d][x][y],min);
                    }
                }
                      for (int x = 0; x < bufImg.getHeight(); x++) {
                    for (int y = 0; y < bufImg.getWidth(); y++) {
                        double temp=ans[d][x][y];
                         ans[d][x][y]=(temp-min)/(max-min);
                    }
                }
            }

            }catch (IOException ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        }
            return ans;
        }

    

    public Vector <TrainingDate> readfile(String filepath) {
        File file = new File(filepath);
        // get the folder list   
        File[] array = file.listFiles();
        for (int i = 0; i < array.length; i++) {
            String file_name = array[i].getName();
            File temp_file = array[i].getAbsoluteFile();
            
        }
    }
}
