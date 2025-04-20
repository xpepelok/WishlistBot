package dev.xpepelok.wishlist.configuration.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties("database")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DatabaseProperties {
    String host;
    int port;
    String database;
    String username;
    String password;
}
