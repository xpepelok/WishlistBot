package dev.xpepelok.wishlist.database.impl;

import com.google.protobuf.ByteString;
import com.zaxxer.hikari.HikariDataSource;
import dev.xpepelok.wishlist.database.AbstractTable;
import dev.xpepelok.wishlist.userdata.model.UserData;
import dev.xpepelok.wishlist.userdata.model.WishData;
import dev.xpepelok.wishlist.util.SerializationUtil;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
public class UserDataTableDatabaseImpl extends AbstractTable implements UserDataTableDatabase {
    public UserDataTableDatabaseImpl(HikariDataSource hikariDataSource) {
        super(hikariDataSource, System.getenv().getOrDefault("USER_DATA", "user_data"));
        forceQuery(
                String.format("CREATE TABLE IF NOT EXISTS `%s` (", this.getTableName()) +
                        "`wish_id` BINARY(16) PRIMARY KEY, " +
                        "`user_id` BIGINT, " +
                        "`name` VARCHAR(36), " +
                        "`wish` VARCHAR(100));"
        );
    }

    @Override
    public CompletableFuture<Optional<UserData>> getUserData(long userID) {
        return CompletableFuture.supplyAsync(() -> {
            String query = String.format("SELECT * FROM `%s` WHERE user_id = ?;", tableName);
            try (Connection c = hikariDataSource.getConnection(); PreparedStatement statement = c.prepareStatement(query)) {
                statement.setLong(1, userID);
                ResultSet rs = statement.executeQuery();
                List<WishData> wishes = new ArrayList<>();
                while (rs.next()) {
                    wishes.add(WishData.newBuilder()
                            .setId(ByteString.copyFrom(rs.getBytes("wish_id")))
                            .setName(rs.getString("name"))
                            .setWish(rs.getString("wish"))
                            .build()
                    );
                }
                return Optional.of(UserData.newBuilder().setUserID(userID).addAllWishes(wishes).build());
            } catch (SQLException e) {
                throw new IllegalArgumentException(String.format("Unable to get wishes for user %s", userID), e);
            }
        });
    }

    @Override
    public void addWish(long userID, WishData wishData) {
        String query = String.format("INSERT INTO `%s` (user_id, wish_id, name, wish) VALUES (?, ?, ?, ?);", tableName);
        try (Connection c = hikariDataSource.getConnection(); PreparedStatement statement = c.prepareStatement(query)) {
            statement.setLong(1, userID);
            statement.setBytes(2, wishData.getId().toByteArray());
            statement.setString(3, wishData.getName());
            statement.setString(4, wishData.getWish());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException(String.format("Unable to save wish from user %s", userID), e);
        }
    }

    @Override
    public void removeWish(byte[] wishID) {
        String query = String.format("DELETE FROM `%s` WHERE wish_id = ?;", tableName);
        try (Connection c = hikariDataSource.getConnection(); PreparedStatement statement = c.prepareStatement(query)) {
            statement.setBytes(1, wishID);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException(String.format("Unable to remove wish with id %s", SerializationUtil.getUUID(wishID)), e);
        }
    }

    @Override
    public void close() {
        super.close();
    }
}