package src;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TableView<Statistic> statsTable;
    @FXML
    private GridPane root;
    @FXML
    private Line dataBus;

    private Server server;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Statistic> statsData = FXCollections.observableArrayList(
                new Statistic("Cache Misses"),
                new Statistic("Cache Hits"),
                new Statistic("Read Requests"),
                new Statistic("Write Requests")
        );

        this.statsTable.setItems(statsData);

        System.out.println(5);

        this.dataBus.setStroke(Color.RED);

        /*try {
            server = new Server(new ServerSocket(8888));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error creating server");
        }

        server.receiveMessage();*/
    }
}
