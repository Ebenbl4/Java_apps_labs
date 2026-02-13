/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab_1;

/**
 *
 * @author tolyan
 */
public class result {
    public static double calculateResult(double upperLimit, double bottomLimit, double step) {
        double result = 0;
        double x = bottomLimit;
        double stepTemp = step;
        
        while(x < upperLimit) {
            stepTemp = (x + stepTemp > upperLimit) ? (upperLimit - x) : step;
            double y1 = Math.sin(x);
            double y2 = Math.sin(x + step);
            result += (y1 + y2) / 2.0 * step;
            x += stepTemp;
        }
        
       //while (x + step < upperLimit) {
       // double y1 = Math.sin(x);
       //double y2 = Math.sin(x + step);
       // result += (y1 + y2) / 2.0 * step;
        //x += step;
   // }
       // if (x < upperLimit) {
       // double y1 = Math.sin(x);
       // double y2 = Math.sin(upperLimit);
       // double last_step = upperLimit - x;
       // result += (y1 + y2) / 2.0 * last_step;
   // }
        return result;
    }
}
