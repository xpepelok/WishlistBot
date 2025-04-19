package dev.xpepelok.wishlist.data.telegram;

import dev.xpepelok.wishlist.service.callback.CallBackService;
import dev.xpepelok.wishlist.service.command.CommandService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TelegramBot extends TelegramLongPollingBot {
    ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
    @Getter
    String botUsername = System.getenv("BOT_USERNAME");
    @Getter
    String botToken = System.getenv("BOT_TOKEN");
    CallBackService callBackService;
    CommandService commandService;

    @Override
    public void onUpdateReceived(Update update) {
        executorService.execute(() -> {
            if (update.hasMessage()) {
                var msg = update.getMessage();
                if (!msg.isCommand()) return;

                commandService.executeCommand(msg.getText(), msg.getFrom(), msg.getChatId());
            } else if (update.hasCallbackQuery()) {
                var query = update.getCallbackQuery();
                callBackService.executeCallback(query, query.getMessage().getChatId());
            }
        });
    }

    public void sendMessage(long chatID, String message) {
        SendMessage sm = new SendMessage();
        sm.setChatId(chatID);
        sm.setText(message);
        try {
            this.execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(String.format("Unable to send message %s to chat %s", message, chatID), e);
        }
    }

    public void sendMessage(long chatID, String message, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage sm = new SendMessage();
        sm.setChatId(chatID);
        sm.setText(message);
        sm.setReplyMarkup(inlineKeyboardMarkup);
        try {
            this.execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(String.format("Unable to send message %s to chat %s", message, chatID), e);
        }
    }
}