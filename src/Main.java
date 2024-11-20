package src;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main extends Application {

    private Scene scene;

    @Override
    public void start(Stage primaryStage) throws IOException {
        // ---------------------------- Scene Configuration ---------------------------- //
        Parent root = FXMLLoader.load(getClass().getResource("window.fxml"));
        this.scene = new Scene(root, 1200, 850);
        primaryStage.setScene(this.scene);
        primaryStage.setTitle("MESI Protocol Simulation");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
