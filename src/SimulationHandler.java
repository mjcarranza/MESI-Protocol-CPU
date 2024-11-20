public class SimulationHandler {

    // private static SocketHandler socketHandler = new SocketHandler();

    public SimulationHandler() {
        // socketHandler.conectar("localhost", 8888);
    }

    public static void simulateBehavior(String message, int miliseconds) {
        if (message != null) {
            System.out.println(message);
            // socketHandler.enviarMensaje(message);
        }
        try {
            Thread.sleep(miliseconds);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
