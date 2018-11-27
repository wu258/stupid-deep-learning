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
    
}
