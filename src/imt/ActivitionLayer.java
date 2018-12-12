/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imt;

import static java.lang.Math.*;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author wu2588
 */
public class ActivitionLayer {

    String type;
    double input_map[][];
    double feature_map[][];
    double derivative_map[][];

    public double relu(double in) {
         if (in > 0) {
            return in;
        } else {
            return 0.25*in;
        }
    }

    public double relu_derivative(double in) {
        if (in > 0) {
            return 1;
        } else {
            return 0.25;
        }
    }

    public double sigmoid(double in) {
        return 1 / (1 + Math.exp(-in));
    }
     public double pooling(double in) {
        return in;
    }
      public double pooling_derivative(double in) {
        return 1;
    }
    public double sigmoid_derivative(double in) {
        return sigmoid(in) * (1 - sigmoid(in));
    }

    ActivitionLayer(String type) {
        this.type = type;
    }

    public double[][] Activation_matrix(double in[][]) {
        double ans[][] = new double[in.length][in[0].length];
        for (int i = 0; i < in.length; i++) {
            for (int j = 0; j < in[0].length; j++) {
                if (type.equals("sigmoid")) {
                    ans[i][j] = sigmoid(in[i][j]);
                } else if (type.equals("relu")) {
                    ans[i][j] = relu(in[i][j]);
                } 
                 else if(type.equals("pooling"))
                {
                    ans[i][j] = pooling(in[i][j]);
                }
                 else {
                    throw new UnsupportedOperationException("warong activation function type!"); //To change body of generated methods, choose Tools | Templates.
                }
            }
        }
        return ans;
    }

    public double[][] matrix_derivative(double in[][]) {
        double ans[][] = new double[in.length][in[0].length];
        for (int i = 0; i < in.length; i++) {
            for (int j = 0; j < in[0].length; j++) {
                if (type.equals("sigmoid")) {
                    ans[i][j] = sigmoid_derivative(in[i][j]);
                } else if (type.equals("relu")) {
                    ans[i][j] = relu_derivative(in[i][j]);
                } 
                else if(type.equals("pooling"))
                {
                    ans[i][j] = pooling_derivative(in[i][j]);
                }
                else {
                    throw new UnsupportedOperationException("warong activation function type!"); //To change body of generated methods, choose Tools | Templates.
                }
            }
        }
        return ans;
    }

    public double[][] forwardPropagation(double in[][]) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        input_map = in;
        feature_map = Activation_matrix(input_map);
        calculaeDerivative();
        return feature_map;
    }

    public double[][] calculaeDerivative() {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        derivative_map = new double[feature_map.length][feature_map[0].length];
        derivative_map = matrix_derivative(input_map);
        return derivative_map;
    }

}
