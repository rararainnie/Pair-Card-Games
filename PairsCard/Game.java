package PairsCard;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;

import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

import java.awt.*;
import java.awt.geom.*;

public class Game {
    private JFrame f;
    private JPanel pnlMenu;
    private PanelGame pnlGame;
    private ArrayList<PanelGame> arrPanelGame = new ArrayList<>();
    private Icon back;
    private Icon[] arrImg = new Icon[53];
    private Timer timer;
    private Timer timerClose1;
    private Timer timerClose2;
    private JButton thisBtn;
    private JButton firstBtn;
    private int move;
    private int secondsElapsed;
    private int opened;
    private int firstOpened;
    private int countPairsCard;
    private boolean stopAnimation = false;

    public Game() {
        f = new JFrame("Pairs Card Game");
        f.setSize(700, 655);
        f.setLayout(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        playMusic();
        detailComponents();
    }

    public class RoundButton extends JButton {
        public RoundButton(String text) {
            super(text);
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Shape shape = new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            g2.setColor(getBackground());
            g2.fill(shape);

            super.paintComponent(g);
        }

        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Shape shape = new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            g2.setColor(getForeground());
            g2.draw(shape);
        }

        public boolean contains(int x, int y) {
            Shape shape = new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            return shape.contains(x, y);
        }
    }

    private void detailComponents() {
        pnlMenu = new JPanel(null);
        pnlMenu.setSize(f.getSize());

        JLabel lbMenu1 = new JLabel("Pairs Card Game");
        lbMenu1.setForeground(Color.white);
        animate(lbMenu1);

        RoundButton btnGame1 = new RoundButton("Game 1 (3 x 4)");
        btnGame1.setFont(new Font("#ZF Terminal", Font.PLAIN, 20));

        RoundButton btnGame2 = new RoundButton("Game 2 (4 x 5)");
        btnGame2.setFont(new Font("#ZF Terminal", Font.PLAIN, 20));

        RoundButton btnGame3 = new RoundButton("Game 3 (4 x 7)");
        btnGame3.setFont(new Font("#ZF Terminal", Font.PLAIN, 20));

        lbMenu1.setBounds(60, 70, 600, 50);

        btnGame1.setBounds(160, 200, 170, 80);
        btnGame1.setBackground(new Color(242, 223, 228));
        btnGame2.setBounds(160, 320, 170, 80);
        btnGame2.setBackground(new Color(230, 230, 250));
        btnGame3.setBounds(160, 440, 170, 80);
        btnGame3.setBackground(new Color(201, 173, 167));

        pnlMenu.add(lbMenu1);
        pnlMenu.add(btnGame1);
        pnlMenu.add(btnGame2);
        pnlMenu.add(btnGame3);

        f.add(pnlMenu);

        ImageIcon bgImage = new ImageIcon("PairsCard/Pictures/bg.jpg");
        JLabel background = new JLabel(bgImage);
        background.setBounds(0, 0, f.getWidth(), f.getHeight());

        pnlMenu.add(background);

        f.setVisible(true);

        btnGame1.addActionListener(e -> start(3, 4));
        btnGame2.addActionListener(e -> start(4, 5));
        btnGame3.addActionListener(e -> start(4, 7));

        addImages();
        setTimer();
    }

