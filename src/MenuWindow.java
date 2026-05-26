import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class MenuWindow extends JFrame {
    private MenuPanel menuPanel;

    public MenuWindow() {
        setTitle("Calorie Adventure - Welcome Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(800, 600);
        
        // Setup custom menu panel
        menuPanel = new MenuPanel(this);
        add(menuPanel);
        
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Start the game
    public void startGame() {
        GameWindow gameWindow = new GameWindow(this);
        gameWindow.setVisible(true);
        setVisible(false); // Hide the menu
    }

    // Direct transition to high scores tab with highlighting of the new score
    public void showHighScoresWithHighlight(String highlightName, int highlightScore) {
        menuPanel.activateHighScoresWithHighlight(highlightName, highlightScore);
        setVisible(true); // Ensure menu is visible
    }
    
    // Custom painted panel for the pixel-art start screen
    private static class MenuPanel extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener {
        private MenuWindow parent;
        private int selectedIndex = 0;
        private final String[] menuOptions = {"NEW GAME", "HIGH SCORES", "INSTRUCTIONS", "QUIT GAME"};
        
        // Overlay tabs state
        private boolean showingHighScores = false;
        private boolean showingInstructions = false;
        private List<String> highScores = new ArrayList<>();
        
        // Highlight states for showing player where they sit
        private String highlightName = null;
        private int highlightScore = -1;
        
        // Stars and background falling objects
        private List<MenuStar> stars = new ArrayList<>();
        private List<MenuFallingItem> fallingItems = new ArrayList<>();
        private Random rand = new Random();
        
        // Title pulsation & float anims
        private double floatPhase = 0;
        private double hueShift = 0;
        
        // Fonts
        private Font titleFont = new Font("Courier New", Font.BOLD, 52);
        private Font optionFont = new Font("Courier New", Font.BOLD, 26);
        private Font scoreFont = new Font("Courier New", Font.BOLD, 20);
        private Font footerFont = new Font("Courier New", Font.ITALIC, 14);

        public MenuPanel(MenuWindow parent) {
            this.parent = parent;
            setPreferredSize(new Dimension(800, 600));
            setBackground(new Color(15, 23, 42));
            setFocusable(true);
            requestFocusInWindow();
            
            addKeyListener(this);
            addMouseListener(this);
            addMouseMotionListener(this);

            // Populate backdrop stars
            for (int i = 0; i < 40; i++) {
                stars.add(new MenuStar(rand.nextInt(800), rand.nextInt(600), rand.nextFloat() * 1.5 + 0.5));
            }
            
            // Populate initial falling healthy/unhealthy background elements
            for (int i = 0; i < 8; i++) {
                fallingItems.add(new MenuFallingItem(rand.nextInt(800), rand.nextInt(600) - 600, rand.nextBoolean()));
            }

            // Load initial high scores
            loadHighScoresCache();

            // Animation Thread
            new Thread(this).start();
        }

        public void activateHighScoresWithHighlight(String name, int score) {
            this.highlightName = name;
            this.highlightScore = score;
            loadHighScoresCache();
            this.showingInstructions = false; // Make sure instructions are closed
            this.showingHighScores = true;
            repaint();
        }

        private void loadHighScoresCache() {
            highScores.clear();
            File file = new File("highscores.txt");
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        highScores.add(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            highScores.sort((s1, s2) -> {
                try {
                    int score1 = Integer.parseInt(s1.split(" - ")[1]);
                    int score2 = Integer.parseInt(s2.split(" - ")[1]);
                    return Integer.compare(score2, score1);
                } catch (Exception e) {
                    return 0;
                }
            });
            
            // Show top 12
            if (highScores.size() > 12) {
                int playerIndex = -1;
                if (highlightName != null) {
                    for (int i = 0; i < highScores.size(); i++) {
                        String[] parts = highScores.get(i).split(" - ");
                        if (parts[0].equals(highlightName) && Integer.parseInt(parts[1]) == highlightScore) {
                            playerIndex = i;
                            break;
                        }
                    }
                }
                
                if (playerIndex >= 12) {
                    List<String> sub = new ArrayList<>(highScores.subList(0, 11));
                    sub.add("... - 0");
                    sub.add(highScores.get(playerIndex) + " [Rank " + (playerIndex + 1) + "]");
                    highScores = sub;
                } else {
                    highScores = highScores.subList(0, 12);
                }
            }
        }

        @Override
        public void run() {
            while (true) {
                floatPhase += 0.06;
                hueShift += 0.005;
                if (hueShift > 1) hueShift = 0;

                for (MenuStar star : stars) {
                    star.y += star.speed * 0.5;
                    if (star.y > 600) {
                        star.y = 0;
                        star.x = rand.nextInt(800);
                    }
                }

                for (MenuFallingItem item : fallingItems) {
                    item.y += item.speed;
                    item.rot += item.rotSpeed;
                    if (item.y > 620) {
                        item.y = -50;
                        item.x = rand.nextInt(800);
                        item.isHealthy = rand.nextBoolean();
                        item.speed = rand.nextFloat() * 2 + 1;
                        item.color = getRandomColor(item.isHealthy);
                    }
                }

                repaint();

                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private Color getRandomColor(boolean healthy) {
            if (healthy) {
                Color[] colors = {new Color(74, 222, 128), new Color(249, 115, 22), new Color(234, 179, 8)};
                return colors[rand.nextInt(colors.length)];
            } else {
                Color[] colors = {new Color(239, 68, 68), new Color(244, 63, 94), new Color(236, 72, 153)};
                return colors[rand.nextInt(colors.length)];
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Stars
            g2d.setColor(new Color(255, 255, 255, 120));
            for (MenuStar star : stars) {
                g2d.fillRect((int) star.x, (int) star.y, (int) (star.speed * 2), (int) (star.speed * 2));
            }

            // Falling blocks
            for (MenuFallingItem item : fallingItems) {
                java.awt.geom.AffineTransform oldT = g2d.getTransform();
                g2d.translate(item.x, item.y);
                g2d.rotate(item.rot);
                g2d.setColor(new Color(item.color.getRed(), item.color.getGreen(), item.color.getBlue(), 80));
                g2d.fillRect(-15, -15, 30, 30);
                g2d.setTransform(oldT);
            }

            if (showingHighScores) {
                drawHighScoresOverlay(g2d);
            } else if (showingInstructions) {
                drawInstructionsOverlay(g2d);
            } else {
                drawMainMenu(g2d);
            }
        }

        private void drawMainMenu(Graphics2D g) {
            int titleY = 135 + (int) (Math.sin(floatPhase) * 8);

            g.setFont(titleFont);
            g.setColor(new Color(30, 27, 75));
            String title = "CALORIE ADVENTURE";
            int titleWidth = g.getFontMetrics().stringWidth(title);
            g.drawString(title, 400 - titleWidth / 2 + 4, titleY + 4);

            g.setColor(Color.getHSBColor((float) hueShift, 0.75f, 0.95f));
            g.drawString(title, 400 - titleWidth / 2, titleY);

            g.setFont(footerFont);
            g.setColor(new Color(148, 163, 184));
            String tag = "A retro retro-active food collector game!";
            int tagWidth = g.getFontMetrics().stringWidth(tag);
            g.drawString(tag, 400 - tagWidth / 2, titleY + 45);

            // Menu choices (4 options)
            g.setFont(optionFont);
            for (int i = 0; i < menuOptions.length; i++) {
                int optY = 290 + i * 60; // Clean layout vertical space
                boolean isSelected = (i == selectedIndex);

                if (isSelected) {
                    g.setColor(new Color(99, 102, 241));
                    g.drawString("> " + menuOptions[i] + " <", 400 - g.getFontMetrics().stringWidth("> " + menuOptions[i] + " <") / 2, optY);
                    
                    g.setColor(new Color(99, 102, 241, 100));
                    int w = g.getFontMetrics().stringWidth(menuOptions[i]) + 40;
                    g.fillRect(400 - w/2, optY + 12, w, 4);
                } else {
                    g.setColor(Color.WHITE);
                    g.drawString(menuOptions[i], 400 - g.getFontMetrics().stringWidth(menuOptions[i]) / 2, optY);
                }
            }

            g.setFont(footerFont);
            g.setColor(new Color(100, 116, 139));
            String tip = "[Use UP/DOWN arrows & ENTER to choose | Or use Mouse]";
            g.drawString(tip, 400 - g.getFontMetrics().stringWidth(tip) / 2, 545);
        }

        private void drawHighScoresOverlay(Graphics2D g) {
            g.setColor(new Color(30, 41, 59, 245));
            g.fillRect(100, 40, 600, 500);
            
            g.setColor(new Color(99, 102, 241));
            g.drawRect(100, 40, 600, 500);
            g.drawRect(104, 44, 592, 492);

            g.setFont(optionFont);
            g.setColor(new Color(253, 224, 71));
            String title = "★ HALL OF HEROES ★";
            g.drawString(title, 400 - g.getFontMetrics().stringWidth(title) / 2, 85);

            g.setFont(scoreFont);
            int startY = 135;
            
            if (highScores.isEmpty()) {
                g.setColor(new Color(148, 163, 184));
                String noScores = "No records yet! Go set a high score!";
                g.drawString(noScores, 400 - g.getFontMetrics().stringWidth(noScores) / 2, 280);
            } else {
                for (int i = 0; i < highScores.size(); i++) {
                    String scoreEntry = highScores.get(i);
                    String[] parts = scoreEntry.split(" - ");
                    String name = parts[0];
                    String val = parts.length > 1 ? parts[1] : "0";

                    boolean isHighlight = false;
                    String cleanName = name;
                    
                    if (name.contains(" [Rank")) {
                        cleanName = name.substring(0, name.indexOf(" [Rank"));
                    }

                    if (highlightName != null && cleanName.equals(highlightName)) {
                        try {
                            int entryScoreValue = Integer.parseInt(val.split(" ")[0]);
                            if (entryScoreValue == highlightScore) {
                                isHighlight = true;
                            }
                        } catch (Exception e) {}
                    }

                    if (name.equals("...")) {
                        g.setColor(new Color(100, 116, 139));
                        g.drawString("...", 400 - g.getFontMetrics().stringWidth("...") / 2, startY + i * 28);
                        continue;
                    }

                    String prefix = (i + 1) + ". ";
                    if (name.contains("[Rank")) {
                        prefix = "";
                    }

                    if (isHighlight) {
                        float flash = (float) (Math.sin(floatPhase * 2) * 0.15 + 0.85);
                        g.setColor(Color.getHSBColor(0.33f, 0.9f, flash));
                    } else if (i == 0 && prefix.length() > 0) {
                        g.setColor(new Color(253, 224, 71));
                    } else if (i == 1 && prefix.length() > 0) {
                        g.setColor(new Color(226, 232, 240));
                    } else if (i == 2 && prefix.length() > 0) {
                        g.setColor(new Color(249, 115, 22));
                    } else {
                        g.setColor(Color.WHITE);
                    }

                    g.drawString(prefix + name, 160, startY + i * 28);
                    g.drawString(val, 520, startY + i * 28);

                    if (isHighlight) {
                        g.setFont(footerFont);
                        g.drawString("◄ YOU!", 580, startY + i * 28 - 2);
                        g.setFont(scoreFont);
                    }
                }
            }

            g.setFont(footerFont);
            g.setColor(new Color(239, 68, 68));
            String backTip = "[Click anywhere or press ESC to return to Menu]";
            g.drawString(backTip, 400 - g.getFontMetrics().stringWidth(backTip) / 2, 510);
        }

        private void drawInstructionsOverlay(Graphics2D g) {
            g.setColor(new Color(30, 41, 59, 245));
            g.fillRect(100, 40, 600, 500);
            
            g.setColor(new Color(99, 102, 241));
            g.drawRect(100, 40, 600, 500);
            g.drawRect(104, 44, 592, 492);

            g.setFont(optionFont);
            g.setColor(new Color(253, 224, 71));
            String title = "★ HOW TO PLAY ★";
            g.drawString(title, 400 - g.getFontMetrics().stringWidth(title) / 2, 85);

            g.setFont(scoreFont);
            int yStart = 140;
            int lineSpacing = 32;
            
            g.setColor(new Color(99, 102, 241));
            g.drawString("CONTROLS:", 140, yStart);
            g.setColor(Color.WHITE);
            g.drawString("• Press A / D or ← / → to slide smoothly.", 140, yStart + lineSpacing);
            
            g.setColor(new Color(74, 222, 128));
            g.drawString("GOAL & HEALTHY FOODS:", 140, yStart + 3 * lineSpacing);
            g.setColor(Color.WHITE);
            g.drawString("• Catch Healthy foods (🍎 🥦 🥕 🍌) to increase Score.", 140, yStart + 4 * lineSpacing);

            g.setColor(new Color(239, 68, 68));
            g.drawString("LIVES & JUNK FOODS:", 140, yStart + 6 * lineSpacing);
            g.setColor(Color.WHITE);
            g.drawString("• You start with 3 Lives. Hitting Junk costs a Life!", 140, yStart + 7 * lineSpacing);

            g.setColor(new Color(253, 224, 71));
            g.drawString("POWER-UP (DUMBBELL):", 140, yStart + 9 * lineSpacing);
            g.setColor(Color.WHITE);
            g.drawString("• Grants points (+50), doubles incoming scores,", 140, yStart + 10 * lineSpacing);
            g.drawString("  and gives Junk Food IMMUNITY for 15 seconds!", 140, yStart + 11 * lineSpacing);

            g.setFont(footerFont);
            g.setColor(new Color(239, 68, 68));
            String backTip = "[Click anywhere or press ESC to return to Menu]";
            g.drawString(backTip, 400 - g.getFontMetrics().stringWidth(backTip) / 2, 510);
        }

        private void handleSelection() {
            if (showingHighScores) {
                showingHighScores = false;
                highlightName = null;
                highlightScore = -1;
                return;
            }
            if (showingInstructions) {
                showingInstructions = false;
                return;
            }

            switch (selectedIndex) {
                case 0: // NEW GAME
                    parent.startGame();
                    break;
                case 1: // HIGH SCORES
                    loadHighScoresCache();
                    showingHighScores = true;
                    break;
                case 2: // INSTRUCTIONS
                    showingInstructions = true;
                    break;
                case 3: // QUIT GAME
                    System.exit(0);
                    break;
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (showingHighScores || showingInstructions) {
                if (key == KeyEvent.VK_ESCAPE || key == KeyEvent.VK_ENTER || key == KeyEvent.VK_SPACE) {
                    showingHighScores = false;
                    showingInstructions = false;
                    highlightName = null;
                    highlightScore = -1;
                    repaint();
                }
                return;
            }

            if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
                selectedIndex = (selectedIndex - 1 + menuOptions.length) % menuOptions.length;
                repaint();
            } else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
                selectedIndex = (selectedIndex + 1) % menuOptions.length;
                repaint();
            } else if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_SPACE) {
                handleSelection();
            }
        }

        @Override public void keyReleased(KeyEvent e) {}
        @Override public void keyTyped(KeyEvent e) {}

        @Override
        public void mouseClicked(MouseEvent e) {
            if (showingHighScores || showingInstructions) {
                showingHighScores = false;
                showingInstructions = false;
                highlightName = null;
                highlightScore = -1;
                repaint();
                return;
            }

            int mx = e.getX();
            int my = e.getY();
            
            for (int i = 0; i < menuOptions.length; i++) {
                int optY = 290 + i * 60;
                int optHeight = 35;
                if (mx > 250 && mx < 550 && my > optY - optHeight && my < optY + 10) {
                    selectedIndex = i;
                    handleSelection();
                    break;
                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (showingHighScores || showingInstructions) return;

            int mx = e.getX();
            int my = e.getY();

            for (int i = 0; i < menuOptions.length; i++) {
                int optY = 290 + i * 60;
                int optHeight = 35;
                if (mx > 250 && mx < 550 && my > optY - optHeight && my < optY + 10) {
                    if (selectedIndex != i) {
                        selectedIndex = i;
                        repaint();
                    }
                }
            }
        }

        @Override public void mousePressed(MouseEvent e) {}
        @Override public void mouseReleased(MouseEvent e) {}
        @Override public void mouseEntered(MouseEvent e) {}
        @Override public void mouseExited(MouseEvent e) {}
        @Override public void mouseDragged(MouseEvent e) {}
    }

    private static class MenuStar {
        double x, y;
        double speed;

        MenuStar(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
        }
    }

    private static class MenuFallingItem {
        double x, y;
        double speed;
        double rot;
        double rotSpeed;
        boolean isHealthy;
        Color color;

        MenuFallingItem(double x, double y, boolean isHealthy) {
            this.x = x;
            this.y = y;
            this.isHealthy = isHealthy;
            this.speed = new Random().nextFloat() * 2 + 1;
            this.rot = new Random().nextDouble() * Math.PI;
            this.rotSpeed = new Random().nextFloat() * 0.04 - 0.02;
            
            Random r = new Random();
            if (isHealthy) {
                Color[] colors = {new Color(74, 222, 128), new Color(249, 115, 22), new Color(234, 179, 8)};
                this.color = colors[r.nextInt(colors.length)];
            } else {
                Color[] colors = {new Color(239, 68, 68), new Color(244, 63, 94), new Color(236, 72, 153)};
                this.color = colors[r.nextInt(colors.length)];
            }
        }
    }
}
