package org.saccharine.grabber.db;

import org.apache.log4j.Logger;
import org.saccharine.grabber.config.DataConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class AppDataBaseService implements AppDataBase<Post> {
    public static final Logger LOGGER = Logger.getLogger(AppDataBaseService.class);
    private final Connection connection;

    public AppDataBaseService(Properties config) {
        try {
            Class.forName(config.getProperty("driver-class-name"));
            connection = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password"));
            LOGGER.info("Connected to database");
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("""
                             INSERT INTO postgres.post.post (id, title, link, description, created)
                             VALUES (?, ?, ?, ?, ?)
                             ON CONFLICT(link) DO NOTHING""")) {
            preparedStatement.setString(1, post.getId());
            preparedStatement.setString(2, post.getTitle());
            preparedStatement.setString(3, post.getLink());
            preparedStatement.setString(4, post.getDescription());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(post.getCreated()));
            LOGGER.info("post " + post.hashCode() + " with id " + post.getId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> result = new ArrayList<>();
        try (PreparedStatement pStatement = connection.prepareStatement("""
                SELECT * FROM postgres.post.post""");
             ResultSet resultSet = pStatement.executeQuery()) {
            while (resultSet.next()) {
                result.add(createPost(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        LOGGER.info("Get list size = " + result.size());
        return result;
    }

    @Override
    public Post findById(UUID id) {
        Post result = null;
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("""
                             SELECT * FROM postgres.post.post WHERE id = ?""")) {
            preparedStatement.setString(1, id.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = createPost(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        LOGGER.info("Get post with id = " + result.getId());
        return result;
    }

    private Post createPost(ResultSet resultSet) {
        try {
            return new Post(
                    resultSet.getString("title"),
                    resultSet.getString("link"),
                    resultSet.getString("description"),
                    resultSet.getTimestamp("created").toLocalDateTime());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
            LOGGER.info("Connection closed");
        }
    }
}