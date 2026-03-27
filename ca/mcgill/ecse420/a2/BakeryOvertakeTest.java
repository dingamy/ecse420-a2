package ca.mcgill.ecse420.a2;

import java.util.concurrent.locks.Lock;

public class BakeryOvertakeTest {
    private static final int X = 3;

    public static void testBakery(int n) throws InterruptedException {
        ThreadID.reset();
        Bakery bakeryLock = new Bakery(n); // use concrete type
        Lock lock = bakeryLock;

        Thread[] threads = new Thread[n];

        for (int i = 0; i < n; i++) {
            threads[i] = new Thread(() -> {
                int id = ThreadID.get();

                for (int j = 0; j < X; j++) {
                    System.out.println("Thread " + id +
                        " [iter " + (j + 1) + "] attempting to lock");

                    lock.lock();
                    try {
                        int label = bakeryLock.getLabel(id);

                        System.out.println("Thread " + id +
                            " [iter " + (j + 1) + "] ENTERED CS with label " + label);

                        // simulate work
                        for (int k = 0; k < 100; k++) {}

                    } finally {
                        lock.unlock();
                    }
                }
            });
        }

        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
    }

    public static void main(String[] args) throws InterruptedException {
        testBakery(8);
    }
}