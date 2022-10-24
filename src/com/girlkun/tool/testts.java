package com.girlkun.tool;

import real.item.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class testts  extends  JFrame{
    private JPanel jPanelMain;
    int index;
    private List<Item> listItem=new ArrayList<>();
    private JComboBox cboItem;
    private JComboBox cboOption;
    private JTextField textField1;
    private List<ItemTemplate> itemTemplates;
    private List<ItemOptionTemplate> itemOptionTemplates;
    private JTable table1;
    private JTextArea textInfo;
    private JLabel JlbImage;
    private JButton addParamsButton;
    private JButton btncreate;
    private JButton btnRemove;
    private JButton button1;
    private DefaultTableModel model;
public  testts(){
    this.index=-1;
    this.model = new DefaultTableModel((Object[]) new String[]{"Index", "Item", "Options", "Params"}, 0) {
        public boolean isCellEditable(int i, int i1) {
            return false;
        }
    };

    table1.setModel(model);

    init();
    this.cboItem.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
            testts.this.loadImageItem((testts.this.itemTemplates.get(testts.this.cboItem.getSelectedIndex())).iconID);
        }
    });
    addParamsButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            testts.this.jButton1ActionPerformed(e);
        }
    });
    setContentPane(jPanelMain);

}
    public static void main(String[] args) {

        testts dialog = new testts();
            dialog.setSize(900,500);
        dialog.setVisible(true);
        dialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        System.exit(0);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
    private void init() {
        this.itemTemplates = ItemTemplateDAO.getAll();
        this.itemTemplates.sort((o1, o2) -> o1.name.compareToIgnoreCase(o2.name));

        this.itemOptionTemplates = ItemOptionTemplateDAO.getAll();
        for (ItemOptionTemplate iot : this.itemOptionTemplates) {
            this.cboOption.addItem(iot.id + " - " + iot.name);
        }
        for (ItemTemplate it : this.itemTemplates) {
            this.cboItem.addItem(it.id + " - " + it.name + " (" + it.description + ")");
        }
    }

    private void loadImageItem(int iconId) {
        try {
            BufferedImage img = ImageIO.read(new File("data/res_icon_new/x4/" + iconId +".png"));
            this.JlbImage.setIcon(new ImageIcon(img.getScaledInstance(img.getWidth() * 2, img.getHeight() * 2, 4)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void showInfoItem(Item item) {
        if (item != null) {
            this.textInfo.setText("");
            this.textInfo.append(item.template.name + "\n");
            for (ItemOption op : item.itemOptions) {
                this.textInfo.append(op.optionTemplate.name.replaceAll("#", op.param + "") + "\n");
            }
        }
    }

    private void jButton1ActionPerformed(ActionEvent evt) {
        Item item = new Item();
        ItemTemplate temp = this.itemTemplates.get(this.cboItem.getSelectedIndex());
        item.template = new ItemTemplate(temp.id, temp.type, temp.gender, temp.name, temp.description, temp.iconID, temp.part, temp.isUpToUp, temp.strRequire);

        ItemOption op = new ItemOption();
        try {
            int param = Integer.parseInt(this.textField1.getText());
            if (param >= 0) {

                op.optionTemplate = new ItemOptionTemplate(((ItemOptionTemplate) this.itemOptionTemplates.get(this.cboOption.getSelectedIndex())).id, ((ItemOptionTemplate) this.itemOptionTemplates.get(this.cboOption.getSelectedIndex())).name,0);
                op.param = (short) param;
//                ((Item) this.listItem.get(this.index)).itemOptions.add(op);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        op.param = 0;
        item.itemOptions.add(op);
        System.out.println(item.template.name);
        this.listItem.add(item);

        fillToTable();
        this.index = this.listItem.size() - 1;

        showInfoItem(item);
        JOptionPane.showMessageDialog(this, "done add option");
        this.table1.setRowSelectionInterval(this.index, this.index);
    }
    private void fillToTable() {
        this.model.setRowCount(0);
        for (int i = 0; i < this.listItem.size(); i++) {

            for (ItemOption io:((Item) this.listItem.get(i)).itemOptions){
                this.model.addRow(new Object[]{
                        Integer.valueOf(i), ((Item) this.listItem.get(i)).template.name,  io.optionTemplate.name, io.param
                });
            }
        }
        this.index = -1;
    }
}
