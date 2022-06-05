import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumberSequenceDemo {
    private static final Logger logger = LoggerFactory.getLogger(NumberSequenceDemo.class);
    private int id = 2;
    private final int LAST = 10;
    private int count = 1;
    private int increment = 1;

    private synchronized void action(int id) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (this.id == id) {
                    this.wait();
                }
                logger.info(String.valueOf(count));
                if (this.id != 2) {
                    var newValue = count + increment;
                    if (newValue == 0 || newValue > 10) this.increment *= -1;
                    count += increment;
                }

                this.id = id;

                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        NumberSequenceDemo numberSequence = new NumberSequenceDemo();
        new Thread(() -> numberSequence.action(1), "th1").start();
        new Thread(() -> numberSequence.action(2), "th2").start();

    }

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
