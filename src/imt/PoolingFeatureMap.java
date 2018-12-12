/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imt;

import java.util.Vector;
import static imt.BasicAlgorithm.*;
import static imt.CnnControler.n;
import java.util.Random;

/**
 *
 * @author wu2588
 */
public class PoolingFeatureMap {

    double b;
    int deep;
    int p_y_size;
    int p_x_size;
    DetleMap detle_map;
    double futuremap[][];
    double pre_futuremap[][];
    double downmap[][];
    String activation_type;
    ActivitionLayer activition_layer;
    String type;
    double B;
    Random random = new Random();

    PoolingFeatureMap(String act_type, int p_ysize, int p_x_size, String type) {
        this.p_y_size = p_ysize;
        this.p_x_size = p_x_size;
        this.type = type;
        activition_layer = new ActivitionLayer(act_type);
        b = random.nextGaussian();
        B = random.nextGaussian();
    }

    public void ForwardPropagation(double input[][]) {//need changde
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        futuremap = subsample(input, p_y_size, p_x_size, type);
        downmap = futuremap;
        futuremap = dot_product(futuremap, B);
        futuremap = matrix_adding(futuremap, b);
        pre_futuremap = futuremap;
        futuremap = activition_layer.forwardPropagation(pre_futuremap);
    }

    public void BackPropagation(String next_layer_type, Vector<double[][]> input_detle_map, double B) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        detle_map = new DetleMap(futuremap.length, futuremap[0].length);
        if (next_layer_type.equals("pooling")) {
            detle_map.calculateDetleMap(next_layer_type, input_detle_map, activition_layer.derivative_map, B);
        }
        updata_b();
        updata_B();
    }

    public void BackPropagation(String next_layer_type, Vector<double[][]> input_detle_map, Vector<double[][]> kernle) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        detle_map = new DetleMap(futuremap.length, futuremap[0].length);
        if (next_layer_type.equals("convolution")) {
            detle_map.calculateDetleMap(next_layer_type, input_detle_map, kernle, activition_layer.derivative_map);
        }
        updata_b();
        updata_B();
    }

    public void BackPropagation(String next_layer_type,double[][] input_detle_map) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        detle_map = new DetleMap(futuremap.length, futuremap[0].length);
        if (next_layer_type.equals("full")) {
            detle_map.detlemap= dot_product(input_detle_map,activition_layer.derivative_map);
           
        }
        updata_b();
        updata_B();
    }

    public double updata_b() {
        b -= (n*sumMatrixElement(detle_map.detlemap));
        return b;
    }

    public double updata_B() {
        double ans[][] = dot_product(downmap, detle_map.detlemap);
        B -= (n*sumMatrixElement(ans));
        
        return B;
    }

    public DetleMap getDetleMap() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public DetleMap getFeatureMap() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
