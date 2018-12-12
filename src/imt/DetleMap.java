/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imt;

import static imt.BasicAlgorithm.conv2;
import static imt.BasicAlgorithm.dot_product;
import static imt.BasicAlgorithm.matrix_adding;
import static imt.BasicAlgorithm.rot180;
import static imt.BasicAlgorithm.up;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author wu2588
 */
public class DetleMap {

    int channel_num;
    int x_size;
    int y_size;
    public  double detlemap[][];

    DetleMap(int y_size, int x_size) {
        this.y_size = y_size;
        this.x_size = x_size;
        detlemap = new double[y_size][x_size];
    }

    public double[][] calculateDetleMap(String next_layer_type, Vector<double[][]> input_data, double derivative_map[][], double B) {
        double ans[][] = new double[y_size][x_size];
        for (Iterator<double[][]> it = input_data.iterator(); it.hasNext();) {
            if (next_layer_type.equals("pooling")) {
                double temp[][] = it.next();
                int up_scale = ans.length / temp.length;
                temp = up(temp, up_scale);
                temp = dot_product(temp, derivative_map);
                temp=dot_product(temp,B);
                ans = matrix_adding(ans, temp);
                //ans=dot_product(ans, 1/input_data.size());
            }
            else
            {
                throw new UnsupportedOperationException("wrong input tyle with new  pooling layer"); //To change body of generated methods, choose Tools | Templates.
            }
        }
        //ans=dot_product(ans, 1/input_data.size());
        detlemap=ans;
        return ans;
    }

    public double[][] calculateDetleMap(String next_layer_type, Vector<double[][]> input_data, Vector<double[][]> kernle, double[][] derivative_map) {
        double ans[][] = new double[y_size][x_size];
        int i = 0;
        for (Iterator<double[][]> it = input_data.iterator(); it.hasNext();) {
            if (next_layer_type.equals("convolution")||next_layer_type.equals("full_convolution")) {
                double temp[][] = it.next();
                double temp_krenel[][] = rot180(kernle.elementAt(i++)); 
                temp = conv2(temp, temp_krenel, "full");
                ans = matrix_adding(ans, temp);
            }
            else
            {
                 throw new UnsupportedOperationException("wrong input tyle with new  Convolution layer");
            }
            ans = dot_product(ans, derivative_map);
        }
        //ans=dot_product(ans, 1/input_data.size());
        detlemap=ans;
        return ans;
    }
}
