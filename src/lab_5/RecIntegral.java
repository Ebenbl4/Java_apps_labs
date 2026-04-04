/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab_5;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class RecIntegral implements Externalizable {

    private static final int THREAD_COUNT = 4;

    private double upperLimit;
    private double lowerLimit;
    private double step;
    private Double result;

    public RecIntegral() {
    }

    public RecIntegral(double upperLimit, double lowerLimit, double step)
            throws InvalidDataException {
        InvalidDataException.validate("Верхний предел", upperLimit);
        InvalidDataException.validate("Нижний предел", lowerLimit);
        InvalidDataException.validate("Шаг", step);
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
        this.step = step;
        this.result = null;
    }

    public RecIntegral(double upperLimit, double lowerLimit, double step, double rez)
            throws InvalidDataException {
        InvalidDataException.validate("Верхний предел", upperLimit);
        InvalidDataException.validate("Нижний предел", lowerLimit);
        InvalidDataException.validate("Шаг", step);
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
        this.step = step;
        this.result = rez;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeDouble(upperLimit);
        out.writeDouble(lowerLimit);
        out.writeDouble(step);
        if (result == null) {
            out.writeBoolean(false);
        } else {
            out.writeBoolean(true);
            out.writeDouble(result);
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.upperLimit = in.readDouble();
        this.lowerLimit = in.readDouble();
        this.step = in.readDouble();
        boolean hasResult = in.readBoolean();
        this.result = hasResult ? in.readDouble() : null;
    }

    public double getUpperLimit() { return upperLimit; }
    public void   setUpperLimit(double v) throws InvalidDataException {
        InvalidDataException.validate("Верхний предел", v); upperLimit = v;
    }

    public double getLowerLimit() { return lowerLimit; }
    public void   setLowerLimit(double v) throws InvalidDataException {
        InvalidDataException.validate("Нижний предел", v); lowerLimit = v;
    }

    public double getStep() { return step; }
    public void   setStep(double v) throws InvalidDataException {
        InvalidDataException.validate("Шаг", v); step = v;
    }

    public Double getResult() { return result; }
    
   public void calculateResult() {
	    
        double intervalLen = (upperLimit - lowerLimit) / THREAD_COUNT;

        IntegralThread[] threads = new IntegralThread[THREAD_COUNT];
		long startTime = System.nanoTime();
        for (int i = 0; i < THREAD_COUNT; i++) {
            double from = lowerLimit  + i * intervalLen;
            double to = (i == THREAD_COUNT - 1) ? upperLimit : lowerLimit + (i + 1) * intervalLen;

            threads[i] = new IntegralThread("IntegralThread-" + (i + 1), from, to, step);
            threads[i].start();
        }

        result = 0.0;
        for (IntegralThread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Поток прерван во время вычисления", e);
            }
            result += t.getPartialResult();
        }
		long endTime = System.nanoTime();
        long durationNano = endTime - startTime;
        double durationMs = durationNano / 1_000_000.0;

        System.out.printf("Время работы: %.3f мс%n", durationMs);

        System.out.println("Итоговый результат = " + result);
    }
}