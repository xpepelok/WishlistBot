package dev.xpepelok.wishlist.repository.callback.handler;

import dev.xpepelok.wishlist.data.callback.CallbackHandler;
import dev.xpepelok.wishlist.util.Pair;

import java.lang.reflect.Method;

public interface CallbackHandlerRepository {
    void addCallback(String name, CallbackHandler annotation, Method method);

    Pair<CallbackHandler, Method> getCallback(String name);
}