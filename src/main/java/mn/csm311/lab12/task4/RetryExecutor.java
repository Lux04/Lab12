package mn.csm311.lab12.task4;

import java.util.function.Supplier;

/**
 * Exponential backoff + jitter-тэй retry хэрэгжүүлэгч.
 */
public class RetryExecutor {

    private final int maxAttempts;
    private final long baseDelayMs;
    private final Sleeper sleeper;

    /**
     * Sleeper нь Thread.sleep-ыг шууд дуудахын оронд тест боломжтой болгосон.
     */
    public interface Sleeper {
        void sleep(long millis) throws InterruptedException;
    }

    public static class RealSleeper implements Sleeper {
        @Override
        public void sleep(long millis) throws InterruptedException {
            Thread.sleep(millis);
        }
    }

    public RetryExecutor(int maxAttempts, long baseDelayMs) {
        this(maxAttempts, baseDelayMs, new RealSleeper());
    }

    public RetryExecutor(int maxAttempts, long baseDelayMs, Sleeper sleeper) {
        if (maxAttempts < 1) {
            throw new IllegalArgumentException("maxAttempts must be >= 1");
        }
        if (baseDelayMs < 0) {
            throw new IllegalArgumentException("baseDelayMs must be >= 0");
        }
        if (sleeper == null) {
            throw new IllegalArgumentException("sleeper must not be null");
        }
        this.maxAttempts = maxAttempts;
        this.baseDelayMs = baseDelayMs;
        this.sleeper = sleeper;
    }

    /**
     * op-ыг retry логиктайгаар ажиллуулна.
     */
    public <T> T execute(Supplier<T> op) {
        if (op == null) {
            throw new IllegalArgumentException("operation must not be null");
        }

        RuntimeException lastError = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return op.get();
            } catch (NonRetryableException e) {
                throw e;
            } catch (RuntimeException e) {
                lastError = e;

                if (attempt == maxAttempts) {
                    break;
                }

                long exponentialDelay = (long) (baseDelayMs * Math.pow(2, attempt - 1));
                long jitter = baseDelayMs == 0 ? 0 : (long) (Math.random() * (baseDelayMs + 1));
                long delay = exponentialDelay + jitter;

                try {
                    sleeper.sleep(delay);
                } catch (InterruptedException interrupted) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("retry sleep interrupted", interrupted);
                }
            }
        }

        throw lastError;
    }
}
