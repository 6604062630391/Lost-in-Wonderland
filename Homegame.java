import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Homegame extends JPanel {
    private JFrame frame;
    private Image backgroundImage;

    public Homegame(JFrame frame) {
        this.frame = frame;
        setPreferredSize(new Dimension(800, 390));
        setLayout(null); 

       
        backgroundImage = new ImageIcon("Home.jpg").getImage();

       
        JButton startButton = new JButton(new ImageIcon("play01.png"));
        startButton.setBounds(300, 200, 200, 80); 
        startButton.setBorderPainted(false); 
        startButton.setContentAreaFilled(false); 
        startButton.setFocusPainted(false); 

        // Create Exit button with icon
        JButton exitButton = new JButton(new ImageIcon("no01.png"));
        exitButton.setBounds(300, 270, 200, 80); 
        exitButton.setBorderPainted(false); 
        exitButton.setContentAreaFilled(false); 
        exitButton.setFocusPainted(false);

        // Add action listeners
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); 
            }
        });

        
        add(startButton);
        add(exitButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
       
        g.drawImage(backgroundImage, 0, 0, 800, 390, this);
    }

    private void startGame() {
        frame.getContentPane().removeAll(); 
        LostinWonderland gamePanel = new LostinWonderland(); 
        frame.add(gamePanel);
        frame.revalidate();
        frame.repaint();
        gamePanel.requestFocusInWindow();
    }
}
