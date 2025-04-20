package dev.xpepelok.wishlist.command.profile;

import dev.xpepelok.wishlist.data.command.Command;
import dev.xpepelok.wishlist.data.command.CommandHandler;
import dev.xpepelok.wishlist.data.telegram.TelegramBot;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.User;

@Command(
        command = "id",
        alias = "myid",
        description = "get your and chat ID"
)
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class IdCommand {
    TelegramBot telegramBot;

    @CommandHandler
    public void handle(User sender, long chat) {
        telegramBot.sendMessage(chat, String.format("User ID: %s, chat ID: %s", sender.getId(), chat));
    }
}