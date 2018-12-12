/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imt;

import static imt.TEST.getConIndex;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author wu2588
 */
public class BasicAlgorithm {

    static public double[][] convolution(double input[][], double kernel[][])//卷积
    {
        int k_l=kernel.length;
        int k_w=kernel[0].length;
        int temp_k_l=k_l;
        //System.out.println("assdsdsdsdsasd"+kernel.length);
        double ans[][]=new double[input.length-k_l+1][input[0].length-k_w+1];
        
        int ans_y=0;
       
        while(temp_k_l<= input.length)
      {
           int ans_x=0;
        for(int i=k_w;i<=input[0].length;i++)
        {
            int t_i=0;
            double total=0;
            for(int y=temp_k_l-k_l;y<temp_k_l;y++)
            {
              int t_j=0;
                for(int x=i-k_w;x<i;x++)
                {
                   total+=(input[y][x]*kernel[t_i][t_j]);
                  t_j++;
                }
                 t_i++;
            }
           ans[ans_y][ans_x]=total;
            ans_x++;
        }
        temp_k_l++;
        ans_y++;
       }
        return ans;
    }

    static public double[][][] convolution(double input[][][], double kernel[][][])//三维卷积
    {
        int k_l = kernel[0].length;
        int k_w = kernel[0][0].length;
        int temp_k_l = k_l;
        //System.out.println("assdsdsdsdsasd"+kernel.length);
        double ans[][][] = new double[input.length][input[0].length - k_l + 1][input[0][0].length - k_w + 1];
        for (int d = 0; d < input.length; d++) {
            ans[d] = convolution(input[d], kernel[d]);
        }
        return ans;
    }

    static public double[][][] matrix_random_init(double in[][][]) {
        Random random = new Random();
        double temp[][][] = new double[in.length][in[0].length][in[0][0].length];
        for (int d = 0; d < in.length; d++) {
            for (int i = 0; i < temp[0].length; i++) {
                for (int j = 0; j < temp[0][0].length; j++) {
                    temp[d][i][j] = random.nextGaussian()/10;
                }
            }
        }
        return temp;
    }

