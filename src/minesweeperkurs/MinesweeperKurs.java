/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package minesweeperkurs;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.*;
/**
 *
 * @author tolyan
 */
public class MinesweeperKurs {
    
    /**
     * @param args the command line arguments
     */
        
    public static void main(String[] args) {
        FlatLightLaf.setup();
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Сапёр");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            // Твой класс с логикой
            GameLogic logic = new GameLogic(); // строки, столбцы, мины
            
            GameField grid = new GameField(logic);
            grid.updateAllCellsRender();
            
            frame.add(grid, BorderLayout.CENTER);
            
            // Можно добавить сверху панель с таймером, счётчиком мин и т.д.
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);
        });
    }
}
    

    
    

