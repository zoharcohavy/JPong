package pong;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MainMenu extends JFrame implements ActionListener {
    //Why declare values in constructors instead of here?
    private static boolean classic = false,baked = false,crack = false,single=false,mult=false;
    private JPanel thisJC,settingsJC,settingsSubJP;
    private JLabel jl1,jl2,jl3;
    private JButton jb1,jb2,jb3,jb4,jb5,jb6,jb7,jb8,jb9;
    private Container jc;
    private int status;
    MainMenu() {
        status = 0;
        thisJC = new JPanel();
        settingsJC = new JPanel();
        jc = this.getContentPane();
        //jc.setBackground(Color.WHITE);
        createMainMenu(thisJC);
        createSettings(settingsJC);
        this.add(thisJC);
        jb1.addActionListener(this);
        jb2.addActionListener(this);
        jb3.addActionListener(this);
        jb4.addActionListener(this);
        jb5.addActionListener(this);
        this.setSize(275,250);
        this.setVisible(true);
        this.setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    public static boolean isClassic() {
        return classic;
    }
    public static boolean isBaked() {
        return baked;
    }
    public static boolean isCrack() {
        return crack;
    }
    private void createSettings(JPanel jp) {
        classic = baked = crack = false;
        jp.setLayout(new BoxLayout(jp,BoxLayout.Y_AXIS));
        settingsSubJP = new JPanel();
        settingsSubJP.setLayout(new FlowLayout());
        settingsSubJP.setAlignmentX(Component.CENTER_ALIGNMENT);
        jl3 = new JLabel("Select a Mode");
        jl3.setBorder(new EmptyBorder(20,20,20,20));
        jl3.setFont(new Font("Comic Sans MS",Font.PLAIN,18));
        jl3.setAlignmentX(Component.CENTER_ALIGNMENT);
        jb6 = new JButton("Classic");
        jb7 = new JButton("Baked");
        jb8 = new JButton("Crack");
        jb6.addActionListener(this);
        jb7.addActionListener(this);
        jb8.addActionListener(this);
        jb6.setPreferredSize(new Dimension(80,50));
        jb7.setPreferredSize(new Dimension(80,50));
        jb8.setPreferredSize(new Dimension(80,50));
        jp.add(jl3);
        settingsSubJP.add(jb6);settingsSubJP.add(jb7);settingsSubJP.add(jb8);
        settingsSubJP.setMaximumSize(new Dimension(350,85));
        jp.add(settingsSubJP);
        jb9 = new JButton("Play!");
        jb9.addActionListener(this);
        jb9.setAlignmentX(Component.CENTER_ALIGNMENT);
        jp.add(jb9);
    }
    private void createMainMenu(JPanel jp) {
        this.setTitle("Pong on Crack");
        jl1 = new JLabel("Pong on Crack");
        jl1.setFont(new Font("Comic Sans MS",Font.PLAIN,24)); //Really? Comic Sans?
        jl1.setAlignmentX(Component.CENTER_ALIGNMENT);
        jl2 = new JLabel(" ");
        jl2.setAlignmentX(Component.CENTER_ALIGNMENT);
        //Button font?
        jb1 = new JButton(" Single Player");
        jb1.setAlignmentX(Component.CENTER_ALIGNMENT);
        jb2 = new JButton("  Split Screen ");
        jb2.setAlignmentX(Component.CENTER_ALIGNMENT);
        jb3 = new JButton("       Online       ");
        jb3.setAlignmentX(Component.CENTER_ALIGNMENT);
        jb4 = new JButton("      Settings    ");
        jb4.setAlignmentX(Component.CENTER_ALIGNMENT);
        jb5 = new JButton("          Exit        ");
        jb5.setAlignmentX(Component.CENTER_ALIGNMENT);
        jp.add(jl1);jp.add(jl2);
        jp.add(jb1);jp.add(jb2);jp.add(jb3);jp.add(jb4);jp.add(jb5);
        jp.setLayout(new BoxLayout(jp,BoxLayout.Y_AXIS));
    }
    public static boolean isSingle() {
        return single;
    }
    public static boolean isMult() {
        return mult;
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jb1) {
            single = true;
            preGame();
        }
        if (e.getSource() == jb2) {
            mult = true;
            preGame();
        }
        else if (e.getSource() == jb5) {
            status = -1;
            this.dispatchEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING));
        }
        else if (e.getSource() == jb6) {
            classic = true;
            baked = crack = false;
            jb6.setBackground(Color.BLUE);
            jb7.setBackground(null);
            jb8.setBackground(null);
        }
        else if (e.getSource() == jb7) {
            baked = true;
            classic = crack = false;
            jb6.setBackground(null);
            jb7.setBackground(Color.BLUE);
            jb8.setBackground(null);
        }
        else if (e.getSource() == jb8) {
            crack = true;
            classic = baked = false;
            jb6.setBackground(null);
            jb7.setBackground(null);
            jb8.setBackground(Color.BLUE);
            MyGUI.makeCrack();
        }
        else if (e.getSource() == jb9) {
            erase();
            status = 2;
        }
    }
    public int getStatus() {
        return status;
    }
    public void hideMe() {
        this.setVisible(false);
    }
    private void preGame() {
        erase();
        this.add(settingsJC);
        this.revalidate();
    }
    private void erase() {
        this.remove(thisJC);
        this.revalidate();
        this.repaint();
    }
}