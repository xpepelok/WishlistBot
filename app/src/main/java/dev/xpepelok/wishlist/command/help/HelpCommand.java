package dev.xpepelok.wishlist.command.help;

import dev.xpepelok.wishlist.configuration.properties.TelegramBotProperties;
import dev.xpepelok.wishlist.data.command.Command;
import dev.xpepelok.wishlist.data.command.CommandHandler;
import dev.xpepelok.wishlist.data.command.SubCommandHandler;
import dev.xpepelok.wishlist.data.telegram.TelegramBot;
import dev.xpepelok.wishlist.repository.command.CommandRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.*;

@Command(
        command = "help",
        usage = "<command>",
        description = "help with commands"
)
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class HelpCommand {
    TelegramBotProperties telegramBotProperties;
    CommandRepository commandRepository;
    TelegramBot telegramBot;

    @CommandHandler(adminOnly = false)
    public void handle(User sender, long chat, String[] args) {
        long senderId = sender.getId();
        if (args.length == 0) {
            telegramBot.sendMessage(chat, String.join("\n", getKnownCommands(senderId)));
            return;
        }
        var cmd = commandRepository.getCommand(args[0]);
        if (cmd == null) {
            telegramBot.sendMessage(chat, String.format("Undefined command: %s", args[0]));
            return;
        }
        telegramBot.sendMessage(chat, String.join("\n", getKnownCommands(senderId, cmd)));
    }

    private List<String> getKnownCommands(long sender) {
        var commands = commandRepository.getCommands();
        var uniqueCommands = new LinkedHashSet<String>();
        uniqueCommands.add("Existing commands: ");

        for (var command : commands) {
            var annotation = command.getClass().getAnnotation(Command.class);
            if (!telegramBotProperties.isAdmin(sender) || annotation.hide()) {
                continue;
            }
            var subCommands = getCountOfSubCommands(command);
            String commandString = String.format(
                    "⊢ /%s%s— %s %s",
                    annotation.command(),
                    annotation.usage().isEmpty() ? " " : String.format(" %s", annotation.usage()),
                    annotation.description(),
                    subCommands == 0 ? "" : String.format("| subcommands amount: %d", subCommands)
            );
            uniqueCommands.add(commandString);
        }
        return new ArrayList<>(uniqueCommands);
    }

    private List<String> getKnownCommands(long sender, Object command) {
        var cmdAnnotation = command.getClass().getAnnotation(Command.class);
        var knownCommands = new LinkedHashSet<String>();
        knownCommands.add(String.format("Subcommands /%s:", cmdAnnotation.command()));

        var subs = getSubCommands(command);
        for (var handler : subs) {
            if (!telegramBotProperties.isAdmin(sender) || handler.hide()) {
                continue;
            }
            knownCommands.add(
                    String.format(
                            "⊢ %s%s— %s",
                            handler.alias()[0],
                            handler.usage().isEmpty() ? " " : String.format(" %s ", handler.usage()),
                            handler.description()
                    )
            );
        }
        return knownCommands.size() == 1 ? new ArrayList<>(List.of("Subcommands doesn't founded!")) : new ArrayList<>(knownCommands);
    }

    private Set<SubCommandHandler> getSubCommands(Object command) {
        var subCommandHandlers = new HashSet<SubCommandHandler>();
        var methods = command.getClass().getMethods();
        for (var method : methods) {
            var annotation = method.getAnnotation(SubCommandHandler.class);
            if (annotation == null) {
                continue;
            }
            subCommandHandlers.add(annotation);
        }
        return subCommandHandlers;
    }

    private int getCountOfSubCommands(Object command) {
        var methods = command.getClass().getMethods();
        int count = 0;
        for (var method : methods) {
            var annotation = method.getAnnotation(SubCommandHandler.class);
            if (annotation == null) {
                continue;
            }
            count++;
        }
        return count;
    }
}