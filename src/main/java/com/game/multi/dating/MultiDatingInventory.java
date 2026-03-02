package com.game.multi.dating;

import java.awt.*;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.*;

public class MultiDatingInventory extends JDialog {
    public MultiDatingInventory(JFrame parent, Map<String, Integer> items, Consumer<String> onUse) {
        super(parent, "My Backpack", true);
        setSize(400, 550);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // --- ส่วนหัวกระเป๋า ---
        JLabel title = new JLabel(
                "<html><center><b style='font-family:Tahoma; font-size:20pt; color:#FF1493;'>🎒 กระเป๋าของฉัน</b><br><font size='3' color='gray'>เลือกของขวัญที่ซื้อมาเพื่อมอบให้เธอ</font></center></html>",
                SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // --- รายการไอเทมในกระเป๋า ---
        DefaultListModel<String> model = new DefaultListModel<>();
        updateListModel(model, items);

        JList<String> list = new JList<>(model);
        list.setFont(new Font("Tahoma", Font.PLAIN, 18));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFixedCellHeight(40);
        add(new JScrollPane(list), BorderLayout.CENTER);

        // --- ปุ่มให้ของขวัญ ---
        JButton useBtn = new JButton(
                "<html><b style='font-family:Tahoma; font-size:16pt;'>🎁 ให้ของขวัญทันที</b></html>");
        useBtn.setBackground(new Color(255, 105, 180));
        useBtn.setForeground(Color.WHITE);
        useBtn.setPreferredSize(new Dimension(0, 70));
        useBtn.setFocusPainted(false);
        useBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        useBtn.addActionListener(e -> {
            int selectedIndex = list.getSelectedIndex();
            if (selectedIndex != -1) {
                String selected = list.getSelectedValue();
                // ดึงชื่อไอเทมออกมาจาก String เช่น "ดอกไม้ (มี 2 ชิ้น)" -> "ดอกไม้"
                String itemName = selected.substring(0, selected.indexOf(" ("));

                // 1. หักของออกจาก Map ในระบบหลัก
                int currentAmount = items.get(itemName);
                items.put(itemName, currentAmount - 1);

                // 2. ส่งชื่อไอเทมกลับไปที่หน้าจอหลัก (เพื่อบวกคะแนน + เปลี่ยนหน้าค้าง 5 วิ)
                onUse.accept(itemName);

                // 3. ✅ ปิดหน้าต่างกระเป๋าทันทีเพื่อให้เห็นปฏิกิริยาตัวละครในหน้าจอหลัก
                dispose();

            } else {
                JOptionPane.showMessageDialog(this, "กรุณาเลือกของที่จะให้ก่อนนะจ๊ะ", "แจ้งเตือน",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        add(useBtn, BorderLayout.SOUTH);

        // ตรวจสอบว่ามีของในกระเป๋าไหมก่อนแสดงผล
        if (model.isEmpty()) {
            JOptionPane.showMessageDialog(this, "กระเป๋าว่างเปล่า ไปซื้อของที่ร้านค้าก่อนนะ!", "ไม่มีไอเทม",
                    JOptionPane.INFORMATION_MESSAGE);
            // ไม่ต้องทำ dispose() ตรงนี้ เพราะยังไม่ได้เรียก setVisible
        } else {
            setVisible(true);
        }
    }

    /**
     * เมธอดสำหรับอัปเดตข้อมูลใน List
     */
    private void updateListModel(DefaultListModel<String> model, Map<String, Integer> items) {
        model.clear();
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            if (entry.getValue() > 0) {
                model.addElement(entry.getKey() + " (มี " + entry.getValue() + " ชิ้น)");
            }
        }
    }
}