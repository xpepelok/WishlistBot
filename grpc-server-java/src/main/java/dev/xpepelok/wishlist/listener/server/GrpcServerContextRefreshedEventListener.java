package dev.xpepelok.wishlist.listener.server;

import dev.xpepelok.wishlist.database.impl.UserDataTableDatabase;
import dev.xpepelok.wishlist.listener.ContextRefreshedEventListener;
import dev.xpepelok.wishlist.service.user.UserDataServiceImpl;
import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GrpcServerContextRefreshedEventListener implements ContextRefreshedEventListener {
    UserDataTableDatabase userDataTableDatabase;

    @Override
    public void initialize() {
        int port = Integer.parseInt(System.getenv().getOrDefault("GRPC_SERVER_PORT", "6979"));
        try {
            var server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
                    .addService(new UserDataServiceImpl(userDataTableDatabase))
                    .build();
            server.start();
            log.info("Server started on port {}", port);
            server.awaitTermination();
            if (server.isTerminated()) {
                userDataTableDatabase.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to start gRPC services", e);
        } catch (InterruptedException e) {
            throw new RuntimeException("Shutdown gRPC services...");
        }
    }
}