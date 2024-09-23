package hallpointer.address.logic.commands;

import hallpointer.address.model.Model;

public class RemarkCommand extends Command {
    public static final String COMMAND_WORD = "remark";

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult("Hello from remark");
    }
}
