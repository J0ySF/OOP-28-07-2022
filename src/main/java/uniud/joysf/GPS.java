package uniud.joysf;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public class GPS extends Device {

    GPS() {
        super(null, null);
    }

    @Override
    protected Value poll(String key) {
        return null;
    }

    @Override
    public void handleCommand(@NotNull Command command) throws InvalidCommandException {

    }
}
