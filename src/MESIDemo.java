package src;

import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class MESIDemo extends Application {

    // Lines representing the buses
    private Line dataBus, addressBus, sharedBus;
    // Vertical lines between buses and memory
    private Line dataMem, addrMem;
    // Vertical lines between buses and cache
    private Line shared0, shared1, shared2, shared3;
    private Line addr0, addr1, addr2, addr3;
    private Line data0, data1, data2, data3;
    // Instance variable for the TextField
    private TextArea inputArea;
    // Map to store labels for each cache block
    private Map<Integer, Label[]> cacheLabelsMap = new HashMap<>();
    // Map to store lines for each CPU
    private Map<Integer, Line[]> cpuLinesMap = new HashMap<>();
    private TableView<Statistic> statsTable;  // TableView for statistics
    private ObservableList<Statistic> statsData; // Data list for the table
    private Label infoLabel;


    @SuppressWarnings("unused")
    @Override
    public void start(Stage primaryStage) {
        // Main layout configuration
        GridPane root = new GridPane();
        
        // Create and configure the "Main Memory" label
        Label memoryLabel = new Label("Main Memory");
        memoryLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        memoryLabel.setTextFill(Color.PURPLE);

        // Center the label only horizontally on the screen using an HBox
        HBox memoryLabelBox = new HBox(memoryLabel);
        memoryLabelBox.setAlignment(Pos.CENTER);  // Horizontal only
        root.add(memoryLabelBox, 0, 0); // Add to the first row

        // Create memory data labels
        Label memoryDataLabel1 = new Label("Addr: A0,   Data: 0");
        Label memoryDataLabel2 = new Label("Addr: A1,   Data: 0");
        Label memoryDataLabel3 = new Label("Addr: A2,   Data: 0");
        Label memoryDataLabel4 = new Label("Addr: A3,   Data: 0");

        // Configure the style of the memory data labels
        memoryDataLabel1.setStyle("-fx-font-size: 14px;");
        memoryDataLabel2.setStyle("-fx-font-size: 14px;");
        memoryDataLabel3.setStyle("-fx-font-size: 14px;");
        memoryDataLabel4.setStyle("-fx-font-size: 14px;");

        // Place memory data labels in a VBox and add a border
        VBox memoryDataBox = new VBox(5, memoryDataLabel1, memoryDataLabel2, memoryDataLabel3, memoryDataLabel4);
        memoryDataBox.setAlignment(Pos.CENTER_LEFT); // Horizontal only
        memoryDataBox.setMaxWidth(VBox.USE_COMPUTED_SIZE); // Adjust width to content
        memoryDataBox.setStyle(
                "-fx-border-color: red; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 5; " +
                "-fx-padding: 10;"  // Space between the border and inner labels
        );

        // Create an HBox to align memoryDataBox horizontally only
        HBox centeredMemoryBox = new HBox(memoryDataBox);
        centeredMemoryBox.setAlignment(Pos.CENTER); // Horizontal only
        root.add(centeredMemoryBox, 0, 1); // Add below the "Main Memory" label

        /////// -------------------------------- ^^^ main memory ^^^ ---------------------------- /////

        // Lines between buses and caches
        dataMem = new Line(0, 0, 0, 50); // Vertical line with 50px height
        dataMem.setStroke(Color.BLACK);
        dataMem.setStrokeWidth(5);

        addrMem = new Line(0, 0, 0, 50); // Vertical line with 50px height
        addrMem.setStroke(Color.BLACK);
        addrMem.setStrokeWidth(5);

        HBox hBoxMemBus = new HBox(50); // Spacer between lines
        hBoxMemBus.setAlignment(Pos.CENTER); // Horizontal only

        hBoxMemBus.getChildren().add(dataMem);
        hBoxMemBus.getChildren().add(addrMem);

        VBox MemBusLinesRow = new VBox(hBoxMemBus); // Space between processors
        MemBusLinesRow.setAlignment(Pos.CENTER); // Horizontal alignment only
        root.add(MemBusLinesRow, 0, 2); // position row 2


        // Lines for the data buses
        dataBus = new Line(0, 0, 670, 0);  // Horizontal line for the data bus
        dataBus.setStroke(Color.BLACK);
        dataBus.setStrokeWidth(5);

        addressBus = new Line(0, 0, 670, 0);  // Horizontal line for the address bus
        addressBus.setStroke(Color.BLACK);
        addressBus.setStrokeWidth(5);

        sharedBus = new Line(0, 0, 670, 0);  // Horizontal line for the shared bus
        sharedBus.setStroke(Color.BLACK);
        sharedBus.setStrokeWidth(5);

        // Create labels for the buses
        Label dataBusLabel = new Label("Data Bus");
        dataBusLabel.setTextFill(Color.RED); // Set text color for the data bus label
        Label addressBusLabel = new Label("Address Bus");
        addressBusLabel.setTextFill(Color.GREEN); // Set text color for the address bus label
        Label sharedBusLabel = new Label("Shared Bus");
        sharedBusLabel.setTextFill(Color.BLUEVIOLET); // Set text color for the shared bus label

        // Add labels and buses to the layout
        VBox busBox = new VBox(5, dataBusLabel, dataBus, addressBusLabel, addressBus, sharedBusLabel, sharedBus);
        busBox.setAlignment(Pos.CENTER); // Align horizontally only
        VBox.setMargin(busBox, new Insets(0, 0, 20, 0));
        root.add(busBox, 0, 3);

        // HBox to add the bus lines to the cache
        shared0 = new Line(0, 0, 0, 50); // Vertical line with a height of 50px
        shared0.setStroke(Color.BLACK);
        shared0.setStrokeWidth(5);

        addr0 = new Line(0, 0, 0, 50);
        addr0.setStroke(Color.BLACK);
        addr0.setStrokeWidth(5);

        data0 = new Line(0, 0, 0, 50); // Vertical line with a height of 50px
        data0.setStroke(Color.BLACK);
        data0.setStrokeWidth(5);

        HBox hBoxBusCash = new HBox(30); // Spacer between lines
        hBoxBusCash.setAlignment(Pos.CENTER); // Align horizontally only

        Region espacio01 = new Region();
        espacio01.setMinWidth(40); // Define the width of the spacer

        hBoxBusCash.getChildren().add(shared0);
        hBoxBusCash.getChildren().add(addr0);
        hBoxBusCash.getChildren().add(data0);
        hBoxBusCash.getChildren().add(espacio01);

        shared1 = new Line(0, 0, 0, 50); // Vertical line with a height of 50px
        shared1.setStroke(Color.BLACK);
        shared1.setStrokeWidth(5);

        addr1 = new Line(0, 0, 0, 50);
        addr1.setStroke(Color.BLACK);
        addr1.setStrokeWidth(5);

        data1 = new Line(0, 0, 0, 50); // Vertical line with a height of 50px
        data1.setStroke(Color.BLACK);
        data1.setStrokeWidth(5);

        Region espacio12 = new Region();
        espacio12.setMinWidth(40); // Define the width of the spacer

        hBoxBusCash.getChildren().add(shared1);
        hBoxBusCash.getChildren().add(addr1);
        hBoxBusCash.getChildren().add(data1);
        hBoxBusCash.getChildren().add(espacio12);

        shared2 = new Line(0, 0, 0, 50); // Vertical line with a height of 50px
        shared2.setStroke(Color.BLACK);
        shared2.setStrokeWidth(5);

        addr2 = new Line(0, 0, 0, 50);
        addr2.setStroke(Color.BLACK);
        addr2.setStrokeWidth(5);

        data2 = new Line(0, 0, 0, 50); // Vertical line with a height of 50px
        data2.setStroke(Color.BLACK);
        data2.setStrokeWidth(5);

        Region espacio23 = new Region();
        espacio23.setMinWidth(40); // Define the width of the spacer

        hBoxBusCash.getChildren().add(shared2);
        hBoxBusCash.getChildren().add(addr2);
        hBoxBusCash.getChildren().add(data2);
        hBoxBusCash.getChildren().add(espacio23);

        shared3 = new Line(0, 0, 0, 50); // Vertical line with a height of 50px
        shared3.setStroke(Color.BLACK);
        shared3.setStrokeWidth(5);

        addr3 = new Line(0, 0, 0, 50);
        addr3.setStroke(Color.BLACK);
        addr3.setStrokeWidth(5);

        data3 = new Line(0, 0, 0, 50); // Vertical line with a height of 50px
        data3.setStroke(Color.BLACK);
        data3.setStrokeWidth(5);

        hBoxBusCash.getChildren().add(shared3);
        hBoxBusCash.getChildren().add(addr3);
        hBoxBusCash.getChildren().add(data3);

        VBox BusCashLinesRow = new VBox(hBoxBusCash); // Space between processors
        BusCashLinesRow.setAlignment(Pos.CENTER); // Horizontal alignment only
        root.add(BusCashLinesRow, 0, 4); // Position in row 2

        /// ------------------------Creating cache blocks and processors -------------------------------------//

        // Create a row for the cache blocks and processors, each in a column
        HBox cpuRow = new HBox(20); // Space between processors
        cpuRow.setAlignment(Pos.CENTER); // Horizontal alignment only
        for (int i = 0; i < 4; i++) {  // Changed to 4 processors
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
            cpuRow.getChildren().add(cpuAndCacheBox);
        }
        root.add(cpuRow, 0, 5); // Add processor row to the next layout row

        // Button stepButton = new Button("Step");  // Button for step-by-step execution
        
        // stepButton.setOnAction(e -> onRead()); // Set button event

        // Create and configure the "Main Memory" label
        infoLabel = new Label("CPU Information: ");
        infoLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Center the label horizontally only on the screen using an HBox
        // HBox btnBox = new HBox(stepButton);
        // btnBox.setAlignment(Pos.CENTER);  // Horizontal only
        // btnBox.setStyle("-fx-padding: 10;");
        // root.add(btnBox, 0, 6); // Add in the first row

        // Center the label horizontally only on the screen using an HBox
        HBox infoLabelBox = new HBox(infoLabel);
        infoLabelBox.setAlignment(Pos.CENTER);  // Horizontal only
        root.add(infoLabelBox, 0, 7); // Add in the first row


        /////////////////////////////////////////////// CODE FOR GRID COLUMN 2 /////////////////////////////////////
        
        // Create Label and TextField for column 2
        Label inputLabel = new Label("Enter Your Code Here:");
        inputLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        inputArea = new TextArea();  // TextArea instance
        inputArea.setPromptText("Code...");
        inputArea.setPrefWidth(450);  // Preferred width
        inputArea.setPrefHeight(100); // Preferred height (for multiple lines)
        inputArea.setMinHeight(100);     // Minimum height
        inputArea.setMaxHeight(100);     // Maximum height

        // Align content of column 2
        VBox inputBox = new VBox(10, inputArea);
        inputBox.setAlignment(Pos.CENTER_LEFT);
        root.add(inputLabel, 2, 0);
        root.add(inputBox, 2, 1);

        // Create and add button to display entered value
        Button showInputButton = new Button("Load Code");
        showInputButton.setOnAction(e -> printInputValue()); // Calls the function on click
        root.add(showInputButton, 2, 2); // Add button below TextField


        // Create and configure the TableView to display statistics
        statsTable = new TableView<>();
        statsTable.setPrefWidth(300);
        
        // Create columns
        TableColumn<Statistic, String> nameColumn = new TableColumn<>("Stat");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(150);

        TableColumn<Statistic, Integer> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        valueColumn.setPrefWidth(100);

        // Add columns to TableView
        statsTable.getColumns().add(nameColumn);
        statsTable.getColumns().add(valueColumn);

        // Add some example data
        /*ObservableList<Statistic> statsData = FXCollections.observableArrayList(
            new Statistic("Cache Misses", 0),
            new Statistic("Cache Hits", 0),
            new Statistic("Read Requests", 0),
            new Statistic("Write Requests", 0)
        );*/
        statsTable.setItems(statsData);

        statsTable.setPrefHeight(150); // Preferred height (for multiple lines)
        statsTable.setMinHeight(150);     // Minimum height
        statsTable.setMaxHeight(150);     // Maximum height

        // Align and add the TableView to the GridPane
        VBox statsBox = new VBox(10, new Label("System Statistics"), statsTable);
        statsBox.setAlignment(Pos.CENTER);
        // Position the VBox in column 2, row 3, and span rows 3 and 4
        root.add(statsBox, 2, 3);          // Add to column 2, row 3
        GridPane.setRowSpan(statsBox, 2);  // Span rows 3 and 4 for TableView


        /////////////////////// SCENE CONFIGURATION ///////////////////////


        // Configure the scene and display
        Scene scene = new Scene(root, 1200, 850); // coordinates (x, y)
        primaryStage.setScene(scene);
        primaryStage.setTitle("MESI Protocol Simulation");
        primaryStage.show();

        // startServer();
    }

    // Function to get and display entered text in console
    public void printInputValue() {
        String inputText = inputArea.getText();
        System.out.println("Entered value: " + inputText);
        inputArea.clear(); // Clear text from TextField
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

    // Method to create a cache block with "Main Memory" border style
    private VBox createCacheBlock(int cpuNumber) {
        Label cacheLabel0 = new Label("State: I | Addr: A0 | Data: 0");
        Label cacheLabel1 = new Label("State: I | Addr: A1 | Data: 0");
        Label cacheLabel2 = new Label("State: I | Addr: A2 | Data: 0");
        Label cacheLabel3 = new Label("State: I | Addr: A3 | Data: 0");
    
        // Create cache identification label
        Label cacheLabel = new Label("Cache " + cpuNumber);
        cacheLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
    
        // Save references of data labels in map
        cacheLabelsMap.put(cpuNumber, new Label[]{cacheLabel0, cacheLabel1, cacheLabel2, cacheLabel3});
    
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

    // Function to update a specific statistic
    public void updateStatistic(String statName, int newValue) {
        // Find statistic in list and update its value
        for (Statistic stat : statsData) {
            if (stat.getName().equals(statName)) {
                stat.setInvalidations(newValue); // Update value
                statsTable.refresh(); // Refresh table to display change
                break;
            }
        }
    }

    // Function for creating lines between cache and CPU
    public HBox updateCpuLines(int cpuNumber) {
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

    // Read action
    private void onRead() {

        // Call get function to obtain newState, newAddress and newData, etc. (replace values in IF)    -------------------------------------------------
        /* 
        Label[] labels = cacheLabelsMap.get(cpuNumber); // get label array corresponding to cpuNumber
        if (labels != null && pairNumber >= 0 && pairNumber < labels.length) {
            labels[pairNumber].setText("State: " + "E" + " | Addr: A" + pairNumber + " | Data: " + "4");
        }
        */

        //System.out.println("CPU " + cpuNumber + " - Read operation to address A" + pairNumber);
        
        // Add logic for each read (consider all possible cases) 
        infoLabel.setTextFill(Color.RED);
        infoLabel.setText("CPU Information: CPU " + "[cpuNumber]" + " - Read operation to address A" + "[dirNumber]");
        addressBus.setStroke(Color.GREEN);  // Change color of address bus
        dataBus.setStroke(Color.RED);     // Change color of data bus
        sharedBus.setStroke(Color.BLUEVIOLET);   // Change color of shared bus
    }

    // Write action
    /* 
    private void onWrite(int cpuNumber, int pairNumber) {
        System.out.println("CPU " + cpuNumber + " - Write operation to address A" + pairNumber);
        
        // Add logic for each write (consider all possible cases)
        infoLabel.setTextFill(Color.RED);
        infoLabel.setText("CPU Information: CPU " + cpuNumber + " - Write operation to address A" + pairNumber);
        addressBus.setStroke(Color.GREEN); // Change color of address bus
        dataBus.setStroke(Color.RED);    // Change color of data bus
        sharedBus.setStroke(Color.BLUEVIOLET);  // Change color of shared bus
    }
    */

    public static void main(String[] args) {
        launch(args);
    }
}




/*
 * for (int i = 0; i < 4; i++) {  // Crear 4 pares de botones de lectura/escritura
            final int pairNumber = i; // Variable local efectivamente final
            Button readButton = new Button("Read A" + pairNumber);
            Button writeButton = new Button("Write A" + pairNumber);

            // Configurar eventos de botón
            readButton.setOnAction(e -> onRead(cpuNumber, pairNumber));
            writeButton.setOnAction(e -> onWrite(cpuNumber, pairNumber));

            // Agregar el par de botones en un HBox
            HBox buttonPair = new HBox(5, readButton, writeButton);
            buttonPair.setAlignment(Pos.CENTER_LEFT); // Solo horizontal
            cpuBox.getChildren().add(buttonPair);
        }
 * 
 */