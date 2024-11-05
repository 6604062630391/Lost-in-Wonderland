import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class LostinWonderland extends JPanel implements ActionListener {

    private Timer timer;
    private int playerX = 50;
    private int playerY = 210;
    private int velocityY = 0;
    private boolean onGround = true;
    private int hp = 10;  // HP เริ่มต้นที่ 10
    private int currentGhostIndex = -1; 

    private boolean ghostEncounter = false;
    private String ghostMessage = "";
    private String userInput = "";

    private final int GRAVITY = 1;
    private final int JUMP_STRENGTH = -15;
    private final int GAME_WIDTH = 2000;
    private final int PLAYER_WIDTH = 50;
    private final int PLAYER_HEIGHT = 60;

    private Image[] playerImage;
    private int currentImageIndex = 0;
    private boolean isMoving = false;
    private int imageChangeCounter = 0;
    private Image ghostImage;
    private Image backgroundImage;
    private Image layerImage;
    private Image groundImage;
    private Image rbImage;
    private Image coinImage;
    private Image heartImage;
    private Image keyImage;
    private Image doorImage;

    private ArrayList<Point> coins = new ArrayList<>();
    private ArrayList<Boolean> coinCollected = new ArrayList<>();
    private int coinsCollected = 0;

    private ArrayList<Point> ghosts = new ArrayList<>();
    private ArrayList<String> ghostWords = new ArrayList<>();
    private ArrayList<Boolean> ghostClearedList = new ArrayList<>();

    private int currentLevel = 1;
    private final int MAX_LEVELS = 2;
    private boolean levelComplete = false;

    private boolean isFacingRight = true;
    private int cameraX = 0;

    private final String[] words = {"Apple", "Mango", "Absorb", "Defeat", "Equip"};

    private boolean hasKey = false;
    private Point keyPosition;
    private Point doorPosition;

    
    public LostinWonderland() {
        setFocusable(true);
        setPreferredSize(new Dimension(800, 390));
        addKeyListener(new TAdapter());
        timer = new Timer(10, this);
        timer.start();

        for (int i = 0; i < 10; i++) {
            int x = 100 + i * 150; 
            int y = (i % 3 == 0) ? 210 : 150;
            coins.add(new Point(x, y));
            coinCollected.add(false);
        }

        for (int i = 0; i < 2; i++) {
            ghosts.add(new Point(460 + i * 600, 200));  
            ghostWords.add(words[i]);
            ghostClearedList.add(false);
        }

        
        playerImage = new Image[]{
            new ImageIcon("player.png").getImage(),
            new ImageIcon("player2.png").getImage(),
            new ImageIcon("player3.png").getImage()
        };
        ghostImage = new ImageIcon("ghost.png").getImage();
        backgroundImage = new ImageIcon("back.png").getImage();
        layerImage = new ImageIcon("layer.png").getImage();
        groundImage = new ImageIcon("ground.png").getImage();
        rbImage = new ImageIcon("ribbon.png").getImage();
        coinImage = new ImageIcon("coin.png").getImage();
        heartImage = new ImageIcon("heart.png").getImage();
        keyImage = new ImageIcon("key.png").getImage();
        doorImage = new ImageIcon("door.png").getImage();
    }



    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        g.setColor(new Color(34, 34, 34));
        g.fillRect(0, 0, getWidth(), getHeight());

        int startX = -cameraX % 160;
        for (int x = startX; x < getWidth(); x += 160) {
            g.drawImage(backgroundImage, x, 0, this);
        }

        startX = -cameraX % 384;
        for (int x = startX; x < getWidth(); x += 384) {
            g.drawImage(layerImage, x, 0, this);
        }

        startX = -cameraX % 140;
        for (int x = startX; x < getWidth(); x += 140) {
            g.drawImage(groundImage, x, 250, this);
        }

        // Draw player
        if (isFacingRight) {
            g.drawImage(playerImage[currentImageIndex], playerX - cameraX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT, this);
        } else {
            g.drawImage(playerImage[currentImageIndex], playerX - cameraX + PLAYER_WIDTH, playerY, -PLAYER_WIDTH, PLAYER_HEIGHT, this);
        }

        Font customFont = new Font("Serif", Font.BOLD, 20);
        g.setFont(customFont);

        // coins
        for (int i = 0; i < coins.size(); i++) {
            if (!coinCollected.get(i)) {
                Point coin = coins.get(i);
                g.drawImage(coinImage, coin.x - cameraX, coin.y, 30, 30, this);
            }
            
        }

        // ghosts
        for (int i = 0; i < ghosts.size(); i++) {
            if (!ghostClearedList.get(i)) {
                Point ghost = ghosts.get(i);
                g.drawImage(ghostImage, ghost.x - cameraX, ghost.y, 50, 60, this);
            }
        }


        if (currentLevel == 2) {
            if (!hasKey) {
                g.drawImage(keyImage, keyPosition.x - cameraX, 250, 30, 19, this);
            }
            g.drawImage(doorImage, doorPosition.x - cameraX, 185, 60, 90, this);
        }

        g.drawImage(heartImage,10,340,30,27,this );
        g.setColor(Color.WHITE);
        g.drawString("HP: " + hp, 50, 360);

        if (currentLevel == 1) {
            g.drawString("Coins x " + coinsCollected, getWidth() - 100, 30);
        } else if (currentLevel == 2) {
            g.drawImage(keyImage,getWidth() - 180,14,30,19,this );
            g.drawString((hasKey ? "Collected !" : "Not Collected"), getWidth() - 140, 30);
        }
        if (ghostEncounter) {
            g.drawImage(rbImage,320,50,164,60,this );
            g.setColor(Color.BLACK);
            g.drawString(ghostMessage, 373, 92);
            g.setColor(Color.WHITE);
            g.drawString("> " + userInput, 320, 360);
        }
    }

    private void checkUserInput() {
        if (userInput.equals(ghostMessage)) {
            ghostClearedList.set(currentGhostIndex, true);
            ghostEncounter = false;
            userInput = "";
        } else {
            hp--;  
            if (hp <= 0) {
                endGame();
            }
        }
    }
    private void endGame() {
        timer.stop();
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        topFrame.getContentPane().removeAll();
        Gameover gameOverPanel = new Gameover(topFrame);
        topFrame.add(gameOverPanel);
        topFrame.revalidate();
        topFrame.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        velocityY += GRAVITY;
        playerY += velocityY;
        if (playerY >= 210) {
            playerY = 210;
            velocityY = 0;
            onGround = true;
        } else {
            onGround = false;
        }

        cameraX = Math.max(0, Math.min(GAME_WIDTH - getWidth(), playerX - getWidth() / 2));
        if (isMoving) {
            imageChangeCounter++;
            if (imageChangeCounter % 10 == 0) { 
                currentImageIndex = (currentImageIndex + 1) % playerImage.length; 
            }
        }
        if (currentLevel == 1) {
            for (int i = 0; i < coins.size(); i++) {
                if (!coinCollected.get(i)) {
                    Point coin = coins.get(i);
                    if (playerX + PLAYER_WIDTH > coin.x && playerX < coin.x + 30 && playerY + PLAYER_HEIGHT > coin.y && playerY < coin.y + 30) {
                        coinCollected.set(i, true);
                        coinsCollected++;
                    }
                }
            }
        }

     
        ghostEncounter = false;
        for (int i = 0; i < ghosts.size(); i++) {
            if (!ghostClearedList.get(i)) {
                Point ghost = ghosts.get(i);
                if (playerX + PLAYER_WIDTH > ghost.x && playerX < ghost.x + 50 && playerY + PLAYER_HEIGHT > ghost.y && playerY < ghost.y + 60) {
                    ghostEncounter = true;
                    ghostMessage = ghostWords.get(i);
                    if (userInput.equalsIgnoreCase(ghostMessage)) {
                        ghostClearedList.set(i, true);
                        ghostEncounter = false;
                        userInput = "";
                    }
                    break;
                }
            }
        }

        if (coinsCollected == coins.size() && currentLevel == 1) {
            levelComplete = true;
            currentLevel++;
            loadLevel(currentLevel);
        }

        if (currentLevel == 2 && !hasKey) {
            if (playerX + PLAYER_WIDTH > keyPosition.x && playerX < keyPosition.x + 30
                && playerY + PLAYER_HEIGHT > keyPosition.y && playerY < keyPosition.y + 30) {
                hasKey = true;
            }
        }

        if (currentLevel == 2 && hasKey) {
            if (playerX + PLAYER_WIDTH > doorPosition.x && playerX < doorPosition.x + 60
                && playerY + PLAYER_HEIGHT > doorPosition.y && playerY < doorPosition.y + 90) {
                showGameComplete();
            }
        }
        
        repaint();
    }

    private void loadLevel(int level) {
        playerX = 50;
        playerY = 210;
        velocityY = 0;
        onGround = true;
        ghostEncounter = false;
        userInput = "";
        coinsCollected = 0;
        levelComplete = false;
    
        coins.clear();
        coinCollected.clear();
    
        ghosts.clear();
        ghostWords.clear();
        ghostClearedList.clear();
    
        switch (level) {
            case 1:
                backgroundImage = new ImageIcon("back.png").getImage();
                layerImage = new ImageIcon("layer.png").getImage();

                for (int i = 0; i < 10; i++) {
                    coins.add(new Point(100 + i * 150, 210));
                    coinCollected.add(false);
                }

                ghosts.add(new Point(500, 200));
                ghostWords.add(words[0]);
                ghostClearedList.add(false);

                ghosts.add(new Point(1100, 200));
                ghostWords.add(words[1]);
                ghostClearedList.add(false);

                break;
    
            case 2:
                backgroundImage = new ImageIcon("back2.png").getImage();
                layerImage = new ImageIcon("layer2.png").getImage();
                
                ghosts.add(new Point(500, 200));
                ghostWords.add(words[2]); 
                ghostClearedList.add(false);
    
                ghosts.add(new Point(780, 200));
                ghostWords.add(words[3]); 
                ghostClearedList.add(false);
    
                ghosts.add(new Point(1380, 200));
                ghostWords.add(words[4]);
                ghostClearedList.add(false);

                keyPosition = new Point(1450, 210);
                doorPosition = new Point(1900, 210);

                break;
        }
    
        repaint();
    }
    

    private void showGameComplete() {
        timer.stop();
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.getContentPane().removeAll(); 
        Win winPanel = new Win(frame);
        frame.add(winPanel); 
        frame.revalidate(); 
        frame.repaint();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_SPACE && onGround && !ghostEncounter) {
                velocityY = JUMP_STRENGTH;
            }
            if (key == KeyEvent.VK_RIGHT && !ghostEncounter) {
                playerX += 5;
                isFacingRight = true;
                isMoving = true;
            } else if (key == KeyEvent.VK_LEFT && !ghostEncounter) {
                playerX -= 5;
                isFacingRight = false;
                isMoving = true;
            }
            
        }
        @Override
        public void keyReleased(KeyEvent e) {
            isMoving = false; 
        }

        public void keyTyped(KeyEvent e) {
            if (ghostEncounter) {
                char c = e.getKeyChar();
                if (Character.isLetter(c)) {
                    userInput += c;
                    if (userInput.length() > ghostMessage.length() || !ghostMessage.startsWith(userInput)) {
                        checkUserInput();
                    } else if (userInput.equals(ghostMessage)) {
                        checkUserInput();
                    }
                }
                if (e.getKeyChar() == '\b' && userInput.length() > 0) {
                    userInput = userInput.substring(0, userInput.length() - 1);
                }
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Lost in Wonderland");
        Homegame homeScreen = new Homegame(frame);
        frame.add(homeScreen);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
} 