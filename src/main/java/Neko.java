import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Neko {
    private JFrame frame;
    private JLabel catLabel;
    private NekoController controller;

    public Neko() {
        initComponents();
        controller = new NekoController(frame, catLabel);
    }

    private void initComponents() {
        frame = new JFrame("Neko");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = frame.getContentPane();
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(0, 0, 0, 0));

        // Create cat label
        catLabel = new JLabel();
        catLabel.setSize(50, 50);
        catLabel.setLocation(75, 75);
        catLabel.setDoubleBuffered(true);
        contentPane.add(catLabel);

        contentPane.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                // Convert mouse point to component coordinates
                Point mousePoint = e.getPoint();
                // Update controller with current mouse position
                controller.setPosition(mousePoint);
            }
        });

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