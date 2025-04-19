package dev.xpepelok.wishlist.repository.command;

import java.util.Collection;

public interface CommandRepository {
    <C> C getCommand(String name);

    <C> void addCommand(String name, C command);

    <C> Collection<C> getCommands();
}