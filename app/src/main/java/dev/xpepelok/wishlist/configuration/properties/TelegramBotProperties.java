package dev.xpepelok.wishlist.configuration.properties;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@RequiredArgsConstructor
@ConfigurationProperties("bot")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TelegramBotProperties {
    List<Long> admins;

    public boolean isAdmin(long id) {
        return this.admins.contains(id);
    }
}