    private void setTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pnlGame.lbTimeSec.setText(String.format("%03d s", ++secondsElapsed));
                if (secondsElapsed == 999)
                    timer.stop();
            }
        });

        timerClose1 = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeCard(true);
            }
        });
        timerClose1.setRepeats(false);

        timerClose2 = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeCard(false);
            }
        });
        timerClose2.setRepeats(false);
    }

    private void start(int row, int col) {
        boolean containsSize = false;
        for (PanelGame pg : arrPanelGame) {
            if (pg.row == row && pg.col == col) {
                containsSize = true;
                pnlGame = pg;
                pnlGame.setVisible(true);
                pnlGame.randomCard();
                break;
            }
        }
        if (!containsSize) {
            pnlGame = new PanelGame(row, col);
            AllButtonListener bl = new AllButtonListener();
            pnlGame.btnHome.addActionListener(bl);
            pnlGame.btnRestart.addActionListener(bl);
            for (int i = 0; i < row * col; i++)
                pnlGame.ListBtnCards.get(i).addActionListener(bl);
            f.add(pnlGame);
            arrPanelGame.add(pnlGame);
        }
        pnlMenu.setVisible(false);
        f.setSize(pnlGame.getSize());
        setDefault();
        timer.start();
    }

    private void setDefault() {
        pnlGame.lbNumMoves.setText("0");
        pnlGame.lbTimeSec.setText("000 s");
        move = 0;
        secondsElapsed = 0;
        opened = 0;
        firstOpened = -1;
        countPairsCard = 0;
    }

    private class AllButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (opened == 2) {
                if (source == thisBtn || source == firstBtn)
                    return;
                if (timerClose1.isRunning()) {
                    timerClose1.stop();
                    closeCard(true);
                } else {
                    timerClose2.stop();
                    closeCard(false);
                }
            }
            if (source == pnlGame.btnHome) {
                resetButton();
                pnlGame.setVisible(false);
                pnlMenu.setVisible(true);
                f.setSize(pnlMenu.getSize());
                return;
            }
            if (source == pnlGame.btnRestart) {
                resetButton();
                setDefault();
                pnlGame.randomCard();
                timer.start();
                return;
            }
            int btnIdx = pnlGame.ListBtnCards.indexOf(source);
            if (btnIdx == firstOpened)
                return;
            int thisImgIdx = pnlGame.btnImg[btnIdx];
            thisBtn = pnlGame.ListBtnCards.get(btnIdx);
            thisBtn.setIcon(arrImg[thisImgIdx]);
            pnlGame.lbNumMoves.setText(Integer.toString(++move));
            if (opened++ == 0)
                firstOpened = btnIdx;
            else {
                int firstImgIdx = pnlGame.btnImg[firstOpened];
                firstBtn = pnlGame.ListBtnCards.get(firstOpened);
                firstOpened = -1;
                if (thisImgIdx == firstImgIdx) {
                    countPairsCard++;
                    if (countPairsCard == pnlGame.row * pnlGame.col / 2) {
                        timer.stop();
                        JOptionPane.showMessageDialog(f,
                                "Congratulations! You win the game. (Move " + move +
                                        " times and it takes " + secondsElapsed + " seconds.)");
                    }
                    timerClose1.start();
                } else
                    timerClose2.start();
            }
        }
    }

    private void closeCard(boolean correct) {
        thisBtn.setIcon(back);
        firstBtn.setIcon(back);
        if (correct) {
            thisBtn.setVisible(false);
            firstBtn.setVisible(false);
        }
        opened = 0;
    }

    private void resetButton() {
        for (JButton b : pnlGame.ListBtnCards)
            if (!b.isVisible())
                b.setVisible(true);

        if (opened == 1)
            pnlGame.ListBtnCards.get(firstOpened).setIcon(back);

        timer.stop();
    }

    private void playMusic() {
        try {
            File file = new File("PairsCard/Pictures/backgroundMusic.wav");
            AudioInputStream stream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(stream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
        }
    }

    private void addImages() {
        back = new ImageIcon(new ImageIcon("PairsCard/Pictures/Back.png")
                .getImage().getScaledInstance(93, 125, java.awt.Image.SCALE_SMOOTH));
        String[] rank = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };
        String[] symbol = { "Spades", "Heart", "Diamond", "Clubs" };
        for (int i = 0; i < 52; i++) {
            arrImg[i] = new ImageIcon(new ImageIcon("PairsCard/Pictures/" + rank[i / 4] + "_" + symbol[i % 4] + ".png")
                    .getImage().getScaledInstance(93, 125, java.awt.Image.SCALE_SMOOTH));
        }
        arrImg[52] = new ImageIcon(new ImageIcon("PairsCard/Pictures/Joker.png").getImage().getScaledInstance(93, 125,
                java.awt.Image.SCALE_SMOOTH));
    }

    public void animate(JLabel label) {
        String message = label.getText();
        label.setFont(new Font("#ZF Terminal", Font.PLAIN, 60));
        new Thread(new Runnable() {
            @Override
            public void run() {
                int index = 0;
                while (!stopAnimation) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    label.setText(message.substring(0, index + 1));
                    index++;
                    if (index == message.length()) {
                        index = 0;
                        try {
                            Thread.sleep(800);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
}