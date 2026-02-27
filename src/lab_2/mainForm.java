/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package lab_2;

import java.awt.Font;
import java.awt.Color;
import java.awt.Component;
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


public class mainForm extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(mainForm.class.getName());
    private LinkedList<RecIntegral> dataList = new LinkedList<>();

    public mainForm() {
        initComponents();
        
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
                l.setHorizontalAlignment(SwingConstants.CENTER); // По центру
                l.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, colorGrid)); // Линия снизу
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

                setText(value instanceof Number ? String.format("%.6f", ((Number)value).doubleValue()) : "");
                setHorizontalAlignment(SwingConstants.RIGHT); 
                if (!isSelected) {
                    setBackground(colorBackground);
                    setForeground(colorText);
                }
                return this;
            }
        });
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Integral Calculator");
        setBackground(new java.awt.Color(50, 3, 9));
        setResizable(false);
        setSize(new java.awt.Dimension(700, 600));

        jPanel1.setBackground(new java.awt.Color(34, 35, 36));
        jPanel1.setName(""); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(700, 600));

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
        ButtonAdd.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 153, 0)));
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
        ButtonCalculate.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 0, 153)));
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
        ButtonDelete.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(153, 0, 0)));
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
        ButtonClear.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(153, 102, 0)));
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
        ButtonFill.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 153, 0)));
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
        jLabel6.setText("Таблица");
        jLabel6.setOpaque(true);

        jLabel7.setBackground(new java.awt.Color(34, 35, 36));
        jLabel7.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Коллекция");
        jLabel7.setOpaque(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(TextFieldBottomLimit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(TextFieldStep, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(TextFieldUpperLimit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 27, Short.MAX_VALUE)
                                .addComponent(jLabel6)
                                .addGap(85, 85, 85)
                                .addComponent(jLabel7)
                                .addGap(78, 78, 78))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(ButtonDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(ButtonAdd, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(ButtonCalculate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(34, 34, 34)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(136, 136, 136))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(ButtonFill, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(ButtonClear, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButtonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ButtonFill, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextFieldUpperLimit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ButtonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(TextFieldBottomLimit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2))
                    .addComponent(ButtonClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(TextFieldStep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(ButtonCalculate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4))
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 718, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

          } 
            catch (NumberFormatException e) {
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
        
        if (selectedRows.length != 0) {
            for (int i = 0; i < selectedRows.length; i++) {
                int modelIndex = TableMain.convertRowIndexToModel(selectedRows[i]);
                RecIntegral rec = dataList.get(modelIndex);
                rec.calculateResult();
                double resValue = rec.getResult();
                model.setValueAt(resValue, modelIndex, 3);

            }
        } else {
            JOptionPane.showMessageDialog(this, "Выберите строки для вычисления", "Ошибка", JOptionPane.WARNING_MESSAGE);
        }
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