    static public double[][] matrix_random_init(double in[][]) {
        Random random = new Random();
        double temp[][] = new double[in.length][in[0].length];

        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[0].length; j++) {
                temp[i][j] = random.nextGaussian()/10;
            }
        }

        return temp;
    }

    static public double[][] rot180(double input[][])//矩阵旋转
    {
        double ans[][] = new double[input.length][input[0].length];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length; j++) {
                ans[input.length - i - 1][input[0].length - j - 1] = input[i][j];
            }
        }
        return ans;
    }

    static public double[][] Kronecker(double A[][], double B[][]) {
        double ans[][] = new double[A.length * B.length][A[0].length * B[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                for (int B_i = 0; B_i < B.length; B_i++) {
                    for (int B_j = 0; B_j < B[0].length; B_j++) {
                        ans[i * B.length + B_i][j * B[0].length + B_j] = A[i][j] * B[B_i][B_j];
                    }
                }
            }
        }
        return ans;
    }

    static public double[][] up(double input[][], int n)//矩阵扩展 上采样
    {
        double one[][] = new double[n][n];
        one = matrix_adding(one, 1);
        double ans[][] = Kronecker(input, one);
        for (int i = 0; i < ans.length; i++) {
            for (int j = 0; j < ans[0].length; j++) {
                ans[i][j] = ans[i][j];//因为均值池化 灵敏度map需要平均
            }
        }

        return ans;
    }

    static public double[][] conv2(double input[][], double kernel[][], String type)//conv2
    {
        double map[][];
        double rot180_kernel[][] = kernel;
        double ans[][] = null;
        if (type.equals("full"))//full模式
        {
            map = pedding(kernel.length - 1, kernel[0].length - 1, input);
            ans = convolution(map, rot180_kernel);

        }
        if (type.equals("valid"))//valid模式
        {

            ans = convolution(input, rot180_kernel);

        }
        return ans;
    }

    static public double[][] dot_product(double A[][], double B[][])//矩阵点乘
    {
        double ans[][] = new double[A.length][A[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                ans[i][j] = A[i][j] * B[i][j];
            }
        }
        return ans;
    }

    static public double[][] dot_product(double A[][], double B)//矩阵点乘
    {
        double ans[][] = new double[A.length][A[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                ans[i][j] = A[i][j] * B;
            }
        }
        return ans;
    }
    static public double[][][] dot_product(double A[][][], double B)//矩阵点乘
    {
        double ans[][][] = new double[A.length][A[0].length][A[0][0].length];
        for(int d=0;d<A.length;d++)
        for (int i = 0; i < A[0].length; i++) {
            for (int j = 0; j < A[0][0].length; j++) {
                ans[d][i][j] = A[d][i][j] * B;
            }
        }
        return ans;
    }
    static public double[][] pedding(int adding_y_count, int adding_x_count, double input[][])//矩阵填0
    {
        double ans[][] = new double[input.length + 2 * adding_y_count][input[0].length + 2 * adding_x_count];
        for (int i = adding_y_count; i < adding_y_count + input.length; i++) {
            for (int j = adding_x_count; j < adding_x_count + input[0].length; j++) {
                ans[i][j] = input[i - adding_y_count][j - adding_x_count];
            }
        }
        return ans;
    }

    static double Sigmoid(double in)////Sigmoid
    {

        return 1 / (1 + Math.exp(-in));

    }

    static double Sigmoid_derivative(double in) {
        return Sigmoid(in) * (1 - Sigmoid(in));
    }

    public static double relu(double in)//relu
    {
        if (in > 0) {
            return in;
        } else {
            return 0.01 * in;
        }
    }

    public static double relu_derivative(double in)//relu求导
    {
        if (in > 0) {
            return 1.0;
        }

        return 0.01;
    }

    static public double[][] matrix_relu(double A[][]) {
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                A[i][j] = relu(A[i][j]);
            }
        }
        return A;

    }

    static public double[][] matrix_derivative(String type, double A[][]) {
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                if (type.equals("relu")) {
                    A[i][j] = relu_derivative(A[i][j]);
                } else if (type.equals("Sigmoid")) {
                    A[i][j] = Sigmoid_derivative(A[i][j]);
                } else {
                    throw new UnsupportedOperationException("WRONG TYPE"); //To change body of generated methods, choose Tools | Templates.
                }
            }
        }
        return A;

    }

    static public double sumMatrixElement(double in[][]) {
        double ans = 0;
        for (int i = 0; i < in.length; i++) {
            for (int j = 0; j < in[0].length; j++) {
                ans += in[i][j];
            }
        }
        return ans;
    }

    static public double[][][] matrix_adding(double A[][][], double B[][][])//矩阵相加
    {
        double temp[][][] = new double[A.length][A[0].length][A[0][0].length];
        for (int d = 0; d < A.length; d++) {
            for (int i = 0; i < temp[0].length; i++) {
                for (int j = 0; j < temp[0][0].length; j++) {
                    temp[d][i][j] = (A[d][i][j] + B[d][i][j]);

                }
            }
        }
        return temp;
    }

    static public double[][] matrix_adding(double A[][], double B[][])//矩阵相加
    {
        double temp[][] = new double[A.length][A[0].length];

        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[0].length; j++) {
                temp[i][j] = (A[i][j] + B[i][j]);

            }
        }

        return temp;
    }

    static public double[][] matrix_adding(double A[][], double B)//矩阵加常数
    {
        double temp[][] = new double[A.length][A[0].length];
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[0].length; j++) {
                temp[i][j] = (A[i][j] + B);

            }
        }
        return temp;
    }

    static public double[][] subsample(double in[][], int y_size, int x_size, String type) {
        double ans[][] = new double[in.length / y_size][in[0].length / x_size];
        int y_count = 0;

        for (int i = y_size; i <= in.length; i += y_size) {
            int x_count = 0;
            for (int j = y_size; j <= in[0].length; j += x_size) {
                double max = -999999999;
                for (int y = i - y_size; y < i; y++) {
                    for (int x = j - x_size; x < j; x++) {
                        if (max < in[y][x]) {
                            max = in[y][x];
                        }
                    }
                }

                ans[y_count][x_count] = max;
                x_count++;
            }

            y_count++;
        }
        return ans;
    }
    
    static void print_matrix(double in[][]) {
        for (int i = 0; i < in.length; i++) {
            for (int j = 0; j < in[0].length; j++) {
                System.out.print(in[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException {

        /*double input[][]=new double[2][2];
         //for(int d=0;d<input.length;d++)
             for(int i=0;i<input.length;i++)
                 for(int j=0;j<input[0].length;j++)
                 {
                     input[i][j]=i+j;
                 }
         print_matrix(input); 
        double a[][] = new double[][]{{1, 2, 4, 7, 8, 9},
        {2, 6, 4, 3, 7, 8},
        {6, 4, 5, 7, 2, 3},
        {3, 4, 9, 7, 6, 3},
        {4, 5, 7, 4, 6, 4},
        {6, 5, 6, 2, 1, 0},
        {7, 8, 9, 10, 28, 76},
        {45, 33, 66, 7, 8, 8},
        {4, 8, 6, 4, 6, 7}};
        double b[][] = new double[][]{
            {-1, -1, -1},
            {-1, 9, -1},
            {-1, -1, -1}};
        print_matrix(convolution(a, b));
*/
         ConIndexSet cis = getConIndex(25, 1, 1);
          Vector<Layer> sturct=new Vector<Layer>();
           
             ConIndexSet cis2 =getConIndex(2, 10, 10);
          ConvolutionLayer cl1 = new ConvolutionLayer(25, 3, 1, 1, cis.con_lab,cis2.front_con_lab, "relu");
          cis = getConIndex(2, 10, 10);
          
            //ConvolutionLayer cl2 = new ConvolutionLayer(2, 1, 1, 1, cis.con_lab,cis.front_con_lab,"sigmoid");
            sturct.add(cl1);
            int layer_num_list[]={1,2};
             FullConnectLayer fl=new FullConnectLayer("sigmoid", layer_num_list, 25,layer_num_list);
            //sturct.add(cl2);
            sturct.add(fl);
            CnnControler cl=new CnnControler(sturct);
            
            Vector <double[][][]> featureMap=new  Vector <double[][][]>();
            double out[][][]=new double[3][1][1];
            out[0][0][0]=1;
              out[1][0][0]=0;
                out[2][0][0]=0;
            featureMap.add(out);
            TrainingDate td=new TrainingDate(out,0);
              featureMap=new  Vector <double[][][]>();
               double out1[][][]=new double[3][1][1];
            out1[0][0][0]=1;
              out1[1][0][0]=0;
                out1[2][0][0]=1;
            featureMap.add(out);
            TrainingDate td2=new TrainingDate(out1,1);
            Vector<TrainingDate> text_data=new Vector<TrainingDate>();
            text_data.add(td);
            text_data.add(td2);
            cl.text_startTraining(text_data, 2);
    }
}
