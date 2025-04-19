package dev.xpepelok.wishlist.callback.wish.next;

import dev.xpepelok.wishlist.data.callback.Callback;
import dev.xpepelok.wishlist.data.callback.CallbackHandler;
import dev.xpepelok.wishlist.service.wish.WishListService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@RequiredArgsConstructor
@Callback(callbackData = "next_wish")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NextWishCallback {
    WishListService wishListService;

    @CallbackHandler(adminOnly = false)
    public void handle(CallbackQuery callbackQuery, long chatId, String[] args) {
        wishListService.printWishes(callbackQuery.getFrom().getId(), chatId, Integer.parseInt(args[0]) + 1);
    }
}