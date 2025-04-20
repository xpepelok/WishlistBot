package dev.xpepelok.wishlist.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.xpepelok.wishlist.configuration.properties.DatabaseProperties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(DatabaseProperties.class)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DatabaseConfiguration {
    DatabaseProperties databaseProperties;

    @Bean
    public HikariDataSource hikariDataSource() {
        var hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(
                String.format(
                        "jdbc:mysql://%s:%d/%s",
                        databaseProperties.getHost(),
                        databaseProperties.getPort(),
                        databaseProperties.getDatabase()
                )
        );
        hikariConfig.setUsername(databaseProperties.getUsername());
        hikariConfig.setPassword(databaseProperties.getPassword());
        hikariConfig.addDataSourceProperty("cachePrepStmts", true);
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", 250);
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        return new HikariDataSource(hikariConfig);
    }
}
