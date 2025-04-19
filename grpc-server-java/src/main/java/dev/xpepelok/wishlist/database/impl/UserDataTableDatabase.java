package dev.xpepelok.wishlist.database.impl;

import dev.xpepelok.wishlist.database.CloseableTable;
import dev.xpepelok.wishlist.userdata.model.UserData;
import dev.xpepelok.wishlist.userdata.model.WishData;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface UserDataTableDatabase extends CloseableTable {
    CompletableFuture<Optional<UserData>> getUserData(long userID);

    void addWish(long userID, WishData wishData);

    void removeWish(byte[] wishID);
}