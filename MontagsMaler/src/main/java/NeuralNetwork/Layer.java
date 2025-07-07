package NeuralNetwork;

import java.util.Arrays;
import java.util.Random;

/**This class is a super class for the layers of the neural network
 *
 */
public class Layer{
    /**weights of the layer
     */
    public double[][] weight;
    /**Biases of the layer
     */
    public double[] bias;

    /**This is the constructor for the layers.
     */
    public Layer(int num_input, int num_bias){
        this.weight=new double[num_bias][num_input];
        this.bias=new double[num_bias];
    }
    /**This is the forward function which should return the matrix multiplication.
     * The forward is only used in the subclasses overridden for the subclass layers.
     *
     * @param input is the input that the layer receives.
     * @return returns the same input.
     */
    public double[][] forward(double[][] input) {
        //return calc(input);
        return input;
    }

    /**This function is the calculation of the matrix multiplication.
     *
     * @param input is the input that the layer receives.
     * @return returns the calculation of the matrix.
     */
    protected double[][] calc(double[][] input){
        //System.out.println(Arrays.toString(input));
        double[][] output= new double[input.length][this.bias.length];
        for(int z=0; z<input.length;z++) {
            for (int i = 0; i < this.bias.length; i++) {
                output[z][i] = this.bias[i];
                for (int j = 0; j < this.weight[i].length; j++) {
                    output[z][i] = output[z][i] + (this.weight[i][j] * input[z][j]);
                }
            }
        }
        return output;
    }

    /**This function initialises the weights randomly with the He-implementation and the biases with 0.
     *
     */
    public void initialize(){
        Random rand = new Random();
        for (int i = 0; i < this.weight.length; i++) {
            for(int j = 0; j < this.weight[i].length; j++) {
                this.weight[i][j] = rand.nextGaussian() * Math.sqrt((2.0 / this.bias.length));
            }
        }
        Arrays.fill(this.bias, 0);
        }

    /**This function returns the weight
     *
     * @return weights.
     */
    public double[][] getWeight() {
        return weight;
    }

    /**This function returns the biases
     *
     * @return biases
     */
    public double[] getBias() {
        return bias;
    }

    /**This function is the derivative activation function but is only used overridden in the subclasses.
     *
     * @param input weighted sum
     * @return returns the input given
     */
    protected double derivativeActivationFunction(double input){return input;}
}
