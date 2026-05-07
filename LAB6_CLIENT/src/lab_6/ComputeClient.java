package lab_6;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ComputeClient implements Runnable {

    private static final Logger LOG = Logger.getLogger(ComputeClient.class.getName());
    private static final int DEFAULT_WORKER_PORT = 9091;
    private static final int DEFAULT_THREAD_COUNT = 4;

    private final String host;
    private final int port;
    private final int threadCount;
    private volatile boolean running = true;

    public ComputeClient() {
        this("127.0.0.1", DEFAULT_WORKER_PORT, DEFAULT_THREAD_COUNT);
    }

    public ComputeClient(String host, int port, int threadCount) {
        this.host = host;
        this.port = port;
        this.threadCount = Math.max(1, threadCount);
    }

    @Override
    public void run() {
        LOG.info("ComputeClient запущен и готов к приёму задач: " + host + ":" + port);

        while (running && !Thread.currentThread().isInterrupted()) {
            try (Socket socket = new Socket(host, port);
                 ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

                LOG.info("Подключено к серверу. Ожидание задачи...");

                Object obj = ois.readObject();
                if (!(obj instanceof RecIntegral)) {
                    LOG.warning("Получен объект неверного типа");
                    continue;
                }

                RecIntegral task = (RecIntegral) obj;
                LOG.info("Получена задача: " + task);

                double result = computeMultiThreaded(task, threadCount);

                task.setResult(result);
                oos.writeObject(task);
                oos.flush();

                LOG.info(String.format("Результат %.8f отправлен", result));

            } catch (IOException ex) {
                if (running) {
                    LOG.log(Level.WARNING, "Ошибка соединения. Переподключение через 1 сек...", ex);
                    sleep(1000);
                }
            } catch (ClassNotFoundException ex) {
                LOG.log(Level.SEVERE, "Ошибка десериализации задачи", ex);
            }
        }

        LOG.info("ComputeClient остановлен.");
    }

    private double computeMultiThreaded(RecIntegral task, int threadCount) {
        double lower = task.getLowerLimit();
        double upper = task.getUpperLimit();
        double step = task.getStep();
        double interval = (upper - lower) / threadCount;

        IntegralThread[] threads = new IntegralThread[threadCount];
        long startTime = System.nanoTime();

        for (int i = 0; i < threadCount; i++) {
            double from = lower + i * interval;
            double to = (i == threadCount - 1) ? upper : lower + (i + 1) * interval;

            threads[i] = new IntegralThread("Worker-" + (i + 1), from, to, step);
            threads[i].start();
        }

        double total = 0.0;
        for (IntegralThread t : threads) {
            try {
                t.join();
                total += t.getPartialResult();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        long elapsed = System.nanoTime() - startTime;
        System.out.printf("[%s] Вычислено за %.3f мс | Результат: %.8f%n",
                Thread.currentThread().getName(), elapsed / 1_000_000.0, total);

        return total;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void stop() {
        running = false;
    }
}