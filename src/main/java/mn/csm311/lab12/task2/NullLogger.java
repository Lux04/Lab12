package mn.csm311.lab12.task2;

/**
 * Null Object Pattern implementation for Logger.
 */
public class NullLogger implements Logger {

    @Override
    public void log(String message) {
        // Intentionally do nothing.
    }

    @Override
    public int logCount() {
        return 0;
    }
}
