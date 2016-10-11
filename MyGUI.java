package pong;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JComponent;
import javax.swing.JFrame;

/*
- Some part of the game that talks about the controls and stuff like that
- The bouncy sound gets really irritating when the ball rolls
- Countdown to start each round
- Point to each player after each round (Point to Player 1!)
- Paddles start in the middle after each score
- Ball goes a little past the paddle when you lose
- Make screen wider
- Roll around the bottom less
- Want the ball to be able to get to the top quadrants after explosion
- Explosions are too consistently on the floor. Ball rolls around wayyy too much
- Single player: Allow the player to moveup or movedown slowly
- Single player: The bot can't reach the very bottom of the screen
*/

public class MyGUI extends JFrame implements KeyListener {
    //Make singleton at some point
    GameGraphics myG = null;
    Container jc;
    private int players;
    private AudioInputStream bounce;
    private static Clip myClip;
    private static ArrayList<MyPaddle> pad;
    private static ArrayList<Ball> bal;
    private static int gameover;
    MyGUI(int players) {
        gameover = 0;
        try {
            bounce = AudioSystem.getAudioInputStream(new File("C:\\Users\\Roman\\Documents\\NetBeansProjects\\Pong\\src\\pong\\paFinal.wav"));
            myClip = AudioSystem.getClip();
            myClip.open(bounce);
        }
        catch (Exception e) {;}
        this.players = players;
        this.setTitle("Pong on Crack");
        this.setSize(500,500);
        jc = this.getContentPane();
        jc.setBackground(Color.WHITE);
        pad = new ArrayList<>();
        bal = new ArrayList<>();
        for (int i = 1;i<=players;i++) pad.add(new MyPaddle(i));
        bal.add(new Ball(1));
        myG = new GameGraphics(pad,bal);
        this.setResizable(false);
        this.add(myG);
        this.setFocusable(true);
        this.addKeyListener(this);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); ////////////////////////////////
    }
    public static void makeCrack() {
        bal.add(new Ball(2));
    }
    public static void bounce() {
        myClip.start();
        myClip.setFramePosition(0);
    }
    public static void setGameover(int gameover) {
        MyGUI.gameover = gameover;
    }
    public static ArrayList<MyPaddle> getPad() {
        return pad;
    }
    public static ArrayList<Ball> getBal() {
        return bal;
    }
    public void start() {
        this.setVisible(true);
        while (gameover == 0) myG.repaint();
    }
    public void keyTyped(KeyEvent e) {
    }
    public void keyPressed(KeyEvent e) {
        if (gameover == 0) {
            if (e.getKeyChar() == 'W' || e.getKeyChar() == 'w') pad.get(0).moveUp(); //get whichever player you are
            else if (e.getKeyChar() == 'S' || e.getKeyChar() == 's') pad.get(0).moveDown();
            else if (e.getKeyCode() == 38) {
                if (MainMenu.isMult()) pad.get(1).moveUp();
                else if (MainMenu.isSingle()) pad.get(0).moveUp();
            }
            else if (e.getKeyCode() == 40) {
                if (MainMenu.isMult()) pad.get(1).moveDown();
                else if (MainMenu.isSingle()) pad.get(0).moveDown();
            }
        }
        else;
    }
    public void keyReleased(KeyEvent e) {
    }
}       
        
