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

public class MultiDatingScreen extends JFrame implements GameClient.MessageListener {
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
        btnA.addActionListener(e -> nextStep(manager.getCurrent().getChoiceScore(0)));
        btnB.addActionListener(e -> nextStep(manager.getCurrent().getChoiceScore(1)));
        btnC.addActionListener(e -> nextStep(manager.getCurrent().getChoiceScore(2)));

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
                    bonus = 3;
                    exp = "ผู้หญิง เขิน.png";
                    msg = "ขอบคุณน้าาาา 😊";
                    break;

                case "กาแฟ": // ใน Shop คุณใช้ชื่อ "กาแฟ"
                case "ช็อกโกแลต": // ใน Shop คุณใช้ ต เต่า (ช็อกโกแลต)
                case "โดนัท":
                    bonus = 2;
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
        settings.setUndecorated(true);
        settings.setResizable(false);
        settings.setSize(450, 550); // ปรับขนาดให้กว้างขวางขึ้นเล็กน้อย
        settings.setLocationRelativeTo(this);
        settings.setBackground(new Color(0, 0, 0, 0)); // ทำให้พื้นหลัง Dialog โปร่งใสเพื่อขอบมนที่แท้จริง

        // แผงเนื้อหาหลักพร้อมการวาด Gradient และขอบมน
        JPanel content = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // พื้นหลังไล่เฉดสีขาวนวลไปชมพูอ่อน
                GradientPaint gp = new GradientPaint(0, 0, Color.WHITE, 0, getHeight(), new Color(255, 240, 245));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);
                
                // เส้นขอบสีชมพูเข้มแบบหนาพรีเมียม
                g2d.setColor(new Color(255, 105, 180));
                g2d.setStroke(new BasicStroke(5));
                g2d.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, 50, 50);
                g2d.dispose();
            }
        };
        content.setOpaque(false);
        settings.setContentPane(content);

        // --- ส่วนหัว (Title) ---
        JLabel lbTitle = new JLabel("SETTINGS", SwingConstants.CENTER);
        lbTitle.setFont(new Font("Tahoma", Font.BOLD, 32));
        lbTitle.setForeground(new Color(255, 20, 147));
        lbTitle.setBorder(new EmptyBorder(40, 0, 20, 0));
        content.add(lbTitle, BorderLayout.NORTH);

        // --- ส่วนกลาง (Controls) ---
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(10, 50, 40, 50));

        // หัวข้อเสียง
        JLabel lbVolume = new JLabel("Music Volume");
        lbVolume.setFont(new Font("Tahoma", Font.BOLD, 18));
        lbVolume.setForeground(new Color(150, 50, 80));
        lbVolume.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ปรับแต่ง Slider
        JSlider volSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, (int) (sound.getVolume() * 100));
        volSlider.setOpaque(false);
        volSlider.setPreferredSize(new Dimension(300, 50));
        volSlider.addChangeListener(e -> {
            float volume = volSlider.getValue() / 100f;
            sound.setVolume(volume);
            lbVolume.setText(volume == 0 ? "Muted 🔇" : "Music Volume: " + volSlider.getValue() + "%");
        });

        // ข้อความแสดงสถานะเสียงปัจจุบัน
        lbVolume.setText(sound.getVolume() == 0 ? "Muted 🔇" : "Music Volume: " + (int)(sound.getVolume()*100) + "%");

        // ปุ่มคำสั่ง
        Font btnFont = new Font("Tahoma", Font.BOLD, 18);
        
        JButton btnResume = createStyledMenuBtn("กลับเข้าสู่เกม", new Color(255, 182, 193), btnFont);
        btnResume.setForeground(new Color(150, 20, 80));
        btnResume.addActionListener(e -> settings.dispose());

        JButton btnLeave = createStyledMenuBtn("ออกจากเกม", new Color(255, 99, 71), btnFont);
        btnLeave.setForeground(Color.WHITE);
        btnLeave.addActionListener(e -> {
            int res = JOptionPane.showConfirmDialog(settings, 
                "คุณแน่ใจนะว่าจะออก ?", "Confirm Exit",
                JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                settings.dispose();
                exitAndGoToMain();
            }
        });

        // ประกอบร่าง
        mainPanel.add(lbVolume);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(volSlider);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        mainPanel.add(btnResume);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
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

    // ======================================================
    // ✅ GameClient.MessageListener Implementation
    // ======================================================
    @Override
    public void onRejected(String reason) {
        SwingUtilities.invokeLater(() -> {
            // 1. หยุดเสียงและไทม์เมอร์
            sound.stopBGM();
            if (timer != null) timer.stop();

            // 2. แสดงแจ้งเตือน
            JOptionPane.showMessageDialog(null, 
                "<html><font face='Tahoma'>⚠️ <b>การเชื่อมต่อสิ้นสุด</b><br>" + reason + "</font></html>", 
                "แจ้งเตือนจากเซิร์ฟเวอร์", 
                JOptionPane.ERROR_MESSAGE);

            // 3. ตัดการเชื่อมต่อและล้างหน้าจอ
            if (client != null) client.disconnect();
            
            // ปิดหน้า Lobby เดิม (ถ้ามี)
            if (parentScreen != null) parentScreen.dispose();
            
            // ปิดหน้าจอการเล่นปัจจุบัน
            this.dispose();

            // 4. กลับหน้าเมนูผ่าน Controller
            if (controller != null) {
                controller.showMainMenu();
            }
        });
    }

    // เมธอดเหล่านี้ต้องใส่ไว้ให้ครบตาม Interface แม้ไม่ได้ใช้งานในหน้านี้
    @Override public void onPlayerListUpdate(java.util.List<String> players) {}
    @Override public void onGameStart() {}
    @Override public void onSystemMessage(String message) {}
    @Override public void onConnectionFailed(String ip) {}
    @Override public void onScoreUpdate(String message) {}
    @Override public void onWinner(String winnerName) {}
    @Override public void onFinalScore() {}
    @Override public void onFinalScoreItem(String playerName, int score) {}

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
        if (allScores == null || allScores.isEmpty()) {
            allScores = new HashMap<>();
            allScores.put((client != null) ? client.getPlayerName() : "คุณ", score);
        }

        String myName = (client != null) ? client.getPlayerName() : "คุณ";

        JDialog resultDlg = new JDialog(this, "บทสรุปความรัก", true);
        resultDlg.setUndecorated(true);
        resultDlg.setSize(480, 620);
        resultDlg.setLocationRelativeTo(this);
        resultDlg.setBackground(new Color(0, 0, 0, 0));

        JPanel root = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, Color.WHITE, 0, getHeight(), new Color(255, 240, 245));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);
                g2.setColor(new Color(255, 20, 147));
                g2.setStroke(new BasicStroke(5));
                g2.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, 50, 50);
                g2.dispose();
            }
        };
        root.setOpaque(false);
        root.setBorder(new EmptyBorder(30, 35, 30, 35));

        // --- 🏆 จัดลำดับและคำนวณ Rank ---
        java.util.List<Map.Entry<String, Integer>> sortedList = allScores.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .collect(java.util.stream.Collectors.toList());

        int myScore = allScores.getOrDefault(myName, 0);
        int maxScore = sortedList.isEmpty() ? 0 : sortedList.get(0).getValue();
        int myRank = 1;
        for (Map.Entry<String, Integer> entry : sortedList) {
            if (entry.getValue() > myScore)
                myRank++;
        }

        long winnersCount = sortedList.stream().filter(e -> e.getValue() == maxScore).count();
        boolean isTieAtFirst = (winnersCount > 1);

        // --- 💬 คืนค่าลอจิกข้อความกวนๆ (ต้นฉบับ) ---
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

        // ปรับ JLabel ให้รองรับ HTML เพื่อให้ข้อความยาวๆ ตัดบรรทัดได้
        JLabel lbStatus = new JLabel("<html><center>" + statusMsg + "</center></html>", SwingConstants.CENTER);
        lbStatus.setFont(new Font("Tahoma", Font.BOLD, 22));
        lbStatus.setForeground(statusColor);
        lbStatus.setPreferredSize(new Dimension(400, 80)); // จองที่ให้ข้อความ 2 บรรทัด
        lbStatus.setBorder(new EmptyBorder(0, 0, 15, 0));
        root.add(lbStatus, BorderLayout.NORTH);

        // --- 📊 ส่วนรายการคะแนน (Leaderboard) ---
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        for (int i = 0; i < sortedList.size(); i++) {
            Map.Entry<String, Integer> entry = sortedList.get(i);
            boolean isMe = entry.getKey().equals(myName);

            JPanel itemCard = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(isMe ? new Color(255, 182, 193, 150) : Color.WHITE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    g2.setColor(new Color(255, 20, 147, 50));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                    g2.dispose();
                }
            };
            itemCard.setOpaque(false);
            itemCard.setBorder(new EmptyBorder(12, 25, 12, 25));
            itemCard.setMaximumSize(new Dimension(420, 60));

            JLabel nameLabel = new JLabel((i + 1) + ". " + entry.getKey() + (isMe ? " (YOU)" : ""));
            nameLabel.setFont(new Font("Tahoma", isMe ? Font.BOLD : Font.PLAIN, 18));

            JLabel scoreLabel = new JLabel(entry.getValue() + " แต้ม");
            scoreLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
            scoreLabel.setForeground(new Color(255, 20, 147));

            itemCard.add(nameLabel, BorderLayout.WEST);
            itemCard.add(scoreLabel, BorderLayout.EAST);

            listPanel.add(itemCard);
            listPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        }

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        root.add(scroll, BorderLayout.CENTER);

        // --- 🔘 ส่วนปุ่มปิด (Footer) ---
        JButton btnClose = new JButton("ตกลง") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 20, 147));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        btnClose.setFont(new Font("Tahoma", Font.BOLD, 18));
        btnClose.setForeground(Color.WHITE);
        btnClose.setContentAreaFilled(false);
        btnClose.setBorderPainted(false);
        btnClose.setFocusPainted(false);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.setPreferredSize(new Dimension(160, 45));
        btnClose.addActionListener(e -> {
            resultDlg.dispose();
            exitAndGoToMain();
        });

        JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnWrapper.setOpaque(false);
        btnWrapper.setBorder(new EmptyBorder(20, 0, 0, 0));
        btnWrapper.add(btnClose);
        root.add(btnWrapper, BorderLayout.SOUTH);

        resultDlg.add(root);
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
        String dialog, cA, cB, cC;

        // 1. ดึงข้อความดิบจาก Choice ที่ถูก Shuffle ไว้แล้ว (0, 1, 2)
        dialog = ev.getDialog();
        cA = ev.getChoiceText(0);
        cB = ev.getChoiceText(1);
        cC = ev.getChoiceText(2);

        // 2. จัดการเรื่องการแทนที่ชื่อ (%name%)
        if (manager.getCurrentIndex() == 0) {
            // เหตุการณ์แรก: ยังไม่รู้จักชื่อกัน
            dialog = dialog.replace("%name%", "..").replace("...", "..");
            cA = cA.replace("%name%", "เรา");
            cB = cB.replace("%name%", "เรา");
            cC = cC.replace("%name%", "เรา");
        } else {
            // เหตุการณ์ต่อๆ ไป: ใช้ชื่อผู้เล่นจริง
            dialog = dialog.replace("%name%", pName).replace("...", pName);
            cA = cA.replace("%name%", pName);
            cB = cB.replace("%name%", pName);
            cC = cC.replace("%name%", pName);
        }

        // 3. แสดงผลบน UI
        dialogLabel.setText("<html><body style='width: 450px; color: #222222; font-family: Tahoma; font-size: 18px;'>“"
                + dialog + "”</body></html>");

        btnA.setText("<html><center>" + cA + "</center></html>");
        btnB.setText("<html><center>" + cB + "</center></html>");
        btnC.setText("<html><center>" + cC + "</center></html>");

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
        heartBar.setPreferredSize(new Dimension(300, 42));
        heartBar.setUI(new BasicProgressBarUI() {
            @Override
            protected void paintDeterminate(Graphics g, JComponent c) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = c.getWidth();
                int h = c.getHeight();

                // --- 1. การคำนวณตำแหน่ง (Dynamic Positioning) ---
                int circleSize = h; // วงกลมสูงเท่าหลอด
                int barH = (int) (h * 0.55); // ตัวหลอดบางลงเพื่อให้ดูแพง
                int barY = (h - barH) / 2;
                int barX = circleSize / 2; // หลอดเริ่มจากกลางวงกลม
                int barW = w - barX - 5;

                // --- 2. วาดตัวหลอด (Bar Background) ---
                // ขอบชมพูเข้มบางๆ
                g2d.setColor(new Color(255, 105, 180));
                g2d.fillRoundRect(barX, barY, barW, barH, 20, 20);
                // พื้นในขาวนวล
                g2d.setColor(new Color(255, 245, 250));
                g2d.fillRoundRect(barX + 2, barY + 2, barW - 4, barH - 4, 18, 18);

                // --- 3. วาดแถบพลัง (Progress Fill) ---
                int progressW = (int) ((barW - 6) * heartBar.getPercentComplete());
                if (progressW > 0) {
                    // ใช้ Gradient ชมพูขาวตามที่ขอ
                    GradientPaint pinkGrad = new GradientPaint(barX + 3, 0, new Color(255, 20, 147),
                            barX + 3 + progressW, 0, new Color(255, 182, 193));
                    g2d.setPaint(pinkGrad);
                    g2d.fillRoundRect(barX + 3, barY + 3, progressW, barH - 6, 15, 15);
                }

                // --- 4. ✅ วาดวงกลมไอคอน (Layered Circle) ---
                // วงนอกสีชมพู (ขอบบางๆ)
                g2d.setColor(new Color(255, 105, 180));
                g2d.fillOval(0, 0, circleSize, circleSize);
                // วงในสีขาว (เด่นขึ้นมา)
                g2d.setColor(Color.WHITE);
                g2d.fillOval(2, 2, circleSize - 4, circleSize - 4);
                // วงในสุดสีชมพูจางๆ
                g2d.setColor(new Color(255, 240, 245));
                g2d.fillOval(4, 4, circleSize - 8, circleSize - 8);

                // --- 5. ✅ วาดหัวใจแบบใช้สมการ (Vector Heart) เพื่อให้อยู่ตรงกลางเป๊ะ! ---
                g2d.setColor(new Color(255, 20, 147));
                drawPerfectHeart(g2d, circleSize / 2, circleSize / 2, circleSize / 3);

                g2d.dispose();
            }

            // ✅ ฟังก์ชันวาดหัวใจให้กึ่งกลางพิกัด x, y
            private void drawPerfectHeart(Graphics2D g2, int x, int y, int size) {
                int width = size;
                int height = size;
                g2.translate(x, y + 2); // ปรับตำแหน่ง y ลงมานิดหน่อยให้ดูสมดุลสายตา

                // วาดหัวใจโดยใช้สมการปีกสองข้าง
                g2.fillArc(-width / 2, -height / 2, width / 2, height / 2, 0, 180);
                g2.fillArc(0, -height / 2, width / 2, height / 2, 0, 180);
                int[] xPoints = { -width / 2, 0, width / 2 };
                int[] yPoints = { -height / 8, height / 2, -height / 8 };
                g2.fillPolygon(xPoints, yPoints, 3);

                g2.translate(-x, -(y + 2)); // คืนค่าแกน
            }
        });

        heartBar.setOpaque(false);
        heartBar.setBorderPainted(false);
        heartBar.setStringPainted(false); // เอาเปอร์เซ็นต์ออก

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