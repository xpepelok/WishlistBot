package dev.xpepelok.wishlist.repository.command.handler;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommandHandlerRepositoryImpl implements CommandHandlerRepository {
    Map<Method, Annotation> annotations = new HashMap<>();
    Map<Object, Method> mainHandlers = new HashMap<>();
    Map<String, Method> subHandlers = new HashMap<>();

    @Override
    public void addCommandHandlerAnnotation(Method method, Annotation annotation) {
        this.annotations.put(method, annotation);
    }

    @Override
    public void addCommandHandler(Object command, Method method) {
        this.mainHandlers.put(command, method);
    }

    @Override
    public void addCommandHandler(String name, Method method) {
        this.subHandlers.put(name, method);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A extends Annotation> A getCommandHandlerAnnotation(Method method) {
        return (A) this.annotations.get(method);
    }

    @Override
    public Method getCommandHandler(Object command) {
        return this.mainHandlers.get(command);
    }

    @Override
    public Method getCommandHandler(String name) {
        return this.subHandlers.get(name);
    }
}