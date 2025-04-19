package dev.xpepelok.wishlist.configuration;

import dev.xpepelok.wishlist.TelegramApplication;
import org.reflections.Reflections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReflectionsConfiguration {
    @Bean
    public Reflections reflections() {
        return new Reflections(TelegramApplication.class.getPackage().getName());
    }
}