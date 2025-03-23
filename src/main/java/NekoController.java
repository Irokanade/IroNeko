import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NekoController {
    private static final double PI = Math.PI;
    private static final int SPEED = 8;
    private static final int ANIMATION_INTERVAL = 100;

    private final JFrame frame;
    private final JLabel catLabel;
    private Point targetPosition;
    private Timer animationTimer;
    private ImageIcon[] images;
    private int no = 0;
    private int state = 2; // Start in idle state
    private double theta = 0;
    private int animationCounter = 0;

    private static final int MOVE_SPEED = 2;
    private static final int LICK_SPEED = 5;    // Higher = slower
    private static final int SCRATCH_SPEED = 1; // Higher = slower
    private static final int YAWN_DURATION = 15; // Total frames for yawn
    private static final int SLEEP_SPEED = 12; // Higher = slower

    public NekoController(JFrame frame, JLabel catLabel) {
        this.frame = frame;
        this.catLabel = catLabel;
        loadKitten();
        initAnimationSystem();
    }

    private void initAnimationSystem() {
        // Always-running animation timer
        animationTimer = new Timer(ANIMATION_INTERVAL, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePosition();
                animateCat();
            }
        });
        animationTimer.start(); // Start immediately
    }

    private void loadKitten() {
        // Same image loading as before
        images = new ImageIcon[33];
        try {
            for (int i = 1; i <= 32; i++) {
                images[i] = new ImageIcon(getClass().getResource("/images/" + i + ".gif"));
            }
            images[0] = images[25];
            catLabel.setIcon(images[0]);
        } catch (Exception e) {
            System.err.println("Error loading images: " + e.getMessage());
        }
    }

    public void setPosition(Point target) {
        this.targetPosition = target;
        if (state != 1) {
            state = 3; // Switch to surprised state if not moving
        }

    }

    private void updatePosition() {
        if (targetPosition == null || state == 3) return;

        Point currentPos = catLabel.getLocation();
        int dx = targetPosition.x - currentPos.x;
        int dy = targetPosition.y - currentPos.y;
        double dist = Math.sqrt(dx*dx + dy*dy);

        if (dist > SPEED) {
            theta = Math.atan2(-dy, dx);
            int newX = currentPos.x + (int)(SPEED * Math.cos(theta));
            int newY = currentPos.y - (int)(SPEED * Math.sin(theta));
            catLabel.setLocation(newX, newY);
        } else {
            catLabel.setLocation(targetPosition);
            targetPosition = null;
            state = 2; // Switch to idle state
            animationCounter = 0; // Reset idle counter
        }
    }

    private void animateCat() {
        switch (state) {
            case 1: // Moving state
                updateMovingAnimation();
                break;
            case 2: // Idle state
                updateIdleAnimation();
                break;
            case 3:
                updateSurprisedAnimation();
        }

        catLabel.setIcon(images[no]);
        frame.repaint();
    }

    private void updateMovingAnimation() {
        if (animationCounter % MOVE_SPEED == 0) {
            if (theta >= -PI/8 && theta <= PI/8) {
                no = (no == 5) ? 6 : 5;
            } else if (theta > PI/8 && theta < 3*PI/8) {
                no = (no == 3) ? 4 : 3;
            } else if (theta >= 3*PI/8 && theta <= 5*PI/8) {
                no = (no == 1) ? 2 : 1;
            } else if (theta > 5*PI/8 && theta < 7*PI/8) {
                no = (no == 15) ? 16 : 15;
            } else if (theta >= 7*PI/8 || theta <= -7*PI/8) {
                no = (no == 13) ? 14 : 13;
            } else if (theta > -7*PI/8 && theta < -5*PI/8) {
                no = (no == 11) ? 12 : 11;
            } else if (theta >= -5*PI/8 && theta <= -3*PI/8) {
                no = (no == 9) ? 10 : 9;
            } else if (theta > -3*PI/8 && theta < -PI/8) {
                no = (no == 7) ? 8 : 7;
            }
        }
        ++animationCounter;
    }

    private void updateIdleAnimation() {
        if (animationCounter < 60) { // Licking animation
            if (animationCounter % LICK_SPEED == 0) {
                no = (no == 31) ? 25 : 31;
            }
        }
        else if (animationCounter < 65) { // Scratching (extended duration)
            if (animationCounter % SCRATCH_SPEED == 0) {
                no = (no == 27) ? 28 : 27;
            }
        }
        else if (animationCounter < YAWN_DURATION + 65) { // Yawning
            no = 26; // Keep yawn image static
        }
        else { // Sleeping
            if (animationCounter % SLEEP_SPEED == 0) {
                no = (no == 29) ? 30 : 29;
            }
        }

        ++animationCounter;
    }

    public void updateSurprisedAnimation() {
        no = 32;
        state = 1;
        animationCounter = 0;
    }
}