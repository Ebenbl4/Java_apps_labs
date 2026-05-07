package lab_6;

import java.io.*;
import java.net.Socket;

public class WorkerHandler extends Thread {
    private final Socket workerSocket;
    private final RecIntegral subTask;
    private double partialResult;
    private boolean success = false;

    public WorkerHandler(Socket socket, RecIntegral task) {
        this.workerSocket = socket;
        this.subTask = task;
    }

    @Override
    public void run() {
        try (
                ObjectOutputStream oos = new ObjectOutputStream(workerSocket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(workerSocket.getInputStream())) {

            oos.writeObject(subTask);
            oos.flush();

            Object response = ois.readObject();
            if (response instanceof RecIntegral) {
                RecIntegral answer = (RecIntegral) response;
                if (answer.getResult() != null) {
                    partialResult = answer.getResult();
                    success = true;
                    System.out.println("WorkerHandler получил результат: " + partialResult);
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Ошибка в WorkerHandler: " + ex.getMessage());
        } finally {
            try { workerSocket.close(); } catch (IOException e) {}
        }
    }

    public double getPartialResult() { return partialResult; }
    public boolean isSuccess() { return success; }
}