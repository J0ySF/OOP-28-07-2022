package uniud.joysf;

/**
 * Class that implements commands using pre-determined strings
 */

public class SimpleCommand implements Command {
    private String string;

    public SimpleCommand(String commandString) {
        string = commandString;
    }

    @Override
    public String asString() {
        return string;
    }
}
