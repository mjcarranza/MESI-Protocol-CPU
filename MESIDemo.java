import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class MESIDemo extends Application {

    // Líneas que representan los buses
    private Line dataBus, addressBus, sharedBus;
    // lineas verticales entre los buses y la memoria
    private Line dataMem, addrMem;
    // lineas verticales entre buses y cache
    private Line shared0, shared1, shared2, shared3;
    private Line addr0, addr1, addr2, addr3;
    private Line data0, data1, data2, data3;
    // lineas verticales entre cache y cpu
    //private Line read0, read1, read2, read3;
    //private Line write0, write1, write2, write3;
    // Mapa para almacenar las etiquetas de cada bloque de caché
    private Map<Integer, Label[]> cacheLabelsMap = new HashMap<>();
    // Mapa para almacenar las líneas de cada CPU
    private Map<Integer, Line[]> cpuLinesMap = new HashMap<>();

    private Label infoLabel;

    @Override
    public void start(Stage primaryStage) {
        // Configuración del layout principal
        GridPane root = new GridPane();
        
        // Crear y configurar la etiqueta de "Memoria Principal"
        Label memoryLabel = new Label("Memoria Principal");
        memoryLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        memoryLabel.setTextFill(Color.PURPLE);

        // Centramos la etiqueta solo horizontalmente en la pantalla usando un HBox
        HBox memoryLabelBox = new HBox(memoryLabel);
        memoryLabelBox.setAlignment(Pos.CENTER);  // Solo horizontalmente
        root.add(memoryLabelBox, 0, 0); // Agregar en la primera fila

        // Crear etiquetas de datos de memoria
        Label memoryDataLabel1 = new Label("Addr: A0,   Data: 0");
        Label memoryDataLabel2 = new Label("Addr: A1,   Data: 0");
        Label memoryDataLabel3 = new Label("Addr: A2,   Data: 0");
        Label memoryDataLabel4 = new Label("Addr: A3,   Data: 0");

        // Configurar el estilo de las etiquetas de datos de memoria
        memoryDataLabel1.setStyle("-fx-font-size: 14px;");
        memoryDataLabel2.setStyle("-fx-font-size: 14px;");
        memoryDataLabel3.setStyle("-fx-font-size: 14px;");
        memoryDataLabel4.setStyle("-fx-font-size: 14px;");

        // Colocar las etiquetas de datos de memoria en un VBox y agregar un borde
        VBox memoryDataBox = new VBox(5, memoryDataLabel1, memoryDataLabel2, memoryDataLabel3, memoryDataLabel4);
        memoryDataBox.setAlignment(Pos.CENTER_LEFT); // Solo horizontal
        memoryDataBox.setMaxWidth(VBox.USE_COMPUTED_SIZE); // Ajustar el ancho al contenido
        memoryDataBox.setStyle(
                "-fx-border-color: red; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 5; " +
                "-fx-padding: 10;"  // Espacio entre el borde y las etiquetas internas
        );

        // Crear un HBox para alinear el memoryDataBox solo horizontalmente
        HBox centeredMemoryBox = new HBox(memoryDataBox);
        centeredMemoryBox.setAlignment(Pos.CENTER); // Solo horizontalmente
        root.add(centeredMemoryBox, 0, 1); // Agregar debajo de la etiqueta "Memoria Principal"

        /////// -------------------------------- ^^^ memoria principal ^^^ ---------------------------- /////

        // lineas entre los buses y las caches
        dataMem = new Line(0, 0, 0, 50); // Línea vertical de 50px de altura
        dataMem.setStroke(Color.BLACK);
        dataMem.setStrokeWidth(5);

        addrMem = new Line(0, 0, 0, 50); // Línea vertical de 50px de altura
        addrMem.setStroke(Color.BLACK);
        addrMem.setStrokeWidth(5);

        HBox hBoxMemBus = new HBox(50); // espaciador entre lineas
        hBoxMemBus.setAlignment(Pos.CENTER); // Solo horizontalmente

        hBoxMemBus.getChildren().add(dataMem);
        hBoxMemBus.getChildren().add(addrMem);

        VBox MemBusLinesRow = new VBox(hBoxMemBus); // Espacio entre procesadores
        MemBusLinesRow.setAlignment(Pos.CENTER); // Solo alineación horizontal
        root.add(MemBusLinesRow, 0, 2); // posicion fila 2


        // lineas para los buses de datos
        dataBus = new Line(0, 0, 670, 0);  // Línea horizontal para el bus de datos
        dataBus.setStroke(Color.BLACK);
        dataBus.setStrokeWidth(5);

        addressBus = new Line(0, 0, 670, 0);  // Línea horizontal para el bus de direcciones
        addressBus.setStroke(Color.BLACK);
        addressBus.setStrokeWidth(5);

        sharedBus = new Line(0, 0, 670, 0);  // Línea horizontal para el bus compartido
        sharedBus.setStroke(Color.BLACK);
        sharedBus.setStrokeWidth(5);

        // Crear etiquetas para los buses
        Label dataBusLabel = new Label("Data Bus");
        dataBusLabel.setTextFill(Color.BLACK); // Establecer el color de texto de la etiqueta del bus de datos
        Label addressBusLabel = new Label("Address Bus");
        addressBusLabel.setTextFill(Color.BLACK); // Establecer el color de texto de la etiqueta del bus de direcciones
        Label sharedBusLabel = new Label("Shared Bus");
        sharedBusLabel.setTextFill(Color.BLACK); // Establecer el color de texto de la etiqueta del bus compartido

        // Agregar etiquetas y buses al layout
        VBox busBox = new VBox(5, dataBusLabel, dataBus, addressBusLabel, addressBus, sharedBusLabel, sharedBus);
        busBox.setAlignment(Pos.CENTER); // Alinear solo horizontalmente
        VBox.setMargin(busBox, new Insets(0, 0, 20, 0));
        root.add(busBox, 0, 3);


        // hbox para agregar las lineas de buses a cache
        shared0 = new Line(0, 0, 0, 50); // Línea vertical de 50px de altura
        shared0.setStroke(Color.BLACK);
        shared0.setStrokeWidth(5);

        addr0 = new Line(0, 0, 0, 50); 
        addr0.setStroke(Color.BLACK);
        addr0.setStrokeWidth(5);

        data0 = new Line(0, 0, 0, 50); // Línea vertical de 50px de altura
        data0.setStroke(Color.BLACK);
        data0.setStrokeWidth(5);

        HBox hBoxBusCash = new HBox(30); // espaciador entre lineas
        hBoxBusCash.setAlignment(Pos.CENTER); // Solo horizontalmente

        Region espacio01 = new Region();
        espacio01.setMinWidth(40); // Define el ancho del espaciador

        hBoxBusCash.getChildren().add(shared0);
        hBoxBusCash.getChildren().add(addr0);
        hBoxBusCash.getChildren().add(data0);
        hBoxBusCash.getChildren().add(espacio01);


        shared1 = new Line(0, 0, 0, 50); // Línea vertical de 50px de altura
        shared1.setStroke(Color.BLACK);
        shared1.setStrokeWidth(5);

        addr1 = new Line(0, 0, 0, 50); 
        addr1.setStroke(Color.BLACK);
        addr1.setStrokeWidth(5);

        data1 = new Line(0, 0, 0, 50); // Línea vertical de 50px de altura
        data1.setStroke(Color.BLACK);
        data1.setStrokeWidth(5);

        Region espacio12 = new Region();
        espacio12.setMinWidth(40); // Define el ancho del espaciador

        hBoxBusCash.getChildren().add(shared1);
        hBoxBusCash.getChildren().add(addr1);
        hBoxBusCash.getChildren().add(data1);
        hBoxBusCash.getChildren().add(espacio12);


        shared2 = new Line(0, 0, 0, 50); // Línea vertical de 50px de altura
        shared2.setStroke(Color.BLACK);
        shared2.setStrokeWidth(5);

        addr2 = new Line(0, 0, 0, 50); 
        addr2.setStroke(Color.BLACK);
        addr2.setStrokeWidth(5);

        data2 = new Line(0, 0, 0, 50); // Línea vertical de 50px de altura
        data2.setStroke(Color.BLACK);
        data2.setStrokeWidth(5);

        Region espacio23 = new Region();
        espacio23.setMinWidth(40); // Define el ancho del espaciador

        hBoxBusCash.getChildren().add(shared2);
        hBoxBusCash.getChildren().add(addr2);
        hBoxBusCash.getChildren().add(data2);
        hBoxBusCash.getChildren().add(espacio23);


        shared3 = new Line(0, 0, 0, 50); // Línea vertical de 50px de altura
        shared3.setStroke(Color.BLACK);
        shared3.setStrokeWidth(5);

        addr3 = new Line(0, 0, 0, 50); 
        addr3.setStroke(Color.BLACK);
        addr3.setStrokeWidth(5);

        data3 = new Line(0, 0, 0, 50); // Línea vertical de 50px de altura
        data3.setStroke(Color.BLACK);
        data3.setStrokeWidth(5);

        hBoxBusCash.getChildren().add(shared3);
        hBoxBusCash.getChildren().add(addr3);
        hBoxBusCash.getChildren().add(data3);


        VBox BusCashLinesRow = new VBox(hBoxBusCash); // Espacio entre procesadores
        BusCashLinesRow.setAlignment(Pos.CENTER); // Solo alineación horizontal
        root.add(BusCashLinesRow, 0, 4); // posicion fila 2



        /// ------------------------Creacion de los bloques de cache y procesadores -------------------------------------// 

        // Crear una fila para los bloques de caché y procesadores, cada uno en una columna
        HBox cpuRow = new HBox(20); // Espacio entre procesadores
        cpuRow.setAlignment(Pos.CENTER); // Solo alineación horizontal
        for (int i = 0; i < 4; i++) {  // Cambiamos a 4 procesadores
            VBox cpuAndCacheBox = new VBox(10); // VBox para alinear verticalmente caché y CPU
            cpuAndCacheBox.setAlignment(Pos.CENTER); // Centrar horizontalmente

            // Crear bloque de caché para el procesador
            VBox cacheBlock = createCacheBlock(i);
            cacheBlock.setStyle(
                "-fx-border-color: blue; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 5; " +
                "-fx-padding: 10;" // Espacio entre el borde y las etiquetas internas
            );
            cpuAndCacheBox.getChildren().add(cacheBlock); // Añadir caché arriba del CPU

            // Crear HBox para las líneas entre caché y CPU
            HBox linesBox = updateCpuLines(i);
            cpuAndCacheBox.getChildren().add(linesBox); // Añadir HBox de líneas al VBox

            // Crear controles de CPU
            VBox cpuControls = createCPUControls(i);
            cpuAndCacheBox.getChildren().add(cpuControls); // Añadir CPU debajo del caché y las líneas

            // Agregar el VBox completo al HBox de la fila
            cpuRow.getChildren().add(cpuAndCacheBox);

        }
        root.add(cpuRow, 0, 5); // Agregar la fila de procesadores en la siguiente fila del layout


        // Crear y configurar la etiqueta de "Memoria Principal"
        infoLabel = new Label("CPU Information: ");
        infoLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Centramos la etiqueta solo horizontalmente en la pantalla usando un HBox
        HBox infoLabelBox = new HBox(infoLabel);
        infoLabelBox.setAlignment(Pos.CENTER);  // Solo horizontalmente
        root.add(infoLabelBox, 0, 6); // Agregar en la primera fila


        // Configurar la escena y mostrar
        Scene scene = new Scene(root, 725, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("MESI Protocol Simulation");
        primaryStage.show(); 
    }

    // Método para crear controles de lectura/escritura para cada CPU
    @SuppressWarnings("unused")
    private VBox createCPUControls(int cpuNumber) {
        VBox cpuBox = new VBox(5); // Espacio entre cada par de botones dentro del CPU

        for (int i = 0; i < 4; i++) {  // Crear 4 pares de botones de lectura/escritura
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

        // Crear una etiqueta de identificación del procesador
        Label cpuLabel = new Label("Processor " + cpuNumber);
        cpuLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        // Agregar la etiqueta debajo de los botones
        cpuBox.getChildren().add(cpuLabel);

        // Establecer el borde del CPU
        cpuBox.setStyle(
                "-fx-border-color: black; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 5; " +
                "-fx-padding: 10;" // Espacio entre el borde y los botones internos
        );

        return cpuBox;
    }

    // Método para crear un bloque de caché con el estilo de borde de la "Memoria Principal"
    private VBox createCacheBlock(int cpuNumber) {
        Label cacheLabel0 = new Label("State: I | Addr: A0 | Data: 0");
        Label cacheLabel1 = new Label("State: I | Addr: A1 | Data: 0");
        Label cacheLabel2 = new Label("State: I | Addr: A2 | Data: 0");
        Label cacheLabel3 = new Label("State: I | Addr: A3 | Data: 0");
    
        // Crear etiqueta de identificación de caché
        Label cacheLabel = new Label("Cache " + cpuNumber);
        cacheLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
    
        // Guardar referencias de las etiquetas de datos en el mapa
        cacheLabelsMap.put(cpuNumber, new Label[]{cacheLabel0, cacheLabel1, cacheLabel2, cacheLabel3});
    
        // Estilo para las etiquetas de datos
        cacheLabel0.setStyle("-fx-font-size: 12px;");
        cacheLabel1.setStyle("-fx-font-size: 12px;");
        cacheLabel2.setStyle("-fx-font-size: 12px;");
        cacheLabel3.setStyle("-fx-font-size: 12px;");
    
        // Colocar las etiquetas en el VBox
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

    // Funcion para la creacion de lineas entre cache y cpu
    public HBox updateCpuLines(int cpuNumber) {
        // Crear dos líneas verticales
        Line line1 = new Line(0, 0, 0, 50); // Línea de 50 píxeles de altura
        line1.setStroke(Color.BLACK);
        line1.setStrokeWidth(5);

        Line line2 = new Line(0, 0, 0, 50); // Segunda línea de 50 píxeles de altura
        line2.setStroke(Color.BLACK);
        line2.setStrokeWidth(5);
        
        // Almacenar las líneas en el mapa
        cpuLinesMap.put(cpuNumber, new Line[]{line1, line2});

        // Crear un contenedor HBox para las líneas
        HBox linesBox = new HBox(40, line1, line2); // Espacio entre las líneas
        linesBox.setAlignment(Pos.CENTER); // Centrar horizontalmente

        return linesBox;
    }

    // Acción de lectura
    private void onRead(int cpuNumber, int pairNumber) {

        // llamar funcion get para obtener newState, newAddress y newData, etc. (sutituir los valores en el IF)    -------------------------------------------------

        Label[] labels = cacheLabelsMap.get(cpuNumber); // obtener el arreglo de etiquetas correspondiente al cpuNumber
        if (labels != null && pairNumber >= 0 && pairNumber < labels.length) {
            labels[pairNumber].setText("State: " + "E" + " | Addr: A" + pairNumber + " | Data: " + "4");
        }

        //System.out.println("CPU " + cpuNumber + " - Read operation to address A" + pairNumber);
        
        // Agregar logica para cada lectura (tomar en cuenta todos los posibles casos) 
        infoLabel.setTextFill(Color.RED);
        infoLabel.setText("CPU Information: CPU " + cpuNumber + " - Read operation to address A" + pairNumber);
        addressBus.setStroke(Color.GREEN);  // Cambiar color del bus de dirección
        dataBus.setStroke(Color.RED);     // Cambiar color del bus de datos
        sharedBus.setStroke(Color.BLUEVIOLET);   // Cambiar color del bus compartido
    }

    // Acción de escritura
    private void onWrite(int cpuNumber, int pairNumber) {
        System.out.println("CPU " + cpuNumber + " - Write operation to address A" + pairNumber);
        
        // Agregar logica para cada escritura (tomar en cuenta todos los posibles casos)
        infoLabel.setTextFill(Color.RED);
        infoLabel.setText("CPU Information: CPU " + cpuNumber + " - Write operation to address A" + pairNumber);
        addressBus.setStroke(Color.GREEN); // Cambiar color del bus de dirección
        dataBus.setStroke(Color.RED);    // Cambiar color del bus de datos
        sharedBus.setStroke(Color.BLUEVIOLET);  // Cambiar color del bus compartido
    }

    public static void main(String[] args) {
        launch(args);
    }
}
