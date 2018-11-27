
import imt.Kernel;
import java.io.IOException;
import java.util.Vector;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author wu2588
 */
 
public class NewClass {
    static void print_matrix(double in[][])
    {
        for(int i=0;i<in.length;i++)
        {
                 for(int j=0;j<in[0].length;j++)
                 {
                     System.out.print(in[i][j]+" ");
                 }
                  System.out.println();
        }
    }
     public static void main(String[] args){
         Kernel k=new Kernel(1,5,5);
         Vector<double[][][]> v=new Vector();
         double input[][][]=new double[3][28][28];
         for(int d=0;d<input.length;d++)
             for(int i=0;i<input[0].length;i++)
                 for(int j=0;j<input[0][0].length;j++)
                 {
                     input[d][i][j]=j;
                 }
         v.add(input);
         k.ForwardPropagation(v);
         print_matrix(k.getFeature_map());
         
     }
}
