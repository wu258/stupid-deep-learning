/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imt;

import static imt.BasicAlgorithm.*;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author wu2588
 */
public class DerivativeMap {//需要修改支持多层卷积核

    double featurMap[][];
    double mul_featurMap[][][];

    double y_size;
    double x_size;

    DerivativeMap(double y_size, double x_size) {
        this.y_size = y_size;
        this.x_size = x_size;
    }

    public double[][] calculateDerivativeMap(Vector<double[][]> input_map, double input_detle_map[][], String type) {
        if (type.equals("kernel")) {
            for (Iterator<double[][]> it = input_map.iterator(); it.hasNext();) {
                double temp[][] = it.next();
                double temp_ans[][] = conv2(temp, input_detle_map, "valid");
                if (featurMap != null) {
                    featurMap = matrix_adding(featurMap, temp_ans);
                } else {
                    featurMap = temp_ans;
                }

            }

            return featurMap;
        } else {
            throw new UnsupportedOperationException("wrong input tyle with new  kernel calculateDerivativeMap");
        }
    }

    public double[][][] calculate_mul_DerivativeMap(Vector<double[][][]> input_map, double input_detle_map[][], String type) {
        if (type.equals("mul_kernel")) {
            for (Iterator<double[][][]> it = input_map.iterator(); it.hasNext();) {
                double temp[][][] = it.next();
                double temp_ans[][][] = new double[temp.length][][];
                for (int d = 0; d < temp.length; d++) {
                    temp_ans[d] = conv2(temp[d], input_detle_map, "valid");
                }
                if (mul_featurMap != null) {
                    mul_featurMap = matrix_adding(mul_featurMap, temp_ans);
                } else {
                    mul_featurMap = new double[temp.length][temp_ans[0].length][temp_ans[0][0].length];
                }

            }

            return mul_featurMap;
        } else {
            throw new UnsupportedOperationException("wrong input tyle with new  kernel calculateDerivativeMap");
        }
    }
}