class MyPaddle {
    private String playerName;
    private JComponent myGame;
    private Integer score;
    private int playerNo,myPaddlex1,myPaddlex2,myPaddley1,myPaddley2,move;
    MyPaddle(int playerNo) {
        this.playerNo = playerNo;
        move = 50;
        if (playerNo == 1) {
            score = new Integer(0);
            myPaddlex1 = 10;
            myPaddlex2 = 10;
            myPaddley1 = 175;
            myPaddley2 = 100;
        }
        else if (playerNo == 2) {
            score = new Integer(0);
            myPaddlex1 = 460;
            myPaddlex2 = 10;
            myPaddley1 = 175;
            myPaddley2 = 100;
        }
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    public String getPlayerName() {
        return playerName;
    }
    public void incScore() {
        score++;
    }
    public void reset() {
        if (playerNo == 1) {
            myPaddlex1 = 10;
            myPaddlex2 = 10;
            myPaddley1 = 175;
            myPaddley2 = 100;
        }
        else if (playerNo == 2) {
            myPaddlex1 = 460;
            myPaddlex2 = 10;
            myPaddley1 = 175;
            myPaddley2 = 100;
        }
    }
    public String getScore() {
        return score.toString();
    }
    public int x1() {
        return myPaddlex1;
    }
    public int x2() {
        return myPaddlex2;
    }
    public int y1() {
        return myPaddley1;
    }
    public int y2() {
        return myPaddley2;
    }
    public void setGame(JComponent myGame) {
        this.myGame = myGame;
    }
    public void moveDown() {
        if (myPaddley1 < 350) {
            myPaddley1 += move;
            myGame.repaint();
        }
        else;
    }
    public void moveUp() {
        if (myPaddley1 > 10) {
            myPaddley1 -= move;
            myGame.repaint();
        }
        else;
    }
    //Zohar's Code
    public void moveUpSlow() {
        if (myPaddley1 > 10) {
            myPaddley1 -= 5;
            myGame.repaint();
        }
        else;
    }
    public void moveDownSlow() {
        if (myPaddley1 < 350) {
            myPaddley1 += 5;
            myGame.repaint();
        }
        else;
    }
    //Zohar's Code
}

class Ball {
    private Random rn;
    private JComponent myGame;
// I added variables here that you need to declare… pretty much everything after gravity was added  
// by me just now, but make sure ALL of these variables are declared with their initialization. 
//Also theres added private booleans, dont forget em
    private int ballNo=1,centerx,centery,radius,velocityx,velocityy, speedindicator,gravity, speedingsteps, speedslower=1,speedfaster=1,explosion=1,fuse=100;
private int frictiondrag = 1;

    private boolean forwait = false, friction = false; 
    public static int opspeed = 15;
    Ball(int ballNo) {
        rn = new Random();
        this.ballNo = ballNo;
        radius = 15;
        reset();
    }
    public int getBallNo() {
        return ballNo;
    }
    public int getCenterx() {
        return centerx;
    }
    public int getCentery() {
        return centery;
    }
    public int getRadius() {
        return radius;
    }
    public void setVelx(int velocityx) {
        this.velocityx = velocityx;
    }
    public void setVely(int velocityy) {
        this.velocityy = velocityy;
    }
    public static int [] solveIntQuad(double a,double b,double c) {
        //Solve quadratic to the nearest integer
        int [] response = new int[2];
        response[0] = (int) Math.floor((-b + Math.sqrt(Math.pow(b,2) - 4*a*c))/(2*a));
        response[1] = (int) Math.floor((-b - Math.sqrt(Math.pow(b,2) - 4*a*c))/(2*a));
        return response;
    }
    public static void collide(Ball b1,Ball b2) {
        int v1x = b1.getVelx();
        int v1y = b1.getVely();
        int v2x = b2.getVelx();
        int v2y = b2.getVely();
        b1.setVelx(v2x);
        b1.setVely(v2y);
        b2.setVelx(v1x);
        b2.setVely(v1y);
    }
    public int centerx() {
        if (centerx > 17 && centerx < 447) {
            centerx -= velocityx;
            //Zohar's Code
            //artifish intel
            if (MainMenu.isSingle()) {
                if (centery < MyGUI.getPad().get(1).y1() + 55) MyGUI.getPad().get(1).moveUpSlow();
                else if (centery > MyGUI.getPad().get(1).y1() + 55) MyGUI.getPad().get(1).moveDownSlow();
            }
            //Zohar's Code
            if (MainMenu.isClassic()) {
                speedindicator ++;
                if (speedindicator%500 == 0 && opspeed !=1) opspeed--;
            }
            else if (MainMenu.isBaked()) {
                gravity++;
                if (gravity%10 == 0) velocityy++;
                if (velocityy == 0) explosion++;
                if (explosion % fuse == 0) {
                   explode();
                   explosion = 1;
                   fuse = rn.nextInt(100) + 300;
               }
               if (Math.abs(velocityx) < 6){
                   speedfaster++;
                   if (speedfaster%20 == 0)
                   {
                   if (velocityx >= 0) velocityx++;
                   else if (velocityx<0) velocityx--;
                   }
               }
               if (Math.abs(velocityx) > 6) {
                   speedslower++;
               
               if (speedslower%50 == 0)
               {
                   if (velocityx >= 0) velocityx--;
               else if (velocityx < 0) velocityx++;
                }
               }
            }
            if (forwait) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {;}
                forwait = false;
            }
        }
        
