import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

    @Override
    public void start(Stage primaryStage) {
        // Configuración del layout principal
        GridPane root = new GridPane();
        
        // Crear buses (líneas)
        dataBus = new Line(0, 0, 300, 0);  // Línea horizontal para el bus de datos
        dataBus.setStroke(Color.RED);
        dataBus.setStrokeWidth(5);

        addressBus = new Line(0, 0, 500, 0);  // Línea horizontal para el bus de direcciones
        addressBus.setStroke(Color.BLUE);
        addressBus.setStrokeWidth(5);
        
        // Agregar buses al layout
        VBox busBox = new VBox(10, dataBus, addressBus);
        root.add(busBox, 0, 0);

        // Crear botones para cada CPU y cache
        VBox cpuBox = new VBox(10);
        for (int i = 0; i < 3; i++) {
            HBox cpuRow = createCPUControls(i);
            cpuBox.getChildren().add(cpuRow);
        }
        root.add(cpuBox, 0, 1);

        // Configurar la escena y mostrar
        Scene scene = new Scene(root, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("MESI Protocol Simulation");
        primaryStage.show();
    }

    // Método para crear controles de lectura/escritura para cada CPU
    private HBox createCPUControls(int cpuNumber) {
        Button readButton = new Button("Read");
        Button writeButton = new Button("Write");

        // Configurar eventos de botón
        readButton.setOnAction(e -> onRead(cpuNumber));
        writeButton.setOnAction(e -> onWrite(cpuNumber));

        return new HBox(10, readButton, writeButton);
    }

    // Acción de lectura
    private void onRead(int cpuNumber) {
        System.out.println("CPU " + cpuNumber + " is performing a read operation.");
        addressBus.setStroke(Color.GREEN);  // Cambiar color del bus de dirección
        dataBus.setStroke(Color.GREEN);     // Cambiar color del bus de datos
    }

    // Acción de escritura
    private void onWrite(int cpuNumber) {
        System.out.println("CPU " + cpuNumber + " is performing a write operation.");
        addressBus.setStroke(Color.ORANGE); // Cambiar color del bus de dirección
        dataBus.setStroke(Color.ORANGE);    // Cambiar color del bus de datos
    }

    public static void main(String[] args) {
        launch(args);
    }
}
