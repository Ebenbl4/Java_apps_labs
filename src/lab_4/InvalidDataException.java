/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab_4;

/**
 *
 * @author student
 */
public class InvalidDataException extends Exception {

    private static final double MIN_VALUE = 0.000001;
    private static final double MAX_VALUE = 1000000;
	
    public InvalidDataException(String paramName, double value) {
        super("Некорректное значение параметра \"" + paramName + "\": " + value
                + ". Допустимый диапазон: [" + MIN_VALUE + "; " + MAX_VALUE + "]");
    }

    public static void validate(String paramName, double value) throws InvalidDataException {
        if (Double.isNaN(value) || Double.isInfinite(value)
                || value < MIN_VALUE || value > MAX_VALUE) {
            throw new InvalidDataException(paramName, value);
        }
    }
}
