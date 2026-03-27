package ca.mcgill.ecse420.a2;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

class Filter implements Lock {
    private final AtomicIntegerArray level;
    private final AtomicIntegerArray victim;
    private final int n;

    public Filter(int n) {
        this.n = n;
        level = new AtomicIntegerArray(n);
        victim = new AtomicIntegerArray(n);
    }
    
    @Override
    public void lock() {
        int me = ThreadID.get();
        for (int i = 1; i < n; i++) {
            level.set(me, i);
            victim.set(i, me);
            
            boolean conflict = true;
            while (conflict) {
                conflict = false;
                for (int k = 0; k < n; k++) {
                    if (k != me && level.get(k) >= i && victim.get(i) == me) {
                        conflict = true;
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void unlock() {
        int me = ThreadID.get();
        level.set(me, 0);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean tryLock() {
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