package dev.xpepelok.wishlist.configuration;

import com.mysql.cj.jdbc.Driver;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfiguration {
    @Bean
    public HikariDataSource hikariDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(
                String.format(
                        "jdbc:mysql://%s:%d/%s",
                        System.getenv().getOrDefault("DATABASE_HOST", "localhost"),
                        Integer.parseInt(System.getenv().getOrDefault("DATABASE_PORT", "3306")),
                        System.getenv().getOrDefault("DATABASE", "example")
                )
        );
        hikariConfig.setUsername(System.getenv().getOrDefault("DATABASE_USER", "root"));
        hikariConfig.setPassword(System.getenv().getOrDefault("DATABASE_PASSWORD", "root"));
        hikariConfig.addDataSourceProperty("cachePrepStmts", true);
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", 250);
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        return new HikariDataSource(hikariConfig);
    }
}