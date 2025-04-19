package dev.xpepelok.wishlist.service.callback;

import dev.xpepelok.wishlist.configuration.properties.TelegramBotProperties;
import dev.xpepelok.wishlist.data.callback.Callback;
import dev.xpepelok.wishlist.data.callback.CallbackHandler;
import dev.xpepelok.wishlist.repository.callback.CallbackRepository;
import dev.xpepelok.wishlist.repository.callback.handler.CallbackHandlerRepository;
import dev.xpepelok.wishlist.util.ArrayUtil;
import dev.xpepelok.wishlist.util.ReflectionUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.reflections.Reflections;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CallBackServiceImpl implements CallBackService {
    CallbackHandlerRepository callbackHandlerRepository;
    TelegramBotProperties telegramBotProperties;
    ApplicationContext applicationContext;
    CallbackRepository callbackRepository;
    Reflections reflections;

    @Override
    public void initializeCallbacks() {
        reflections.getTypesAnnotatedWith(Callback.class).forEach(clazz -> {
            try {
                var callback = ReflectionUtil.createObjectInstance(clazz, applicationContext);
                var ann = callback.getClass().getAnnotation(Callback.class);
                callbackRepository.addCallback(ann.callbackData(), callback);

                var handlerMethod = getCallbackHandlerMethod(callback);
                callbackHandlerRepository.addCallback(ann.callbackData(), handlerMethod.getAnnotation(CallbackHandler.class), handlerMethod);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Unable to initialize callbacks", e);
            }
        });
    }

    private Method getCallbackHandlerMethod(Object callback) {
        for (var method : callback.getClass().getMethods()) {
            var handler = method.getAnnotation(CallbackHandler.class);
            if (handler == null) continue;

            return method;
        }
        throw new IllegalArgumentException(String.format("Callback %s does not have handler", callback.getClass().getName()));
    }

    @Override
    public void executeCallback(CallbackQuery callbackQuery, long chat) {
        var callbackData = callbackQuery.getData();
        if (callbackData.isEmpty()) return;
        var splitData = callbackData.split(":");
        var target = splitData[0];
        var callback = callbackRepository.getCallback(target);
        if (callback == null) return;

        var handler = callbackHandlerRepository.getCallback(target);
        if (handler == null) return;

        if (handler.first().adminOnly() && !telegramBotProperties.isAdmin(callbackQuery.getFrom().getId())) {
            return;
        }
        invokeMethod(handler.second(), callback, callbackQuery, chat, ArrayUtil.skip(splitData, 1));
    }

    private void invokeMethod(Method method, Object callback, CallbackQuery callbackQuery, long chatID, String[] args) {
        method.setAccessible(true);
        try {
            var count = method.getParameterCount();
            if (count == 2) {
                method.invoke(callback, callbackQuery, chatID);
            } else if (count >= 3) {
                method.invoke(callback, callbackQuery, chatID, args);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(String.format("Unable to invoke method to callback %s", callback.getClass().getName()), e);
        }
    }
}