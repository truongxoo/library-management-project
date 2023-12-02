package example.java.designpattern;

class EagerInitializedSingleton {

    private static final EagerInitializedSingleton INSTANCE = new EagerInitializedSingleton();

    private EagerInitializedSingleton() {

    }

    public static EagerInitializedSingleton getInstance() {
        return INSTANCE;
    }
}

class LazyInitializedSingleton {

    private static LazyInitializedSingleton instance;

    private LazyInitializedSingleton() {
    }

    public static LazyInitializedSingleton getInstance() {
        if (instance == null) {
            instance = new LazyInitializedSingleton();
        }
        return instance;
    }
}

class StaticBlockSingleton {

    private static final StaticBlockSingleton INSTANCE;

    private StaticBlockSingleton() {
    }

    static {
        try {
            INSTANCE = new StaticBlockSingleton();
        } catch (Exception e) {
            throw new RuntimeException("Exception occured in creating singleton instance");
        }
    }

    public static StaticBlockSingleton getInstance() {
        return INSTANCE;
    }
}

class DoubleCheckLockingSingleton {

    private static volatile DoubleCheckLockingSingleton instance;

    private DoubleCheckLockingSingleton() {
    }
    public static DoubleCheckLockingSingleton getInstance() {
        if (instance == null) {
            synchronized (DoubleCheckLockingSingleton.class) {
                if (instance == null) {
                    instance = new DoubleCheckLockingSingleton();
                }
            }
        }
        return instance;
    }
}

class BillPughSingleton {

    private BillPughSingleton() {
    }

    public static BillPughSingleton getInstance() {
        return SingletonHelper.INSTANCE;
    }

    private static class SingletonHelper {
        private static final BillPughSingleton INSTANCE = new BillPughSingleton();
    }
}

public class SingletonPatternDemo {

}
