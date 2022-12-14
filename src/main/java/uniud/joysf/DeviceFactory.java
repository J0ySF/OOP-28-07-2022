package uniud.joysf;

/**
 * Interface used to delegate the device creation.
 */

public interface DeviceFactory {
    /**
     * @return a new device
     */
    public Device createDevice();
}
