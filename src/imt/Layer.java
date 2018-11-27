/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imt;

import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author wu2588
 */
public interface Layer {
   
   public void ForwardPropagation(Vector input);
   public void BackPropagation(String next_layer_type,ConvolutionFeatureMap convolution_featureMap[]);
   public void BackPropagation(String next_layer_type,PoolingFeatureMap output_data[]);
   public Vector<double[][]> getDetleMaps();
   public Vector<double[][]> getFeatureMaps();
      public void BackPropagation(String next_layer_type,Vector<double[][]> input_detle) ;
      public String getLaterType();
}
