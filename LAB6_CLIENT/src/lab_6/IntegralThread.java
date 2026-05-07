package lab_6;

public class IntegralThread extends Thread {

    private final double from;
    private final double to;
    private final double step;
    private double partialResult;

    public IntegralThread(String name, double from, double to, double step) {
        super(name);
        this.from = from;
        this.to = to;
        this.step = step;
        this.partialResult = 0.0;
    }

    @Override
    public void run() {
        System.out.println(getName() + " начал вычисление на [" + from + "; " + to + "]");

        double x = from;
        double y1 = Math.sin(x);

        while (x + step < to) {
            double nextX = x + step;
            double y2 = Math.sin(nextX);
            partialResult += (y1 + y2) / 2.0 * step;
            x = nextX;
            y1 = y2;
        }

        if (x < to) {
            partialResult += (y1 + Math.sin(to)) / 2.0 * (to - x);
        }

        System.out.println(getName() + " завершил. Частичный результат = " + partialResult);
    }

    public double getPartialResult() {
        return partialResult;
    }
}