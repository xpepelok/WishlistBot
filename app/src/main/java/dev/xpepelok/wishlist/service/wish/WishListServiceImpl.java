package dev.xpepelok.wishlist.service.wish;

import dev.xpepelok.wishlist.data.command.CommandResponse;
import dev.xpepelok.wishlist.data.telegram.TelegramBot;
import dev.xpepelok.wishlist.data.telegram.button.Button;
import dev.xpepelok.wishlist.service.user.UserDataService;
import dev.xpepelok.wishlist.util.KeyboardUtil;
import dev.xpepelok.wishlist.util.SerializationUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WishListServiceImpl implements WishListService {
    UserDataService userDataService;
    TelegramBot telegramBot;

    public void printWishes(long userID, long chatID, int index) {
        var wishes = userDataService.getUserData(userID).getWishesList();
        if (wishes.isEmpty()) {
            telegramBot.sendMessage(chatID, "Your list is empty!");
            return;
        }
        var wishesSize = wishes.size();
        if (wishesSize < index) {
            telegramBot.sendMessage(chatID, String.format("Index cannot be less than %d!", wishesSize));
            return;
        }
        var wish = wishes.get(index);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(KeyboardUtil.formCallBackButtons(
                new Button("❌ Remove", String.format("%s:%s", "remove_wish", SerializationUtil.getUUID(wish.getId().toByteArray()))),
                wishes.size() > (index + 1) ? new Button(wishes.get(index + 1).getName(), String.format("%s:%d", "next_wish", index)) : null
        ));
        telegramBot.sendMessage(chatID, CommandResponse.WISH_FORM.format(wish.getName(), wish.getWish()), inlineKeyboardMarkup);
    }
}