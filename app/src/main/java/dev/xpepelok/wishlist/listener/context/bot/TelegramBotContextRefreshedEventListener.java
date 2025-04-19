package dev.xpepelok.wishlist.listener.context.bot;

import dev.xpepelok.wishlist.data.telegram.TelegramBot;
import dev.xpepelok.wishlist.listener.context.ContextRefreshedEventListener;
import dev.xpepelok.wishlist.service.callback.CallBackService;
import dev.xpepelok.wishlist.service.command.CommandService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TelegramBotContextRefreshedEventListener implements ContextRefreshedEventListener {
    CallBackService callBackService;
    CommandService commandService;
    TelegramBot telegramBot;

    @Override
    public void initialize() {
        try {
            new TelegramBotsApi(DefaultBotSession.class).registerBot(telegramBot);
            callBackService.initializeCallbacks();
            commandService.initializeCommands();
        } catch (TelegramApiException e) {
            throw new IllegalStateException("Failed to register TelegramBot", e);
        }
    }
}