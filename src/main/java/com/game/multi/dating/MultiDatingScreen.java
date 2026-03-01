package com.game.multi.dating;

import com.game.controllers.GameController;
import com.game.network.GameClient;
import com.game.ui.MenuGame; // ✅ มั่นใจว่า Import ตัวนี้มาแล้ว
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
    private GameController controller; // ✅ เพิ่มตัวแปรเก็บ Controller

    private JLayeredPane layeredPane = new JLayeredPane();
    private JLabel bgLabel = new JLabel();
    private JLabel characterLabel = new JLabel();

    private JProgressBar heartBar = new JProgressBar(0, 100);
    private JLabel nameLabel = new JLabel();
    private JLabel dialogLabel = new JLabel();
    private JLabel timerLabel = new JLabel();
    private JLabel moneyLabel = new JLabel();

    private JButton btnA, btnB, btnC;
    private JButton btnBag, btnShop, btnSound, btnExit;
    private MultiDatingTimer timer;

    private String currentBG = "";
    private final Font thaiFont = new Font("Tahoma", Font.BOLD, 18);

    // ✅ ปรับ Constructor ให้รับ 2 ค่า (Client และ Controller)
    public MultiDatingScreen(GameClient client, GameController controller) {
        this.client = client;
        this.controller = controller; // ✅ เก็บไว้ส่งต่อให้ MenuGame

        btnA = createChoiceBtn();
        btnB = createChoiceBtn();
        btnC = createChoiceBtn();
        btnBag = createIconBtn("🎒");
        btnShop = createIconBtn("🛒");
        btnSound = createIconBtn("🔊");
        btnExit = createIconBtn("❌");

        setTitle("💕 ศึกชิงนาง Online - Competition");
        setSize(1000, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setContentPane(layeredPane);
        styleComponents();
        setupLayout();
        setupActions();
        loadEvent();

        sound.stopBGM();
        sound.playBGM("audiotest2.wav");

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                relayoutUI();
            }
        });

        timer = new MultiDatingTimer(60, timerLabel, this::finishGame);
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
                g2d.setColor(new Color(255, 230, 235));
                g2d.fillRoundRect(0, 0, w, h, 15, 15);
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 105, 180), mappedW, 0,
                        new Color(255, 182, 193));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, mappedW, h, 15, 15);
            }
        });
        heartBar.setFont(thaiFont.deriveFont(14f));
        heartBar.setStringPainted(true);

        // ✅ แก้ไขพื้นหลังชื่อให้ดูพรีเมียม ขอบชมพูเข้ม
        nameLabel.setOpaque(true);
        nameLabel.setBackground(new Color(255, 255, 255, 245));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setBorder(BorderFactory.createLineBorder(new Color(255, 20, 147), 3, true));
    }

    private void setupLayout() {
        bgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        layeredPane.add(bgLabel, Integer.valueOf(0));

        characterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        layeredPane.add(characterLabel, Integer.valueOf(100));

        JPanel topUI = new JPanel(new BorderLayout());
        topUI.setOpaque(false);
        topUI.setName("topBar");

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setOpaque(false);
        leftPanel.add(heartBar);
        leftPanel.add(moneyLabel);
        leftPanel.add(btnBag);
        leftPanel.add(btnShop);
        leftPanel.add(btnSound);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(timerLabel);
        rightPanel.add(btnExit);

        topUI.add(leftPanel, BorderLayout.WEST);
        topUI.add(rightPanel, BorderLayout.EAST);
        layeredPane.add(topUI, Integer.valueOf(200));

        JPanel dialogBox = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 215));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2d.setColor(new Color(255, 182, 193));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
            }
        };
        dialogBox.setOpaque(false);
        dialogBox.setName("dialogBox");
        dialogLabel.setBorder(new EmptyBorder(20, 30, 20, 30));
        dialogBox.add(dialogLabel, BorderLayout.CENTER);

        JPanel choicePanel = new JPanel(new GridLayout(3, 1, 8, 8));
        choicePanel.setOpaque(false);
        choicePanel.add(btnA);
        choicePanel.add(btnB);
        choicePanel.add(btnC);
        dialogBox.add(choicePanel, BorderLayout.EAST);

        layeredPane.add(nameLabel, Integer.valueOf(300));
        layeredPane.add(dialogBox, Integer.valueOf(300));
    }

    private void setupActions() {
        btnA.addActionListener(e -> nextStep(manager.getCurrent().getScoreA()));
        btnB.addActionListener(e -> nextStep(manager.getCurrent().getScoreB()));
        btnC.addActionListener(e -> nextStep(manager.getCurrent().getScoreC()));

        btnSound.addActionListener(e -> {
            sound.toggleMute();
            btnSound.setText(sound.isMuted() ? "🔇" : "🔊");
        });

        btnExit.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this,
                    "คุณต้องการออกจากเกมและกลับสู่หน้าหลักใช่หรือไม่?",
                    "ยืนยันการออก", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                exitAndDisconnect();
            }
        });

        btnBag.addActionListener(e -> new MultiDatingInventory(this, inventory, (item) -> {
            score += 20;
            updateScoreUI();
        }).setVisible(true));

        btnShop.addActionListener(e -> new MultiDatingShop(this, money, (item, price) -> {
            if (money >= price) {
                money -= price;
                inventory.put(item, inventory.getOrDefault(item, 0) + 1);
                loadEvent();
                return true;
            }
            return false;
        }).setVisible(true));
    }

    private void exitAndDisconnect() {
        sound.stopBGM();
        if (timer != null)
            timer.stop();
        if (client != null) {
            client.sendAction("LEAVE");
            client.disconnect();
        }
        dispose();

        // ✅ แก้บัค NPE: ส่งตัวแปร controller ที่เก็บไว้เข้าไปแทน null
        SwingUtilities.invokeLater(() -> {
            if (controller != null) {
                new MenuGame(controller).setVisible(true);
            } else {
                System.err.println("❌ Error: Controller is null, cannot return to Menu");
            }
        });
    }

    private void relayoutUI() {
        int w = layeredPane.getWidth(), h = layeredPane.getHeight();
        if (w <= 0 || h <= 0)
            return;

        bgLabel.setBounds(0, 0, w, h);
        int charW = (int) (w * 0.45), charH = (int) (h * 0.85);
        characterLabel.setBounds((w - charW) / 2, h - charH - 20, charW, charH);

        Component topBar = getComponentByName("topBar");
        if (topBar != null)
            topBar.setBounds(15, 15, w - 30, 60);

        int boxH = 180;
        Component dialogBox = getComponentByName("dialogBox");
        if (dialogBox != null)
            dialogBox.setBounds(30, h - boxH - 40, w - 60, boxH);
        nameLabel.setBounds(70, h - boxH - 95, 220, 48);

        refreshImages();
        layeredPane.repaint();
    }

    private void refreshImages() {
        int w = bgLabel.getWidth(), h = bgLabel.getHeight();
        if (w <= 0 || h <= 0)
            return;
        bgLabel.setIcon(loadImage("src/main/resources/images/backgrounds/" + currentBG, w, h));
        int cw = characterLabel.getWidth(), ch = characterLabel.getHeight();
        if (cw > 0 && ch > 0) {
            characterLabel.setIcon(loadImage("src/main/resources/images/Characters/ผู้หญิง ตัวเอก.png", cw, ch));
        }
    }

    private ImageIcon loadImage(String path, int w, int h) {
        try {
            File file = new File(path);
            Image img = null;
            if (file.exists()) {
                img = new ImageIcon(file.getAbsolutePath()).getImage();
            } else {
                String resPath = path.replace("src/main/resources", "");
                URL url = getClass().getResource(resPath);
                if (url != null)
                    img = new ImageIcon(url).getImage();
            }
            if (img != null)
                return new ImageIcon(img.getScaledInstance(w, h, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void loadEvent() {
        if (manager.isFinished()) {
            finishGame();
            return;
        }
        MultiDatingEvent ev = manager.getCurrent();
        currentBG = ev.getBackground();
        moneyLabel.setText("💰 " + money);
        moneyLabel.setFont(thaiFont.deriveFont(22f));
        moneyLabel.setForeground(new Color(255, 140, 0));

        // ✅ แก้ไขชื่อตัวละคร: สีชมพูเข้ม พร้อมเงาขาวให้อ่านง่าย
        nameLabel.setText("<html><body style='padding: 5px; font-family:Tahoma; color:#FF1493;'>"
                + "<span style='text-shadow: 1px 1px 2px #FFFFFF;'>คิม แจฮยอน</span></body></html>");

        dialogLabel.setText(
                "<html><body style='width: 450px; font-family:Tahoma;'>“" + ev.getDialog() + "”</body></html>");

        btnA.setText("<html><body style='font-family:Tahoma;'>" + ev.getChoiceA() + "</body></html>");
        btnB.setText("<html><body style='font-family:Tahoma;'>" + ev.getChoiceB() + "</body></html>");
        btnC.setText("<html><body style='font-family:Tahoma;'>" + ev.getChoiceC() + "</body></html>");

        relayoutUI();
    }

    private void updateScoreUI() {
        heartBar.setValue(Math.min(score, 100));
        heartBar.setString("ความประทับใจ: " + score + "%");
    }

    private void nextStep(int pts) {
        score += pts;
        updateScoreUI();
        manager.next();
        loadEvent();
    }

    private Component getComponentByName(String name) {
        for (Component c : layeredPane.getComponents()) {
            if (name.equals(c.getName()))
                return c;
        }
        return null;
    }

    private JButton createChoiceBtn() {
        JButton b = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        b.setBackground(new Color(255, 255, 255, 230));
        b.setFont(thaiFont.deriveFont(Font.PLAIN, 15f));
        b.setContentAreaFilled(false);
        b.setBorder(BorderFactory.createLineBorder(new Color(255, 182, 193), 2, true));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JButton createIconBtn(String icon) {
        JButton b = new JButton(icon) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 200));
                g2.fillOval(5, 5, getWidth() - 10, getHeight() - 10);
                g2.setColor(new Color(255, 182, 193));
                g2.drawOval(5, 5, getWidth() - 10, getHeight() - 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void finishGame() {
        if (client != null)
            client.sendAction("FINISH:" + score);
        MultiDatingResultDialog.showResult(score);
        exitAndDisconnect();
    }
}