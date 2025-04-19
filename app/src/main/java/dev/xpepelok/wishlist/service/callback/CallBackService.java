package dev.xpepelok.wishlist.service.callback;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface CallBackService {
    void initializeCallbacks();

    void executeCallback(CallbackQuery callbackQuery, long chat);
}