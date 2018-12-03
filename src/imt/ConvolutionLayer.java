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
public class ConvolutionLayer implements Layer {

    /**
     * @return the convolution_featureMap
     */
    public ConvolutionFeatureMap[] getConvolution_featureMap() {
        return convolution_featureMap;
    }

    /**
     * @param convolution_featureMap the convolution_featureMap to set
     */
    public void setConvolution_featureMap(ConvolutionFeatureMap[] convolution_featureMap) {
        this.convolution_featureMap = convolution_featureMap;
    }

    int featureMap_num;
    private ConvolutionFeatureMap convolution_featureMap[];
    Vector<Integer> con_lab[];
    public Vector<Integer> front_con_lab[];
    int k_deep;
    int k_y_size;
    int k_x_size;
    Vector<double[][]> featureMap;
    Vector<double[][]> input;
    Vector<DetleMap> DetleMap;
    double a=1;
    ConvolutionLayer(int featureMap_num, int k_deep, int k_y_size, int k_x_size, Vector<Integer> con_lab[], String act_type) {
        this.featureMap_num = featureMap_num;
        convolution_featureMap = new ConvolutionFeatureMap[featureMap_num];
        this.k_deep = k_deep;
        this.k_y_size = k_y_size;
        this.k_x_size = k_x_size;
        this.con_lab = con_lab;
        featureMap = new Vector<double[][]>();
        for (int i = 0; i < featureMap_num; i++) {
            convolution_featureMap[i] = new ConvolutionFeatureMap(k_deep, k_y_size, k_x_size, act_type);
        }
    }
     ConvolutionLayer(int featureMap_num, int k_deep, int k_y_size, int k_x_size, Vector<Integer> con_lab[],Vector<Integer> front_con_lab[], String act_type) {
        this.featureMap_num = featureMap_num;
        this.front_con_lab=front_con_lab;
        convolution_featureMap = new ConvolutionFeatureMap[featureMap_num];
        this.k_deep = k_deep;
        this.k_y_size = k_y_size;
        this.k_x_size = k_x_size;
        this.con_lab = con_lab;
        featureMap = new Vector<double[][]>();
        for (int i = 0; i < featureMap_num; i++) {
            convolution_featureMap[i] = new ConvolutionFeatureMap(k_deep, k_y_size, k_x_size, act_type);
        }
    }

    @Override
    public void ForwardPropagation(Vector input) {
        featureMap = new Vector<double[][]>();
        this.input = input;
        if (k_deep > 1) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            for (int i = 0; i < convolution_featureMap.length; i++) {
                Vector<double[][][]> temp_input = new Vector();
                for (Iterator<Integer> it = con_lab[i].iterator(); it.hasNext();) {
                    int temp_index = it.next();
                    temp_input.add((double[][][]) input.elementAt(temp_index));
                }
                convolution_featureMap[i].mul_deep_ForwardPropagation(temp_input);
                featureMap.add(convolution_featureMap[i].futuremap);
            }
        } else {
            for (int i = 0; i < convolution_featureMap.length; i++) {
                Vector<double[][]> temp_input = new Vector();
                for (Iterator<Integer> it = con_lab[i].iterator(); it.hasNext();) {
                    int temp_index = it.next();
                  //  System.out.println(temp_index);
                    temp_input.add((double[][]) input.elementAt(temp_index));
                }
                convolution_featureMap[i].ForwardPropagation(temp_input);
                featureMap.add(convolution_featureMap[i].futuremap);
            }
        }
    }

    @Override
    public void BackPropagation(String next_layer_type, ConvolutionFeatureMap input_convolution_featureMap[]) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if (next_layer_type.equals("convolution")) {
            DetleMap = new Vector<DetleMap>();
            for (int i = 0; i < convolution_featureMap.length; i++) {
                Vector<double[][]> temp_input_detle = new Vector();
                Vector<double[][]> temp_input_kernel = new Vector();
                for (Iterator<Integer> it = front_con_lab[i].iterator(); it.hasNext();) {
                    int temp_index = it.next();
                    temp_input_detle.add((double[][]) input_convolution_featureMap[temp_index].detle_map.detlemap);
                    temp_input_kernel.add(input_convolution_featureMap[temp_index].kernel.getKernel_map());
                }
                convolution_featureMap[i].BackPropagation(next_layer_type, temp_input_detle, temp_input_kernel);
                DetleMap.add(convolution_featureMap[i].detle_map);
            }
        }
    }

    @Override
    public void BackPropagation(String next_layer_type, PoolingFeatureMap[] output_data) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if (next_layer_type.equals("pooling")) {
            DetleMap = new Vector<DetleMap>();
            for (int i = 0; i < convolution_featureMap.length; i++) {
                Vector<double[][]> temp_input_detle = new Vector();
                temp_input_detle.add(output_data[i].detle_map.detlemap);
                double B = output_data[i].B;
                convolution_featureMap[i].BackPropagation(next_layer_type, temp_input_detle, B);
                DetleMap.add(convolution_featureMap[i].detle_map);
            }
        }
    }

    @Override
    public void BackPropagation(String next_layer_type, Vector<double[][]> input_detle) {////jhjh

        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if (next_layer_type.equals("full")) {
            DetleMap = new Vector<DetleMap>();
            int count = 0;
            for (int i=0;i<input_detle.size(); i++) {
                double ans[][] =input_detle.elementAt(i);
                convolution_featureMap[i].BackPropagation(next_layer_type, ans);
                DetleMap.add(convolution_featureMap[i].detle_map);
              
            }
        }
    }

    @Override
    public Vector<double[][]> getDetleMaps() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Vector<double[][]> getFeatureMaps() {
        return featureMap;
    }

    @Override
    public String getLaterType() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        a=2;
        return "Convolution";
    }

}
