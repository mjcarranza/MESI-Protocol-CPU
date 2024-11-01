import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class MESIDemo extends Application {

    // Líneas que representan los buses
    private Line dataBus;
    private Line addressBus;
    private Line sharedBus;

    // Líneas verticales entre la caché y el CPU para cada procesador
    //private Line[] cacheToCpuLines = new Line[4]; // Arreglo para las líneas entre caché y CPU

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
        Label memoryDataLabel1 = new Label("Address: A0, Data: 0");
        Label memoryDataLabel2 = new Label("Address: A1, Data: 0");
        Label memoryDataLabel3 = new Label("Address: A2, Data: 0");
        Label memoryDataLabel4 = new Label("Address: A3, Data: 0");

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
                "-fx-border-color: black; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 5; " +
                "-fx-padding: 10;" // Espacio entre el borde y las etiquetas internas
        );

        // Crear un HBox para alinear el memoryDataBox solo horizontalmente
        HBox centeredMemoryBox = new HBox(memoryDataBox);
        centeredMemoryBox.setAlignment(Pos.CENTER); // Solo horizontalmente
        root.add(centeredMemoryBox, 0, 1); // Agregar debajo de la etiqueta "Memoria Principal"

        // Crear buses (líneas)
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
        root.add(busBox, 0, 2);

        // Crear una fila para los bloques de caché y procesadores, cada uno en una columna
        HBox cpuRow = new HBox(20); // Espacio entre procesadores
        cpuRow.setAlignment(Pos.CENTER); // Solo alineación horizontal
        for (int i = 0; i < 4; i++) {  // Cambiamos a 4 procesadores
            VBox cpuAndCacheBox = new VBox(10); // VBox para alinear verticalmente caché y CPU
            cpuAndCacheBox.setAlignment(Pos.CENTER); // Centrar horizontalmente

            // Crear bloque de caché para el procesador
            VBox cacheBlock = createCacheBlock(i);
            cpuAndCacheBox.getChildren().add(cacheBlock); // Añadir caché arriba del CPU

            // Crear HBox para las líneas entre caché y CPU
            HBox linesBox = new HBox(5); // Espacio entre las líneas
            linesBox.setAlignment(Pos.CENTER); // Centrar horizontalmente

            // Crear línea vertical entre caché y CPU
            Line cacheToCpuLine = new Line(0, 0, 0, 50); // Línea vertical de 50px de altura
            cacheToCpuLine.setStroke(Color.BLACK);
            cacheToCpuLine.setStrokeWidth(5);

            // Crear línea adicional
            Line additionalLine = new Line(0, 0, 0, 50); // Línea adicional
            additionalLine.setStroke(Color.BLACK);
            additionalLine.setStrokeWidth(5);

            // Añadir ambas líneas al HBox
            linesBox.getChildren().addAll(cacheToCpuLine, additionalLine);
            cpuAndCacheBox.getChildren().add(linesBox); // Añadir HBox de líneas al VBox

            // Crear controles de CPU
            VBox cpuControls = createCPUControls(i);
            cpuAndCacheBox.getChildren().add(cpuControls); // Añadir CPU debajo del caché y las líneas

            // Agregar el VBox completo al HBox de la fila
            cpuRow.getChildren().add(cpuAndCacheBox);
        }
        root.add(cpuRow, 0, 3); // Agregar la fila de procesadores en la siguiente fila del layout


        // Configurar la escena y mostrar
        Scene scene = new Scene(root, 670, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("MESI Protocol Simulation");
        primaryStage.show();
    }

    // Método para crear controles de lectura/escritura para cada CPU
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
        Label cacheLabel1 = new Label("Cache Address: C0, Data: 0");
        Label cacheLabel2 = new Label("Cache Address: C1, Data: 0");
        Label cacheLabel3 = new Label("Cache Address: C2, Data: 0");
        Label cacheLabel4 = new Label("Cache Address: C3, Data: 0");

        // Configurar el estilo de las etiquetas de datos de caché
        cacheLabel1.setStyle("-fx-font-size: 12px;");
        cacheLabel2.setStyle("-fx-font-size: 12px;");
        cacheLabel3.setStyle("-fx-font-size: 12px;");
        cacheLabel4.setStyle("-fx-font-size: 12px;");

        // Colocar las etiquetas de datos de caché en un VBox y agregar un borde
        VBox cacheDataBox = new VBox(5, cacheLabel1, cacheLabel2, cacheLabel3, cacheLabel4);
        cacheDataBox.setAlignment(Pos.CENTER);
        cacheDataBox.setMaxWidth(VBox.USE_COMPUTED_SIZE); // Ajustar el ancho al contenido
        cacheDataBox.setStyle(
                "-fx-border-color: black; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 5; " +
                "-fx-padding: 10;" // Espacio entre el borde y las etiquetas internas
        );

        return cacheDataBox;
    }

    // Acción de lectura
    private void onRead(int cpuNumber, int pairNumber) {
        System.out.println("CPU " + cpuNumber + " - Read operation " + pairNumber);
        addressBus.setStroke(Color.GREEN);  // Cambiar color del bus de dirección
        dataBus.setStroke(Color.GREEN);     // Cambiar color del bus de datos
        sharedBus.setStroke(Color.GREEN);   // Cambiar color del bus compartido
    }

    // Acción de escritura
    private void onWrite(int cpuNumber, int pairNumber) {
        System.out.println("CPU " + cpuNumber + " - Write operation " + pairNumber);
        addressBus.setStroke(Color.ORANGE); // Cambiar color del bus de dirección
        dataBus.setStroke(Color.ORANGE);    // Cambiar color del bus de datos
        sharedBus.setStroke(Color.ORANGE);  // Cambiar color del bus compartido
    }

    public static void main(String[] args) {
        launch(args);
    }
}
