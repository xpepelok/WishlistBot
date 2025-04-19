package dev.xpepelok.wishlist.service.user;

import com.google.protobuf.ByteString;
import dev.xpepelok.wishlist.userdata.grpc.*;
import dev.xpepelok.wishlist.userdata.model.UserData;
import dev.xpepelok.wishlist.userdata.model.WishData;
import dev.xpepelok.wishlist.util.SerializationUtil;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDataServiceImpl implements UserDataService {
    UserDataServiceGrpc.UserDataServiceBlockingStub stub;
    ExecutorService executorService = Executors.newCachedThreadPool();

    public UserDataServiceImpl(ManagedChannel managedChannel) {
        this.stub = UserDataServiceGrpc.newBlockingStub(managedChannel);
    }

    @Override
    public UserData getUserData(long userID) {
        try {
            return getUserDataViaDatabase(userID);
        } catch (StatusRuntimeException e) {
            return createUserData(userID);
        }
    }

    @Override
    public void addWish(long userID, WishData wishData) {
        executorService.submit(() -> {
            var request = AddWishRequest
                    .newBuilder()
                    .setUserID(userID)
                    .setData(wishData)
                    .build();

            try {
                stub.addWish(request);
            } catch (StatusRuntimeException e) {
                throw new RuntimeException(String.format("Unable to save wish from user %s", userID), e);
            }
        });
    }

    @Override
    public void removeWish(byte[] wishID) {
        executorService.submit(() -> {
            var request = RemoveWishRequest
                    .newBuilder()
                    .setWishID(ByteString.copyFrom(wishID))
                    .build();

            try {
                stub.removeWish(request);
            } catch (StatusRuntimeException e) {
                throw new RuntimeException(String.format("Unable to remove wish with id %s", SerializationUtil.getUUID(wishID)), e);
            }
        });
    }

    private UserData getUserDataViaDatabase(long userID) {
        var request = GetUserDataRequest
                .newBuilder()
                .setUserID(userID)
                .build();

        return stub.getUserData(request).getData();
    }

    private UserData createUserData(long userID) {
        var createDataRequest = CreateUserDataRequest
                .newBuilder()
                .setUserID(userID)
                .build();

        return stub.createUserData(createDataRequest).getData();
    }
}