package utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;


/**
 * This class manages expected outputs
 * for example, this class translates the one-hot output to the right string
 *
 */
public class ExpectedOutput {
    String[] differentOutputs;
    int numOfOutputs;
    /**
     * this is the constructor of this class.
     * the expected outputs are read from a csv-file and stored in an array
     * also, the number of outputs is getting saved
     *
     */
    public ExpectedOutput() throws FileNotFoundException {
        /*(for creating a jar file)
        InputStream instr = this.getClass().getResourceAsStream("expectedOutputs.csv");
        assert instr != null;
        Scanner scanner = new Scanner(instr);
         */
        File file = new File("MontagsMaler/layers/expectedOutputs.csv");
        Scanner scanner = new Scanner(file);

        String data = scanner.nextLine();
        scanner.close();
        differentOutputs = data.split(",");
        numOfOutputs = differentOutputs.length;
    }

    /**
     * Returns the number of outputs that exist
     *
     * @return number of outputs
     *
     */
    public int getNumOfOutputs(){
        return  numOfOutputs;
    }

    /**
     * Returns the array that consists of strings of all the different "things" that the NN can guess
     *
     * @return the array that consists of strings of all the different "things" that the NN can guess
     *
     */
    public String[] getDifferentOutputs(){
        return differentOutputs;
    }

    /**
     * returns the right expected output for the string.
     * The string must be a "thing" that the NN can guess
     *
     * @param name string of a "thing" that the NN can guess
     *
     * @return returns an array in one-hot form for the name that was input
     *
     */
    public double[] getExpectedOutputArray(String name){
        double[] outputArray = new double[numOfOutputs];
        outputArray[Arrays.asList(differentOutputs).indexOf(name)] = 1;
        return outputArray;
    }


    /**
     * returns the name of the "thing" at differentOutputs[index]
     *
     * @param index index for differentOutputs of this class
     *
     * @return returns the string of differentOutputs at the index
     *
     */
    public String getStringAtIndex(int index){
        return differentOutputs[index];
    }
}