package PairsCard;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.util.ArrayList;

public class PanelGame extends JPanel {
    int row;
    int col;
    JLabel lbMove;
    JLabel lbNumMoves;
    JLabel lbTime;
    JLabel lbTimeSec;
    JLabel lbBar1;
    JLabel lbBar2;
    JLabel lbBar3;
    JButton btnHome;
    JButton btnRestart;
    ArrayList<JButton> ListBtnCards = new ArrayList<>();
    int[] btnImg;

    public PanelGame(int row, int col) {
        this.row = row;
        this.col = col;
        setSize((93+20) * col + 20, (125+20) * row + 85);
        setLayout(null);
        detailComponents();
    }
    private void detailComponents() {
        btnImg = new int[row * col];

        Icon homeScreen = new ImageIcon(new ImageIcon("PairsCard/Pictures/Home.png")
                          .getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH));
        Icon reStart = new ImageIcon(new ImageIcon("PairsCard/Pictures/Restart.png")
                       .getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH));
        btnHome = new JButton(homeScreen);
        lbBar1 = new JLabel(" | ");
        lbMove = new JLabel("Move : ");
        lbNumMoves = new JLabel("0");
        lbBar2 = new JLabel(" | ");
        lbTime = new JLabel("Time :");
        lbTimeSec = new JLabel("000 s");
        lbBar3 = new JLabel(" | ");
        btnRestart = new JButton(reStart);
        
        btnHome.setContentAreaFilled(false);
        btnHome.setBorderPainted(false);
        btnHome.setFocusPainted(false);
        btnRestart.setContentAreaFilled(false);
        btnRestart.setBorderPainted(false);
        btnRestart.setFocusPainted(false);

        int n = ((93+20) * col + 20 - 310) / 2;
        btnHome.setBounds(n, 10, 40, 40);
        lbBar1.setBounds(45 + n, 20, 20, 20);
        lbMove.setBounds(65 + n, 20, 50, 20);
        lbNumMoves.setBounds(115 + n, 20, 50, 20);
        lbBar2.setBounds(140 + n, 20, 20, 20);
        lbTime.setBounds(160 + n, 20, 50, 20);
        lbTimeSec.setBounds(210 + n, 20, 50, 20);
        lbBar3.setBounds(250 + n, 20, 20, 20);
        btnRestart.setBounds(270 + n, 10, 40, 40);

        add(lbMove);
        add(lbNumMoves);
        add(lbTime);
        add(lbTimeSec);
        add(lbBar1);
        add(lbBar2);
        add(lbBar3);
        add(btnHome);
        add(btnRestart);

        buildButton();
        randomCard();
    }
    private void buildButton() {
        Icon back = new ImageIcon(new ImageIcon("PairsCard/Pictures/Back.png")
                    .getImage().getScaledInstance(93, 125, java.awt.Image.SCALE_SMOOTH));
        for (int i = 0; i < row * col; i++) {
            JButton btnCard = new JButton(back);
            btnCard.setBounds(i % col * (93 + 20) + 20, i / col * (125 + 20) + 60, 93, 125);
            ListBtnCards.add(btnCard);
            add(btnCard);
        }
    }
    public void randomCard() {
        ArrayList<Integer> btnLeft = new ArrayList<>();
        ArrayList<Integer> cardLeft = new ArrayList<>();
        for (int i = 0; i < row * col; i++)
            btnLeft.add(i);
        for (int i = 0; i < 14; i++)
            cardLeft.add(i);
        for (int i = 0; i < row * col / 2; i++) {
            int idxCard = (int) (Math.random() * (14 - i));
            int idxImg = cardLeft.get(idxCard) * 4;
            cardLeft.remove(idxCard);
            if (idxImg != 52)
                idxImg += (int) (Math.random() * 4);
            for (int j = 0; j < 2; j++) {
                int idxNum = (int) (Math.random() * (row * col - 2 * i - j));
                btnImg[btnLeft.get(idxNum)] = idxImg;
                btnLeft.remove(idxNum);
            }
        }
    }
}
