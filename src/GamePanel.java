import javax.swing.*;
import java.util.Random;
import javax.swing.JPanel;

import java.awt.event.*;
import java.awt.FontMetrics;
import java.awt.*;


public class GamePanel extends JPanel implements ActionListener {


    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts =  6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'D';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){
        
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
       
    }

    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
        requestFocusInWindow();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);

    }

    public void draw(Graphics g){
        if (running){
            // draws a grid
            // for(int i=0; i<SCREEN_HEIGHT/UNIT_SIZE;i++){
            //     g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
            //     g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
            // }
            // draws an apple
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // draws the snake
            for (int i=0; i<bodyParts; i++){
                if(i==0){
                    // the head
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    // the body
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
        } else {
            gameOver(g);
        }
    }

    public void newApple(){
        // creates random coordinates
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
        
    }

    public void move(){
        for(int i = bodyParts; i>0;i--){
            y[i] = y[i-1];
            x[i] = x[i-1];
        }

        switch (direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
        }
    }

    

    public void checkApple(){
        // checks if apple is eaten and increases bodysize
        if (x[0]==appleX && y[0] == appleY){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions(){
        // checks if head collids with body
        for (int i = bodyParts; i>0; i--){
            if(x[0]==x[i] && y[0]==y[i]){
                running = false;
            }
        }
        // checks if head touches left border
        if(x[0] < 0){
            running = false;
        }
        // checks if head touches right border
        if(x[0] > SCREEN_WIDTH){
            running = false;
        }
        // checks if head touches bottom border
        if(y[0] > SCREEN_HEIGHT){
            running = false;
        }
        // checks if head touches bottom border
        if(y[0] < 0){
            running = false;
        }

        if (!running){
            timer.stop();
        }
        
    }

    public void gameOver(Graphics g){
        //Game Over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
        
    }


    @Override
    public void actionPerformed(ActionEvent e) {
       if(running){
        move();
        checkApple();
        checkCollisions();
       }
       repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
            case KeyEvent.VK_LEFT:
                if(direction != 'R'){
                    direction = 'L';
                }
                break;

            case KeyEvent.VK_RIGHT:
                if(direction != 'L'){
                    direction = 'R';
                }
                break;

            case KeyEvent.VK_UP:
                if(direction != 'D'){
                    direction = 'U';
                }
                break;

            case KeyEvent.VK_DOWN:
                if(direction != 'U'){
                    direction = 'D';
                }
                break;
            }
            
        }
    }
    
}
