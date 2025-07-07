package NeuralNetwork;

import static java.lang.Math.exp;


/**This class is the outputlayer of the nn and the subclass of the layers class.
 *
 */
public class OutputLayer extends Layer{

    /**This is the constructor of the output layer.
     *
     */
    public OutputLayer(int num_input, int num_bias){
        super(num_input,num_bias);
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
    protected double[][] activation(double[][] input) {//Sigmoid activation function
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length; j++) {
                input[i][j] = (1 / (1 + Math.exp(-(input[i][j]))));
            }
        }
        return input;
/*
        for (int i=0; i< input.length; i++){
            double sum= 0;
            for(int z=0; z<input[i].length;z++){
                sum+=(double)Math.exp(input[i][z]);
            }
            for(int j=0; j< input[i].length; j++){
                input[i][j]=(double) Math.exp(input[i][j])/sum;
            }
        }
        return input;
 */
    }
    /*protected double[][] activation(double[][] input){//Softmax activation function
        double[][] output=new double[input.length][input[0].length];
        for(int i=0; i< input.length;i++){
            double largest=double.MIN_VALUE;
            for(int j=0; j<input[i].length;j++) {
                if(input[i][j]>largest){
                    largest=input[i][j];
                }
            }
            for(int j=0; j<input[i].length;j++){
                output[i][j]=(double)exp(input[i][j]-largest);
            }
        }
        double[] norm_base=new double[output.length];
        for(int i=0; i< input.length;i++){
            for(int j=0; j<input[i].length;j++){
                norm_base[i]=norm_base[i]+output[i][j];
            }
        }
        for(int i=0; i< input.length;i++){
            for(int j=0; j<input[i].length;j++){
                output[i][j]=output[i][j]/norm_base[i];
            }
        }
        return output;
    }*/
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