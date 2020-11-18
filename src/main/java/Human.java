import javax.swing.*;
import java.awt.*;
import java.util.Objects;


public class Human extends JFrame {
    JLabel hum = new JLabel();
    private final int ICO_WIDTH = 80;
    private final int ICO_HEIGHT = 80;
    private final int ANIMATION_SPEED = 50; // пауза между кадрами (милисек)

    public Human(Container container, int x, int y) {

        hum.setIcon(iconResize(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/runSequense/01.png"))), ICO_WIDTH, ICO_HEIGHT));
        hum.setBounds(x - (hum.getIcon().getIconWidth() / 2), y - (hum.getIcon().getIconHeight() / 2), hum.getIcon().getIconWidth(), hum.getIcon().getIconHeight());
        container.add(hum);
    }

    public void move(char direction) {

        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {

                        if (direction == 'r') {
                            for (int i = 0; i < 6; i++) {
                                hum.setIcon(iconResize(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/runSequense/01.png"))), ICO_WIDTH, ICO_HEIGHT));
                                hum.setBounds(hum.getX() + 10, hum.getY(), hum.getWidth(), hum.getHeight());
                                Thread.sleep(ANIMATION_SPEED);
                                hum.setIcon(iconResize(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/runSequense/02.png"))), ICO_WIDTH, ICO_HEIGHT));
                                hum.setBounds(hum.getX() + 10, hum.getY(), hum.getWidth(), hum.getHeight());
                                Thread.sleep(ANIMATION_SPEED);
                                hum.setIcon(iconResize(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/runSequense/03.png"))), ICO_WIDTH, ICO_HEIGHT));
                                hum.setBounds(hum.getX() + 10, hum.getY(), hum.getWidth(), hum.getHeight());
                                Thread.sleep(ANIMATION_SPEED);
                                hum.setIcon(iconResize(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/runSequense/04.png"))), ICO_WIDTH, ICO_HEIGHT));
                                hum.setBounds(hum.getX() + 10, hum.getY(), hum.getWidth(), hum.getHeight());
                                Thread.sleep(ANIMATION_SPEED);
                                hum.setIcon(iconResize(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/runSequense/05.png"))), ICO_WIDTH, ICO_HEIGHT));
                                hum.setBounds(hum.getX() + 10, hum.getY(), hum.getWidth(), hum.getHeight());
                                Thread.sleep(ANIMATION_SPEED);
                                hum.setIcon(iconResize(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/runSequense/06.png"))), ICO_WIDTH, ICO_HEIGHT));
                                hum.setBounds(hum.getX() + 10, hum.getY(), hum.getWidth(), hum.getHeight());
                                Thread.sleep(ANIMATION_SPEED);
                            }
                        }


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    public ImageIcon iconResize(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        icon = new ImageIcon(newImg);
        return icon;
    }
}

