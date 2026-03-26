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
            new MinesweeperWindow().setVisible(true);
        });
    }
}
    

    
    

