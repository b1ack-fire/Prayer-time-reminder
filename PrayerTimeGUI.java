package prayertime;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

public class PrayerTimeGUI extends JFrame {

    // --- Components ---
    private JLabel timeLabel, dateLabel;
    private JTextArea displayArea;
    private JPanel headerPanel, buttonPanel; // Panels we need to repaint
    private ThemeButton themeSwitch;
    private AnimationPanel mainPanel; // Custom Panel for the Wave
    
    // --- State ---
    private boolean isDarkMode = false;
    private int hadithIndex = 0;
    private ArrayList<JButton> menuButtons = new ArrayList<>(); // To update colors easily

    // --- Data ---
    private final String[] hadithList = {
        "\"The strong man is not the one who can overpower the tiger, but the one who can control himself when he is angry.\"\n(Sahih Al-Bukhari)",
        "\"Take advantage of five before five: Your youth before your old age, your health before your illness, your richness before your poverty, your free time before your work, and your life before your death.\"",
        "\"The best of you are those who have the best of manners and character.\"\n(Sahih Al-Bukhari)",
        "\"A good word is charity.\"\n(Sahih Al-Bukhari & Muslim)",
        "\"God does not look at your forms and possessions, but He looks at your hearts and your deeds.\""
    };

    public PrayerTimeGUI() {
        setTitle("Prayer Time Companion");
        setSize(500, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 1. Setup the Animation Background Panel
        mainPanel = new AnimationPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        setContentPane(mainPanel);

        setupHeader();
        setupDisplayScreen();
        setupControlGrid();

        startLiveClock();
        showRandomHadith();
        
        // Apply initial colors
        updateTextColors();
    }

    // --- GUI Setup Methods ---

    private void setupHeader() {
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(30, 30, 10, 30));

        // Left: Clock
        JPanel clockPanel = new JPanel(new GridLayout(2, 1));
        clockPanel.setOpaque(false);
        
        timeLabel = new JLabel("00:00 AM");
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 44));
        
        dateLabel = new JLabel("Loading date...");
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        clockPanel.add(timeLabel);
        clockPanel.add(dateLabel);

        // Right: Theme Switch
        JPanel switchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        switchPanel.setOpaque(false);
        themeSwitch = new ThemeButton();
        
        // TRIGGER ANIMATION ON CLICK
        themeSwitch.addActionListener(e -> {
            // 1. Calculate center of the button relative to the frame
            Point p = SwingUtilities.convertPoint(themeSwitch, 
                        themeSwitch.getWidth()/2, themeSwitch.getHeight()/2, mainPanel);
            
            // 2. Start the Wave
            mainPanel.startWaveAnimation(p.x, p.y);
            
            // 3. Toggle Logic
            isDarkMode = !isDarkMode;
            themeSwitch.setDark(isDarkMode);
            
            // 4. Update Text Colors
            updateTextColors();
        });
        
        switchPanel.add(themeSwitch);

        headerPanel.add(clockPanel, BorderLayout.CENTER);
        headerPanel.add(switchPanel, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
    }

    private void setupDisplayScreen() {
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.setBorder(new EmptyBorder(10, 30, 20, 30));

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        displayArea.setLineWrap(true);
        displayArea.setWrapStyleWord(true);
        displayArea.setOpaque(false);
        displayArea.setMargin(new Insets(15, 15, 15, 15));

        // Glass Card Background
        JPanel glassPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Card Color: Dark semi-transparent in Light Mode (for contrast), Lighter in Dark Mode
                Color cardColor = isDarkMode ? new Color(255, 255, 255, 30) : new Color(0, 0, 0, 30);
                g2.setColor(cardColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
            }
        };
        glassPanel.setOpaque(false);
        glassPanel.add(displayArea);

        centerWrapper.add(glassPanel, BorderLayout.CENTER);
        mainPanel.add(centerWrapper, BorderLayout.CENTER);
    }

    private void setupControlGrid() {
        buttonPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(0, 30, 40, 30));

        menuButtons.add(createModernButton("📅  Today's Plan", e -> showToday()));
        menuButtons.add(createModernButton("🕌  Friday / Jumu'ah", e -> showFriday()));
        menuButtons.add(createModernButton("⏱  Current Status", e -> checkStatus()));
        menuButtons.add(createModernButton("📖  Read Hadith", e -> showNextHadith()));
        menuButtons.add(createModernButton("⚙️  Overloading", e -> demoOverload()));
        menuButtons.add(createModernButton("📑  Copy Data", e -> demoCopy()));

        for (JButton btn : menuButtons) {
            buttonPanel.add(btn);
        }
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    // --- Helper: Update Text Colors based on Mode ---
    private void updateTextColors() {
        // Define Colors
        Color mainText = isDarkMode ? Color.WHITE : new Color(33, 33, 33); // White vs Dark Grey
        Color subText  = isDarkMode ? new Color(220, 220, 220) : new Color(60, 60, 60);
        
        // Apply
        timeLabel.setForeground(mainText);
        dateLabel.setForeground(subText);
        displayArea.setForeground(mainText); // Text inside the card
        
        // Update Buttons
        for (JButton btn : menuButtons) {
            btn.setForeground(mainText);
        }
        
        // Force repaint of glass panel to update background darkness
        mainPanel.repaint();
    }

    // --- Logic Methods ---
    private void showToday() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d"));
        displayArea.setText("📅  Daily Prayer Schedule\n" + date + "\n\n" +
                "Fajr       04:45 AM  -  06:01 AM\n" +
                "Dhuhr    11:58 AM  -  04:14 PM\n" +
                "Asr        04:15 PM  -  05:30 PM\n" +
                "Maghrib 05:33 PM  -  06:03 PM\n" +
                "Isha       06:45 PM  -  11:30 PM");
    }
    private void showFriday() {
        displayArea.setText("🕌  Jumu'ah Special Schedule\n\n" +
                "Khutbah Starts:   12:45 PM\n" +
                "Congregation:     01:30 PM\n\n" +
                "Replaces Dhuhr on Fridays.");
    }
    private void checkStatus() {
        LocalTime now = LocalTime.now();
        int min = now.getHour() * 60 + now.getMinute();
        String status = (min >= 285 && min < 360) ? "Time for Fajr" : 
                        (min >= 718 && min < 975) ? "Time for Dhuhr" :
                        (min >= 975 && min < 1050) ? "Time for Asr" :
                        (min >= 1055 && min < 1125) ? "Time for Maghrib" :
                        (min >= 1125) ? "Time for Isha" : "No specific prayer time.";
        displayArea.setText("⏱  Current Status\n\n" + status);
    }
    private void showNextHadith() {
        hadithIndex = (hadithIndex + 1) % hadithList.length;
        displayArea.setText("📖  Hadith\n\n" + hadithList[hadithIndex]);
    }
    private void showRandomHadith() {
        hadithIndex = new Random().nextInt(hadithList.length);
        displayArea.setText("✨  Thought of the Moment\n\n" + hadithList[hadithIndex]);
    }
    private void demoOverload() { displayArea.setText("⚙️ Overloading Demo:\nSystem handles multiple 'setReminder' commands."); }
    private void demoCopy() { displayArea.setText("📑 Copy Constructor:\nCreated a safe backup of today's schedule."); }

    private void startLiveClock() {
        new Timer(1000, e -> {
            LocalDateTime now = LocalDateTime.now();
            timeLabel.setText(now.format(DateTimeFormatter.ofPattern("h:mm a")));
            dateLabel.setText(now.format(DateTimeFormatter.ofPattern("EEEE, MMMM d")));
        }).start();
    }

    // --- CUSTOM UI COMPONENTS ---

    private JButton createModernButton(String text, ActionListener action) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Button background: Darker in light mode for visibility
                if (getModel().isRollover()) {
                    g2.setColor(isDarkMode ? new Color(255, 255, 255, 50) : new Color(0, 0, 0, 30));
                } else {
                    g2.setColor(isDarkMode ? new Color(255, 255, 255, 20) : new Color(0, 0, 0, 10));
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);
        return btn;
    }

    // --- THE MAGIC ANIMATION PANEL ---
    // This handles the "Wave" effect
    class AnimationPanel extends JPanel {
        private float radius = 0;
        private float maxRadius = 0;
        private int centerX, centerY;
        private boolean isAnimating = false;
        private Timer animTimer;

        public AnimationPanel() {
            // 60 FPS Timer
            animTimer = new Timer(16, e -> {
                radius += 40; // Speed of wave
                if (radius > maxRadius) {
                    isAnimating = false;
                    animTimer.stop();
                }
                repaint();
            });
        }

        public void startWaveAnimation(int x, int y) {
            this.centerX = x;
            this.centerY = y;
            this.radius = 0;
            // Calculate max distance to corner for full coverage
            this.maxRadius = (float) Math.sqrt(getWidth()*getWidth() + getHeight()*getHeight());
            this.isAnimating = true;
            animTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            
            int w = getWidth();
            int h = getHeight();

            // Define the Two Gradients
            // Light Mode: Light Blue -> Teal
            GradientPaint lightGP = new GradientPaint(0, 0, new Color(60, 140, 230), 0, h, new Color(60, 200, 170));
            // Dark Mode: Deep Midnight Blue -> Black
            GradientPaint darkGP = new GradientPaint(0, 0, new Color(20, 30, 48), 0, h, new Color(36, 59, 85));

            if (isAnimating) {
                // 1. Draw the "Old" background (If we are switching TO dark, old is Light)
                g2d.setPaint(isDarkMode ? lightGP : darkGP);
                g2d.fillRect(0, 0, w, h);

                // 2. Create the Wave Circle (The "New" background)
                Area screen = new Area(new Rectangle(0, 0, w, h));
                Ellipse2D.Float circle = new Ellipse2D.Float(centerX - radius, centerY - radius, radius * 2, radius * 2);
                
                // 3. Clip to the circle and paint "New" background
                g2d.setClip(circle);
                g2d.setPaint(isDarkMode ? darkGP : lightGP);
                g2d.fillRect(0, 0, w, h);
            } else {
                // Static State (Not animating)
                g2d.setPaint(isDarkMode ? darkGP : lightGP);
                g2d.fillRect(0, 0, w, h);
            }
        }
    }

    // --- THEME BUTTON (Sun/Moon) ---
    class ThemeButton extends JButton {
        private boolean isDark = false;
        public ThemeButton() {
            setPreferredSize(new Dimension(50, 50));
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        public void setDark(boolean d) { isDark = d; repaint(); }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Background Circle
            g2.setColor(isDark ? new Color(255, 255, 255, 30) : new Color(0, 0, 0, 20));
            g2.fillOval(0, 0, getWidth(), getHeight());

            if (isDark) {
                // Moon
                g2.setColor(new Color(255, 223, 0)); // Gold
                g2.fillOval(12, 12, 26, 26);
                g2.setColor(new Color(30, 30, 30)); // Shadow for crescent
                g2.fillOval(8, 8, 20, 20);
            } else {
                // Sun
                g2.setColor(Color.ORANGE);
                g2.fillOval(12, 12, 26, 26);
                g2.setColor(new Color(255, 200, 0)); // Rays/Detail
                g2.drawOval(12, 12, 26, 26);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PrayerTimeGUI().setVisible(true));
    }
}