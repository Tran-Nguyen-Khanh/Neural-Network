package GUI;

import NeuralNetwork.Module;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
/**The Application of Montagsmaler
 * This class creates the scene and the Window, in which the content of the scene is shown.
 *
 *@version 1.0
 */
public class Montagsmaler extends Application {
    static Module neuralNetwork;

    /**sets up the scene
     * This class creates the scene and sets up the elements from the .fxml-file
     *
     */
    @Override
    public void start(Stage stage) throws IOException {
        Group group = new Group();
        FXMLLoader fxmlLoader = new FXMLLoader(Montagsmaler.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(group);
        group.getChildren().add(fxmlLoader.load());


        Color grey = Color.web("#27374D");
        scene.setFill(grey);

        stage.widthProperty().addListener((observable, oldValue, newValue) -> {
            double multiplier = stage.getHeight() > (double)newValue ? (double)newValue / 600:stage.getHeight()/600 ;
            double change = stage.getHeight() > (double)newValue ? ((double)newValue - (double)600) / 2: (stage.getHeight() - (double)600) / 2;
            group.lookup("#Vbox1").setTranslateX(change);
            group.lookup("#Vbox1").setTranslateY(change);
            group.lookup("#Vbox1").setScaleX(multiplier);
            group.lookup("#Vbox1").setScaleY(multiplier);

        });

        stage.heightProperty().addListener((observable, oldValue, newValue) -> {
            double multiplier = stage.getWidth() > (double)newValue? (double)newValue  / 600: stage.getWidth()/600 ;
            double change = stage.getWidth() > (double)newValue ? ((double)newValue - (double)600) / 2: (stage.getWidth() - (double)600) / 2;
            group.lookup("#Vbox1").setTranslateX(change);
            group.lookup("#Vbox1").setTranslateY(change);
            group.lookup("#Vbox1").setScaleY(multiplier);
            group.lookup("#Vbox1").setScaleX(multiplier);

        });

        stage.getIcons().add(new Image("icon.png"));
        stage.setTitle("Montagsmaler!");
        stage.setScene(scene);
        stage.show();
    }

    /**launches the app
     * calls init() and start() to which the stage(window) is passed.
     *
     */
    public static void startApp(Module m) {
        neuralNetwork = m;
        launch();
    }

    /**
     * This method is used by MMController to access the NN
     *
     * @return returns the instance of the NN if it exists, otherwise null will be returned
     *
     */
    public  static Module getModule() {
        if (neuralNetwork != null) {
            return neuralNetwork;
        }
        else{
            return null;
        }
    }
}