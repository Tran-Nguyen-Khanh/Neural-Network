package utility;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;

/**
 * This class is used to prepare an image for the AI.
 * This class is a utility class, that must not be instantiated.
 * the main-purpose of this class is to rescale images into the requested size and turn that rescaled
 * images into 1D-arrays(proper input-format for the neural network)
 */
public final class BufferedImageUtil {

    /**
     *  This method takes in Files and returns 1D-arrays
     *  this is done by taking the mean value of the rgb-values of each pixels.
     *  Depending on the threshold the value of the 1D-array at this pixel will be 1 or 0
     *  its important to only use this method with pictures that have a white background
     *
     *  This method comes in handy when the original resolution matches the desired resolution since the rescaling can be skipped
     *
     * @param f a file that must contain a png or jpeg
     *
     * @return 1D-Array that is used as Input by the neural networks
     *
     */
    public static int[] toArray(File f) throws IOException {
        BufferedImage BI = ImageIO.read(f);
        int height = BI.getHeight();
        int width = BI.getWidth();
        int[] returnArray = new int[width * height];
        int[] dataBuffInt = BI.getRGB(0, 0, width, height, null, 0, width);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color color = new Color(dataBuffInt[j + (i * width)]);
                returnArray[j + (i * width)] = ((color.getRed() + color.getGreen() + color.getBlue()) / 3)  > 245 ? 0 : 1;
            }
        }
        return returnArray;
    }

    /**
     *  This method takes in BufferedImages and returns 1D-arrays
     *  this is done by taking the mean value of the rgb-values of each pixels.
     *  Depending on the threshold the value of the 1D-array at this pixel will be 1 or 0
     *  its important to only use this method with pictures that have a white background
     *
     * @param BI the picture in the state in which it will get turned into an 1D-Array
     *
     * @return 1D-Array that is used as Input by the neural networks
     *
     */
    public static double[] toArray(BufferedImage BI){
        int height = BI.getHeight();
        int width = BI.getWidth();
        double[] returnArray = new double[width * height];
        int[] dataBuffInt = BI.getRGB(0, 0, width, height, null, 0, width);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color c = new Color(dataBuffInt[j + (i * width)]);
                returnArray[j + (i * width)] = ((c.getRed() + c.getGreen() + c.getBlue()) / 3) > 245 ? 0 : 1;
            }
        }
        return returnArray;
    }


    /**
     * This method rescales BufferedImages to the needed resolution.
     * Note that pictures will always be transformed to a 1:1 ratio when being rescaled
     *
     * @param BI the picture in its original resolution
     * @param resolution resolution to which the picture gets resized
     *
     * @return rescaled version of the input BI
     *
     */
    public static BufferedImage rescale(BufferedImage BI, int resolution){
        BufferedImage newBI = new BufferedImage(resolution, resolution, BufferedImage.TYPE_INT_ARGB);
        Image scaledImage = BI.getScaledInstance(resolution, resolution, BufferedImage.SCALE_SMOOTH);

        newBI.getGraphics().drawImage(scaledImage, 0, 0, null);
        return newBI;
    }


    /**
     * This method first rescales the images and then turns it into an 1D-array
     *
     * @param BI the picture which will get turned into an 1D-Array
     *
     * @return 1D-Array that is used as Input by the neural networks
     *
     */
    public static double[] rescaleAndToArray(BufferedImage BI, int resolution){
        return toArray(rescale(BI, resolution));
    }

    /**
     *
     * This method takes in a files that must be and image(png, jpeg), rescales it and turns it into an 1D-array
     * @param f contains the image
     *
     * @return 1D-Array that is used as Input by the neural networks
     *
     */
    public static double[] rescaleAndToArray(File f, int resolution) throws IOException {
        BufferedImage BI = ImageIO.read(f);
        return toArray(rescale(BI, resolution));
    }

    /**
     * This method rebuilds an array back into an images and saves it to a files
     * Note that this method is only important for debugging and checking how much information got lost by rescaling
     * the original picture and turning the original picture into an 1D-array.
     * the pictures gets saved to the same folder, the csv-files for the NN are in, so it may be useful to change the
     * location
     *
     * @param a 1D-array (input for neural network)
     * @param res resolution of the picture that was used to generate the 1D-array
     *
     */
    public static void saveArrayAsPNG(double[] a, int res) throws IOException {
        File f = new File("Montagsmaler/layers/output.png");
        BufferedImage newBI = new BufferedImage(res, res, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < res; i++) {//für jede reihe
            for (int j = 0; j < res; j++) {//füe jede spalte
                newBI.setRGB(j, i, a[i * res + j] < 1 ? Color.white.getRGB() : Color.black.getRGB());
            }
        }
        System.out.println(ImageIO.write(newBI, "png", f));
    }

}
