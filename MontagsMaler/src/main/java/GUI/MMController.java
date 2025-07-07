package GUI;


import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.StrokeLineCap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;
import utility.BufferedImageUtil;


/**Controller of the application Montagsmaler
 * This class handles different events that occur.
 * For example, if a button is pressed, the corresponding method is called
 * and handles the event
 *
 *@version 1.0
 */
public class MMController {
    /**Text field from the fxml-file, that will tell the User, what the AI thinks, what was drawn on the canvas.
     */
    @FXML
    public TextField output;

    /**canvas from the fxml-file. This is the canvas that is being drawn on.
     */
    @FXML
    private Canvas canvas;

    /**WriteableImage Stack, past states of the canvas are being stored on this stack.
     */
    Stack<WritableImage> history = new Stack<>();
    GraphicsContext GC;



    @FXML
    public void initialize(){
        GC = canvas.getGraphicsContext2D();
        GC.setLineCap(StrokeLineCap.ROUND);
        GC.setLineWidth(10);
    }



    /**when the mouse button is first pressed, the former Canvas-content gets saved
     * and a new path is opened.
     *
     */
    @FXML
    private void pressed() {
        SaveCanvasToStack();
        GC.beginPath();


    }

    /**while dragged is called again and again, the line will be drawn at the cursors pos.
     *
     */
    @FXML
    private void dragged(MouseEvent e) {
        GC.lineTo(e.getX(), e.getY());
        GC.stroke();
        GC.closePath();
        GC.beginPath();
        GC.moveTo(e.getX(), e.getY());
    }

    /**draws the last stroke and closes the path since the mouse button is no longer being pressed
     *
     */
    @FXML
    private void released(MouseEvent e) {
        GC.lineTo(e.getX(), e.getY());
        GC.stroke();
        GC.closePath();
    }

    /**saves to current Canvas-content
     * This method creates a snapshot of the canvas and saves it on a stack.
     * when calling undo, the element on top of the stack gets popped.
     *
     */
    private void SaveCanvasToStack() {
        int height = (int) canvas.getHeight();
        int width = (int) canvas.getWidth();

        SnapshotParameters parameters = new SnapshotParameters();
        WritableImage WI = new WritableImage(width, height);
        WritableImage copy = canvas.snapshot(parameters, WI);
        history.add(copy);
    }

    /**Undoes the last stroke.
     * This is done by drawing the image that is on top of the stack onto the canvas
     * and popping that image from the stack.
     *
     */
    @FXML
    private void undo() {
        if (!history.isEmpty()) {
            canvas.getGraphicsContext2D().drawImage(history.pop(), 0, 0);
        }
    }

    /**Will give the signal to evaluate the drawn picture(not working yet)
     *
     */
    @FXML
    private void evaluate(){
        int res = 30;
        int height = (int) canvas.getHeight();
        int width = (int) canvas.getWidth();
        SnapshotParameters parameters = new SnapshotParameters();
        WritableImage canvasSnapshot = canvas.snapshot(parameters, new WritableImage(width, height));
        BufferedImage img = SwingFXUtils.fromFXImage(canvasSnapshot, new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB));
        double[][] array =  {BufferedImageUtil.rescaleAndToArray(img, res)};

        //passing array to the neural network
        assert Montagsmaler.getModule() != null;
        String result = Montagsmaler.getModule().eval(array);//TODO
        output.setText(result);


    }
}

