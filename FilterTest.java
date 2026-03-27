import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

public class FilterTest {
    private static final int X = 100000;

    public static boolean testMutualExclusion(int n) throws InterruptedException {
        ThreadID.reset();
        Lock filterLock = new Filter(n);

        AtomicInteger threadsInCriticalSection = new AtomicInteger(0);
        AtomicBoolean failed = new AtomicBoolean(false);
        AtomicInteger counter = new AtomicInteger(0);
        Thread[] threads = new Thread[n];

        for (int i = 0; i < n; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < X; j++) {
                    filterLock.lock();
                    try {
                        int current = threadsInCriticalSection.incrementAndGet();
                        if (current > 1) {
                            failed.set(true);
                            System.err.println("Violation! Threads in CS: " + current);
                        }
                        counter.incrementAndGet();
                        // placeholder to simulate work
                        for (int k = 0; k < 100; k++) {}
                        threadsInCriticalSection.decrementAndGet();
                    } finally {
                        filterLock.unlock();
                    }
                }
            });
        }

        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();

        int expected = n * X;

        return !failed.get() && counter.get() == expected;
    }

    public static void main(String[] args) throws InterruptedException {
        for (int n = 2; n <= 8; n++) {
            boolean passed = testMutualExclusion(n);
            System.out.println("n = " + n + ": " + (passed ? "PASS" : "FAIL"));
        }
    }
}