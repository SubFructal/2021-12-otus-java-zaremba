import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NumberSequenceDemo {
    private static final Logger logger = LoggerFactory.getLogger(NumberSequenceDemo.class);
    private static final int THREADS_COUNT = 2;
    private static final int LAST_VALUE = 10;
    private static final int VALID_ID = 1;
    private int id = 2;
    private int increment = 1;
    private int count;

    private synchronized void action(int id) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (this.id == id) {
                    this.wait();
                }
                if (id == VALID_ID) {
                    count += increment;
                    if (count == 0 || count == LAST_VALUE) {
                        this.increment *= -1;
                    }
                }
                logger.info(String.valueOf(count));
                this.id = id;
                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        var numberSequence = new NumberSequenceDemo();
//        new Thread(() -> numberSequence.action(1), "thread1").start();
//        new Thread(() -> numberSequence.action(2), "thread2").start();
        var executor = Executors.newFixedThreadPool(THREADS_COUNT);
        executor.execute(() -> numberSequence.action(1));
        executor.execute(() -> numberSequence.action(2));
        executor.shutdown();
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
