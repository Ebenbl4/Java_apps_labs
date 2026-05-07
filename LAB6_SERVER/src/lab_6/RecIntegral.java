package lab_6;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class RecIntegral implements Externalizable {
    private double upperLimit;
    private double lowerLimit;
    private double step;
    private Double result;

    public RecIntegral() {}

    public RecIntegral(double upperLimit, double lowerLimit, double step) throws InvalidDataException {
        InvalidDataException.validate("Верхний предел", upperLimit);
        InvalidDataException.validate("Нижний предел", lowerLimit);
        InvalidDataException.validate("Шаг", step);
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
        this.step = step;
        this.result = null;
    }

    public RecIntegral(double upperLimit, double lowerLimit, double step, double result) throws InvalidDataException {
        this(upperLimit, lowerLimit, step);
        this.result = result;
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
        upperLimit   = in.readDouble();
        lowerLimit   = in.readDouble();
        step         = in.readDouble();
        boolean has  = in.readBoolean();
        result       = has ? in.readDouble() : null;
    }

    public double getUpperLimit() { return upperLimit; }
    public void setUpperLimit(double v) throws InvalidDataException {
        InvalidDataException.validate("Верхний предел", v); upperLimit = v;
    }

    public double getLowerLimit() { return lowerLimit; }
    public void setLowerLimit(double v) throws InvalidDataException {
        InvalidDataException.validate("Нижний предел", v); lowerLimit = v;
    }

    public double getStep() { return step; }
    public void setStep(double v) throws InvalidDataException {
        InvalidDataException.validate("Шаг", v); step = v;
    }

    public Double getResult() { return result; }
    public void setResult(double result) { this.result = result; }

    @Override
    public String toString() {
        return String.format("RecIntegral[%.4f, %.4f, step=%.6f, result=%s]",
                lowerLimit, upperLimit, step,
                result == null ? "null" : String.format("%.8f", result));
    }
}
