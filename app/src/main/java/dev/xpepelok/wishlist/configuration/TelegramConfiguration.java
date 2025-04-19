package dev.xpepelok.wishlist.configuration;

import dev.xpepelok.wishlist.configuration.properties.TelegramBotProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TelegramBotProperties.class)
public class TelegramConfiguration {
}