        else {
            MyGUI.bounce();
            velocityx *= -1;
            if (centerx <= 17) {
                centerx = 18;
                //Zohar's Code
                //controls the collision projectile direction... man i sound smart
                if (MainMenu.isClassic()) {
                    if (centery > MyGUI.getPad().get(0).y1() - 10 && centery <= MyGUI.getPad().get(0).y1() + 10) velocityy -= 4;
                    if (centery > MyGUI.getPad().get(0).y1() + 10 && centery <= MyGUI.getPad().get(0).y1() + 30) velocityy -= 2;
                    if (centery > MyGUI.getPad().get(0).y1() + 60 && centery <= MyGUI.getPad().get(0).y1() + 80) velocityy += 2;
                    if (centery > MyGUI.getPad().get(0).y1() + 80 && centery <= MyGUI.getPad().get(0).y1() + 100) velocityy += 4;
                }
                //Zohar's Code
                if (((centery < MyGUI.getPad().get(0).y1() - 10)  || (centery > MyGUI.getPad().get(0).y1() + 100))) {
                    MyGUI.getPad().get(1).incScore();
                    for (Ball b : MyGUI.getBal()) b.reset();
                    opspeed = 15;
                   try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {;}
                   forwait = true;
                }
            }
            else if (centerx >= 447) {
                centerx = 446;
                //Zohar's Code
                 //controls the collision projectile direction... man i sound smart again
                if (MainMenu.isClassic()) {
                    if (centery > MyGUI.getPad().get(1).y1() - 10 && centery <= MyGUI.getPad().get(1).y1() + 10) velocityy -= 4;
                    if (centery > MyGUI.getPad().get(1).y1() + 10 && centery <= MyGUI.getPad().get(1).y1() + 30) velocityy -= 2;
                    if (centery > MyGUI.getPad().get(1).y1() + 60 && centery <= MyGUI.getPad().get(1).y1() + 80) velocityy += 2;
                    if (centery > MyGUI.getPad().get(1).y1() + 80 && centery <= MyGUI.getPad().get(1).y1() + 100) velocityy += 4;
                }
                //Zohar's Code
                if ((centery < MyGUI.getPad().get(1).y1() - 10) || (centery > MyGUI.getPad().get(1).y1() + 100)) {
                    MyGUI.getPad().get(0).incScore();
                    for (Ball b : MyGUI.getBal()) b.reset();
                    opspeed = 15;
                   try {
                       Thread.sleep(500);
                   } catch (InterruptedException ex) {;}
                    forwait = true;
                }
            }
        }
        if (MainMenu.isCrack() && this.ballNo == 1) {
            Ball otherBall;
            int gcx,gcy,gcr;
            otherBall = MyGUI.getBal().get(1);
            gcy = otherBall.getCentery();
            gcx = otherBall.getCenterx();
            gcr = otherBall.getRadius();
            if (2*gcr >= 2*Math.sqrt(Math.pow(centerx - gcx,2) + Math.pow(centery - gcy,2))) collide(this,otherBall);
        }
        return centerx;
    }
    public int centery() {
        if (centery > 0 && centery < 450)
            centery += velocityy;
        else {
            velocityy = -(velocityy-1);
            if (centery <= 0) centery = 1;
            else if (centery >= 450) centery = 449;
        }
        return centery;
    }
    public int radius() {
        return radius;
    }
    public void setGame(JComponent myGame) {
        this.myGame = myGame;
    }
public void explode() {
    try {
        Thread.sleep(250);
    } catch (InterruptedException ex) {;}
    velocityx = 10; velocityy = (-1)*rn.nextInt(12 - 0 + 1) + 0;
}
public int getVelx() {
    return velocityx;
}
public int getVely() {
    return velocityy;
}
    public void reset() {
            centerx = 225;
            velocityx = (4+(int)rn.nextGaussian()*2);
            switch (this.ballNo) {
                case 1:
                    if (MyGUI.getBal().size() == 2) centery = 195;
                    else centery = 225;
                    velocityx *= (int)Math.pow(-1,rn.nextInt(2));
                    break;
                case 2:
                    centery = 255;
                    velocityx *= (-1)*Math.signum(MyGUI.getBal().get(0).getVelx());
                    break;
                default:break;
            }
            velocityy = (4+(int)rn.nextGaussian()*2)*(int)Math.pow(-1,rn.nextInt(2));
            if (Math.pow(velocityx,2) <= velocityy || velocityx == 0 || velocityy == 0) reset();
        }
    }

