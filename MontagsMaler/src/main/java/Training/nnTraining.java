package Training;

import NeuralNetwork.Module;
import utility.BufferedImageUtil;
import utility.ExpectedOutput;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;


/**This is the nnTraining class which is used for training the nn with given data.
 *
 */
public class nnTraining {

    /**This is the main function which is used for training the nn with given data.
     *
     */
    public static void main(String[] args) throws IOException {
        int resolution = 30;
        int[] structureNeuralNetwork = {40, 40, 40, 40, 10};//TODO: change
        Module neuralNetwork = new Module(structureNeuralNetwork, resolution * resolution);

        //get folders
        File f = new File("MontagsMaler/src/main/resources/images");//TODO: change pathname
        File[] files = f.listFiles();

        //Save weights biases when we reset the weights and biases //TODO:change
        neuralNetwork.saveWB();

        //load files from folders into an 2d-array
        File[][] imageFiles;
        assert  (files != null);
        imageFiles = new File[files.length][];
        for(int i = 0; i < files.length; i++) {
            imageFiles[i] = files[i].listFiles();
            }

        double average_cost = 0;
        Random rand = new Random();
        utility.ExpectedOutput expectedOutput = new ExpectedOutput();
        for(int i = 0; i < 10000; i++){
            System.out.println(i + 1);
            //chooses random png
            //System.out.println(imageFiles.length);
            //int rndFolder = rand.nextInt(imageFiles.length);
            //int rndPNG = rand.nextInt(imageFiles[rndFolder].length);
            //File next = imageFiles[rndFolder][rndPNG];
            File[] inputFileBatch = new File[1];
            double[][] expectedOutputs = new double[inputFileBatch.length][];
            double[][] learnArray = new double[inputFileBatch.length][resolution * resolution];

            for(int j = 0; j < inputFileBatch.length; j++){
                int rndFolder = rand.nextInt(imageFiles.length);
                int rndPNG = rand.nextInt(imageFiles[rndFolder].length);
                inputFileBatch[j] = imageFiles[rndFolder][rndPNG];
                learnArray[j] =  BufferedImageUtil.rescaleAndToArray(inputFileBatch[j], resolution);

                expectedOutputs[j] = expectedOutput.getExpectedOutputArray(inputFileBatch[j].getParentFile().getName());
                //System.out.println(Arrays.toString(expectedOutput.getExpectedOutputArray(inputFileBatch[j].getParentFile().getName())));
                //System.out.println(inputFileBatch[j].getParentFile().getName());
            }
            System.out.println(inputFileBatch[0].getParentFile().getName());


            neuralNetwork.Learn(learnArray,0.1, expectedOutputs);
            //average_cost += neuralNetwork.LossFunction(neuralNetwork.forward(learnArray), expectedOutput.getExpectedOutputArray(next.getParentFile().getName()));
            }
            //System.out.println("Average Cost: " + average_cost/100);


/*
        assert files != null;
        int[][] inputArray = new int[files.length][resolution*resolution];
        double[][] learnArray = new double[files.length][resolution*resolution];

        for(int i = 0; i < files.length; i++)
        {
            inputArray[i] = BufferedImageUtil.rescaleAndToArray(files[i], resolution);
        }
        for(int i = 0; i < inputArray.length; i++){
            for(int j = 0; j < inputArray[0].length; j++){
                learnArray[i][j] = inputArray[i][j];
            }
        }
        utility.ExpectedOutput expectedOutput = new ExpectedOutput();
        System.out.println(Arrays.toString(expectedOutput.getDifferentOutputs()));
        System.out.println(Arrays.toString(expectedOutput.getExpectedOutputArray("Tiger")));

        neuralNetwork.Learn(learnArray,0.3f, expectedOutput.getExpectedOutputArray("Tiger"));
*/
    }
}
