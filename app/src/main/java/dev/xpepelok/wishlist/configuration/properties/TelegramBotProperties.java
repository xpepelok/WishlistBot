package dev.xpepelok.wishlist.configuration.properties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@RequiredArgsConstructor
@ConfigurationProperties("bot")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramBotProperties {
    List<Long> admins;

    public boolean isAdmin(long id) {
        return this.admins.contains(id);
    }
}