package dev.xpepelok.wishlist.repository.callback;

public interface CallbackRepository {
    <C> C getCallback(String data);

    <C> void addCallback(String callbackData, C callback);
}