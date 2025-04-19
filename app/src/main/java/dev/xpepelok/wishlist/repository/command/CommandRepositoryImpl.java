package dev.xpepelok.wishlist.repository.command;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommandRepositoryImpl implements CommandRepository {
    Map<String, Object> knownCommands = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getCommand(String name) {
        return (C) knownCommands.get(name);
    }

    @Override
    public <C> void addCommand(String name, C command) {
        knownCommands.put(name, command);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> Collection<C> getCommands() {
        return (Collection<C>) knownCommands.values();
    }
}