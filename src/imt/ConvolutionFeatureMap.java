/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imt;

import static imt.BasicAlgorithm.dot_product;
import static imt.BasicAlgorithm.matrix_adding;
import static imt.CnnControler.n;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author wu2588
 */
public class ConvolutionFeatureMap {
    int front_index_y;
    int front_index_x;
    int k_y_size;
    int k_x_size;
    int k_deep;
    Kernel kernel;
    private Vector<double[][][]> mlu_input_data;
    private Vector<double[][]> input_data;
    DetleMap detle_map;
    double b;
    double futuremap[][];
    double pre_futuremap[][];
    Random random = new Random();
    String activation_type;
    ActivitionLayer activition_layer;
    
    ConvolutionFeatureMap(int k_deep, int k_y_size, int k_x_size, String act_type) {
        this.k_y_size = k_y_size;
        this.k_x_size = k_x_size;
        kernel = new Kernel(k_deep, k_y_size, k_x_size);
        activition_layer = new ActivitionLayer(act_type);
        b = random.nextGaussian();
    }

    public void mul_deep_ForwardPropagation(Vector<double[][][]> input) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        this.mlu_input_data = input;
        kernel.ForwardPropagation(input);
        pre_futuremap = kernel.getFeature_map();
        pre_futuremap = matrix_adding(pre_futuremap, b);
        futuremap = activition_layer.forwardPropagation(pre_futuremap);
    }

    public void ForwardPropagation(Vector<double[][]> input) {//sdssddsds
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        this.input_data = input;
        kernel.ForwardPropagation(input);
        pre_futuremap = kernel.getFeature_map();
        pre_futuremap = matrix_adding(pre_futuremap, b);
        futuremap = activition_layer.forwardPropagation(pre_futuremap);
    }

    public void BackPropagation(String next_layer_type, Vector<double[][]> input_detle_map, double B) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        detle_map = new DetleMap(futuremap.length, futuremap[0].length);
        if (next_layer_type.equals("pooling")) {
            detle_map.calculateDetleMap(next_layer_type, input_detle_map, activition_layer.derivative_map, B);
        }
        b-=n*updateB(detle_map.detlemap);
        kernel.BackPropagation(detle_map.detlemap);

    }

    public void BackPropagation(String next_layer_type, Vector<double[][]> input_detle_map, Vector<double[][]> kernle) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        detle_map = new DetleMap(futuremap.length, futuremap[0].length);
        if (next_layer_type.equals("convolution")) {
            detle_map.calculateDetleMap(next_layer_type, input_detle_map, kernle, activition_layer.derivative_map);
             b-=n*updateB(detle_map.detlemap);
        kernel.BackPropagation(detle_map.detlemap);
        }
        else if(next_layer_type.equals("full_convolution"))
        {
            detle_map.calculateDetleMap(next_layer_type, input_detle_map, kernle, activition_layer.derivative_map);
        }
       
    }

    public void BackPropagation(String next_layer_type, double[][] input_detle_map) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        detle_map = new DetleMap(futuremap.length, futuremap[0].length);
        if (next_layer_type.equals("full")) {
            detle_map.detlemap = dot_product(input_detle_map, activition_layer.derivative_map);
        }
        b-=n*updateB(detle_map.detlemap);
        kernel.BackPropagation(detle_map.detlemap);
    }

    public double updateB(double in_detle[][]) {
        double total=0;
        for (int i = 0; i < in_detle.length; i++) {
            for (int j = 0; j < in_detle[0].length; j++) {
                total+=in_detle[i][j];
            }
        }
        return total;
    }

    public DetleMap getDetleMap() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public DetleMap getFeatureMap() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
