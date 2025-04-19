package dev.xpepelok.wishlist.repository.callback;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CallbackRepositoryImpl implements CallbackRepository {
    Map<String, Object> callbacks = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getCallback(String data) {
        return (C) callbacks.get(data);
    }

    @Override
    public <C> void addCallback(String callbackData, C callback) {
        callbacks.put(callbackData, callback);
    }
}