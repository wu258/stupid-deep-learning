/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imt;

import static imt.BasicAlgorithm.matrix_adding;
import static imt.TEST.getConIndex;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author wu2588
 */
public class FullConnectLayer implements Layer {

    /**
     * @return the featureMap
     */
    public Vector<double[][]> getFeatureMap() {
        return featureMap;
    }

    /**
     * @param featureMap the featureMap to set
     */
    public void setFeatureMap(Vector<double[][]> featureMap) {
        this.featureMap = featureMap;
    }
    Vector<DetleMap> DetleMap;
    private Vector<double[][]> featureMap;
    ConvolutionLayer convolutionlayer[];
    int input_y_size;
    int input_x_size;

    FullConnectLayer(String act_type, int layer_num[], int input_size, int output_num[]) {
        input_y_size = (int) Math.sqrt(input_size);
        input_x_size = (int) Math.sqrt(input_size);
        ConIndexSet cis = getConIndex(1, 1, 1);
        ConIndexSet cis1 = getConIndex(2, 1, 1);
        convolutionlayer = new ConvolutionLayer[layer_num.length];
        convolutionlayer[0] = new ConvolutionLayer(1, 1, 1, 1, cis.con_lab, cis1.front_con_lab, "sigmoid");
        for (int i = 1; i < layer_num.length; i++) {
            cis = getConIndex(layer_num[i], layer_num[i - 1], layer_num[i - 1]);
           // ConIndexSet cis2 = getConIndex(layer_num[i], layer_num[i + 1], layer_num[i + 1]);
            convolutionlayer[i] = new ConvolutionLayer(output_num[i], 1, input_y_size, input_x_size, cis.con_lab, cis.front_con_lab, "sigmoid");
        }

    }

    @Override
    public void ForwardPropagation(Vector input) {
        setFeatureMap(new Vector<double[][]>());
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        double[][] input_map = (double[][]) input.elementAt(0);
        Vector<double[][]> in = new Vector();
        in.add(input_map);
        ConvolutionFeatureMap[] convolution_featureMap = new ConvolutionFeatureMap[1];
        convolution_featureMap[0] = new ConvolutionFeatureMap(1, 1, 1, "sigmoid");
        convolution_featureMap[0].futuremap = input_map;
        double in_dir_maix[][] = new double[input_y_size][input_x_size];
        convolution_featureMap[0].activition_layer.derivative_map = matrix_adding(in_dir_maix, 1.0);
        convolutionlayer[0].featureMap = in;
        convolutionlayer[0].setConvolution_featureMap(convolution_featureMap);
        Vector<double[][]> front_output = null;
        Boolean flag = false;
        front_output = convolutionlayer[0].getFeatureMaps();
        for (int i = 1; i < convolutionlayer.length; i++) {
            convolutionlayer[i].ForwardPropagation(front_output);
            front_output = convolutionlayer[i].getFeatureMaps();
        }
        setFeatureMap(front_output);
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
    public Vector<DetleMap> getDetleMaps() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return convolutionlayer[0].DetleMap;
    }

    @Override
    public Vector<double[][]> getFeatureMaps() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return getFeatureMap();
    }

    @Override
    public String getLaterType() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return "full";
    }

    @Override
    public void BackPropagation(String next_layer_type, Vector<double[][]> input_detle) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        // convolutionlayer.BackPropagation(next_layer_type, input_detle);
        if (next_layer_type.equals("full")) {
            for (int i = convolutionlayer.length - 1; i >= 0; i--) {
                if (i == convolutionlayer.length - 1) {
                    convolutionlayer[i].BackPropagation("full", input_detle);
                } else {
                    ConvolutionFeatureMap temp_input[] = ((ConvolutionLayer) convolutionlayer[i + 1]).getConvolution_featureMap();
                    convolutionlayer[i].BackPropagation("full_convolution", temp_input);
                }
            }
        }
        DetleMap= convolutionlayer[0].DetleMap;
    }

}
