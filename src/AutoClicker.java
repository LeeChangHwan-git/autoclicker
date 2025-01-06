import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.concurrent.*;

public class AutoClicker {
    private Robot robot;
    private BufferedImage buttonImage;

    public AutoClicker() {
        try {
            robot = new Robot();
            buttonImage = ImageIO.read(new File("complete.png"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void start() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        executor.scheduleAtFixedRate(() -> {
            try {
                Point buttonLocation = findButton();
                if (buttonLocation != null) {
                    clickButton(buttonLocation);
                    System.out.println("버튼 클릭 완료: " + buttonLocation.x + ", " + buttonLocation.y);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    private Point findButton() {
        try {
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screen = robot.createScreenCapture(screenRect);

            outerLoop:
            for (int x = 0; x < screen.getWidth() - buttonImage.getWidth(); x++) {
                middleLoop:
                for (int y = 0; y < screen.getHeight() - buttonImage.getHeight(); y++) {
                    if (screen.getRGB(x, y) != buttonImage.getRGB(0, 0)) {
                        continue middleLoop;
                    }

                    for (int i = 0; i < buttonImage.getWidth(); i++) {
                        for (int j = 0; j < buttonImage.getHeight(); j++) {
                            if (screen.getRGB(x + i, y + j) != buttonImage.getRGB(i, j)) {
                                continue middleLoop;
                            }
                        }
                    }
                    return new Point(x + buttonImage.getWidth()/2, y + buttonImage.getHeight()/2);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private void clickButton(Point location) {
        robot.mouseMove(location.x, location.y);
        robot.delay(100);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(50);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public static void main(String[] args) {
        System.out.println("자동 클릭 프로그램을 시작합니다...");
        AutoClicker clicker = new AutoClicker();
        clicker.start();
    }
}