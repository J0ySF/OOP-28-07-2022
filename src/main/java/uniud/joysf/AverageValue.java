package uniud.joysf;

import java.time.Duration;
import java.time.Instant;

/**
 * This interface represents an average value's storage and computation
 * Average values are made up of many instant values
 */

public interface AverageValue {
    /**
     * Adds a new value to the average value computation, registered at a specific point in time
     * @param instant when the value has been collected
     * @param value the new value
     */
    void register(Instant instant, Value value);

    /**
     * Computes the average value using the given duration.
     * @param duration the time span that contains the values that will be averaged
     * @return the average value inside the time duration
     */
    Value compute(Duration duration);
}
