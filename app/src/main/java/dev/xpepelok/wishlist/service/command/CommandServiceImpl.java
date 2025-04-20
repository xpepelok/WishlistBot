package dev.xpepelok.wishlist.service.command;

import dev.xpepelok.wishlist.configuration.properties.TelegramBotProperties;
import dev.xpepelok.wishlist.data.command.Command;
import dev.xpepelok.wishlist.data.command.CommandHandler;
import dev.xpepelok.wishlist.data.command.SubCommandHandler;
import dev.xpepelok.wishlist.repository.command.CommandRepository;
import dev.xpepelok.wishlist.repository.command.handler.CommandHandlerRepository;
import dev.xpepelok.wishlist.util.ArrayUtil;
import dev.xpepelok.wishlist.util.ReflectionUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.reflections.Reflections;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Log4j2
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommandServiceImpl implements CommandService {
    CommandHandlerRepository commandHandlerRepository;
    TelegramBotProperties telegramBotProperties;
    ApplicationContext applicationContext;
    CommandRepository commandRepository;
    Reflections reflections;

    @Override
    public void initializeCommands() {
        reflections.getTypesAnnotatedWith(Command.class).forEach(clazz -> {
            try {
                var command = ReflectionUtil.createObjectInstance(clazz, applicationContext);
                var ann = command.getClass().getAnnotation(Command.class);
                commandRepository.addCommand(ann.command(), command);
                for (String alias : ann.alias()) {
                    commandRepository.addCommand(alias, command);
                }
                initializeHandlers();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Unable to initialize commands", e);
            }
        });
    }

    private void initializeHandlers() {
        var commands = commandRepository.getCommands();
        for (var command : commands) {
            var methods = command.getClass().getMethods();
            boolean hasMainHandler = false;

            for (var method : methods) {
                if (initializeCommandHandler(command, method)) {
                    hasMainHandler = true;
                }

                initializeSubCommandHandlers(method);
            }
            if (!hasMainHandler) {
                throw new IllegalArgumentException(String.format("Command %s does not have handler", command.getClass().getName()));
            }
        }
    }

    private boolean initializeCommandHandler(Object command, Method method) {
        var handler = method.getAnnotation(CommandHandler.class);
        if (handler == null) return false;

        commandHandlerRepository.addCommandHandlerAnnotation(method, handler);
        commandHandlerRepository.addCommandHandler(command, method);
        return true;
    }

    private void initializeSubCommandHandlers(Method method) {
        var handler = method.getAnnotation(SubCommandHandler.class);
        if (handler == null) return;

        for (var name : handler.alias()) {
            commandHandlerRepository.addCommandHandler(name, method);
            commandHandlerRepository.addCommandHandlerAnnotation(method, handler);
        }
    }

    @Override
    public void executeCommand(String cmd, User sender, long chat) {
        var splitMsgArray = cmd.substring(1).split(" ");
        if (splitMsgArray.length == 0) return;

        var subCmd = splitMsgArray[0];
        var command = commandRepository.getCommand(subCmd);
        if (command == null) return;

        // Если /<cmd>
        if (splitMsgArray.length == 1) {
            handleMainCommand(command, sender, chat, ArrayUtil.skip(splitMsgArray, 1));
            return;
        }
        // Если sub command выполнится, то второй метод не вызовет, НО если sub command НЕ выполнится, то вызовет main command.
        if (!handleSubCommand(subCmd, command, sender, chat, ArrayUtil.skip(splitMsgArray, 1))) {
            handleMainCommand(command, sender, chat, ArrayUtil.skip(splitMsgArray, 1));
        }
    }

    private void handleMainCommand(Object command, User sender, long chatID, String... args) {
        var method = commandHandlerRepository.getCommandHandler(command);
        var handler = commandHandlerRepository.<CommandHandler>getCommandHandlerAnnotation(method);
        if (handler.adminOnly() && !telegramBotProperties.isAdmin(sender.getId())) return;

        invokeMethod(method, command, sender, chatID, args);
    }

    private boolean handleSubCommand(String subCmd, Object command, User sender, long chatID, String... args) {
        var method = commandHandlerRepository.getCommandHandler(subCmd);
        if (method == null) return false;

        var handler = commandHandlerRepository.<SubCommandHandler>getCommandHandlerAnnotation(method);
        if (handler == null) return false;

        if (handler.adminOnly() && !telegramBotProperties.isAdmin(sender.getId())) return true;


        invokeMethod(method, command, sender, chatID, ArrayUtil.skip(args, 1));
        return true;
    }

    private void invokeMethod(Method method, Object command, User sender, long chatID, String... args) {
        method.setAccessible(true);
        try {
            var count = method.getParameterCount();
            if (count == 2) {
                method.invoke(command, sender, chatID);
            } else if (count >= 3) {
                method.invoke(command, sender, chatID, args);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(String.format("Unable to invoke method to command %s", command.getClass().getName()), e);
        }
    }
}