class GameGraphics extends JComponent {
    private String score1,score2;
    ArrayList<MyPaddle> paddles;
    ArrayList<Ball> balls;
    GameGraphics(ArrayList<MyPaddle> paddles,ArrayList<Ball> balls) {
        this.paddles = paddles;
        this.balls = balls;
        for (MyPaddle p : paddles) {
            p.setGame(this);
        }
    }
    public void paint(Graphics g) {
        int cx,cy,r;
        for (MyPaddle p : paddles) g.fillRect(p.x1(),p.y1(),p.x2(),p.y2());
        try {
            Thread.sleep(Ball.opspeed);
        } catch (InterruptedException ex) {;}
        for (Ball b : balls) {
            cx = b.centerx();
            cy = b.centery();
            r = b.radius();
            switch (b.getBallNo()) {
                case 1:
                    g.setColor(Color.RED);
                    break;
                case 2:
                    g.setColor(Color.BLUE);
                    break;
                default:break;
            }
            g.fillOval(cx,cy,r,r);
        }
        score1 = MyGUI.getPad().get(0).getScore();
        score2 = MyGUI.getPad().get(1).getScore();
        g.setColor(Color.GREEN);
        g.drawString("Player 1: " + MyGUI.getPad().get(0).getScore(),70,10);
        g.drawString("Player 2: " + MyGUI.getPad().get(1).getScore(),350,10);
        if (score1.equals("10")) {
            MyGUI.setGameover(1);
            g.drawString("Player 1 wins!!!",190,265);
        }
        else if (score2.equals("10")) {
            MyGUI.setGameover(2);
            g.drawString("Player 2 wins!!!",190,265);
        }
    }
}

