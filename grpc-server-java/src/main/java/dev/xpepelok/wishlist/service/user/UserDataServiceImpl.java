package dev.xpepelok.wishlist.service.user;

import dev.xpepelok.wishlist.database.impl.UserDataTableDatabase;
import dev.xpepelok.wishlist.userdata.grpc.*;
import dev.xpepelok.wishlist.userdata.model.UserData;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDataServiceImpl extends UserDataServiceGrpc.UserDataServiceImplBase {
    UserDataTableDatabase userDataTableDatabase;

    @Override
    public void createUserData(CreateUserDataRequest request,
                               StreamObserver<CreateUserDataResponse> responseStreamObserver) {
        var userData = UserData.newBuilder()
                .setUserID(request.getUserID())
                .build();

        var response = CreateUserDataResponse.newBuilder().setData(userData).build();
        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void getUserData(GetUserDataRequest request,
                            StreamObserver<GetUserDataResponse> responseStreamObserver) {
        userDataTableDatabase.getUserData(request.getUserID()).thenAccept(data -> {
            try {
                var response = GetUserDataResponse
                        .newBuilder()
                        .setData(data.orElseThrow(() -> new RuntimeException("Data doesn't founded in database")))
                        .build();

                responseStreamObserver.onNext(response);

                responseStreamObserver.onCompleted();
            } catch (Exception e) {
                responseStreamObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).withCause(e.getCause()).asException());
            }
        });
    }

    @Override
    public void addWish(AddWishRequest request,
                        StreamObserver<AddWishResponse> responseStreamObserver) {
        userDataTableDatabase.addWish(request.getUserID(), request.getData());
        var response = AddWishResponse.newBuilder().build();
        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void removeWish(RemoveWishRequest request,
                           StreamObserver<RemoveWishResponse> responseStreamObserver) {
        userDataTableDatabase.removeWish(request.getWishID().toByteArray());
        var response = RemoveWishResponse.newBuilder().build();
        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }
}