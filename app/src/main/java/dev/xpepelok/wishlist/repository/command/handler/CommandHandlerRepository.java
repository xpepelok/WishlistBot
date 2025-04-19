package dev.xpepelok.wishlist.repository.command.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface CommandHandlerRepository {
    void addCommandHandlerAnnotation(Method method, Annotation annotation);

    void addCommandHandler(Object command, Method method);

    void addCommandHandler(String name, Method method);

    <A extends Annotation> A getCommandHandlerAnnotation(Method method);

    Method getCommandHandler(Object command);

    Method getCommandHandler(String name);
}