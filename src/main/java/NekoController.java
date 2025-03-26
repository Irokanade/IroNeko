import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class NekoController {
    private static final double PI = Math.PI;
    private static final int SPEED = 5;
    private static final int ANIMATION_INTERVAL = 50;

    private final JFrame frame;
    private final JLabel catLabel;
    private Point targetPosition;
    private Timer animationTimer;
    private ImageIcon[] images;
    private int no = 0;
    private int state = 2;
    private double theta = 0;
    private int idleAnimationCounter = 0;
    private int moveAnimationCounter = 0;

    private static final int MOVE_SPEED = 4;
    private static final int LICK_SPEED = 5;
    private static final int SCRATCH_SPEED = 2;
    private static final int YAWN_DURATION = 15;
    private static final int SLEEP_SPEED = 12;

    public NekoController(JFrame frame, JLabel catLabel) {
        this.frame = frame;
        this.catLabel = catLabel;
        loadKitten();
        initAnimationSystem();
    }

    private void initAnimationSystem() {
        animationTimer = new Timer(ANIMATION_INTERVAL, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePosition();
                animateCat();
            }
        });
        animationTimer.start();
    }

    private void loadKitten() {
        images = new ImageIcon[33];
        for (int i = 1; i <= 32; i++) {
            images[i] = new ImageIcon(
                    Objects.requireNonNull(getClass().getResource("/images/" + i + ".gif"))
            );
        }
        images[0] = images[25];
        catLabel.setIcon(images[0]);
    }

    public void setPosition(Point target) {
        this.targetPosition = target;
    }

    private void updatePosition() {
        // Convert mouse position to frame coordinates
        Point screenPoint = MouseInfo.getPointerInfo().getLocation();
        Point convertedPoint = new Point(screenPoint);
        SwingUtilities.convertPointFromScreen(convertedPoint, frame.getContentPane());

        setPosition(convertedPoint);

        if (targetPosition == null || state == 3) return;

        Point currentPos = catLabel.getLocation();
        int dx = targetPosition.x - currentPos.x;
        int dy = targetPosition.y - currentPos.y;
        double dist = Math.sqrt(dx*dx + dy*dy);

        // Reset animation counter if cat is not idle
        if (state != 2) {
            idleAnimationCounter = 0;
        }

        if (dist > SPEED) {
            state = 1;
            theta = Math.atan2(-dy, dx);
            int newX = currentPos.x + (int)(SPEED * Math.cos(theta));
            int newY = currentPos.y - (int)(SPEED * Math.sin(theta));
            setPosition(new Point(newX, newY));
        } else {
            state = 2;
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
        catLabel.setLocation(targetPosition);
    }

    private void updateMovingAnimation() {
        if (moveAnimationCounter % MOVE_SPEED == 0) {
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
        ++moveAnimationCounter;
    }

    private void updateIdleAnimation() {
        if (idleAnimationCounter < 60) { // Licking animation
            if (idleAnimationCounter % LICK_SPEED == 0) {
                no = (no == 31) ? 25 : 31;
            }
        }
        else if (idleAnimationCounter < 65) { // Scratching (extended duration)
            if (idleAnimationCounter % SCRATCH_SPEED == 0) {
                no = (no == 27) ? 28 : 27;
            }
        }
        else if (idleAnimationCounter < YAWN_DURATION + 65) { // Yawning
            no = 26; // Keep yawn image static
        }
        else { // Sleeping
            if (idleAnimationCounter % SLEEP_SPEED == 0) {
                no = (no == 29) ? 30 : 29;
            }
        }

        ++idleAnimationCounter;
    }

    public void updateSurprisedAnimation() {
        no = 32;
        state = 1;
        idleAnimationCounter = 0;
    }
}