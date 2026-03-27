package ca.mcgill.ecse420.a2;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

class Bakery implements Lock {
    private final AtomicIntegerArray flag;   // 0 = false, 1 = true
    private final AtomicIntegerArray label;

    public Bakery(int n) {
        flag = new AtomicIntegerArray(n);
        label = new AtomicIntegerArray(n);
        for (int i = 0; i < n; i++) {
            flag.set(i, 0);
            label.set(i, 0);
        }
    }

    @Override
    public void lock() {
        int i = ThreadID.get();
        int n = flag.length();

        flag.set(i, 1);

        int max = 0;
        for (int j = 0; j < n; j++) {
            int lj = label.get(j);
            if (lj > max) {
                max = lj;
            }
        }

        int myLabel = max + 1;
        label.set(i, myLabel);

        for (int k = 0; k < n; k++) {
            if (k == i) continue;

            while (flag.get(k) == 1 && (label.get(k) < myLabel || (label.get(k) == myLabel && k < i))) {
            }
        }
    }
    public int getLabel(int i) {
        return label.get(i);
    }

    @Override
    public void unlock() {
        int i = ThreadID.get();
        flag.set(i, 0);
        label.set(i, 0);
    }

    @Override
    public boolean tryLock() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException();
    }
}