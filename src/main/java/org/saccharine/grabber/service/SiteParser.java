package org.saccharine.grabber.service;

import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.saccharine.grabber.db.Post;
import org.saccharine.grabber.utils.AppDateTimeParser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SiteParser implements Parse<Post> {
    public static final Logger LOGGER = Logger.getLogger(SiteParser.class);
    private final AppDateTimeParser appDateTimeParser;
    private final Properties config;

    public SiteParser(AppDateTimeParser appDateTimeParser, Properties config) {
        this.appDateTimeParser = appDateTimeParser;
        this.config = config;
    }

    private String retrieveDescription(String link) throws IOException {
        String description = config.getProperty("description");
        Connection connection = Jsoup.connect(link);
        Document document = connection.get();
        Elements select = document.select(description);
        return select.text();
    }

    @Override
    public List<Post> list(String link) throws IOException {
        String prefix = config.getProperty("prefix");
        String suffix = config.getProperty("suffix");
        String inner = config.getProperty("inner");
        int pageNumber = Integer.parseInt(config.getProperty("pageNumber"));
        List<Post> posts = new ArrayList<>();
        try {
            for (int i = 1; i <= pageNumber; i++) {
                String fullLink = String.format("%s%s%d%s", link, prefix, i, suffix);
                Connection connection = Jsoup.connect(fullLink);
                Document document = connection.get();
                Elements rows = document.select(inner);
                for (Element row : rows) {
                    Post post = createPost(row);
                    posts.add(post);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("Size of posts: " + posts.size());
        return posts;
    }

    public Post createPost(Element row) throws IOException {
        String sourceLink = config.getProperty("sourceLink");
        String propertyDate = config.getProperty("date");
        String title = config.getProperty("title");
        Element dateElement = row.select(propertyDate).first().child(0);
        Element titleElement = row.select(title).first();
        Element linkElement = titleElement.child(0);
        String vacancyName = titleElement.text();
        String link = String.format("%s%s", sourceLink, linkElement.attr("href"));
        String date = dateElement.attr("datetime");
        String description = retrieveDescription(link);
        LocalDateTime localDateTime = appDateTimeParser.parse(date);
        LOGGER.info("Added new entity " + localDateTime);
        return new Post(vacancyName, link, description, localDateTime);
    }
}