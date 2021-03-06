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
public class PoolingLayer implements Layer{

    /**
     * @return the pooling_featureMap
     */
    public PoolingFeatureMap[] getPooling_featureMap() {
        return pooling_featureMap;
    }

    /**
     * @param pooling_featureMap the pooling_featureMap to set
     */
    public void setPooling_featureMap(PoolingFeatureMap[] pooling_featureMap) {
        this.pooling_featureMap = pooling_featureMap;
    }
    Vector<double[][]> input_data;
    Vector <double[][]> featureMap;
    private PoolingFeatureMap pooling_featureMap[];
     Vector<Integer> con_lab[];
     Vector <DetleMap>DetleMap;
     String act_type;
    int p_y_size;
    int p_x_size;
    int size;
    PoolingLayer(String act_type,int size,int p_y_size,int p_x_size,Vector<Integer> con_lab[])
    {
        this.p_y_size=p_y_size;
        this.p_x_size=p_x_size;
        this.size=size;
        pooling_featureMap=new PoolingFeatureMap[size];
        featureMap=new Vector<double[][]>();
        this.con_lab=con_lab;
        this.act_type=act_type;
         
        for (int i=0;i<size;i++) {
          
           PoolingFeatureMap temp_p=new PoolingFeatureMap(act_type,p_y_size,p_x_size,"max");
            pooling_featureMap[i]=temp_p;
        }
       
    }
    @Override
    public void ForwardPropagation(Vector input) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        this.input_data=input;
        featureMap=new Vector<double[][]>();
       
        for(int i=0;i<size;i++)
        {
            pooling_featureMap[i].ForwardPropagation(input_data.elementAt(i));
            featureMap.add(pooling_featureMap[i].futuremap);
        }
        this.featureMap=featureMap;
    }

    

    

    @Override
    public void BackPropagation(String next_layer_type, ConvolutionFeatureMap input_convolution_featureMap[]) {
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if (next_layer_type.equals("convolution")) {
              DetleMap=new Vector <DetleMap>();
            for (int i = 0; i < pooling_featureMap.length; i++) {
                Vector<double[][]> temp_input_detle = new Vector();
                Vector<double[][]> temp_input_kernel = new Vector();
                for (Iterator<Integer> it = con_lab[i].iterator(); it.hasNext();) {
                    int temp_index = it.next();
                    temp_input_detle.add((double[][]) input_convolution_featureMap[temp_index].detle_map.detlemap);
                    temp_input_kernel.add(input_convolution_featureMap[temp_index].kernel.getKernel_map());
                }
                pooling_featureMap[i].BackPropagation(next_layer_type, temp_input_detle, temp_input_kernel);
                 DetleMap.add(pooling_featureMap[i].detle_map);
            }
            
          
        }
        else
        {
              throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

    @Override
    public void BackPropagation(String next_layer_type, PoolingFeatureMap[] output_data) {
       
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if(next_layer_type.equals("pooling")){
              DetleMap=new Vector <DetleMap>();
         for (int i = 0; i < pooling_featureMap.length; i++) {
              Vector<double[][]> temp_input_detle = new Vector();
              temp_input_detle.add(output_data[i].detle_map.detlemap);
              double B=output_data[i].B;
                pooling_featureMap[i].BackPropagation(next_layer_type, temp_input_detle, B);
             DetleMap.add(pooling_featureMap[i].detle_map);
         }
        }else
        {
              throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
    public void BackPropagation(String next_layer_type,Vector<double[][]> input_detle) {
       
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if(next_layer_type.equals("full")){
           DetleMap=new Vector <DetleMap>();
        int count=0;
            for (Iterator<double[][]> it = input_detle.iterator(); it.hasNext();) {
              double ans[][]=it.next();
                pooling_featureMap[count].BackPropagation(next_layer_type, ans);
              DetleMap.add(pooling_featureMap[count++].detle_map);
          }
        }
    }

    @Override
    public Vector<DetleMap> getDetleMaps() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Vector<double[][]> getFeatureMaps() {
       return featureMap;
    }

    @Override
    public String getLaterType() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return "pooling";
    }

  
    

  
    
}
