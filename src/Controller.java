package src;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
    @FXML
    private Button button;

    private Server server;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Statistic> statsData = FXCollections.observableArrayList(
                new Statistic("Cache 0", 0, 0, 0, 0, 0),
                new Statistic("Cache 1", 0, 0, 0, 0, 0),
                new Statistic("Cache 2", 0, 0, 0, 0, 0),
                new Statistic("Cache 3", 0, 0, 0, 0, 0),
                new Statistic("Total", 0, 0, 0, 0, 0)
        );

        this.statsTable.setItems(statsData);

        try {
            this.server = new Server(new ServerSocket(8888));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error creating server.");
        }


        this.server.receiveMessage(this.root);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                server.sendMessage("1", root);
            }
        });
    }

    public static void handleMessage(String message, GridPane root) {
        if (message != null && !message.isEmpty()) {
            String firstChar = message.substring(0, 1);
            clearBuses(root);
            System.out.println(message);

            switch (firstChar) {
                case "C":
                    handleCaches(message, root);
                    break;
                case "M":
                    handleMainMemory(message, root);
                    break;
                case "B":
                    handleBuses(message, root);
                    break;
                case "S":
                    handleStats(message, root);
                    break;
            }
        }
    }

    public static void clearBuses(GridPane root) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                VBox busBox = (VBox) root.getChildren().get(3);

                for (int i = 0; i < 6; i++) {
                    if (i % 2 != 0) {
                        Line busLine = (Line) busBox.getChildren().get(i);
                        busLine.setStroke(Color.BLACK);
                    }
                }

                VBox busCashLinesRow = (VBox) root.getChildren().get(4);
                HBox busCashBox = (HBox) busCashLinesRow.getChildren().get(0);

                for (int i = 0; i < 15; i++) {
                    if (i !=3 && i!=7 && i!=11) {
                        Line busLine = (Line) busCashBox.getChildren().get(i);
                        busLine.setStroke(Color.BLACK);
                    }
                }

                HBox cpuRow = (HBox) root.getChildren().get(5);

                for (int i = 0; i < 4; i++) {
                    VBox cpuAndCacheBox = (VBox) cpuRow.getChildren().get(i);

                    HBox linesBox = (HBox) cpuAndCacheBox.getChildren().get(1);
                    for (int j = 0; j < 2; j++) {
                        Line line = (Line) linesBox.getChildren().get(j);
                        line.setStroke(Color.BLACK);
                    }
                }
            }
        });
    }

    public static void handleCaches(String message, GridPane root) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                HBox cpuRow = (HBox) root.getChildren().get(5);

                String secondChar = message.substring(1, 2);
                Integer cacheNumber = Integer.valueOf(secondChar);
                VBox cpuAndCacheBox = (VBox) cpuRow.getChildren().get(cacheNumber);
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

                for (int i = 0; i < 8; i++) {
                    Label currentLabel = (Label) cacheDataBox.getChildren().get(i);
                    String actualAddress = "";
                    Integer firstIndex = 17;
                    if (!currentLabel.getText().isEmpty()) {
                        while (true) {
                            nextChar = currentLabel.getText().substring(firstIndex, firstIndex + 1);
                            if (!nextChar.equals(" ")) {
                                actualAddress = actualAddress + nextChar;
                            } else {
                                break;
                            }
                            firstIndex++;
                        }
                    }
                    if (address.equals(actualAddress)) {
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
                    Label label2 = (Label) cacheDataBox.getChildren().get(2);
                    Label label3 = (Label) cacheDataBox.getChildren().get(3);
                    Label label4 = (Label) cacheDataBox.getChildren().get(4);
                    Label label5 = (Label) cacheDataBox.getChildren().get(5);
                    Label label6 = (Label) cacheDataBox.getChildren().get(6);
                    Label label7 = (Label) cacheDataBox.getChildren().get(7);

                    label7.setText(label6.getText());
                    label6.setText(label5.getText());
                    label5.setText(label4.getText());
                    label4.setText(label3.getText());
                    label3.setText(label2.getText());
                    label2.setText(label1.getText());
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
                Integer index = 3;
                String address = "";
                Boolean isInMemory = false;

                while (true) {
                    String nextChar = message.substring(index, index + 1);

                    if (!nextChar.equals("|")) {
                        address = address + nextChar;
                    } else {
                        index++;
                        break;
                    }
                    index++;
                }

                String data = message.substring(index);
                Label currentLabel = null;

                for (int i = 0; i < memoryDataBox.getChildren().size(); i++) {
                    currentLabel = (Label) memoryDataBox.getChildren().get(i);
                    String actualAddress = "";
                    Integer firstIndex = 6;
                    if (!currentLabel.getText().isEmpty()) {
                        while (true) {
                            String nextChar = currentLabel.getText().substring(firstIndex, firstIndex + 1);
                            if (!nextChar.equals(",")) {
                                actualAddress = actualAddress + nextChar;
                            } else {
                                break;
                            }
                            firstIndex++;
                        }
                    }
                    if (address.equals(actualAddress) || currentLabel.getText().isEmpty()) {
                        isInMemory = true;
                        break;
                    }
                }

                String newLabelText = "Addr: " + address + ",   Data: " + data;

                if (currentLabel != null && isInMemory) {
                    currentLabel.setText(newLabelText);
                } else {
                    Label newLabel = new Label(newLabelText);
                    newLabel.setStyle("-fx-font-size: 14px;");
                    memoryDataBox.getChildren().add(newLabel);
                }
            }
        });
    }

    public static void handleBuses(String message, GridPane root) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String busType = message.substring(1, 2);
                Integer cacheNumber = null;
                if (!message.substring(2).equals("M")) {
                    cacheNumber = Integer.valueOf(message.substring(2));
                }
                Integer busIndex = 0;
                Integer cacheIndex = -1;
                Integer cpuIndex = 0;
                Color busColor = Color.BLACK;

                switch (busType) {
                    case "A":
                        if (cacheNumber != null) {
                            switch (cacheNumber) {
                                case 0:
                                    cacheIndex = 1;
                                    break;
                                case 1:
                                    cacheIndex = 5;
                                    break;
                                case 2:
                                    cacheIndex = 9;
                                    break;
                                case 3:
                                    cacheIndex = 13;
                                    break;
                            }
                        }
                        busIndex = 3;
                        cpuIndex = 0;
                        busColor = Color.GREEN;
                        break;
                    case "D":
                        if (cacheNumber != null) {
                            switch (cacheNumber) {
                                case 0:
                                    cacheIndex = 2;
                                    break;
                                case 1:
                                    cacheIndex = 6;
                                    break;
                                case 2:
                                    cacheIndex = 10;
                                    break;
                                case 3:
                                    cacheIndex = 14;
                                    break;
                            }
                        }
                        busIndex = 1;
                        cpuIndex = 1;
                        busColor = Color.RED;
                        break;
                    case "S":
                        if (cacheNumber != null) {
                            switch (cacheNumber) {
                                case 0:
                                    cacheIndex = 0;
                                    break;
                                case 1:
                                    cacheIndex = 4;
                                    break;
                                case 2:
                                    cacheIndex = 8;
                                    break;
                                case 3:
                                    cacheIndex = 12;
                                    break;
                            }
                        }
                        busIndex = 5;
                        cpuIndex = -1;
                        busColor = Color.BLUEVIOLET;
                        break;
                }

                VBox busBox = (VBox) root.getChildren().get(3);
                Line busLine = (Line) busBox.getChildren().get(busIndex);
                busLine.setStroke(busColor);

                if (cacheIndex >= 0) {
                    VBox busCashLinesRow = (VBox) root.getChildren().get(4);
                    HBox busCashHBox = (HBox) busCashLinesRow.getChildren().get(0);
                    Line cacheLine = (Line) busCashHBox.getChildren().get(cacheIndex);
                    cacheLine.setStroke(busColor);
                }

                if (cpuIndex >= 0 && cacheNumber!=null) {
                    HBox cpuRow = (HBox) root.getChildren().get(5);
                    VBox cpuAndCacheBox = (VBox) cpuRow.getChildren().get(cacheNumber);
                    HBox linesBox = (HBox) cpuAndCacheBox.getChildren().get(1);
                    Line cpuLine = (Line) linesBox.getChildren().get(cpuIndex);
                    cpuLine.setStroke(busColor);
                }

            }
        });
    }

    public static void handleStats(String message, GridPane root) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                VBox statsBox = (VBox) root.getChildren().get(7);
                TableView<Statistic> statsTable = (TableView<Statistic>) statsBox.getChildren().get(1);

                String type = message.substring(1, 2);
                String cacheNumber = message.substring(2);
                Integer rowIndex = 0;
                Integer columnIndex = 0;
                rowIndex = Integer.valueOf(cacheNumber);
                Statistic specificStat = (Statistic) statsTable.getItems().get(rowIndex);
                Statistic generalStat = (Statistic) statsTable.getItems().get(4);
                Integer specificValue, generalValue;

                switch (type){
                    case "I":
                        specificValue = specificStat.getInvalidations();
                        generalValue = generalStat.getInvalidations();
                        specificStat.setInvalidations(specificValue + 1);
                        generalStat.setInvalidations(generalValue + 1);
                        break;
                    case "R":
                        specificValue = specificStat.getReadRequests();
                        generalValue = generalStat.getReadRequests();
                        specificStat.setReadRequests(specificValue + 1);
                        generalStat.setReadRequests(generalValue + 1);
                        break;
                    case "W":
                        specificValue = specificStat.getWriteRequest();
                        generalValue = generalStat.getWriteRequest();
                        specificStat.setWriteRequest(specificValue + 1);
                        generalStat.setWriteRequest(generalValue + 1);
                        break;
                    case "M":
                        specificValue = specificStat.getCacheMisses();
                        generalValue = generalStat.getCacheMisses();
                        specificStat.setCacheMisses(specificValue + 1);
                        generalStat.setCacheMisses(generalValue + 1);
                        break;
                    case "H":
                        specificValue = specificStat.getCacheHits();
                        generalValue = generalStat.getCacheHits();
                        specificStat.setCacheHits(specificValue + 1);
                        generalStat.setCacheHits(generalValue + 1);
                        break;
                }

                statsTable.refresh();
            }
        });
    }
}
