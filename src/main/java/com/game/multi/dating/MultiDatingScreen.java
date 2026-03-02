package com.game.multi.dating;

import com.game.controllers.GameController;
import com.game.network.GameClient;
import com.game.ui.MenuGame;
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

    private JLayeredPane layeredPane = new JLayeredPane();
    private JLabel bgLabel = new JLabel();
    private JLabel characterLabel = new JLabel();

    private JProgressBar heartBar = new JProgressBar(0, 100);
    private JLabel nameLabel = new JLabel();
    private JLabel dialogLabel = new JLabel();
    private JLabel timerLabel = new JLabel();

    private JButton btnA, btnB, btnC;
    private JButton btnBag, btnShop, btnSound, btnExit;
    private MultiDatingTimer timer;

    private String currentBG = "";
    private String currentExpressionFile = "ผู้หญิง ตัวเอก.png";
    private final Font thaiFont = new Font("Tahoma", Font.BOLD, 18);

    private boolean isReactionActive = false; // ✅ ล็อคเฉพาะตอนขอบคุณ (ให้ของ)
    private Timer expressionTimer; // ✅ คุมเวลา 5 วินาทีตอนให้ของ

    public MultiDatingScreen(GameClient client, GameController controller) {
        this.client = client;
        this.controller = controller;

        btnA = createChoiceBtn();
        btnB = createChoiceBtn();
        btnC = createChoiceBtn();
        btnBag = createIconBtn("🎒");
        btnShop = createIconBtn("🛒");
        btnSound = createIconBtn("🔊");
        btnExit = createIconBtn("❌");

        setTitle("💕 ศึกชิงนาง Online");
        setSize(1100, 750);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setContentPane(layeredPane);
        styleComponents();
        setupLayout();
        setupActions();
        loadEvent();
        updateScoreUI();

        sound.stopBGM();
        sound.playBGM("audiotest2.wav");

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                relayoutUI();
            }
        });

        timer = new MultiDatingTimer(600, timerLabel, this::finishGame);
        timer.start();

        setVisible(true);
    }

    private void styleComponents() {
        heartBar.setUI(new BasicProgressBarUI() {
            @Override
            protected void paintDeterminate(Graphics g, JComponent c) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = c.getWidth(), h = c.getHeight();
                int mappedW = (int) (w * heartBar.getPercentComplete());
                g2d.setColor(new Color(255, 255, 255, 150));
                g2d.fillRoundRect(0, 0, w, h, 15, 15);
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 20, 147), mappedW, 0,
                        new Color(255, 105, 180));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, mappedW, h, 15, 15);
                g2d.setColor(new Color(255, 20, 147));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, w - 1, h - 1, 15, 15);
            }
        });
        heartBar.setFont(new Font("Tahoma", Font.BOLD, 16));
        heartBar.setForeground(Color.RED);
        heartBar.setStringPainted(true);
        timerLabel.setFont(thaiFont.deriveFont(24f));
        timerLabel.setForeground(Color.WHITE);
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
        toolPanel.add(btnSound);
        toolPanel.add(timerLabel);
        toolPanel.add(btnExit);
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
        dialogLabel.setForeground(new Color(40, 40, 40));
        interactionPanel.add(dialogLabel, BorderLayout.CENTER);

        JPanel choiceContainer = new JPanel(new GridLayout(3, 1, 0, 10));
        choiceContainer.setOpaque(false);
        choiceContainer.setPreferredSize(new Dimension(300, 0));
        choiceContainer.add(btnA);
        choiceContainer.add(btnB);
        choiceContainer.add(btnC);
        interactionPanel.add(choiceContainer, BorderLayout.EAST);

        layeredPane.add(interactionPanel, Integer.valueOf(300));
        layeredPane.add(nameLabel, Integer.valueOf(400));
    }

    private void relayoutUI() {
        int w = layeredPane.getWidth(), h = layeredPane.getHeight();
        if (w <= 0 || h <= 0)
            return;
        bgLabel.setBounds(0, 0, w, h);
        Component topBar = getComponentByName("topBar");
        if (topBar != null)
            topBar.setBounds(0, 0, w, 100);
        int panelH = 210, panelW = (int) (w * 0.92);
        int panelX = (w - panelW) / 2, panelY = h - panelH - 40;
        Component interactionPanel = getComponentByName("interactionPanel");
        if (interactionPanel != null)
            interactionPanel.setBounds(panelX, panelY, panelW, panelH);
        nameLabel.setBounds(panelX + 50, panelY - 30, 220, 50);
        heartBar.setPreferredSize(new Dimension(300, 35));
        refreshImages();
        layeredPane.repaint();
    }

    private void refreshImages() {
        int w = layeredPane.getWidth(), h = layeredPane.getHeight();
        if (w <= 0 || h <= 0)
            return;
        bgLabel.setIcon(loadImage("src/main/resources/images/backgrounds/" + currentBG, w, h, false));
        int targetH = (int) (h * 0.85);
        ImageIcon charIcon = loadImage("src/main/resources/images/Characters/" + currentExpressionFile, -1, targetH,
                true);
        if (charIcon != null) {
            characterLabel.setIcon(charIcon);
            int newW = charIcon.getIconWidth(), newH = charIcon.getIconHeight();
            characterLabel.setBounds((w / 2) - (newW / 2), h - newH - 20, newW, newH);
        }
    }

    // ✅ เมธอดขอบคุณ: เปลี่ยนหน้า + คำพูด + ซ่อนปุ่ม 5 วิ (เฉพาะให้ของเท่านั้น)
    private void showThankYouReaction(String fileName, String message) {
        if (expressionTimer != null)
            expressionTimer.stop();

        isReactionActive = true;
        this.currentExpressionFile = fileName;

        btnA.setVisible(false);
        btnB.setVisible(false);
        btnC.setVisible(false);

        dialogLabel.setText("<html><body style='width: 450px; color: #FF1493; font-family: Tahoma; font-size: 18px;'>"
                + "<p style='line-height: 1.4;'>“" + message + "”</p></body></html>");
        refreshImages();

        expressionTimer = new Timer(5000, e -> {
            isReactionActive = false;
            btnA.setVisible(true);
            btnB.setVisible(true);
            btnC.setVisible(true);
            loadEvent(); // กลับสู่เนื้อเรื่องปกติ (คืนค่าสีหน้าตามด่านปัจจุบัน)
        });
        expressionTimer.setRepeats(false);
        expressionTimer.start();
    }

    private void nextStep(int pts) {
        if (isReactionActive)
            return;
        score += pts;
        updateScoreUI();

        // ✅ ไม่มีการใช้ Timer เปลี่ยนหน้าปกติ เพื่อให้ค้างหน้าตามเหตุการณ์
        manager.next();
        loadEvent();
    }

    private void loadEvent() {
        if (isReactionActive)
            return;
        if (manager.isFinished()) {
            finishGame();
            return;
        }

        MultiDatingEvent ev = manager.getCurrent();
        // ✅ บังคับค้างสีหน้าตามที่ Manager กำหนดในเหตุการณ์นั้นๆ
        this.currentExpressionFile = ev.getDefaultExpression();
        currentBG = ev.getBackground();

        // 👤 1. ดึงชื่อผู้เล่นจากระบบ (ถ้าไม่มีให้ใช้คำว่า "เธอ")
        String playerName = (client != null && client.getPlayerName() != null) ? client.getPlayerName() : "เธอ";

        // 🔄 2. แทนที่จุดไข่ปลา "..." ด้วยชื่อผู้เล่นจริง
        String processedDialog = ev.getDialog().replace("...", playerName);
        String processedA = ev.getChoiceA().replace("...", playerName);
        String processedB = ev.getChoiceB().replace("...", playerName);
        String processedC = ev.getChoiceC().replace("...", playerName);

        // 🏷️ ตั้งชื่อตัวละครหญิง (คัง ฮานา)
        nameLabel.setText("คัง ฮานา");

        // 💬 แสดงบทสนทนาที่ใส่ชื่อผู้เล่นแล้ว
        dialogLabel.setText("<html><body style='width: 450px; color: #222222; font-family: Tahoma; font-size: 18px;'>"
                + "<p style='line-height: 1.4;'>“" + processedDialog + "”</p></body></html>");

        // 🔘 แสดงปุ่มตัวเลือกที่ใส่ชื่อผู้เล่นแล้ว
        btnA.setText("<html><center>" + processedA + "</center></html>");
        btnB.setText("<html><center>" + processedB + "</center></html>");
        btnC.setText("<html><center>" + processedC + "</center></html>");

        relayoutUI();
    }

    private void updateScoreUI() {
        int displayScore = Math.min(score, 100);
        heartBar.setValue(displayScore);
        heartBar.setString(displayScore + " / 100");
    }

    private void setupActions() {
        btnA.addActionListener(e -> nextStep(manager.getCurrent().getScoreA()));
        btnB.addActionListener(e -> nextStep(manager.getCurrent().getScoreB()));
        btnC.addActionListener(e -> nextStep(manager.getCurrent().getScoreC()));

        btnShop.addActionListener(e -> new MultiDatingShop(this, money, (item, price) -> {
            if (money >= price) {
                money -= price;
                inventory.put(item, inventory.getOrDefault(item, 0) + 1);
                return true;
            }
            return false;
        }).setVisible(true));

        btnBag.addActionListener(e -> new MultiDatingInventory(this, inventory, (item) -> {
            int bonus = 0;
            String exp = "ผู้หญิง ตัวเอก.png";
            String thankMsg = "";
            switch (item) {
                case "ดอกไม้":
                case "ช็อกโกแลต":
                    bonus = 15;
                    exp = "ผู้หญิง เขิน.png";
                    thankMsg = "อุ๋ยยยยยย ขอบคุณน้าาา เธอน่ารักที่สุดเลยยย 😊😊";
                    break;
                case "กาแฟ":
                case "ทิวลิป":
                case "เค้กส้ม":
                    bonus = 10;
                    exp = "ผู้หญิง ยิ้ม.png";
                    thankMsg = "ขอบใจน้า ❤";
                    break;
                default:
                    bonus = 1;
                    exp = "ผู้หญิง ตัวเอก.png";
                    thankMsg = "อืมมม ขอบคุณ";
                    break;
            }
            score += bonus;
            updateScoreUI();
            showThankYouReaction(exp, thankMsg); // ✅ ล็อค 5 วินาที
        }));

        btnSound.addActionListener(e -> {
            sound.toggleMute();
            btnSound.setText(sound.isMuted() ? "🔇" : "🔊");
        });
        btnExit.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "ออกจากเกม?", "ยืนยัน", 0) == 0)
                exitAndDisconnect();
        });
    }

    private void exitAndDisconnect() {
        sound.stopBGM();
        if (timer != null)
            timer.stop();
        if (expressionTimer != null)
            expressionTimer.stop();
        if (client != null) {
            client.sendAction("LEAVE");
            client.disconnect();
        }
        dispose();
        SwingUtilities.invokeLater(() -> {
            if (controller != null)
                new MenuGame(controller).setVisible(true);
        });
    }

    private void finishGame() {
        if (client != null)
            client.sendAction("FINISH:" + score);
        Map<String, Integer> allScores = new HashMap<>();
        String myName = (client != null) ? client.getPlayerName() : "ผู้เล่น";
        allScores.put(myName, score);
        allScores.put("คู่แข่ง A", 70);
        allScores.put("คู่แข่ง B", 50);
        MultiDatingResultDialog.showResult(allScores, myName);
        exitAndDisconnect();
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
            File file = new File(path);
            Image img = (file.exists()) ? new ImageIcon(file.getAbsolutePath()).getImage() : null;
            if (img == null) {
                URL url = getClass().getResource(path.replace("src/main/resources", ""));
                if (url != null)
                    img = new ImageIcon(url).getImage();
            }
            if (img != null) {
                int finalW = targetW, finalH = targetH;
                if (keepRatio) {
                    double ratio = (double) img.getWidth(null) / img.getHeight(null);
                    if (targetW == -1)
                        finalW = (int) (targetH * ratio);
                    else if (targetH == -1)
                        finalH = (int) (targetW / ratio);
                }
                return new ImageIcon(img.getScaledInstance(finalW, finalH, Image.SCALE_SMOOTH));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private JButton createChoiceBtn() {
        JButton b = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed())
                    g2.setColor(new Color(255, 105, 180));
                else if (getModel().isRollover())
                    g2.setColor(new Color(255, 182, 193));
                else
                    g2.setColor(new Color(255, 240, 245));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(new Color(255, 20, 147));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        b.setForeground(new Color(255, 20, 147));
        b.setFont(thaiFont.deriveFont(Font.BOLD, 16f));
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(5, 10, 5, 10));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
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