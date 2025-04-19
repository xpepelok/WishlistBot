package dev.xpepelok.wishlist.command.start;

import dev.xpepelok.wishlist.data.command.Command;
import dev.xpepelok.wishlist.data.command.CommandHandler;
import dev.xpepelok.wishlist.data.command.CommandResponse;
import dev.xpepelok.wishlist.data.telegram.TelegramBot;
import dev.xpepelok.wishlist.service.user.UserDataService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.User;

@Command(
        command = "start",
        usage = "<command>",
        description = "help with commands"
)
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StartCommand {
    UserDataService userDataService;
    TelegramBot telegramBot;

    @CommandHandler(adminOnly = false)
    public void handle(User sender, long chat) {
        telegramBot.sendMessage(chat, CommandResponse.START_MESSAGE.getRawMessage());
    }
}