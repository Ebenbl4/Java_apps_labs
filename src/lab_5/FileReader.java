/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab_5;

import java.io.*;
import java.util.LinkedList;

public class FileReader {

	public static LinkedList<RecIntegral> loadText(File file)
			throws IOException, InvalidDataException {
		LinkedList<RecIntegral> dataList = new LinkedList<>();

		try (BufferedReader reader = new BufferedReader(new java.io.FileReader(file))) {
			String line;
			int lineNumber = 0;

			while ((line = reader.readLine()) != null) {
				lineNumber++;
				line = line.trim();
				if (line.isEmpty()) {
					continue;
				}

				String[] parts = line.split(";");
				if (parts.length != 4) {
					throw new IOException(
							"Ошибка формата в строке " + lineNumber
							+ ": ожидается 4 значения, получено " + parts.length);
				}

				try {
					double upper = Double.parseDouble(parts[0].trim().replace(",", "."));
					double lower = Double.parseDouble(parts[1].trim().replace(",", "."));
					double step = Double.parseDouble(parts[2].trim().replace(",", "."));
					RecIntegral rec;
					if ("null".equals(parts[3].trim().replace(",", "."))) {
						rec = new RecIntegral(upper, lower, step);
					} else {
						double rez = Double.parseDouble(parts[3].trim().replace(",", "."));
						rec = new RecIntegral(upper, lower, step, rez);
					}
					dataList.add(rec);
				} catch (NumberFormatException ex) {
					throw new IOException(
							"Ошибка парсинга числа в строке " + lineNumber + ": " + ex.getMessage());
				}
			}
		}
		return dataList;
	}

	@SuppressWarnings("unchecked")
	public static LinkedList<RecIntegral> loadBinary(File file)
			throws IOException, ClassNotFoundException {

		LinkedList<RecIntegral> list = new LinkedList<>();

		try (ObjectInputStream ois = new ObjectInputStream(
				new BufferedInputStream(new FileInputStream(file)))) {

			int count = ois.readInt();

			for (int i = 0; i < count; i++) {
				Object obj = ois.readObject();
				if (obj instanceof RecIntegral) {
					list.add((RecIntegral) obj);
				} else {
					throw new IOException("В файле обнаружен некорректный тип объекта");
				}
			}
		}

		return list;
	}
}
