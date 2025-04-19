package dev.xpepelok.wishlist.callback.wish.remove;

import dev.xpepelok.wishlist.data.callback.Callback;
import dev.xpepelok.wishlist.data.callback.CallbackHandler;
import dev.xpepelok.wishlist.data.telegram.TelegramBot;
import dev.xpepelok.wishlist.service.user.UserDataService;
import dev.xpepelok.wishlist.util.SerializationUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.UUID;

@RequiredArgsConstructor
@Callback(callbackData = "remove_wish")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RemoveWishCallback {
    UserDataService userDataService;
    TelegramBot telegramBot;

    @CallbackHandler(adminOnly = false)
    public void handle(CallbackQuery callbackQuery, long chatId, String[] args) {
        var wishID = UUID.fromString(args[0]);
        userDataService.removeWish(SerializationUtil.getUUID(wishID));
        telegramBot.sendMessage(chatId, "Your wish was successfully removed!");
    }
}