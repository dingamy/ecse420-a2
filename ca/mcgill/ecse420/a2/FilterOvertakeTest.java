package ca.mcgill.ecse420.a2;
import java.util.concurrent.locks.Lock;

public class FilterOvertakeTest {
    private static final int X = 3;

    public static void testMutualExclusion(int n) throws InterruptedException {
        ThreadID.reset();
        Lock filterLock = new Filter(n);
        Thread[] threads = new Thread[n];

        for (int i = 0; i < n; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < X; j++) {
                    System.out.println("Thread " + ThreadID.get() + " on iteration " + (j + 1) + "/" + X + " attempting to lock");
                    filterLock.lock();
                    try {
                        System.out.println("Thread " + ThreadID.get() + " on iteration " + (j + 1) + "/" + X + " entered critical section");
                        // placeholder to simulate work
                        for (int k = 0; k < 100; k++) {}
                    } finally {
                        filterLock.unlock();
                    }
                }
            });
        }

        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();

    }

    public static void main(String[] args) throws InterruptedException {
        testMutualExclusion(8);
    }
}