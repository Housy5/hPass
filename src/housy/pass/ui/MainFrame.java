package housy.pass.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {

    private JPanel content;
    private int width = 800;
    private int height = width / 4 * 3; // 4:3 aspect ratio.
    private Dimension dimension = new Dimension(width, height);

    public MainFrame() {
        try {
            setTitle("hPass");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            content = new JPanel();
            content.setPreferredSize(dimension);
            content.setBackground(new Color(0xfffaf1));
            content.setLayout(new CenteredLayout());
            content.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, false));

            setContentPane(content);
            pack();
            setLocationRelativeTo(null);
            setResizable(false);
            setIconImage(ImageIO.read(getClass().getResource("/res/padlock.png")));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void showPage(JPanel page) {
        synchronized (getTreeLock()) {
            content.removeAll();
            content.add(page);
            content.revalidate();
            content.repaint();
        }
    }
}
