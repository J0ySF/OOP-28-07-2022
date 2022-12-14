package uniud.joysf;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Control Panel instance handles all connected devices and updates the enabled devices' values periodically.
 */

public class ControlPanel {

    private final PollingClock pollingClock;
    private final List<Device> devices;
    private final HashMap<DeviceToken, Device> tokens;

    /**
     * Constructs a new control panel, which has no connected devices.
     */
    ControlPanel(@NotNull Frequency pollingFrequency) {
        pollingClock = new PollingClock(pollingFrequency);
        devices = new ArrayList<>();
        tokens = new HashMap<>();
    }

    /**
     * Exception thrown when a device is not found
     */
    public static class DeviceNotFoundException extends Exception {

    }

    /**
     * Get the real device instance from a token
     * @param deviceToken the token which represents the device to external users
     * @return the real device instance
     * @throws DeviceNotFoundException when the token does not pair with any device instance
     */
    private Device matchDevice(@NotNull DeviceToken deviceToken) throws DeviceNotFoundException{
        Device device = tokens.get(deviceToken);
        if(device == null)
            throw new DeviceNotFoundException();
        return device;
    }

    /**
     * Creates a new device using the deviceFactory and adds it to the connected devices.
     * @param deviceFactory the device factory used to instantiate the new device
     * @return a device handle which can be used to query the device via some ControlPanel methods
     */
    public DeviceToken addDevice(@NotNull DeviceFactory deviceFactory) {
        Device device = deviceFactory.createDevice();
        devices.add(device);
        pollingClock.addDevice(device);
        DeviceToken token = new DeviceToken();
        tokens.put(token, device);
        return token;
    }

    /**
     * Removes a device from the control panel
     * @param deviceToken the token which represents the device
     * @throws DeviceNotFoundException when the device token does not represent a valid device for this control panel
     * instance
     */
    public void removeDevice(@NotNull DeviceToken deviceToken) throws DeviceNotFoundException {
        Device device = matchDevice(deviceToken);
        pollingClock.removeDevice(device);
        devices.remove(device);
        tokens.remove(deviceToken, device);
    }

    /**
     * Executes a command on a device
     * @param deviceToken the token which represents the device
     * @param command the command to be executed on the device
     * @throws DeviceNotFoundException when the device token does not represent a valid device for this control panel
     * instance
     * @throws Device.InvalidCommandException when the command is not valid for this device
     */
    public void sendCommand(@NotNull DeviceToken deviceToken, @NotNull Command command) throws DeviceNotFoundException, Device.InvalidCommandException {
        matchDevice(deviceToken).handleCommand(command);
    }

    /**
     * Extract a current value from the device
     * @param deviceToken the token which represents the device
     * @param key name that represents the value to be extracted
     * @return the extracted value associated with the given key
     * @throws DeviceNotFoundException when the device token does not represent a valid device for this control panel
     * instance
     * @throws Device.VariableDoesNotExistException when the key does not represent a valid value on this device
     */
    public Value extractValue(@NotNull DeviceToken deviceToken, @NotNull String key) throws DeviceNotFoundException, Device.VariableDoesNotExistException {
        return matchDevice(deviceToken).extractInstantValue(key);
    }

    /**
     * Extract an average value from the device
     * @param deviceToken the token which represents the device
     * @param key name that represents the value to be extracted
     * @param duration the time span in which the average is computed
     * @return the extracted value associated with the given key
     * @throws DeviceNotFoundException when the device token does not represent a valid device for this control panel
     * instance
     * @throws Device.VariableDoesNotExistException when the key does not represent a valid value on this device
     */
    public Value movingAverage(@NotNull DeviceToken deviceToken, @NotNull String key, @NotNull Duration duration) throws DeviceNotFoundException, Device.VariableDoesNotExistException {
        return matchDevice(deviceToken).extractAverageValue(key, duration);
    }
}
