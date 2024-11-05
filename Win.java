import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Win extends JPanel {
    private JFrame frame;
    private Image backgroundImage;
    private Image restartButtonImage;
    private Image exitButtonImage;

    public Win(JFrame frame) {
        this.frame = frame;
        setPreferredSize(new Dimension(800, 390));
        setLayout(null);

        backgroundImage = new ImageIcon("win.jpg").getImage();

        restartButtonImage = new ImageIcon("restart01.png").getImage(); 
        exitButtonImage = new ImageIcon("no01.png").getImage(); 

        // Create Restart button
        JButton restartButton = new JButton(new ImageIcon(restartButtonImage));
        restartButton.setBounds(300, 200, 200, 50); 
        restartButton.setContentAreaFilled(false);
        restartButton.setBorderPainted(false);
        restartButton.setFocusPainted(false);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });

        // Create Exit button
        JButton exitButton = new JButton(new ImageIcon(exitButtonImage));
        exitButton.setBounds(300, 270, 200, 50); 
        exitButton.setContentAreaFilled(false);
        exitButton.setBorderPainted(false);
        exitButton.setFocusPainted(false); 
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); 
            }
        });

       
        add(restartButton);
        add(exitButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        
        g.drawImage(backgroundImage, 0, 0, 800, 390, this);
    }

    private void restartGame() {
        frame.getContentPane().removeAll(); 
        LostinWonderland gamePanel = new LostinWonderland(); 
        frame.add(gamePanel); 
        frame.revalidate(); 
        frame.repaint(); 
        gamePanel.requestFocusInWindow();
    }
}