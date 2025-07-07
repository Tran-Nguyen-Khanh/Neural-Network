package NeuralNetwork;

import java.util.Arrays;
import java.io.*;
import java.util.Objects;

import utility.ExpectedOutput;

/**This class is the modulation of the neural network.
 * It is basically the connection between the layers.
 *
 * @author Tarek Kaebler&lt;tarek.kaebler@stud.uni-hannover.de&gt;
 */
public class Module implements GUI_NN_Interface{
    /**layer array of all the layers in the nn.
     */
    private Layer[] layers;
    /**number of inputs or nodes from the input layer.
     */
    private int num_input;
    double[][] safedA;
    double[][] safedZ;

    private ExpectedOutput expectedOutput;

    /**This is the constructor of the Module class.
     * This sets up the whole nn.
     *
     * @param num_input is the number of inputs in the nn. In this case the resolution * resolution.
     * @param structure is an array of the number of nodes for each layer.
     *
     */
    public Module(int[] structure, int num_input){//defines size and structure of neural network, the structure is defined without the input layer!
        try {
            expectedOutput = new ExpectedOutput();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        this.num_input=num_input;
        this.layers=new Layer[structure.length];
        for(int i=0; i<structure.length; i++){
            if(i!=0) {
                if(i==structure.length-1){
                    this.layers[i] = new OutputLayer(structure[i-1],structure[i]);
                }
                else {
                    this.layers[i] = new HiddenLayer(structure[i - 1], structure[i]);
                }
            }
            else{
                this.layers[i] = new HiddenLayer(num_input,structure[i]);
            }
        }
        boolean initialise = false; //manually switch if you want to initialise the weights and biases new. (reset)
        if(initialise){
            for(int i = 0; i < this.layers.length; i++)
            {
                this.layers[i].initialize();
            }
            saveWB();
            System.exit(0);
        }
        else{
            this.loadWB("MontagsMaler/layers/weights.csv", "MontagsMaler/layers/biases.csv");
        }
        //load the weights and biases that are saved in an csv file. (For adjusting the saved weights and biases of the neural network)
        //this.loadWB("MontagsMaler/layers/weights.csv", "MontagsMaler/layers/biases.csv");
        //initialises weights randomly and biases to 0. (For beginning of the neural network)
        /*for(int i = 0; i < this.layers.length; i++)
        {
            this.layers[i].initialize();
        }*/
        safedA = new double[layers.length + 1][];
        safedZ = new double[layers.length][];//+1 so that the inputlayer is included
    }


    /**This function calculates the output of the whole network.
     */
    public double[][]forward(double[][] input){//makes a forward pass of the neural network on the input data
        //double[][] output=new double[input.length][input[0].length];
        for (Layer layer : this.layers) {
            input = layer.forward(input);
        }
        //System.out.println(Arrays.deepToString(input));
        return input;
    }


    /**This function evaluates the result and chooses the node with the highest probability of being the right answer of the picture.
     *
     * @param input is the input data
     * @return String name of the object that is probably drawn.
     */
    public String eval (double[][] input){
        double[][] tmp = forward(input);
        int index = 0;
        for (int i = 0; i < tmp[0].length; i++) {
            if(tmp[0][i] > tmp[0][index]){
                index = i;
            }
        }
        return expectedOutput.getStringAtIndex(index);
    }


    /**This is the lossfunction of the nn with the mean squared loss function.
     *
     * @param input is the actual output of the nn.
     * @param expected_Output is the expected output of the picture.
     */
    public double LossFunction(double[][] input, double[] expected_Output){
        double temp=0;
        for (double[] doubles : input) {
            for (int j = 0; j < doubles.length; j++) {
                temp = temp + (double) Math.pow((doubles[j] - expected_Output[j]), 2);
            }
        }
        //System.out.println(temp/(expected_Output.length* input.length));
        return (temp / input.length); //Changed
    }


    /**This function saves the weights and biases in a csv file.
     */
    public void saveWB() {
        try {
            //All the weights in the neural network
            BufferedWriter writer_weights = new BufferedWriter(new FileWriter("./MontagsMaler/layers/weights.csv"));
            //String weight_csv;
            StringBuilder weight_csv = new StringBuilder();
            for(int i = 0; i < this.layers.length; i++)
            {
                double[][] weights = this.layers[i].getWeight();
                for(int j = 0; j < weights.length; j++)
                {
                    for(int k = 0; k < weights[j].length; k++)
                    {
                        weight_csv.append(Double.toString(weights[j][k])).append(",");
                    }
                }
                weight_csv.append("\n");
            }
            writer_weights.write(weight_csv.toString());
            writer_weights.close();

            //All the biases in the neural network
            BufferedWriter writer_biases = new BufferedWriter(new FileWriter("./MontagsMaler/layers/biases.csv"));
            StringBuilder bias_csv = new StringBuilder();
            for(int i = 0; i < layers.length; i++)
            {
                double[] biases = layers[i].getBias();
                for(int j = 0; j < biases.length; j++)
                {
                    bias_csv.append(Double.toString(biases[j])).append(",");
                }
                bias_csv.append("\n");
            }
            writer_biases.write(bias_csv.toString());
            writer_biases.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**This function loads the weights and biases that were saved in a csv file.
     *
     * @param path_biases is the path to the csv of the biases.
     * @param path_weights is the path to the csv of the weights.
     */
    public void loadWB(String path_weights, String path_biases) {
        try {
            //initialise weights.
            //BufferedReader reader_weights = new BufferedReader(new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream("weights.csv")))); //for jar-creation
            BufferedReader reader_weights = new BufferedReader(new FileReader(path_weights));
            int layer_index = 0;
            String line;
            String[][] weights_str = new String[layers.length][];
            while ((line = reader_weights.readLine()) != null) {
                weights_str[layer_index] = line.split(",");
                layer_index++;
            }
            //System.out.println(weights_str[0].length); (test/used for debugging)

            int csv_index = 0;
            for (int i = 0; i < layers.length; i++) {
                for (int j = 0; j < layers[i].weight.length; j++) {
                    for (int k = 0; k < layers[i].weight[j].length; k++) {
                        //System.out.println(csv_index); (test/used for debugging)
                        layers[i].weight[j][k] = Double.parseDouble((weights_str[i][csv_index]));
                        csv_index++;
                    }
                }
                csv_index = 0;
            }

            //initialise biases.
            BufferedReader reader_biases = new BufferedReader(new FileReader(path_biases));
            layer_index = 0;
            String[][] biases_str = new String[layers.length][];
            while ((line = reader_biases.readLine()) != null) {
                biases_str[layer_index] = line.split(",");
                layer_index++;
            }

            for (int i = 0; i < layers.length; i++) {
                for (int j = 0; j < layers[i].bias.length; j++) {
                    this.layers[i].bias[j] = Double.parseDouble((biases_str[i][j]));
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**This is the learnfunction where which is used for training the nn.
     *
     * @param input is the data input.
     * @param learnRate is the learnrate.
     * @param expected_Output is the expected output.
     */
    public void Learn(double[][] input, double learnRate, double[][] expected_Output) {
        //initialises derivative weights sizes
        double[][] output_display = forward(input);
        System.out.print("before learning output: ");
        System.out.println(Arrays.toString(output_display[0]));
        int temp = this.num_input;
        double[][][] derivative_weights = new double[this.layers.length][][];
        for (int i = 0; i < this.layers.length; i++) {
            derivative_weights[i] = new double[layers[i].bias.length][];
            for (int j = 0; j < layers[i].bias.length; j++) {
                derivative_weights[i][j] = new double[temp];
            }
            temp = layers[i].bias.length;
        }
        //initialises derivative biases sizes
        double[][] derivative_bias = new double[this.layers.length][];
        for (int i = 0; i < this.layers.length; i++) {
            derivative_bias[i] = new double[layers[i].bias.length];
        }


        double[][] savedInput = forward(input);
        double[][] derivatedNodes;

        for (int i = 0; i < input.length; i++) {
            forwardAndSave(input[i]);
            derivatedNodes = new double[layers.length][];
            double[] derivatedLoss = averageDerivativeLossFunction(input, expected_Output);
            derivatedNodes[layers.length - 1] = new double[layers[layers.length - 1].bias.length];
            for (int z = 0; z < derivatedNodes[layers.length - 1].length; z++) {
                derivatedNodes[layers.length - 1][z] = derivatedLoss[z] * safedZ[layers.length - 1][z];
            }


            for (int j = layers.length - 1; j >= 0; j--) {
                if (j != layers.length - 1) {
                    //dz
                    derivatedNodes[j] = new double[layers[j].bias.length];
                    for (int l = 0; l < layers[j].bias.length; l++) {
                        for (int m = 0; m < layers[j + 1].bias.length; m++) {
                            derivatedNodes[j][l] += layers[j + 1].weight[m][l] * derivatedNodes[j + 1][m];
                        }
                        derivatedNodes[j][l] *= safedZ[j][l];
                    }
                }
                //dZ/dW
                for (int l = 0; l < layers[j].weight.length; l++) {//nodes of actual layer
                    for (int m = 0; m < layers[j].weight[l].length; m++) { //targeted layer
                        derivative_weights[j][l][m] += derivatedNodes[j][l] * safedA[j][m];
                    }
                    derivative_bias[j][l] += derivatedNodes[j][l];
                }
            }
        }

        /*
        //initialises derivative weights values
        double h=0.001;
        double delta_cost;
        double org_loss=LossFunction(forward(input),expected_Output);
        for (int i=0;i<this.layers.length;i++) {
            for (int j = 0; j < this.layers[i].weight.length; j++) {
                for (int z = 0; z < this.layers[i].weight[j].length; z++) {
                    this.layers[i].weight[j][z] = this.layers[i].weight[j][z] + h;
                    delta_cost=LossFunction(forward(input),expected_Output)-org_loss;
                    //if(delta_cost != 0){System.out.println(delta_cost/h);}
                    this.layers[i].weight[j][z] = this.layers[i].weight[j][z] - h;
                    derivative_weights[i][j][z]=delta_cost/h;
                }
            }
        }
        //initialises derivative biases values
        for (int i=0;i<this.layers.length;i++) {
            for (int j = 0; j < this.layers[i].bias.length; j++) {
                this.layers[i].bias[j]= this.layers[i].bias[j] +h;
                delta_cost=LossFunction(forward(input),expected_Output)-org_loss;
                //if(delta_cost != 0){System.out.println(delta_cost/h);}
                this.layers[i].bias[j]= this.layers[i].bias[j] -h;
                derivative_bias[i][j]=delta_cost/h;
            }
        }
*/
            for (int i = 0; i < this.layers.length; i++) {
                for (int j = 0; j < this.layers[i].weight.length; j++) {
                    for (int z = 0; z < this.layers[i].weight[j].length; z++) {
                        this.layers[i].weight[j][z] -= (derivative_weights[i][j][z]/input.length) * learnRate; //Changed
                    }
                }
            }

            for (int i = 0; i < this.layers.length; i++) {
                for (int j = 0; j < this.layers[i].bias.length; j++) {
                    this.layers[i].bias[j] -= (derivative_bias[i][j]/input.length) * learnRate; //Changed
                }
            }
        //Displaying the progress
        output_display = forward(input);
        System.out.print("After learning output: ");
        System.out.println(Arrays.toString(output_display[0]));
        //System.out.println("Cost: " + this.LossFunction(forward(input), expected_Output));
        System.out.println();
        this.saveWB();
    }

    /*public static void main(String[] args){
        int[] struct={50,50,60,70,20};
        Module test= new Module(struct,4);
        double[][] output=test.forward(input);
        for (double[] doubles : output) {
            System.out.println(Arrays.toString(doubles));
        }

    }*/


    /**This function saves important information for the backpropagation.
     *
     */
    public void forwardAndSave(double[] input) {
        //System.out.println(Arrays.toString(input));
        safedA[0] = input;
        //saving z and a in arrays
        for (int i = 1; i <= layers.length; i++) {
            double[][] input1 = new double[1][];
            input1[0] = input;
            double[][] temp = layers[i - 1].calc(input1);
            input = layers[i - 1].forward(input1)[0];
            safedA[i] = input;
            safedZ[i - 1] = temp[0];
            for(int j = 0; j < safedZ[i - 1].length; j++){
                safedZ[i - 1][j] = layers[i -1 ].derivativeActivationFunction(safedZ[i - 1][j]);
            }
        }
    }


    /**This function is the derivative of the lossfunction for the backpropagation.
     *
     * @param input is the actual output of the nn.
     * @param expectedOutputs is the expected output of the nn.
     * @return returns the derivative of the loss function.
     */
            public double[] averageDerivativeLossFunction(double[][] input, double[][] expectedOutputs) {
                double[] average_loss = new double[layers[layers.length - 1].bias.length];
                double[][] inputs = this.forward(input);
                for (int i = 0; i < inputs.length; i++) {//for every input
                    for(int j = 0; j < layers[layers.length - 1].bias.length; j++){//for every output
                        average_loss[j] += inputs[i][j] - expectedOutputs[i][j];
                    }
                }
                for(int i = 0; i < layers[layers.length - 1].bias.length; i++){
                    average_loss[i] /= input.length;
                    average_loss[i] *= 2;
                }
                //System.out.println(average_loss);
                return average_loss;
            }



}
