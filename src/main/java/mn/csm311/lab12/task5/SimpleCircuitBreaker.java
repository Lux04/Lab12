package mn.csm311.lab12.task5;

import java.util.function.Supplier;

/**
 * Энгийн Circuit Breaker.
 */
public class SimpleCircuitBreaker {

    public enum State { CLOSED, OPEN, HALF_OPEN }

    public interface Clock {
        long now();
    }

    public static class SystemClock implements Clock {
        @Override
        public long now() { return System.currentTimeMillis(); }
    }

    private final int failureThreshold;
    private final long resetTimeoutMs;
    private final Clock clock;

    private State state = State.CLOSED;
    private int failureCount = 0;
    private long openedAt = 0L;

    public SimpleCircuitBreaker(int failureThreshold, long resetTimeoutMs) {
        this(failureThreshold, resetTimeoutMs, new SystemClock());
    }

    public SimpleCircuitBreaker(int failureThreshold, long resetTimeoutMs, Clock clock) {
        if (failureThreshold < 1) {
            throw new IllegalArgumentException("failureThreshold must be >= 1");
        }
        if (resetTimeoutMs < 0) {
            throw new IllegalArgumentException("resetTimeoutMs must be >= 0");
        }
        if (clock == null) {
            throw new IllegalArgumentException("clock must not be null");
        }
        this.failureThreshold = failureThreshold;
        this.resetTimeoutMs = resetTimeoutMs;
        this.clock = clock;
    }

    public State state() {
        if (state == State.OPEN && clock.now() - openedAt >= resetTimeoutMs) {
            state = State.HALF_OPEN;
        }
        return state;
    }

    /**
     * Үйлчилгээний дуудлагыг circuit breaker-ээр бүрхэнэ.
     */
    public <T> T execute(Supplier<T> op) {
        if (op == null) {
            throw new IllegalArgumentException("operation must not be null");
        }

        State current = state();
        if (current == State.OPEN) {
            throw new CircuitBreakerOpenException("circuit is open");
        }

        try {
            T result = op.get();
            onSuccess();
            return result;
        } catch (RuntimeException e) {
            onFailure();
            throw e;
        }
    }

    private void onSuccess() {
        state = State.CLOSED;
        failureCount = 0;
    }

    private void onFailure() {
        State current = state();

        if (current == State.HALF_OPEN) {
            state = State.OPEN;
            openedAt = clock.now();
            failureCount = 0;
            return;
        }

        if (current == State.CLOSED) {
            failureCount++;
            if (failureCount >= failureThreshold) {
                state = State.OPEN;
                openedAt = clock.now();
            }
        }
    }
}
