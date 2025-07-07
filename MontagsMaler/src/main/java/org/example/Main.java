package org.example;

import NeuralNetwork.Module;
import GUI.Montagsmaler;

/**This is the main class where the app starts.
 *
 */
public class Main {
    /**This is the main class where the app starts.
     *
     */
    public static void main(String[] args) {
        int res = 30;
        int[] nnStructure = {40, 40, 40, 40, 10};//TODO: change
        Module nn = new Module(nnStructure, res * res);
        Montagsmaler.startApp(nn);
    }
}