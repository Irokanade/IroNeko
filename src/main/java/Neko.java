import javax.swing.*;
import java.awt.*;

public class Neko {
    private JFrame frame;
    private JLabel catLabel;

    public Neko() {
        initComponents();
        new NekoController(frame, catLabel);
    }

    private void initComponents() {
        frame = new JFrame("Neko");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(true);

        Container contentPane = frame.getContentPane();
        contentPane.setLayout(null);

        // Create cat label
        catLabel = new JLabel();
        catLabel.setSize(50, 50);
        catLabel.setLocation(75, 75);
        contentPane.add(catLabel);


        frame.setSize(200, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Neko();
            }
        });
    }
}