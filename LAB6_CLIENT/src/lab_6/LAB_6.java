package lab_6;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class LAB_6 {
    private static ComputeClient computeClient;

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));

        computeClient = new ComputeClient();
        Thread workerThread = new Thread(computeClient, "ComputeClient-Worker");
        workerThread.setDaemon(true);
        workerThread.start();

        System.out.println("ComputeClient запущен в фоновом режиме.");

        javax.swing.SwingUtilities.invokeLater(() -> {
            new mainForm().setVisible(true);
        });
    }

    public static void stopWorker() {
        if (computeClient != null) {
            computeClient.stop();
        }
    }
}
