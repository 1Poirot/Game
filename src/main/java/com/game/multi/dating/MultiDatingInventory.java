package com.game.multi.dating;

import java.awt.*;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.*;

public class MultiDatingInventory extends JDialog {
    public MultiDatingInventory(JFrame parent, Map<String, Integer> items, Consumer<String> onUse) {
        super(parent, "My Backpack", true);
        setSize(400, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("<html><b style='font-family:Tahoma; font-size:18pt;'> 🎒 กระเป๋าของฉัน</b></html>",
                SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        DefaultListModel<String> model = new DefaultListModel<>();
        for (String key : items.keySet()) {
            if (items.get(key) > 0) {
                model.addElement(key + " (มี " + items.get(key) + " ชิ้น)");
            }
        }

        JList<String> list = new JList<>(model);
        list.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(new JScrollPane(list), BorderLayout.CENTER);

        JButton useBtn = new JButton("<html><b style='font-family:Tahoma;'>🎁 ให้ของขวัญ</b></html>");
        useBtn.addActionListener(e -> {
            int selectedIndex = list.getSelectedIndex(); // ใช้ Index แทนการ split string
            if (selectedIndex != -1) {
                String selected = list.getSelectedValue();
                String itemName = selected.split(" ")[0];

                // หักของออกใน Map
                items.put(itemName, items.get(itemName) - 1);
                onUse.accept(itemName);

                // ✅ อัปเดตรายการในหน้าจอทันที (ไม่ต้องปิดหน้าต่าง)
                if (items.get(itemName) <= 0) {
                    model.remove(selectedIndex);
                } else {
                    model.set(selectedIndex, itemName + " (มี " + items.get(itemName) + " ชิ้น)");
                }

                if (model.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "ของหมดเกลี้ยงแล้วจ้า!");
                    dispose();
                }
            } else {
                JOptionPane.showMessageDialog(this, "กรุณาเลือกของที่จะให้ก่อน");
            }
        });

        add(useBtn, BorderLayout.SOUTH);
        if (model.isEmpty()) {
            JOptionPane.showMessageDialog(this, "กระเป๋าว่างเปล่า ไปซื้อของที่ร้านค้าก่อนนะ!");
            dispose();
        } else {
            setVisible(true);
        }
    }
}