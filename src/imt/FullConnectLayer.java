/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imt;

import static imt.TEST.getConIndex;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author wu2588
 */
public class FullConnectLayer implements Layer{
    ConvolutionLayer convolutionlayer;
    FullConnectLayer(String act_type,int input_size,int output_num,int layer_num)
    {
        int input_y_size= (int) Math.sqrt(input_size);
        int input_x_size=(int) Math.sqrt(input_size);
        ConIndexSet cis = getConIndex(2, 1, 1);
        convolutionlayer=new ConvolutionLayer(2,1,input_y_size,input_x_size,cis.con_lab,act_type);
        
    }
    @Override
    public void ForwardPropagation(Vector input) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
       convolutionlayer.ForwardPropagation(input);
    }

    @Override
    public void BackPropagation(String next_layer_type, ConvolutionFeatureMap[] convolution_featureMap) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
       
    }

    @Override
    public void BackPropagation(String next_layer_type, PoolingFeatureMap[] output_data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Vector<double[][]> getDetleMaps() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Vector<double[][]> getFeatureMaps() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public void BackPropagation(Vector<double[][]> input_detle) {
         convolutionlayer.BackPropagation("full", input_detle);
         
    }

    @Override
    public String getLaterType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
