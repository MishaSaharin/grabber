package org.saccharine.grabber.config;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.Properties;

public class DataConfig {
    private final Properties config;

    public DataConfig(Properties config) {
        this.config = config;
    }

    public DataSource dataSource(Properties config) {
        HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName(config.getProperty("driver"));
        ds.setJdbcUrl(config.getProperty("url"));
        ds.setUsername(config.getProperty("user"));
        ds.setPassword(config.getProperty("password"));
        return ds;
    }
}