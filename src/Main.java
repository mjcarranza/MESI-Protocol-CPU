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

    // Lines representing the buses
    private Line dataBus, addressBus, sharedBus;
    // Vertical lines between buses and memory
    private Line dataMemory, addressMemory;
    // Vertical lines between buses and cache
    private Line shared0, shared1, shared2, shared3;
    private Line address0, address1, address2, address3;
    private Line data0, data1, data2, data3;
    // Map to store labels for each cache block
    private Map<Integer, Label[]> cacheLabelsMap = new HashMap<>();
    // Map to store lines for each CPU
    private Map<Integer, Line[]> cpuLinesMap = new HashMap<>();
    private TableView<Statistic> statsTable;
    private ObservableList<Statistic> statsData;
    private Label infoLabel;
    private Label memoryLabel;
    private HBox memoryLabelBox;
    private GridPane root;
    private Scene scene;
    private Label memoryDataLabel1, memoryDataLabel2, memoryDataLabel3, memoryDataLabel4;
    private VBox memoryDataBox;
    private HBox centeredMemoryBox;
    private HBox memoryBusHBox;
    private VBox memoryBusLinesRow;
    private Label dataBusLabel, addessBusLabel, sharedBusLabel;
    private VBox busBox;
    private HBox busCashHBox;
    private Region space01, space12, space23;
    private VBox busCashLinesRow;
    private HBox cpuRow;
    private HBox infoLabelBox;
    private VBox statsBox;

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Main layout configuration
        /* this.root = new GridPane();
        this.root.setPadding(new Insets(0, 0, 0, 20));

        this.setMemoryLabel();
        this.setMemoryDataLabels();
        this.setMemoryDataBox();
        this.setCenteredMemoryBox();

        // ---------------------------- main memory ---------------------------- //
        // Lines between buses and caches
        this.setDataMemory();
        this.setAddressMemory();
        this.setMemoryBusHBox();
        this.setMemoryBusLinesRow();

        // Lines for the data buses
        this.setDataBus();
        this.setAddressBus();
        this.setSharedBus();

        // Create labels for the buses
        this.setBusesLabels();
        this.setBusBox();

        this.setCache0Lines();
        this.setCache1Lines();
        this.setCache2Lines();
        this.setCache3Lines();
        this.setRegions();
        this.setBusCashHBox();
        this.setBusCashLinesRow();

        // -------------------- Creating cache blocks and processors -------------------- //
        this.setCpuRow();
        this.setInfoLabel();
        this.setInfoLabelBox();

        // -------------------- Code for GRID Column 2 -------------------- //
        this.setStatsTable();
        this.setStatsBox();*/


        // ---------------------------- Scene Configuration ---------------------------- //
        Parent root = FXMLLoader.load(getClass().getResource("window.fxml"));
        this.scene = new Scene(root, 1200, 850);
        primaryStage.setScene(this.scene);
        primaryStage.setTitle("MESI Protocol Simulation");
        primaryStage.show();
    }

    private void setStatsBox() {
        // Align and add the TableView to the GridPane
        this.statsBox = new VBox(10, new Label("System Statistics"), this.statsTable);
        this.statsBox.setAlignment(Pos.CENTER);

        // Position the Vbox in column 2, row 3 and span rows 3 and 4
        this.root.add(this.statsBox, 2, 1);
        GridPane.setRowSpan(this.statsBox, 2);
    }

    private void setStatsTable() {
        // Create and configure the TableView to display statistics
        this.statsTable = new TableView<>();
        this.statsTable.setPrefWidth(300);

        // Create columns
        TableColumn<Statistic, String> nameColumn = new TableColumn<>("Stat");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(150);

        TableColumn<Statistic, Integer> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        valueColumn.setPrefWidth(100);

        // Add columns to TableView
        this.statsTable.getColumns().add(nameColumn);
        this.statsTable.getColumns().add(valueColumn);

        // Add some example data
        ObservableList<Statistic> statsData = FXCollections.observableArrayList(
                new Statistic("Cache Misses"),
                new Statistic("Cache Hits"),
                new Statistic("Read Requests"),
                new Statistic("Write Requests")
        );

        this.statsTable.setItems(statsData);

        this.statsTable.setPrefHeight(150); // Preferred height (for multiple lines)
        this.statsTable.setMinHeight(150); // Minimum height
        this.statsTable.setMaxHeight(150); // Maximum height
    }

    private void setInfoLabelBox() {
        // Center the label horizontally only on the screen using an HBox
        this.infoLabelBox = new HBox(this.infoLabel);
        this.infoLabelBox.setAlignment(Pos.CENTER); // Horizontal only
        this.root.add(this.infoLabelBox, 0, 7); // Add in the first row
    }

    private void setInfoLabel() {
        // Create and configure the "Main Memory" Label
        this.infoLabel = new Label("CPU Information: ");
        this.infoLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
    }

    private void setCpuRow() {
        // Create a row for the cache blocks and processors, each in a column
        this.cpuRow = new HBox(20); // Space between processors
        this.cpuRow.setAlignment(Pos.CENTER); // Horizontal alignment only

        for (int i = 0; i < 4; i++) { // Changed to 4 processors
            VBox cpuAndCacheBox = new VBox(10); // VBox to vertically align cache and CPU
            cpuAndCacheBox.setAlignment(Pos.CENTER); // Center horizontally

            // Create cache block for the processor
            VBox cacheBlock = createCacheBlock(i);
            cacheBlock.setStyle(
                    "-fx-border-color: blue; " +
                    "-fx-border-width: 2; " +
                    "-fx-border-radius: 5; " +
                    "-fx-padding: 10;" // Space between the border and internal labels
            );

            cpuAndCacheBox.getChildren().add(cacheBlock); // Add cache above CPU

            // Create HBox for the lines between cache and CPU
            HBox linesBox = updateCpuLines(i);
            cpuAndCacheBox.getChildren().add(linesBox); // Add lines HBox to VBox

            // Create CPU controls
            VBox cpuControls = createCPUControls(i);
            cpuAndCacheBox.getChildren().add(cpuControls); // Add CPU below cache and lines

            // Add the complete VBox to the HBox of the row
            this.cpuRow.getChildren().add(cpuAndCacheBox);
        }

        this.root.add(this.cpuRow, 0, 5); // Add processor row to the next layout row
    }

    // Method to create read/write controls for each CPU
    private VBox createCPUControls(int cpuNumber) {
        // Load image from resources or file system
        Image image = new Image("file:cpu.png");

        // Create ImageView and configure properties
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(140);
        imageView.setFitHeight(140);
        imageView.setPreserveRatio(true);

        VBox cpuBox = new VBox(5); // Space between each pair of buttons within CPU

        // Create processor identification label
        Label cpuLabel = new Label("Processor " + cpuNumber);
        cpuLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        // Add label below buttons
        cpuBox.getChildren().add(imageView);
        cpuBox.getChildren().add(cpuLabel);

        // Set CPU border
        cpuBox.setStyle(
                "-fx-border-color: black; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 5; " +
                        "-fx-padding: 10;" // Space between border and internal buttons
        );

        return cpuBox;
    }

    private HBox updateCpuLines(int cpuNumber) {
        // Function for creating lines between cache and CPU

        // Create two vertical lines
        Line line1 = new Line(0, 0, 0, 50); // 50-pixel height line
        line1.setStroke(Color.BLACK);
        line1.setStrokeWidth(5);

        Line line2 = new Line(0, 0, 0, 50); // Second 50-pixel height line
        line2.setStroke(Color.BLACK);
        line2.setStrokeWidth(5);

        // Store lines in map
        cpuLinesMap.put(cpuNumber, new Line[]{line1, line2});

        // Create HBox container for lines
        HBox linesBox = new HBox(40, line1, line2); // Space between lines
        linesBox.setAlignment(Pos.CENTER); // Center horizontally

        return linesBox;
    }

    private VBox createCacheBlock(int cpuNumber) {
        // Method to create a cache block with "Main Memory" border style
        Label cacheLabel0 = new Label("State: I | Addr: A0 | Data: 0");
        Label cacheLabel1 = new Label("State: I | Addr: A1 | Data: 0");
        Label cacheLabel2 = new Label("State: I | Addr: A2 | Data: 0");
        Label cacheLabel3 = new Label("State: I | Addr: A3 | Data: 0");

        // Create cache identification label
        Label cacheLabel = new Label("Cache " + cpuNumber);
        cacheLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        // Save references of data labels in map
        this.cacheLabelsMap.put(cpuNumber, new Label[]{cacheLabel0, cacheLabel1, cacheLabel2, cacheLabel3});

        // Style for data labels
        cacheLabel0.setStyle("-fx-font-size: 12px;");
        cacheLabel1.setStyle("-fx-font-size: 12px;");
        cacheLabel2.setStyle("-fx-font-size: 12px;");
        cacheLabel3.setStyle("-fx-font-size: 12px;");

        // Place labels in VBox
        VBox cacheDataBox = new VBox(5, cacheLabel0, cacheLabel1, cacheLabel2, cacheLabel3, cacheLabel);
        cacheDataBox.setAlignment(Pos.CENTER);
        cacheDataBox.setStyle(
                "-fx-border-color: black; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 5; " +
                "-fx-padding: 10;"
        );

        return cacheDataBox;
    }

    private void setBusCashLinesRow() {
        this.busCashLinesRow = new VBox(this.busCashHBox); // Space between processors
        this.busCashLinesRow.setAlignment(Pos.CENTER); // Horizontal alignment only
        this.busCashLinesRow.setPadding(new Insets(10, 0, 10, 0));
        this.root.add(this.busCashLinesRow, 0, 4); // Position in row 2
    }

    private void setBusCashHBox() {
        this.busCashHBox = new HBox(30); // Spacer between lines
        this.busCashHBox.setAlignment(Pos.CENTER); // Align horizontally only

        this.busCashHBox.getChildren().add(this.shared0);
        this.busCashHBox.getChildren().add(this.address0);
        this.busCashHBox.getChildren().add(this.data0);
        this.busCashHBox.getChildren().add(space01);

        this.busCashHBox.getChildren().add(this.shared1);
        this.busCashHBox.getChildren().add(this.address1);
        this.busCashHBox.getChildren().add(this.data1);
        this.busCashHBox.getChildren().add(this.space12);

        this.busCashHBox.getChildren().add(this.shared2);
        this.busCashHBox.getChildren().add(this.address2);
        this.busCashHBox.getChildren().add(this.data2);
        this.busCashHBox.getChildren().add(this.space23);

        this.busCashHBox.getChildren().add(this.shared3);
        this.busCashHBox.getChildren().add(this.address3);
        this.busCashHBox.getChildren().add(this.data3);
    }

    private void setRegions() {
        this.space01 = new Region();
        this.space01.setMinWidth(40); // Define the width of the spacer

        this.space12 = new Region();
        this.space12.setMinWidth(40); // Define the width of the spacer

        this.space23 = new Region();
        this.space23.setMinWidth(40);
    }

    private void setCache3Lines() {
        this.shared3 = new Line(0, 0, 0, 50); // Vertical line with a height of 50px
        this.shared3.setStroke(Color.BLACK);
        this.shared3.setStrokeWidth(5);

        this.address3 = new Line(0, 0, 0, 50);
        this.address3.setStroke(Color.BLACK);
        this.address3.setStrokeWidth(5);

        this.data3 = new Line(0, 0, 0, 50);
        this.data3.setStroke(Color.BLACK);
        this.data3.setStrokeWidth(5);
    }

    private void setCache2Lines() {
        this.shared2 = new Line(0, 0, 0, 50); // Vertical line with a height of 50px
        this.shared2.setStroke(Color.BLACK);
        this.shared2.setStrokeWidth(5);

        this.address2 = new Line(0, 0, 0, 50);
        this.address2.setStroke(Color.BLACK);
        this.address2.setStrokeWidth(5);

        this.data2 = new Line(0, 0, 0, 50);
        this.data2.setStroke(Color.BLACK);
        this.data2.setStrokeWidth(5);
    }

    private void setCache1Lines() {
        this.shared1 = new Line(0, 0, 0, 50); // Vertical line with a height of 50px
        this.shared1.setStroke(Color.BLACK);
        this.shared1.setStrokeWidth(5);

        this.address1 = new Line(0, 0, 0, 50);
        this.address1.setStroke(Color.BLACK);
        this.address1.setStrokeWidth(5);

        this.data1 = new Line(0, 0, 0, 50);
        this.data1.setStroke(Color.BLACK);
        this.data1.setStrokeWidth(5);
    }

    private void setCache0Lines() {
        this.shared0 = new Line(0, 0, 0, 50); // Vertical line with a height of 50px
        this.shared0.setStroke(Color.BLACK);
        this.shared0.setStrokeWidth(5);

        this.address0 = new Line(0, 0, 0, 50);
        this.address0.setStroke(Color.BLACK);
        this.address0.setStrokeWidth(5);

        this.data0 = new Line(0, 0, 0, 50);
        this.data0.setStroke(Color.BLACK);
        this.data0.setStrokeWidth(5);
    }

    private void setBusBox() {
        // Add labels and buses to the layout
        this.busBox = new VBox(
                5,
                this.dataBusLabel,
                this.dataBus,
                this.addessBusLabel,
                this.addressBus,
                this.sharedBusLabel,
                this.sharedBus
        );
        this.busBox.setAlignment(Pos.CENTER); // Align horizontally only
        VBox.setMargin(this.busBox, new Insets(0, 0, 20, 0));
        this.root.add(this.busBox, 0, 3);
    }

    private void setBusesLabels() {
        this.dataBusLabel = new Label("Data Bus");
        this.dataBusLabel.setTextFill(Color.RED); // Set text color for the data bus label

        this.addessBusLabel = new Label("Address Bus");
        this.addessBusLabel.setTextFill(Color.GREEN); // Set text color for the address bus label

        this.sharedBusLabel = new Label("Shared Bus");
        this.sharedBusLabel.setTextFill(Color.BLUEVIOLET); // Set text color for the shared bus label
    }

    private void setSharedBus() {
        this.sharedBus = new Line(0, 0, 670, 0);
        this.sharedBus.setStroke(Color.BLACK);
        this.sharedBus.setStrokeWidth(5);
    }

    private void setAddressBus() {
        this.addressBus = new Line(0, 0, 670, 0); // Horizontal line for the address bus
        this.addressBus.setStroke(Color.BLACK);
        this.addressBus.setStrokeWidth(5);
    }

    private void setDataBus() {
        this.dataBus = new Line(0, 0, 670, 0); // Horizontal line for the data bus
        this.dataBus.setStroke(Color.BLACK);
        this.dataBus.setStrokeWidth(5);
    }

    private void setMemoryBusLinesRow() {
        this.memoryBusLinesRow = new VBox(this.memoryBusHBox); // Space between processors
        this.memoryBusLinesRow.setAlignment(Pos.CENTER); // Horizontal alignment only
        this.root.add(this.memoryBusLinesRow, 0, 2); // Position row 2
    }

    private void setMemoryBusHBox() {
        this.memoryBusHBox = new HBox(50); // Spacer between lines
        this.memoryBusHBox.setAlignment(Pos.CENTER); // Horizontal only
        this.memoryBusHBox.getChildren().add(this.dataMemory);
        this.memoryBusHBox.getChildren().add(this.addressMemory);
    }

    private void setAddressMemory() {
        this.addressMemory = new Line(0, 0, 0, 50); // Vertical line with 50px height
        this.addressMemory.setStroke(Color.BLACK);
        this.addressMemory.setStrokeWidth(5);
    }

    private void setDataMemory() {
        this.dataMemory = new Line(0, 0, 0, 50); // Vertical line with 50px height
        this.dataMemory.setStroke(Color.BLACK);
        this.dataMemory.setStrokeWidth(5);
    }

    private void setCenteredMemoryBox() {
        // Create an HBox to align memoryDataBox horizontally only
        this.centeredMemoryBox = new HBox(this.memoryDataBox);
        this.centeredMemoryBox.setAlignment(Pos.CENTER); // Horizontal only
        this.root.add(this.centeredMemoryBox, 0, 1); // Add below the "Main Memory" label
    }

    private void setMemoryDataBox() {
        // Place memory data labels in a VBox and add a border
        this.memoryDataBox = new VBox(5, this.memoryDataLabel1, this.memoryDataLabel2, this.memoryDataLabel3, this.memoryDataLabel4);
        this.memoryDataBox.setAlignment(Pos.CENTER_LEFT);
        this.memoryDataBox.setMaxWidth(VBox.USE_COMPUTED_SIZE);
        this.memoryDataBox.setStyle(
                "-fx-border-color: red; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 5; " +
                        "-fx-padding: 10;"  // Space between the border and inner labels
        );
    }

    private void setMemoryDataLabels() {
        // Create memory data labels
        this.memoryDataLabel1 = new Label("Addr: A0,   Data: 0");
        this.memoryDataLabel2 = new Label("Addr: A1,   Data: 0");
        this.memoryDataLabel3 = new Label("Addr: A2,   Data: 0");
        this.memoryDataLabel4 = new Label("Addr: A3,   Data: 0");

        // Configure the style of the memory data labels
        this.memoryDataLabel1.setStyle("-fx-font-size: 14px;");
        this.memoryDataLabel2.setStyle("-fx-font-size: 14px;");
        this.memoryDataLabel3.setStyle("-fx-font-size: 14px;");
        this.memoryDataLabel4.setStyle("-fx-font-size: 14px;");
    }

    private void setMemoryLabel() {
        // Create and configure the "Main Memory" Label
        this.memoryLabel = new Label("Main Memory");
        this.memoryLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        this.memoryLabel.setTextFill(Color.PURPLE);

        // Center the label only horizontally on the screen using an HBox
        this.memoryLabelBox = new HBox(this.memoryLabel);
        this.memoryLabelBox.setAlignment(Pos.CENTER); // Horizontal only
        this.root.add(memoryLabelBox, 0, 0); // Add to the first row
    }

    public static void main(String[] args) {
        launch(args);
    }
}
