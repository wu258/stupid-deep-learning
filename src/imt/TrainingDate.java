/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imt;

/**
 *
 * @author wu2588
 */
public class TrainingDate {

    double featureMap[][][];
    int target;

    TrainingDate(double[][][] input, int tar) {
        this.featureMap = input;
        this.target = tar;
    }
}
