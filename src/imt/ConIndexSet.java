/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imt;

import java.util.Vector;

/**
 *
 * @author wu2588
 */
public class ConIndexSet {
    public Vector<Integer> con_lab[];
    public Vector<Integer> front_con_lab[];
    ConIndexSet(Vector<Integer> con_lab[], Vector<Integer> front_con_lab[])
    {
        this.con_lab=con_lab;
        this.front_con_lab=front_con_lab;
    }
}
