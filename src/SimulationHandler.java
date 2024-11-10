public class SimulationHandler {

    private static SocketHandler socketHandler = new SocketHandler();

    public SimulationHandler() {
        socketHandler.conectar("localhost", 8888);
    }

    public static void simulateBehavior(String message, int miliseconds) {
        System.out.println(message);
        socketHandler.enviarMensaje(message);
        Statistics.addTimeSearching(miliseconds);
        try {
            Thread.sleep(miliseconds);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
