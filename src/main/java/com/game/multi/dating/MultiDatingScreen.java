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
            sound.playBGM("audiotest2.wav");
        } catch (Exception e) {
            System.err.println("Sound error");
        }

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                relayoutUI();
            }
        });

        timer = new MultiDatingTimer(600, timerLabel, this::finishGame);
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
            switch (item) {
                case "ดอกไม้":
                case "ช็อกโกแลต":
                    bonus = 15;
                    exp = "ผู้หญิง เขิน.png";
                    msg = "ขอบคุณน้าาาา 😊";
                    break;
                case "กาแฟ":
                case "ทิวลิป":
                    bonus = 10;
                    exp = "ผู้หญิง ยิ้ม.png";
                    msg = "ขอบใจน้า ❤";
                    break;
                default:
                    bonus = 1;
                    msg = "อืม ขอบคุณ";
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

        // ✅ 1. เอาแถบหัวออกเพื่อให้ลากขยับไม่ได้ และล็อก Resizable
        settings.setUndecorated(true);
        settings.setResizable(false);
        settings.setSize(380, 420);
        settings.setLocationRelativeTo(this); // Flex ไว้ตรงกลางหน้าจอเกม

        // ✅ 2. ดักจับการปิดหน้าต่างผ่านกากบาท (ถ้ามีแถบ) ให้ไม่ทำอะไร
        settings.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        JPanel content = new JPanel(new GridLayout(4, 1, 10, 15));
        content.setBackground(new Color(255, 245, 250));

        // ✅ 3. เพิ่มเส้นขอบหนาๆ แทนแถบ Windows ที่หายไป
        content.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 20, 147), 3),
                new EmptyBorder(30, 50, 30, 50)));
        settings.setContentPane(content);

        Font forceFont = new Font("Tahoma", Font.BOLD, 16);
        JLabel lbTitle = new JLabel("เมนูตั้งค่า", SwingConstants.CENTER);
        lbTitle.setFont(new Font("Tahoma", Font.BOLD, 22));
        lbTitle.setForeground(new Color(255, 20, 147));

        JButton btnResume = new JButton("กลับไปเล่นต่อ");
        JButton btnMute = new JButton(sound.isMuted() ? "เปิดเสียงเพลง" : "ปิดเสียงเพลง");
        JButton btnLeave = new JButton("ออกจากห้องแข่ง");
        btnLeave.setForeground(Color.RED);

        JButton[] allBtns = { btnResume, btnMute, btnLeave };
        for (JButton b : allBtns) {
            b.setFont(forceFont);
            b.setFocusPainted(false);
            b.setBackground(Color.WHITE);
        }

        btnResume.addActionListener(e -> settings.dispose());
        btnMute.addActionListener(e -> {
            sound.toggleMute();
            btnMute.setText(sound.isMuted() ? "เปิดเสียงเพลง" : "ปิดเสียงเพลง");
        });
        btnLeave.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(settings, "ออกจากเกมใช่หรือไม่?", "ยืนยัน", 0) == 0) {
                settings.dispose();
                exitAndGoToMain();
            }
        });

        content.add(lbTitle);
        content.add(btnResume);
        content.add(btnMute);
        content.add(btnLeave);
        settings.setVisible(true);
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

    private void finishGame() {
        if (client != null)
            client.sendAction("FINISH:" + score);
        Map<String, Integer> res = new HashMap<>();
        String n = (client != null) ? client.getPlayerName() : "Player";
        res.put(n, score);
        MultiDatingResultDialog.showResult(res, n);
        exitAndGoToMain();
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

        // ✅ แก้ไข: เปลี่ยน %name% เป็นชื่อผู้เล่นจริง
        String pName = (client != null && client.getPlayerName() != null) ? client.getPlayerName() : "ผู้เล่น";
        String dialog = ev.getDialog().replace("%name%", pName).replace("...", pName);

        dialogLabel.setText("<html><body style='width: 450px; color: #222222; font-family: Tahoma; font-size: 18px;'>“"
                + dialog + "”</body></html>");

        btnA.setText("<html><center>" + ev.getChoiceA().replace("%name%", pName) + "</center></html>");
        btnB.setText("<html><center>" + ev.getChoiceB().replace("%name%", pName) + "</center></html>");
        btnC.setText("<html><center>" + ev.getChoiceC().replace("%name%", pName) + "</center></html>");
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
        heartBar.setUI(new BasicProgressBarUI() {
            @Override
            protected void paintDeterminate(Graphics g, JComponent c) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = c.getWidth(), h = c.getHeight();
                int mw = (int) (w * heartBar.getPercentComplete());
                g2d.setColor(new Color(255, 255, 255, 150));
                g2d.fillRoundRect(0, 0, w, h, 15, 15);
                g2d.setPaint(new GradientPaint(0, 0, new Color(255, 20, 147), mw, 0, new Color(255, 105, 180)));
                g2d.fillRoundRect(0, 0, mw, h, 15, 15);
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
        nameLabel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true),
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