/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab_6;

import java.io.*;
import java.util.LinkedList;

public class FileWriter {

    public static void saveText(File file, LinkedList<RecIntegral> dataList) 
            throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(file))) {
            for (RecIntegral rec : dataList) {
                StringBuilder sb = new StringBuilder();
                sb.append(rec.getUpperLimit()).append(";");
                sb.append(rec.getLowerLimit()).append(";");
                sb.append(rec.getStep()).append(";");
                sb.append(rec.getResult() == null ? "null" : rec.getResult());
                writer.write(sb.toString());
                writer.newLine();
            }
        }
    }

    public static void saveBinary(File file, LinkedList<RecIntegral> dataList)
        throws IOException {
    try (ObjectOutputStream oos = new ObjectOutputStream(
            new BufferedOutputStream(new FileOutputStream(file)))) {

        oos.writeInt(dataList.size());

        for (RecIntegral rec : dataList) {
            oos.writeObject(rec);
        }

        oos.flush();
    }
}
}
