package com.game.multi.dating;

import com.game.controllers.GameController;
import com.game.network.GameClient;
import com.game.ui.MultiplayerScreen;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class MultiDatingScreen extends JFrame {
    private final MultiDatingEventManager manager = new MultiDatingEventManager();
    private final MultiDatingSound sound = MultiDatingSound.getInstance();

    private int score = 0, money = 1000;
    private Map<String, Integer> inventory = new HashMap<>();
    private GameClient client;
    private GameController controller;
    private MultiplayerScreen parentScreen;

    private JLayeredPane layeredPane = new JLayeredPane();
    private JLabel bgLabel = new JLabel();
    private JLabel characterLabel = new JLabel();

    private JProgressBar heartBar = new JProgressBar(0, 100);
    private JLabel nameLabel = new JLabel("คัง ฮานา");
    private JLabel dialogLabel = new JLabel();
    private JLabel timerLabel = new JLabel();

    private JButton btnA, btnB, btnC;
    private JButton btnBag, btnShop, btnSettings;
    private MultiDatingTimer timer;

    private String currentBG = "";
    private String currentExpressionFile = "ผู้หญิง ตัวเอก.png";
    private final Font thaiFont = new Font("Tahoma", Font.BOLD, 18);

    private boolean isReactionActive = false;
    private Timer expressionTimer;

    public MultiDatingScreen(GameClient client, GameController controller, MultiplayerScreen parent) {
        this.client = client;
        this.controller = controller;
        this.parentScreen = parent;

        setTitle("💕 ศึกชิงนาง Online - ห้องแข่งจริง");
        setSize(1100, 750);
        setMinimumSize(new Dimension(1000, 650));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        btnA = createChoiceBtn();
        btnB = createChoiceBtn();
        btnC = createChoiceBtn();
        btnBag = createIconBtn("🎒");
        btnShop = createIconBtn("🛒");
        btnSettings = createIconBtn("⚙️");

        setContentPane(layeredPane);
        styleComponents();
        setupLayout();
        setupActions();
        loadEvent();
        updateScoreUI();

        try {
            sound.stopBGM();
            sound.playBGM("multihome.wav");
        } catch (Exception e) {
            System.err.println("Sound error");
        }

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                relayoutUI();
            }
        });

        timer = new MultiDatingTimer(10, timerLabel, this::finishGame);
        timer.start();

        setVisible(true);
        relayoutUI();
    }

    private void relayoutUI() {
        int w = getWidth(), h = getHeight();
        if (w <= 0 || h <= 0)
            return;

        bgLabel.setBounds(0, 0, w, h);
        Component topBar = getComponentByName("topBar");
        if (topBar != null)
            topBar.setBounds(0, 0, w, 100);

        int panelH = 220;
        int panelW = (int) (w * 0.95);
        int panelX = (w - panelW) / 2;
        int panelY = h - panelH - 60;

        Component interactionPanel = getComponentByName("interactionPanel");
        if (interactionPanel != null)
            interactionPanel.setBounds(panelX, panelY, panelW, panelH);

        nameLabel.setBounds(panelX + 50, panelY - 25, 220, 50);

        refreshImages();
        layeredPane.repaint();
    }

    private void refreshImages() {
        int w = getWidth(), h = getHeight();
        bgLabel.setIcon(loadImage("src/main/resources/images/backgrounds/" + currentBG, w, h, false));

        int targetH = (int) (h * 0.82);
        ImageIcon charIcon = loadImage("src/main/resources/images/Characters/" + currentExpressionFile, -1, targetH,
                true);
        if (charIcon != null) {
            characterLabel.setIcon(charIcon);
            int cw = charIcon.getIconWidth();
            characterLabel.setBounds((w / 2) - (cw / 2), h - targetH - 30, cw, targetH);
        }
    }

    private void setupActions() {
        btnA.addActionListener(e -> nextStep(manager.getCurrent().getScoreA()));
        btnB.addActionListener(e -> nextStep(manager.getCurrent().getScoreB()));
        btnC.addActionListener(e -> nextStep(manager.getCurrent().getScoreC()));

        btnShop.addActionListener(e -> new MultiDatingShop(this, money, (item, price) -> {
            if (this.money >= price) {
                this.money -= price;
                inventory.put(item, inventory.getOrDefault(item, 0) + 1);
                return true;
            }
            return false;
        }).setVisible(true));

        btnBag.addActionListener(e -> new MultiDatingInventory(this, inventory, (item) -> {
            int bonus = 0;
            String exp = "ผู้หญิง ตัวเอก.png";
            String msg = "";
            // แก้ไขใน MultiDatingScreen.java
            switch (item.trim()) {
                case "ดอกไม้": // ต้องตรงกับใน Shop
                case "สร้อยคอ":
                    bonus = 15;
                    exp = "ผู้หญิง เขิน.png";
                    msg = "ขอบคุณน้าาาา 😊";
                    break;

                case "กาแฟ": // ใน Shop คุณใช้ชื่อ "กาแฟ"
                case "ช็อกโกแลต": // ใน Shop คุณใช้ ต เต่า (ช็อกโกแลต)
                case "โดนัท":
                    bonus = 10;
                    exp = "ผู้หญิง ยิ้ม.png";
                    msg = "ขอบใจน้า ❤";
                    break;

                default:
                    bonus = 1;
                    msg = "อืม ขอบคุณ"; // ถ้าชื่อไม่ตรงกันเลย มันจะมาตกที่นี่
                    break;
            }
            score += bonus;
            updateScoreUI();
            showThankYouReaction(exp, msg);
        }));

        btnSettings.addActionListener(e -> showSettingsDialog());
    }

    private void showSettingsDialog() {
        JDialog settings = new JDialog(this, "Settings", true);

        // ✅ 1. ล็อกสเปค: ห้ามขยับ ห้ามปรับขนาด และเอาแถบหัวออก
        settings.setUndecorated(true);
        settings.setResizable(false);
        settings.setSize(420, 520);
        settings.setLocationRelativeTo(this); // Flex ไว้ตรงกลางเสมอ

        // ✅ 2. ดักจับกากบาท (JFrame หลัก) ให้หน้าต่างนี้ยังคงอยู่
        settings.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(new Color(255, 250, 252));
        content.setBorder(BorderFactory.createLineBorder(new Color(255, 105, 180), 4, true));
        settings.setContentPane(content);

        // --- ส่วนหัว ---
        JLabel lbTitle = new JLabel("SETTINGS", SwingConstants.CENTER);
        lbTitle.setFont(new Font("Tahoma", Font.BOLD, 26));
        lbTitle.setForeground(new Color(255, 20, 147));
        lbTitle.setBorder(new EmptyBorder(30, 0, 10, 0));
        content.add(lbTitle, BorderLayout.NORTH);

        // --- ส่วนกลาง (Slider + Buttons) ---
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(10, 50, 30, 50));

        // ✅ ส่วนควบคุมเสียงแบบ Slider
        JLabel lbVolume = new JLabel("ระดับเสียงเพลง");
        lbVolume.setFont(new Font("Tahoma", Font.BOLD, 16));
        lbVolume.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSlider volSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, (int) (sound.getVolume() * 100));
        volSlider.setOpaque(false);
        volSlider.setMajorTickSpacing(25);
        volSlider.setPaintTicks(false);
        volSlider.addChangeListener(e -> {
            float volume = volSlider.getValue() / 100f;
            sound.setVolume(volume);
            lbVolume.setText(volume == 0 ? "ปิดเสียง" : "ระดับเสียง: " + volSlider.getValue() + "%");
        });

        // ปุ่มรายการ
        Font btnFont = new Font("Tahoma", Font.BOLD, 17);
        JButton btnResume = createStyledMenuBtn("กลับไปเล่นต่อ", new Color(255, 182, 193), btnFont);
        btnResume.addActionListener(e -> settings.dispose());

        JButton btnLeave = createStyledMenuBtn("ออกจากห้องแข่ง", new Color(255, 99, 71), btnFont);
        btnLeave.setForeground(Color.WHITE);
        btnLeave.addActionListener(e -> {
            int res = JOptionPane.showConfirmDialog(settings, "คุณแน่ใจนะว่าจะทิ้งเกมนี้ไป?", "ยืนยัน",
                    JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                settings.dispose();
                exitAndGoToMain();
            }
        });

        // เพิ่มคอมโพเนนต์ลงแผง
        mainPanel.add(lbVolume);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(volSlider);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        mainPanel.add(btnResume);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(btnLeave);

        content.add(mainPanel, BorderLayout.CENTER);
        settings.setVisible(true);
    }

    // Helper สำหรับสร้างปุ่มสวยๆ (ห้ามลบ)
    private JButton createStyledMenuBtn(String text, Color bg, Font font) {
        JButton b = new JButton(text);
        b.setFont(font);
        b.setBackground(bg);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(300, 50));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                b.setBackground(bg.brighter());
            }

            public void mouseExited(MouseEvent e) {
                b.setBackground(bg);
            }
        });
        return b;
    }

    private void exitAndGoToMain() {
        sound.stopBGM();
        if (timer != null)
            timer.stop();
        if (client != null) {
            client.sendAction("LEAVE");
            client.disconnect();
        }
        dispose();
        if (parentScreen != null)
            parentScreen.dispose();
        SwingUtilities.invokeLater(() -> {
            if (controller != null)
                controller.showMainMenu();
        });
    }

    // ✅ แก้ไข: ลอจิกจบเกม (แยกขาดระหว่างเล่นคนเดียว กับ เล่นหลายคน)
    // ✅ แก้ไข: ลอจิกจบเกม (เล่นคนเดียวจบเลย / เล่นหลายคน "ต้อง" ค้างหน้ารอ Server
    // เท่านั้น)
    // ✅ แก้ไข: ลอจิกจบเกม (ล็อกจำนวนคนตามจริงในห้อง 2 หรือ 3 คน)
    // ✅ ใน MultiDatingScreen.java
    private void finishGame() {
        if (timer != null)
            timer.stop();
        sound.stopBGM();

        int playerCount = (client != null) ? client.getPlayerList().size() : 1;

        if (playerCount > 1) {
            // 🌐 ส่งคะแนนบอก Server (รอบเดียวพอ)
            if (client != null) {
                client.sendAction("FINISH:" + score);
            }

            // ล็อกหน้าจอรอ ห้ามโชว์ผลเอง
            btnA.setVisible(false);
            btnB.setVisible(false);
            btnC.setVisible(false);
            // ✅ แก้ไขบรรทัดเดิมให้เป็นแบบนี้ครับ
            dialogLabel.setText(
                    "<html><center><font face='Tahoma'>✨ บันทึกคะแนนสำเร็จ! ✨<br>กรุณารอเพื่อนสักครู่...</font></center></html>");

        } else {
            // 👤 เล่นคนเดียว สรุปทันที
            Map<String, Integer> solo = new HashMap<>();
            solo.put(client != null ? client.getPlayerName() : "คุณ", score);
            showFinalResults(solo);
        }
    }

    // ✅ เมธอดแสดงหน้าสรุปผล (Ranking 1, 2, 3 + ลอจิกแฟนคู่)
    // ✅ เมธอดแสดงหน้าสรุปผล (แก้ลอจิก Ranking ให้แม่นยำ ไม่เป็นที่ 1 กันทุกคน)
    public void showFinalResults(Map<String, Integer> allScores) {
        // ✅ 1. เพิ่มเกราะป้องกัน: ถ้าไม่มีข้อมูลคะแนนเลย
        // ให้ใส่คะแนนเราคนเดียวป้องกันโปรแกรมพัง
        if (allScores == null || allScores.isEmpty()) {
            allScores = new HashMap<>();
            String name = (client != null) ? client.getPlayerName() : "คุณ";
            allScores.put(name, score);
        }

        String myName = (client != null) ? client.getPlayerName() : "คุณ";

        // --- ส่วนวาด Dialog เหมือนเดิม ---
        JDialog resultDlg = new JDialog(this, "บทสรุปความรัก", true);
        resultDlg.setUndecorated(true);
        resultDlg.setSize(500, 650);
        resultDlg.setLocationRelativeTo(this);

        JPanel p = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 192, 203), 0, getHeight(), Color.WHITE);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.setColor(new Color(255, 20, 147));
                g2.setStroke(new BasicStroke(6));
                g2.drawRoundRect(3, 3, getWidth() - 7, getHeight() - 7, 40, 40);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(35, 40, 35, 40));

        // ✅ 2. จัดลำดับคะแนน
        java.util.List<Map.Entry<String, Integer>> sortedList = allScores.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .collect(java.util.stream.Collectors.toList());

        // ✅ 3. คำนวณอันดับด้วยความปลอดภัย (เช็ค Size ก่อน Get)
        int myScore = allScores.getOrDefault(myName, 0);
        int myRank = 1;
        int maxScore = sortedList.isEmpty() ? 0 : sortedList.get(0).getValue();

        for (Map.Entry<String, Integer> entry : sortedList) {
            if (entry.getValue() > myScore)
                myRank++;
        }

        long winnersCount = sortedList.stream().filter(e -> e.getValue() == maxScore).count();
        boolean isTieAtFirst = (winnersCount > 1);

        // --- ลอจิกข้อความเดิม ---
        String statusMsg;
        Color statusColor;
        if (isTieAtFirst && myScore == maxScore && allScores.size() > 1) {
            statusMsg = "💖 เสมอกัน! งั้นเป็นแฟนคู่ไปเลย 💖";
            statusColor = new Color(255, 105, 180);
        } else if (myRank == 1) {
            statusMsg = "👑 ยินดีด้วยคุณชนะใจเธอ";
            statusColor = new Color(50, 205, 50);
        } else if (myRank == 2) {
            statusMsg = "😐 ว้ายยย...เกือบชนะแต่เป็นได้แค่เพื่อน";
            statusColor = Color.ORANGE;
        } else {
            statusMsg = "💀 สมน้ำหน้า เขาไม่คิดแม้แต่จะมองไปฝึกมาใหม่";
            statusColor = Color.RED;
        }

        JLabel lbStatus = new JLabel("<html><center>" + statusMsg + "</center></html>", SwingConstants.CENTER);
        lbStatus.setFont(new Font("Tahoma", Font.BOLD, 22));
        lbStatus.setForeground(statusColor);
        lbStatus.setBorder(new EmptyBorder(0, 0, 20, 0));
        p.add(lbStatus, BorderLayout.NORTH);

        // --- 📊 4. รายการ Leaderboard ---
        JPanel listP = new JPanel();
        listP.setLayout(new BoxLayout(listP, BoxLayout.Y_AXIS));
        listP.setOpaque(false);

        int displayRank = 1;
        int lastScore = -1;
        int actualRank = 0;

        for (int i = 0; i < sortedList.size(); i++) {
            Map.Entry<String, Integer> entry = sortedList.get(i);

            // ลอจิกแสดงเลขลำดับ (ถ้าคะแนนเท่ากัน ให้ลำดับเลขเดียวกัน)
            if (entry.getValue() != lastScore) {
                actualRank = i + 1;
            }
            lastScore = entry.getValue();

            JPanel item = new JPanel(new BorderLayout());
            item.setOpaque(true);
            boolean isMe = entry.getKey().equals(myName);
            item.setBackground(isMe ? new Color(255, 20, 147, 40) : Color.WHITE);
            item.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(255, 182, 193), 2, true),
                    new EmptyBorder(12, 25, 12, 25)));

            JLabel nameLbl = new JLabel(actualRank + ". " + entry.getKey() + (isMe ? " (YOU)" : ""));
            nameLbl.setFont(new Font("Tahoma", Font.BOLD, 18));

            JLabel scoreLbl = new JLabel(entry.getValue() + " แต้ม");
            scoreLbl.setFont(new Font("Tahoma", Font.BOLD, 20));
            scoreLbl.setForeground(new Color(255, 20, 147));

            item.add(nameLbl, BorderLayout.WEST);
            item.add(scoreLbl, BorderLayout.EAST);
            item.setMaximumSize(new Dimension(420, 60));
            listP.add(item);
            listP.add(Box.createRigidArea(new Dimension(0, 12)));
        }

        JScrollPane scroll = new JScrollPane(listP);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        p.add(scroll, BorderLayout.CENTER);

        JButton btnClose = new JButton("ตกลง");
        btnClose.setFont(new Font("Tahoma", Font.BOLD, 20));
        btnClose.setBackground(new Color(255, 20, 147));
        btnClose.setForeground(Color.WHITE);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> {
            resultDlg.dispose();
            exitAndGoToMain();
        });

        JPanel btnP = new JPanel(new FlowLayout());
        btnP.setOpaque(false);
        btnP.add(btnClose);
        p.add(btnP, BorderLayout.SOUTH);

        resultDlg.add(p);
        resultDlg.setVisible(true);
    }

    private void loadEvent() {
        if (isReactionActive || manager.isFinished()) {
            if (manager.isFinished())
                finishGame();
            return;
        }
        MultiDatingEvent ev = manager.getCurrent();
        this.currentExpressionFile = ev.getDefaultExpression();
        currentBG = ev.getBackground();

        String pName = (client != null && client.getPlayerName() != null) ? client.getPlayerName() : "ผู้เล่น";
        String dialog, choiceA, choiceB, choiceC;

        // ✅ เช็คว่าเป็นเหตุการณ์แรก (Index 0) หรือไม่
        if (manager.getCurrentIndex() == 0) {
            // เหตุการณ์แรก: ใช้คำกลางๆ แทนชื่อ (ยังไม่รู้จักกัน)
            dialog = ev.getDialog().replace("%name%", "..").replace("...", "..");
            choiceA = ev.getChoiceA().replace("%name%", "เรา");
            choiceB = ev.getChoiceB().replace("%name%", "เรา");
            choiceC = ev.getChoiceC().replace("%name%", "เรา");
        } else {
            // เหตุการณ์ต่อๆ ไป: ใช้ชื่อผู้เล่นจริงตามปกติ
            dialog = ev.getDialog().replace("%name%", pName).replace("...", pName);
            choiceA = ev.getChoiceA().replace("%name%", pName);
            choiceB = ev.getChoiceB().replace("%name%", pName);
            choiceC = ev.getChoiceC().replace("%name%", pName);
        }

        dialogLabel.setText("<html><body style='width: 450px; color: #222222; font-family: Tahoma; font-size: 18px;'>“"
                + dialog + "”</body></html>");

        btnA.setText("<html><center>" + choiceA + "</center></html>");
        btnB.setText("<html><center>" + choiceB + "</center></html>");
        btnC.setText("<html><center>" + choiceC + "</center></html>");
        relayoutUI();
    }

    private void showThankYouReaction(String fileName, String message) {
        if (expressionTimer != null)
            expressionTimer.stop();
        isReactionActive = true;
        this.currentExpressionFile = fileName;
        btnA.setVisible(false);
        btnB.setVisible(false);
        btnC.setVisible(false);
        dialogLabel.setText("<html><body style='width: 450px; color: #FF1493; font-family: Tahoma; font-size: 18px;'>“"
                + message + "”</body></html>");
        refreshImages();
        expressionTimer = new Timer(5000, e -> {
            isReactionActive = false;
            btnA.setVisible(true);
            btnB.setVisible(true);
            btnC.setVisible(true);
            loadEvent();
        });
        expressionTimer.setRepeats(false);
        expressionTimer.start();
    }

    private void nextStep(int pts) {
        if (!isReactionActive) {
            score += pts;
            updateScoreUI();
            manager.next();
            loadEvent();
        }
    }

    private void updateScoreUI() {
        heartBar.setValue(Math.min(score, 100));
        heartBar.setString(Math.min(score, 100) + " / 100");
    }

    private Component getComponentByName(String name) {
        for (Component c : layeredPane.getComponents()) {
            if (name.equals(c.getName()))
                return c;
        }
        return null;
    }

    private ImageIcon loadImage(String path, int targetW, int targetH, boolean keepRatio) {
        try {
            Image img = null;
            File file = new File(path);
            if (file.exists())
                img = new ImageIcon(file.getAbsolutePath()).getImage();
            else {
                URL url = getClass().getResource(path.replace("src/main/resources", ""));
                if (url != null)
                    img = new ImageIcon(url).getImage();
            }
            if (img != null) {
                int fW = targetW, fH = targetH;
                if (keepRatio) {
                    double r = (double) img.getWidth(null) / img.getHeight(null);
                    if (fW == -1)
                        fW = (int) (fH * r);
                    else if (fH == -1)
                        fH = (int) (fW / r);
                }
                return new ImageIcon(img.getScaledInstance(Math.max(1, fW), Math.max(1, fH), Image.SCALE_SMOOTH));
            }
        } catch (Exception e) {
        }
        return null;
    }

    private void styleComponents() {
        // --- ปรับแต่ง Heart Bar (หลอดหัวใจ) ---
        heartBar.setUI(new BasicProgressBarUI() {
            @Override
            protected void paintDeterminate(Graphics g, JComponent c) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = c.getWidth(), h = c.getHeight();
                int mw = (int) (w * heartBar.getPercentComplete());

                // วาดพื้นหลังหลอด (สีขาวจางๆ)
                g2d.setColor(new Color(255, 255, 255, 180));
                g2d.fillRoundRect(0, 0, w, h, 20, 20);

                // วาดส่วนที่มีคะแนน (ไล่เฉดสีชมพู)
                if (mw > 0) {
                    g2d.setPaint(new GradientPaint(0, 0, new Color(255, 20, 147), mw, 0, new Color(255, 105, 180)));
                    g2d.fillRoundRect(0, 0, mw, h, 20, 20);
                }

                // วาดเส้นขอบหลอดให้คมชัด
                g2d.setColor(new Color(255, 20, 147));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, w - 2, h - 2, 20, 20);
            }
        });

        heartBar.setFont(new Font("Tahoma", Font.BOLD, 18));
        heartBar.setForeground(Color.WHITE); // ✅ เปลี่ยนสีตัวเลขเป็นสีขาวเพื่อให้ตัดกับหลอดสีชมพู
        heartBar.setStringPainted(true);

        // --- ✅ แก้ปัญหา Emoji สี่เหลี่ยมที่ Timer ---
        // ใช้ฟอนต์ Segoe UI Emoji เพื่อให้รูปนาฬิกาแสดงผลได้
        timerLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 26));
        timerLabel.setForeground(Color.WHITE);
        // ใส่เงาให้ตัวเลขเวลาหน่อยจะได้อ่านง่ายบนพื้นหลังสว่าง
        timerLabel.setUI(new javax.swing.plaf.basic.BasicLabelUI());

        // --- ปรับแต่งป้ายชื่อ ---
        nameLabel.setOpaque(true);
        nameLabel.setBackground(new Color(255, 20, 147));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setFont(thaiFont.deriveFont(Font.BOLD, 22f));
        nameLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2, true),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
    }

    private void setupLayout() {
        layeredPane.add(bgLabel, Integer.valueOf(0));
        layeredPane.add(characterLabel, Integer.valueOf(100));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setName("topBar");
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        statusPanel.setOpaque(false);
        statusPanel.add(heartBar);
        topBar.add(statusPanel, BorderLayout.WEST);

        JPanel toolPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        toolPanel.setOpaque(false);
        toolPanel.add(btnBag);
        toolPanel.add(btnShop);
        toolPanel.add(timerLabel);
        toolPanel.add(btnSettings);
        topBar.add(toolPanel, BorderLayout.EAST);
        layeredPane.add(topBar, Integer.valueOf(200));

        JPanel interactionPanel = new JPanel(new BorderLayout(25, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 240));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2d.setColor(new Color(255, 182, 193));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 40, 40);
            }
        };
        interactionPanel.setName("interactionPanel");
        interactionPanel.setOpaque(false);
        interactionPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        interactionPanel.add(dialogLabel, BorderLayout.CENTER);

        JPanel choiceContainer = new JPanel(new GridLayout(3, 1, 0, 10));
        choiceContainer.setOpaque(false);
        choiceContainer.setPreferredSize(new Dimension(350, 0));
        choiceContainer.add(btnA);
        choiceContainer.add(btnB);
        choiceContainer.add(btnC);
        interactionPanel.add(choiceContainer, BorderLayout.EAST);

        layeredPane.add(interactionPanel, Integer.valueOf(300));
        layeredPane.add(nameLabel, Integer.valueOf(500));
    }

    private JButton createChoiceBtn() {
        JButton b = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // ตั้งค่าสีพื้นหลังตอนปกติ / ตอนเอาเมาส์วาง / ตอนกด
                if (getModel().isPressed()) {
                    g2.setColor(new Color(255, 105, 180)); // สีเข้มขึ้นตอนกด
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 182, 193)); // สีอ่อนลงตอนเมาส์ชี้
                } else {
                    g2.setColor(new Color(255, 240, 245)); // สีปกติ
                }

                // วาดพื้นหลังมนๆ (แต่ไม่วาดเส้นขอบแล้ว)
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                super.paintComponent(g);
                g2.dispose();
            }
        };

        b.setForeground(new Color(255, 20, 147)); // สีตัวอักษรชมพูเข้ม
        b.setFont(new Font("Tahoma", Font.BOLD, 16));

        // ✅ จุดสำคัญ: สั่งไม่ให้วาด Border มาตรฐานของปุ่ม
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false); // เอาเส้นประตอนกดออกด้วย

        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(5, 10, 5, 10)); // เพิ่มระยะห่างข้างในแทน

        return b;
    }

    private JButton createIconBtn(String icon) {
        JButton b = new JButton(icon);
        b.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }
}