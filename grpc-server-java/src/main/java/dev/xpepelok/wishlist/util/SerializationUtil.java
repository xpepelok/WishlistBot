package dev.xpepelok.wishlist.util;

import io.grpc.netty.shaded.io.netty.buffer.ByteBuf;
import io.grpc.netty.shaded.io.netty.buffer.Unpooled;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public final class SerializationUtil {
    public static UUID getUUID(byte[] bytes) {
        ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
        return new UUID(byteBuf.readLong(), byteBuf.readLong());
    }
}