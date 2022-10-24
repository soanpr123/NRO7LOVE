package com.girlkun.tool;

import real.item.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CreateRoundDB extends  JFrame {
    private JPanel jPmain;
    int index;
    private List<Item> listItem=new ArrayList<>();
    private List<ItemTemplate> itemTemplates;
    private List<ItemOptionTemplate> itemOptionTemplates;
    private DefaultTableModel model;
    private JComboBox cboItem;
    private JComboBox cboOtion;
    private JButton btnAddItem;
    private JTable table1;
    private JTextField textParams;
    private JButton btnAddParams;
    private JTextArea textArea1;
    private JLabel jbImage;
    private JButton btnRemove;
    private JButton createButton;

    public  CreateRoundDB(){
        this.model = new DefaultTableModel((Object[]) new String[]{"Index", "Item", "Options", "Params"}, 0) {
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };

        table1.setModel(model);
        init();
        this.cboItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                    CreateRoundDB.this.loadImageItem((CreateRoundDB.this.itemTemplates.get(CreateRoundDB.this.cboItem.getSelectedIndex())).iconID);

            }
        });
    setContentPane(jPmain);
}
    private void init() {
        btnAddParams.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jButton2ActionPerformed(e);
            }
        });
        btnAddItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jButton1ActionPerformed(e);
            }
        });
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CreateRoundDB.this.tblListMouseClicked(e);
            }
        });
        this.itemTemplates = ItemTemplateDAO.getAll();
        this.itemTemplates.sort((o1, o2) -> o1.name.compareToIgnoreCase(o2.name));

        this.itemOptionTemplates = ItemOptionTemplateDAO.getAll();
        for (ItemOptionTemplate iot : this.itemOptionTemplates) {
            this.cboOtion.addItem(iot.id + " - " + iot.name);
        }
        for (ItemTemplate it : this.itemTemplates) {
            this.cboItem.addItem(it.id + " - " + it.name + " (" + it.description + ")");
        }
    }

    private void loadImageItem(int iconId) {
        try {
            BufferedImage img = ImageIO.read(new File("data/res_icon_new/x4/" + iconId +".png"));
            this.jbImage.setIcon(new ImageIcon(img.getScaledInstance(img.getWidth() * 2, img.getHeight() * 2, 4)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void jButton1ActionPerformed(ActionEvent evt) {
        Item item = new Item();
        ItemTemplate temp = this.itemTemplates.get(this.cboItem.getSelectedIndex());
        item.template = new ItemTemplate(temp.id, temp.type, temp.gender, temp.name, temp.description, temp.iconID, temp.part, temp.isUpToUp, temp.strRequire);

        ItemOption op = new ItemOption();
        op.optionTemplate = new ItemOptionTemplate(73, "",0);
        op.param = 0;
        item.itemOptions.add(op);
        this.listItem.add(item);
        fillToTable();
        this.index = this.listItem.size() - 1;
        showInfoItem(item);
        this.table1.setRowSelectionInterval(this.index, this.index);
    }

    private void tblListMouseClicked(MouseEvent evt) {
        this.index = this.table1.getSelectedRow();
        if (this.index != -1) {
            loadImageItem(((Item) this.listItem.get(this.index)).template.iconID);
            showInfoItem(this.listItem.get(this.index));
        }
    }

    private void fillToTable() {
        this.model.setRowCount(0);
        for (int i = 0; i < this.listItem.size(); i++) {
            this.model.addRow(new Object[]{
                    Integer.valueOf(i), ((Item) this.listItem.get(i)).template.name, 0, 0
            });
        }
        this.index = -1;
    }

    private void jButton2ActionPerformed(ActionEvent evt) {
        if (this.index != -1) {
            try {
                int param = Integer.parseInt(this.textParams.getText());
                if (param >= 0) {
                    ItemOption op = new ItemOption();
                    op.optionTemplate = new ItemOptionTemplate(((ItemOptionTemplate) this.itemOptionTemplates.get(this.cboOtion.getSelectedIndex())).id, ((ItemOptionTemplate) this.itemOptionTemplates.get(this.cboOtion.getSelectedIndex())).name,0);
                    op.param = (short) param;
                    ((Item) this.listItem.get(this.index)).itemOptions.add(op);
                    showInfoItem(this.listItem.get(this.index));
                    JOptionPane.showMessageDialog(this, "done add option");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showInfoItem(Item item) {
        if (item != null) {
            this.textArea1.setText("");
            this.textArea1.append(item.template.name + "\n");
            for (ItemOption op : item.itemOptions) {
                this.textArea1.append(op.optionTemplate.name.replaceAll("#", op.param + "") + "\n");
            }
        }
    }

    public static void main(String[] args) {
        CreateRoundDB dialog = new CreateRoundDB();
        dialog.setSize(900,500);
        dialog.setVisible(true);
        dialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
