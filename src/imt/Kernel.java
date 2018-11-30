/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imt;

import java.util.Vector;
import static imt.BasicAlgorithm.*;
import java.util.Iterator;

/**
 *
 * @author wu2588
 */
public class Kernel {

    /**
     * @return the n
     */
    public double getN() {
        return n;
    }

    /**
     * @param n the n to set
     */
    public void setN(double n) {
        this.n = n;
    }

    /**
     * @return the deep
     */
    public int getDeep() {
        return deep;
    }

    /**
     * @param deep the deep to set
     */
    public void setDeep(int deep) {
        this.deep = deep;
    }

    /**
     * @return the y_size
     */
    public int getY_size() {
        return y_size;
    }

    /**
     * @param y_size the y_size to set
     */
    public void setY_size(int y_size) {
        this.y_size = y_size;
    }

    /**
     * @return the x_size
     */
    public int getX_size() {
        return x_size;
    }

    /**
     * @param x_size the x_size to set
     */
    public void setX_size(int x_size) {
        this.x_size = x_size;
    }

    /**
     * @return the kernel_map
     */
    public double[][] getKernel_map() {
        return kernel_map;
    }

    /**
     * @param kernel_map the kernel_map to set
     */
    public void setKernel_map(double[][] kernel_map) {
        this.kernel_map = kernel_map;
    }

    /**
     * @return the feature_map
     */
    public double[][] getFeature_map() {
        return feature_map;
    }

    /**
     * @param feature_map the feature_map to set
     */
    public void setFeature_map(double[][] feature_map) {
        this.feature_map = feature_map;
    }

    /**
     * @return the input_data
     */
    public Vector<double[][]> getInput_data() {
        return input_data;
    }

    /**
     * @param input_data the input_data to set
     */
    public void setInput_data(Vector<double[][]> input_data) {
        this.input_data = input_data;
    }

    /**
     * @return the kernel_derivative
     */
    public DetleMap getKernel_derivative() {
        return kernel_derivative;
    }

    /**
     * @param kernel_derivative the kernel_derivative to set
     */
    public void setKernel_derivative(DetleMap kernel_derivative) {
        this.kernel_derivative = kernel_derivative;
    }
    private int deep;
    private int y_size;
    private int x_size;
    private double kernel_map[][];
    double deep_kernel_map[][][];
    double mul_kernel_derivative_map[][][];
    double kernel_derivative_map[][];
    private double n;
    private double feature_map[][];
    private Vector<double[][]> input_data;
    Vector<double[][][]> deep_input_data;
    //Vector<double[][][]> output_data;
    private DetleMap kernel_derivative;
    DerivativeMap derivative_map;

    public Kernel(int deep, int y_size, int x_size) {
        this.deep = deep;
        this.y_size = y_size;
        this.x_size = x_size;
        derivative_map = new DerivativeMap(y_size, x_size);
        kernel_map = new double[y_size][x_size];
        kernel_map = matrix_random_init(kernel_map);
        if(deep>1)
        {
            deep_kernel_map=new double[deep][y_size][x_size];
            matrix_random_init(deep_kernel_map);
        }
    }

    public void ForwardPropagation(Vector input) {// need recheak
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        if (deep <=1) {
            setInput_data(input);
            int k_y = getKernel_map().length;
            int k_x = getKernel_map()[0].length;
            double temp[][] = (double[][]) input.elementAt(0);
            setFeature_map(new double[temp.length - k_y + 1][temp[0].length - k_x + 1]);
            for (Iterator<double[][]> it = getInput_data().iterator(); it.hasNext();) {
                double temp_input[][] = it.next();
                double temp_ans[][] = convolution(temp_input, getKernel_map());
                for (int i = 0; i < temp_ans.length; i++) {
                    for (int j = 0; j < temp_ans[0].length; j++) {
                        feature_map[i][j] += temp_ans[i][j];
                    }
                }
            }
        } else {
            deep_input_data = input;
            int k_y = deep_kernel_map[0].length;
            int k_x = deep_kernel_map[0][0].length;
            double temp[][][] = (double[][][]) input.elementAt(0);
            setFeature_map(new double[temp[0].length - k_y + 1][temp[0][0].length - k_x + 1]);
            for (Iterator<double[][][]> it = deep_input_data.iterator(); it.hasNext();) {
                double temp_input[][][] = it.next();
                double temp_ans[][][] = convolution(temp_input, deep_kernel_map);
                for (int d = 0; d < deep; d++) {
                    for (int i = 0; i < temp_ans.length; i++) {
                        for (int j = 0; j < temp_ans[0].length; j++) {
                            feature_map[i][j] += temp_ans[d][i][j];
                        }
                    }
                }
            }
        }
    }

    public double[][] kernel_updata() {
     
            for (int i = 0; i < kernel_map.length; i++) {
                for (int j = 0; j < kernel_map[0].length; j++) {
                    kernel_map[i][j] += getN() * kernel_derivative_map[i][j];
                }
            }
            
        return kernel_map;

    }

    public double[][][] mul_kernel_updata() {
         for (int d = 0; d < mul_kernel_derivative_map.length; d++) {
                for (int i = 0; i < kernel_map.length; i++) {
                    for (int j = 0; j < kernel_map[0].length; j++) {
                        deep_kernel_map[d][i][j] += getN() * mul_kernel_derivative_map[d][i][j];
                    }
                }
            }
        return deep_kernel_map;
    }

    public void BackPropagation(double input_detle_map[][]) {//多层卷积核对应的多层上一层的X，这里要改
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if (deep > 1) {
            mul_kernel_derivative_map = derivative_map.calculate_mul_DerivativeMap(deep_input_data, input_detle_map, "mul_kernel");
            mul_kernel_updata();
        } else {
            kernel_derivative_map = derivative_map.calculateDerivativeMap(input_data, input_detle_map, "kernel");
            kernel_updata();
        }

    }

    public DetleMap getDetleMap() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public DetleMap getFeatureMap() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
