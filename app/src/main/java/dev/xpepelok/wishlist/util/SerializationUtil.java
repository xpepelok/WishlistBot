package dev.xpepelok.wishlist.util;

import io.grpc.netty.shaded.io.netty.buffer.ByteBuf;
import io.grpc.netty.shaded.io.netty.buffer.Unpooled;
import lombok.experimental.UtilityClass;

import java.nio.ByteBuffer;
import java.util.UUID;

@UtilityClass
public final class SerializationUtil {
    public static byte[] getUUID(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    public static UUID getUUID(byte[] array) {
        ByteBuf byteBuf = Unpooled.wrappedBuffer(array);
        return new UUID(byteBuf.readLong(), byteBuf.readLong());
    }
}