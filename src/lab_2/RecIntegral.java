/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab_2;

public class RecIntegral {
    private double upperLimit;
    private double lowerLimit;
    private double step;
    private Double result; 

    public RecIntegral(double upperLimit, double lowerLimit, double step) {
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
        this.step = step;
        this.result = null;
    }

    public double getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(double upperLimit) {
        this.upperLimit = upperLimit;
    }

    public double getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(double lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
    }

    public Double getResult() {
        return result;
    }
    
    public void calculateResult() {
        result = 0.0;
        double x = lowerLimit;
        double y1 = Math.sin(x);

        while (x + step < upperLimit) {
            double nextX = x + step;
            double y2 = Math.sin(nextX);
            this.result += (y1 + y2) / 2.0 * step;
            x = nextX;
            y1 = y2;
        }
        this.result += (y1 + Math.sin(upperLimit)) / 2.0 * (upperLimit - x);
    }
}
