/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab_4;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class RecIntegral implements Externalizable {
    
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
        if (hasResult) {
            this.result = in.readDouble();
        } else {
            this.result = null;
        }
    }
	
    public double getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(double upperLimit) throws InvalidDataException {
        InvalidDataException.validate("Верхний предел", upperLimit);
        this.upperLimit = upperLimit;
    }

    public double getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(double lowerLimit) throws InvalidDataException {
        InvalidDataException.validate("Нижний предел", lowerLimit);
        this.lowerLimit = lowerLimit;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) throws InvalidDataException {
        InvalidDataException.validate("Шаг", step);
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
