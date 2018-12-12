/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imt;

import static imt.CnnControler.setN;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Math.max;
import static java.lang.Math.min;
import java.text.NumberFormat;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author wu2588
 */
public class TEST {

    public static ConIndexSet getConIndex(int out_num, int range, int mod_number) {
        Vector<Integer> con_lab[] = new Vector[out_num];
        Vector<Integer> front_con_lab[] = new Vector[range];
        for (int i = 0; i < range; i++) {
            front_con_lab[i] = new Vector<Integer>();
        }
        int count = 0;
        for (int i = 0; i < out_num; i++) {
            con_lab[i] = new Vector<Integer>();
            for (int j = 0; j < mod_number; j++) {
                front_con_lab[count].add(i);
                con_lab[i].add(count++);
                if (count >= range) {
                    count = 0;
                }
            }
        }
        return new ConIndexSet(con_lab, front_con_lab);
    }

    public static void main(String[] args) throws IOException {
        Vector<Layer> sturct=new Vector<Layer>();
      
        ConIndexSet cis1 = getConIndex(6, 1, 1);
        ConvolutionLayer cl1 = new ConvolutionLayer(6, 3, 5, 5, cis1.con_lab, "relu");
         ConIndexSet cis2 = getConIndex(16, 6, 6);
        PoolingLayer p1 = new PoolingLayer("relu", 6, 2, 2, cis2.front_con_lab);
         ConIndexSet cis3 = getConIndex(16, 4, 4);
        ConvolutionLayer cl2 = new ConvolutionLayer(16, 1, 5, 5, cis3.con_lab, "relu");
         ConIndexSet cis4 = getConIndex(144, 16, 16);
        PoolingLayer p2 = new PoolingLayer("relu", 16, 2, 2, cis4.front_con_lab);
         ConIndexSet cis5 = getConIndex(144, 16, 16);
        ConIndexSet cis6 =getConIndex(84, 144, 144);
        ConvolutionLayer cl3 = new ConvolutionLayer(144, 1, 5, 5, cis5.con_lab,cis6.front_con_lab, "relu");
        
        /*
        ConvolutionLayer cl4 = new ConvolutionLayer(84, 1, 1, 1, cis.con_lab,cis2.front_con_lab,"sigmoid");
        cis = getConIndex(2, 84, 84);
        
        ConvolutionLayer cl5 = new ConvolutionLayer(2, 1, 1, 1, cis.con_lab,cis.front_con_lab,"sigmoid");
*/
        int layer_num_list[]={1,2};
         FullConnectLayer fl=new FullConnectLayer("sigmoid", layer_num_list, 144,layer_num_list);
        sturct.add(cl1);
        sturct.add(p1);
        sturct.add(cl2);
        sturct.add(p2);
        sturct.add(cl3);
        sturct.add(fl);
        CnnControler cl=new CnnControler(sturct);
        setN(0.00001);
        cl.startTraining("C:\\Users\\wu2588\\Desktop\\1", 2);//file path,output number.
    }

}
