package NeuralNetwork;

/**This class is a subclass of the Layer class.
 * This subclass represents the hidden layers of the neural network.
 *
 */
public class HiddenLayer extends Layer {
    /**This is the constructor for the hidden layers
     *It calls the constructor of the superclass.
     *
     * @param num_bias is the number of biases.
     * @param num_input is the number of inputs connected to layer.
     */
    public HiddenLayer(int num_input, int num_bias) {
        super(num_input, num_bias);
    }

    /**This function calculates the matrix multiplication including the activation for this particular layer
     *
     * @param input is the input that the layer receives and calculates with it.
     * @return returns array of the new output calculated with the given input.
     */
    @Override
    public double[][] forward(double[][] input) {
        return activation(super.calc(input));
    }

    /**This is the Sigmoid activation of the hiddenlayer.
     *
     * @param input is the input after the matrix multiplication.
     * @return returns the final calculation of the layer with the activation.
     */
    protected double[][] activation(double[][] input) {//Sigmoid activation
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length; j++) {
                input[i][j] = (1 / (1 + Math.exp(-(input[i][j]))));
            }
        }
        return input;
    }

    /**This is the derivative of the Sigmoid activation.
     *
     * @param input takes weighted sum
     * @return the derivative of the weighted sum
     */
    @Override
    protected double derivativeActivationFunction(double input){
        return (1 / (1 + Math.exp(-(input)))) * (1 - (1 / (1 + Math.exp(-(input)))));
    }

}
