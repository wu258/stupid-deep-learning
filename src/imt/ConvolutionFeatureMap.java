/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imt;

import static imt.BasicAlgorithm.dot_product;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author wu2588
 */
public class ConvolutionFeatureMap {

    int k_y_size;
    int k_x_size;
    int k_deep;
    Kernel kernel;
    private Vector<double[][][]> mlu_input_data;
      private Vector<double[][]> input_data;
    DetleMap detle_map;
   
    double futuremap[][];
    double pre_futuremap[][];
    
    String activation_type;
    ActivitionLayer activition_layer;
    ConvolutionFeatureMap(int k_deep, int k_y_size, int k_x_size,String act_type) {
        this.k_y_size = k_y_size;
        this.k_x_size = k_x_size;
        kernel = new Kernel(k_deep, k_y_size, k_x_size);
        activition_layer=new ActivitionLayer(act_type);
        
    }

    public void mul_deep_ForwardPropagation(Vector<double[][][]> input) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        this.mlu_input_data=input;
        kernel.ForwardPropagation(input);
        pre_futuremap = kernel.getFeature_map();
         futuremap =activition_layer.forwardPropagation(pre_futuremap);
    }
    public void ForwardPropagation(Vector<double[][]> input) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        this.input_data=input;
        kernel.ForwardPropagation(input);
        pre_futuremap = kernel.getFeature_map();
         futuremap =activition_layer.forwardPropagation(pre_futuremap);
    }
   
    public void BackPropagation(String next_layer_type,Vector<double[][]>input_detle_map,double B) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
          detle_map=new DetleMap(futuremap.length,futuremap[0].length);
          if(next_layer_type.equals("pooling"))
          {
              detle_map.calculateDetleMap(next_layer_type, input_detle_map, activition_layer.derivative_map, B);
          }
          kernel.BackPropagation(detle_map.detlemap);
       
    }
     public void BackPropagation(String next_layer_type, Vector<double[][]> input_detle_map, Vector<double[][]> kernle) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
          detle_map=new DetleMap(futuremap.length,futuremap[0].length);
          if(next_layer_type.equals("convolution"))
          {
              detle_map.calculateDetleMap(next_layer_type, input_detle_map, kernle, activition_layer.derivative_map);
          }
          kernel.BackPropagation(detle_map.detlemap);
    }
     public void BackPropagation(String next_layer_type,double[][] input_detle_map) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        detle_map = new DetleMap(futuremap.length, futuremap[0].length);
        if (next_layer_type.equals("full")) {
            detle_map.detlemap= dot_product(input_detle_map,activition_layer.derivative_mapdsdsds);
           
        }
        kernel.BackPropagation(detle_map.detlemap);
    }
    public DetleMap getDetleMap() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public DetleMap getFeatureMap() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
