import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int boardWidth;
    int boardHeight;
    int tileSize = 25;
    int score;

    Tile snakeHead;
    Tile food;
    ArrayList<Tile> snakeBody;

    Random random;
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;

    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardHeight;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();
        food = new Tile( 10, 10);
        random = new Random();
        gameLoop = new Timer(100, this);
        gameLoop.start();
        velocityX = 0;
        velocityY = 1;
        score = 0;
        placeFood();
    }

    public boolean collision(Tile t1, Tile t2) {
        if ((t1.x == t2.x) && (t1.y == t2.y)) return true;
        return false;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        //grid
        /* 
        for (int i = 0; i < boardWidth/tileSize; i++) {
            g.drawLine(0, i*tileSize, boardWidth, i*tileSize);
            g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
        } 
        */

        g.setColor(Color.RED);
        g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);

        g.setColor(Color.GREEN);
        g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);

        for (int i = 0; i < snakeBody.size(); i++) {
            Tile t = snakeBody.get(i);
            g.fillRect(t.x * tileSize, t.y * tileSize, tileSize, tileSize);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Score: " + score, 10, 20);

        if (gameOver == true) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN,50));
            g.drawString("GAME OVER", boardWidth/2 - 220, boardHeight/2 - 50);
            g.drawString("Click enter to restart", boardWidth/2 - 220, boardHeight/2);
        }
    }

    public void placeFood() {
        food.x = random.nextInt(boardWidth/tileSize) ;
        food.y = random.nextInt(boardHeight/tileSize);
    }

    public void move() {
        //collision with food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            score++;
            placeFood();
        }
        
        //moving the parts of snake's body
        for (int j = snakeBody.size() - 1; j >= 0; j--) {
            Tile t = snakeBody.get(j);
            if (j == 0) {
                t.x = snakeHead.x;
                t.y = snakeHead.y;
            } else {
                Tile t_next = snakeBody.get(j-1);
                t.x = t_next.x;
                t.y = t_next.y;

            }
        }

        //moving the head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

         //checking the game over conditions
         for (int i = 0; i < snakeBody.size(); i++) {
            Tile t = snakeBody.get(i);

            if (collision(t, snakeHead)){
                gameOver = true;
            }
        }

        if ((snakeHead.x < 0) 
        || (snakeHead.x > boardWidth / tileSize) 
        || (snakeHead.y < 0) 
        || (snakeHead.y > boardHeight / tileSize)) {
            gameOver = true;
        }
     }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        move();
        repaint();

        if (gameOver) {
            gameLoop.stop();
        }
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (gameOver && key == KeyEvent.VK_ENTER) {
            restartGame();
            return;
        }

        switch( key ) {
            case KeyEvent.VK_UP:
            if (velocityY != 1) {
                velocityX = 0;
                velocityY = -1;
            }
                break;
            case KeyEvent.VK_DOWN:
            if (velocityY != -1) {
                velocityX = 0;
                velocityY = 1;
            }
                break;
            case KeyEvent.VK_RIGHT:
            if (velocityX != -1) {
                velocityX = 1;
                velocityY = 0;
            }
                break;
            default:
            if (velocityX != 1) {
                velocityX = -1;
                velocityY = 0;
            }
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
    }

    public void restartGame() {
        snakeHead = new Tile(5, 5);
        snakeBody.clear();
        velocityX = 0;
        velocityY = 1;
        score = 0;
        placeFood();
        gameOver = false;
        gameLoop.start();
    }
    
}
