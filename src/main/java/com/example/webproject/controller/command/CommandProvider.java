package com.example.webproject.controller.command;

import com.example.webproject.controller.command.impl.*;

import java.util.EnumMap;
import java.util.Optional;

import static com.example.webproject.controller.command.CommandType.*;

public class CommandProvider {
    private static final CommandProvider instance = new CommandProvider();
    private final EnumMap<CommandType, Command> commands = new EnumMap<>(CommandType.class);

    public static CommandProvider getInstance() {
        return instance;
    }

    private CommandProvider() {
        commands.put(TO_LOGIN_PAGE, new ToLoginPage());
        commands.put(TO_REGISTER_PAGE,new ToRegisterPage());
        commands.put(REGISTER,new RegisterCommand());
        commands.put(LOGIN,new LoginCommand());
        commands.put(LOGOUT,new LogOutCommand());
        commands.put(FIND_SERVICES, new FindService());
    }

    public Optional<Command> defineCommand(String commandName) {
        Optional<CommandType> commandType = typeForName(commandName);
        if (commandType.isPresent()) {
            Command command = commands.get(commandType.get());
            return Optional.of(command);
        }else {
            return Optional.empty();
        }
    }

    private Optional<CommandType> typeForName(String commandName) {
        try {
            CommandType commandType = valueOf(commandName.toUpperCase());
            return Optional.of(commandType);
        } catch (NullPointerException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
