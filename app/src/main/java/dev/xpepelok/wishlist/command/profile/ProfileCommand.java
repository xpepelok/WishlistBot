package dev.xpepelok.wishlist.command.profile;

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
        command = "profile",
        alias = "myprofile",
        description = "get your profile"
)
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProfileCommand {
    UserDataService userDataService;
    TelegramBot telegramBot;

    @CommandHandler(adminOnly = false)
    public void handle(User sender, long chat) {
        var fromID = sender.getId();
        var userData = userDataService.getUserData(fromID);
        telegramBot.sendMessage(
                chat,
                CommandResponse.USER_DATA_MSG.format(
                        sender.getFirstName(),
                        fromID,
                        userData.getWishesList().size()
                )
        );
    }
}