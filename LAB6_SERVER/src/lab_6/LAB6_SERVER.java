package lab_6;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class LAB6_SERVER {

    private static final int REQUEST_PORT = 9090;
    private static final int WORKER_PORT = 9091;
    private static final int CORE_POOL_SIZE = 10;
    public static final LinkedBlockingQueue<Socket> workerQueue = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));
        System.out.println("Запуск сервера LAB6_SERVER...");
        System.out.println("Ожидание запросов на порту " + REQUEST_PORT);
        System.out.println("Ожидание рабочих узлов на порту " + WORKER_PORT);

        ThreadPoolExecutor requestExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE, CORE_POOL_SIZE, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

        new Thread(() -> {
            try (ServerSocket requestSocket = new ServerSocket(REQUEST_PORT)) {
                while (!Thread.currentThread().isInterrupted()) {
                    Socket socket = requestSocket.accept();
                    requestExecutor.execute(new RequestHandler(socket));
                }
            } catch (IOException ex) {
                System.err.println("Ошибка RequestSocket: " + ex.getMessage());
            }
        }).start();

        new Thread(() -> {
            try (ServerSocket workerSocket = new ServerSocket(WORKER_PORT)) {
                while (!Thread.currentThread().isInterrupted()) {
                    Socket socket = workerSocket.accept();
                    workerQueue.add(socket);
                    System.out.println("Добавлен рабочий узел в очередь. Всего: " + workerQueue.size());
                }
            } catch (IOException ex) {
                System.err.println("Ошибка WorkerSocket: " + ex.getMessage());
            }
        }).start();
    }
}
