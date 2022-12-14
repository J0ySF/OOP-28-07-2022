package uniud.joysf;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Abstract class which represents a device that can be mounted onto a control panel, it's objective is to collect
 * and store environmental data.
 * Inherited classes must implement a poll method to collect data using a device-specific implementation and a
 * handleCommand method which handles commands specific to the implemented device.
 */

public abstract class Device {

    /**
     * Internal class which is used to store a collected data type's instant and average state over time.
     */
    private static class Data {
        private Value instantValue;
        private AverageValue averageStrategy;

        /**
         * Constructs a data using a given average value strategy
         * @param averageStrategy the strategy used
         */
        public Data(AverageValue averageStrategy) {
            this.averageStrategy = averageStrategy;
        }

        /**
         * Registers a new value as the new instant value and adds it to the average value
         * @param value the new registered value
         * @param instant the instant the value was sampled
         */
        public void register(@NotNull Value value, @NotNull Instant instant) {
            instantValue = value;
            averageStrategy.register(instant, value);
        }
    }

    private HashMap<String, Data> values;

    /**
     * Constructs a device that holds a specific set of queryable keys with associated average value calculation
     * strategies.
     * @param keys a set of variable names and average value calculation strategy couples.
     */
    Device(AverageValue averageValueStrategy, Set<Map.Entry<String, AverageValue>> keys) {
        values = new HashMap<>();
        for (Map.Entry<String, AverageValue> k: keys) {
            values.put(k.getKey(), new Data(k.getValue()));
        }
    }

    /**
     * Method called asynchronously when it's time to sample environmental data, alters every stored value's instant
     * and average value.
     * @param instant the instant this callback was generated
     */
    public final void pollingCallback(Instant instant) {
        for(Map.Entry<String, Data> entry : values.entrySet()) {
            Value value = poll(entry.getKey());
            entry.getValue().register(value, instant);
        }
    }

    /**
     * Exception thrown when a queried variable does not exist inside a device.
     */
    public static class VariableDoesNotExistException extends Exception {

    }

    /**
     * Queries the given key's associated instant value
     * @param key the key which represents the variable's name
     * @return the value of the variable associated to the key
     * @throws VariableDoesNotExistException when the key is not present inside the device
     */
    public final Value extractInstantValue(@NotNull String key) throws VariableDoesNotExistException {
        try {
            return values.get(key).instantValue;
        }
        catch (Exception e) {
            throw new VariableDoesNotExistException();
        }
    }

    /**
     * Queries the given key's associated average value
     * @param key the key which represents the variable's name
     * @param duration the desired average's time span
     * @return the value of the variable associated to the key
     * @throws VariableDoesNotExistException when the key is not present inside the device
     */
    public final Value extractAverageValue(@NotNull String key, @NotNull Duration duration) throws VariableDoesNotExistException {
        try {
            return values.get(key).averageStrategy.compute(duration);
        }
        catch (Exception e) {
            throw new VariableDoesNotExistException();
        }
    }

    /**
     * Polls a value specified by a key
     * @param key the variable's name
     * @return the collected value
     */
    protected abstract Value poll(String key);

    /**
     * Exception thrown when a given command is not valud.
     */
    public static class InvalidCommandException extends Exception {

    }

    /**
     * Executed a command specific to the device
     * @param command a command which tells the device to perform a specific action
     * @throws InvalidCommandException when the command is not valid
     */
    public abstract void handleCommand(@NotNull Command command) throws InvalidCommandException;
}
