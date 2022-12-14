package uniud.joysf;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Class that runs an asynchronous loop which calls polling methods periodically
 */

public class PollingClock {

    private final ArrayList<Device> devices;
    private Frequency frequency;

    /**
     * Constructs a new polling clock that sends polling signals every frequency pulse
     * @param frequency the frequency at which the polling signals are sent
     */
    public PollingClock(@NotNull Frequency frequency) {
        devices = new ArrayList<>();
        this.frequency = frequency;

        // TODO: Implement update thread that calls the poll method on all devices at the time interval dictated by
        //  frequency
    }

    /**
     * Adds a device to the set of devices that will be alerted every frequency pulse
     * @param device the device to be added
     */
    public void addDevice(@NotNull Device device) {
        devices.add(device);
    }

    /**
     * Removes a device to the set of devices that will be alerted every frequency pulse
     * @param device the device to be removed
     */
    public void removeDevice(@NotNull Device device) {
        devices.remove(device);
    }
}
