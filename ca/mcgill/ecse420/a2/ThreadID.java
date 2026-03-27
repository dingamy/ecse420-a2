package ca.mcgill.ecse420.a2;

public class ThreadID {
    private static volatile int currentID = 0;
    private static ThreadLocalID threadLocalId = new ThreadLocalID();
    public static int get() {
        return threadLocalId.get();
    }

    public static void reset() {
        currentID = 0;
    }

    private static class ThreadLocalID extends ThreadLocal<Integer> {
        @Override
        protected synchronized Integer initialValue() {
            return currentID++;
        }
    }
}
