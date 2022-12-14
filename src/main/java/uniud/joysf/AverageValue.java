package uniud.joysf;

import java.time.Duration;
import java.time.Instant;

/**
 * This interface represents an average value's storage and computation
 */

public interface AverageValueStrategy {
    void register(Instant instant, Value value);
    Value compute(Duration duration);
}
