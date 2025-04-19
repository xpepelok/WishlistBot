package dev.xpepelok.wishlist.command.wish;

import com.google.protobuf.ByteString;
import dev.xpepelok.wishlist.data.command.Command;
import dev.xpepelok.wishlist.data.command.CommandHandler;
import dev.xpepelok.wishlist.data.telegram.TelegramBot;
import dev.xpepelok.wishlist.service.user.UserDataService;
import dev.xpepelok.wishlist.userdata.model.WishData;
import dev.xpepelok.wishlist.util.SerializationUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Arrays;
import java.util.UUID;

@Command(
        command = "add",
        alias = {"wish-add", "wishadd", "wd", "wa"},
        description = "save your wish"
)
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class WishAddCommand {
    UserDataService userDataService;
    TelegramBot telegramBot;

    @CommandHandler(adminOnly = false)
    public void handle(User sender, long chat, String[] args) {
        try {
            var reference = args[0];
            if (reference.length() > 100) {
                telegramBot.sendMessage(chat, "Your reference is too large! Max symbols must be not less than 100!");
                return;
            }
            var name = buildNameViaArgs(Arrays.copyOfRange(args, 1, args.length));
            if (name.length() > 36) {
                telegramBot.sendMessage(chat, "Name cannot be longer than 36 characters!");
                return;
            }

            addWish(sender.getId(), chat, args[0], name);
        } catch (Exception e) {
            telegramBot.sendMessage(chat, "Check your arguments!");
        }
    }

    private String buildNameViaArgs(String... args) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            builder.append(args[i]);
            if (i < args.length - 1) {
                builder.append(" ");
            }
        }
        return builder.toString();
    }

    private void addWish(long userID, long chatID, String reference, String name) {
        val wish = WishData.newBuilder()
                .setName(name)
                .setId(ByteString.copyFrom(SerializationUtil.getUUID(UUID.randomUUID())))
                .setWish(reference).build();
        userDataService.addWish(userID, wish);
        telegramBot.sendMessage(chatID, String.format("You have successfully added a wish with the name »%s» and the link «%s» to your wishlist!", name, reference));
    }
}