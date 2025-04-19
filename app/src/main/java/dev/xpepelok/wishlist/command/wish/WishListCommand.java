package dev.xpepelok.wishlist.command.wish;

import dev.xpepelok.wishlist.data.command.Command;
import dev.xpepelok.wishlist.data.command.CommandHandler;
import dev.xpepelok.wishlist.data.telegram.TelegramBot;
import dev.xpepelok.wishlist.service.user.UserDataService;
import dev.xpepelok.wishlist.service.wish.WishListService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.User;

@Command(
        command = "wishes",
        alias = {"wlist", "list", "wishlist", "wishl"},
        description = "get your wish list"
)
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class WishListCommand {
    WishListService wishListService;

    @CommandHandler(
            adminOnly = false
    )
    public void handle(User sender, long chat, String[] args) {
        if (args.length == 0) {
            wishListService.printWishes(sender.getId(), chat, 0);
            return;
        }
        wishListService.printWishes(sender.getId(), chat, Integer.parseInt(args[0]));
    }
}