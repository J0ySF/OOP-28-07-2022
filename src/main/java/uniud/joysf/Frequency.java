package uniud.joysf;

import org.jetbrains.annotations.NotNull;

/**
 * This class represents a frequency.
 */

public class Frequency {

    enum Unit {
        MINUTE,
        SECOND,
        MILLISECOND,
    }

    /**
     * Creates a new frequency from unit of time and pulse count
     * @param pulses a discrete number
     * @param unit a unit of time
     */
    public Frequency(int pulses, @NotNull Unit unit) {

    }
}
