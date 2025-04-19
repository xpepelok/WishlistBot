package dev.xpepelok.wishlist.configuration;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.NameResolverRegistry;
import io.grpc.internal.DnsNameResolverProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WishlistServiceConfiguration {
    @Bean
    public ManagedChannel managedChannel() {
        NameResolverRegistry.getDefaultRegistry().register(new DnsNameResolverProvider());
        var environments = System.getenv();
        return ManagedChannelBuilder.forTarget(
                String.format(
                        "%s:%s",
                        environments.getOrDefault("GRPC_SERVER_HOST", "localhost"),
                        environments.getOrDefault("GRPC_SERVER_PORT", "6035")
                )
        ).usePlaintext().build();
    }
}