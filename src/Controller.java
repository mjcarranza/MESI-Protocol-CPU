package src;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

    private Server server;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Statistic> statsData = FXCollections.observableArrayList(
                new Statistic("Cache Misses", 0),
                new Statistic("Cache Hits", 0),
                new Statistic("Read Requests", 0),
                new Statistic("Write Requests", 0)
        );

        this.statsTable.setItems(statsData);

        try {
            this.server = new Server(new ServerSocket(8888));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error creating server.");
        }


        this.server.receiveMessage(this.root);
    }

    public static void handleMessage(String message, GridPane root) {
        if (message != null && !message.isEmpty()) {
            String firstChar = message.substring(0, 1);

            switch (firstChar) {
                case "C":
                    if (message.substring(1, 2).equals("a")){
                        handleStats(message, root);
                    } else {
                        handleCaches(message, root);
                    }
                    break;
                case "M":
                    handleMainMemory(message, root);
                    break;
                case "B":
                    handleBuses(message, root);
                    break;
                default:
                    handleStats(message, root);
            }
        }
    }

    public static void handleCaches(String message, GridPane root) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                HBox cpuRow = (HBox) root.getChildren().get(5);

                String secondChar = message.substring(1, 2);
                Integer cacheNumber = Integer.valueOf(secondChar);
                VBox cpuAndCacheBox = (VBox) cpuRow.getChildren().get(cacheNumber - 1);
                VBox cacheDataBox = (VBox) cpuAndCacheBox.getChildren().get(0);
                Label newLabel = null;
                Boolean isInCache = false;

                Integer index = 6;
                String state = message.substring(4, 5);
                String address = "";
                String nextChar;
                while (true) {
                    nextChar = message.substring(index, index+1);
                    if (!nextChar.equals("|")) {
                        address = address + nextChar;
                    } else {
                        index++;
                        break;
                    }

                    index++;
                }

                String data = "";
                while (index < message.length()) {
                    nextChar = message.substring(index, index+1);
                    data = data + nextChar;
                    index++;
                }

                String labelString = "State: " + state + " | Addr: " + address + " | Data: " + data;

                for (int i = 0; i < 2; i++) {
                    Label currentLabel = (Label) cacheDataBox.getChildren().get(i);
                    if (currentLabel.getText().contains(address)) {
                        newLabel = currentLabel;
                        isInCache = true;
                        break;
                    }
                }

                if (isInCache) {
                    newLabel.setText(labelString);
                } else {
                    Label label0 = (Label) cacheDataBox.getChildren().get(0);
                    Label label1 = (Label) cacheDataBox.getChildren().get(1);

                    label1.setText(label0.getText());
                    label0.setText(labelString);
                }

            }
        });
    }

    public static void handleMainMemory(String message, GridPane root) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                HBox centeredMemoryBox = (HBox) root.getChildren().get(1);
                VBox memoryDataBox = (VBox) centeredMemoryBox.getChildren().getFirst();
                Integer index = 0;
                String address = message.substring(3, 5);
                String data = message.substring(6);
                Label currentLabel = null;

                for (int i = 0; i < 4; i++) {
                    currentLabel = (Label) memoryDataBox.getChildren().get(i);
                    if (currentLabel.getText().contains(address)) {
                        break;
                    }
                }

                String newLabel = "Addr: " + address + ",   Data: " + data;

                if (currentLabel != null) {
                    currentLabel.setText(newLabel);
                }
            }
        });
    }

    public static void handleBuses(String message, GridPane root) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                VBox busBox = (VBox) root.getChildren().get(3);

                String busType = message.substring(1);
                Integer busIndex = 0;
                Color busColor = Color.BLACK;

                switch (busType) {
                    case "A":
                        busIndex = 3;
                        busColor = Color.GREEN;
                        break;
                    case "D":
                        busIndex = 1;
                        busColor = Color.RED;
                        break;
                    case "S":
                        busIndex = 5;
                        busColor = Color.BLUEVIOLET;
                        break;
                }

                Line busLine = (Line) busBox.getChildren().get(busIndex);
                busLine.setStroke(busColor);
            }
        });
    }

    public static void handleStats(String message, GridPane root) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String stat = message.substring(10);
                Integer rowIndex = 0;

                switch (stat){
                    case "Cache miss!":
                        rowIndex = 0;
                        break;
                    case "Cache hit!":
                        rowIndex = 1;
                        break;
                    case "Read request":
                        rowIndex = 2;
                        break;
                    case "Write request":
                        rowIndex = 3;
                        break;
                }

                VBox statsBox = (VBox) root.getChildren().get(7);
                TableView<Statistic> statsTable = (TableView<Statistic>) statsBox.getChildren().get(1);
                Statistic statistic = (Statistic) statsTable.getItems().get(rowIndex);
                Integer oldValue = statistic.getValue();
                statistic.setValue(oldValue + 1);
                statsTable.refresh();
            }
        });
    }
}
