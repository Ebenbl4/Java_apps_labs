/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package lab_6;

import java.awt.Font;
import java.awt.Color;
import java.awt.Component;
import java.awt.*;
import java.util.LinkedList;
import javax.swing.table.*;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingUtilities;
import java.io.*;
import java.net.Socket;

public class mainForm extends javax.swing.JFrame {

	private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(mainForm.class.getName());
	private LinkedList<RecIntegral> dataList = new LinkedList<>();

	private static final String SERVER_HOST = "127.0.0.1";
	private static final int SERVER_REQUEST_PORT = 9090;
	private static final int WORKER_COUNT = 2;

	public mainForm() {
		initComponents();

		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				LAB_6.stopWorker();
				System.out.println("ComputeClient остановлен при закрытии окна.");
				dispose();
			}
		});

		TableModel model = TableMain.getModel();

		model.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE) {
					int row = e.getFirstRow();
					int column = e.getColumn();

					if (column == 3 || row < 0 || column < 0) {
						return;
					}

					RecIntegral currentRecord = dataList.get(row);
					Object valueObj = model.getValueAt(row, column);

					try {
						double newValue = Double.parseDouble(valueObj.toString().replace(",", "."));
						switch (column) {
							case 0:
								currentRecord.setUpperLimit(newValue);
								break;
							case 1:
								currentRecord.setLowerLimit(newValue);
								break;
							case 2:
								currentRecord.setStep(newValue);
								break;
						}
					} catch (InvalidDataException ex) {
						JOptionPane.showMessageDialog(mainForm.this,
								ex.getMessage(),
								"Некорректные данные", JOptionPane.WARNING_MESSAGE);
						switch (column) {
							case 0:
								model.setValueAt(currentRecord.getUpperLimit(), row, column);
								break;
							case 1:
								model.setValueAt(currentRecord.getLowerLimit(), row, column);
								break;
							case 2:
								model.setValueAt(currentRecord.getStep(), row, column);
								break;
						}
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(mainForm.this,
								"Введите числовое значение",
								"Ошибка", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		Color colorBackground = new Color(34, 35, 36);
		Color colorHeader = new Color(45, 48, 50);
		Color colorSelection = new Color(75, 110, 175);
		Color colorText = new Color(220, 220, 220);
		Color colorGrid = new Color(60, 60, 60);

		jScrollPane1.getViewport().setBackground(colorBackground);
		jScrollPane1.setBorder(BorderFactory.createEmptyBorder());
		jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);

		TableMain.setBackground(colorBackground);
		TableMain.setForeground(colorText);
		TableMain.setSelectionBackground(colorSelection);
		TableMain.setSelectionForeground(Color.WHITE);
		TableMain.setGridColor(colorGrid);
		TableMain.setShowVerticalLines(false);
		TableMain.setRowHeight(30);
		TableMain.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));

		JTableHeader header = TableMain.getTableHeader();
		header.setFont(new Font("JetBrains Mono", Font.BOLD, 14));
		header.setBackground(colorHeader);
		header.setForeground(Color.WHITE);
		header.setOpaque(true);

		header.setDefaultRenderer(new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				l.setBackground(colorHeader);
				l.setForeground(Color.WHITE);
				l.setHorizontalAlignment(SwingConstants.CENTER);
				l.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, colorGrid));
				return l;
			}
		});

		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

		TableMain.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
		TableMain.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
		TableMain.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);
		TableMain.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				setText(value instanceof Number ? String.format("%.6f", ((Number) value).doubleValue()) : "");
				setHorizontalAlignment(SwingConstants.RIGHT);
				if (!isSelected) {
					setBackground(colorBackground);
					setForeground(colorText);
				}
				return this;
			}
		});
	}

	private boolean sendTaskToServer(RecIntegral task) {
    final String SERVER_HOST = "127.0.0.1";
    final int SERVER_REQUEST_PORT = 9090;
    final int WORKER_COUNT = 0;

    try (Socket socket = new Socket(SERVER_HOST, SERVER_REQUEST_PORT);
         ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
         ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

        oos.writeObject("REQUEST");
        oos.writeObject(task);
        oos.writeInt(WORKER_COUNT);
        oos.flush();

        Object response = ois.readObject();
        if (response instanceof RecIntegral) {
            RecIntegral resultTask = (RecIntegral) response;
            if (resultTask.getResult() != null) {
                task.setResult(resultTask.getResult());
                return true;
            }
        }
    } catch (Exception ex) {
        logger.log(java.util.logging.Level.SEVERE, "Ошибка связи с сервером", ex);
    }
    return false;
}

	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new backgroundPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableMain = new javax.swing.JTable();
        TextFieldUpperLimit = new javax.swing.JTextField();
        TextFieldBottomLimit = new javax.swing.JTextField();
        TextFieldStep = new javax.swing.JTextField();
        ButtonAdd = new javax.swing.JButton();
        ButtonCalculate = new javax.swing.JButton();
        ButtonDelete = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        ButtonClear = new javax.swing.JButton();
        ButtonFill = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        ButtonSaveText = new javax.swing.JButton();
        ButtonLoadText = new javax.swing.JButton();
        ButtonSaveBin = new javax.swing.JButton();
        ButtonLoadBin = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Integral Calculator");
        setBackground(new java.awt.Color(50, 3, 9));
        setResizable(false);
        setSize(new java.awt.Dimension(740, 600));

        jPanel1.setBackground(new java.awt.Color(34, 35, 36));
        jPanel1.setName(""); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(730, 630));

        jScrollPane1.setForeground(new java.awt.Color(102, 102, 102));
        jScrollPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jScrollPane1.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N

        TableMain.setFont(new java.awt.Font("JetBrains Mono", 0, 16)); // NOI18N
        TableMain.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Верхний предел", "Нижний предел", "Шаг", "Результат"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TableMain.getTableHeader().setReorderingAllowed(false);
        TableMain.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                TableMainAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        TableMain.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                TableMainPropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(TableMain);

        TextFieldUpperLimit.setBackground(new java.awt.Color(204, 204, 204));
        TextFieldUpperLimit.setFont(new java.awt.Font("JetBrains Mono", 0, 16)); // NOI18N
        TextFieldUpperLimit.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 204, 0)));
        TextFieldUpperLimit.setPreferredSize(new java.awt.Dimension(157, 23));
        TextFieldUpperLimit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextFieldUpperLimitActionPerformed(evt);
            }
        });

        TextFieldBottomLimit.setBackground(new java.awt.Color(204, 204, 204));
        TextFieldBottomLimit.setFont(new java.awt.Font("JetBrains Mono", 0, 16)); // NOI18N
        TextFieldBottomLimit.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 204, 0)));
        TextFieldBottomLimit.setPreferredSize(new java.awt.Dimension(157, 23));
        TextFieldBottomLimit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextFieldBottomLimitActionPerformed(evt);
            }
        });

        TextFieldStep.setBackground(new java.awt.Color(204, 204, 204));
        TextFieldStep.setFont(new java.awt.Font("JetBrains Mono", 0, 16)); // NOI18N
        TextFieldStep.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 204, 0)));
        TextFieldStep.setPreferredSize(new java.awt.Dimension(157, 23));
        TextFieldStep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextFieldStepActionPerformed(evt);
            }
        });

        ButtonAdd.setBackground(new java.awt.Color(55, 57, 58));
        ButtonAdd.setFont(new java.awt.Font("JetBrains Mono", 0, 16)); // NOI18N
        ButtonAdd.setForeground(new java.awt.Color(255, 255, 255));
        ButtonAdd.setText("Добавить");
        ButtonAdd.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 204, 0)));
        ButtonAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonAdd.setMaximumSize(new java.awt.Dimension(62, 23));
        ButtonAdd.setMinimumSize(new java.awt.Dimension(62, 23));
        ButtonAdd.setPreferredSize(new java.awt.Dimension(130, 23));
        ButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonAddActionPerformed(evt);
            }
        });

        ButtonCalculate.setBackground(new java.awt.Color(55, 57, 58));
        ButtonCalculate.setFont(new java.awt.Font("JetBrains Mono", 0, 16)); // NOI18N
        ButtonCalculate.setForeground(new java.awt.Color(255, 255, 255));
        ButtonCalculate.setText("Рассчитать");
        ButtonCalculate.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 0, 204)));
        ButtonCalculate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonCalculate.setPreferredSize(new java.awt.Dimension(130, 23));
        ButtonCalculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonCalculateActionPerformed(evt);
            }
        });

        ButtonDelete.setBackground(new java.awt.Color(55, 57, 58));
        ButtonDelete.setFont(new java.awt.Font("JetBrains Mono", 0, 16)); // NOI18N
        ButtonDelete.setForeground(new java.awt.Color(255, 255, 255));
        ButtonDelete.setText("Удалить");
        ButtonDelete.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 0)));
        ButtonDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonDelete.setPreferredSize(new java.awt.Dimension(130, 23));
        ButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonDeleteActionPerformed(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(34, 35, 36));
        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 0, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Верхний предел");
        jLabel1.setOpaque(true);

        jLabel2.setBackground(new java.awt.Color(34, 35, 36));
        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 0, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Нижний предел");
        jLabel2.setOpaque(true);

        jLabel3.setBackground(new java.awt.Color(34, 35, 36));
        jLabel3.setFont(new java.awt.Font("JetBrains Mono", 0, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Шаг");
        jLabel3.setOpaque(true);

        jLabel4.setBackground(new java.awt.Color(34, 35, 36));
        jLabel4.setFont(new java.awt.Font("JetBrains Mono", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Sin(x)");
        jLabel4.setOpaque(true);

        ButtonClear.setBackground(new java.awt.Color(55, 57, 58));
        ButtonClear.setFont(new java.awt.Font("JetBrains Mono", 0, 16)); // NOI18N
        ButtonClear.setForeground(new java.awt.Color(255, 255, 255));
        ButtonClear.setText("Очистить");
        ButtonClear.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 102, 0)));
        ButtonClear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonClear.setPreferredSize(new java.awt.Dimension(130, 23));
        ButtonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonClearActionPerformed(evt);
            }
        });

        ButtonFill.setBackground(new java.awt.Color(55, 57, 58));
        ButtonFill.setFont(new java.awt.Font("JetBrains Mono", 0, 16)); // NOI18N
        ButtonFill.setForeground(new java.awt.Color(255, 255, 255));
        ButtonFill.setText("Заполнить");
        ButtonFill.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 204, 0)));
        ButtonFill.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonFill.setPreferredSize(new java.awt.Dimension(130, 23));
        ButtonFill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonFillActionPerformed(evt);
            }
        });

        jLabel6.setBackground(new java.awt.Color(34, 35, 36));
        jLabel6.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Таблица");
        jLabel6.setAlignmentX(0.5F);
        jLabel6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel6.setOpaque(true);

        jLabel7.setBackground(new java.awt.Color(34, 35, 36));
        jLabel7.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Коллекция");
        jLabel7.setOpaque(true);

        jLabel8.setBackground(new java.awt.Color(34, 35, 36));
        jLabel8.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Файлы");
        jLabel8.setOpaque(true);

        ButtonSaveText.setBackground(new java.awt.Color(55, 57, 58));
        ButtonSaveText.setFont(new java.awt.Font("JetBrains Mono", 0, 16)); // NOI18N
        ButtonSaveText.setForeground(new java.awt.Color(255, 255, 255));
        ButtonSaveText.setText("Сохранить (Т)");
        ButtonSaveText.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 204, 0)));
        ButtonSaveText.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonSaveText.setPreferredSize(new java.awt.Dimension(130, 23));
        ButtonSaveText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSaveTextActionPerformed(evt);
            }
        });

        ButtonLoadText.setBackground(new java.awt.Color(55, 57, 58));
        ButtonLoadText.setFont(new java.awt.Font("JetBrains Mono", 0, 16)); // NOI18N
        ButtonLoadText.setForeground(new java.awt.Color(255, 255, 255));
        ButtonLoadText.setText("Загрузить (Т)");
        ButtonLoadText.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 102, 0)));
        ButtonLoadText.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonLoadText.setPreferredSize(new java.awt.Dimension(130, 23));
        ButtonLoadText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonLoadTextActionPerformed(evt);
            }
        });

        ButtonSaveBin.setBackground(new java.awt.Color(55, 57, 58));
        ButtonSaveBin.setFont(new java.awt.Font("JetBrains Mono", 0, 16)); // NOI18N
        ButtonSaveBin.setForeground(new java.awt.Color(255, 255, 255));
        ButtonSaveBin.setText("Сохранить (Bin)");
        ButtonSaveBin.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 0, 204)));
        ButtonSaveBin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonSaveBin.setPreferredSize(new java.awt.Dimension(130, 23));
        ButtonSaveBin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSaveBinActionPerformed(evt);
            }
        });

        ButtonLoadBin.setBackground(new java.awt.Color(55, 57, 58));
        ButtonLoadBin.setFont(new java.awt.Font("JetBrains Mono", 0, 16)); // NOI18N
        ButtonLoadBin.setForeground(new java.awt.Color(255, 255, 255));
        ButtonLoadBin.setText("Загрузить (Bin)");
        ButtonLoadBin.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 0, 204)));
        ButtonLoadBin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonLoadBin.setPreferredSize(new java.awt.Dimension(130, 23));
        ButtonLoadBin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonLoadBinActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(TextFieldBottomLimit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .addComponent(TextFieldUpperLimit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                    .addComponent(TextFieldStep, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ButtonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                    .addComponent(ButtonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                    .addComponent(ButtonCalculate, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ButtonClear, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(jLabel4)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ButtonLoadBin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(ButtonLoadText, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                                .addGap(20, 20, 20))
                            .addComponent(ButtonSaveBin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ButtonFill, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ButtonSaveText, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(0, 11, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TextFieldUpperLimit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(ButtonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ButtonFill, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ButtonSaveText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ButtonClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(ButtonLoadText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(TextFieldBottomLimit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(ButtonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(ButtonSaveBin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextFieldStep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(ButtonCalculate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(ButtonLoadBin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 660, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TableMainAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_TableMainAncestorAdded
		// TODO add your handling code here:
    }//GEN-LAST:event_TableMainAncestorAdded

    private void TextFieldUpperLimitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextFieldUpperLimitActionPerformed
		// TODO add your handling code here:
    }//GEN-LAST:event_TextFieldUpperLimitActionPerformed

    private void TextFieldStepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextFieldStepActionPerformed
		// TODO add your handling code here:
    }//GEN-LAST:event_TextFieldStepActionPerformed

    private void ButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonAddActionPerformed
		try {
			double upperLimit = Double.parseDouble(TextFieldUpperLimit.getText().trim());
			double bottomLimit = Double.parseDouble(TextFieldBottomLimit.getText().trim());
			double step = Double.parseDouble(TextFieldStep.getText().trim());

			if (upperLimit <= bottomLimit) {
				JOptionPane.showMessageDialog(this, "Верхний предел не может быть меньше нижнего", "Ошибка", JOptionPane.WARNING_MESSAGE);
				return;
			}

			if (step <= 0) {
				JOptionPane.showMessageDialog(this, "Шаг должен быть положительным", "Ошибка", JOptionPane.WARNING_MESSAGE);
				return;
			}

			if (upperLimit - bottomLimit <= step) {
				JOptionPane.showMessageDialog(this, "Шаг должен находиться в пределах интегрирования", "Ошибка", JOptionPane.WARNING_MESSAGE);
				return;
			}

			RecIntegral record = new RecIntegral(upperLimit, bottomLimit, step);
			dataList.addFirst(record);
			DefaultTableModel model = (DefaultTableModel) TableMain.getModel();
			Object[] newRow = {upperLimit, bottomLimit, step, null};
			int maxRows = 31;
			if (model.getRowCount() >= maxRows) {
				model.removeRow(model.getRowCount() - 1);
				dataList.removeLast();
			}
			model.insertRow(0, newRow);

		} catch (InvalidDataException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Некорректные данные", JOptionPane.WARNING_MESSAGE);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Некорректные данные: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
		}
    }//GEN-LAST:event_ButtonAddActionPerformed

    private void ButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonDeleteActionPerformed
		DefaultTableModel model = (DefaultTableModel) TableMain.getModel();
		int[] selectedRows = TableMain.getSelectedRows();

		if (selectedRows.length != 0) {
			for (int i = selectedRows.length - 1; i >= 0; i--) {
				int modelIndex = TableMain.convertRowIndexToModel(selectedRows[i]);
				model.removeRow(modelIndex);
				if (modelIndex >= 0 && modelIndex < dataList.size()) {
					dataList.remove(modelIndex);
				}
			}
		} else {
			JOptionPane.showMessageDialog(this, "Выберите строки для удаления", "Ошибка", JOptionPane.WARNING_MESSAGE);
		}
    }//GEN-LAST:event_ButtonDeleteActionPerformed

    private void ButtonCalculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonCalculateActionPerformed
		DefaultTableModel model = (DefaultTableModel) TableMain.getModel();
		int[] selectedRows = TableMain.getSelectedRows();

		if (selectedRows.length == 0) {
			JOptionPane.showMessageDialog(this,
					"Выберите строки для вычисления",
					"Ошибка", JOptionPane.WARNING_MESSAGE);
			return;
		}

		ButtonCalculate.setEnabled(false);
		ButtonCalculate.setText("Вычисление...");

		new Thread(() -> {
			for (int viewIndex : selectedRows) {
				final int modelIndex = TableMain.convertRowIndexToModel(viewIndex);
				final RecIntegral originalRec = dataList.get(modelIndex);

				RecIntegral task;
				try {
					task = new RecIntegral(
							originalRec.getUpperLimit(),
							originalRec.getLowerLimit(),
							originalRec.getStep()
					);
				} catch (InvalidDataException e) {
					SwingUtilities.invokeLater(()
							-> JOptionPane.showMessageDialog(this,
									"Некорректные данные в строке " + (modelIndex + 1) + ": " + e.getMessage(),
									"Ошибка", JOptionPane.ERROR_MESSAGE));
					continue;
				}

				boolean success = sendTaskToServer(task);

				if (success && task.getResult() != null) {
					final double result = task.getResult();
					SwingUtilities.invokeLater(() -> {
						originalRec.setResult(result);
						model.setValueAt(result, modelIndex, 3);
					});
				} else {
					SwingUtilities.invokeLater(()
							-> JOptionPane.showMessageDialog(this,
									"Не удалось получить результат для строки " + (modelIndex + 1),
									"Ошибка", JOptionPane.WARNING_MESSAGE));
				}
			}

			SwingUtilities.invokeLater(() -> {
				ButtonCalculate.setEnabled(true);
				ButtonCalculate.setText("Рассчитать");
			});
		}, "Calculation-Thread").start();
    }//GEN-LAST:event_ButtonCalculateActionPerformed

    private void ButtonClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonClearActionPerformed
		DefaultTableModel model = (DefaultTableModel) TableMain.getModel();
		model.setRowCount(0);
    }//GEN-LAST:event_ButtonClearActionPerformed

    private void ButtonFillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonFillActionPerformed
		DefaultTableModel model = (DefaultTableModel) TableMain.getModel();
		model.setRowCount(0);

		for (RecIntegral rec : dataList) {
			Object[] row = {
				rec.getUpperLimit(),
				rec.getLowerLimit(),
				rec.getStep(),
				rec.getResult()
			};
			model.addRow(row);
		}
    }//GEN-LAST:event_ButtonFillActionPerformed

    private void TextFieldBottomLimitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextFieldBottomLimitActionPerformed
		// TODO add your handling code here:
    }//GEN-LAST:event_TextFieldBottomLimitActionPerformed

    private void TableMainPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_TableMainPropertyChange

    }//GEN-LAST:event_TableMainPropertyChange

    private void ButtonSaveTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonSaveTextActionPerformed
		javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
		fileChooser.setDialogTitle("Сохранить в текстовый файл");
		fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
				"Текстовые файлы (*.txt)", "txt"));

		int userSelection = fileChooser.showSaveDialog(this);
		if (userSelection == javax.swing.JFileChooser.APPROVE_OPTION) {
			java.io.File file = fileChooser.getSelectedFile();
			if (!file.getName().toLowerCase().endsWith(".txt")) {
				file = new java.io.File(file.getAbsolutePath() + ".txt");
			}
			try {
				FileWriter.saveText(file, dataList);
				JOptionPane.showMessageDialog(this,
						"Данные успешно сохранены в текстовый файл",
						"Сохранение", JOptionPane.INFORMATION_MESSAGE);
			} catch (java.io.IOException ex) {
				JOptionPane.showMessageDialog(this,
						"Ошибка сохранения файла: " + ex.getMessage(),
						"Ошибка", JOptionPane.ERROR_MESSAGE);
			}
		}
    }//GEN-LAST:event_ButtonSaveTextActionPerformed

    private void ButtonLoadTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonLoadTextActionPerformed
		javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
		fileChooser.setDialogTitle("Загрузить из текстового файла");
		fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
				"Текстовые файлы (*.txt)", "txt"));

		int userSelection = fileChooser.showOpenDialog(this);
		if (userSelection == javax.swing.JFileChooser.APPROVE_OPTION) {
			java.io.File file = fileChooser.getSelectedFile();
			if (!file.getName().toLowerCase().endsWith(".txt")) {
				file = new java.io.File(file.getAbsolutePath() + ".txt");
			}
			try {
				LinkedList<RecIntegral> loadedData = FileReader.loadText(file);
				dataList.clear();
				dataList.addAll(loadedData);
				DefaultTableModel model = (DefaultTableModel) TableMain.getModel();
				model.setRowCount(0);
				for (RecIntegral rec : dataList) {
					Object[] row = {
						rec.getUpperLimit(),
						rec.getLowerLimit(),
						rec.getStep(),
						rec.getResult()
					};
					model.addRow(row);
				}

				JOptionPane.showMessageDialog(this,
						"Данные успешно загружены из текстового файла",
						"Загрузка", JOptionPane.INFORMATION_MESSAGE);
			} catch (java.io.IOException | InvalidDataException ex) {
				JOptionPane.showMessageDialog(this,
						"Ошибка загрузки файла: " + ex.getMessage(),
						"Ошибка", JOptionPane.ERROR_MESSAGE);
			}
		}
    }//GEN-LAST:event_ButtonLoadTextActionPerformed

    private void ButtonSaveBinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonSaveBinActionPerformed
		javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
		fileChooser.setDialogTitle("Сохранить в двоичный файл");
		fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
				"Двоичные файлы (*.dat)", "dat"));

		int userSelection = fileChooser.showSaveDialog(this);
		if (userSelection == javax.swing.JFileChooser.APPROVE_OPTION) {
			java.io.File file = fileChooser.getSelectedFile();
			if (!file.getName().toLowerCase().endsWith(".dat")) {
				file = new java.io.File(file.getAbsolutePath() + ".dat");
			}
			try {
				FileWriter.saveBinary(file, dataList);
				JOptionPane.showMessageDialog(this,
						"Данные успешно сохранены в двоичный файл",
						"Сохранение", JOptionPane.INFORMATION_MESSAGE);
			} catch (java.io.IOException ex) {
				JOptionPane.showMessageDialog(this,
						"Ошибка сохранения файла: " + ex.getMessage(),
						"Ошибка", JOptionPane.ERROR_MESSAGE);
			}
		}
    }//GEN-LAST:event_ButtonSaveBinActionPerformed

    private void ButtonLoadBinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonLoadBinActionPerformed
		javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
		fileChooser.setDialogTitle("Загрузить из двоичного файла");
		fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
				"Двоичные файлы (*.dat)", "dat"));

		int userSelection = fileChooser.showOpenDialog(this);
		if (userSelection == javax.swing.JFileChooser.APPROVE_OPTION) {
			java.io.File file = fileChooser.getSelectedFile();
			if (!file.getName().toLowerCase().endsWith(".dat")) {
				file = new java.io.File(file.getAbsolutePath() + ".dat");
			}
			try {
				LinkedList<RecIntegral> loadedData = FileReader.loadBinary(file);
				dataList.clear();
				dataList.addAll(loadedData);
				DefaultTableModel model = (DefaultTableModel) TableMain.getModel();
				model.setRowCount(0);
				for (RecIntegral rec : dataList) {
					Object[] row = {
						rec.getUpperLimit(),
						rec.getLowerLimit(),
						rec.getStep(),
						rec.getResult()
					};
					model.addRow(row);
				}

				JOptionPane.showMessageDialog(this,
						"Данные успешно загружены из двоичного файла",
						"Загрузка", JOptionPane.INFORMATION_MESSAGE);
			} catch (java.io.IOException | ClassNotFoundException ex) {
				JOptionPane.showMessageDialog(this,
						"Ошибка загрузки файла: " + ex.getMessage(),
						"Ошибка", JOptionPane.ERROR_MESSAGE);
			}
		}
    }//GEN-LAST:event_ButtonLoadBinActionPerformed

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
		/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
			logger.log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(() -> new mainForm().setVisible(true));
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonAdd;
    private javax.swing.JButton ButtonCalculate;
    private javax.swing.JButton ButtonClear;
    private javax.swing.JButton ButtonDelete;
    private javax.swing.JButton ButtonFill;
    private javax.swing.JButton ButtonLoadBin;
    private javax.swing.JButton ButtonLoadText;
    private javax.swing.JButton ButtonSaveBin;
    private javax.swing.JButton ButtonSaveText;
    private javax.swing.JTable TableMain;
    private javax.swing.JTextField TextFieldBottomLimit;
    private javax.swing.JTextField TextFieldStep;
    private javax.swing.JTextField TextFieldUpperLimit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
