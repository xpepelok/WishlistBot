package dev.xpepelok.wishlist.util;

import lombok.experimental.UtilityClass;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@UtilityClass
public final class ReflectionUtil {
    @SuppressWarnings("unchecked")
    public static <O> O createObjectInstance(Class<O> clazz, ApplicationContext applicationContext) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
        constructor.setAccessible(true);

        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] dependencies = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            dependencies[i] = applicationContext.getBean(parameterTypes[i]);
        }

        return (O) constructor.newInstance(dependencies);
    }
}