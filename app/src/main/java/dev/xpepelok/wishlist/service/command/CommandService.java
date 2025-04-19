package dev.xpepelok.wishlist.service.command;

import org.telegram.telegrambots.meta.api.objects.User;

public interface CommandService {
    void initializeCommands();

    void executeCommand(String cmd, User sender, long chat);
}