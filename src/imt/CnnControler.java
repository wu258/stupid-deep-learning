/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imt;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author wu2588
 */
public class CnnControler {

    Vector<Layer> sturct;

    CnnControler(Vector<Layer> sturct) {
        this.sturct = sturct;
    }

    public void ForwardPropagation(Vector<double[][][]> input) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Boolean flag = false;
        Vector<double[][]> front_output = null;
        for (Iterator<Layer> sit = sturct.iterator(); sit.hasNext();) {
            if (!flag) {
                sit.next().ForwardPropagation(input);
                front_output = sit.next().getFeatureMaps();
                flag = true;
            } else {
                sit.next().ForwardPropagation(front_output);
                front_output = sit.next().getFeatureMaps();
            }
        }

    }

    public void BackPropagation(Vector<Double> target) {
        for (int i = sturct.size() - 1; i >= 0; i--) {
            if (i == sturct.size() - 1) {
                Vector<double[][]> full_connect_out = sturct.elementAt(i).getFeatureMaps();
                Vector<double[][]> temp_set_detle = new Vector<double[][]>();
                double temp_detle[] = new double[full_connect_out.size()];
                for (int j = 0; j < full_connect_out.size(); j++) {
                    temp_detle[j] = (target.elementAt(j) - full_connect_out.elementAt(j)[0][0]);
                    double temp_detle_map[][] = new double[0][0];
                    temp_detle_map[0][0] = temp_detle[j];
                    temp_set_detle.add(temp_detle_map);
                    sturct.elementAt(i).BackPropagation("full", temp_set_detle);
                }
            } else {
                String next_Layer_type = sturct.elementAt(i + 1).getLaterType();
                if (next_Layer_type.equals("Convolution")) {
                    ConvolutionFeatureMap temp_input[] = ((ConvolutionLayer) sturct.elementAt(i + 1)).getConvolution_featureMap();
                    sturct.elementAt(i).BackPropagation("convolution", temp_input);
                } else if (next_Layer_type.equals("pooling")) {
                    PoolingFeatureMap temp_input[] = ((PoolingLayer) sturct.elementAt(i + 1)).getPooling_featureMap();
                    sturct.elementAt(i).BackPropagation("pooling", temp_input);
                }

            }

        }
    }

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
                double max = -999999999;
                double min = 999999999;
                for (int x = 0; x < bufImg.getHeight(); x++) {
                    for (int y = 0; y < bufImg.getWidth(); y++) {
                        max = max(ans[d][x][y], max);
                        min = min(ans[d][x][y], min);
                    }
                }
                for (int x = 0; x < bufImg.getHeight(); x++) {
                    for (int y = 0; y < bufImg.getWidth(); y++) {
                        double temp = ans[d][x][y];
                        ans[d][x][y] = (temp - min) / (max - min);
                    }
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ans;
    }

    public Vector<TrainingDate> readfile(String filepath, int output_num) {
        File file = new File(filepath);
        // get the folder list   
        Vector<TrainingDate> ans = new Vector<TrainingDate>();
        File[] array = file.listFiles();
        for (int i = 0; i < array.length; i++) {
            String file_name = array[i].getName();
            int target = file_name.charAt(0) - '0';
            File temp_file = array[i].getAbsoluteFile();
            double temp[][][] = getDate(temp_file, 28, 28);

            TrainingDate td = new TrainingDate(temp, target);
            ans.add(td);
        }
        return ans;
    }

    public Boolean isCorrect(Vector<double[][]> model_output, int tar) {
        double max = -999999999;
        int index = -1;
        int count = 0;
        for (Iterator<double[][]> it = model_output.iterator(); it.hasNext();) {

            if (max <= it.next()[0][0]) {
                max = max(max, it.next()[0][0]);
                index = count;
            }
            count++;
        }
        if (tar != index) {
            return false;
        }
        return true;
    }

    public double TestSetAccuracy(String file_floder, int output_num) {
        Vector<TrainingDate> text_data = readfile(file_floder, output_num);
        int count = 0;
        for (Iterator<TrainingDate> it = text_data.iterator(); it.hasNext();) {
            TrainingDate td = it.next();
            ForwardPropagation(td.featureMap);
            Vector<double[][]> model_output = sturct.lastElement().getFeatureMaps();
            Boolean b = isCorrect(model_output, td.target);
            if (b) {
                count++;
            }

        }
        return count / text_data.size();
    }
    public Vector<Double> setupTarget(int input,int output_num)
    {
        Vector<Double> temp=new Vector<Double>();
        for(int i=0;i<output_num;i++)
        {
            if(i!=input)
            {
                temp.add(0.0);
            }
            else
            {
                temp.add(1.0);
            }
        }
        return temp;
    }
    public void startTraining(String file_floder, int output_num) {

        Vector<TrainingDate> text_data = readfile(file_floder, output_num);
        while (true) {
            int count = 0;
            for (Iterator<TrainingDate> it = text_data.iterator(); it.hasNext();) {
                TrainingDate td = it.next();
                ForwardPropagation(td.featureMap);
                Vector<double[][]> model_output = sturct.lastElement().getFeatureMaps();
                Boolean b = isCorrect(model_output, td.target);
                if (b) {
                    count++;
                }

            }
        }
    }
}
