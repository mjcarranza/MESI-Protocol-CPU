public class SimulationHandler {

    public static void simulateBehavior(String message, int miliseconds) {
        if (message != null) {
            System.out.println(message);
            try {
                SocketHandler.sendMessage(message);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        try {
            Thread.sleep(miliseconds);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
