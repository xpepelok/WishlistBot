package dev.xpepelok.wishlist.repository.callback.handler;

import dev.xpepelok.wishlist.data.callback.CallbackHandler;
import dev.xpepelok.wishlist.util.Pair;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CallbackHandlerRepositoryImpl implements CallbackHandlerRepository {
    Map<String, Pair<CallbackHandler, Method>> handlers = new HashMap<>();

    @Override
    public void addCallback(String name, CallbackHandler annotation, Method method) {
        this.handlers.put(name, new Pair<>(annotation, method));
    }

    @Override
    public Pair<CallbackHandler, Method> getCallback(String name) {
        return this.handlers.get(name);
    }
}