package lab_6;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class RequestHandler implements Runnable {

    private final Socket requestSocket;

    public RequestHandler(Socket socket) {
        this.requestSocket = socket;
    }

    @Override
    public void run() {
        try (
            ObjectInputStream ois = new ObjectInputStream(requestSocket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(requestSocket.getOutputStream())
        ) {
            String type = (String) ois.readObject();
            if (!"REQUEST".equals(type)) {
                return;
            }

            RecIntegral task = (RecIntegral) ois.readObject();
            int requestedWorkers = ois.readInt();

            int availableWorkers = LAB6_SERVER.workerQueue.size();
            if (availableWorkers == 0) {
                System.err.println("Нет доступных рабочих узлов!");
                sendError(oos);
                return;
            }

            int workerCount = availableWorkers;

            System.out.println("Запрос на вычисление. Доступно рабочих узлов: " + availableWorkers 
                    + " → будет использовано: " + workerCount);

            List<Socket> workerSockets = new ArrayList<>();
            for (int i = 0; i < workerCount; i++) {
                Socket ws = LAB6_SERVER.workerQueue.poll();
                if (ws != null && !ws.isClosed()) {
                    workerSockets.add(ws);
                }
            }

            int actualWorkerCount = workerSockets.size();
            System.out.println("Реально используется рабочих узлов: " + actualWorkerCount);

            if (actualWorkerCount == 0) {
                sendError(oos);
                return;
            }

            double lower = task.getLowerLimit();
            double upper = task.getUpperLimit();
            double step = task.getStep();
            double interval = (upper - lower) / actualWorkerCount;

            RecIntegral[] subTasks = new RecIntegral[actualWorkerCount];
            for (int i = 0; i < actualWorkerCount; i++) {
                double subLower = lower + i * interval;
                double subUpper = (i == actualWorkerCount - 1) ? upper : lower + (i + 1) * interval;

                if (subUpper < subLower) {
                    double temp = subLower;
                    subLower = subUpper;
                    subUpper = temp;
                }

                subTasks[i] = new RecIntegral(subUpper, subLower, step);
                System.out.println("Подзадача " + (i+1) + ": [" + subLower + "; " + subUpper + "]");
            }

            List<WorkerHandler> handlers = new ArrayList<>();
            for (int i = 0; i < actualWorkerCount; i++) {
                WorkerHandler handler = new WorkerHandler(workerSockets.get(i), subTasks[i]);
                handler.start();
                handlers.add(handler);
            }

            double totalResult = 0.0;
            for (WorkerHandler handler : handlers) {
                try {
                    handler.join();
                    totalResult += handler.getPartialResult();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            for (Socket ws : workerSockets) {
                if (ws != null && !ws.isClosed()) {
                    LAB6_SERVER.workerQueue.add(ws);
                }
            }
            System.out.println("Рабочие узлы возвращены в очередь. Всего: " + LAB6_SERVER.workerQueue.size());

            RecIntegral resultTask = new RecIntegral(2, 1, 1);
            resultTask.setResult(totalResult);
            oos.writeObject(resultTask);
            oos.flush();

            System.out.println("Задача выполнена. Результат: " + totalResult);

        } catch (Exception ex) {
            System.err.println("Ошибка в RequestHandler: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (requestSocket != null && !requestSocket.isClosed()) {
                    requestSocket.close();
                }
            } catch (IOException ignored) {}
        }
    }

    private void sendError(ObjectOutputStream oos) {
        try {
            RecIntegral errorRes = new RecIntegral(2, 1, 1);
            errorRes.setResult(Double.NaN);
            oos.writeObject(errorRes);
            oos.flush();
        } catch (Exception ignored) {}
    }
} 