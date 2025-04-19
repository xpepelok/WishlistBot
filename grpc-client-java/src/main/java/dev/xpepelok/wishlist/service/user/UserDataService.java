package dev.xpepelok.wishlist.service.user;

import dev.xpepelok.wishlist.userdata.model.UserData;
import dev.xpepelok.wishlist.userdata.model.WishData;

public interface UserDataService {
    UserData getUserData(long userID);

    void addWish(long userID, WishData wishData);

    void removeWish(byte[] wishID);
}