/*
package pong;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class MyGUI extends JFrame implements KeyListener {
    //Make singleton at some point
    GameGraphics myG = null;
    Container jc;
    private int players;
    private AudioInputStream bounce;
    private static Clip myClip;
    private static ArrayList<MyPaddle> pad;
    private static ArrayList<Ball> bal;
    private static int gameover;
    MyGUI(int players) {
        gameover = 0;
        try {
            bounce = AudioSystem.getAudioInputStream(new File("C:\\Users\\Roman\\Documents\\NetBeansProjects\\Pong\\src\\pong\\paFinal.wav"));
            myClip = AudioSystem.getClip();
            myClip.open(bounce);
        }
        catch (Exception e) {;}
        this.players = players;
        this.setTitle("Pong on Crack");
        this.setSize(500,500);
        jc = this.getContentPane();
        jc.setBackground(Color.WHITE);
        pad = new ArrayList<>();
        bal = new ArrayList<>();
        for (int i = 1;i<=players;i++) pad.add(new MyPaddle(i));
        bal.add(new Ball(1));
        myG = new GameGraphics(pad,bal);
        this.setResizable(false);
        this.add(myG);
        this.setFocusable(true);
        this.addKeyListener(this);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); ////////////////////////////////
    }
    public static void bounce() {
        myClip.start();
        myClip.setFramePosition(0);
    }
    public static void setGameover(int gameover) {
        MyGUI.gameover = gameover;
    }
    public static ArrayList<MyPaddle> getPad() {
        return pad;
    }
    public static ArrayList<Ball> getBal() {
        return bal;
    }
    public void start() {
        this.setVisible(true);
        while (gameover == 0) myG.repaint();
    }
    public void keyTyped(KeyEvent e) {
    }
    public void keyPressed(KeyEvent e) {
        if (gameover == 0) {
            if (e.getKeyChar() == 'W' || e.getKeyChar() == 'w') pad.get(0).moveUp(); //get whichever player you are
            else if (e.getKeyChar() == 'S' || e.getKeyChar() == 's') pad.get(0).moveDown();
            else if (e.getKeyCode() == 38) {
                try {
                    pad.get(1).moveUp();
                }
                catch (Exception exc) {
                    pad.get(0).moveUp();
                }
            }
            else if (e.getKeyCode() == 40) {
                try {
                    pad.get(1).moveDown();
                }
                catch (Exception exc) {
                    pad.get(0).moveDown();
                }
            }
        }
        else;
    }
    public void keyReleased(KeyEvent e) {
    }
}       
        
class MyPaddle {
    private String playerName;
    private JComponent myGame;
    private Integer score;
    private int playerNo,myPaddlex1,myPaddlex2,myPaddley1,myPaddley2,move;
    MyPaddle(int playerNo) {
        this.playerNo = playerNo;
        move = 50;
        if (playerNo == 1) {
            score = new Integer(0);
            myPaddlex1 = 10;
            myPaddlex2 = 10;
            myPaddley1 = 175;
            myPaddley2 = 100;
        }
        else if (playerNo == 2) {
            score = new Integer(0);
            myPaddlex1 = 460;
            myPaddlex2 = 10;
            myPaddley1 = 175;
            myPaddley2 = 100;
        }
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    public String getPlayerName() {
        return playerName;
    }
    public void incScore() {
        score++;
    }
    public void reset() {
        if (playerNo == 1) {
            myPaddlex1 = 10;
            myPaddlex2 = 10;
            myPaddley1 = 175;
            myPaddley2 = 100;
        }
        else if (playerNo == 2) {
            myPaddlex1 = 460;
            myPaddlex2 = 10;
            myPaddley1 = 175;
            myPaddley2 = 100;
        }
    }
    public String getScore() {
        return score.toString();
    }
    public int x1() {
        return myPaddlex1;
    }
    public int x2() {
        return myPaddlex2;
    }
    public int y1() {
        return myPaddley1;
    }
    public int y2() {
        return myPaddley2;
    }
    public void setGame(JComponent myGame) {
        this.myGame = myGame;
    }
    public void moveDown() {
        if (myPaddley1 < 350) {
            myPaddley1 += move;
            myGame.repaint();
        }
        else;
    }
    public void moveUp() {
        if (myPaddley1 > 10) {
            myPaddley1 -= move;
            myGame.repaint();
        }
        else;
    }
}

class Ball {
    private Random rn;
    private JComponent myGame;
// I added variables here that you need to declare… pretty much everything after gravity was added  
// by me just now, but make sure ALL of these variables are declared with their initialization. 
//Also theres added private booleans, dont forget em
    private int ballNo=1,centerx,centery,radius,velocityx,velocityy, speedindicator,gravity, speedingsteps, speedslower=1,speedfaster=1,explosion=1,fuse=100;
private int frictiondrag = 1;

    private boolean forwait = false, friction = false; 
    public static int opspeed = 15;
    Ball(int ballNo) {
        rn = new Random();
        this.ballNo = ballNo;
        radius = 15;
        reset();
    }
    public int centerx() {
        if (centerx > 17 && centerx < 447) {
            centerx -= velocityx;
            if (MainMenu.isClassic()) {
                speedindicator++;
                if (speedindicator % 500 == 0 && opspeed !=1) opspeed--;
            }
            if (MainMenu.isBaked()) {
            gravity++;
            if (gravity%10 == 0) velocityy++;
            if (velocityy == 0) explosion++;
            if (explosion % 100 == 0 || friction) {
            friction = true;
                   explosion = 1;
                   frictiondrag++;
                   if (frictiondrag%40 == 0) {
                       if (velocityx < 0) velocityx++;
                       if (velocityx > 0) velocityx--;
                   }
                   if (velocityx == 0) {
                       explode(); friction = false;
                   }
                                               }
                 if (Math.abs(velocityx) < 6 && friction == false){
                   speedfaster++;
                   if (speedfaster%20 == 0)
                   {
                   if (velocityx >= 0) velocityx++;
                   else if (velocityx<0) velocityx--;
                   }
               }
               if (Math.abs(velocityx) > 6) {
                   speedslower++;
               
               if (speedslower%50 == 0)
               {
                   if (velocityx >= 0) velocityx--;
               else if (velocityx < 0) velocityx++;
               }}
            }
            if (forwait) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {;}
                forwait = false;
            }
        }
        else {
            MyGUI.bounce();
            velocityx *= -1;
            if (centerx <= 17) {
                centerx = 18;
                if ((centery < MyGUI.getPad().get(0).y1() - 10)  || (centery > MyGUI.getPad().get(0).y1() + 100)) {
                    MyGUI.getPad().get(1).incScore();
                    MyGUI.getBal().get(0).reset();
                    opspeed = 15;
                   try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {;}
                   forwait = true;
                }
            }
            else if (centerx >= 447) {
                centerx = 446;
                if ((centery < MyGUI.getPad().get(1).y1() - 10) || (centery > MyGUI.getPad().get(1).y1() + 100)) {
                    MyGUI.getPad().get(0).incScore();
                    MyGUI.getBal().get(0).reset();
                    opspeed = 15;
                   try {
                       Thread.sleep(500);
                   } catch (InterruptedException ex) {;}
                    forwait = true;
                }
            }
        }
        return centerx;
    }
    public int centery() {
        if (centery > 0 && centery < 450)
            centery += velocityy;
        else {
            MyGUI.bounce();
            if (MainMenu.isClassic()) velocityy = -velocityy;
           velocityy = -(velocityy-1);

            if (centery <= 0) centery = 1;
            else if (centery >= 450) centery = 449;
        }
        return centery;
    }
    public int radius() {
        return radius;
    }
    public void setGame(JComponent myGame) {
        this.myGame = myGame;
    }
public void explode() {
    try {  Thread.sleep(250);
        } catch (InterruptedException ex) {;}
        velocityx = 10; velocityy = rn.nextInt(20);
}
    public void reset() {
        if (this.ballNo == 1) {
            centerx = 225;
            centery = 225;
            velocityx = 4*(int)Math.pow(-1,rn.nextInt(2)); velocityy = rn.nextInt(10)-5;
            if (velocityy == 0) velocityy = -4;
        }
    }
}

class GameGraphics extends JComponent {
    private String score1,score2;
    ArrayList<MyPaddle> paddles;
    ArrayList<Ball> balls;
    GameGraphics(ArrayList<MyPaddle> paddles,ArrayList<Ball> balls) {
        this.paddles = paddles;
        this.balls = balls;
        for (MyPaddle p : paddles) {
            p.setGame(this);
        }
    }
    public void paint(Graphics g) {
        for (MyPaddle p : paddles) g.fillRect(p.x1(),p.y1(),p.x2(),p.y2());
        try {
            Thread.sleep(Ball.opspeed);
        } catch (InterruptedException ex) {;}
        for (Ball b : balls) g.fillOval(b.centerx(),b.centery(),b.radius(),b.radius());
        score1 = MyGUI.getPad().get(0).getScore();
        score2 = MyGUI.getPad().get(1).getScore();
        g.drawString("Player 1: " + MyGUI.getPad().get(0).getScore(),70,10);
        g.drawString("Player 2: " + MyGUI.getPad().get(1).getScore(),350,10);
        if (score1.equals("10")) {
            MyGUI.setGameover(1);
            g.drawString("Player 1 wins!!!",190,265);
        }
        else if (score2.equals("10")) {
            MyGUI.setGameover(2);
            g.drawString("Player 2 wins!!!",190,265);
        }
    }
}
*/
/*
package pong;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JComponent;
import javax.swing.JFrame;

//I found a bug once. Last round, the ball hits the paddle in the middle of the paddle and it counts against him. He loses.

public class MyGUI extends JFrame implements KeyListener {
    //Make singleton at some point
    GameGraphics myG = null;
    Container jc;
    private int players;
    private AudioInputStream bounce;
    private static Clip myClip;
    private static ArrayList<MyPaddle> pad;
    private static ArrayList<Ball> bal;
    private static int gameover;
    MyGUI(int players) {
        gameover = 0;
        try {
            bounce = AudioSystem.getAudioInputStream(new File("C:\\Users\\Roman\\Documents\\NetBeansProjects\\Pong\\src\\pong\\paFinal.wav")); //Make this universal for all computers
            myClip = AudioSystem.getClip();
            myClip.open(bounce);
        }
        catch (Exception e) {;}
        this.players = players;
        this.setTitle("Pong on Crack");
        this.setSize(500,500);
        jc = this.getContentPane();
        jc.setBackground(Color.WHITE);
        pad = new ArrayList<>();
        bal = new ArrayList<>();
        for (int i = 1;i<=players;i++) pad.add(new MyPaddle(i));
        bal.add(new Ball(1));
        myG = new GameGraphics(pad,bal);
        this.add(myG);
        this.setFocusable(true);
        this.addKeyListener(this);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); ////////////////////////////////
    }
    public static void bounce() {
        myClip.start();
        myClip.setFramePosition(0);
    }
    public static void setGameover(int gameover) {
        MyGUI.gameover = gameover;
    }
    public static ArrayList<MyPaddle> getPad() {
        return pad;
    }
    public static ArrayList<Ball> getBal() {
        return bal;
    }
    public void start() {
        this.setVisible(true);
        while (gameover == 0) myG.repaint();
    }
    public void keyTyped(KeyEvent e) {
    }
    public void keyPressed(KeyEvent e) {
        if (gameover == 0) {
            if (e.getKeyChar() == 'W' || e.getKeyChar() == 'w') pad.get(0).moveUp(); //get whichever player you are
            else if (e.getKeyChar() == 'S' || e.getKeyChar() == 's') pad.get(0).moveDown();
            else if (e.getKeyCode() == 38) {
                try {
                    pad.get(1).moveUp();
                }
                catch (Exception exc) {
                    pad.get(0).moveUp();
                }
            }
            else if (e.getKeyCode() == 40) {
                try {
                    pad.get(1).moveDown();
                }
                catch (Exception exc) {
                    pad.get(0).moveDown();
                }
            }
            else;
        }
        else;
    }
    public void keyReleased(KeyEvent e) {
    }
}       
        
class MyPaddle {
    private String playerName;
    private JComponent myGame;
    private Integer score;
    private int playerNo,myPaddlex1,myPaddlex2,myPaddley1,myPaddley2,move;
    MyPaddle(int playerNo) {
        this.playerNo = playerNo;
        move = 50;
        if (playerNo == 1) {
            score = new Integer(0);
            myPaddlex1 = 10;
            myPaddlex2 = 10;
            myPaddley1 = 175;
            myPaddley2 = 100;
        }
        else if (playerNo == 2) {
            score = new Integer(0);
            myPaddlex1 = 460;
            myPaddlex2 = 10;
            myPaddley1 = 175;
            myPaddley2 = 100;
        }
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    public String getPlayerName() {
        return playerName;
    }
    public void incScore() {
        score++;
    }
    public void reset() {
        if (playerNo == 1) {
            myPaddlex1 = 10;
            myPaddlex2 = 10;
            myPaddley1 = 175;
            myPaddley2 = 100;
        }
        else if (playerNo == 2) {
            myPaddlex1 = 460;
            myPaddlex2 = 10;
            myPaddley1 = 175;
            myPaddley2 = 100;
        }
    }
    public String getScore() {
        return score.toString();
    }
    public int x1() {
        return myPaddlex1;
    }
    public int x2() {
        return myPaddlex2;
    }
    public int y1() {
        return myPaddley1;
    }
    public int y2() {
        return myPaddley2;
    }
    public void setGame(JComponent myGame) {
        this.myGame = myGame;
    }
    public void moveDown() {
        if (myPaddley1 < 350) {
            myPaddley1 += move;
            myGame.repaint();
        }
        else;
    }
    public void moveUp() {
        if (myPaddley1 > 10) {
            myPaddley1 -= move;
            myGame.repaint();
        }
        else;
    }
}

class Ball {
    private Random rn;
    private JComponent myGame;
    private int ballNo=1,centerx,centery,radius,velocityx,velocityy,speedindicator,gravity;
    private boolean forwait = false;
    public static int opspeed = 10;
    Ball(int ballNo) {
        rn = new Random();
        this.ballNo = ballNo;
        radius = 15;
        reset();
    }
    public int centerx() {
        if (centerx > 17 && centerx < 447) {
            centerx -= velocityx;
            speedindicator++;
            if (speedindicator % 500 == 0 && opspeed !=1) opspeed--;
            if (forwait) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {;}
                forwait = false;
            }
        }
        else {
            MyGUI.bounce();
            velocityx *= -1;
            if (centerx <= 17) {
                centerx = 18;
                if ((centery < MyGUI.getPad().get(0).y1() - 10)  || (centery > MyGUI.getPad().get(0).y1() + 100)) {
                    MyGUI.getPad().get(1).incScore();
                    MyGUI.getBal().get(0).reset();
                    opspeed = 10;
                   try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {;}
                   forwait = true;
                }
            }
            else if (centerx >= 447) {
                centerx = 446;
                if ((centery < MyGUI.getPad().get(1).y1() - 10) || (centery > MyGUI.getPad().get(1).y1() + 100)) {
                    MyGUI.getPad().get(0).incScore();
                    MyGUI.getBal().get(0).reset();
                    opspeed = 10;
                   try {
                       Thread.sleep(500);
                   } catch (InterruptedException ex) {;}
                    forwait = true;
                }
            }
        }
        return centerx;
    }
    public int centery() {
        if (centery > 0 && centery < 450)
            centery += velocityy;
        else {
            MyGUI.bounce();
            velocityy = -velocityy;
            if (centery <= 0) centery = 1;
            else if (centery >= 450) centery = 449;
        }
        return centery;
    }
    public int radius() {
        return radius;
    }
    public void setGame(JComponent myGame) {
        this.myGame = myGame;
    }
    public void reset() {
        if (this.ballNo == 1) {
            centerx = 225;
            centery = 225;
            velocityx = 3*(int)Math.pow(-1,rn.nextInt(2)); velocityy = rn.nextInt(7)-3;
            if (velocityy == 0) velocityy = -4;
        }
    }
}

class GameGraphics extends JComponent {
    private String score1,score2;
    ArrayList<MyPaddle> paddles;
    ArrayList<Ball> balls;
    GameGraphics(ArrayList<MyPaddle> paddles,ArrayList<Ball> balls) {
        this.paddles = paddles;
        this.balls = balls;
        for (MyPaddle p : paddles) {
            p.setGame(this);
        }
    }
    public void paint(Graphics g) {
        for (MyPaddle p : paddles) g.fillRect(p.x1(),p.y1(),p.x2(),p.y2());
        try {
            Thread.sleep(Ball.opspeed);
        } catch (InterruptedException ex) {;}
        for (Ball b : balls) g.fillOval(b.centerx(),b.centery(),b.radius(),b.radius());
        score1 = MyGUI.getPad().get(0).getScore();
        score2 = MyGUI.getPad().get(1).getScore();
        g.drawString("Player 1: " + MyGUI.getPad().get(0).getScore(),70,10);
        g.drawString("Player 2: " + MyGUI.getPad().get(1).getScore(),350,10);
        if (score1.equals("10")) {
            MyGUI.setGameover(1);
            g.drawString("Player 1 wins!!!",190,265);
        }
        else if (score2.equals("10")) {
            MyGUI.setGameover(2);
            g.drawString("Player 2 wins!!!",190,265);
        }
    }
}*/